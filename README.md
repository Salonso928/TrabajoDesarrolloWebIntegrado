
🍽️ Sabor y Tradición

Sistema web de gestión para el restaurante Sabor y Tradición, desarrollado para administrar usuarios y reservas de manera eficiente. Permite registrar, consultar, actualizar y cancelar reservas, así como gestionar la disponibilidad de mesas y horarios. Cuenta con un backend desarrollado en Spring Boot y autenticación mediante JWT.
El backend de Sabor y Tradición cuenta con documentación interactiva desarrollada mediante Swagger UI y OpenAPI 3.

La documentación permite consultar los endpoints disponibles, sus parámetros, estructuras de datos, respuestas exitosas y posibles errores.


Para visualizar y probar los servicios REST, inicia el backend y accede a:

http://localhost:8080/swagger-ui/index.html

📚 Documentación API

       📄   Descargar Swagger YAML

       🌐   Visualizar documentación en Swagger Editor


🔹 Módulos documentados
Autenticación: registro e inicio de sesión.
Usuarios: consulta y actualización de información.
Reservas: registro, cancelación, finalización y consulta de reservas.
Mesas y horarios: consulta de disponibilidad y turnos.
🔹 Seguridad

Los endpoints protegidos utilizan autenticación mediante JWT (JSON Web Token). Swagger permite ingresar el token mediante el botón Authorize para probar los servicios que requieren autenticación.