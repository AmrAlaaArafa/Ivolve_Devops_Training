<img width="1447" height="177" alt="image" src="https://github.com/user-attachments/assets/c3827968-a8df-420a-8ffb-d20419cc8d59" /># Lab 14: StatefulSet with Headless Service

## Objective
Deploy MySQL as a StatefulSet with a headless service, backed by a PersistentVolumeClaim, consuming its root password from a Secret, and tolerating the `node=worker:NoSchedule` taint .


This cluster has 2 nodes, with the taint   applied to the worker:
```
Name:               minikube
Taints:             <none>
Name:               minikube-m02
Taints:             node=worker:NoSchedule
```


# The `ivolve` namespace, `mysql-config` ConfigMap, and `mysql-secret` Secret (lab11/lab12) 

```bash
 notepad mysql.pv.yml
 notepad mysql.pvc.yml
 notepad mysql-headless-service.yml
 notepad mysql-statefulset.yml
```
<img width="966" height="102" alt="image" src="https://github.com/user-attachments/assets/38bfbf9c-ae45-4c8f-9610-ff1a2e90a6c5" />




## Apply Everything

```bash
kubectl apply -f ..\lab12\configmap.yml
kubectl apply -f ..\lab12\secret.yml
kubectl apply -f mysql-pv.yml
kubectl apply -f mysql-pvc.yml
kubectl apply -f mysql-headless-service.yml
kubectl apply -f mysql-statefulset.yml
```
<img width="1072" height="212" alt="image" src="https://github.com/user-attachments/assets/62d513ec-e63a-4000-ab0e-44cee6fd2eff" />


## Verify the apply &  Making sure the pod laneded on the right node

```bash
kubectl get statefulset -n ivolve
kubectl get pods -n ivolve -o wide
```
<img width="1447" height="177" alt="image" src="https://github.com/user-attachments/assets/9cf867b6-6787-4fb6-92df-57387ff7db3c" />


```bash
kubectl get pvc -n ivolve
```
<img width="1452" height="147" alt="image" src="https://github.com/user-attachments/assets/23ae17a1-b9e3-4054-b911-addbdb114013" />

## Confirm the Database Is Operational

```bash
kubectl exec -it mysql-statefulset-0 -n ivolve -- sh
mysql -u "root" -p
SHOW DATABASES;
```
<img width="1295" height="642" alt="image" src="https://github.com/user-attachments/assets/25687885-40c6-41ee-8849-0a134fe10875" />


## NOTES:
# `accessModes: ReadWriteOnc `:a MySQL data directory should only ever be written by a single instance at a time; sharing it across multiple writers would corrupt the database.
# `clusterIP: None` makes this headless — DNS resolves directly to individual pod IPs (e.g. `mysql-statefulset-0.mysql-headless.ivolve.svc.cluster.local`) instead of load-balancing through a single virtual IP.
# This is required for StatefulSets, since each replica needs a stable, individually addressable network identity

## Key Takeaway

| Concept | Role |
|---|---|
| StatefulSet | Manages pods with stable network identities and stable storage — unlike a Deployment, replicas aren't interchangeable. |
| Headless Service (`clusterIP: None`) | Required for StatefulSets — provides per-pod DNS records instead of a single load-balanced IP. |
| Toleration | Permits (does not force) scheduling onto a tainted node. |
| PVC in a StatefulSet | Provides durable storage for stateful data (`/var/lib/mysql`) that survives pod restarts. |
