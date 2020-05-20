# keycloak-nhslogin

Keycloak Social Login extension for NHS Login.

The default OIDC connect provider in Keycloak is currently fixed to using an RSA-256 signing key.  

This provider is a simple extension to the built in OIDC provider to use an RSA-512 key as required by NHS Login.  

## Source Build

Clone this repository and run `mvn package`.  

Then deploy the ear into $KEYCLOAK_HOME/standalone/deployments/ directory

You can find `keycloak-nhslogin-ear-<version>.ear` under `ear/target` directory.

## Setup

### NHS Login  

There is an on-boarding process for NHS Login which will be documented here shortly.  

Before on-boarding has started and you've been given access to the sandpit environment the online mock environment can be used:  

https://oidc.mock.signin.nhs.uk/client  

This online mock requires no setup or registration.  

### Keycloak

- Import/Create an RSA512 signing key for the realm you wish to configure.  
The private key for the mock environment can be found in the guidance section here:  
https://oidc.mock.signin.nhs.uk/client

- Add NHS Login Identity Provider to the realm which you want to configure. It will be listed under the Social section.

- Configure the provider using the well known configuration for the environbment being used.  
The configuration for the mock can be found at the url:  

https://oidc.mock.signin.nhs.uk/.well-known/openid-configuration


## Licence

[Apache License, Version 2.0](https://www.apache.org/licenses/LICENSE-2.0)


## Credits

- [Hiroyuki Wada](https://github.com/wadahiro) for the Discord Keycloak plugin that this project was based on.



