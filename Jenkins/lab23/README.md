# Lab 23: CI/CD Pipeline Implementation with Shared Libraries

## Objective
Build a pipeline with  `BuildImage`, and `DeployOnK8s` stages, using a Jenkins Shared Library for reusable logic



## Jenkins Shared Library

Shared Libraries let multiple pipelines call the same reusable step functions instead of duplicating logic. Structure:

```
shared-library/
└── vars/
    ├── buildImage.groovy
    └── deployToK8s.groovy
```



**Manage Jenkins → System → Global Trusted Pipeline Libraries**:

- **Name**: `ivolve-shared-library`
- **Default version**: `main`
- **Retrieval method**: Modern SCM → Git
- **Repository URL**: this repo's URL
- **Library Path**: `Kubernetes/Lab23/shared-library` (since the library lives in a subdirectory of the main training repo rather than its own dedicated repo)

## Console Output
<img width="1917" height="927" alt="image" src="https://github.com/user-attachments/assets/78c97be5-417c-422a-a50f-d9742205e17d" />
<img width="1895" height="937" alt="image" src="https://github.com/user-attachments/assets/b54f0b10-2f49-440f-b798-4661a2d05803" />
<img width="1892" height="922" alt="image" src="https://github.com/user-attachments/assets/3aad7e4f-3bfb-4dbb-8819-462094ac7ef4" />
<img width="1906" height="827" alt="image" src="https://github.com/user-attachments/assets/b2121443-f175-4838-9ca5-f31718155d08" />


