pipeline {
    agent {
        docker {
            image 'atlassianlabs/docker-node-jdk-chrome-firefox'
            args '-v /root/.m2:/root/.m2 -p 8888:8080'
        }
    }
    stages {
        stage('Build') {
            steps {
                sh 'mvn -B -DskipTests clean package'
            }
        }
        stage('Test') {
             steps {
                 sh 'mvn test'
             }
             post {
                 always {
                    junit 'backend/target/surefire-reports/*.xml'
                 }
             }
        }
        stage('Deliver') {
             steps {
                 sh './start.sh'
             }
        }
    }
}