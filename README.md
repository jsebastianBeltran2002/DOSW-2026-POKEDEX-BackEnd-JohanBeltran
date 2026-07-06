# Megadex API 

### Autor: Johan Sebastian Beltran Gutierrez
### Asignatura: Desarrollo y Operaciones de Software (DOSW) · 2026 Intersemestral

---

## Descripción del Proyecto

**Megadex** es una API REST robusta diseñada para entrenadores y entusiastas del ecosistema Pokémon. La plataforma permite la exploración en profundidad, búsqueda avanzada, filtrado dinámico y comparación analítica de criaturas, además de ofrecer herramientas para la gestión de equipos enfocadas en el análisis competitivo, marcas de favoritos y monitoreo de estadísticas de uso global del catálogo.

El sistema implementa un estricto control de accesos basado en tres roles fundamentales:
* **Visitante:** Consultas básicas del catálogo público.
* **Usuario estándar:** Gestión de equipos personalizados, favoritos y análisis avanzado.
* **Administrador:** Control total sobre el catálogo, gestión de usuarios y auditoría del sistema.

La seguridad implementa un esquema dual de autenticación: mediante credenciales nativas del sistema (Username/Password) y de manera externa utilizando **Gmail (OAuth2)**.

---

## 🛠Stack Tecnológico

El backend está desarrollado sobre una arquitectura moderna e industrial utilizando las siguientes tecnologías:

| Tecnología | Propósito / Uso en el Sistema |
| :--- | :--- |
| **Java 21 / Spring Boot 3.3** | Framework y entorno principal de desarrollo. |
| **Spring Security 6 + JWT + OAuth2** | Infraestructura de autenticación, emisión de tokens y control de accesos. |
| **Spring Data JPA + PostgreSQL** | Persistencia de datos relacionales (catálogo central, perfiles de usuario y equipos). |
| **Spring Data MongoDB** | Persistencia no relacional orientada a logs analíticos e histórico de consultas. |
| **MapStruct** | Mapeo eficiente de objetos desacoplados entre capas (`DTO` ↔ `Core` ↔ `Entity`). |
| **Flyway** | Control de versiones y migraciones evolutivas del esquema de base de datos relacional. |
| **Springdoc OpenAPI (Swagger)** | Generación automática de la documentación interactiva para los endpoints de la API. |
| **JUnit 5 + Mockito + JaCoCo** | Framework de pruebas y mocks con una cobertura mínima obligatoria del 70%. |
| **Docker Compose** | Orquestación local y ágil de los contenedores de PostgreSQL y MongoDB. |

---

## Arquitectura del Sistema

**Megadex** se rige bajo un patrón de **Arquitectura por Capas con Inversión de Dependencias (Hexagonal/Ports & Adapters)**. Este enfoque garantiza que la lógica de negocio se mantenga completamente agnóstica a los frameworks, bases de datos o librerías externas.

* **Controller:** Expone los endpoints REST y gestiona las peticiones HTTP externas. Depende exclusivamente de las interfaces definidas en el Core.
* **Core:** Contiene las reglas de negocio puras. No posee acoplamiento con la persistencia ni con el framework; define contratos (puertos).
* **Persistence:** Implementa los contratos del Core mediante adaptadores concretos utilizando JPA y MongoDB.
* **Config & Security:** Capas transversales que proveen inyección de dependencias y políticas de seguridad perimetral a toda la aplicación.

---

## Documentación y Requerimientos

El análisis detallado del sistema comprende un total de **18 requerimientos funcionales (RF)**, **5 requerimientos no funcionales (RNF)** evaluados bajo el estándar de calidad **ISO/IEC 25010** y sus respectivas reglas de negocio.

Toda la documentación técnica se encuentra organizada dentro de la carpeta `docs/`:

* **Especificación de Requerimientos:** Se puede consultar el documento formal de ingeniería en:  
  `docs/_Análisis_de_requerimientos_MEGADEX.docx.pdf`

---

## Diagramas del Sistema

### 1. Diagrama de Contexto
![Diagrama de Contexto](docs/Diagrama%20de%20contexto/Diagrama%20de%20contexto.png)

### 2. Diagrama de Clases (Capa Core)
![Diagrama de Clases](docs/Diagrama%20de%20clases/Diagrama%20de%20clases.png)

### 3. Diagramas de Casos de Uso (RF-01 a RF-18)
![RF-01](docs/Casos%20de%20uso/RF-01.png)
![RF-02](docs/Casos%20de%20uso/RF-02.png)
![RF-03](docs/Casos%20de%20uso/RF-03.png)
![RF-04](docs/Casos%20de%20uso/RF-04.png)
![RF-05](docs/Casos%20de%20uso/RF-05.png)
![RF-06](docs/Casos%20de%20uso/RF-06.png)
![RF-07](docs/Casos%20de%20uso/RF-07.png)
![RF-08](docs/Casos%20de%20uso/RF-08.png)
![RF-09](docs/Casos%20de%20uso/RF-09.png)
![RF-10](docs/Casos%20de%20uso/RF-10.png)
![RF-11](docs/Casos%20de%20uso/RF-11.png)
![RF-12](docs/Casos%20de%20uso/RF-12.png)
![RF-13](docs/Casos%20de%20uso/RF-13.png)
![RF-14](docs/Casos%20de%20uso/RF-14.png)
![RF-15](docs/Casos%20de%20uso/RF-15.png)
![RF-16](docs/Casos%20de%20uso/RF-16.png)
![RF-17](docs/Casos%20de%20uso/RF-17.png)
![RF-18](docs/Casos%20de%20uso/RF-18.png)