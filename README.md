# 🏪 Mini-Tienda - Sistema de Gestión de Inventario

Sistema de gestión de inventario desarrollado con **arquitectura por capas** y patrón **MVC**, utilizando JavaFX para la interfaz gráfica y MySQL como base de datos.

---

## 📋 Descripción

Mini-Tienda es una aplicación de escritorio que permite gestionar el inventario de productos de una tienda, implementando las operaciones CRUD (Crear, Leer, Actualizar, Eliminar) con validaciones de negocio y manejo robusto de excepciones personalizadas.

---

## 🏗️ Arquitectura del Proyecto

El proyecto implementa una **arquitectura por capas** siguiendo el patrón **MVC (Modelo-Vista-Controlador)**:

```
com.mycompany.domain/
├── 📁 config/              # Configuración de conexión a BD
│   └── DBconexion.java     # Singleton para gestión de conexiones
│
├── 📁 modelo/              # MODELO: Entidades de dominio
│   └── Producto.java       # Entidad Producto (POJO)
│
├── 📁 repository/          # ACCESO A DATOS (DAO Pattern)
│   ├── IDBC/
│   │   └── ProductoDAO.java        # Interface del DAO
│   └── JDBC/
│       └── ProductoDAOImpl.java    # Implementación JDBC
│
├── 📁 service/             # CONTROLADOR: Lógica de negocio
│   ├── InventarioServiceLocal.java    # Interface del servicio
│   └── impl/
│       └── InventarioServiceImpl.java # Implementación del servicio
│
├── 📁 ui/                  # VISTA: Capa de presentación
│   └── MainApp.java        # Aplicación JavaFX
│
└── 📁 exceptions/          # Excepciones personalizadas
    ├── DatoInvalidoException.java
    ├── DuplicadoException.java
    └── PersistenciaException.java
```

### 🔄 Flujo de Capas

```
┌─────────────────┐
│  UI (JavaFX)    │  ← VISTA: Interfaz gráfica del usuario
└────────┬────────┘
         │
         ↓
┌─────────────────┐
│  Service Layer  │  ← CONTROLADOR: Validaciones y lógica de negocio
└────────┬────────┘
         │
         ↓
┌─────────────────┐
│   DAO Layer     │  ← MODELO: Acceso a datos (CRUD)
└────────┬────────┘
         │
         ↓
┌─────────────────┐
│  MySQL Database │  ← Base de datos
└─────────────────┘
```

---

## 🛠️ Tecnologías Utilizadas

| Componente | Tecnología | Versión |
|------------|------------|---------|
| **Lenguaje** | Java | 11 |
| **Build Tool** | Maven | 3.11.0 |
| **Base de Datos** | MySQL | 8.0.33 |
| **UI Framework** | JavaFX | 17.0.2 |
| **JDBC Driver** | MySQL Connector/J | 8.0.33 |

---

## 📦 Dependencias (pom.xml)

```xml
<dependencies>
    <!-- JavaFX Controls -->
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-controls</artifactId>
        <version>17.0.2</version>
    </dependency>
    
    <!-- JavaFX FXML -->
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-fxml</artifactId>
        <version>17.0.2</version>
    </dependency>
    
    <!-- MySQL Connector/J -->
    <dependency>
        <groupId>com.mysql</groupId>
        <artifactId>mysql-connector-j</artifactId>
        <version>8.0.33</version>
    </dependency>
</dependencies>
```

---

## 🗄️ Configuración de Base de Datos

### Credenciales MySQL

```java
URL: jdbc:mysql://localhost:3306/minitienda
Usuario: root
Contraseña: 1234
```

### Script SQL para crear la base de datos

```sql
CREATE DATABASE IF NOT EXISTS minitienda;
USE minitienda;

CREATE TABLE productos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    precio DECIMAL(10, 2) NOT NULL CHECK (precio > 0),
    stock INT NOT NULL CHECK (stock >= 0),
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Insertar datos de ejemplo
INSERT INTO productos (nombre, precio, stock) VALUES
('Laptop HP', 799.99, 10),
('Mouse Logitech', 25.50, 50),
('Teclado Mecánico', 89.99, 30),
('Monitor Samsung 24"', 199.99, 15),
('Webcam HD', 45.00, 25);
```

---

## ⚙️ Instalación y Ejecución

### Requisitos Previos

- ☕ Java JDK 11 o superior
- 🛢️ MySQL Server 8.0 o superior
- 📦 Maven 3.6 o superior

### Pasos de Instalación

1. **Clonar el repositorio**
   ```bash
   git clone <url-del-repositorio>
   cd Semana4
   ```

2. **Configurar la base de datos**
   - Ejecutar el script SQL proporcionado arriba
   - Verificar las credenciales en `DBconexion.java`

3. **Compilar el proyecto**
   ```bash
   mvn clean compile
   ```

4. **Ejecutar la aplicación**
   ```bash
   mvn javafx:run
   ```

   O alternativamente:
   ```bash
   mvn clean package
   java -jar target/Semana4-1.0-SNAPSHOT.jar
   ```

---

## 🎯 Funcionalidades

### ✨ Operaciones Principales

| Funcionalidad | Descripción | Validaciones |
|---------------|-------------|--------------|
| **➕ Agregar Producto** | Inserta un nuevo producto al inventario | Nombre único, precio > 0, stock ≥ 0 |
| **💰 Actualizar Precio** | Modifica el precio de un producto existente | Precio > 0 |
| **📊 Actualizar Stock** | Modifica el stock de un producto | Stock ≥ 0 |
| **🗑️ Eliminar Producto** | Elimina un producto del inventario | Confirmación requerida |
| **🔍 Buscar Producto** | Busca productos por nombre (búsqueda parcial) | - |
| **🔄 Refrescar** | Recarga la lista de productos | - |

### 📊 Estadísticas en Tiempo Real

La aplicación muestra un panel de estadísticas que incluye:
- Total de operaciones exitosas
- Cantidad de altas realizadas
- Cantidad de bajas realizadas
- Cantidad de actualizaciones realizadas

---

## 🚨 Manejo de Excepciones

### Excepciones Personalizadas

| Excepción | Cuándo se lanza | Ejemplo |
|-----------|-----------------|---------|
| **DatoInvalidoException** | Datos que no cumplen validaciones de negocio | Precio negativo, stock negativo, nombre vacío |
| **DuplicadoException** | Se intenta agregar un producto con nombre existente | Producto "Laptop HP" ya existe |
| **PersistenciaException** | Error en operaciones de base de datos | Conexión perdida, SQL malformado |

### Ejemplo de Flujo de Excepciones

```
Usuario ingresa precio = -100
         ↓
    UI (MainApp)
         ↓
Service (InventarioServiceImpl) → ❌ DatoInvalidoException
         ↓
    UI muestra mensaje: "El precio debe ser mayor a cero"
```

---

## 📸 Capturas de Pantalla

### Interfaz Principal
```
┌─────────────────────────────────────────────────────────┐
│         🏪 SISTEMA DE GESTIÓN DE INVENTARIO             │
│   Arquitectura por Capas con Excepciones Personalizadas │
├─────────────────────────────────────────────────────────┤
│  📦 Inventario de Productos                             │
│  ┌───────────────────────────────────────────────────┐  │
│  │ ID │ Producto          │ Precio   │ Stock      │  │
│  ├────┼──────────────────┼──────────┼───────────┤  │
│  │ 1  │ Laptop HP         │ $799.99  │ 10        │  │
│  │ 2  │ Mouse Logitech    │ $25.50   │ 50        │  │
│  │ 3  │ Teclado Mecánico  │ $89.99   │ 30        │  │
│  └───────────────────────────────────────────────────┘  │
├─────────────────────────────────────────────────────────┤
│ 📊 Operaciones: 12 | ➕ Altas: 3 | ➖ Bajas: 1 | ✏️ Act: 8│
└─────────────────────────────────────────────────────────┘
```

---

## 🔐 Buenas Prácticas Implementadas

### ✅ Patrón Singleton
- Conexión a base de datos única y reutilizable
- Evita múltiples conexiones simultáneas

### ✅ Try-with-resources
- Cierre automático de recursos JDBC
- Prevención de memory leaks

### ✅ PreparedStatement
- Prevención de SQL Injection
- Mejor rendimiento en consultas repetidas

### ✅ Separación de Responsabilidades
- **UI**: Solo presentación, sin lógica de negocio
- **Service**: Validaciones y coordinación
- **DAO**: Acceso exclusivo a datos

### ✅ Manejo Robusto de Errores
- Excepciones personalizadas por tipo de error
- Mensajes claros y amigables al usuario
- Log de operaciones en consola

---

## 🚀 Posibles Mejoras Futuras

- [ ] Implementar Spring Boot para backend REST
- [ ] Agregar frontend Angular/React
- [ ] Sistema de autenticación y roles
- [ ] Reportes en PDF/Excel
- [ ] Auditoría de operaciones (log histórico)
- [ ] Backup automático de base de datos
- [ ] Soporte para múltiples sucursales
- [ ] Notificaciones cuando stock es bajo

---

## 👨‍💻 Autor

**Proyecto Académico - Arquitectura por Capas**

---

## 📄 Licencia

Este proyecto es de uso académico y educativo.

---

## 📞 Soporte

Para dudas o problemas:
- Revisar los logs en consola
- Verificar credenciales de MySQL
- Asegurar que el puerto 3306 esté disponible
- Verificar que JavaFX esté correctamente instalado

---

**Versión:** 1.0-SNAPSHOT  
**Última actualización:** 2025
