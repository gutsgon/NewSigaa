# API SIGAA

Uma API RESTful desenvolvida com Spring Boot para o sistema SIGAA do Instituto Federal de Sergipe (IFS), com autenticação e autorização via Keycloak e persistência usando Microsoft SQL Server (produção) e H2 (testes).

## Índice

- [Tecnologias](#tecnologias)
- [Pré-requisitos](#pré-requisitos)
- [Setup do Banco de Dados](#setup-do-banco-de-dados)
- [Configuração do Keycloak](#configuração-do-keycloak)
- [Instalação](#instalação)
- [Configuração de Perfis](#configuração-de-perfis)
- [Build e Execução](#build-e-execução)
- [Testes](#testes)
- [Documentação da API](#documentação-da-api)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Licença](#licença)

## Tecnologias

- Java 17+
- Maven
- Spring Boot 3.2
- Spring Data JPA
- Keycloak Spring Boot Adapter 25.0.3
- Microsoft SQL Server (produção)
- H2 Database (testes)
- Swagger (OpenAPI)

## Pré-requisitos

- JDK 17 ou superior instalado e configurado (`java -version`).
- Maven instalado ou use o wrapper included (`./mvnw`).
- Microsoft SQL Server (ou Docker) para ambiente de produção.
- Keycloak (você pode usar Docker ou instalação local).
- Opcional: ferramentas para executar scripts SQL (SSMS, Azure Data Studio, etc.).

## Setup do Banco de Dados

1. Crie uma instância do SQL Server (ex: `localhost\sqlexpress01`).
2. Crie um banco de dados chamado `NewSigaa`.
3. Crie um usuário `usrNewSigaa` e defina uma senha segura.
4. Conceda as permissões necessárias ao usuário para CRUD nas tabelas.

> **Nota**: As configurações default de conexão ficam em `src/main/resources/application-sqlserver.properties`. Ajuste `spring.datasource.url`, `username` e `password` conforme sua instância.

## Configuração do Keycloak

1. Inicie um servidor Keycloak (ex: `docker run -p 8081:8080 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin quay.io/keycloak/keycloak:25.0.3 start-dev`).
2. Acesse o console (http://localhost:8081) e faça login como `admin`.
3. Crie um Realm chamado `ifs`.
4. Dentro do Realm, crie um Client com as seguintes definições:
   - **Client ID**: `newsigaa-back`
   - **Client Protocol**: `openid-connect`
   - **Access Type**: `confidential`
   - **Valid Redirect URIs**: `*`
   - Salve e anote o **Secret** gerado.
5. Atualize o arquivo `src/main/resources/policy-enforcer.json` com os dados do seu Realm e Client (campo `realm`, `auth-server-url`, `resource` e `credentials.secret`).

## Instalação

```bash
# Clone o repositório
git clone https://github.com/seu-usuario/NewSigaa.git
cd NewSigaa/api-new-sigaa
```

## Configuração de Perfis

A aplicação usa Spring Profiles para alternar entre banco de produção (`sqlserver`) e H2 (`h2`).

- **Produção (SQL Server)**: definido por `spring.profiles.active=sqlserver` em `application.properties`.
- **Testes (H2)**: use `-Dspring.profiles.active=h2` ou altere `application.properties` para `spring.profiles.active=h2`.

## Build e Execução

### Usando o Wrapper do Maven

```bash
# Compilar
docker-compose run --rm api mvnw clean package

# Executar
./mvnw spring-boot:run
``` 

### Executando o JAR Gerado

```bash
mvn clean package
java -jar target/api-new-sigaa-1.0.jar
```

A API estará disponível em: `http://localhost:8080/api/newsigaa`

## Testes

Para executar testes integrados com H2:

```bash
mvn test -Dspring.profiles.active=h2
```

## Documentação da API

A documentação Swagger (OpenAPI) é gerada automaticamente e pode ser acessada em:

```
http://localhost:8080/api/newsigaa/swagger-ui/index.html
http://localhost:8080/api/newsigaa/v3/api-docs
```

## Estrutura do Projeto

```
api-new-sigaa/
├── src/main/java    # Código-fonte Java
│   ├── config       # Configurações (Swagger, ModelMapper, Security)
│   ├── rest         # Controllers e DTOs
│   ├── service      # Regras de negócio
│   └── exception    # Tratamento de exceções
├── src/main/resources
│   ├── application-*.properties  # Perfis de configuração
│   └── policy-enforcer.json      # Config Keycloak
└── pom.xml
```

## Licença

Este projeto está licenciado sob a [MIT License](LICENSE).
