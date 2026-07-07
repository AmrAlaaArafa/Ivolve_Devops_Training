<img width="1362" height="657" alt="image" src="https://github.com/user-attachments/assets/1bc002e6-ed38-4808-8b4d-0be8ea52df60" />
# Lab 4: Run Java Spring Boot App in a Container (Prebuilt JAR)

## Objective
Containerize the same Spring Boot application as Lab3, but build the JAR **outside** the container (locally) and use a lightweight Java runtime image to run it — resulting in a smaller final image.

## Application
- Source: [Ibrahim-Adel15/Docker-1](https://github.com/Ibrahim-Adel15/Docker-1)
- Framework: Spring Boot, Java 17
- Endpoint: `GET /` → returns `Hello from Dockerized Spring Boot!`

## Build the Application Locally

```bash
mvn package
```
<img width="1467" height="630" alt="image" src="https://github.com/user-attachments/assets/542741bc-36ca-4f25-90b5-1741fe2236a0" />


## Build the Image

```bash
docker build  -f .\Dockerfile2 . -t labjar
docker images labjar
```

## Run the Container

```bash
docker run -d -p 8080:8080 --name container2 labjar
```
<img width="1362" height="657" alt="image" src="https://github.com/user-attachments/assets/c5e906ef-ef3f-47d1-902e-83bf0cc33e4f" />


```bash
curl http://localhost:8080/
```
<img width="1412" height="452" alt="image" src="https://github.com/user-attachments/assets/52599faf-df8f-430c-adbf-07a457d99bdd" />



## Stop and Remove the Container

```bash

docker rm -f container2
```

## Image Size Note

This image is significantly smaller than Lab3's `app1` image, since it excludes Maven and the JDK compiler entirely — only the JRE and the final JAR are needed at runtime. Compare:

```bash
docker images
```

| Image | Base | Approx. Size |
|-------|------|--------------|
| app1 (Lab3) | maven:3-eclipse-temurin-17-alpine | Larger (includes build tools) |
| app2 (Lab4) | eclipse-temurin:17-jre | Smaller (runtime only) |
