package com.bcb.trust.front.modules.system.model.validator;

import java.util.ArrayList;
import java.util.List;

import com.bcb.trust.front.modules.system.model.entity.SystemUserEntity;
import com.bcb.trust.front.modules.system.model.repository.SystemUserEntityRepository;

public class SystemUserValidator {

    private boolean valid;

    private List<String> errorList;

    private SystemUserEntityRepository systemUserEntityRepository;

    private SystemUserEntity systemUserEntity;

    public SystemUserValidator() {
        this.valid = true;
        this.errorList = new ArrayList<>();
    }

    public boolean isValid() {
        return valid;
    }

    /**
     * Indicate if has encounter error on validation process
     * 
     * @return True if has errors, false if not error found.
     * @author Giovanni Rodriguez <grodriguez@bcbcasadebolsa.com>
     */
    public boolean hasErrors() {
        return this.errorList.size() > 0;
    }

    /**
     * Return the errors found on validation process
     * 
     * @return The error list of validation process.
     * @author Giovanni Rodriguez <grodriguez@bcbcasadebolsa.com>
     */
    public List<String> getErrorList() {
        return errorList;
    }

    public SystemUserEntityRepository getSystemUserEntityRepository() {
        return systemUserEntityRepository;
    }

    public void setSystemUserEntityRepository(SystemUserEntityRepository systemUserEntityRepository) {
        this.systemUserEntityRepository = systemUserEntityRepository;
    }

    public SystemUserEntity getSystemUserEntity() {
        return systemUserEntity;
    }

    public void setSystemUserEntity(SystemUserEntity systemUserEntity) {
        this.valid = true;
        this.errorList.clear();

        this.systemUserEntity = systemUserEntity;
    }


    public boolean validateNickname(String nickname) {
        
        if (this.systemUserEntityRepository != null) {
            SystemUserEntity found = this.systemUserEntityRepository.findByNickname(nickname);
            if (found != null) {
                this.valid &= false;
                errorList.add("Nickname ya registrado");
            }

        }

        return valid;
    }

}
