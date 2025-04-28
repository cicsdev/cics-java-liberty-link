# Sample: cics-java-liberty-link
[![Build](https://github.com/cicsdev/cics-java-liberty-link/actions/workflows/java.yaml/badge.svg)](https://github.com/cicsdev/cics-java-liberty-link/actions/workflows/java.yaml)

A sample CICS Java application showing use of the `com.ibm.cics.server.invocation.CICSProgram` annotation to link to an Enterprise Java program running in a Liberty JVM server.

## Contents
A set of sample components that demonstrate how to annotate a POJO packaged in a WAR (Web application), deploy it to a Liberty JVM server, and then LINK to (or call) that POJO from a CICS program or CICS transaction.

- [cics-java-liberty-link](./cics-java-liberty-link) - Top-level project.
- [cics-java-liberty-link-app](./cics-java-liberty-link-app) - Web project.
- [cics-java-liberty-link-bundle](./cics-java-liberty-link-bundle) - CICS bundle plug-in based project, contains Web application and WLPH transaction bundle-parts. Use with Gradle and Maven builds.
- [etc/eclipse_projects/com.ibm.cics.server.examples.wlp.link.bundle](./etc/eclipse_projects/com.ibm.cics.server.examples.wlp.link.bundle) - CICS Explorer based CICS bundle project, contains Web application and WLPH transaction bundle-parts. Use with CICS Explorer 'Export to zFS' deployment capability.
- [etc/config/liberty/server.xml](./etc/config/liberty/server.xml) - A template `server.xml` demonstrating the minimum configuration required to run the sample.

## Prerequisites
- CICS TS V5.5 or later
- Java SE 1.8 or later on the workstation
- One of the following:
    - Eclipse with the IBM CICS SDK for Java EE, Jakarta EE and Liberty
    - An IDE of your choice that supports Gradle or Maven
    - A command line version of Gradle or Apache Maven on the workstation

## Downloading

- Clone the repository using your IDEs support, such as the Eclipse Git plugin
- **or**, download the sample as a [ZIP](https://github.com/cicsdev/cics-java-liberty-link/archive/main.zip) and unzip onto the workstation

> [!TIP]
> Eclipse Git provides an 'Import existing Projects' check-box when cloning a repository.

### Check dependencies
 
If you are building this sample with Gradle or Maven you should verify that the correct CICS TS bill of materials (BOM) is specified for your target release of CICS. The BOM specifies a consistent set of artifacts, and adds information about their scope. In the example below the version specified is compatible with CICS TS V5.5 with JCICS APAR PH25409, or newer. That is, the Java byte codes built by compiling against this version of JCICS will be compatible with later CICS TS versions and subsequent JCICS APARs. 
You can browse the published versions of the CICS BOM at [Maven Central.](https://mvnrepository.com/artifact/com.ibm.cics/com.ibm.cics.ts.bom)
 
Gradle (build.gradle): 

`compileOnly enforcedPlatform("com.ibm.cics:com.ibm.cics.ts.bom:5.5-20200519131930-PH25409")`

Maven (POM.xml):

``` xml	
<dependencyManagement>
    <dependencies>
      <dependency>
        <groupId>com.ibm.cics</groupId>
        <artifactId>com.ibm.cics.ts.bom</artifactId>
        <version>5.5-20200519131930-PH25409</version>
        <type>pom</type>
        <scope>import</scope>
      </dependency>
    </dependencies>
  </dependencyManagement>
  ```

## Building 

You can build the sample in a variety of ways:
- Using the implicit compile/build of the Eclipse based CICS Explorer SDK
- Using the built-in Gradle or Maven support of your IDE (For example: *buildship* or *m2e* in Eclipse which integrate with the "Run As..." menu.)
- or you can build it from the command line with Gradle or Maven
  

> [!IMPORTANT]
> When you initially import the project to your IDE, you might experience local project compile errors. Resolving these errors depends on how you wish to build and deploying the sample. If you are building and deploying through CICS Explorer SDK and 'Export to zFS' you should edit the link-app's Project properties, select 'Java Build Path', on the Libraries tab select 'Classpath', click 'Add Library', select 'CICS with Enterprise Java and Liberty' Library, and choose the appropriate CICS and Enterprise Java versions.
If you are building and deploying with Gradle or Maven then you don't necessarily need to fix the local errors, but to do so, you can run a tooling refresh on the link-app project. For example, in Eclipse: right-click on "Project", select "Gradle -> Refresh Gradle Project", **or** right-click on "Project", select "Maven -> Update Project...".

> [!TIP]
> In Eclipse, Gradle (buildship) is able to fully refresh and resolve the local classpath even if the project was previously updated by Maven. However, Maven (m2e) does not currently reciprocate that capability. If you previously refreshed the project with Gradle, you'll need to manually remove the 'Project Dependencies' entry on the Java build-path of your Project Properties to avoid duplication errors when performing a Maven Project Update.

### Building with Eclipse

If you are using the Egit client to clone the repo, remember to tick the button to import all projects. Otherwise, you should manually Import the projects into CICS Explorer using File &rarr; Import &rarr; General &rarr; Existing projects into workspace, then follow the error resolution advice above.

### Building with Gradle

For a complete build you should run the settings.gradle file in the top-level 'cics-java-liberty-link' directory which is designed to invoke the individual build.gradle files for each project. 

If successful, a WAR file is created inside the `cics-java-liberty-link-app/build/libs` directory and a CICS bundle ZIP file inside the `cics-java-liberty-link-bundle/build/distribution` directory. 

[!NOTE]
In Eclipse, the output 'build' directory is often hidden by default. From the Package Explorer pane, select the three dot menu, choose filters and un-check the Gradle build folder to view its contents.

The JVM server the CICS bundle is targeted at is controlled through the `cics.jvmserver` property, defined in the [`cics-java-liberty-link-bundle/build.gradle`](cics-java-liberty-link-bundle/build.gradle) file, or alternatively can be set on the command line:

**Gradle Wrapper (Linux/Mac):**
```shell
./gradlew clean build
```
**Gradle Wrapper (Windows):**
```shell
gradle.bat clean build
```
**Gradle (command-line):**
```shell
gradle clean build
```
**Gradle (command-line & setting jvmserver):**
```shell
gradle clean build -Pcics.jvmserver=MYJVM
```

### Building with Apache Maven

For a complete build you should run the pom.xml file in the top-level 'cics-java-liberty-link' directory. A WAR file is created inside the `cics-java-liberty-link-app/target` directory and a CICS bundle ZIP file inside the `cics-java-liberty-link-bundle/target` directory.

If building a CICS bundle ZIP the CICS JVM server name for the WAR bundle part should be modified in the 
 `cics.jvmserver` property, defined in [`cics-java-liberty-link-bundle/pom.xml`](cics-java-liberty-link-bundle/pom.xml) file under the `defaultjvmserver` configuration property, or alternatively can be set on the command line.

**Maven Wrapper (Linux/Mac):**
```shell
./mvnw clean verify
```
**Maven Wrapper (Windows):**
```shell
mvnw.cmd clean verify
```
**Maven (command-line):**
```shell
mvn clean verify
```
**Maven (command-line & setting jvmserver):**
```shell
mvn clean verify -Dcics.jvmserver=MYJVM
```

## Deploying to a Liberty JVM server

Ensure you have the following features defined in your Liberty server.xml:
* `cicsts:link-1.0`

A template server.xml is provided [here](./etc/config/liberty/server.xml).

### Deploying CICS Bundles with CICS Explorer
1. Optionally, change the name of the JVMSERVER in the .warbundle file of the CICS bundle project from DFHWLP to the name of your JVMSERVER resource defined in CICS. 
2. Export the bundle project to zFS by selecting 'Export Bundle project to z/OS Unix File System' from the context menu.
3. In CICS, create a bundle definition, setting the bundle directory attribute to the zFS location you just exported to, and install it. 
4. Check the CICS region for the dynamically created PROGRAM resource HELLOWLP using the Programs view in CICS Explorer, or the CEMT INQUIRE PROGRAM command.

### Deploying CICS Bundles from Gradle or Maven
1. Manually upload the ZIP file from the _cics-java-liberty-link-bundle/target_ or _cics-java-liberty-link-bundle/build/distributions_ directory to zFS.
2. Unzip this ZIP file on zFS (e.g. `${JAVA_HOME}/bin/jar xf /path/to/bundle.zip`).
3. Create a CICS BUNDLE resource definition, setting the bundle directory attribute to the zFS location you just extracted to, and install it into the CICS region. 
4. Check the CICS region for the dynamically created PROGRAM resource HELLOWLP using the Programs view in CICS Explorer, or the CEMT INQUIRE PROGRAM command.

### Deploying directly with Liberty's application configuration
1. Manually upload the WAR file from the _cics-java-liberty-link-app/target_ or _cics-java-liberty-link-app/build/libs_ directory to zFS.
2. Add an `<application>` element to the Liberty server.xml to define the web application.
3. Check the CICS region for the dynamically created PROGRAM resource HELLOWLP using the Programs view in CICS Explorer, or the CEMT INQUIRE PROGRAM command.


## Running

If you deployed the sample through CICS Explorer (Export to zFS), or through the CICS bundle plug-in as a ZIP and manually FTP'd the resource to zFS, the sample can be run using the `WLPH` transaction that is provided as a bundlepart within the CICS bundle definition.

Alternatively, you can define your own CICS transaction to invoke the program 'HELLOWLP', or you can use the CECI transaction to invoke the sample program:

```text
CECI LINK PROGRAM(HELLOWLP)
```

The program prints a message to the STDOUT output stream of the JVM server it runs on.

You can pass a channel with a container:

```text
CECI PUT CONTAINER(NAME) CHAR FROM(MATTHEW) CHANNEL(CHAN)
CECI LINK PROG(HELLOWLP) CHANNEL(CHAN)
```

(ensure both commands are entered in the same CECI session).



## Find out more
For more information about invoking Java EE applications in a Liberty JVM server from CICS programs, see [Linking to Java applications in a Liberty JVM server by using the @CICSProgram annotation](https://www.ibm.com/docs/en/cics-ts/latest?topic=djariljs-linking-java-applications-in-liberty-jvm-server-by-using-cicsprogram-annotation).


## License
This project is licensed under [Eclipse Public License - v 2.0](LICENSE).

## Usage terms
By downloading, installing, and/or using this sample, you acknowledge that separate license terms may apply to any dependencies that might be required as part of the installation and/or execution and/or automated build of the sample, including the following IBM license terms for relevant IBM components:

• IBM CICS development components terms: https://www.ibm.com/support/customer/csol/terms/?id=L-ACRR-BBZLGX