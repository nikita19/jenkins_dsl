pipelineJob('Nagios_job') {
  displayName('Set nagios downtime')
  description('Set nagios downtime using Jenkins DSL.')
  parameters {
    credentialsParam('Credentials to nagios server') {
            type('com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl')
            required()
            defaultValue('nagios')
            description('SSH Key for deploying build artifacts')
        }
    stringParam('HOST', 'localhost', 'HOST description')
    stringParam('SERVICES', 'PING,HTTP', 'SERVICES description')
    stringParam('MINUTES', '30', 'MINUTES description')
    stringParam('COMMENT', 'Run by jenkins', 'COMMENT description')
  }

  definition {
  	cps {
  		script('''
          node {
            NAGIP="10.128.46.200"
            // USER="nagiosadmin"
            // PASS="1q2w3e"
            def splitService = SERVICES.split(",")
            for (int i = 0; i < splitService.length; i++) {
                stage ("Set downtime for nagios host: ${HOST} service: ${splitService[i]}"){
                    withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'nagios',
                                    usernameVariable: 'USER', passwordVariable: 'PASS']]) {
                    if (env.SERVICES == '') {
                        cmd_type = "55"
                    }
                    else {
                        cmd_type = "56"
                        println "Scheduling downtime on nagios for services"
                    }
                    sh """#!/usr/bin/env bash
                    function die { echo \$1;  exit 1; }
                    STARTDATE=`date "+%d-%m-%Y+%H%%3A%M%%3A%S"`
                    ENDDATE=`date "+%d-%m-%Y+%H%%3A%M%%3A%S" -d "$MINUTES min"`
        
                    curl --silent --show-error \
                        --data cmd_typ=${cmd_type} \
                        --data cmd_mod=2 \
                        --data host=${HOST} \
                        --data service="${splitService[i]}" \
                        --data com_data="$COMMENT" \
                        --data trigger=0 \
                        --data start_time="\$STARTDATE" \
                        --data end_time="\$ENDDATE" \
                        --data fixed=1 \
                        --data hours=2 \
                        --data minutes=0 \
                        --data childoptions=0 \
                        --data btnSubmit=Commit \
                        --insecure \
                        https://${NAGIP}/nagios/cgi-bin/cmd.cgi -u "${USER}:${PASS}" | grep -q "Your command request was successfully submitted to Nagios for processing." ||         die "Failed to contact nagios";
                    echo Scheduled downtime on nagios
                    """
                    }
                }
            }
          }   
  	    '''.stripIndent())
  	}
  }
}