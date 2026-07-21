# Lab 17: Pod Resource Management with CPU and Memory Requests and Limits

## Objective
Update the existing Node.js Deployment to set explicit CPU/memory requests and limits, then verify them via `kubectl describe pod` and monitor real usage with `kubectl top pod`.

## modifying the deployment yaml file

<img width="767" height="385" alt="image" src="https://github.com/user-attachments/assets/1729dfba-8c38-4fd1-9799-05bc7976a544" />



## Apply the Update and Verify

```bash
kubectl apply -f lab15-16/nodejs-deployment.yml
kubectl describe pod nodejs-app-769549cc7f-fz5rr -n ivolve
```
<img width="1152" height="416" alt="image" src="https://github.com/user-attachments/assets/b76052a5-62c7-4b4d-ab72-d3bd8f4388b4" />




## Enable Metrics Server and Monitor Real-Time Usage with `kubectl top`

Not enabled by default on minikube:

```bash
minikube addons enable metrics-server
kubectl top pod -n ivolve
```
<img width="892" height="105" alt="image" src="https://github.com/user-attachments/assets/90d85a15-d065-4dfd-954d-3ae3a76bfe3e" />


Setting both appropriately prevents a single pod from getting all the resources  on the same node while still giving the scheduler accurate information about how much capacity a pod actually needs.
