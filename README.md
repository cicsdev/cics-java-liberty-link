# cics-java-liberty-link
Sample CICS Java program showing use of the com.ibm.cics.server.invocation.CICSProgram annotation for Link to Liberty.

## License
This project is licensed under [Apache License Version 2.0](LICENSE).   

## Contents
This is a set of sample Eclipse projects for Link to Liberty, demonstrating how you can annotate a POJO packaged in a WAR in a Liberty JVM server, to allow it be called from a CICS program.

- projects/com.ibm.cics.server.examples.wlp.link - Web project
- projects/com.ibm.cics.server.examples.wlp.link.bundle - CICS bundle project

## Prerequisites
- CICS TS V5.3 with APAR PI63005, or later
- A configured Liberty JVM server
- Java SE 1.7 or later on the z/OS system
- Java SE 1.7 or later on the workstation
- CICS Explorer with CICS SDK for Servlet and JSP support

## Deploying the sample
1. Import the projects into CICS Explorer using File -> Import -> General -> Existing projects into workspace. 
  - If using the egit client, you can just clone the repo and tick the button to import all projects.
2. Change the name of the JVMSERVER in the .warbundle file from DFHWLP to the name of the JVMSERVER resource defined in CICS. 
3. Export the bundle project to zFS by selecting 'Export Bundle project to z/OS Unix File System' from the context menu.
4. Ensure the `cicsts:link-1.0` feature is present in server.xml and the JVMSERVER is ENABLED.
5. Create a bundle definition, setting the bundle directory attribute to the zFS location you just exported to, and install it. 
6. Check the CICS region for the dynamically created PROGRAM resource HELLOWLP using the PROGRAMS view in CICS Explorer, or the CEMT INQUIRE PROGRAM command.

## Trying out the sample
You can use CECI to invoke the sample program:
`CECI LINK PROGRAM(HELLOWLP)`
and look for output in the MSGUSR DD card of the CICS log or the JVMServer output

You can pass a channel with a container:

`CECI PUT CONTAINER(NAME) CHAR FROM(MATTHEW) CHANNEL(CHAN)`

`CECI LINK PROG(HELLOWLP) CHANNEL(CHAN)`

`CECI GET CONTAINER(OUTPUT) CHAR CHANNEL(CHAN)`

(ensure both commands are entered in the same CECI session)

## Find out more
For more information on invoking Java EE applications in a Liberty JVM server from CICS programs see [this article](https://developer.ibm.com/cics/2016/11/14/link-to-liberty-now-available-in-cics-ts-v5-3/).
