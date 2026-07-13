# Lab 9: Containerized Node.js and MySQL Stack Using Docker Compose

## Objective
Run a Node.js with a MySQL database using Docker Compose

## Application
- Source: [Ibrahim-Adel15/kubernets-app](https://github.com/Ibrahim-Adel15/kubernets-app)
- Stack: Node.js + MySQL
- The app requires a database named **`ivolve`** to already exist — it checks for it on startup and retries indefinitely if not found:

# 1-Cloning the repo + creating the docker-compose file
``bash
git clone https://github.com/Ibrahim-Adel15/kubernets-app.git
cd .\kubernets-app\
notepad docker-compose-yml
```
<img width="1025" height="262" alt="image" src="https://github.com/user-attachments/assets/c0afbe5b-73f9-4d36-9df3-a0a9f9f9a700" />

# 2- Applying docker compose command and verify it's created
```bash
docker compose up -d
docker ps -a
```
<img width="1460" height="702" alt="image" src="https://github.com/user-attachments/assets/33b0918c-18d0-449a-95ed-0062aada26f7" />

# 3- Verify the app is working
```bash
docker logs -f 2d9be571aaf6
 or
docker compose logs app
```
<img width="785" height="521" alt="image" src="https://github.com/user-attachments/assets/0aa716a9-72f3-487f-b6e0-57abbaf798ff" />

```bash
curl http://localhost:3000
```
<img width="1375" height="567" alt="image" src="https://github.com/user-attachments/assets/c68c68c1-b055-4171-8436-065356e6e186" />

# 4- Verify /health and /ready


```bash
curl http://localhost:3000/health
curl http://localhost:3000/ready
```

<img width="1300" height="465" alt="image" src="https://github.com/user-attachments/assets/f7e43132-4a63-4ad9-8690-ba0754f7c7ac" />
<img width="1317" height="487" alt="image" src="https://github.com/user-attachments/assets/4cf3546e-d3f1-4bf7-9881-bf9a8999ac8f" />

# 5- Verify app access logs at /app/logs/

```bash
docker exec -it 2d9be571aaf6 sh
cat /app/logs/access.log
```
<img width="1465" height="222" alt="image" src="https://github.com/user-attachments/assets/25d8a683-b57d-4cc5-bd64-8e45cd3ca714" />

# 6- Push the Docker image into your DockerHub

```bash
docker login
docker tag kubernets-app-app amralaa21/kubernetes-app:1
docker push amralaa21/kubernetes-app:1
```
<img width="1201" height="375" alt="image" src="https://github.com/user-attachments/assets/07d59618-d696-491a-8f52-dc215fe6824f" />

# 7- Cleanup

Add `-v` to also remove the named volumes (`db_data`, `app_log`):
```bash
docker compose down -v
```
<img width="592" height="187" alt="image" src="https://github.com/user-attachments/assets/e28da5cd-5bf2-4bc1-9423-e59ca4747e4e" />
