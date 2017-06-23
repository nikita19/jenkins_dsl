job('Test run'){
  scm {
    github('jobs/test2.groovy', 'master')
  }
  triggers {
  	cron('@hourly')
  }
  steps {
    shell('echo "Hello World!"')
  }
}