## POST /api/auth/register
Registra un nuevo usuario.

### Request Body:
```json
{
  "name": "John",
  "lastName": "Doe",
  "phoneNumber": "2222222" ,
  "email" : "example@gmail.com",
  "password" : "12345" ,
  "confirmPassword" : "12345"
}
```
### Responses
- 201 (Created)
  Devuelve un string "User registered successfully" en caso de ser creado

- 400 (Bad Request)
  "Invalid user details."