docker stop mysql-for-build-test
docker rm mysql-for-build-test
docker run --name=mysql-for-build-test -e MYSQL_ROOT_PASSWORD=password -e MYSQL_DATABASE=dynamo --net=host -d mysql:5.5

cwd=$(pwd)

docker stop DynamoBuild
docker rm DynamoBuild

echo docker run --name=DynamoBuild -v ${cwd}/target:/target -e GIT_REPO=https://github.com/ping2ravi/dynamo.git --net=host ping2ravi/maven-build-docker:latest
docker run --name=DynamoBuild -v ${cwd}/target:/target -e GIT_REPO=https://github.com/ping2ravi/dynamo.git --net=host ping2ravi/maven-build-docker:latest
 
docker stop mysql-for-build-test
docker rm mysql-for-build-test
docker rm DynamoBuild