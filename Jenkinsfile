
pipeline {
    agent any

    tools {
        maven 'Maven'
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build and Test') {
            steps {
                bat 'mvn -B clean verify'
            }
        }

        stage('Archive JAR and WAR') {
            steps {
                archiveArtifacts artifacts: 'core/target/*.jar, web-app/target/*.war',
                                 fingerprint: true
            }
        }

        stage('Deploy to Nexus') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'nexus-credentials',
                        usernameVariable: 'NEXUS_USER',
                        passwordVariable: 'NEXUS_PASS'
                    )
                ]) {
                    script {
                        writeFile file: 'nexus-settings.xml',
                            text: """<settings>
                              <servers>
                                <server>
                                  <id>nexus-snapshots</id>
                                  <username>\${NEXUS_USER}</username>
                                  <password>\${NEXUS_PASS}</password>
                                </server>
                                <server>
                                  <id>nexus-releases</id>
                                  <username>\${NEXUS_USER}</username>
                                  <password>\${NEXUS_PASS}</password>
                                </server>
                              </servers>
                            </settings>"""
                    }

                    bat 'mvn -B -s nexus-settings.xml deploy -DskipTests'
                }
            }
            post {
                always {
                    bat 'if exist nexus-settings.xml del /f /q nexus-settings.xml'
                }
            }
        }

        stage('Deploy to Tomcat') {
            steps {
                deploy adapters: [
                    tomcat9(
                        credentialsId: 'tomcat-credentials',
                        path: '',
                        url: 'http://localhost:8082'
                    )
                ],
                contextPath: 'jenkins-demo',
                war: 'web-app/target/demo-web.war'
            }
        }
    }

    post {
        always {
            junit allowEmptyResults: true,
                  testResults: '**/target/surefire-reports/*.xml'
        }

        success {
            emailext(
                to: 'YOUR_GMAIL@gmail.com',
                subject: "SUCCESS: Deployment completed - ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: """
Hello,

Your application deployment has been completed successfully!

Job: ${env.JOB_NAME}
Build Number: ${env.BUILD_NUMBER}
Status: SUCCESS

Application URL:
http://localhost:8082/jenkins-demo/

Jenkins Build:
${env.BUILD_URL}

Regards,
Jenkins CI/CD Pipeline
"""
            )
        }

        failure {
            emailext(
                to: 'YOUR_GMAIL@gmail.com',
                subject: "FAILED: Deployment failed - ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: """
Hello,

Your Jenkins deployment pipeline has failed.

Job: ${env.JOB_NAME}
Build Number: ${env.BUILD_NUMBER}
Status: FAILURE

Please check the Jenkins console output:
${env.BUILD_URL}console

Regards,
Jenkins CI/CD Pipeline
"""
            )
        }
    }
}
