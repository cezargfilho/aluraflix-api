# aluraflix-api

Alura Challange Back End

## Stack de tecnologias
| Nome          |Versão         |
| ------------- |:-------------:|
| Java          |   	11       |
| Spring Boot   |      2.5.2    |

### Comandos
**Para inicializar a API**
```sh
mvn exec:java -Dexec.mainClass="br.com.alura.aluraflix.AluraflixApplication"
```
**Para utilizar Docker**
```
mvn clean package -Dmaven.test.skip=true -Dspring.profiles.active=prod
docker build -t alura/aluraflix-api .
docker run -p 8080:8080 -e SPRING_PROFILES_ACTIVE='prod'
```


## Semana 1
Criação de um crud básico:

 - GET/videos
 - GET/videos/1
 - POST/videos
 - PUT/videos
 - DELETE/videos/1
 - Validação de campos
 - Validação de URL
 - Testes manuais com Postman

## Semana 2

 - GET/categorias
 - GET/categorias/1
 - GET/categorias/1/videos
 - POST/categorias
 - PUT/categorias
 - DELETE/categorias/1
 - Testes unitários
 - Testes de integração
