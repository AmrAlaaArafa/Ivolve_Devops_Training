# Build Tools with Maven and Gradle
A complete build workflows for both **Maven** and **Gradle** Java applications.
The steps include cloning the source code, running unit tests, packaging the applications, and running them.

## Step 1 :Clone the Repository
'''git clone https://github.com/Ibrahim-Adel15/calculator-gradle.git
 cd calculator-gradle'''

obsidian://open?vault=Obsidian%20Vault&file=Pasted%20image%2020260706152607.png

## Step 2 : isntall gradle
'''scoop install gradle'''

## Step 3 : JDK not found
'''winget install Microsoft.OpenJDK.17'''

obsidian://open?vault=Obsidian%20Vault&file=Pasted%20image%2020260706154455.png

## Step 4 : testing the app
'''gradle test'''
obsidian://open?vault=Obsidian%20Vault&file=Pasted%20image%2020260706155318.png

## Step 4 : building the app and verifying the reports
'''gradle build'''
obsidian://open?vault=Obsidian%20Vault&file=Pasted%20image%2020260706155655.png
obsidian://open?vault=Obsidian%20Vault&file=Pasted%20image%2020260706155805.png

## Step 5 : Run the app and verify it's working
'''java -jar build\libs\calculator.jar'''
obsidian://open?vault=Obsidian%20Vault&file=Pasted%20image%2020260706155850.png
