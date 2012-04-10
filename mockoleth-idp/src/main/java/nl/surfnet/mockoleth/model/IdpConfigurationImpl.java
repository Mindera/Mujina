/*
 * Copyright 2012 SURFnet bv, The Netherlands
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nl.surfnet.mockoleth.model;

import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.surfnet.mockoleth.spring.security.CustomAuthentication;

public class IdpConfigurationImpl extends CommonConfigurationImpl implements IdpConfiguration {

    private final static Logger LOGGER = LoggerFactory.getLogger(IdpConfigurationImpl.class);

    private Map<String, String> attributes = new TreeMap<String, String>();
    private Collection<CustomAuthentication> users = new ArrayList<CustomAuthentication>();

    public IdpConfigurationImpl() {
        reset();
    }

    @Override
    public void reset() {
        entityId = "idp";
        attributes.clear();
        attributes.put("urn:mace:dir:attribute-def:uid", "john.doe");
        attributes.put("urn:mace:dir:attribute-def:cn", "John Doe");
        attributes.put("urn:mace:dir:attribute-def:givenName", "John");
        attributes.put("urn:mace:dir:attribute-def:sn", "Doe");
        attributes.put("urn:mace:dir:attribute-def:displayName", "John Doe");
        attributes.put("urn:mace:dir:attribute-def:mail", "j.doe@example.com");
        attributes.put("urn:mace:terena.org:attribute-def:schacHomeOrganization", "example.com");
        attributes.put("urn:mace:dir:attribute-def:eduPersonPrincipalName", "j.doe@example.com");
        attributes.put("urn:oid:1.3.6.1.4.1.1076.20.100.10.10.1", "guest");
        try {
            keyStore = KeyStore.getInstance("JKS");
            keyStore.load(null, keystorePassword.toCharArray());
            appendToKeyStore(keyStore, "idp", "idp-crt.pem", "idp-key.pkcs8.der", keystorePassword.toCharArray());
            appendToKeyStore(keyStore, "sp", "idp-crt.pem", "idp-key.pkcs8.der", keystorePassword.toCharArray());
            privateKeyPasswords.put("idp", keystorePassword);
            privateKeyPasswords.put("sp", keystorePassword);
        } catch (Exception e) {
            LOGGER.error("Unable to create default keystore", e);
        }
        users.clear();
        final CustomAuthentication admin = new CustomAuthentication("admin", "secret");
        admin.addAuthority("ROLE_USER");
        admin.addAuthority("ROLE_ADMIN");
        users.add(admin);
        final CustomAuthentication user = new CustomAuthentication("user", "secret");
        user.addAuthority("ROLE_USER");
        users.add(user);
    }

    @Override
    public Map<String, String> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<CustomAuthentication> getUsers() {
        return users;
    }

}