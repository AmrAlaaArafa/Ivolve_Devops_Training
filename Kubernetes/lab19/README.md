# Lab 19: Node-Wide Pod Management with DaemonSet & Prometheus

##  Overview
This lab demonstrates how to deploy a node-level monitoring agent (`node-exporter`) across all Kubernetes nodes using a **DaemonSet** with taint tolerations, and integrate it with a **Prometheus** server.

## 🛠️ Architecture & Components
* **Namespace:** `monitoring` 📁
* **DaemonSet:** `node-exporter` running on port `9100` 📊
* **Tolerations:** Configured with `operator: "Exists"` to run on all nodes (including control-plane nodes).
* **Monitoring Stack:** Prometheus server deployed via `Deployment` and `Config` to scrape node metrics.

## 🚀 Steps & Execution

## Create Monitoring Namespace
```bash
kubectl create namespace monitoring
```
<img width="1097" height="107" alt="image" src="https://github.com/user-attachments/assets/509d9136-824f-4b12-b78e-73f14b1bbb14" />

## Applying the Node-Exporter
```bash
kubectl apply -f node-exporter-daemonset.yml
```
<img width="1212" height="85" alt="image" src="https://github.com/user-attachments/assets/cbdc4e7f-93e1-4e4b-a897-aa5b3b858d99" />

## Verify the Node-Exporter
```bash
kubectl get pods -n monitoring -o wide
```
<img width="1502" height="105" alt="image" src="https://github.com/user-attachments/assets/c66dc668-b117-4497-a9f1-f5ca056df6a0" />


## TO test we need to get ip of node
```bash
kubectl get node  -o wide 
kubectl port-forward daemonset/node-exporter 9100:9100 -n monitoring
curl http://localhost:9100/metrics

```
<img width="1427" height="117" alt="image" src="https://github.com/user-attachments/assets/ef0f60e8-1e98-4c59-b90c-39703bf93241" />

<img width="1465" height="762" alt="image" src="https://github.com/user-attachments/assets/bb6eda5a-bf6d-4443-8dc0-5f1d221da513" />


## Integrating  Prometheus
```bash
kubectl apply -f .\prometheus.yml
kubectl apply -f prometheus-deployment.yml
```
<img width="1162" height="127" alt="image" src="https://github.com/user-attachments/assets/3d950fe3-80b4-4fb0-ba60-b5eaf1255496" />

## Verify Prometheus

```bash
kubectl port-forward deployment/prometheus-deployment 9090:9090 -n monitoring
```
<img width="1906" height="967" alt="image" src="https://github.com/user-attachments/assets/6f441a0f-e5ea-47ec-8dfd-c766c799e17f" />




