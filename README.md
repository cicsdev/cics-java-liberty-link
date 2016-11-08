# cics-java-liberty-link
Samples for Link to Liberty

## Contents
This is a set of sample projects for Link to Liberty, demonstrating how you can invoke a POJO packaged in a WAR in a Liberty JVM server from a CICS program.

- projects/com.ibm.cics.server.examples.wlp.link - Web project
- projects/com.ibm.cics.server.examples.wlp.link.bundle - CICS bundle project

## Deploying the sample
1. Import the projects into CICS Explorer using File -> Import -> General -> Existing projects into workspace. 
- If using the egit client, you can just clone the repo and tick the button to import all projects.
2. Change the name of the JVMSERVER in the .warbundle file to a suitable value for your installation.
3. Export the bundle project to zFS by selecting 'Export Bundle project to z/OS Unix File System' from the contxt menu.
4. Ensure the `cicsts:link-1.0` feature is present in server.xml and the JVMSERVER is ENABLED.
5. Create a bundle defintion pointing to the zFS location you just exported to, and install it.
6. Check for the dynamically created PROGRAM resource HELLOWLP.

## Trying out the sample
You can use CECI to invoke the sample program:
`CECI LINK PROGRAM(HELLOWLP)`
and look for output in the STDOUT location for the JVMSERVER.

You can pass a channel with a container:
`CECI PUT CONTAINER(NAME) CHAR FROM(MATTHEW) CHANNEL(CHAN)`

`CECI LINK PROG(HELLOWLP) CHANNEL(CHAN)`

## Found out more
For more information on invoking Java EE applications in a Liberty JVM server from CICS programs see [this article](https://developer.ibm.com/cics/2016/11/02/link-to-liberty/).
