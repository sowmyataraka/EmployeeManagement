
pipeline {

    agent any

    tools {
        jdk 'JDK21'
        maven 'Maven3'
    }

    options {
        timestamps()
        disableConcurrentBuilds()
        skipDefaultCheckout(true)
        buildDiscarder(logRotator(numToKeepStr: '10'))
    }

    stages {

        // 1. Checkout the branch or PR revision detected by Jenkins
        stage('Checkout') {
            steps {
                echo "Branch: ${env.BRANCH_NAME}"
                echo "Pull Request ID: ${env.CHANGE_ID ?: 'Not a PR'}"
                echo "PR Target: ${env.CHANGE_TARGET ?: 'N/A'}"

                checkout scm
            }
        }

        // 2. Compile on every branch and PR
        stage('Compile') {
            steps {
                echo 'Compiling Employee Management application'
                sh 'mvn -B clean compile'
            }
        }

        // 3. Unit tests on every branch and PR
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
                    changeRequest()
                }
            }
            steps {
                echo 'Running complete unit and integration test suite'
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
