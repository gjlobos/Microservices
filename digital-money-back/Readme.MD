Gestión IAM con aplicación de terceros (Keycloak)
Archivo JSON de configuración del reino Digital-Money se adjunta en el raíz (realm-export-Digital-Money.json).
Archivo JSON de colección de Postman para pruebas se adjunta en el raíz (Proyecto integrador Final.postman_collection.json).
Verificar cuál es la Keycloak Base URL en Keycloak. Verificar si está la misma dirección en las líneas 133 y 181 del KeyCloakUserRepository.java. También verificar que coincida la dirección en las líneas 10, 11 y 39 del application.yml del user-service. Y también verificar que coincida la dirección en la línea 31 del OAuth2ResourceServerSecurityConfiguration.java.
Verificar cuál es el realm en Keycloak. Verificar si está el mismo realm en el application.yml del user-service en las líneas 10, 11 y 40. También se debe verificar en la línea 31 del OAuth2ResourceServerSecurityConfiguration.java del paquete security del user-service. También se debe verificar en la lína 134 y 182 del KeyCloakUserRepository.java. 
Verificar en Keycloak que no se haya modificado el secret del client_id. Si cambió, se debe actualizar el application.yml del user-service en las líneas 17 y 42. 
Verificar en Keycloak que no se haya modificado el client_id. Si cambió, se debe actualizar el application.yml del user-service en las líneas 9, 14, 15 y 41.
Se hace una modificación para probar el cicd
Se hace una modificación para probar el cicd
Se hace una modificación para probar el cicd