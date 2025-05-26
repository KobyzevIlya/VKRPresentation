job('build-and-deploy') {
    label('cross')

    description('Ручной запуск сборки артефакта и доставки его на плату.')

    scm {
        git {
            remote {
                url('http://192.168.10.151/riscv-lab/infra/build-and-deploy.git')
                credentials('jenkins-token-read-write')
            }
        }
    }

    steps {
        shell('''
            echo "=== Сборка ==="
            make clean && make

            echo "=== Доставка ==="
            ansible-playbook -i ansible/inventory.ini ansible/deliver.yml
        ''')
    }
}