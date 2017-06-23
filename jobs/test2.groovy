pipelineJob('Pipejob') {
  definition {
  	cps {
  		sandbox()
  		script("""
          node {
          	  try {
                stage ('1-st stage'){
                    build "DSL-Tutorial-1-Test"
                }
                stage ('2-nd stage'){
                	echo "build was finished"
                }
              } catch (e) {
              	currentBuild.result = 'FAILED'
                throw e
              }
          }
  	    """.stripIndent())
  		}
  	}
}