package com.rateme.rateme.Security;

import com.rateme.rateme.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SecurityManager {

    //Map to store the tokens of the user
    private final Map<String, User> accessList = new ConcurrentHashMap<>();

    //create user token
    public String createUserToken(User user){
        String token = UUID.randomUUID().toString();
        accessList.put(token,user);
        return token;
    }

    //remove token
    public boolean removeUserToken(String token){
        User removedUser = accessList.remove(token); //returns the user which is removed.
        return removedUser != null;
    }

    //check if the token is valid or not
    public boolean tokenIsValid(String token){
        return accessList.containsKey(token);
    }

    //get user from the String key
    public User getUser(String token){
        if(!this.tokenIsValid(token)) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Token is not found");
        return accessList.get(token);
    }

    //check if token is in accepted
    public void checkIfTokenIsAccepted(String token){
        if(!this.tokenIsValid(token)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }
    //check if the given token is from the user or not
    public void checkTokenFromUser(String token,long userId){
        //first check wheather the token exist or not
        if(userId!=this.getUser(token).getId()){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }
}