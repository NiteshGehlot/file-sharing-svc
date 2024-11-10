# File-sharing-svc
This project provides a REST API for securely uploading, encrypting, and storing files in an Amazon S3 bucket.
Users can upload files with a passcode, receive a unique URL for download, and retrieve the file by decrypting it with the same passcode.

### **Features**:-
1. _File Upload_ :- Accepts file with a passcode, encrypts the file using the passcode and store it temporarily. Provides a unique url to download the file.
2. _File Download_ :- The uniqueurl is passed to this api to download the file. Passcode is needed to download the file.
3. _Automatic Cleanup_ :- Automatically deletes the file that are more than 48 hours in the storage.

### **Technologies Used:-**

* Java 23 +
* Spring boot 3.3.5
* AES Encryption
* Maven
* Scheduler

### Steps to run the code:-

1. Clone the repository

   ``` 
   https://github.com/NiteshGehlot/file-sharing-svc.git
   ```

2. Build the project

    `mvn clean install`

3. Run the application
    
    `mvn spring-boot:run`

### Api Details:-

1. UploadFileApi

endpoint :- `{{host}}//files/upload/local-storage`

httpMethod:- POST

RequestBodyType - FormData

Parameters:- 
* file   (Multipart file)
* passcode (Password for the file)

Response:- 

Returns a unique url which can be used to download the file with the help of a password that is used while uploading.


2. DownloadFileApi

Endpoint :- `{{host}}///download/local-storage/{uniqueUrl}?passcode={passcode}`

HttpMethod:- GET

Parameters:-
* uniqueUrl   (The unique filename that we get in download api)
* passcode (Password for the file)

Response:-

Return the file as a response.

Note:- This service has a scheduler which automatically deletes the files from the storage after 48 hours of insertion

### Future Enhancements :-

1. Integrate database(MySql or PgSql) to keep track of the files inserted in a particular period.
2. Add a check so that there are no duplicate files inserted. (Original filename can be stored in the database)
3. Use AWS S3 storage to store the temporary files instead of local storage
4. Add authentication to the api.


### This Service is been deployed on AWS EC2 intance. 

### To access the api through the IP , you can use the below curl.

1. Upload Api:- 

```
curl --location 'http://13.235.83.1:8080/files/upload/local-storage' \--form 'file=@"/G:/Nitesh Documents/Resume/NiteshResume_Java_3YOE.pdf"' \--form 'passcode="nitesh"'
```


2. Download Api :-

```
curl --location 'http://13.235.83.1:8080/files/download/local-storage/296b656f-3293-4172-8af8-58dee81d95de.pdf?passcode=nitesh'
```


### System Architecture Diagram :-

![](F:\file-sharing-svc\SystemArchitecture.png)

Note:- Postman collection is attached in the github repository.