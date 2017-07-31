node {
  NAGIP="10.128.46.200"

  withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'nagios',
                  usernameVariable: 'USER', passwordVariable: 'PASS']]) {

    
    def splitService = SERVICES.split(",")
    
    for (int i = 0; i < splitService.length; i++) {
        stage ("Set downtime for nagios host: ${HOST} service: ${splitService[i]}"){
            if (env.SERVICES == '') {
                cmd_type = "55"
            }
            else {
                cmd_type = "56"
                println "Scheduling downtime on nagios for services"
            }
            sh """

STARTDATE=`date "+%d-%m-%Y+%H%%3A%M%%3A%S"`
ENDDATE=`date "+%d-%m-%Y+%H%%3A%M%%3A%S" -d "$MINUTES min"`  

if curl --silent --show-error \
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
    https://${NAGIP}/nagios/cgi-bin/cmd.cgi -u "${USER}:${PASS}" |
        grep -q "Your command request was successfully submitted to Nagios for processing."
then 
    echo Scheduled downtime on nagios
else
    echo "Failed to contact nagios";
fi

"""
            }  // stages
    } // for
  }   // with credentials 
}     // node