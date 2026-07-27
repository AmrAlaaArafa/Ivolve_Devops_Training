

def call(Map config) {
    String k8sDir         = config.k8sDir
    String deploymentFile = config.deploymentFile
    String imageName      = config.imageName
    String tag            = config.tag ?: env.BUILD_NUMBER
    String server         = config.server

    dir(k8sDir) {
        sh """
            sed -i "s|image .*|image: ${imageName}:${tag}|" ${deploymentFile}
        """
    }

    withCredentials([string(credentialsId: 'serviceaccount-token', variable: 'TOKEN')]) {
        sh """
            kubectl apply -f ${k8sDir}/${deploymentFile} --server=${server} --token=\$TOKEN --insecure-skip-tls-verify=true --validate=false
        """
    }

    echo "deployToK8s: ${imageName}:${tag} deployed via ${k8sDir}/${deploymentFile}"
}
