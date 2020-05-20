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

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.keycloak.broker.oidc.OIDCIdentityProvider;
import org.keycloak.broker.oidc.OIDCIdentityProviderConfig;
import org.keycloak.broker.social.SocialIdentityProvider;
import org.keycloak.crypto.Algorithm;
import org.keycloak.crypto.AsymmetricSignatureProvider;
import org.keycloak.crypto.KeyWrapper;
import org.keycloak.crypto.MacSignatureSignerContext;
import org.keycloak.crypto.SignatureSignerContext;
import org.keycloak.models.KeycloakSession;
import org.keycloak.protocol.oidc.OIDCLoginProtocol;
import org.keycloak.vault.VaultStringSecret;


public class NHSOIDCIdentityProvider extends OIDCIdentityProvider implements SocialIdentityProvider<OIDCIdentityProviderConfig> {

    public NHSOIDCIdentityProvider(KeycloakSession session, OIDCIdentityProviderConfig config) {
        super(session, config);
    }
    protected SignatureSignerContext getSignatureContext() {
        if (getConfig().getClientAuthMethod().equals(OIDCLoginProtocol.CLIENT_SECRET_JWT)) {
            try (VaultStringSecret vaultStringSecret = session.vault().getStringSecret(getConfig().getClientSecret())) {
                KeyWrapper key = new KeyWrapper();
                key.setAlgorithm(Algorithm.HS256);
                byte[] decodedSecret = vaultStringSecret.get().orElse(getConfig().getClientSecret()).getBytes();
                SecretKey secret = new SecretKeySpec(decodedSecret, 0, decodedSecret.length, Algorithm.HS256);
                key.setSecretKey(secret);
                return new MacSignatureSignerContext(key);
            }
        }
        return new AsymmetricSignatureProvider(session, Algorithm.RS512).signer();
    }
}
