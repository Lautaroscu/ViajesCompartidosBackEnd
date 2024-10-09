# TripController API Documentation

Este controlador maneja las operaciones relacionadas con los viajes y pasajeros.

## Base URL

---

## GET `/api/trips`
Obtiene la lista de todos los viajes.

- **Respuesta exitosa (200)**: Devuelve una lista de objetos `OutputTripDTO`.
- **Formato de respuesta**:
  ```json
  [
    {
      "id": 1,
      "origin": "Tandil",
      "destination": "Juarez",
      "date": "2024-08-12T17:30:00",
      "maxPassengers": 4,
      "countPassengers": 2
    } ,
    {
      "id": 2,
      "origin": "Juarez",
      "destination": "Tandil",
      "date": "2024-08-12T17:30:00",
      "maxPassengers": 3,
      "countPassengers": 0
    }
  ]

## GET `/api/trips/{tripId}`
Obtiene la informacion de un viaje en especifico dado un ID.

- **Respuesta exitosa (200)**: Devuelve un unico objeto `OutputTripDTO`.
- **Formato de respuesta**:
  ```json
  [
    {
      "id": 1,
      "origin": "Tandil",
      "destination": "Juarez",
      "date": "2024-08-12T17:30:00",
      "maxPassengers": 4,
      "countPassengers": 2
    }
  ]

## GET `/api/trips/user/{userId}?rol=owner`

## GET `/api/trips/user/{userId}?rol=passenger`

## Descripción
Este endpoint permite recuperar una lista de viajes (trips) dependiendo el rol del usuario en el viaje. Es útil para obtener todos los viajes creados por un usuario específico. O
tambien, todos los vijes en los que el usuario forme el rol de pasajero.

## Parámetros de Ruta
- **`userId`** (int): El ID del usuario (propietario) para el cual se desean recuperar los viajes.
- **`rol`** (string)
## Respuesta
### Código de Estado
- **200 OK**: Se devuelve una lista de objetos `Trip` en caso de que existan viajes asociados al `userId` proporcionado.
- **404 Not Found**: Si no se encuentran viajes asociados al `userId`. O bien no se proporcione el rol correctamente.

### Cuerpo de la Respuesta
```json
[
  {
    "tripId": 1,
    "origin": "Ciudad A",
    "destination": "Ciudad B",
    "date": "2024-10-01T12:00:00",
    "maxPassengers": 4,
    "countPassengers": 2,
    "owner": {
      "userId": 1,
      "firstName": "Juan",
      "lastName": "Pérez"
    },
    "passengers": [
      {
        "userId": 2,
        "firstName": "Ana",
        "lastName": "Gómez"
      }
    ]
  }
]
```

## POST /api/trips/passengers
Agrega un pasajero a un viaje.

### Request Body:
```json
{
  "tripID": 1,
  "passengerID": 10
}
```
### Responses:
- 201 (Created)
Devuelve un objeto OutputTripPassengerDTO con los detalles del pasajero y el viaje.

- 400 (Bad Request)
"The passenger is already on the trip."
"Trip is full, cannot add more passengers."

- 404 (Not Found)
"User not found."
"Trip not found."

- 500 (Internal Server Error)
"Unexpected error occurred."

## PUT /api/trips/{tripId}
  Actualiza los detalles de un viaje existente.

### Request Body:
```json
{
  "origin": "Tandil",
  "destination": "Juarez",
  "date": "2024-08-12T17:30:00",
  "ownerId": 1,
  "maxPassengers": 4
}
```
#### Path Parameter:
- tripId: ID del viaje a actualizar.
### Responses:
- 201 (Created)
  Devuelve el objeto OutputTripDTO actualizado con los detalles del viaje.

- 400 (Bad Request)
  "Invalid trip details."
- 404 (Not Found)
  "Trip not found."

## DELETE /api/trips/{tripId}
Elimina un viaje dado un id

### Path Parameter:
- tripId: ID del viaje a eliminar.
### Responses:
- 200 (Ok)
- 404 (Not Found)
  "Trip not found."


## DELETE /api/trips/passengers/{tripId}/{userId}
Elimina un pasajero de un viaje, dado un id de viaje, y un id usuario

### Path Parameter:
- tripId: ID del viaje 
- userId: ID del usuario a eliminar del viaje.
### Responses:
- 200 (Ok)
- 404 (Not Found)
  "The passenger is not on the trip."






