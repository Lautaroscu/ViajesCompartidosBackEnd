# User API Documentation

## GET /api/users
Obtiene una lista de todos los usuarios.

### Responses:

#### 200 (OK)
Devuelve una lista de objetos `OutputUserDTO` con los detalles de todos los usuarios.

---

## GET /api/users/{userId}
Obtiene los detalles de un usuario específico por su ID.

### Path Parameter:
- `userId`: ID del usuario a obtener.

### Responses:

#### 200 (OK)
Devuelve un objeto `OutputUserDTO` con los detalles del usuario.

#### 404 (Not Found)
- "User not found."

---



## PUT /api/users/{userId}
Actualiza los detalles de un usuario existente.

### Request Body:
```json
{
  "name": "John",
  "lastName": "Doe Edited",
  "phoneNumber": "1111111"
}
```
### Responses
- 201 (Created)
  Devuelve el objeto OutputUserDTO actualizado con los detalles del usuario.

- 400 (Bad Request)
  "Invalid user details."
- 404 (Not Found)
  "User not found."

## DELETE /api/users/{userId}
Elimina un usuario existente.
### Responses
- 200 (Ok)
  Devuelve el objeto OutputUserDTO eliminado con los detalles del usuario.

- 404 (Not Found)
  "User not found."




