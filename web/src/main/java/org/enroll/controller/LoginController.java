package org.enroll.controller;

import org.enroll.configuration.LoginProperties;
import org.enroll.service.interfaces.IRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    LoginProperties properties;

    @Autowired
    private IRegistrationService RegistrationService;


    @RequestMapping("/doLogin")

    public ModelAndView doLogin(String name, String pass){
        ModelAndView modelAndView = new ModelAndView();
        if (name == null||pass==null) {
            modelAndView.setViewName("login");
        }else {
            if(properties.getAdminName().equals(name) && properties.getAdminPass().equals(pass)){
                modelAndView.setViewName("backstage");
            } else {
                if(checkUser(name,pass)){
                    modelAndView.setViewName("user");
                }else{
                modelAndView.setViewName("loginError");
                }
            }
        }
        return modelAndView;
    }

    @RequestMapping("/user")
    public ModelAndView user(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("user");
        return modelAndView;
    }

        @RequestMapping("/register")
    public ModelAndView register(String name, String pass, String email){
        ModelAndView modelAndView = new ModelAndView();
        if (name == null||pass==null||email==null) {
            modelAndView.setViewName("register");
        } else {
            if(RegistrationService.isValidEmail(email)&&RegistrationService.isValidPassword(pass)){
                RegistrationService.registerUser(name,pass,email);
                modelAndView.setViewName("registerSuccess");
            }else {
                modelAndView.setViewName("registerError");
            }
    }

        return modelAndView;
    }

    @RequestMapping("/back")
    public ModelAndView back() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("backstage");
        return modelAndView;
    }

    @RequestMapping("/reset")
    public ModelAndView reset() {
        ModelAndView modelAndView = new ModelAndView();
//        if (email == null) {
        //弃用
            modelAndView.setViewName("reset");
//        } else {
//            try {
//                // 检查邮箱是否存在并更新密码
//                if (!(RegistrationService.checkEmailExists(email))) {
//                    RegistrationService.updatePasswordByEmail(email);
//                    // 密码更新成功，重定向到登录页面，并返回成功消息
//                    modelAndView.setViewName("login");
//                } else {
//                    // 邮箱不存在，返回重置密码页面并显示错误消息
//                    modelAndView.setViewName("reset");
//                }
//            } catch (Exception e) {
//                // 捕获异常，返回错误页面或者错误消息
//                modelAndView.setViewName("registerError");
//            }
//        }
        return modelAndView;
    }

    private Boolean checkUser(String name,String pass){
        if(RegistrationService.checkUsernameExists(name)){
            if (RegistrationService.getPasswordByUsername(name).equals(pass)){
                 return true;
            }else{
                System.out.println(RegistrationService.getPasswordByUsername(pass));
                return false;
            }
        }
        System.out.println(RegistrationService.getPasswordByUsername(name));
        System.out.println(RegistrationService.checkUsernameExists(name));
        return false;
    }

}
