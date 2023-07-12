pipeline {
    agent any
    triggers {
       // poll repo every 2 minute for changes
       pollSCM('H/5 * * * *')
   }
    stages {
        stage('Git Pull') {
            steps {
                git branch: 'main', url: 'https://github.com/samin-irtiza-bjit/Final_Project_LMS.git'
            }
        }
        
        stage('Build Application'){
            steps{
                sh 'chmod +x mvnw'
                sh './mvnw clean install'
            }
        }
        stage('Build Image') {
            steps{
                script{
                    new_image=docker.build("saminbjit/sparklms")
                }
            }
        }
        stage('Push Image to DockerHub') {
            steps {
                script{
                   
                    withDockerRegistry([ credentialsId: "dockerhub-login", url: "" ]) {
                        new_image.push()
                    }
                }
            }
        }
        
        stage('Deploy in Kubernetes'){
            steps{
                kubeconfig(caCertificate: 'f', credentialsId: 'kubeconfig', serverUrl: '') {
                    sh 'kubectl delete -f mysql.yml && sleep 10'
                    sh 'kubectl delete -f sparklms.yml'
                    sh 'kubectl apply -f mysql.yml && sleep 10'
                    sh 'kubectl apply -f sparklms.yml'
                }
            }
        }
    }
}
