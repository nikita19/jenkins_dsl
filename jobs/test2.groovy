pipelineJob('Pipejob') {
  displayName('Pipeline job')
  description('Creates pipeline job using Jenkins DSL.')
  properties {
    githubProjectUrl('https://github.com/nikita19/jenkins_dsl.git')
  }
  triggers {
    githubPush()
  }
  definition {
  	cps {
  		sandbox()
  		script("""
          node {
          	  try {
          	  	ansiColor('xterm') {
                  stage ('\u001B[31m1-st stage'){
                      build "DSL-Tutorial-1-Test"
                  }
                  stage ('\u001B[0m2-nd stage'){
                  	echo "build was finished"
                  }
                }
              } catch (e) {
              	currentBuild.result = 'FAILED'
                throw e
              }
          }
  	    """.stripIndent())
  		}
  	}
  wrappers { colorizeOutput() }
}