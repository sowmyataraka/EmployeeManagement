pipeline {

    agent any

    tools {
        jdk 'JDK21'
        maven 'Maven3'
    }

    options {
        timestamps()
        disableConcurrentBuilds()
    }

    stages {

        // 1. Checkout GitHub Repository
        stage('Checkout') {
            steps {
                echo 'Checking out Employee Management repository'

                git branch: 'main',
                    url: 'https://github.com/sowmyataraka/EmployeeManagement.git'
            }
        }

        // 2. Compile Application
        stage('Compile') {
            steps {
                echo 'Compiling Employee Management application'

                sh 'mvn -B clean compile'
            }
        }

        // 3. Unit Testing - JUnit and Mockito
        stage('Unit Testing') {
            steps {
                echo 'Running EmployeeServiceTest'

                sh 'mvn -B -Dtest=EmployeeServiceTest test'
            }
        }

        // 4. API Integration Testing - Spring Boot
       
stage('API Integration Testing') {
    steps {
        echo 'API integration tests are not implemented yet - skipping'
    }
}
    
        // 5. Full Test Suite
        stage('Full Test Suite') {
            steps {
                echo 'Running all unit and integration tests'

                sh 'mvn -B test'
            }
        }

        // 6. Package Spring Boot JAR
        stage('Package Application') {
            steps {
                echo 'Packaging Employee Management application'

                sh 'mvn -B package -DskipTests'
            }
        }

        // 7. Archive JAR
        stage('Archive Application') {
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
            echo 'SUCCESS: Employee Management pipeline completed!'
        }

        failure {
            echo 'FAILURE: Please check Jenkins console output and test reports.'
        }

        cleanup {
            echo 'Employee Management pipeline execution completed.'
        }
    }
}
