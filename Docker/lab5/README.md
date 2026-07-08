# Lab 5: Multi-Stage Build for JAVA App

## Objective
Combine the build and run steps from Lab3 and Lab4 into a single **multi-stage Dockerfile**

## Application
Same application used in Lab3-4:
- Source: [Ibrahim-Adel15/Docker-1](https://github.com/Ibrahim-Adel15/Docker-1)
- Framework:  Java 17

## Clone the Repo &&  Build the Image


```bash
git clone https://github.com/Ibrahim-Adel15/Docker-1.git
cd Docker-1
notepad Dockerfile3
docker build  -f .\Dockerfile3 . -t lab5
```
<img width="1450" height="637" alt="image" src="https://github.com/user-attachments/assets/fa6a9249-0524-426d-8024-9646c32592d3" />

## Notice The Size of the Image
```bash
docker images lab5
```
<img width="842" height="101" alt="image" src="https://github.com/user-attachments/assets/3c56a04d-62ae-4040-a848-95afb6be74d4" />

## Run the Container

```bash
docker run -d -p 8080:8080 --name container3 lab5
docker ps -a
```
<img width="1450" height="306" alt="image" src="https://github.com/user-attachments/assets/e4361a34-fdfb-40ad-beef-89fa526e7408" />


## Test

```bash
curl http://localhost:8080/
```
<img width="1437" height="612" alt="image" src="https://github.com/user-attachments/assets/b955cf31-393b-403d-83f4-f9dc049529d1" />



## Stop and Remove the Container && Delete the Image

```bash
docker rm -f container3
docker rmi lab5

```

## Image Size Comparison

| Image | Approach | Base Image(s) | Approx. Size |
|-------|----------|----------------|--------------|
| app1 (Lab3) | Build inside container | maven:3.9-eclipse-temurin-17 | Larger (includes build tools) |
| app2 (Lab4) | Prebuilt JAR copied in | eclipse-temurin:17-jre | Smaller (runtime only) |
| app3 (Lab5) | Multi-stage build | maven:3.9-eclipse-temurin-17 → eclipse-temurin:17-jre | Small (same as app2, no manual local build needed) |

## Why Multi-Stage Is the Best of Both

- Like Lab3: builds the app automatically inside Docker — no need to have Maven installed locally or run `mvn package` by hand.
- Like Lab4: final image only contains the JRE and the JAR — no leftover build tools or source code bloating the image.
