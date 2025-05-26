job('first-job') {
    label('board')

    triggers {
        cron('H 12 * * *')  // каждый день в 12:00
    }

    steps {
        shell('echo "Я что-то делаю"')
    }
}