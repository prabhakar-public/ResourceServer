package com.appsdeveloperblog.ws.api.resourceserver.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class KeycloakRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    Map<String, String> cachedRoleMap = null;

    // TODO: hard coded for now. how do we change it later? application.yml? spring may not be ready when this method is called.. need to check. try bootstrap properties?
    public static final String ROLE_MAPPING_FILE_PATH = "src/main/resources/si-security-role-mapping.yml";

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Map<String, Object> realmAccess = (Map<String, Object>) jwt.getClaims().get("realm_access");

        if (realmAccess == null || realmAccess.isEmpty()) {
            return new ArrayList<>();
        }

        List<String> intRoleList = getInternalRoleList((List<String>) realmAccess.get("roles"));
        Collection<GrantedAuthority> returnValue = intRoleList
                .stream().map(roleName -> "ROLE_" + roleName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return returnValue;
    }

    // returns the list of internal roles, matching the given externalRoles
    private List<String> getInternalRoleList(List<String> externalRoles) {
        Map<String, String> roleMap = yamlToHashMap(ROLE_MAPPING_FILE_PATH);

        // return only unique roles
        return externalRoles.stream().map(roleMap::get).distinct().collect(Collectors.toList());
    }


    // returns a hashmap with external role as key and its internal role mapping as value
    private synchronized Map<String, String> yamlToHashMap(String filePath) {
        if (cachedRoleMap == null) {
            // Create ObjectMapper instance for YAML
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

            // Read YAML file and convert to HashMap
            File file = new File(filePath);
            Map<String, String> fileRoleMap;

            try {
                fileRoleMap = (HashMap<String, String>) mapper.readValue(file, HashMap.class);
            } catch (IOException e) {
                log.error("Could not read the role mapping file successfully. Exception: " + e.getMessage());
                cachedRoleMap = new HashMap<>(); // assign an empty hashmap
                return cachedRoleMap; // return it
            }
            cachedRoleMap = convertToExternalRoleMap(fileRoleMap);

        }
        return cachedRoleMap;
    }

    // Converts the internal_role->external_role_list to external_role->internal_role mapping needed for later.
    // If an external role is listed twice, the last one wins
    private Map<String, String> convertToExternalRoleMap(Map<String, String> fileRoleMap) {
        HashMap<String, String> returnMap = new HashMap<>();
        for (Map.Entry<String, String> entry : fileRoleMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            List<String> extRoleList = List.of(value.split(","));
            for (String extRole : extRoleList) {
                returnMap.put(extRole.trim(), key);
            }
        }
        return returnMap;
    }

    public static void main(String[] args) {

        List<String> testRoles = List.of("kc-sip-nj-dev-prv-read", "kc-sip-nj-dev-prv-write", "aad-sip-nj-dev-prv-read");
        KeycloakRoleConverter converter = new KeycloakRoleConverter();
        List<String> intRoleList = converter.getInternalRoleList(testRoles);
        log.info("Unique internal role list = " + String.join(",", intRoleList));
        List<String> testRoles2 = List.of("kc-sip-nj-dev-prv-admin", "kc-sip-nj-dev-prv-write", "aad-sip-nj-dev-prv-read");
        List<String> intRoleList2 = converter.getInternalRoleList(testRoles2);
        log.info("Unique internal role list2 = " + String.join(",", intRoleList2));

    }
}
