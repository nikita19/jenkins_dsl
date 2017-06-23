job{
  name 'Test run'
  triggers {
  	scm('*/15 * * * *')
  }
  steps {
    shell('echo "Hello World!"')
  }
}