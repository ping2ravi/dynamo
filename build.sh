docker stop mysql-for-build-test
docker rm mysql-for-build-test
docker run --name=mysql-for-build-test -e MYSQL_ROOT_PASSWORD=password -e MYSQL_DATABASE=dynamo --net=host -d mysql:5.5

cwd=$(pwd)

docker stop DynamoBuild
docker rm DynamoBuild


echo mkdir -p ${cwd}/target
mkdir -p ${cwd}/target

BUILD_OUTPUT_DIRECTORY=/var/build-output

echo docker run --name=DynamoBuild -e MVN_COMMAND="clean verify -P all-tests" -e BUILD_OUTPUT=$BUILD_OUTPUT_DIRECTORY -v $HOST_JENKINS_HOME/workspace/dynamo-build/build-output:$BUILD_OUTPUT_DIRECTORY -v ~/.m2:/root/.m2/ -e GIT_REPO=https://github.com/ping2ravi/dynamo.git --net=host ping2ravi/maven-build-docker:latest

docker run --name=DynamoBuild -e MVN_COMMAND="clean verify -P all-tests" -e BUILD_OUTPUT=$BUILD_OUTPUT_DIRECTORY -v $HOST_JENKINS_HOME/workspace/dynamo-build/build-output:$BUILD_OUTPUT_DIRECTORY -v ~/.m2:/root/.m2/ -e GIT_REPO=https://github.com/ping2ravi/dynamo.git --net=host ping2ravi/maven-build-docker:latest
 
docker stop mysql-for-build-test
docker rm mysql-for-build-test
docker rm DynamoBuild
