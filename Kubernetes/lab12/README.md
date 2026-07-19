# Lab 12: Managing Configuration and Sensitive Data with ConfigMaps and Secrets

## Objective
Store non-sensitive MySQL configuration in a ConfigMap and sensitive MySQL credentials in a Secret

## Refering to lab 9

# to encode using powershell

```bash
 [Convert]::ToBase64String([Text.Encoding]::UTF8.GetBytes("amr123"))
  [Convert]::ToBase64String([Text.Encoding]::UTF8.GetBytes("rootpassword"))
```
<img width="1456" height="152" alt="image" src="https://github.com/user-attachments/assets/684ac353-fbf4-4ad6-926b-645c3780947c" />

# Creating the Configmap.yml & the Secret.yml
```bash
notepad configmap.yml
notepad secret.yml
```
<img width="1012" height="56" alt="image" src="https://github.com/user-attachments/assets/f44cffbd-b0ca-45e0-af78-35f541a95db6" />

'Note:' type: Opaque — the generic Secret type for arbitrary key-value data
# Applying the yaml files
```bash
kubectl apply -f configmap.yaml
kubectl apply -f secret.yaml
```
<img width="1007" height="117" alt="image" src="https://github.com/user-attachments/assets/4af5ab5f-577b-4b35-8d32-893225caa2ee" />

# Verifying their creation
```bash
kubectl get configmap
kubectl get secret
```
<img width="962" height="185" alt="image" src="https://github.com/user-attachments/assets/79b9b3b8-e6e7-4036-b93b-554b955f8042" />




## Summary

| Object | Purpose | Encoding | Who should consume it |
|---|---|---|---|
| ConfigMap | Non-sensitive config (hostnames, usernames, flags) | Plaintext | Any pod that needs the config |
| Secret | Sensitive data (passwords, tokens, keys) | Base64 (not encryption) | Only pods that specifically need that value — inject individual keys rather than the whole Secret where possible |
