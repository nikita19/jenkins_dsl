String directory = 'examples'

folder(directory) {
    description 'Contains jobs for graphite stack'
}

pipelineJob("delete downtimes(dsl)") {
    parameters {
    credentialsParam('Credentials to nagios server') {
        type('com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl')
        required()
        defaultValue('nagios')
        description('SSH Key for deploying build artifacts')
        }
    stringParam('HOST', 'localhost', 'HOST description')
    stringParam('SERVICES', 'PING,HTTP', 'SERVICES description')
    stringParam('AUTHOR','Nagios Admin', 'author for downtime')
    }
    definition {
        logRotator {
            numToKeep(5)
            artifactNumToKeep(1)
            daysToKeep(7)
        }
        cps {
            sandbox()
            script(readFileFromWorkspace('scripts/nagios_delete_downtime_script.groovy'))
        }   // cps
    }       // definition
}           // job
