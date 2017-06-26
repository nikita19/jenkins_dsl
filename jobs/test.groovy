job('Test run'){
  scm {
    github('jobs/test2.groovy', 'master')
  }
  triggers {
  	scm('H/15 * * * *')
  }
  steps {
    shell('echo "Hello World!"')
  }

}
