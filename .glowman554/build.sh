(
    cd $1
    (
        cd external
        bash install.sh
    )

    (
        cd frontend
        npm run build
    )

    mvn package

    sudo docker compose build
)

