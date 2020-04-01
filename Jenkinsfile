#!groovy
//Jenkinsfile (Declarative Pipeline)

// Application properties
def APPLICATION_PROPERTIES_RELATIVE_PATH = "/src/main/resources/application.properties"

// carpeta tmp
def TEMP_FOLDER_JENKINS = "../../tmp"

// Code
def ONBOARDING_CODE = "http://localhost:8929/onboarding/document-validator"
def ONBOARDING_BRANCH = "develop"

// Ansible
def ANSIBLE_ONBOARDING = "http://localhost:8929/onboarding/onboarding-variables-template"
def ANSIBLE_BRANCH = "master"
def ANSIBLE_YML = "onboarding.yml"

// Credentials
def GIT_CREDENTIALS_JENKINS = "a5932594-056d-4567-898c-30e09e282285"



// -- Keep only 15 builds
echo 'Discard old build'
properties([buildDiscarder(logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '', 
    daysToKeepStr: '', numToKeepStr: "15"))])

pipeline {
    
    agent any
    
    // -- Display a timestamp on the log.
    options{
    	timestamps()
    }
    
    stages {
    
	    // --------------------------------
	    // -- STAGE: Prepare Environment
	    // --------------------------------
	    stage("Prepare Environment") {
	        steps {
	            script {
	                try {
	                	
	                	echo "Azure Connection"
			            withCredentials([azureServicePrincipal('OnboardingExcibitAzureCredentials')]) {
				            sh 'az login --service-principal -u $AZURE_CLIENT_ID -p $AZURE_CLIENT_SECRET -t $AZURE_TENANT_ID'
				            sh 'az account set -s $AZURE_SUBSCRIPTION_ID'
				            sh 'az resource list'
				        } 
		        
	                	//sh """
                			//mkdir ${TEMP_FOLDER_JENKINS}
                		//"""

	                	
	                	// -- Ansible Git Template
	                    echo "Downloading ANSIBLE from: ${ANSIBLE_ONBOARDING}. Branch: ${ANSIBLE_BRANCH}"
	                    checkout([$class: "GitSCM", branches: [[name: "${ANSIBLE_BRANCH}"]], doGenerateSubmoduleConfigurations: false, 
	                    extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: "${GIT_CREDENTIALS_JENKINS}", 
	                    url: "${ANSIBLE_ONBOARDING}"]]])
	                    	                    
	                                       
	                    sh """
	                        sudo cp ${ANSIBLE_YML} ${TEMP_FOLDER_JENKINS}
	                    """
	                	                                
	                } catch (err) { 
	                    echo "Prepare Environment Stage failed"                                          
	                }
	            }   
	        }
	    }


	    // --------------------------------
	    // -- STAGE: Code
	    // --------------------------------
	    stage("Code") {
	        steps {
	            script {
	                try {
	                
	                   	// -- Code Git Template
	                    echo "Downloading CODE from: ${ONBOARDING_CODE}. Branch: ${ONBOARDING_BRANCH}"
	                    checkout([$class: "GitSCM", branches: [[name: "${ONBOARDING_BRANCH}"]], doGenerateSubmoduleConfigurations: false, 
	                    extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: "${GIT_CREDENTIALS_JENKINS}", 
	                    url: "${ONBOARDING_CODE}"]]])
	                    
	                    sh """
	                        pwd
	                    """
	                                
	                } catch (err) { 
	                    echo "Code Stage failed"                                          
	                }
	            }   
	        }
	    }
            

	    // --------------------------------
	    // -- STAGE: Ansible
	    // --------------------------------
	    stage("Ansible") {
	        steps {
	            script {
	                try {
	                   
	                   	/*sh """	                     
	                        pwd
	                    """
	                    
	                    sh """	                     
	                        echo ${APPLICATION_PROPERTIES_RELATIVE_PATH}
	                    """
	                    
	                    sh """	                     
	                        ansible-playbook ${TEMP_FOLDER_JENKINS}/${ANSIBLE_YML} --extra-vars "application_properties=${APPLICATION_PROPERTIES_RELATIVE_PATH}"
	                    """*/
	                    
	                } catch (err) { 
	                    echo "Ansible Stage failed"                                          
	                }
	            }   
	        }
	    }
	    


	    // --------------------------------
	    // -- STAGE: Build
	    // --------------------------------
	    stage("Build") {
	        steps {
	            echo "Build"
	            script {
	                try {
	                    sh """
	                        mvn clean install
	                    """
	                } catch (err) { 
	                    echo "Build Stage failed"                                          
	                }
	            }  
	        }
	    }


	    // --------------------------------
	    // -- STAGE: Deploy
	    // --------------------------------
	    stage("Deploy") {
	        steps {
	             
	        }
	    }

	    // Parameters needed: JOB_PLATFORM_NAME, JOB_APP_NAME, JOB_DEVICE_NAME, 
	    // JOB_PLATFORM_NAME, JOB_EMULATOR_PLATFORM_VERSION
	    // --------------------------------
	    // -- STAGE: Api Test
	    // --------------------------------
	    stage("Api Test") {
	        steps {
	            script {
	                // -- Api Test
	                if (JOB_PLATFORM_NAME == "java"){
	                    echo "Launching Api Test on API"
	                }
	                
	                // -- Script to launch Api Test
	                script {
	                    try {
	                        if (JOB_APP_NAME) {
	                            sh """
	                                mvn clean -DdeviceName="${JOB_DEVICE_NAME}" -DdevicePlatformName="${JOB_PLATFORM_NAME}" -DdevicePlatformVersion="${JOB_EMULATOR_PLATFORM_VERSION}" -DdeviceApp="${JOB_APP_NAME}" -DtestSuite="${SUITE_PATH}" test
	                            """
	                        }
	                        else {
	                            sh """
	                                mvn clean -DdeviceName="${JOB_DEVICE_NAME}" -DdevicePlatformName="${JOB_PLATFORM_NAME}" -DdevicePlatformVersion="${JOB_EMULATOR_PLATFORM_VERSION}" -DtestSuite="${SUITE_PATH}" test
	                            """
	                        }
	                        echo "Publishing Junit Results"
	                        junit "**/target/surefire-reports/junitreports/*.xml"
	
	                    } catch (err) { 
	                        echo "Api Test Stage failed - Rolling Back"
	                        archiveArtifacts "**/screenshot/*.png"
	                        echo "Publishing Junit Results"
	                        junit "**/target/surefire-reports/junitreports/*.xml"
	                    }
	                }   
	            }
	        }
	    }

    } // -- End stage
    // ----------------------------------------------
    // -- STAGE: Post Build actions
    // ----------------------------------------------
    post ("Post-Build Actions"){
        success ("JOB SUCCESS"){
            sh "echo Success Job"
        }

        failure ("JOB FAILURE"){
            sh "echo Failure Job"   
        }

        unstable ("JOB UNSTABLE") {
            sh "echo Unstable Job" 
        }

        aborted ("JOB ABORTED") {
            sh "echo Aborted Job"
        }
    }
}