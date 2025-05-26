# BYJ ERP - Sistema ERP Multiplataforma

## 🐳 Base de Datos con Docker

### ▶️ Crear base de datos

```bash
docker compose up -d
```

### ▶️ Iniciar base de datos

```bash
docker compose start
```

### ⏹️ Detener base de datos

```bash
docker compose stop
```

### ❌ Eliminar volumenes y contenedores (reset completo)

```bash
docker compose down -v
```
>Usar **sudo** en linux
---

## 🚀 Comandos Útiles

Android Studio no permite iniciar mas de una aplicacion a la vez, por lo que sera necesario ejecutar la segunda instancia desde el terminal
> provados en linux

### 🟣 Iniciar el servidor

```bash
./gradlew :server:run -Dorg.gradle.java.home=/lib/jvm/java-21-openjdk/
```

> *-Dorg.gradle.java.home* debe apuntar a tu instancia de *java 21*

---

### 💻 Iniciar app de escritorio (desktop)

Desde Android Studio o con:

```bash
./gradlew :composeApp:run -Dorg.gradle.java.home=/lib/jvm/java-21-openjdk/
```

> *-Dorg.gradle.java.home* debe apuntar a tu instancia de *java 21*

---

### 📱 Iniciar app Android

Desde Android Studio o con:

```bash
./gradlew :composeApp:installDebug
```

iniciar manualmente

> debes tener un emulador iniciado (Android Studio) o dispositivo conectado (con depuracion USB).

---

## 🌐 Configuración de Google Cloud para OAuth2

### 🔧 Pasos para crear el proyecto y configurar OAuth

1. **Ir a la consola de Google Cloud**
   [https://console.cloud.google.com/](https://console.cloud.google.com/)

2. **Crear un nuevo proyecto**

3. **Habilitar APIs necesarias**
   Ve a **API & Services > Library** y habilita:

   * Google People API

4. **Crear credenciales OAuth 2.0**

   * Ir a **API & Services > Credentials**
   * Clic en **Create Credentials > OAuth client ID**
   * Selecciona tipo de aplicación:

      * Para el **servidor**: selecciona "Web application"
      * Agrega `http://localhost:8080/auth/callback` y `http://<tu dominio publico>:8080/auth/callback` como **Authorized redirect URI**
      * Agrega `http://localhost:8080` y `http://<tu dominio publico>:8080` como **Authorized JavaScript origins**, por si acaso

     > *"tu dominio publico"* solo es necesario para android
   * Guarda el `CLIENT_ID` y `CLIENT_SECRET`, son necesarios para el `.env` del servidor.

5. **Scopes requeridos**

   * Los scopes necesarios son:

     ```text
     https://www.googleapis.com/auth/userinfo.profile
     https://www.googleapis.com/auth/userinfo.email
     openid
     ```

---

## 🦆 Crear dominio publico con DuckDNS

Para Android es necesario usar un dominio publico en el servidor,
[DuckDNS](https://www.duckdns.org/) es un servicio gratuito util para este fin.

### Pasos para obtener un dominio en DuckDNS
1. provar de tienes acceso a una IPv6 en [esta](https://test-ipv6.com/) pagina o similares

2. crear una cuenta en [DuckDNS](https://www.duckdns.org/)

3. darle un nombre propio a tu dominio

4. darle la IPv6 del equipo con el servidor y precionar **update IPv6** dejando el IPv4 en blanco

> esto podria tomar hasta 30 minutos para reflejar la nueva ip

5. usa tu nuevo dominio cada vez que tengas que usar la ip del servidor, ejemplo dominio:

     ```text
     proyectron.duckdns.org
     ```

---

## ⚙️ Crear .env's
para cada uno de estos existe un **.example** con el que guiarse en el mismo path
### 🤖 android
path:
```text
byj_erp/composeApp/src/androidMain/assets/env.properties
```

### 🖥️ desktop
path:
```text
byj_erp/composeApp/.env
```
> esta en el root de ComposeApp pero solo lo usa Desktop
### 🖥 server
path:
```text
byj_erp/server/.env
```