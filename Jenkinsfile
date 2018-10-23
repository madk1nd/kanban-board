pipeline {
	agent any
	stages {
		stage('Build') {
			steps {
				sh 'mvn -B -DskipTests clean package -P prod'
			}
		}
		stage('Test') {
			steps {
				sh 'mvn test -P prod'
			}
		}
		stage('Deploy') {
			when {
				branch 'deploy'
			}
			steps {
				sh 'docker-compose up -d --build'
			}
		}
	}
}
