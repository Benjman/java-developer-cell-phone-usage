#### Implementation of [phone usage report](https://github.com/WCF-Insurance/java-developer-cell-phone-usage):
##### Build
Run the shell script `. ./build.sh`.
##### Run
Run the shell script `. ./run.sh` (also builds the project).
##### Notes
I don't have a printer, and this has been the first time I've created a print job. [Oracle's guide](https://docs.oracle.com/javase/7/docs/technotes/guides/jps/spec/jpsOverview.fm4.html) was helpful, but honestly I'm crossing my fingers that my ascii byte array will properly print. 

---

The project follows a basic MVC pattern, with abstracted data access layers for extensibilty.

The data access layer implementations are simply in-memory repositories that are loaded on application start-up after deserializing the CSV files. I feel like this could have been abstracted to use an adapter pattern, but I was running short on time.
 
The report generation code is kind of a mess. It was the last thing I got to, and time was running short. With more time I would have made this template driven, with either XSLT or HTML.

The services are using the singleton pattern, but require inversion of control based repository injection. Typically I would rely on traditional dependency injection frameworks, but since I wrote this in pure Java, I had to initialize the singleton on application initialization. Kind of hokey, but it works.

Thank you for your time.
