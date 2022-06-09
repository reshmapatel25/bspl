package com.example.register.service;

import com.example.register.model.User;
import com.example.register.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepository userRepo;

    public UserDetailsServiceImpl(UserRepository userRepo) {
        this.userRepo = userRepo;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {   String uname;
        User user = userRepo.findByEmailOrMobile(username);
        if(user == null) {
            throw new UsernameNotFoundException(username);
        }else{
            if(username.equalsIgnoreCase(user.getEmail())){
                uname=user.getEmail();
            }
            else{
                uname=user.getMobile();
            }
        }
        List<GrantedAuthority> authorityList=new ArrayList<>();
        SimpleGrantedAuthority authority=null;
        if(username.equalsIgnoreCase("admin ")){
            authority=new SimpleGrantedAuthority("ADMIN");
        }else{
            authority=new SimpleGrantedAuthority("USER");
        }
        authorityList.add(authority);
       return new org.springframework.security.core.userdetails.User(uname, user.getPassword(), authorityList);
    }
}
