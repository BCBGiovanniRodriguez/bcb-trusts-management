package com.bcb.trust.front.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bcb.trust.front.model.trusts.dto.UserAuthenticated;
import com.bcb.trust.front.model.trusts.service.SystemUserRepository;
import com.bcb.trust.front.modules.system.model.entity.SystemUserEntity;

@Service
public class DatabaseUserDetailsService implements UserDetailsService {

    @Autowired
    private SystemUserRepository systemUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("DatabaseUserDetailsService::loadUserByUsername()");
        SystemUserEntity systemUser = null;
        System.out.println("Username: " + username);
        try {
            // Fetch the user from the database
            systemUser = systemUserRepository.findByNickname(username);

            // If the user is not found, throw an exception
            if (systemUser == null) {
                throw new UsernameNotFoundException("User not found with username: " + username);
            }

            //System.out.println(systemUser.toString());
        } catch (Exception e) {
            System.out.println("Error in DatabaseUserDetailsService::loadUserByUsername()" + e.getMessage());
        }
        

        // Return a UserDetails object
        return new UserAuthenticated(systemUser);
    }

}
