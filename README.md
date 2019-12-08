# A very insecure application
Demonstrates common security vulnerabilities. Student submission for F-Secure
cybersecurity MOOC.

## Running

### Install Java 11
If you're running a reasonable recent version of Ubuntu or its derivatives,
you can get Java 11 from official repository.

```
sudo apt update
sudo apt install openjdk-11-jdk
```

If this is not an option, [AdoptOpenJDK](https://adoptopenjdk.net/) has
downloads available for most operating system. While OpenJ9 and Java 12+
should work, it is probably best that you pick OpenJDK 11.

### Enable Java 11
You should configure your OS to use Java 11. If that is not possible,
you can also set it as environmental variable manually before running.

On Ubuntu 18.04, something like this should work:
```
JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64/
```
For AdoptOpenJDK users, point <code>JAVA_HOME</code> to where you installed
the JDK. This should work on Windows if you're using (Git) Bash, too.

### Running
Use Gradle wrapper to fetch Gradle, required libraries and run the application:
```
./gradlew bootRun
```
For Windows <code>cmd.exe</code> users, you should use <code>gradlew.bat</code>
instead. DO NOT use system-provided Gradle; it is probably too old.

The application will be available at [localhost:8080](http://localhost:8080).

## Vulnerabilities demonstrated
