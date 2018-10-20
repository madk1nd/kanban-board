pipeline {
	agent any
	stages {
		stage('Build') {
			steps {
				sh 'mvn -B -DskipTests clean package -P prod'
			}
			post {
                always {
                    emailext body: "Something is wrong with ${env.BUILD_URL}. Step Build",
                    recipientProviders: [[$class: 'DevelopersRecipientProvider'], [$class: 'RequesterRecipientProvider']],
                    subject: 'Build stage failed',
                    to: '$DEFAULT_RECIPIENTS'
                }
            }
		}
		stage('Test') {
			steps {
				sh 'mvn test -P prod'
			}
			post {
				always {
	                emailext body: "Something is wrong with ${env.BUILD_URL}. Step Test",
	                recipientProviders: [[$class: 'DevelopersRecipientProvider'], [$class: 'RequesterRecipientProvider']],
	                subject: 'Test stage failed',
	                to: '$DEFAULT_RECIPIENTS'
                }
            }
		}
		stage('Deploy') {
			when {
				branch 'deploy'
			}
			steps {
				sh 'docker-compose up -d --build'
			}
			post {
                always {
                    emailext body: "Something is wrong with ${env.BUILD_URL}. Step Deploy",
                    recipientProviders: [[$class: 'DevelopersRecipientProvider'], [$class: 'RequesterRecipientProvider']],
                    subject: 'Deploy stage failed',
                    to: '$DEFAULT_RECIPIENTS'
                }
            }
		}
	}
}
