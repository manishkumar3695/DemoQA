pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
            withMaven   {
                sh 'mvn clean test -Dcucumber.options="src/main/resources/featureFiles --tags @Manish"'
            }
            }
        }
    }
}