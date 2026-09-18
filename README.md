# TV Maze Middleware API

API middleware que consume los servicios de [TV Maze](https://www.tvmaze.com/api) y
expone endpoints REST para búsqueda de shows, obtención por ID, y gestión de
comentarios/calificaciones persistidos en MongoDB Atlas.

---

## Stack Tecnológico

| Tecnología | Versión | Uso |
|------------|---------|-----|
| Java | 17 | Lenguaje base |
| Spring Boot | 3.2.5 | Framework principal |
| Spring WebFlux | — | API reactiva no bloqueante |
| Spring Data MongoDB Reactive | — | Persistencia reactiva |
| MongoDB Atlas | — | Base de datos en la nube |
| WebClient | — | Cliente HTTP reactivo para TV Maze |
| Maven | — | Gestión de dependencias |


---

## Arquitectura

El proyecto sigue **Arquitectura Hexagonal (Puertos y Adaptadores)**

---

## Cómo Ejecutar el Proyecto

### Requisitos previos

- **Java 17** o superior
- **Maven 3.8+**
- **Git**
- Una cuenta gratuita en [MongoDB Atlas](https://www.mongodb.com/cloud/atlas)

---

### Paso 1 — Clonar el repositorio

```bash
git clone <url-del-repo>
cd APITvMaze
```

---

### Paso 2 — Crear tu propio cluster en MongoDB Atlas

El proyecto **NO incluye credenciales reales**. Cada persona que ejecute la app debe
crear su propio cluster gratuito en MongoDB Atlas y usar su propia cadena de conexión.

**Pasos:**

1. Regístrate gratis en [MongoDB Atlas](https://www.mongodb.com/cloud/atlas).
2. Crea un **cluster gratuito (M0)**.
3. En el menú izquierdo, entra a **Network Access** y agrega la IP `0.0.0.0/0`
   (permite acceso desde cualquier IP — válido para desarrollo/pruebas).
4. En **Database Access**, crea un usuario de base de datos con permisos de
   **Read and write to any database**.
5. En **Clusters** → haz clic en **Connect** → **Drivers** → copia la
   **connection string**. Se verá así:

   ```
   mongodb+srv://<usuario>:<password>@<tu-cluster>.xxxxx.mongodb.net/?retryWrites=true&w=majority
   ```

6. Reemplaza `<usuario>` y `<password>` por los que creaste en el paso 4, y
   agrega la base de datos `tvmaze` antes de los parámetros `?`:

   ```
   mongodb+srv://<usuario>:<password>@<tu-cluster>.xxxxx.mongodb.net/tvmaze?retryWrites=true&w=majority
   ```

---

### Paso 3 — Setear la variable de entorno `MONGO_URI`

Abre una **terminal** y setea la variable `MONGO_URI` con **tu propia**
cadena de conexión. **La terminal debe ser la misma desde la que correrás el proyecto.**

####  Windows CMD

```cmd
set MONGO_URI=mongodb+srv://<usuario>:<password>@<tu-cluster>.xxxxx.mongodb.net/tvmaze?retryWrites=true&w=majority
```

####  Windows PowerShell

```powershell
$env:MONGO_URI="mongodb+srv://<usuario>:<password>@<tu-cluster>.xxxxx.mongodb.net/tvmaze?retryWrites=true&w=majority"
```

#### Linux /  macOS (bash / zsh)

```bash
export MONGO_URI="mongodb+srv://<usuario>:<password>@<tu-cluster>.xxxxx.mongodb.net/tvmaze?retryWrites=true&w=majority"
```

**Reemplaza:**
- `<usuario>` → tu usuario de MongoDB Atlas
- `<password>` → tu contraseña
- `<tu-cluster>` → el subdominio de tu cluster (ej. `cluster0.abc123`)

**Verificar que se seteó correctamente:**

| Sistema | Comando |
|---------|---------|
| Windows CMD | `echo %MONGO_URI%` |
| PowerShell | `echo $env:MONGO_URI` |
| Linux / macOS | `echo $MONGO_URI` |

Debe imprimir tu cadena de conexión. Si imprime vacío, la variable no se seteó.

>  **Nota:** la variable de entorno es **temporal** y solo vive mientras la
> terminal esté abierta. Si cierras la consola, debes volver a setearla.
> Por eso es importante correr el paso 4 en la **misma terminal**.

---

### Paso 4 — Compilar y ejecutar (en la misma terminal)

Desde la **misma terminal** donde seteaste `MONGO_URI`:

```bash
mvn clean install
mvn spring-boot:run
```



La aplicación arranca en **`http://localhost:8080`**.

**Salida esperada (logs):**

```
INFO ... Netty started on port 8080
INFO ... Started ApiTvMaze in X.XXX seconds
```

Si ves esos dos mensajes, todo está funcionando.

---

### Paso 5 — Probar los endpoints

##  Endpoints

###  Probar la API — Swagger UI

Una vez que la aplicación esté corriendo, abre en tu navegador:

** http://localhost:8080/swagger-ui.html**

Desde ahí puedes:

- Ver **todos los endpoints** con sus parámetros y schemas.
- Probar cada endpoint **directamente desde el navegador** (botón **"Try it out"**).
- **Cero problemas** con `curl`, escapes de PowerShell, CMD, etc.

---

### Endpoints disponibles

| Método | Ruta | Descripción |
|--------|------|-------------|
| `GET` | `/api/shows/search?q={query}` | Buscar shows por criterio (incluye comentarios) |
| `GET` | `/api/shows/{showId}` | Obtener show por ID (con cache en MongoDB) |
| `POST` | `/api/comments` | Guardar comentario + rating (0-5) |

>  Los detalles de cada endpoint (request/response) están documentados
> automáticamente en **Swagger UI**.



---


### ¿Por qué no incluyo mi propia URI?

El proyecto **no incluye credenciales reales** en el repositorio por buenas
prácticas de seguridad. Cada ejecutor debe crear **su propio cluster gratuito**
en MongoDB Atlas (toma ~10 minutos) y usar su propia cadena de conexión.
