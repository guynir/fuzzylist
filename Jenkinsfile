pipeline {
    agent any

    environment {
        // Our local Microk8s registry.
        DOCKER_REPOSITORY = "http://10.0.1.1:32000"
    }

    stages {
        stage("Build binaries & Test") {
            parallel {
                stage('Gradle - clean, build, test') {
                    steps {
                        sh "./gradlew clean"
                        sh "./gradlew build"
                        sh "./gradlew :common:test"
                        dir ("webapp-boot") {
                            script {
                                def image = docker.build("webapp-boot:latest")
                                docker.withRegistry(env.DOCKER_REPOSITORY) {
                                    image.push()
                                }
                            }
                        }
                    }
                }

                stage('NPM: clean, build, test') {
                    steps {
                        dir ("webapp-ui") {
                            script {
                                def image = docker.build("webapp-ui:latest")
                                docker.withRegistry(env.DOCKER_REPOSITORY) {
                                    image.push()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

