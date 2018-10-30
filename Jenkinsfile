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
				sh 'mvn surefire:test -P prod'
			}
		}
		stage('Deploy') {
			when {
				branch 'master'
			}
			steps {
				sh 'docker-compose up -d --build'
			}
		}
	}
}
