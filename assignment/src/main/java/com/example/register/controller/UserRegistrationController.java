package com.example.register.controller;


import com.example.register.model.User;
import com.example.register.service.UserRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.List;

@RestController
public class UserRegistrationController {
    @Autowired
    UserRegistrationService userRegistrationService;

    @GetMapping("/user")
    public String getThisUser(Principal principal){
        //Principal principal=this
        return userRegistrationService.getThisUser(principal);
    }
    @GetMapping("/users")
    public List<User> getAllUsers(){
    // List<User>  userList=
     return userRegistrationService.getAllUsers();
    }
    @PostMapping("/registerUser")
    @ResponseStatus(HttpStatus.CREATED)
    public User registerUser(@Valid @RequestBody User user, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
    //User user1=
    return userRegistrationService.registerUser(user, getSiteURL(request));
    }
    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        System.out.println(request.getRequestURL());
        System.out.println(siteURL);
        String s= siteURL.replace(request.getServletPath(), "");
        System.out.println(s);
        return s;
    }
    @GetMapping("/verifyEmail")
    public String verifyEmail(@Param("code") String code) {
        if (userRegistrationService.verify(code)) {
            return "email-verification_success";
        } else {
            return "email-verification_fail";
        }
    }
    @GetMapping("/verifyOtp")
    public String verifyOtp(@Param("otp") String otp) {
        if (userRegistrationService.verifyOtp(otp)) {
            return "otp-verification_success";
        } else {
            return "otp-verification_fail";
        }
    }

}
