# Creates issues in any Github repository from command line

#### Command to build the application
```
mvn clean install
```

#### Command to start the application
```
java -jar target/create-github-defect-0.1-SNAPSHOT.jar
```

#### Command to create defect in Github project
```
curl -X POST "http://localhost:8055/defect/?body=<DEFECT BODY>&label=<LABEL>&reponame=<REPOSITORY NAME>&title=<DEFECT TITLE>&token=<GIT TOKEN>&username=<USER NAME>" -H "accept: */*"
```

#### Command to get list of defects from Github project
```
curl -X GET "http://localhost:8055/defect/token/<GIT TOKEN>/username/<USER NAME>/reponame/<REPOSITORY NAME>" -H "accept: */*"
```

#### Swagger URL
[http://localhost:8055/swagger-ui.html!](http://localhost:8055/swagger-ui.html)

#### How to get Personal Access Token?
 * Login to www.github.com
 * Open "Settings" (Click on your user profile picture on top right hand side corner) - or open this direct [URL!](https://github.com/settings/tokens)
 * Click "Developer settings" -> "Personal access tokens" -> "Generate new token"
 * Check "repo" checkbox and provide Note
 * Click "Generate token" button
 * Newly created "Personal access token" is displayed for first time only. Save it privately.

#### Build docker image
Build Docker image
```
docker build -t ravikalla/create-github-defect:1.0 .
```
Run Docker container
```
docker run -p 8055:8055 --name create-github-defect ravikalla/create-github-defect:1.0 &
```
Stop Docker container
```
docker stop create-github-defect
```
Remove Docker container
```
docker rm create-github-defect
```
Remove Docker image
```
docker rmi ravikalla/create-github-defect:1.0
```
Push Docker image to Dockerhub
```
docker push ravikalla/create-github-defect:1.0
```
