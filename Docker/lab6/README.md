# Lab 6: Managing Docker Environment Variables Across Build and Runtime

## Objective
Demonstrate three different ways to set environment variables for a containerized app: directly in the `docker run` command, through an external env file, and inside the Dockerfile at build time.

## Application
- Source: [Ibrahim-Adel15/Docker-3](https://github.com/Ibrahim-Adel15/Docker-3)
- Framework: Flask (Python)



## Clone repo and Build the Image

```bash
git clone https://github.com/Ibrahim-Adel15/Docker-3.git 
cd Docker-3
notepad Dockerfile
docker build -t envapp .
docker images envapp
```
<img width="1461" height="610" alt="image" src="https://github.com/user-attachments/assets/9f43ef59-945f-43c6-b1b6-bbb14c5f2b63" />


## Run 3 Containers with Different Env Var Sources

### 1 Set variables directly in the docker run command -> development, us-east

```bash
docker run -d -p 8081:8080 --name container_dev -e APP_MODE=development -e APP_REGION=us-east  envapp

docker ps
```
<img width="1465" height="220" alt="image" src="https://github.com/user-attachments/assets/2e6e7f45-eed0-45fa-a271-453abcd3ee44" />

```bash
curl http://localhost:8081/
```
<img width="1385" height="472" alt="image" src="https://github.com/user-attachments/assets/0e7c3035-1506-48fa-9547-c1cc1ec5798c" />

### 2 Set variables from seperate env file → staging, us-west

`stage.env`:
```
APP_MODE=staging
APP_REGION=us-west
```

```bash
docker run -d -p 8082:8080 --name container_staging   --env-file stage.env  envapp
docker ps
```
<img width="1470" height="182" alt="image" src="https://github.com/user-attachments/assets/e81e8d59-214c-47da-9b5e-a5dd65e01e89" />
```bash
curl http://localhost:8082/
```
<img width="1425" height="492" alt="image" src="https://github.com/user-attachments/assets/ca24e28e-8fa8-4b45-9736-9c63f679dd06" />


### 3 Use the Environment variables that has beend set inside docker file  → production, canada-west

```bash
docker run -d -p 8083:8080 --name container_prod envapp
```
<img width="1422" height="202" alt="image" src="https://github.com/user-attachments/assets/144fc8f1-72b9-429a-a805-fc38c8e14fcc" />

```bash
curl http://localhost:8083/
```
<img width="1446" height="516" alt="image" src="https://github.com/user-attachments/assets/a5fffda6-cfc2-43a4-8e23-6414e0e29423" />

## Cleanup

```bash
docker stop container_dev container_stage container_prod
docker rm container_dev container_stage container_prod
```
