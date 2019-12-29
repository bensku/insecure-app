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
**Problem:** The application has configured use of plain-text password encoder,
even though the class that implements it has been deprecated for security
reasons. This means that the passwords in the database are stored as plain
text. Should they be compromised, the attackers will certainly be able to
log in as any user.

More importantly, many users recycle passwords even if that is discouraged.
Plain-text passwords could be used to compromise accounts they have on other
services that might be otherwise very security.

**Solution:** Use a password encoder from Spring Security that has not been
deprecated. Most of them are drop-in replacements significantly improve
security.

### Injection
**Problem:** When registering new users, the application concatenates user
input into an SQL query. This leads to SQL injection that enables anyone to
execute any SQL code in the application database. The database contents could
be tampered with, deleted or retrieved by attackers. And to make matters worse,
the passwords found there are in plain text...

**Solution:** Do not concatenate user input into an SQL query. Instead, use
prepared statements provided by Java's SQL API to safely inject values into
the queries. Alternatively, validate that user name and password do not contain
any SQL special characters. The former is generally better idea.

### Broken Authentication
**Problem:** There is no protection against dictionary or brute force attacks.
Weak and even *empty* passwords are permitted when registering accounts.
Changing passwords, should they become compromised, is not possible.
As a tip of the cake, the passwords are stored as plain text due top security
misconfiguration.

**Solution:** Implement rate limiting to make brute force attacks less
effective. Legitimate users will never try to log into their account from one
IP address more than once a few seconds.

Do not permit too short passwords in registration; ideally,
also validate that given password has not been leaked by a local copy of
haveibeenpwned.com database. At the very least, check that the password is
not in 10k most common passwords.

### Cross-Site Scripting
**Problem:** All logged-in users can set contents of a shared note. For
formatting purposes, HTML has been explicitly allowed there. Unfortunately,
scripts (and other dangerous parts of HTML) are not stripped or escaped, so
any user may gain ability to execute scripts on all other users.

The scripts will be free to modify page content and send requests without
being impacted by CORS. This could allow conducting fraud against the other
users, or enable the attackers to control another application on hosted on same
domain on behalf of users. Mining cryptocurrencies or attempting privilege
escalation on users could also be possible.

**Solution:** Escape all HTML in user input. If rich text formatting is
desired, require user to input it in a safe format e.g. Markdown. Render the
rich text to HTML on server or client that is about to view it. Alternatively,
use a HTML sanization library to strip all tags that have not been whitelisted.
Don't try to sanitize HTML yourself; it is a very difficult.

As additional layer of defense, the application could be configured to emit
HTTP headers that requests browsers not to execute *any* scripts. This would
prevent future usability improvements with Javascript, which is not ideal.

### Broken Access Control
**Problem:** Setting notes in index page is done via HTTP GET. The user can
be tricked to copy-paste an URL to their browser that will replace current note
with something else. As long as XSS is a problem, that something else might be
a malicious script. Even without XSS, something nasty could be "written" as a note.

Besides the obvious issues, this behavior is also contradictory to HTTP
standard. When standards are not followed, interoperability issues are more
likely to arise. Some of them might degrade security.

**Solution:** Never use HTTP GET for anything but *getting* things. HTTP
standard defines GET (plus HEAD, OPTIONS and TRACE) as *safe*, which means
they are intended only for information retrieval. They should not have any
side effects.