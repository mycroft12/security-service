package com.fullstack.services;

import com.fullstack.dao.AppRoleRepository;
import com.fullstack.dao.AppUserRepository;
import com.fullstack.entities.AppRole;
import com.fullstack.entities.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private AppRoleRepository appRoleRepository;
    @Autowired
    private BCryptPasswordEncoder encoder;


    @Override
    public AppUser saveUser(String username, String password, String confirmedPassword) {

        AppUser user=appUserRepository.findByUsername(username);
        if(user != null) throw new RuntimeException("User already exists");
        if(!password.equals(confirmedPassword)) throw new RuntimeException("Wrong confirmed password");
        //Otherwise we gonna create the user
        AppUser appUser=new AppUser();

        appUser.setUsername(username);
        appUser.setActivated(true);
        appUser.setPassword(encoder.encode(password));
        appUserRepository.save(appUser);
        addRoleToUser(username,"USER");
        return appUser;
    }

    @Override
    public AppRole saveRole(AppRole role) {
        return appRoleRepository.save(role);
    }

    @Override
    public AppUser loadUserByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        AppUser user=appUserRepository.findByUsername(username);
        AppRole role=appRoleRepository.findByRoleName(roleName);
        user.getRoles().add(role);
    }
}
