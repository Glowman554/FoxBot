(
    cd $1

    (
        cd frontend
        npm run build
    )

    mvn package

    sudo docker compose build
)

