package com.rateme.rateme.dbaccess;

import com.rateme.rateme.model.User;
import com.rateme.rateme.Security.PasswordTools;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository //just like a component but for DBAccess
@Transactional //No need to manually do rollback and commit now
public class UserDataAccess {
    private final EntityManager entityManager;

    @Autowired
    public UserDataAccess(EntityManager entityManager){
        this.entityManager=entityManager;
    }

    public User findUserById(long id){
        return entityManager.find(User.class,id);
    }

    public User findUserByName(String username){
        try{
            return entityManager.createQuery(
                    "SELECT u FROM User u WHERE u.username = :username"
                            , User.class)
                    .setParameter("username", username).getSingleResult();
            //Searching the username from the DB. No need of username as username is Unique in db
        } catch (NoResultException e) {
            return null;
        }
    }

    public User findUserByEmail(String email){
        try {
            return entityManager.createQuery(
                    "SELECT u FROM User u WHERE u.email = :email",
                            User.class)
                    .setParameter("email",email).
                    getSingleResult();
        }catch (NoResultException e){
            return null;
        }
    }

    public User createUser(String username,String password,String firstname,String lastname,String email,String street,String street_nr,String zip,String city){
        if (findUserByName(username) != null) {
            throw new IllegalArgumentException("This username already exists");
        }
        if (findUserByEmail(email) != null) {
            throw new IllegalArgumentException("This username already exists");
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setStreet(street);
        user.setStreet_nr(street_nr);
        user.setZip(zip);
        user.setCity(city);

        //Security
        byte[] salt = PasswordTools.generateSalt();
        byte[] passwordHash = PasswordTools.generatePasswordHash(password,salt);
        user.setPasswordSalt(salt);
        user.setPasswordHash(passwordHash);

        //saving user in Database
        try {
            entityManager.persist(user);
            entityManager.flush();
        }catch (Exception e){
            System.out.println("Error in creating new user in database");
            throw e;
        }
        return user;
    }

    public void deleteUserById(long userId){
        //check if user exists or not before deleting
        User foundUser = findUserById(userId);
        if (foundUser == null) {
            throw new IllegalArgumentException("User with id " + userId + " not found");
        }

        //deleting the rating of the user before.
        entityManager.createQuery("DELETE FROM Rating r WHERE r.user.id = :userId")
                .setParameter("userId", userId)
                .executeUpdate();
        //delete the user
        entityManager.remove(foundUser);
    }

    public void deleteUser(User user){
        deleteUserById(user.getId());
    }
}