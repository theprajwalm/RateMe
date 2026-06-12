package com.rateme.rateme.controller;

import com.rateme.rateme.DBAccess.dbaccessuser;
import com.rateme.rateme.dto.UserDTI;
import com.rateme.rateme.dto.UserDTO;
import com.rateme.rateme.model.User;
import com.rateme.rateme.util.PasswordTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserConroller {
    @Autowired
    private final dbaccessuser accessuser;

    public UserConroller(dbaccessuser accessuser){
        this.accessuser=accessuser;
    }

    @PostMapping("/register")
    public void register(@RequestBody UserDTI userDTI){

//        //checking if username and email exits or not in db as only one username and email is allowed
//        User userWithUsername =accessuser.findUserByName(userDTI.username());
//        User userWithEmail =accessuser.findUserByEmail(userDTI.email());
//
//        if(userWithUsername !=null){
//            throw new IllegalArgumentException("Username already exits!");
//        }
//        if(userWithEmail != null){
//            throw new IllegalArgumentException("Email already exits!");
//        }


//        user.setUsername(userDTI.username());
//        user.setEmail(userDTI.email());
//
//        user.setFirstname(userDTI.firstname());
//        user.setLastname(userDTI.lastname());
//        user.setStreet(userDTI.street());
//        user.setStreet_nr(userDTI.street_nr());
//        user.setZip(userDTI.zip());
//        user.setCity(userDTI.city());
        accessuser.createUser(
                userDTI.username(),
                userDTI.password(),
                userDTI.firstname(),
                userDTI.lastname(),
                userDTI.email(),
                userDTI.street(),
                userDTI.street_nr(),
                userDTI.zip(),
                userDTI.city());

//        //Security
//        byte[] passwordSalt = PasswordTools.generateSalt();
//        user.setPasswordSalt(passwordSalt);
//
//        byte[] passwordHash = PasswordTools.generatePasswordHash(userDTI.password(), passwordSalt);
//        user.setPasswordHash(passwordHash);
    }

}