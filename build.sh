docker run --name=mysql -e MYSQL_ROOT_PASSWORD=password -e MYSQL_DATABASE=dynamo --net=host -d mysql:5.5
mvn clean install
docker stop mysql
docker rm mysql

