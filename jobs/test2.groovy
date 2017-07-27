pipelineJob('Pipejob') {
  displayName('Pipeline job')
  description('Creates pipeline job using Jenkins DSL.')
  definition {
  	cps {
  		sandbox()
  		script("""
          node {
          	  try {
                stage ('1-st stage'){
                    build "Hello World2"
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

listView('admin'){
    jobs {
        name('Hello World')
        name('Hello World2')
        name('Hello World3')
        name('Hello World7')
    }
    columns{
        name()
        status()
    }
}