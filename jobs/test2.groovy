pipelineJob('Pipejob') {
  definition {
  	cps {
  		sandbox()
  		script("""
  			node {
  				stage('init') {
  					build 'Pipeline-build'

  				}
  			}
  	    """.stripIndent())
  		}
  	}
}