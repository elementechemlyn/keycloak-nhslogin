/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.keycloak.social.nhslogin;

import org.keycloak.broker.oidc.OAuth2IdentityProviderConfig;
import org.keycloak.broker.oidc.OIDCIdentityProviderConfig;
import org.keycloak.broker.provider.AbstractIdentityProviderFactory;
import org.keycloak.broker.social.SocialIdentityProviderFactory;
import org.keycloak.models.IdentityProviderModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.protocol.oidc.representations.OIDCConfigurationRepresentation;
import org.keycloak.util.JsonSerialization;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class NHSOIDCIdentityProviderFactory<C extends OAuth2IdentityProviderConfig> extends AbstractIdentityProviderFactory<NHSOIDCIdentityProvider>
    implements SocialIdentityProviderFactory<NHSOIDCIdentityProvider> {

            public static final String PROVIDER_ID = "nhsoidc";
            public static final String PROVIDER_NAME = "NHS Login OIDC";
            
            @Override
            public String getName() {
                return PROVIDER_NAME;
            }
        
            @Override
            public NHSOIDCIdentityProvider create(KeycloakSession session, IdentityProviderModel model) {
                return new NHSOIDCIdentityProvider(session, new NHSOIDCIdentityProviderConfig(model));
            }
        
            @Override
            public OIDCIdentityProviderConfig createConfig() {
                return new OIDCIdentityProviderConfig();
            }
        
            @Override
            public String getId() {
                return PROVIDER_ID;
            }
        
            @Override
            public Map<String, String> parseConfig(KeycloakSession session, InputStream inputStream) {
                return parseOIDCConfig(session, inputStream);
            }
        
            protected static Map<String, String> parseOIDCConfig(KeycloakSession session, InputStream inputStream) {
                OIDCConfigurationRepresentation rep;
                try {
                    rep = JsonSerialization.readValue(inputStream, OIDCConfigurationRepresentation.class);
                } catch (IOException e) {
                    throw new RuntimeException("failed to load openid connect metadata", e);
                }
                OIDCIdentityProviderConfig config = new OIDCIdentityProviderConfig(new IdentityProviderModel());
                config.setIssuer(rep.getIssuer());
                config.setLogoutUrl(rep.getLogoutEndpoint());
                config.setAuthorizationUrl(rep.getAuthorizationEndpoint());
                config.setTokenUrl(rep.getTokenEndpoint());
                config.setUserInfoUrl(rep.getUserinfoEndpoint());
                if (rep.getJwksUri() != null) {
                    config.setValidateSignature(true);
                    config.setUseJwksUrl(true);
                    config.setJwksUrl(rep.getJwksUri());
                }
                return config.getConfig();
            }
        
}