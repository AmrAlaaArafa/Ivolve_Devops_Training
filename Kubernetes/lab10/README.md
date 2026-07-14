# Lab 10: Node Isolation Using Taints in Kubernetes

## Objective
Run a 2-node Kubernetes cluster and isolate one node from regular pod scheduling by applying a taint, then verify the taint is correctly applied.

## Tooling
- [minikube](https://minikube.sigs.k8s.io/) — used to run a local multi-node Kubernetes cluster
- `kubectl` — Kubernetes CLI

## Step 1: Start a 2-Node Cluster

```bash
minikube start --nodes 2
```
<img width="1462" height="695" alt="image" src="https://github.com/user-attachments/assets/0a87c414-001e-4a92-9049-1e54dc37e1a9" />


## Step 2: Verify Both Nodes Are Up

```bash
kubectl get nodes
```
<img width="867" height="112" alt="image" src="https://github.com/user-attachments/assets/ae16f0f4-8395-49c8-89b3-79a7a4b94d29" />



## Step 3: Taint the Worker Node

```bash
kubectl taint nodes minikube-m02 node=worker:NoSchedule
```
<img width="1250" height="75" alt="image" src="https://github.com/user-attachments/assets/b554791a-f779-4c34-867d-c64af0393807" />


pods without a matching toleration will not be scheduled onto this node.

## Step 4: Describe All Nodes to Verify the Taint

```bash
kubectl describe nodes
```
<img width="1250" height="75" alt="image" src="https://github.com/user-attachments/assets/203b28ce-0c2f-4b85-81b8-d50a2eaa0ad0" />

OR
```bash
kubectl describe node minikube-m02 | findstr Taints
```
<img width="1212" height="52" alt="image" src="https://github.com/user-attachments/assets/37972ba1-98d1-45e4-916c-af04425dfbd8" />



## Summary

A **taint** is applied to a node to repel pods from being scheduled there unless the pod explicitly has a matching **toleration**. This is the opposite mechanism of node affinity/selectors (which pods use to request specific nodes) 
taints let the *node* refuse pods, rather than pods requesting nodes.

| Effect | Behavior |
|---|---|
| `NoSchedule` | New pods without a matching toleration will not be scheduled here. Existing pods already running are unaffected. |
| `PreferNoSchedule` | Scheduler tries to avoid this node, but will use it if no other option exists. |
| `NoExecute` | New pods won't be scheduled, and existing pods without a toleration are evicted. |

