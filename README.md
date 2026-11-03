# CulturaApp - Backend (API REST)

API REST para la gestión de préstamos de implementos culturales
(Bienestar Universitario - Área de Cultura). Desarrollada con **Spring Boot**
(Java) y **MongoDB**.

## Requisitos previos

- **Java 17** o superior (`java -version` para verificar)
- **Maven** (o usar el wrapper `mvnw` si lo agregas desde tu IDE)
- **MongoDB** corriendo en tu máquina, o una base en **MongoDB Atlas**

## Instalar y correr MongoDB localmente (opción más simple)

1. Descarga MongoDB Community Server: https://www.mongodb.com/try/download/community
2. Instálalo y asegúrate de que el servicio quede corriendo (por defecto en el puerto `27017`).
3. No necesitas crear la base de datos ni las colecciones a mano: Spring Boot
   las crea automáticamente la primera vez que guardas un dato.

Si prefieres no instalar nada localmente, puedes crear una base gratuita en
[MongoDB Atlas](https://www.mongodb.com/cloud/atlas/register) y copiar la
cadena de conexión que te dan (`mongodb+srv://usuario:clave@...`).

## Configurar la conexión

Abre `src/main/resources/application.properties` y ajusta esta línea según tu caso:

```properties
spring.data.mongodb.uri=mongodb://localhost:27017/culturaapp
```

## Cómo correr el proyecto

### Opción 1: Desde un IDE (recomendado si usas IntelliJ o Eclipse)
1. Abre la carpeta `culturaapp-backend` como proyecto Maven.
2. Deja que descargue las dependencias (necesita internet la primera vez).
3. Ejecuta la clase `CulturaappBackendApplication`.

### Opción 2: Desde la terminal
```bash
cd culturaapp-backend
mvn spring-boot:run
```

Si todo funciona, verás un mensaje de Spring Boot indicando que la
aplicación inició en el puerto `8080`.

## Probar que funciona

Abre el navegador o Postman y visita:

```
GET http://localhost:8080/api/implementos
```

Debe responder `[]` (lista vacía) si aún no has creado implementos.

## Endpoints disponibles

### Implementos
| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/implementos` | Lista todos los implementos |
| GET | `/api/implementos?nombre=microfono` | Busca implementos por nombre (parcial, sin distinguir mayúsculas) |
| GET | `/api/implementos/{id}` | Obtiene un implemento por id |
| POST | `/api/implementos` | Crea un implemento |
| PUT | `/api/implementos/{id}` | Actualiza un implemento |
| DELETE | `/api/implementos/{id}` | Elimina un implemento |

Ejemplo de cuerpo para crear un implemento (POST). `codigo`, `ubicacion` y
`condicion` son opcionales: si no los envías, se autocompletan igual que en
la maqueta de Figma (código autogenerado tipo `IMP-001`, ubicación "Área de
Cultura" y condición "Bueno").
```json
{
  "nombre": "Micrófono inalámbrico",
  "tipo": "Audio",
  "descripcion": "Micrófono para presentaciones del grupo de teatro",
  "ubicacion": "Área de Cultura",
  "condicion": "Bueno"
}
```

### Préstamos
| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/prestamos` | Lista todos los préstamos |
| GET | `/api/prestamos?estado=activo` | Lista solo los préstamos activos |
| POST | `/api/prestamos` | Registra un préstamo (si el implemento está disponible) |
| PATCH | `/api/prestamos/{id}/devolver` | Registra la devolución de un préstamo |

Ejemplo de cuerpo para registrar un préstamo (POST). `fechaLimite` es
opcional: si no la envías, se calcula automáticamente como hoy + 3 días.
```json
{
  "implementoId": "<id de un implemento existente>",
  "responsable": "Juan Pérez",
  "fechaLimite": "2026-10-20T18:00:00"
}
```

### Estado "Vencido"

Cada vez que se consulta `GET /api/prestamos` (con o sin filtro), el
sistema revisa automáticamente los préstamos `ACTIVO` cuya `fechaLimite`
ya pasó y los marca como `VENCIDO` — igual que el badge rojo "Vencido" de
la pantalla "Préstamos del encargado" en Figma. Puedes filtrar con
`?estado=vencido` para ver solo esos.

## Regla de negocio implementada

No se puede registrar un préstamo si el implemento ya está en estado
`PRESTADO`. En ese caso la API responde con código **409 (Conflict)** y un
mensaje explicando el motivo. Al registrar la devolución, el implemento
vuelve automáticamente a `DISPONIBLE`.

## Códigos de respuesta usados

- `200 OK` - consulta exitosa
- `201 Created` - creación exitosa
- `204 No Content` - eliminación exitosa
- `400 Bad Request` - datos faltantes o inválidos
- `404 Not Found` - implemento o préstamo no encontrado
- `409 Conflict` - violación de la regla de negocio (implemento ya prestado, o préstamo ya devuelto)
- `500 Internal Server Error` - error inesperado del servidor

## Estructura del proyecto

```
src/main/java/com/culturaapp/backend/
├── CulturaappBackendApplication.java   (clase principal)
├── model/          (entidades Implemento y Prestamo)
├── repository/     (acceso a MongoDB)
├── service/        (lógica de negocio y validaciones)
├── controller/      (endpoints REST)
└── exception/       (manejo centralizado de errores)
```

## Colección de pruebas

En este mismo proyecto se incluye `culturaapp.postman_collection.json`,
listo para importar en Postman o Insomnia. Incluye todos los endpoints y
dos pruebas pensadas para demostrar la regla de negocio y el manejo de
errores (préstamo duplicado → 409, implemento inexistente → 404).

### Orden sugerido para probar
1. Crear un implemento (`POST /api/implementos`) y copiar el `id` que devuelve.
2. Pegar ese `id` en la variable `implementoId` de Postman.
3. Registrar un préstamo con ese `implementoId` (`POST /api/prestamos`) y copiar el `id` del préstamo.
4. Pegarlo en la variable `prestamoId`.
5. Intentar crear otro préstamo con el mismo `implementoId` → debe fallar con 409.
6. Devolver el préstamo (`PATCH /api/prestamos/{id}/devolver`).
7. Verificar que el implemento volvió a estar `DISPONIBLE` (`GET /api/implementos/{id}`).
