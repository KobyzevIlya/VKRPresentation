job('build-and-publish-deb') {
    label('cross')

    description('Сборка .deb пакета из исходника и публикация в Nexus APT-репозиторий')

    scm {
        git {
            remote {
                url('http://192.168.10.151/riscv-lab/infra/build-and-deploy.git')
                credentials('jenkins-token-read-write')
            }
        }
    }

    wrappers {
        credentialsBinding {
            string('NEXUS_USERNAME', 'nexus-username')
            string('NEXUS_PASSWORD', 'nexus-password')
        }
    }

    steps {
        shell('''
            make clean
            make
            make packages

            if [ ! -f main-riscv.deb ]; then
              echo "Package not found!"
              exit 1
            fi

            curl -u "$NEXUS_USERNAME:$NEXUS_PASSWORD" -X POST http://192.168.10.100:8081/service/rest/v1/components?repository=c-apt -F apt.asset=@main-riscv.deb
        ''')
    }
}