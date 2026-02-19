package com.bcb.trust.front.modules.system.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bcb.trust.front.modules.system.model.entity.CatalogProfileEntity;
import com.bcb.trust.front.modules.system.model.entity.CatalogResourceEntity;
import com.bcb.trust.front.modules.system.model.entity.CatalogUserEntity;
import com.bcb.trust.front.modules.system.model.entity.ConfigurationProfileResourceEntity;
import com.bcb.trust.front.modules.system.model.repository.ProfileRepository;
import com.bcb.trust.front.modules.system.model.repository.ProfileResourceRepository;
import com.bcb.trust.front.modules.system.model.repository.ResourceRepository;
import com.bcb.trust.front.modules.system.model.repository.UserEntityRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@RestController
@RequestMapping("/api/system")
public class SystemProfileController {

    @Autowired
    private ResourceRepository systemResourceRepository;

    @Autowired
    private ProfileRepository systemProfileRepository;

    @Autowired
    private ProfileResourceRepository profileResourceRepository;

    @Autowired
    private UserEntityRepository systemUserEntityRepository;

    @GetMapping("/profile")
    public String getProfiles() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String jsonResponse = null;
        Map<String, Object> resultMap = new HashMap<>();
        List<CatalogProfileEntity> profileEntityList = null;

        try {
            profileEntityList = systemProfileRepository.findAll();

            resultMap.put("status", 1);
            resultMap.put("message", "Petición Correcta");
            resultMap.put("data", profileEntityList);

            jsonResponse = mapper.writeValueAsString(resultMap);
        } catch (Exception e) {
            resultMap.put("status", 0);
            resultMap.put("message", "Error en SystemProfileController::getAll[" + e.getLocalizedMessage() + "]");
            resultMap.put("data", null);

            jsonResponse = mapper.writeValueAsString(resultMap);
        }

        return jsonResponse;
    }

    @GetMapping("/profile/resource/{id}")
    public String getMethodName(@PathVariable Long id) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String jsonResponse = null;
        Map<String, Object> resultMap = new HashMap<>();

        Set<CatalogResourceEntity> resourceSet = new HashSet<>();
        List<Map> resourceList = new ArrayList<>();

        try {
            Optional<CatalogProfileEntity> result = systemProfileRepository.findById(id);

            if (result.isPresent()) {
                CatalogProfileEntity profileEntity = result.get();

                /*
                 * for (SystemResourceEntity systemResourceEntity :
                 * profileEntity.getResources()) {
                 * resourceList.add(systemResourceEntity.toMap());
                 * }
                 */
            }

            resultMap.put("status", 1);
            resultMap.put("message", "Petición Correcta");
            resultMap.put("data", resourceList);

            jsonResponse = mapper.writeValueAsString(resultMap);

        } catch (Exception e) {
            resultMap.put("status", 0);
            resultMap.put("message", "Error en SystemProfileController::getAll[" + e.getLocalizedMessage() + "]");
            resultMap.put("data", null);

            jsonResponse = mapper.writeValueAsString(resultMap);
        }

        return jsonResponse;
    }

    @PostMapping("/profile")
    public String saveProfile(@RequestBody Map<String, Object> data) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String jsonResponse = null;
        Map<String, Object> resultMap = new HashMap<>();
        String members = null;

        try {
            if (data.size() < 0) {
                throw new Exception("No se envíaron datos");
            }

            CatalogProfileEntity profileEntity = new CatalogProfileEntity();

            if (!data.containsKey("name")) {
                throw new Exception("Nombre es requerido");
            }

            members = data.get("members").toString();
            // System.out.println(members);
            if (members != null) {
                // profileEntity.setDescription(Integer.parseInt(members));
            }

            profileEntity.setName(data.get("name").toString());
            // profileEntity.setStatus(SystemProfileEntity.STATUS_ENABLED);
            profileEntity.setCreatedAt(LocalDateTime.now());

            systemProfileRepository.saveAndFlush(profileEntity);

            resultMap.put("status", 1);
            resultMap.put("message", "Petición Correcta");
            resultMap.put("data", profileEntity);

            jsonResponse = mapper.writeValueAsString(resultMap);
        } catch (Exception e) {
            resultMap.put("status", 0);
            resultMap.put("message", "Error en: SystemProfileController::post[" + e.getLocalizedMessage() + "]");
            resultMap.put("data", null);

            jsonResponse = mapper.writeValueAsString(resultMap);
        }

        return jsonResponse;
    }

    @PutMapping("/profile/{id}")
    public String putProfile(@PathVariable Long id, @RequestBody Map<String, Object> data)
            throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String jsonResponse = null;
        Map<String, Object> resultMap = new HashMap<>();
        CatalogProfileEntity profileEntity = null;

        try {
            Optional<CatalogProfileEntity> result = systemProfileRepository.findById(id);

            if (!result.isPresent()) {
                throw new Exception("Registro con identificador: " + id + " no encontrado");
            } else {
                // Get data from request body
                String name = data.get("name").toString();
                Integer members = null;
                Object membersObj = data.get("members");
                List<Long> resourceList = (ArrayList<Long>) data.get("resourceIds");

                if (membersObj != null) {
                    members = Integer.parseInt(membersObj.toString());
                }

                profileEntity = result.get();
                profileEntity.setName(name);
                // profileEntity.setDescription(members);

                if (resourceList.size() > 0) {
                    List<CatalogResourceEntity> resourceEntityList = systemResourceRepository.findAllById(resourceList);

                    // profileEntity.setResources(new HashSet<>());

                    for (CatalogResourceEntity resourceEntity : resourceEntityList) {
                        // profileEntity.getResources().add(resourceEntity);
                    }
                }

                systemProfileRepository.saveAndFlush(profileEntity);
            }

            resultMap.put("status", 1);
            resultMap.put("message", "Petición Correcta");
            resultMap.put("data", profileEntity.toMap());

            jsonResponse = mapper.writeValueAsString(resultMap);

        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            resultMap.put("status", 0);
            resultMap.put("message", "Error en SystemProfileController::put[" + e.getLocalizedMessage() + "]");
            resultMap.put("data", null);

            jsonResponse = mapper.writeValueAsString(resultMap);
        }

        return jsonResponse;
    }

    @PostMapping("/profile/assign-resources")
    public String addResources(@RequestBody Map<String, Object> data, Authentication authentication)
            throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String jsonResponse = null;
        Map<String, Object> resultMap = new HashMap<>();
        Long profileId;

        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            CatalogUserEntity systemUserEntity = systemUserEntityRepository.findByNickname(userDetails.getUsername());

            profileId = Long.parseLong(data.get("profileId").toString());
            Optional<CatalogProfileEntity> profileResult = systemProfileRepository.findById(profileId);

            if (!profileResult.isPresent()) {
                throw new Exception("Perfil con identificador: " + profileId + " no encontrado");
            }

            CatalogProfileEntity profileEntity = profileResult.get();
            List<Long> resourceIds = (ArrayList<Long>) data.get("resourceIds");

            if (resourceIds.size() > 0) {
                List<CatalogResourceEntity> resourceEntityList = systemResourceRepository.findAllById(resourceIds);

                for (CatalogResourceEntity resourceEntity : resourceEntityList) {
                    ConfigurationProfileResourceEntity profileResourceEntity = new ConfigurationProfileResourceEntity();
                    profileResourceEntity.setProfileEntity(profileEntity);
                    profileResourceEntity.setResourceEntity(resourceEntity);
                    profileResourceEntity.setAssignedAt(LocalDateTime.now());
                    profileResourceEntity.setAssignedBy(systemUserEntity);
                    profileResourceEntity.setActive(1);
                    profileResourceEntity.setPermissionType(1);

                    profileResourceRepository.saveAndFlush(profileResourceEntity);
                }
            }

            resultMap.put("status", 1);
            resultMap.put("message", "Recursos agregados correctamente");
            resultMap.put("data", profileEntity.toMap());

        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            resultMap.put("status", 0);
            resultMap.put("message", "Error en SystemProfileController::addResources[" + e.getLocalizedMessage() + "]");
            resultMap.put("data", null);
        }

        jsonResponse = mapper.writeValueAsString(resultMap);
        return jsonResponse;
    }
}
