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
                cmd_type = "78"
                dataparam = "query=downtimelist&hostname=${HOST}&downtimeobjecttypes=host"
                println "Deleting downtime on hosts"
                println dataparam
            }
            else {
                cmd_type = "79"
                dataparam = "query=downtimelist&hostname=${HOST}&servicedescription=${splitService[i]}&downtimeobjecttypes=service"
                println "Deleting downtime on services"
                println dataparam
            }
                sh """#!/usr/bin/env bash
downtimelist=\$(curl --silent -k -u "${USER}:${PASS}"  https://${NAGIP}/nagios/cgi-bin/statusjson.cgi -d "${dataparam}" | python -c 'import json,sys;data=json.load(sys.stdin);print data["data"]["downtimelist"]')
echo "\$downtimelist"
filteredList=`echo \$downtimelist | tr -d '[,]'`
for id in \$filteredList; do 
    author=\$(curl --silent -k -u "nagiosadmin:1q2w3e" https://10.128.46.200/nagios/cgi-bin/statusjson.cgi/ -d "query=downtimelist&details=true&hostname=localhost&servicedescription=HTTP&downtimeobjecttypes=service" | python -c 'import json,sys;data=json.load(sys.stdin);print data["data"]["downtimelist"]["${id}"]["author"]')
    echo "\$author"
    if [ "\$author" == "${AUTHOR}" ]; then 
        curl --silent --show-error  --data cmd_typ=${cmd_type} --data cmd_mod=2 --data down_id=\$id  --data "com_data=Updating+application" --data btnSubmit=Commit --insecure https://10.128.46.200/nagios/cgi-bin/cmd.cgi -u "nagiosadmin:1q2w3e"; 
    fi;
done
echo "DONE"
                """   

            } //creds
            
        } // stage
    }     // for
}         // node


