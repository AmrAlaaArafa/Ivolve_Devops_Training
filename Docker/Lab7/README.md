# Lab 7: Docker Volume and Bind Mount with Nginx

## Objective
the difference between a **named volume** (Docker-managed storage) and a **bind mount** (direct host folder mapping) 

## Image Used
This lab uses the official **`nginx`** image directly from Docker Hub — no  Dockerfile is needed

```bash
docker pull nginx
```

## Step 1: Create the Named Volume for Logs and Verify it exists

```bash
docker volume create nginx_logs
docker volume inspect nginx_logs
```
<img width="1451" height="706" alt="image" src="https://github.com/user-attachments/assets/3da88bb2-4687-42b6-9210-1da2b1552f40" />


## Step 2: Create the Bind Mount Directory and the HTML file

```bash
mkdir nginx-bind/html
cd nginx-bind/html
notepad index.html
```
<img width="682" height="307" alt="image" src="https://github.com/user-attachments/assets/4feb55be-a3bd-4f57-a95e-c90b9b6be42e" />



## Step 3: Run the Nginx Container with Volume + Bind Mount

```bash
docker run -d --name nginx_container -p 8080:80 -v nginx_logs:/var/log/nginx -v D:\lab7\nginx-bind\html:/usr/share/nginx/html nginx
docker ps
```
<img width="1387" height="77" alt="image" src="https://github.com/user-attachments/assets/d8b7561b-139c-42ef-9f6f-77678d2ff93c" />


- `-v nginx_logs:/var/log/nginx` → **named volume**, Docker-managed, persists independently of the host folder layout.
- `-v D:\lab7\nginx-bind\html:/usr/share/nginx/html` → **bind mount**, maps a specific host folder directly into the container; edits on the host reflect instantly inside the container.


## Step 4: Verify the Page

```bash
curl http://localhost:8080
```
<img width="1345" height="666" alt="image" src="https://github.com/user-attachments/assets/a4c8866a-e12a-4312-9f7d-a481647a0240" />



## Step 5: Edit the HTML on the Host and Re-verify

after editing in the index file

```bash
curl http://localhost:8080
```
<img width="1420" height="647" alt="image" src="https://github.com/user-attachments/assets/20d276d1-d7f4-4db9-98bd-60aa52c0f5f4" />


The updated content appears immediately — **no container restart required**, since the bind mount reflects host filesystem changes live.

## Step 6: Verify Logs in the Volume

By default, the official Nginx image symlinks its logs to the container's stdout/stderr rather than writing real files:

```bash
docker exec -it nginx_container sh
ls -la /var/log/nginx
```
<img width="807" height="187" alt="image" src="https://github.com/user-attachments/assets/7576b983-448c-4a4b-9b09-22bf7bc1cc78" />





## Step 7: Delete the Volume and Verify It

```bash
docker rm -f nginx_container
docker volume rm nginx_logs
docker volume ls
```
<img width="632" height="196" alt="image" src="https://github.com/user-attachments/assets/308bc303-a05f-4023-a1a9-ef566adc333f" />


## Summary

Volumes are best for data, Docker should manage and persist (databases, logs). Bind mounts are best for live development work where you want host edits reflected immediately inside the container.
