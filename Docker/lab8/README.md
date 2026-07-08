# Lab 8: Custom Docker Network for Microservices

## Objective
Demonstrate how Docker's custom networks provide automatic DNS-based service discovery between containers by name

## Application
- Source: [Ibrahim-Adel15/Docker5](https://github.com/Ibrahim-Adel15/Docker5)
- Two services: a Flask **frontend** and a Flask **backend**
- The frontend calls the backend over HTTP using its **container name** as the hostname:




```bash
git clone https://github.com/Ibrahim-Adel15/Docker5.git
cd Docker5
```

## Build Both Images

```bash
notepad frontend/Dockerfile
cd frontend
docker build -t frontend-app .
```
<img width="1452" height="397" alt="image" src="https://github.com/user-attachments/assets/3c9dbed8-7ebf-432d-9f78-adad4198e1de" />

```bash
notepad backend/Dockerfile
cd backend 
docker build -t backend-app .
```
<img width="1436" height="512" alt="image" src="https://github.com/user-attachments/assets/4efa5f9d-3c7b-4b48-8f2f-4c1a6a643292" />


## Create the Custom Network

```bash
docker network create ivolve-network
docker network ls
```
<img width="1017" height="240" alt="image" src="https://github.com/user-attachments/assets/bc4bb001-f599-470e-90c7-65fb1e4de53c" />


## Run the Backend on `ivolve-network`

```bash
docker run -d --name backend --network ivolve-network backend-app
```

> The container must be named exactly `backend` — the frontend code calls `http://backend:5000`, relying on Docker's DNS resolving that name.
<img width="997" height="72" alt="image" src="https://github.com/user-attachments/assets/5de5b009-5651-436e-b833-45d72ff79e5c" />

## Run frontend1 on `ivolve-network`

```bash
docker run -d --name frontend1 -p 5001:5000 --network ivolve-network frontend-app
```

## Run frontend2 on the Default Network

```bash
docker run -d --name frontend2 -p 5002:5000 frontend-app
```
<img width="1257" height="135" alt="image" src="https://github.com/user-attachments/assets/95dab58a-8908-4d9f-98b2-46922d977988" />



## Verify Communication

```bash
curl http://localhost:5001
```
<img width="1357" height="546" alt="image" src="https://github.com/user-attachments/assets/d116db36-5ee5-40c2-b028-d4d5e78233e2" />



```bash
curl http://localhost:5002
```
<img width="1435" height="536" alt="image" src="https://github.com/user-attachments/assets/2da27f7b-8af1-4d76-b8ad-764d5e447fdc" />

##NOTE:since frontend2 is on the default bridge network and cannot resolve the `backend` hostname (custom networks provide automatic DNS resolution by container name
the default bridge network does not

## Inspect the Network

```bash
docker network inspect ivolve-network
```
<img width="1065" height="677" alt="image" src="https://github.com/user-attachments/assets/49957095-fd5d-4863-9c7a-dd58837fb367" />




## Cleanup

```bash
docker rm -f frontend1
docker rm -f frontend2
docker rm -f backend
docker network rm ivolve-network
```

## Summary
This is a core reason custom networks are used in real microservice deployments: services can reference each other by name (e.g. `backend`, `db`, `redis`) instead of hardcoding IP addresses that change every time a container restarts.
