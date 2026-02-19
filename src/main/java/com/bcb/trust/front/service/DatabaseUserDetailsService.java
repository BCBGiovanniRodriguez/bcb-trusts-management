package com.bcb.trust.front.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bcb.trust.front.model.trusts.dto.UserAuthenticated;
import com.bcb.trust.front.modules.system.model.entity.CatalogUserEntity;
import com.bcb.trust.front.modules.system.model.entity.ConfigurationProfileResourceEntity;
import com.bcb.trust.front.modules.system.model.entity.ConfigurationUserProfileEntity;
import com.bcb.trust.front.modules.system.model.repository.ConfigurationUserProfileRepository;
import com.bcb.trust.front.modules.system.model.repository.ProfileResourceRepository;
import com.bcb.trust.front.modules.system.model.repository.UserEntityRepository;

@Service
public class DatabaseUserDetailsService implements UserDetailsService {

    @Autowired
    private UserEntityRepository systemUserRepository;

    @Autowired
    private ConfigurationUserProfileRepository userProfileRepository;

    @Autowired
    private ProfileResourceRepository profileResourceRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("DatabaseUserDetailsService::loadUserByUsername()");
        CatalogUserEntity systemUser = null;
        List<ConfigurationUserProfileEntity> userProfileEntityList = new ArrayList<>();
        List<ConfigurationProfileResourceEntity> profileResourceEntityList = new ArrayList<>();
        Set<GrantedAuthority> authorities = new HashSet<>();
        UserAuthenticated userAuthenticated = null;
        
        try {
            systemUser = systemUserRepository.findByNickname(username);

            if (systemUser == null) {
                throw new UsernameNotFoundException("User not found with username: " + username);
            }

            userProfileEntityList = userProfileRepository.findByUserEntity(systemUser);

            for (ConfigurationUserProfileEntity userProfileEntity : userProfileEntityList) {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + userProfileEntity.getProfileEntity().getCode()));
                profileResourceEntityList = profileResourceRepository.findByProfileEntity(userProfileEntity.getProfileEntity());

                for (ConfigurationProfileResourceEntity profileResourceEntity : profileResourceEntityList) {
                    authorities.add(new SimpleGrantedAuthority(profileResourceEntity.getResourceEntity().getCode()));
                }
            }

            System.out.println("Autorities registered");
            for (GrantedAuthority grantedAuthority : authorities) {
                System.out.println("Authority: " + grantedAuthority);
            }

            userAuthenticated = new UserAuthenticated();
            userAuthenticated.setSystemUser(systemUser);
            userAuthenticated.setAuthorities(authorities);

        } catch (Exception e) {
            System.out.println("Error in DatabaseUserDetailsService::loadUserByUsername()" + e.getMessage());
        }

        if (userAuthenticated == null) {
            throw new UsernameNotFoundException("Error en autenticación de usuario");
        }

        return userAuthenticated;
    }

}
