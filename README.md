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
If possible, configure your OS to use Java 11. Alternatively,
read below for instructions about using environmental variables to set Java
version.

#### Using environment variables
You can set Java installation directory before running the application
by changing environment variables. On Bash and similar shells, use
```
JAVA_HOME=<Java install dir>
```

For example, on Ubuntu 18.04 something like this should work:
```
JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64/
```

On most Linux distros and macOS, a shell similar to Bash is available.
On Windows, you can install Git with Bash and use it.

### Running
Use Gradle wrapper to fetch Gradle, required libraries and run the application:
```
./gradlew bootRun
```
For Windows <code>cmd.exe</code> users, you should use <code>gradlew.bat</code>
instead. DO NOT use system-provided Gradle; it is probably too old.

The application will be available at [localhost:8080](http://localhost:8080).

### Troubleshooting
The application has been tested on Linux only, but it does not use any
platform-specific features. Provided that you install Java correctly
(e.g. by following the instructions above), everything should work.

## Vulnerabilities demonstrated

### Security Misconfiguration
**Problem:** The application has configured use of plain-text password encoder.
Should attackers get access to it (which is not going to be difficult),
plain text passwords of users will be exposed.

**Solution:** Switch to a secure password encoder, e.g. Bcrypt.

### Injection
**Problem:** When registering new users, the application concatenates user
input with SQL queries. This leads to SQL injection, and anyone could execute
any SQL query in the database. To make matters worse, this database has
plain text passwords (see above).

**Solution:** Instead of using prepared statements in a brain-dead way, use
them to pass user data to database in safe way. Alternatively, validate that
user name and password do not contain any SQL special characters. The former
option is, of course, much better.

