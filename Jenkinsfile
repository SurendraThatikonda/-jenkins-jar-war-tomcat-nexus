pipeline {
    agent any
    // Must match the Maven installation Name under Manage Jenkins > Tools.
    tools { maven 'Maven' }

    stages {
        stage('Checkout') { steps { echo 'Source checked out from GitHub' } }
        stage('Build and Test') { steps { bat 'mvn -B clean verify' } }
        stage('Archive JAR and WAR') {
            steps {
                archiveArtifacts artifacts: 'core/target/*.jar,web-app/target/*.war', fingerprint: true
            }
        }
        stage('Deploy to Nexus') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'nexus-credentials', usernameVariable: 'NEXUS_USER', passwordVariable: 'NEXUS_PASSWORD')]) {
                    script {
                        def esc = { v -> v.replace('&','&amp;').replace('<','&lt;').replace('>','&gt;').replace('"','&quot;').replace("'",'&apos;') }
                        writeFile file: 'nexus-settings.xml', text: """<?xml version="1.0" encoding="UTF-8"?>
<settings xmlns="http://maven.apache.org/SETTINGS/1.2.0"><servers>
<server><id>nexus-snapshots</id><username>${esc(env.NEXUS_USER)}</username><password>${esc(env.NEXUS_PASSWORD)}</password></server>
<server><id>nexus-releases</id><username>${esc(env.NEXUS_USER)}</username><password>${esc(env.NEXUS_PASSWORD)}</password></server>
</servers></settings>"""
                        try { bat 'mvn -B -s nexus-settings.xml deploy -DskipTests' }
                        finally { bat 'if exist nexus-settings.xml del /q nexus-settings.xml' }
                    }
                }
            }
        }
        stage('Deploy WAR to Tomcat') {
            steps {
                deploy adapters: [tomcat9(credentialsId: 'tomcat-credentials', path: '', url: 'http://localhost:8082')],
                       contextPath: 'jenkins-demo', war: 'web-app/target/demo-web.war'
            }
        }
    }
    post {
        always { junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml' }
        success { echo 'JAR and WAR built, archived, published to Nexus, and WAR deployed to Tomcat.' }
        failure { echo 'Pipeline failed. Review the stage and console output.' }
    }
}
