# cics-java-liberty-link
[![Build](https://github.com/SoftlySplinter/cics-java-liberty-link/actions/workflows/java.yaml/badge.svg)](https://github.com/SoftlySplinter/cics-java-liberty-link/actions/workflows/java.yaml)

Sample CICS Java application showing use of the `com.ibm.cics.server.invocation.CICSProgram` annotation to link to an enterprise Java program running in a Liberty JVM server.

## Contents
This is a set of sample Eclipse projects for Link to Liberty, demonstrating how you can annotate a POJO packaged in a WAR in a Liberty JVM server, to allow it be called from a CICS program.

- [cics-java-liberty-link-app](./cics-java-liberty-link-app) - Web project
- [cics-java-liberty-link-bundle](./cics-java-liberty-link-bundle) - CICS bundle project (Maven/Gradle)
- [etc/config/liberty/server.xml](./etc/config/liberty/server.xml) - Template `server.xml` file.
- [etc/eclipse_projects/com.ibm.cics.server.examples.wlp.link.bundle](./etc/eclipse_projects/com.ibm.cics.server.examples.wlp.link.bundle) - CICS bundle project (CICS Explorer)

## Prerequisites
* CICS TS V5.5 or later
* Java SE 1.8 or later on the workstation
* Eclipse with the IBM CICS SDK for Java EE, Jakarta EE and Liberty, or any IDE that supports usage of the Maven Central artifact [com.ibm.cics:com.ibm.cics.server.](https://search.maven.org/artifact/com.ibm.cics/com.ibm.cics.server)
* Either Gradle or Apache Maven on the workstation

## Downloading

- Clone the repository using your IDEs support, such as the Eclipse Git plugin
- **or**, download the sample as a [ZIP](https://github.com/cicsdev/cics-java-liberty-springboot-jcics/archive/main.zip) and unzip onto the workstation

> [!TIP]
> Eclipse Git provides an 'Import existing Projects' check-box when cloning a repository.

### Check dependencies
 
Before building this sample, you should verify that the correct CICS TS bill of materials (BOM) is specified for your target release of CICS. The BOM specifies a consistent set of artifacts, and adds information about their scope. In the example below the version specified is compatible with CICS TS V5.5 with JCICS APAR PH25409, or newer. That is, the Java byte codes built by compiling against this version of JCICS will be compatible with later CICS TS versions and subsequent JCICS APARs. 
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

You can build the sample using an IDE of your choice, or you can build it from the command line. For both approaches, using Gradle or Maven is the recommended way to get a consistent version of build tooling. 
  
For an IDE, taking Eclipse as an example, the plug-ins for Gradle *buildship* and Maven *m2e* will integrate with the "Run As..." capability, allowing you to specify a specific version of your chosen build tool.

The required build-tasks are typically `clean build` for Gradle and `clean package` for Maven. Once run, Gradle will generate a WAR file in the `build/libs` directory, while Maven will generate it in the `target` directory.

> [!NOTE]
> If you import the project to your IDE, you might experience local project compile errors. To resolve these errors you should run a tooling refresh on that project. For example, in Eclipse: right-click on "Project", select "Gradle -> Refresh Gradle Project", **or** right-click on "Project", select "Maven -> Update Project...".

> [!TIP]
> In Eclipse, Gradle (buildship) is able to fully refresh and resolve the local classpath even if the project was previously updated by Maven. However, Maven (m2e) does not currently reciprocate that capability. If you previously refreshed the project with Gradle, you'll need to manually remove the 'Project Dependencies' entry on the Java build-path of your Project Properties to avoid duplication errors when performing a Maven Project Update.

### Eclipse

Import the projects into CICS Explorer using File &rarr; Import &rarr; General &rarr; Existing projects into workspace.
> [!NOTE]
> If using the egit client, you can just clone the repo and tick the button to import all projects.

### Gradle (command line)

Run the following in a local command prompt:

```shell
gradle clean build
```

This creates a WAR file inside the `cics-java-liberty-link-app/build/libs` directory and a CICS bundle ZIP file inside the `cics-java-liberty-link-bundle/build/distribution` directory.

The JVM server the CICS bundle is targeted at is controlled through the `jvmserver` property, defined in the [`gradle.properties`](gradle.properties) file, or in the command line:

```shell
gradle clean build -Pjvmserver=MYJVMS
```

### Maven (command line)


Run the following in a local command prompt:

```shell
mvn clean package
```

This creates a WAR file inside the `cics-java-liberty-link-app/target` directory and a CICS bundle zIP file inside the `cics-java-liberty-link-bundle/target` directory.

The JVM server the CICS bundle is targeted at is controlled throught the `jvmserver` property, defined in [`cics-java-liberty-link-bundle/pom.xml`](cics-java-liberty-link-bundle/pom.xml) file under the `defaultjvmserver` configuration property.

## Deploying to a Liberty JVM server

Ensure you have the following features defined in your Liberty server.xml:
* `cicsts:link-1.0`

A template server.xml is provided [here](./etc/config/liberty/server.xml).

### Deploying CICS Bundles with CICS Explorer
1. Change the name of the JVMSERVER in the .warbundle file of the CICS bundle project from DFHWLP to the name of the JVMSERVER resource defined in CICS. 
2. Export the bundle project to zFS by selecting 'Export Bundle project to z/OS Unix File System' from the context menu.
3. Create a bundle definition, setting the bundle directory attribute to the zFS location you just exported to, and install it. 
4. Check the CICS region for the dynamically created PROGRAM resource HELLOWLP using the Programs view in CICS Explorer, or the CEMT INQUIRE PROGRAM command.

### Deploying CICS Bundles from Gradle or Maven
1. Manually upload the ZIP file from the _cics-java-liberty-link-bundle/target_ or _cics-java-liberty-link-bundle/build/distributions_ directory to zFS.
2. Unzip this ZIP file on zFS (e.g. `${JAVA_HOME}/bin/jar xf /path/to/bundle.zip`).
3. Create a CICS BUNDLE resource definition, setting the bundle directory attribute to the zFS location you just extracted to, and install it into the CICS region. 
4. Check the CICS region for the dynamically created PROGRAM resource HELLOWLP using the Programs view in CICS Explorer, or the CEMT INQUIRE PROGRAM command.

### Deploying with Liberty configuration 
1. Manually upload the WAR file from the _cics-java-liberty-link-app/target_ or _cics-java-liberty-link-app/build/libs_ directory to zFS.
2. Add an `<application>` element to the Liberty server.xml to define the web application.
3. Check the CICS region for the dynamically created PROGRAM resource HELLOWLP using the Programs view in CICS Explorer, or the CEMT INQUIRE PROGRAM command.


## Running

You can use the CECI transaction to invoke the sample program:

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

Alternatively, the enterprise Java program can be run using the provided `WLPH` transaction.

## Find out more
For more information about invoking Java EE applications in a Liberty JVM server from CICS programs, see [Linking to Java applications in a Liberty JVM server by using the @CICSProgram annotation](https://www.ibm.com/docs/en/cics-ts/latest?topic=djariljs-linking-java-applications-in-liberty-jvm-server-by-using-cicsprogram-annotation).
