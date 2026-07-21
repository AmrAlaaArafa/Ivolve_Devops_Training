# Lab 18: Control Pod-to-Pod Traffic via Network Policy

## Objective
Define a NetworkPolicy that restricts incoming traffic to MySQL pods, allowing connections only from application pods on port 3306.

- `podSelector: app: mysql` — this policy applies to pods matching this label (the MySQL StatefulSet's pod).
- `policyTypes: [Ingress]` — only incoming traffic is restricted; outbound traffic from MySQL is unaffected.
- `ingress.from.podSelector: app: nodejs-app` — only pods carrying this label are permitted to initiate connections.
- `ports: 3306` — even permitted pods are restricted to this port only.

## Apply It

```bash
notepad allow-app-to-mysql.yml
kubectl apply -f allow-app-to-mysql.yml
```
<img width="1177" height="105" alt="image" src="https://github.com/user-attachments/assets/ba16a77e-8b65-49a2-a142-a18b084a00f3" />


## Verify it

```bash
kubectl describe networkpolicy allow-app-to-mysql -n ivolve
```
<img width="1302" height="347" alt="image" src="https://github.com/user-attachments/assets/bad3bcb9-2da5-4520-9aae-fa694ccc3ccc" />


## Testing Methodology

To prove the policy actually restricts traffic, a separate pod without the `app: nodejs-app` label was created and used to attempt a MySQL connection.

Since the `ivolve` namespace's ResourceQuota (Lab11) caps pods at 2, and both slots were already in use (`mysql-statefulset-0` + the running `nodejs-app` pod), the quota was temporarily raised to 3 to allow the test pod to run alongside the existing pods without disturbing them:

```bash
kubectl edit resourcequota resource-pod -n ivolve
# pods: "2"  →  pods: "3"
```
<img width="1217" height="90" alt="image" src="https://github.com/user-attachments/assets/f1acec67-12d6-4f36-9ca0-57818b5dfe17" />

Test pod, deliberately labeled to NOT match the policy's allowed selector:

```bash
kubectl run test-blocked --image=mysql:5.7 -n ivolve --labels="app=other"
```

Attempted connection:
```bash
kubectl exec -it test-blocked -n ivolve -- mysql -h mysql -u amr -pamr123 -e "SHOW DATABASES;"
```

## Result — Connection Succeeded Despite the Label Mismatch

```
+--------------------+
| Database           |
+--------------------+
| information_schema |
| ivolve             |
| performance_schema |
+--------------------+
```

The `test-blocked` pod, which does **not** carry the `app: nodejs-app` label, was still able to connect to MySQL on port 3306 — meaning the NetworkPolicy is **not being enforced**.

## why?-> CNI Plugin Without NetworkPolicy Support

`NetworkPolicy` is a Kubernetes API object, but enforcing it is the responsibility of the cluster's **CNI (Container Network Interface) plugin** — not the Kubernetes control plane itself.
Minikube's default CNI does not implement NetworkPolicy enforcement. This means:

- `kubectl apply` succeeds without error.
- `kubectl describe networkpolicy` correctly shows the policy's configuration.


## How This Would Be Fixed

Recreating the cluster with a CNI that supports NetworkPolicy (e.g. Calico) enables real enforcement:

```bash
minikube start --cni=calico --nodes 2
```

This was not done for this lab, since it would require rebuilding every prior lab (Lab11 onward) on a fresh cluster.
The NetworkPolicy manifest itself is correct and would enforce as intended on a CNI that supports it — this is a networking-layer limitation of the test environment, not an error in the policy definition.

## Key Takeaway

A `NetworkPolicy` resource being accepted by the API server is **not proof that it's enforced**. Enforcement depends entirely on the cluster's CNI plugin supporting the NetworkPolicy API (Calico, Cilium, Weave Net, and others do; many lightweight/default setups, including minikube's default CNI, do not). Always verify enforcement with an actual connectivity test rather than assuming a successfully-applied policy is doing anything.
