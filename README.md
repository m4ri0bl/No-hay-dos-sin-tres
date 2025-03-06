# 🎉 Sistema de Gestión de Invitados 🎉

## 📋 Descripción

Este proyecto es un **Sistema de Gestión de Invitados** para eventos desarrollado como parte del curso de aplicaciones web. La aplicación permite administrar fácilmente la lista de invitados para cualquier tipo de evento, como conferencias, bodas o seminarios.

## ✨ Características

- 👥 **Consulta de Invitados**: Visualización de todos los invitados registrados
- ➕ **Alta de Invitados**: Registro de nuevos invitados en el sistema
- ➖ **Baja de Invitados**: Eliminación de invitados existentes
- 🔄 **Interfaz Responsiva**: Diseño adaptable a diferentes dispositivos
- 🔒 **Validación de Datos**: Verificación de la información ingresada

## 🛠️ Tecnologías Utilizadas

- **Backend**: 
  - ☕ Java 8+
  - 🧰 Servlets
  - 📄 JSP (JavaServer Pages)

- **Frontend**:
  - 🎨 HTML5
  - 💅 CSS3
  - 📝 JSTL

- **Base de Datos**:
  - 🗄️ MySQL

- **Herramientas de Desarrollo**:
  - 📦 Maven
  - 🚀 Webapp Runner (Tomcat)

## 📊 Estructura del Proyecto

La aplicación sigue una arquitectura MVC (Modelo-Vista-Controlador):

```
gestorInvitados/
├── src/
│   ├── main/
│   │   ├── java/               # Código fuente Java
│   │   │   └── com/gestioninvitados/
│   │   │       ├── dao/        # Acceso a datos
│   │   │       ├── modelo/     # Entidades
│   │   │       ├── servicio/   # Lógica de negocio
│   │   │       ├── servlet/    # Controladores
│   │   │       └── util/       # Utilidades
│   │   ├── resources/          # Archivos de configuración
│   │   └── webapp/             # Vistas y recursos web
│   │       ├── css/            # Estilos
│   │       └── WEB-INF/        # Configuración web
└── pom.xml                     # Configuración Maven
```

## 🚀 Instalación y Ejecución

### Prerrequisitos

- ☕ JDK 8 o superior
- 📦 Maven 3.6 o superior
- 🗄️ MySQL

### Configuración de la Base de Datos

1. Crea una base de datos en MySQL
2. Ejecuta el siguiente script SQL para crear la tabla necesaria:

```sql
CREATE TABLE invitados (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL
);
```

3. Configura las credenciales de la base de datos en el archivo `src/main/resources/db.properties`

### Compilación y Ejecución

1. Clona este repositorio:
```bash
git clone https://github.com/tu-usuario/gestor-invitados.git
cd gestor-invitados
```

2. Compila el proyecto:
```bash
mvn clean package
```

3. Ejecuta la aplicación localmente:
```bash
java -jar target/dependency/webapp-runner.jar target/*.war
```

4. Abre tu navegador y visita: `http://localhost:8080`

## 🚢 Despliegue

### En una plataforma gratuita (Render, Railway, etc.)

1. Sube el proyecto a GitHub
2. Regístrate en la plataforma de despliegue seleccionada
3. Conecta tu repositorio de GitHub
4. Configura el comando de inicio:
```
java $JAVA_OPTS -jar target/dependency/webapp-runner.jar --port $PORT target/*.war
```

## 👨‍💻 Autores

* **Iker Arias, Ali y Mario Blanco** - *Trabajo No Hay Dos Sin Tres* 

## 📄 Licencia

Este proyecto está bajo la Licencia MIT - mira el archivo [LICENSE](LICENSE) para detalles

## 🎯 Estado del Proyecto

- [x] Configuración inicial del proyecto
- [x] Implementación de la funcionalidad de listar invitados
- [x] Implementación de la funcionalidad de alta de invitados
- [x] Implementación de la funcionalidad de baja de invitados
- [x] Despliegue en plataforma gratuita
- [ ] Funcionalidades adicionales (actualización, búsqueda, etc.)

## 🙏 Agradecimientos

* Profesor del curso de Desarrollo de Aplicaciones Web
* Compañeros de clase por el feedback y las pruebas

---
