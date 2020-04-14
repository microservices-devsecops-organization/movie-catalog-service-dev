// ===============================================================================================================
// If the project doesn't use sidecar, all stages/steps and variables declaration for this purpose must be removed
// If the project uses more than one sidecar, then, a new stage/step and variable declaration for this sidecar must be added 
// ===============================================================================================================
def PRINCIPAL_PROJECT = 'movie-catalog-service'
def SIDECAR_PROJECT = 'movie-catalog-service-sidecar-fatura'
def PROJECTS = [PRINCIPAL_PROJECT,SIDECAR_PROJECT]

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
        HARBOR_PROJECT = "poc-arquitetura" 
        RANCHER_NAMESPACE = "poc-arqtec"
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
            parallel {
                stage('Principal project') {
                    steps {
                        // IMPORT BY SHARED LIBRARY
                        mavenSharedLibV2(PRINCIPAL_PROJECT)
                    }
                }
                stage('Sidecar project') {
                    steps {
                        // IMPORT BY SHARED LIBRARY
                        mavenSharedLibV2(SIDECAR_PROJECT)
                    }
                }
            }
        }

        stage('Code Quality for principal project') {
            steps {
                // IMPORT BY SHARED LIBRARY
                sonarqubeSharedLibV2(PRINCIPAL_PROJECT)
            }
        }

        stage('Code Quality for sidecar project') {
            steps {
                // IMPORT BY SHARED LIBRARY
                sonarqubeSharedLibV2(SIDECAR_PROJECT)
            }
        }

        stage('Principal project') {
            when {
                // only master/develop/release branches publishes to Nexus
                // only master/develop/release branches publishes to Harbor
                anyOf { branch 'develop'; branch 'release/*'; branch 'master' }
            }
            parallel {
                stage("Publish to Nexus") {
                    steps {
                        // IMPORT BY SHARED LIBRARY
                        nexusSharedLibV2(PRINCIPAL_PROJECT)
                    }
                }
                stage("Publish to Harbor") {
                    steps {
                        // IMPORT BY SHARED LIBRARY
                        harborSharedLibV2(PRINCIPAL_PROJECT)
                    }
                }
            }
        }

        stage('Sidecar project') {
            when {
                // only master/develop/release branches publishes to Nexus
                // only master/develop/release branches publishes to Harbor
                anyOf { branch 'develop'; branch 'release/*'; branch 'master' }
            }
            // for sidecar
            parallel {
                stage("Publish to Nexus") {
                    steps {
                        // IMPORT BY SHARED LIBRARY
                        nexusSharedLibV2(SIDECAR_PROJECT)
                    }
                }
                stage("Publish to Harbor") {
                    steps {
                        // IMPORT BY SHARED LIBRARY
                        harborSharedLibV2(SIDECAR_PROJECT)
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
                // TODO
                kubernetesDeployLib('dev',PROJECTS)
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
                kubernetesDeployLib('hom',PROJECTS)
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
                kubernetesDeployLib('prod',PROJECTS)
            }
        }

        stage('Post PROD') {
            when {
                // only master branches undeploy from PROD
                branch 'master'
            }
            steps {
                // IMPORT BY SHARED LIBRARY
                rollbackLibV2(PRINCIPAL_PROJECT)
            }
        }
    }
}
