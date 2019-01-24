### Installation & Execution
 * Checkout package into your machine by using `git clone git@github.com:paras885/FileDownloader.git` command.
 * There are two ways to define file urls which want to download:
   * **Use default file:**
     * Put file urls with different protocols in `tst/sampledata/inputFile.txt` file.
   * **Create custom file and provide it when start application via command line arguments.** 
     * Use this parameter to provide custom inputFile and directory where we want to store data.
        * `-PcommandLineArgs=inputFilePath,directoryPathToSaveData`.
 * To start downloader use this command :
    * `gradle clean && gradle startDownloading` [commandLineArguments are optional to pass].
 * Url pattern, which we must follow:
   *  `protocol://filePath` or `protocol://filePath/additional-attributes?key1=value1&key2=value2` [See **ftp** protocol business logic for more detail].  

### Assumptions
  * If commandLineArgs not passed then program will use default parameters for inputFile[`tst/sampledata/inputFile.txt` file] and directory[current directory where application running].
  * For default value, protocol specific directory will be created in current folder[For example : ./http/downalodedFile].
  * If downloader face issue during file downloading it will skip that file that remove partially downloaded file.

### Libraries used
   * **Gradle:** As build tool to compile and execute java classes.
   * **Google Guice:** As dependency injection to provide polymorphism in source code.
   * **Slf4j, Log4J:** To log activities in application life cycle.
   * **Lombok:** To avoid boilerplate code for builder, setter, getter methods of pojo.
   * **JMockit:** To mock objects in unit testing.
   * **JUnit:** To write and execute unit test cases.
   
### TODO [Future plan]
   * Address todos which have added in source code during development.
   * Support more protocols.     
   * Introduce retry mechanism to download file for partially downloaded files.
   * Introduce schedulers to schedule small files in one or two thread and divide large files in multi threads.