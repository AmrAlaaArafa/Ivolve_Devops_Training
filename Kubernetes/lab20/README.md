

## Lab 20: Securing Kubernetes with RBAC and Service Accounts

## 🎯 Overview
This lab covers securing Kubernetes workloads by setting up **Role-Based Access Control (RBAC)** for a ServiceAccount (`jenkins-sa`) with least-privilege permissions.

## 🛠️ RBAC Components
* **Namespace:** `ivolve` 📁
* **ServiceAccount:** `jenkins-sa` 👤
* **Role:** `pod-reader` (grants `get` and `list` on `pods`) 📜
* **RoleBinding:** `read-pods` (binds `jenkins-sa` to `pod-reader`) 🔗

## 🚀 Steps & Execution

##  Creating the service account inside ivolve namepsace
```bash
 kubectl create serviceaccount jenkins-sa -n ivolve
 kubectl create token jenkins-sa --duration=24h -n ivolve
```
<img width="1487" height="287" alt="image" src="https://github.com/user-attachments/assets/e32bd63d-1051-439c-bb53-a214603a293f" />


##  Creating the Role and Role binding yaml files and applying them
```bash
kubectl apply -f pod-reader-role.yml
kubectl apply -f pod-reader-rolebinding.yml
```
<img width="1255" height="175" alt="image" src="https://github.com/user-attachments/assets/fecbcc7b-a5b9-4e16-969f-0a57feb74d20" />




## Validate the role can only list pods.
```bash
 kubectl auth can-i list pods --as=system:serviceaccount:ivolve:jenkins-sa -n ivolve
 kubectl auth can-i delete pods --as=system:serviceaccount:ivolve:jenkins-sa -n ivolve
```

