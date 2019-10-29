pipeline {

    // agent any
    agent {
        label 'jenkins-slave-jnlp'
    }

    // Set Maven as tool build
    tools { 
        // maven 'Maven 3.3.9' 
        maven 'MAVEN-3.6.2' //http://jenkins.dev.k8s.claro.com.br
        // jdk 'jdk8' 
        jdk 'JAVA8' //http://jenkins.dev.k8s.claro.com.br 
    }

    environment {
        PROJECT_NAME = 'movie-catalog-service'
        NEXUS_REPOSITORY = "${PROJECT_NAME}" 
        NEXUS_PROJECT_SOURCE = "${PROJECT_NAME}/target" 
        NEXUS_FILE = "${NEXUS_PROJECT_SOURCE}/movie-catalog-service-0.0.110-SNAPSHOT.jar" 
        HARBOR_PROJECT = "poc-arquitetura" //"${PROJECT_NAME}" 
        HARBOR_IMAGE = "${PROJECT_NAME}-img" 
        SONAR_PROJECT_KEY = "${PROJECT_NAME}" 
        SONAR_PROJECT_NAME = "${PROJECT_NAME}" 
        SONAR_PROJECT_SOURCE = "${NEXUS_PROJECT_SOURCE}" 
        POST_PROD_TIME = "3"
        POST_PROD_UNIT = "MINUTES" // SECONDS, MINUTES, HOURS
    }

    // Import Shared Library source
    libraries {
        // lib('my-shared-lib@master')
        lib('JenkinsSharedLibrary@master') //http://jenkins.dev.k8s.claro.com.br
    }

    options {
        // Update status to Gitlab
        gitLabConnection('Gitlab-Connection')
    }

    stages {
        stage('Build & Tests') {
            steps {
                // IMPORT BY SHARED LIBRARY
                mavenSharedLib()
            }
        }

        stage('Code Quality') {
            steps {
                // IMPORT BY SHARED LIBRARY
                sonarqubeSharedLib()
            }
        }

        stage("Repository Artifacts") {
            parallel {
                stage("Publish to Nexus") {
                    steps {
                        // IMPORT BY SHARED LIBRARY
                        nexusSharedLib()
                    }
                }
                stage("Publish to Harbor") {
                    steps {
                        // IMPORT BY SHARED LIBRARY
                        harborSharedLib()
                    }
                }
            }
        }

        stage("Deploy") {
            parallel {
                stage('DEV') {
                    steps {
                        // IMPORT BY SHARED LIBRARY
                        kubernetesDEVSharedLib()
                    }
                }

                stage('QA') {
                    steps {
                        // IMPORT BY SHARED LIBRARY
                        kubernetesQASharedLib()
                    }
                }

                stage('PROD') {
                    steps {
                        // IMPORT BY SHARED LIBRARY
                        kubernetesPRODSharedLib()
                    }
                }
            }
        }

        stage('Post-Prod') {
            steps {
                // IMPORT BY SHARED LIBRARY
                rollbackLib()
            }
        }

    }
}