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
        pom = readMavenPom file: "${PROJECT_NAME}/pom.xml";
        PROJECT_NAME = 'movie-catalog-service'

        NEXUS_REPOSITORY = "ClaroBrasil/Microservices" 
        NEXUS_PROJECT_SOURCE = "${PROJECT_NAME}/target" 
        NEXUS_FILE = "${NEXUS_PROJECT_SOURCE}/${PROJECT_NAME}-${pom.version}.${pom.packaging}"
        
        HARBOR_PROJECT = "poc-arquitetura" //"${PROJECT_NAME}" 
        HARBOR_IMAGE = "${PROJECT_NAME}-img" 
        
        SONAR_PROJECT_KEY = "${PROJECT_NAME}" 
        SONAR_PROJECT_NAME = "${PROJECT_NAME}" 
        SONAR_PROJECT_BINARY = "${PROJECT_NAME}/target/classes"
        SONAR_PROJECT_SOURCE = "${PROJECT_NAME}/src"

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
            when {
                // only master/develop/release branches publishes to Nexus
                // only master/develop/release branches publishes to Harbor
                anyOf { branch 'develop'; branch 'release/*'; branch 'master' }
            }
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

        stage('Deploy DEV') {   
            when {
                // only master/develop/release branches deploy to DEV
                anyOf { branch 'develop'; branch 'release/*'; branch 'master' }
            }
            steps {
                // IMPORT BY SHARED LIBRARY
                kubernetesDEVSharedLib()
            }
        }

        stage ('Intergation Tests') {
            when {
                // only master/develop/release branches goes throught Integration test
                anyOf { branch 'develop'; branch 'release/*'; branch 'master' }
            }
            steps {
                // IMPORT BY SHARED LIBRARY
                integrationTestLib()
            }
        }

        stage ('DAST and Penetration tests') {
            when {
                // only master/develop/release branches goes throught DAST /  Penetration test
                anyOf { branch 'develop'; branch 'release/*'; branch 'master' }
            }
            steps {
                // IMPORT BY SHARED LIBRARY
                penetrationTestLib()
            }
        }

        stage('Deploy QA') {
            when {
                // only master/release branches deploy to QA
                anyOf { branch 'release/*'; branch 'master' }
            }
            steps {
                // IMPORT BY SHARED LIBRARY
                kubernetesQASharedLib()
            }
        }

        stage ('Performance Tests') {
            when {
                // only master/release branches goes throught performance test
                anyOf { branch 'release/*'; branch 'master' }
            }
            steps {
                // IMPORT BY SHARED LIBRARY
                performanceTestLib()
            }
        }

        stage('Deploy PROD') {
            when {
                // only master branches deploy to PROD
                branch 'master'
            }
            steps {
                // IMPORT BY SHARED LIBRARY
                kubernetesPRODSharedLib()
            }
        }

        stage('Post PROD') {
            when {
                // only master branches undeploy from PROD
                branch 'master'
            }
            steps {
                // IMPORT BY SHARED LIBRARY
                rollbackLib()
            }
        }
    }
}