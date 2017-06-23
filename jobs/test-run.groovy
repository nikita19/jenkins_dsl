job{
  name 'Test run'
  scm {
  	git(https://github.com/nikita19/jenkins_dsl.git)
  }
  triggers {
  	scm('*/15 * * * *')
  }
  steps {
    shell('echo "Hello World!"')
  }
}