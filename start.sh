sudo docker compose down
mvn package
(
    cd frontend
    npm run build
)
sudo docker compose build
sudo docker compose up -d
sudo docker compose logs -f