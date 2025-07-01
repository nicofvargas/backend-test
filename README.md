# 📘 API REST - Motor de Usuarios v1.0

> Documentación para desarrolladores frontend que deseen consumir la API del proyecto **backend-test**.

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
