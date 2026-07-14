# Lab 11: Namespace Management and Resource Quota Enforcement

## Objective
Create a dedicated Kubernetes namespace and enforce a hard limit on the number of pods allowed within it, using a ResourceQuota.


(Reuses the same 2-node cluster from [lab10]).)

## Step 1: Create the `ivolve` Namespace

```bash
kubectl create namespace ivolve
kubectl get namespaces
```
<img width="787" height="226" alt="image" src="https://github.com/user-attachments/assets/4e611871-08d8-4d13-9d0a-38bf68765a43" />




## Step 2: Create ResourceQuota & Apply the Quota

```bash
notepad resource-quota.yml
kubectl apply -f .\resource-quota.yml
kubectl get resourcequota -A
kubectl describe resourcequota resource-pod -n ivolve
```
<img width="1227" height="212" alt="image" src="https://github.com/user-attachments/assets/be3f3a48-2c95-465c-8e87-b5a8c26b54d3" />




## Step 3: Test Enforcement

Create a deployment requesting 3 replicas, to prove only 2 are admitted:

```bash
kubectl create deployment front-test --image=frontend-app --replicas=3 -n ivolve
kubectl describe resourcequota resource-pod -n ivolve
```
<img width="1262" height="137" alt="image" src="https://github.com/user-attachments/assets/88c4b13b-d1ab-4a14-b0a1-9f2e3370f9e9" />



## Summary

A **ResourceQuota** enforces hard limits on aggregate resource consumption **within a single namespace**.
This is a core mechanism for multi-tenant clusters — preventing one team or application from consuming unbounded cluster resources, regardless of how many deployments or pods they attempt to create.

