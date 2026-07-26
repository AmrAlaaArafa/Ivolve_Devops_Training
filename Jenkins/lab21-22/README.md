# Lab 21: Role-based Authorization

## Using role-based startegy
<img width="1896" height="937" alt="image" src="https://github.com/user-attachments/assets/3e8371dd-fcfa-4a9e-bcf7-209c10d47962" />

## Making roles
<img width="1900" height="895" alt="image" src="https://github.com/user-attachments/assets/8f0b3dd5-86f3-499d-8df6-0e2d132f9f7e" />

## Assigning roles
<img width="1872" height="916" alt="image" src="https://github.com/user-attachments/assets/78a9ec05-02ae-4377-995b-5fc4c32eed9f" />





# Lab 22: Jenkins Pipeline for Application Deployment

## Objective
Automate building, pushing, and deploying the Node.js application to Kubernetes using a Jenkins declarative pipeline 
building on the Docker image from [Lab9](../../Docker/lab9) and deploying the complete stack built across Labs 11–18.



## Prerequisites

- Jenkins running with Docker CLI available inside its execution environment (Docker socket mounted).
- A Kubernetes ServiceAccount (`jenkins-sa`) in the `ivolve` namespace, scoped with RBAC permissions .
- Jenkins credentials configured:
   — Username/Password credential for Docker Hub.
   — Secret text credential containing the ServiceAccount's token.
note: No need for github repo is public
<img width="1865" height="567" alt="image" src="https://github.com/user-attachments/assets/e4f2bf4d-c620-4d2c-8bb0-e73ada2a2ef7" />


Apply and generate a long-lived token:

```bash
kubectl apply -f jenkins-role.yml
kubectl apply -f jnekins-rolebinding.yml
kubectl create token jenkins-sa -n ivolve --duration=87600h
```



### Reaching the cluster's API server from Jenkins


```bash
kubectl cluster-info

```
 Docker Desktop's special DNS name `host.docker.internal` is used instead to reach the host machine's forwarded API server port:
 From inside Jenkins: `https://host.docker.internal:<port>`

## Success

<img width="1915" height="992" alt="image" src="https://github.com/user-attachments/assets/68e7f969-b995-45ff-9ea1-0f35c4ba9f48" />

## Console Output

<img width="1877" height="912" alt="image" src="https://github.com/user-attachments/assets/ce5ef733-dd4b-4f00-957c-d2b5e49ca074" />
<img width="1916" height="977" alt="image" src="https://github.com/user-attachments/assets/4eb70418-f063-49f0-b700-317e1a24bd09" />
<img width="1917" height="977" alt="image" src="https://github.com/user-attachments/assets/e1e7c5da-e670-4d48-9a55-5cf462df53d5" />


### Design notes

- **Unique image tags via `BUILD_NUMBER`** instead of `latest` — every pipeline run produces a traceable, distinct image.
- **`withCredentials` with single-quoted `sh` blocks** — avoids Jenkins' Groovy string interpolation security warning; the shell substitutes `$TOKEN` at runtime instead of Groovy embedding it directly into the command string (which would otherwise risk leaking the secret in process listings).
- **RBAC scoped to the `ivolve` namespace only**, with only the specific resource verbs the pipeline actually needs — not cluster-admin.





## Troubleshooting Encountered

| Issue | Cause | Fix |
|---|---|---|
| `Forbidden: cannot get resource "deployments"` | Token belonged to a ServiceAccount without a matching RBAC Role/RoleBinding | Created Role + RoleBinding scoped to the exact ServiceAccount name in use, with the full set of resource verbs the pipeline needs |
| Jenkins couldn't reach the cluster via `127.0.0.1` | Jenkins runs in its own container; `127.0.0.1` there refers to the Jenkins container, not the host running minikube | Used Docker Desktop's `host.docker.internal` DNS name instead |

