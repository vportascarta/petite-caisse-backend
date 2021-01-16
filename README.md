# Faire un build du backend

Conseiller de le faire sur Linux ou WSL

1. Installer DOCKER, JAVA 11, et MAVEN.
1. Ouvrir le projet dans VSCode et installer toutes les extensions conseillées.
1. Ouvrir le Command Palette (CTRL+SHIFT+A) et exécuter `Maven: Execute Command...` puis sélectionner package.
1. Le fichier JAR se situe dans le dossier `target`

# Faire l'image Docker

1. Une fois le fichier JAR créé, faire un `docker build -t petite-caisse-backend .`
1. Faire ensuite le `docker tag ...` et le `docker push ...` selon vos configurations


# Exemple de fichier de propriétés : `application.yml`

```yaml
app:
  config:
    fronendOrigin: http://localhost:3000
  auth:
    tokenSecret: <32 chars Base64 string>
    tokenExpirationMsec: 604800000
    authorizedApiToken:
        - <64 chars Base64 string>

spring:
  jpa:
    hibernate:
      ddl-auto: update
  datasource:
    url: jdbc:mysql://<host BD>:<port>/<nom BD>
    username: <user BD>
    password: <mot de passe BD>

server:
  port: <port du serveur>
```
