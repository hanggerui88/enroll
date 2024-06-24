package org.enroll.service.impl;

import org.enroll.mapper.UserMapper;
import org.enroll.service.interfaces.IRegistrationService;
import org.enroll.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class RegistrationServiceImpl implements IRegistrationService {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z]).{5,}$";

    @Autowired
    private UserMapper userMapper;

    @Override
    public void registerUser(String name, String pass,String email) {

            if (userMapper.checkUsernameExists(name)) {
                throw new IllegalArgumentException(name+"用户已存在");
            }else {
                addUser(name,pass,email);
            }
        }


    private void addUser(String username, String password, String email) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        userMapper.insertUser(user);
    }

    @Override
    public Boolean checkUsernameExists(String username){
        return userMapper.checkUsernameExists(username);
    }
    @Override
    public Boolean checkEmailExists(String email){
        return userMapper.checkUsernameExists(email);
    }

    @Override
    public void updatePasswordByEmail(String email) {
        if(userMapper.checkEmailExists(email)){
            userMapper.updatePasswordByEmail(email);
        }
    }
    @Override
    public String getPasswordByUsername(String username){
        return userMapper.getPasswordByUsername(username);
    }

    @Override
    public  boolean isValidEmail(String email){
        Pattern pattern = Pattern.compile(EMAIL_REGEX);

        // 创建匹配器对象
        Matcher matcher = pattern.matcher(email);

        // 返回是否匹配
        return matcher.matches();
    }

    @Override
    public  boolean isValidPassword(String password) {
        // 编译正则表达式
        Pattern pattern = Pattern.compile(PASSWORD_REGEX);

        // 创建匹配器对象
        Matcher matcher = pattern.matcher(password);

        // 返回是否匹配
        return matcher.matches();
    }
}
