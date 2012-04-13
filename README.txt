                    U-Prove Crypto SDK V1.1 (Java Edition)
                    ======================================

The U-Prove Crypto SDK V1.1 (Java Edition) implements the U-Prove Cryptographic
Specification V1.1 [UPCS].  This SDK was developed by Microsoft to support
experimentation with the foundational features of the U-Prove technology.  It
is made available under the Apache 2.0 open-source license, with patent rights
granted under the OSP.

This package can be found on the MSDN Code Gallery at
http://code.msdn.microsoft.com/uprovesdkjava.  For more information about
U-Prove, visit http://www.microsoft.com/u-prove.


CONTENTS:
---------

LICENSE.txt           - The Copyright license and patent grant under which this
                        package is distributed
README.txt            - This file
ant-junit.policy      - Java policy file required for Ant-driven junit test
build.properties      - Local settings controlling the Ant-driven build process
build.xml             - An Ant build file
src/main              - The SDK source code
src/sample            - Sample source code
src/test              - JUnit test code
target/               - Output directory created by build process
.classpath            - Eclipse data file
.project              - Eclipse data file


BUILDING THE SDK:
-----------------

The SDK includes a .project file (among others) for Eclipse Helios.  It is
recommended that Eclipse be started using the -vm switch, specifying the full
path to the java executable included in the JDK.

The included build.xml file can be used with Apache Ant to build the SDK,
sample, and test classes, run the unit tests, package the SDK classes into a
.jar file, and generate documentation via javadoc.  The build.properties file
must be edited to set the junit.jar property to the full path to the junit.jar
file.  All artifacts built via Ant are placed in the "target" directory.

Alternatively, the SDK classes can be compiled directly via javac with a
command similar to the following:

%JAVA_HOME%\bin\javac -sourcepath src\main\java -d target\classes src\main\java\com\microsoft\uprove\*.java


USING THE UNIT TESTS:
---------------------

The test suites in the src\test directory require JUnit at both compile time
and run time. JUnit can be found at http://junit.org/.  The suites can be run
via the "test" target in the Ant build file or via Eclipse's JUnit integration.

The ConfigImplTest test case must be run in isolation; tests run after it in
the same VM will fail with access control exceptions.  This is expected.
Either run tests via the Ant build file (which will run each test in its own
VM) or run the cases individually in Eclipse.


USING THE SDK:
--------------

Place the SDK classes (in a .jar file or in a directory hierarchy) in your Java
CLASSPATH; see
http://java.sun.com/javase/6/docs/technotes/tools/windows/classpath.html.


REFERENCES:
-----------

[UPCS]    Christian Paquin. U-Prove Cryptographic Specification V1.1.  Microsoft
          Corporation, February 2011. http://www.microsoft.com/u-prove.



Copyright (c) Microsoft. All rights reserved.
This code is licensed under the Apache License Version 2.0.
THIS CODE IS PROVIDED *AS IS* WITHOUT WARRANTY OF
ANY KIND, EITHER EXPRESS OR IMPLIED, INCLUDING ANY
IMPLIED WARRANTIES OF FITNESS FOR A PARTICULAR
PURPOSE, MERCHANTABILITY, OR NON-INFRINGEMENT.
