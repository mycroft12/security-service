package com.fullstack.web;

import com.fullstack.dto.UserForm;
import com.fullstack.entities.AppUser;
import com.fullstack.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/register")
    public AppUser register(@RequestBody UserForm userForm)
    {
        return accountService.saveUser(userForm.getUsername(),userForm.getPassword(),userForm.getConfirmedPassword());
    }

}
