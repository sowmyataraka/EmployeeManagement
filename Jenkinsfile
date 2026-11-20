
pipeline {

    agent any

    tools {
        jdk 'JDK21'
        maven 'Maven3'
    }

    options {
        timestamps()
        disableConcurrentBuilds()
        buildDiscarder(logRotator(
            numToKeepStr: '10'
        ))
    }

    stages {

        // 1. Checkout GitHub Repository
        stage('Checkout') {
            steps {
                echo "Building branch: ${env.BRANCH_NAME}"
                checkout scm
            }
        }

        // 2. Compile application - all branches
        stage('Compile') {
            steps {
                echo 'Compiling Employee Management application'
                sh 'mvn -B clean compile'
            }
        }

        // 3. Unit testing - all branches
        stage('Unit Testing') {
            steps {
                echo 'Running EmployeeServiceTest'
                sh 'mvn -B -Dtest=EmployeeServiceTest test'
            }
        }

        // 4. Integration testing - develop and above
        stage('API Integration Testing') {
            when {
                anyOf {
                    branch 'develop'
                    branch 'release/*'
                    branch 'main'
                    branch 'hotfix/*'
                }
            }
            steps {
                echo 'Integration testing stage'

                // Replace this command when integration tests
                // have been implemented.
                echo 'Integration tests are not implemented yet'
            }
        }

        // 5. Full test suite - develop and above
        stage('Full Test Suite') {
            when {
                anyOf {
                    branch 'develop'
                    branch 'release/*'
                    branch 'main'
                    branch 'hotfix/*'
                }
            }
            steps {
                echo 'Running all unit and integration tests'

                sh 'mvn -B test'
            }
        }

        // 6. Package Spring Boot JAR
        stage('Package Application') {
            when {
                anyOf {
                    branch 'release/*'
                    branch 'main'
                    branch 'hotfix/*'
                }
            }
            steps {
                echo 'Packaging Employee Management application'
                sh 'mvn -B package -DskipTests'
            }
        }

        // 7. Archive JAR
        stage('Archive Application') {
            when {
                anyOf {
                    branch 'release/*'
                    branch 'main'
                    branch 'hotfix/*'
                }
            }
            steps {
                archiveArtifacts(
                    artifacts: 'target/*.jar',
                    fingerprint: true
                )
            }
        }
    }

    post {

        always {
            echo 'Publishing test reports'

            junit(
                testResults: 'target/surefire-reports/TEST-*.xml',
                allowEmptyResults: true
            )
        }

        success {
            echo "SUCCESS: Pipeline completed for ${env.BRANCH_NAME}"
        }

        failure {
            echo "FAILURE: Pipeline failed for ${env.BRANCH_NAME}"
            echo 'Check Jenkins console output and test reports.'
        }

        cleanup {
            echo 'Pipeline execution completed.'
        }
    }
}
