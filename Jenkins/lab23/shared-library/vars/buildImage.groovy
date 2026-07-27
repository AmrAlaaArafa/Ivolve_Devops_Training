
def call(Map config) {
    String appDir    = config.appDir
    String imageName = config.imageName
    String tag       = config.tag ?: env.BUILD_NUMBER

    dir(appDir) {
        sh "docker build -t ${imageName}:${tag} ."
    }

    withCredentials([usernamePassword(credentialsId: 'dockerhub', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
        sh """
            docker login -u "\$DOCKER_USER" -p "\$DOCKER_PASS"
            docker push ${imageName}:${tag}
        """
    }

    sh "docker rmi ${imageName}:${tag} || true"
    echo "buildImage: built, pushed, and removed local copy of ${imageName}:${tag}"
}
