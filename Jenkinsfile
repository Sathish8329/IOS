pipeline {
    agent any

    environment {
        XCODE_SCHEME = 'YourScheme'
        XCODE_WORKSPACE = 'YourWorkspace' // or use XCODE_PROJECT for a project file
        EXPORT_OPTIONS_PLIST = 'ExportOptions.plist'
    }

    stages {
        stage('Build and Export IPA') {
            steps {
                checkout scm
                sh "xcodebuild -workspace ${XCODE_WORKSPACE}.xcworkspace -scheme ${XCODE_SCHEME} -configuration Release -archivePath build/${XCODE_SCHEME}.xcarchive archive"
                sh "xcodebuild -exportArchive -archivePath build/${XCODE_SCHEME}.xcarchive -exportPath build -exportOptionsPlist ${EXPORT_OPTIONS_PLIST}"
                archiveArtifacts artifacts: "build/*.ipa", allowEmptyArchive: true
            }
        }

        stage('Trigger Other Jobs') {
            steps {
                echo 'IPA Generation Successful!'
                build job: 'updatemanifest', parameters: [string(name: 'DOCKERTAG', value: env.BUILD_NUMBER)]
            }
        }
    }
}
