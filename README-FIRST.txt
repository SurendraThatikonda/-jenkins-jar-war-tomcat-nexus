JENKINS JAR + WAR + NEXUS + TOMCAT PRACTICE

This is a NEW multi-module Maven project:
- core module creates a JAR and includes a JUnit test.
- web-app module creates a WAR for Tomcat 10.1.
- Jenkinsfile builds/tests, archives both artifacts, publishes Maven modules to Nexus snapshots, then deploys the WAR.

Extract to a NEW folder such as C:\Jenkins-JAR-WAR. Back up your old C:\Jenkins-Practice first. Do not overwrite your old project yet.

Existing endpoints:
Jenkins http://localhost:8080
Nexus http://localhost:8081
Tomcat http://localhost:8082

Existing Jenkins credential IDs:
nexus-credentials
tomcat-credentials

IMPORTANT: Jenkinsfile uses Maven tool name 'Maven'. This must exactly match the Name shown under Manage Jenkins > Tools > Maven installations. If your name differs, edit tools { maven 'Maven' }.

The Tomcat deployment stage uses the Deploy to Container plugin's tomcat9 adapter. If the installed plugin rejects Tomcat 10.1 or the adapter is unavailable, stop and share the exact error before changing versions.

Expected artifacts:
core/target/demo-core-1.0-SNAPSHOT.jar
web-app/target/demo-web.war

Expected deployed URL:
http://localhost:8082/jenkins-demo/

Do not put passwords or nexus-settings.xml in GitHub. Never share passwords in screenshots.
