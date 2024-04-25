# User Auth Service API Controllers Documentation

Este projeto é construído em Java utilizando o framework Spring Boot, com segurança reforçada pelo Spring Security e integração com JWT e cookies para autenticação stateless. Ele emprega JUnit para garantir uma cobertura de testes das principais funcionalidades, assegurando a confiabilidade do sistema.

Desenvolvido inicialmente com SQL Server, a aplicação é facilmente adaptável a outros sistemas de gerenciamento de banco de dados devido ao uso do JPA, que abstrai a camada de acesso a dados e facilita a portabilidade entre diferentes bancos.

A arquitetura do sistema segue o padrão MVC, com ênfase em clean code e a implementação de interfaces para uma melhor abstração e desacoplamento dos serviços. Essas práticas permitem uma manutenção e escalabilidade mais eficientes, adequando-se às necessidades de projetos que demandam robustez e flexibilidade.

## Auth Controller

### Authenticate User

- **Endpoint**: `POST /auth/login`
- **Request Body**: `UserLoginDTO`

  | Field        | Type     | Description      |
  |--------------|----------|------------------|
  | **username** | `String` | User's username. |
  | **password** | `String` | User's password. |

- **Responses**:
    - `200 OK`: User logged successfully.

### Register User

- **Endpoint**: `POST /auth/register`
  **Request Body**: `UserRegisterDTO`

  | Field         | Type     | Description                                 |
  |---------------|----------|---------------------------------------------|
  | **username**  | `String` | New user's username.                        |
  | **password**  | `String` | New user's password.                        |
  | **roleName**  | `String` | Role name assigned to the user.             |
  | **personID**  | `Long`   | ID of the person associated with this user. |

- **Responses**:
    - `201 Created`: User registered successfully.

### Register User Full

Registers a new user along with their full details in the system.

- **Endpoint**: `POST /auth/register/full`
  **Request Body**: `UserFullRegisterDTO`

  | Field         | Type                    | Description                       |
  |---------------|-------------------------|-----------------------------------|
  | **username**  | `String`                | New user's username.              |
  | **password**  | `String`                | New user's password.              |
  | **roleName**  | `String`                | Role name assigned to the user.   |
  | **person**    | `PersonModificationDTO` | Personal details of the new user. |

- **Responses**:
    - `201 Created`: User with full details successfully registered. Includes `UserDTO` structure in response.

## Person Controller

### Register Person

- **Endpoint**: `POST /person/register`
- **Request Body**: `PersonModificationDTO`

  | Field           | Type     | Description                       |
  |-----------------|----------|-----------------------------------|
  | **name**        | `String` | The person's name.                |
  | **cpf**         | `String` | The person's CPF.                 |
  | **email**       | `String` | The person's email address.       |
  | **dateOfBirth** | `Date`   | The person's date of birth.       |

- **Responses**:
    - `201 Created`: Person successfully registered.

### Update Person

- **Endpoint**: `PATCH /person/{id}`
- **Path Variable**:
    - `id` (Long): The ID of the person to update.
- **Request Body**: `PersonModificationDTO`

  | Field        | Type     | Description                          |
  |--------------|----------|--------------------------------------|
  | name         | `String` | Updated name of the person.          |
  | cpf          | `String` | Updated CPF of the person.           |
  | email        | `String` | Updated email address of the person. |
  | dateOfBirth  | `Date`   | Updated date of birth of the person. |

- **Responses**:
    - `202 Accepted`: Person successfully updated.

### Get Person

- **Endpoint**: `GET /person/{id}`
- **Path Variable**:
    - `id` (Long): The ID of the person to retrieve.
- **Responses**:
    - `200 OK`: Successfully retrieved the person details.

### Get Person List

- **Endpoint**: `GET /person/list`
- **Query Parameters**: `PersonGetDTO`

| Field | Type      | Description                                                    |
|-------|-----------|----------------------------------------------------------------|
| name  | `String`  | Filter by the person's name.                                   |
| cpf   | `String`  | Filter by the person's CPF.                                    |
| email | `String`  | Filter by the person's email address.                          |
| page  | `Integer` | Page number for pagination (default is 0).                     |
| size  | `Integer` | Number of records per page for pagination (default is 10).     |
| sort  | `String`  | Sorting criteria in the format: asc or desc (default is desc). |

- **Responses**:
    - `200 OK`: Successfully retrieved a list of persons matching the filters. The response includes pagination
      information and a list of persons, each with the following structure:

```json
{
  "content": [
    {
      "id": "Long",
      "name": "String",
      "cpf": "String",
      "email": "String"
    },
    ...
  ],
  "pageable": {
    "sort": {
      "sorted": true,
      "unsorted": false,
      "empty": false
    },
    "offset": "Long",
    "pageSize": "Integer",
    "pageNumber": "Integer",
    "paged": true,
    "unpaged": false
  },
  "totalPages": "Integer",
  "totalElements": "Long",
  "last": true,
  "size": "Integer",
  "number": "Integer",
  "sort": {
    "sorted": true,
    "unsorted": false,
    "empty": false
  },
  "numberOfElements": "Integer",
  "first": true,
  "empty": false
}
```

## User Controller

### Update User

Updates an existing user's information.

- **Endpoint**: `PATCH /user/{id}`
- **Path Variable**:
    - `id` (Long): The ID of the user to update.
- **Request Body**: `UserModificationDTO`

  | Field       | Type                    | Description                         |
  |-------------|-------------------------|-------------------------------------|
  | username    | `String`                | Updated username of the user.       |
  | roleName    | `String`                | Updated role name of the user.      |
  | enabled     | `Boolean`               | Updated status of the user account. |
  | person      | `PersonModificationDTO` | Person details for the user.        |

- **Responses**:
    - `202 Accepted`: User successfully updated.

### Get User

Retrieves details of a specific user by their ID.

- **Endpoint**: `GET /user/{id}`
- **Path Variable**:
    - `id` (Long): The ID of the user to retrieve.

- **Responses**:
    - `200 OK`: Successfully retrieved the user details.

### Get User List

Retrieves a list of users based on filters.

- **Endpoint**: `GET /user/list`
- **Query Parameters**: `UserGetDTO`

  | Field      | Type      | Description                                                     |
  |------------|-----------|-----------------------------------------------------------------|
  | username   | `String`  |  Filter by the user's username.                                 |
  | enabled    | `Boolean` |  Filter by the user's status.                                   |
  | roleName   | `String`  |  Filter by the user's role name.                                |
  | name       | `String`  |  Filter by the person's name associated with the user.          |
  | cpf        | `String`  |  Filter by the person's CPF associated with the user.           |
  | email      | `String`  |  Filter by the person's email address associated with the user. |
  | page       | `Integer` |  Page number for pagination (default is 0).                     |
  | size       | `Integer` |  Number of records per page for pagination (default is 10).     |
  | sort       | `String`  |  Sorting criteria in the format: asc or desc (default is desc). |

- **Responses**:
    - `200 OK`: Successfully retrieved a list of users matching the filters. The response includes pagination
      information and a list of users, each with the following structure:

      ```json
      {
      "content": [
      {
      "id": Long,
      "username": "String",
      "roleName": "String",
      "enabled": Boolean,
      "person": {
      "id": Long,
      "name": "String",
      "cpf": "String",
      "email": "String"
      }
      },
      ...
      ],
      "pageable": {
      "sort": {
      "sorted": Boolean,
      "unsorted": Boolean,
      "empty": Boolean
      },
      "offset": Long,
      "pageSize": Integer,
      "pageNumber": Integer,
      "paged": Boolean,
      "unpaged": Boolean
      },
      "totalPages": Integer,
      "totalElements": Long,
      "last": Boolean,
      "size": Integer,
      "number": Integer,
      "sort": {
      "sorted": Boolean,
      "unsorted": Boolean,
      "empty": Boolean
      },
      "numberOfElements": Integer,
      "first": Boolean,
      "empty": Boolean
      }
      ```

## UserRole Controller

### Register User Role

Creates a new user role in the system.

- **Endpoint**: `POST /userRole/register`
- **Request Body**: `UserRoleModificationDTO`

  | Field        | Type     | Description                     |
  |--------------|----------|---------------------------------|
  | **roleName** | `String` | The name of the user role.      |

- **Responses**:
    - `201 Created`: User role successfully registered.

### Update User Role

Updates an existing user role's information.

- **Endpoint**: `PATCH /userRole/{id}`
- **Path Variable**:
    - `id` (Long): The ID of the user role to update.
- **Request Body**: `UserRoleModificationDTO`

  | Field     | Type     | Description                      |
  |-----------|----------|----------------------------------|
  | roleName  | `String` | Updated name of the user role.   |

- **Responses**:
    - `202 Accepted`: User role successfully updated.

### Get User Role

Retrieves details of a specific user role by their ID.

- **Endpoint**: `GET /userRole/{id}`
- **Path Variable**:
    - `id` (Long): The ID of the user role to retrieve.

- **Responses**:
    - `200 OK`: Successfully retrieved the user role details.

### Get User Role List

Retrieves a list of all user roles in the system.

- **Endpoint**: `GET /userRole/list`

- **Responses**:
    - `200 OK`: Successfully retrieved a list of all user roles.
