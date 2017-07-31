String directory = 'examples'

folder(directory) {
    description 'Contains jobs for graphite stack'
}

pipelineJob("create downtimes(dsl)") {
    definition {
        logRotator {
            numToKeep(5)
            artifactNumToKeep(1)
            daysToKeep(7)
        }
        cps {
            sandbox()
            script(readFileFromWorkspace('../scripts/nagios_create_downtime.groovy'))
        }
    }
}
