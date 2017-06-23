pipelineJob('Pipejob') {
  definition {
  	cps {
  		sandbox()
  		script("""
          node {
              stage ('1-st stage'){
                  build "DSL-Tutorial-1-Test"
              }
          }
  	    """.stripIndent())
  		}
  	}
}