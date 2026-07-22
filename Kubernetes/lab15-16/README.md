# Lab 15 & 16: Node.js Deployment, Init Container DB Setup, and Ingress Exposure

These two labs are combined into one deployment because they modify the same `nodejs-app` Deployment 
Lab16 adds an init container on top of the Deployment Lab15 creates.
A bonus Ingress setup is included at the end to expose the app via a custom hostname.

## Objective

- **Lab 15**: Deploy the Node.js app with 2 replicas, a private-registry pull secret, environment variables from ConfigMap/Secret, a toleration for the Lab10 taint, the Lab13 PVC mounted for logs, and a ClusterIP service.
- **Lab 16**: Add an init container that creates the `ivolve` database and grants the app user privileges *before* the main app container starts — using the mysql:5.7 client image.
- **Bonus**: Expose the app externally via an Nginx Ingress controller and a custom hostname.

## Prerequisites

- `ivolve` namespace, `mysql-config` ConfigMap, `mysql-secret` Secret (Lab11/12)
- `mysql-statefulset` running with the `mysql` (or `mysql-headless`) service (Lab14)
- `app-logs-pvc` bound (Lab13)
- `node=worker:NoSchedule` taint applied to the worker node (Lab10)

 # NOTE: mysql:8 image used instead of mysql:5.7  for easier troubleshooting as mysql:8 already pulled locally before
 


## Step 1: Docker Hub Image Pull Secret

```bash
notepad dockerhub-secret.yml
kubectl apply -f dockerhub-secret.yml
kubectl get secret dockerhub-secret -n ivolve
```

<img width="1466" height="235" alt="image" src="https://github.com/user-attachments/assets/f9f0c3a2-4786-401f-8e1b-1d34b9e02254" />

## Step 2:  Deployment and Service yaml file (Lab15 + Lab16 combined)

```bash
notepad nodejs-deployment.yml
notepad nodejs-service.yml
kubectl apply -f nodejs-deployment.yml
kubectl apply -f nodejs-service.yml
```
<img width="1445" height="491" alt="image" src="https://github.com/user-attachments/assets/792983fa-d5f3-4b61-9dd8-ca91ebf0f92f" />

# note: A problem i faced in the init container, MySQL 8 strictly requires you to **`CREATE USER` first**, and then run **`GRANT` separately**.    
mysql -h "$DB_HOST" -u root -p"$MYSQL_ROOT_PASSWORD" -e "CREATE USER IF NOT EXISTS '$DB_USER'@'%'"

## Step 3: Verify — Expect Exactly 1 Pod Running, 1 Pending due to our quota  restrection

```bash
kubectl get pod -n ivolve
```
<img width="1202" height="187" alt="image" src="https://github.com/user-attachments/assets/dbde4c39-3dfd-444d-b9a2-6c04b60b7682" />



This is **expected**: the Lab11 ResourceQuota caps the `ivolve` namespace at 2 pods total. Since `mysql-statefulset-0` already occupies one slot, only 1 of the 2 requested `nodejs-app` replicas is admitted.



## Step 4: Verify the App Logs

```bash
 kubectl logs nodejs-app-5f4fb57d48-gfb7z -n ivolve -c nodejs-app
```
<img width="1377" height="92" alt="image" src="https://github.com/user-attachments/assets/7e857667-1266-41a1-ac44-7fd7b21bf972" />


## Step 5: Manually connect to MySQL and verify the database + user privileges

```bash
kubectl exec -it mysql-statefulset-0 -n ivolve -- mysql -u root -p"rootpassword" -e "SHOW DATABASES; SHOW GRANTS FOR 'amr'@'%';
```
<img width="1410" height="412" alt="image" src="https://github.com/user-attachments/assets/bc30691d-c4d9-425a-a815-7f40f048abf9" />



---

## Bonus: Exposing the Cluster with Ingress

### Enable the Ingress Addon (minikube)

```bash
minikube addons enable ingress
notepad nodejs-ingress.yml
kubectl apply -f nodejs-ingress.yaml
kubectl get ingress -n ivolve
```

<img width="1096" height="247" alt="image" src="https://github.com/user-attachments/assets/51a234c8-eedd-44f6-9e8d-aa6131bd7cd0" />


### Map the Hostname (Windows)
## we get the minikube ip to map the hostname


```bash
minikube ip
```

<img width="1395" height="150" alt="image" src="https://github.com/user-attachments/assets/1869e9ca-96c4-42b2-a630-da50e36440f7" />


Add to `C:\Windows\System32\drivers\etc\hosts` (Notepad as Administrator):
```
127.0.0.1    ivolve.com
```

##NOTE:  the Minikube IP is isolated inside Docker's network, making it unreachable directly from PowerShell so we need minikube tunnel to creates a network route from your host machine directly 
```bash
 minikube tunnel
```
<img width="1037" height="435" alt="image" src="https://github.com/user-attachments/assets/4e240595-87f4-4126-9938-98bd245a61f0" />

### Test

```bash
curl.exe http://ivolve.com/health
```
<img width="1347" height="165" alt="image" src="https://github.com/user-attachments/assets/44e36e57-7648-47ce-9ccb-4cbbbac51cdf" />

```bash
curl.exe http://ivolve.com/ready
```
<img width="972" height="45" alt="image" src="https://github.com/user-attachments/assets/c453408d-3c3d-48b9-8507-d621efe274de" />

```bash
kubectl exec -it mysql-statefulset-0 -n ivolve -- mysql -u root -p"rootpassword" -e "SHOW DATABASES; SHOW GRANTS FOR 'amr'@'%';"
```
<img width="1457" height="412" alt="image" src="https://github.com/user-attachments/assets/43eed18d-7e0c-4a23-8063-f893166ad5b8" />



## Troubleshooting Notes (real issues hit and fixed during this lab)

| Symptom | Root Cause | Fix |
|---|---|---|
| `0/2 nodes are available: node(s) didn't match Pod's node affinity/selector` | `hostPath` PV only existed on one node | Created the hostPath directory on all nodes, or ensured pod scheduling matched where the path existed |
| `db-init` CrashLoopBackOff: `Unknown MySQL server host 'mysql'` | `DB_HOST` in the ConfigMap didn't match the actual Service name from Lab14 (`mysql-headless`) | Renamed the headless service to `mysql` (or updated `DB_HOST` to match whatever the service is actually named) |
| App logs: `Access denied for user 'amr'@'...'` | MySQL's data directory (via the PVC) had already been initialized previously with a different password than currently in the Secret — the official image only applies `MYSQL_USER`/`MYSQL_PASSWORD` on first-ever init | Manually reset the password with `ALTER USER ... IDENTIFIED BY ...` to match the Secret |
| App logs: `'ivolve' database not found` | Init container's SQL had syntax errors (see next row) so it silently failed to actually create the database | Fixed the SQL; verified manually with `SHOW DATABASES` |
| `db-init` logs: `ERROR at line 1: Unknown command '\''.`  | Nested single/double quoting collision between YAML block scalar → `sh -c` → `mysql -e "..."`, and incorrectly quoting identifiers (`\'ivolve\'`) as if they were string literals | Rewrote the init script using a heredoc (`cat << EOF > file.sql`) piped into `mysql < file.sql`, avoiding inline `-e` quoting entirely; removed quotes around identifiers (only `user@host` needs quoting, not database/table names) |
| Deployment changes seemingly not taking effect after `kubectl apply` | The Lab11 ResourceQuota (2 pods max) blocked the rolling update from ever admitting a new pod, since a rolling update briefly needs an extra pod slot — old ReplicaSet's pod kept running indefinitely | Deleted the stale running pod (or the whole Deployment) to free the quota slot, allowing the newest ReplicaSet's pod to finally be admitted |
| `curl http://ivolve.com/...` → `502 Bad Gateway` | Symptom of the above issues — the app wasn't listening yet because it hadn't connected to MySQL | Resolved once the underlying MySQL connection issues were fixed |

### Key takeaway from this lab

A ResourceQuota sized exactly to current usage can silently block Deployment rollouts, since Kubernetes' default rolling update strategy needs a temporary extra pod slot to run old and new pods side by side. Worth accounting for quota headroom in namespaces where deployments are updated frequently.
