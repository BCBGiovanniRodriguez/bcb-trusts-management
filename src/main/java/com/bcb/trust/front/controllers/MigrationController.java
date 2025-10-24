package com.bcb.trust.front.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bcb.trust.front.modules.legacy.service.WorkerService;
import com.bcb.trust.front.modules.system.model.entity.SystemUserEntity;
import com.bcb.trust.front.modules.system.model.repository.SystemUserEntityRepository;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
@RequestMapping("/migration")
public class MigrationController {

    @Autowired
    private SystemUserEntityRepository systemUserEntityRepository;

    @Autowired
    private WorkerService workerService;

    @GetMapping("/")
    public String index() {

        try {
            
        } catch (Exception e) {
            System.out.println("" + e.getLocalizedMessage());
        }

        return "migration/index";
    }

    @GetMapping("/migrate-requests")
    public String migrateRequests(Authentication authentication) {

        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            SystemUserEntity systemUserEntity = systemUserEntityRepository.findByNickname(userDetails.getUsername());

            workerService.migrateRequest(systemUserEntity);

        } catch (Exception e) {
            System.out.println("" + e.getLocalizedMessage());
        }

        return "migration/migrate-requests";
    }
    

    @GetMapping("/migrate-trustors-trustees")
    public String migrateTrustorsTrustees() {

        try {
            workerService.migrateTrusterAndTrustees();
        } catch (Exception e) {
            System.out.println("" + e.getLocalizedMessage());
        }
        
        return "migration/migrate-trustors-trustees";
    }
    
    @GetMapping("/migrate-trusts")
    public String migrateTrusts(Authentication authentication) {

        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            SystemUserEntity systemUserEntity = systemUserEntityRepository.findByNickname(userDetails.getUsername());

            workerService.migrateTrusts(systemUserEntity);

        } catch (Exception e) {
            System.out.println("" + e.getLocalizedMessage());
        }

        return "migration/migrate-trustors-trustees";
    }
    
    @GetMapping("/sirjum-generate-periods")
    public String sirjumGeneratePeriods() {

        try {
            workerService.generateFixedPeriods(1045);

        } catch (Exception e) {
            System.out.println("" + e.getLocalizedMessage());
        }

        return "migration/sirum-generate-periods";
    }

    @GetMapping("/sirjum-migrate-workers")
    public String sirjumMigrateWorkers() {

        try {
            
        } catch (Exception e) {
            System.out.println("" + e.getLocalizedMessage());
        }
        
        return "migration/sirjum-migrate-workers";
    }
    
}
