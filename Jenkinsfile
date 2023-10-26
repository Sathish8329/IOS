pipeline {
    agent any

    environment {
        XCODE_SCHEME = 'YourScheme'
        XCODE_WORKSPACE = 'YourWorkspace' // or use XCODE_PROJECT for a project file
        EXPORT_OPTIONS_PLIST = 'ExportOptions.plist'
        OUTPUT_DIR = "${WORKSPACE}/build"
        IPA_FILE = "${OUTPUT_DIR}/YourApp.ipa"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build and Export IPA') {
            steps {
                sh "xcodebuild -workspace ${XCODE_WORKSPACE}.xcworkspace -scheme ${XCODE_SCHEME} -configuration Release -archivePath ${OUTPUT_DIR}/${XCODE_SCHEME}.xcarchive archive"
                sh "xcodebuild -exportArchive -archivePath ${OUTPUT_DIR}/${XCODE_SCHEME}.xcarchive -exportPath ${OUTPUT_DIR} -exportOptionsPlist ${EXPORT_OPTIONS_PLIST}"
            }
        }

        stage('Archive and Publish IPA') {
            steps {
                archiveArtifacts artifacts: "build/*.ipa", allowEmptyArchive: true
            }
        }
    }
}
