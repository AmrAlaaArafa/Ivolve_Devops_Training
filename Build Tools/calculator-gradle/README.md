# Build Tools with  Gradle
The steps include cloning the source code, running unit tests, packaging the applications, and running them.

## Step 1 : Clone the Repository
```
git clone https://github.com/Ibrahim-Adel15/calculator-gradle.git
    cd calculator-gradle
```

<img width="1127" height="592" alt="image" src="https://github.com/user-attachments/assets/d07aaa96-8a11-4c46-8b7a-2c5d82c28cf4" />


## Step 2 : isntall gradle
```scoop install gradle```

## Step 3 : JDK not found
```winget install Microsoft.OpenJDK.17```

<img width="1151" height="471" alt="image" src="https://github.com/user-attachments/assets/deab83dc-6a4e-4cad-a183-a695b53de0cf" />


## Step 4 : testing the app
```gradle test```
<img width="1382" height="562" alt="image" src="https://github.com/user-attachments/assets/0db44334-d11c-4b89-8647-7ca73c54a15f" />

## Step 4 : building the app and verifying the reports
```gradle build```
<img width="1465" height="225" alt="image" src="https://github.com/user-attachments/assets/900fa2fb-c59b-432b-918e-2be73c52f187" />
<img width="942" height="697" alt="image" src="https://github.com/user-attachments/assets/2e69e82e-8b1f-45fe-9f21-0de2cf5a60cc" />



## Step 5 : Run the app and verify it's working
```java -jar build\libs\calculator.jar```
<img width="870" height="215" alt="image" src="https://github.com/user-attachments/assets/07e0a01c-a522-46cb-89f9-ea4c594607dc" />

