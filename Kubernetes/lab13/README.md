# Lab 13: Persistent Storage Setup for Application Logging

## Objective
Define a PersistentVolume backed by hostPath storage, and a matching PersistentVolumeClaim, to provide durable shared storage for application logs across pod replicas.

## Prerequisites

## Creating both pv and pvc yaml files
```bash
notepad pv.yml
notepad pvc.yml
```
<img width="792" height="56" alt="image" src="https://github.com/user-attachments/assets/688cbb78-828a-4cdc-8edc-06ebcfcfe485" />





## Apply Both

```bash
kubectl apply -f pv.yml
kubectl apply -f pvc.yml
```
<img width="912" height="107" alt="image" src="https://github.com/user-attachments/assets/4b38b38f-e419-4855-b259-c7b301c768fc" />


## Verify Binding

```bash
kubectl get pv
```
Expected:
```
NAME          CAPACITY   ACCESS MODES   RECLAIM POLICY   STATUS   CLAIM
app-logs-pv   1Gi        RWX            Retain           Bound    ivolve/app-logs-pvc
```

```bash
kubectl get pv,pvc
```
<img width="1790" height="285" alt="image" src="https://github.com/user-attachments/assets/4d1cef09-4dad-4fd5-a2b7-7fe5a63dfef5" />


`STATUS: Bound` on both confirms the PVC successfully matched and reserved the PV.

## Summary

| Concept | Role |
|---|---|
| PersistentVolume (PV) | Cluster-level storage resource, provisioned ahead of time (or dynamically) — defines *what* storage exists and its properties. |
| PersistentVolumeClaim (PVC) | Namespace-level request for storage — a pod references the PVC, not the PV directly, decoupling application manifests from the underlying storage implementation. |
| `ReadWriteMany` | Required when multiple pod replicas need concurrent read/write access to the same storage — e.g. shared application logs. |
| `Retain` reclaim policy | Protects data from automatic deletion when a PVC is removed — appropriate for logs or other data that shouldn't be lost accidentally. |



## Cleanup

```bash
kubectl delete pvc app-logs-pvc -n ivolve
kubectl delete pv app-logs-pv
```
