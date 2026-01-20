package com.bcb.trust.front.modules.system.model.validator;

import java.util.ArrayList;
import java.util.List;

import com.bcb.trust.front.modules.system.model.entity.SystemResourceEntity;
import com.bcb.trust.front.modules.system.model.repository.SystemResourceRepository;

public class SystemResourceValidator {

    private boolean valid;

    private List<String> errorList;

    private SystemResourceRepository systemResourceRepository;

    private SystemResourceEntity systemResourceEntity;

    public SystemResourceValidator() {
        this.valid = true;
        this.errorList = new ArrayList<>();
    }

    public void setSystemResourceRepository(SystemResourceRepository systemResourceRepository) {
        this.systemResourceRepository = systemResourceRepository;
    }

    public void setSystemResourceEntity(SystemResourceEntity systemResourceEntity) {
        this.systemResourceEntity = systemResourceEntity;
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
     * @throws Exception If entity is not provided and if uniqueness is required but
     *                   repository not provided.
     * @author Giovanni Rodriguez <grodriguez@bcbcasadebolsa.com>
     */
    public void validate(boolean unique) throws Exception {

        if (unique && this.systemResourceRepository == null) {
            throw new Exception("Repository no proporcionado");
        }

        if (systemResourceEntity == null) {
            throw new Exception("Entidad base no proporcionada");
        }

        String name = this.systemResourceEntity.getAction();
        String code = this.systemResourceEntity.getCode();

        if (unique) {
            validateElement(name, unique);
            validateCode(code, unique);
        } else {
            validateElement(name);
            validateCode(code);
        }

    }

    /**
     * Perform validation on attribute name of entity provided.
     * 
     * @param element
     * @return True if fit the conditions
     */
    public boolean validateElement(String element) {

        if (element == null) {
            valid &= false;
            errorList.add("Valor de Nombre no proporcionado");
        } else {
            if (element.length() == 0) {
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
     * @param element
     * @param unique  If true and repository provided perform uniqueness validation
     *                otherwise perform normal validation
     * @return True if fit the conditions
     */
    public boolean validateElement(String element, boolean unique) {

        if (unique && this.systemResourceRepository != null) {
            SystemResourceEntity found = this.systemResourceRepository.findOneByElement(element);
            if (found != null) {
                errorList.add("Elemento ya registrado");
            }

            valid &= validateElement(element) & (found == null);
        } else {
            valid &= validateElement(element);
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

        if (unique && this.systemResourceRepository != null) {
            SystemResourceEntity found = this.systemResourceRepository.findOneByCode(code);
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
