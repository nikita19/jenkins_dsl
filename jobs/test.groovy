job('Test run'){
  scm {
    github('jobs/2.groovy', 'master')
  }
  triggers {
  	cron('@hourly')
  }
  steps {
    shell('echo "Hello World!"')
  }
}