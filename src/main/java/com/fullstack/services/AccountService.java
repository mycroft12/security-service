package com.fullstack.services;

import com.fullstack.entities.AppRole;
import com.fullstack.entities.AppUser;

public interface AccountService {

    public AppUser saveUser(String username,String password, String confirmedPassword);
    public AppRole saveRole(AppRole role);
    public AppUser loadUserByUsername(String username);
    public void addRoleToUser(String username, String roleName);
}
