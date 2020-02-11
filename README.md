# Creates issues in any Github repository from command line

#### Command to create defect in Github project
```
java -cp ./create-github-defect-0.1-SNAPSHOT-jar-with-dependencies.jar in.ravikalla.githubapi.FetchSoftwareRequirementReviewMeeting "<GITHUB PERSONAL ACCESS TOKEN>" "<DEFECT TITLE>" "<DEFECT BODY>" "<DEFECT LABEL>"
```

#### How to get Personal Access Token?
 * Login to www.github.com
 * Open "Settings" (Click on your user profile picture on top right hand side corner) - or open this direct [URL!](https://github.com/settings/tokens)
 * Click "Developer settings" -> "Personal access tokens" -> "Generate new token"
 * Check "repo" checkbox and provide Note
 * Click "Generate token" button
 * Newly created "Personal access token" is displayed for first time only. Save it privately.