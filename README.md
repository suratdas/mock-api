# Mock API

### Create a mock api for TDD
If you want to write Rest API tests even before the backend is ready, you can use this as a mock server to test your tests.
Example calls are
* Run this Spring Boot program 
  * From IDE : Use your IDE to launch a server
  * Using standalone : Create a package using command `mvn package` and then run the resultant jar file from target folder using command `java -jar <jar_file.jar>`
* Configuration
  * Set a file to be responded with GET http://localhost/setFile?fileName=myfile.json (Ensure myfiel.json exists in the same location as the jar)
  * Set a sequence of files (one will be called after another) with POST http://localhost/setFiles with body `
  {
    "fileList": [
      "response1.json",
      "response2.json"
    ],
    "statusList": [
      200,
      400
    ]
  }`
* Any time the next API call is made, the server will respond with the files located in the same directory. 
