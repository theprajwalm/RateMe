package com.rateme.rateme.controller;

import com.rateme.rateme.DBAccess.dbaccessuser;
import com.rateme.rateme.Security.PasswordTools;
import com.rateme.rateme.Security.SecurityManager;
import com.rateme.rateme.dto.LogInDTO;
import com.rateme.rateme.dto.UserDTI;
import com.rateme.rateme.dto.UserDTO;
import com.rateme.rateme.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/user")

public class UserController {
    @Autowired
    private final dbaccessuser accessuser;
    private final SecurityManager securityManager;

    public UserController(dbaccessuser accessuser,SecurityManager securityManager){
        this.accessuser=accessuser;
        this.securityManager=securityManager;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDTI userDTI){
        try {
            User user =accessuser.createUser(
                    userDTI.username(),
                    userDTI.password(),
                    userDTI.firstname(),
                    userDTI.lastname(),
                    userDTI.email(),
                    userDTI.street(),
                    userDTI.street_nr(),
                    userDTI.zip(),
                    userDTI.city());


                //Generate the token Security
                String token = securityManager.createUserToken(user);
                UserDTO userDTO = new UserDTO(token, user.getUsername(),user.getFirstname(),user.getLastname(),user.getEmail());

                //returning token in header and response in body.
                return ResponseEntity.ok().header("Authorization",token).body(userDTO);
            }
        catch (Exception e){
                return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestBody LogInDTO logInDTO){
        //Check if username and password are valid or not
        User user =accessuser.findUserByName(logInDTO.username());
        if(user==null){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Incorrect Login Credentials");
        }

        //check password
        boolean checkPassword =PasswordTools.checkPassword(logInDTO.password(), user.getPasswordHash(),user.getPasswordSalt());
        if(!checkPassword) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Incorrect Login Credentials");

        //create the token everytime a userlogin cause when userlog out token is removed.
        String token = securityManager.createUserToken(user);

        UserDTO userDTO = new UserDTO(token, user.getUsername(),user.getFirstname(), user.getLastname(),user.getEmail());
        return ResponseEntity.ok().header("Authorization",token).body(userDTO);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser(@RequestHeader("Authorization") String token){
        //check if the token is still valid
        securityManager.checkIfTokenIsAccepted(token);
        //get user from the token
       User user= securityManager.getUser(token);

        UserDTO userDTO=new UserDTO(null, user.getUsername(),user.getFirstname(), user.getLastname(), user.getEmail());
        return ResponseEntity.ok().header("Authorization",token).body(userDTO);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logOut(@RequestHeader("Authorization") String token){
        //check if the token is still valid
        securityManager.checkIfTokenIsAccepted(token);

        //remove the token from the map when logout
        securityManager.removeUserToken(token);

        return ResponseEntity.ok().header("Authorization",token).body("log out successful");
    }
}