job('test-pipeline-repo-job') {
    label('board')

    scm {
        git {
            remote {
                url('http://192.168.10.151/riscv-lab/infra/test-pipeline-repo.git')
                credentials('jenkins-token-read-write')
            }
        }
    }

    triggers {
        genericTrigger {
            genericVariables {
                genericVariable {
                    key('gitlab_event')
                    value('\$.object_kind')
                }
                genericVariable {
                    key('gitlab_user')
                    value('\$.user.username')
                }
            }
            token('push-trigger')
            printContributedVariables(true)
            printPostContent(true)
            causeString('GitLab MR push от $gitlab_user')
            regexpFilterText('$gitlab_event')
            regexpFilterExpression('push')
        }
    }

    steps {
        shell('''
            git config user.name "Jenkins CI"
            git config user.email "jenkins@example.com"
        ''')

        shell('echo "Сработала MR джоба"')
    }
}