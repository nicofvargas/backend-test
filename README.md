# 📘 API REST - Motor de Usuarios v1.0

> Documentación para desarrolladores frontend que deseen consumir la API del proyecto **backend-test**.
> Este motor de usuarios es una plantilla inicial para reutilizarse en distintos proyectos.
### Tecnologías Principales

*   **Lenguaje:** Java 21
*   **Framework Principal:** Spring Boot 3
*   **Acceso a Datos:** Spring Data JPA
*   **Implementación de JPA:** Hibernate
*   **Base de Datos:** PostgreSQL (para persistencia) y H2 (para pruebas iniciales)
*   **Gestor de Dependencias y Construcción:** Apache Maven
### Arquitectura y Patrones de Diseño

*   **Arquitectura General:** Monolito bien estructurado con el patrón **Package by Feature**, que organiza el código en módulos de negocio independientes (`usuario`, `auth`, etc.) para máxima cohesión y escalabilidad.
*   **Capas de la Aplicación:** Diseño clásico de 3 capas:
    *   **Capa de Controlador (`@RestController`):** Expone los endpoints REST y maneja las peticiones/respuestas HTTP.
    *   **Capa de Servicio (`@Service`):** Contiene toda la lógica de negocio, desacoplada de la web y de los datos.
    *   **Capa de Repositorio (`@Repository`):** Abstrae el acceso a la base de datos a través de interfaces de Spring Data JPA.
*   **Patrón DTO (Data Transfer Object):** Se utiliza para desacoplar la API de la estructura interna de la base de datos, con DTOs específicos para peticiones (`Request`) y respuestas (`Response`).

### Características de Seguridad Implementadas

La seguridad es un pilar fundamental de este proyecto, implementada con **Spring Security**.

*   **Autenticación Basada en Tokens (JWT):**
    *   Se utiliza el estándar **JSON Web Tokens** para una autenticación `stateless` (sin estado), ideal para APIs consumidas por frontends modernos (SPAs) y aplicaciones móviles.
    *   La librería `jjwt` se encarga de la creación y validación de los tokens.
    *   Se ha implementado un endpoint de login (`POST /api/auth/login`) que, tras validar las credenciales, emite un token JWT firmado con una clave secreta.

*   **Autorización Basada en Roles:**
    *   El acceso a los endpoints está controlado por roles (`USER`, `ADMIN`).
    *   La configuración de seguridad (`SecurityConfig`) define de manera granular qué roles pueden acceder a qué endpoints y con qué métodos HTTP.

*   **Cifrado de Contraseñas:**
    *   Las contraseñas de los usuarios **nunca** se almacenan en texto plano. Se utiliza el algoritmo **BCrypt** (`BCryptPasswordEncoder`) para hashearlas de forma segura antes de persistirlas en la base de datos.

### Ciclo de Vida Completo de la Cuenta de Usuario

Se ha implementado un "motor de usuarios" completo y reutilizable que gestiona todo el ciclo de vida de una cuenta:

*   **Registro Seguro:** Un endpoint público permite el registro de nuevos usuarios, asignando por defecto el rol `USER`.
*   **Verificación de Email:** Las nuevas cuentas se crean en estado `PENDIENTE_VERIFICACION` y se debe usar un token (simulado por consola) para activarlas antes de poder iniciar sesión.
*   **Recuperación de Contraseña:** Un flujo completo de "he olvidado mi contraseña" que permite a los usuarios solicitar un token de reseteo y establecer una nueva contraseña de forma segura.
*   **Gestión de Estado de Cuenta:** Los administradores pueden `BLOQUEAR` o `ACTIVAR` cuentas de usuario, impidiendo o permitiendo su acceso al sistema.

### Calidad y Robustez de la API

*   **Manejo de Errores Global:** Un `GlobalExceptionHandler` centralizado captura las excepciones de la aplicación y devuelve respuestas de error en formato JSON consistentes y con los códigos de estado HTTP apropiados (`404`, `400`, `401`, `403`, `409`).
*   **Validación de Datos:** Se utiliza `jakarta.validation` (`@Valid`, `@NotBlank`, etc.) en los DTOs de petición para validar los datos de entrada antes de que lleguen a la lógica de negocio.
*   **Paginación y Ordenamiento:** Los endpoints que devuelven listas soportan paginación (`page`, `size`) y ordenamiento (`sort`) para manejar grandes volúmenes de datos de manera eficiente.
---

## 🧩 Índice

1. [Conceptos Generales](#conceptos-generales)
2. [Flujo de Autenticación y Registro](#flujo-de-autenticación-y-registro)
3. [Gestión de Perfil](#gestión-de-perfil)
4. [Administración de Usuarios (Admin)](#administración-de-usuarios-admin)
5. [Errores](#errores)
6. [Tips para Frontend](#tips-para-frontend)
7. [Contacto](#contacto)

---

## 📌 Conceptos Generales

- **URL Base:**  
  ```
  http://localhost:8080
  ```

- **Formato de Datos:**  
  Todas las peticiones y respuestas usan `JSON`.  
  Las peticiones con cuerpo deben tener el header:
  ```
  Content-Type: application/json
  ```

- **Autenticación:**  
  La mayoría de los endpoints están protegidos con **JWT Bearer Token**.  
  Flujo:
  1. Iniciar sesión con `POST /api/auth/login`.
  2. Recibir el token.
  3. Incluirlo en los headers:
     ```
     Authorization: Bearer <token>
     ```

---

## 🔐 Flujo de Autenticación y Registro

### ✅ Registrar un nuevo usuario

- **POST** `/api/v1/usuarios`
- **Auth:** ❌ No requiere
- **Body:**
```json
{
  "username": "nuevo_usuario",
  "password": "contraseña",
  "email": "usuario@usuario.com"
}
```
- **Response (201):**
```json
{
  "id": 1,
  "username": "nuevo_usuario",
  "role": "USER"
}
```

### ✅ Verificar cuenta de email

- **GET** `/api/auth/verify?token=<token>`
- **Auth:** ❌ No requiere
- **Response (200):**
```json
{
  "success": true,
  "message": "¡Cuenta verificada exitosamente! Ahora puedes iniciar sesión."
}
```

### 🔓 Iniciar sesión (Login)

- **POST** `/api/auth/login`
- **Auth:** ❌ No requiere
- **Body:**
```json
{
  "username": "nuevo_usuario",
  "password": "contraseña"
}
```
- **Response (200):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

### 🔁 Solicitar restablecimiento de contraseña

- **POST** `/api/auth/forgot-password`
- **Auth:** ❌ No requiere
- **Body:**
```json
{
  "email": "usuario@usuario.com"
}
```
- **Response (200):**
```json
{
  "success": true,
  "message": "Se ha enviado un token de reseteo."
}
```

### 🔁 Restablecer la contraseña

- **POST** `/api/auth/reset-password`
- **Auth:** ❌ No requiere
- **Body:**
```json
{
  "token": "token_de_reseteo",
  "newPassword": "nuevacontraseña"
}
```
- **Response (200):**
```json
{
  "success": true,
  "message": "La contraseña ha sido restablecida exitosamente."
}
```

---

## 👤 Gestión de Perfil

### 📄 Obtener mi perfil

- **GET** `/api/auth/me`
- **Auth:** ✅ Requiere Bearer Token
- **Response (200):**
```json
{
  "id": 1,
  "username": "nuevo_usuario",
  "role": "USER"
}
```

### 🔑 Cambiar mi contraseña

- **PUT** `/api/auth/change-password`
- **Auth:** ✅ Requiere Bearer Token de ADMIN
- **Body:**
```json
{
  "oldPassword": "actual123",
  "newPassword": "nueva456"
}
```
- **Response (200):**
```json
{
  "success": true,
  "message": "La contraseña ha sido actualizada exitosamente."
}
```

---

## 🛠️ Administración de Usuarios (Admin)

> Estos endpoints requieren un usuario con el rol `ADMIN`.

### 📋 Listar todos los usuarios

- **GET** `/api/v1/usuarios`
- **Auth:** ✅ Bearer Token de Admin
- **Query Params:** `page`, `size`, `sort`

### 🔄 Cambiar el rol de un usuario

- **PUT** `/api/v1/usuarios/{id}/role`
- **Auth:** ✅ Admin
- **Body:**
```json
{
  "newRole": "ADMIN"
}
```

### 🔒 Cambiar estado de un usuario

- **PUT** `/api/v1/usuarios/{id}/status`
- **Auth:** ✅ Admin
- **Body:**
```json
{
  "newState": "BLOQUEADO"
}
```
> Estados válidos: `ACTIVO`, `BLOQUEADO`

### 🗑️ Eliminar usuario

- **DELETE** `/api/v1/usuarios/{id}`
- **Auth:** ✅ Admin
- **Response:** `204 No Content`

---

## ⚠️ Errores

Todas las respuestas de error tienen formato JSON:

```json
{
  "timestamp": "2025-06-30T10:00:00.123456",
  "status": 404,
  "error": "Not Found",
  "message": "Mensaje descriptivo del error.",
  "path": "/ruta/del/endpoint"
}
```

---

## 💡 Tips para Frontend

- Usar POSTMAN para comprobar los response.
- Verificar expiración del token (si está implementada).
- Manejar el error 401 para redirigir al login.
- Usar el token JWT en los headers para endpoints protegidos.

---

## 📬 Contacto

Proyecto desarrollado por [@nicofvargas](https://github.com/nicofvargas).  

---
