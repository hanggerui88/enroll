package org.enroll.service.interfaces;


public interface IRegistrationService {

     void registerUser(String name, String pass,String email);
     Boolean checkUsernameExists(String name);
     Boolean checkEmailExists(String name);
     void updatePasswordByEmail(String email);
     String getPasswordByUsername(String username);
     boolean isValidEmail(String email);
     boolean isValidPassword(String password);
}
