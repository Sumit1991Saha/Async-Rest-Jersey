echo "Building application"
mvn clean install
echo "Building Image"
docker build -t async-rest-jersey -f Dockerfile .
echo "Running container"

