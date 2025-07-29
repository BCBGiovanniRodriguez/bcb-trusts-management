package com.bcb.trust.front.modules.system.model.validator;

import java.util.ArrayList;
import java.util.List;

import com.bcb.trust.front.modules.system.model.entity.SystemPermissionEntity;
import com.bcb.trust.front.modules.system.model.repository.SystemPermissionRepository;

public class SystemPermissionValidator {

    private boolean valid;

    private List<String> errorList;

    private SystemPermissionRepository systemPermissionRepository;

    private SystemPermissionEntity systemPermissionEntity;

    public SystemPermissionValidator() {
        this.valid = true;
        this.errorList = new ArrayList<>();
    }

    public void setSystemPermissionRepository(SystemPermissionRepository systemPermissionRepository) {
        this.systemPermissionRepository = systemPermissionRepository;
    }

    public void setSystemPermissionEntity(SystemPermissionEntity systemPermissionEntity) {
        this.systemPermissionEntity = systemPermissionEntity;
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
    public List<String> getErrors() {
        return errorList;
    }

    /**
     * Perform validation over the entity provided.
     * 
     * @param unique If true, search values in database for uniqueness.
     * @throws Exception If entity is not provided and if uniqueness is required but repository not provided.
     * @author Giovanni Rodriguez <grodriguez@bcbcasadebolsa.com>
     */
    public void validate(boolean unique) throws Exception {
        
        if (unique && this.systemPermissionRepository == null) {
            throw new Exception("Repository no proporcionado");
        }

        if (systemPermissionEntity == null) {
            throw new Exception("Entidad base no proporcionada");
        }

        String name = this.systemPermissionEntity.getName();
        String code = this.systemPermissionEntity.getCode();

        if (unique) {
            validateName(name, unique);
            validateCode(code, unique);
        } else {
            validateName(name);
            validateCode(code);
        }

    }

    /**
     * Perform validation on attribute name of entity provided.
     * 
     * @param name
     * @return True if fit the conditions
     */
    public boolean validateName(String name) {

        if (name == null) {
            valid &= false;
            errorList.add("Valor de Nombre no proporcionado");
        } else {
            if (name.length() == 0) {
                valid &= false;
                errorList.add("Longitud de Nombre es 0");
            }

            valid &= true;
        }

        return valid;
    }

    /**
     * Perform validation on attribute name of entity provided.
     * 
     * @param name
     * @param unique If true and repository provided perform uniqueness validation otherwise perform normal validation
     * @return True if fit the conditions
     */
    public boolean validateName(String name, boolean unique) {
        
        if (unique && this.systemPermissionRepository != null) {
            SystemPermissionEntity found = this.systemPermissionRepository.findOneByName(name);
            if (found != null) {
                errorList.add("Nombre ya registrado");
            }

            valid &= validateName(name) & (found == null);
        } else {
            valid &= validateName(name);
        }

        return valid;
    }

    /**
     * 
     * @param code
     * @return
     */
    public boolean validateCode(String code) {
        
        if (code == null) {
            valid &= false;
            errorList.add("Valor de Código no proporcionado");
        } else {
            if (code.length() == 0) {
                valid &= false;
                errorList.add("Longitud de Código es 0");
            }

            valid &= true;
        }

        return valid;
    }

    public boolean validateCode(String code, boolean unique) {
        
        if (unique && this.systemPermissionRepository != null) {
            SystemPermissionEntity found = this.systemPermissionRepository.findOneByCode(code);
            if (found != null) {
                errorList.add("Código ya registrado");
            }
            
            valid &= validateCode(code) & (found == null);
        } else {
            valid &= validateCode(code);
        }

        return valid;
    }
}
