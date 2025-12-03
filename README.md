
Este proyecto es una aplicación de gestión de usuarios desarrollada con Spring Boot en el backend y JavaScript en el frontend.
Su objetivo principal es demostrar un flujo completo de autenticación con JWT (JSON Web Tokens) y operaciones CRUD sobre usuarios, siguiendo buenas prácticas de arquitectura y seguridad.

Pensado como un proyecto de aprendizaje y portfolio, refleja conocimientos sólidos en:
• 	Backend robusto con Spring Boot.

• 	Persistencia con JPA/Hibernate y EntityManager.

• 	Seguridad con JWT y Argon2 para hashing de contraseñas.

• 	Consumo de endpoints desde frontend con JavaScript

• 	Uso de una plantilla HTML y CSS común para toda la aplicación, garantizando coherencia visual y simplicidad en el diseño.

🛠️ Tecnologías utilizadas

• 	Java 17 + Spring Boot

• 	JPA/Hibernate con MySQL/MariaDB

• 	JWT para autenticación

• 	Argon2 para almacenamiento seguro de contraseñas

• 	JavaScript (ES6) para consumo de API

• 	HTML/CSS con plantilla reutilizable para toda la interfaz


🔑 Funcionalidades principales

• 	Registro de usuarios con contraseñas hasheadas.

• 	Login que devuelve un JWT válido.

• 	Protección de endpoints mediante validación de token.

• 	Gestión de usuarios:

• 	Listado de usuarios ().

• 	Eliminación de usuarios ().

• 	Frontend sencillo que:

• 	Realiza login y guarda el token en .

• 	Consume endpoints protegidos con cabecera .

• 	Renderiza tabla dinámica de usuarios y permite eliminarlos.

• 	Utiliza una plantilla HTML/CSS única para mantener consistencia en todas las vistas.
