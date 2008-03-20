
TestLangPacks utility for IzPack installation program
version 0.2
20.3.2008/Ari Voutilainen


Motivation

IzPack has grown up a lot. Individual language file has many language
ID's. There could be typos which may generate wrong elements or
attributes. In addition new strings will come here and there so it
could be hard to do maintenance. In addition I couldn't get Unix
script to work well in my Windows and Cygwin.


Solution

Solution will be to make utility which will resolve those problems. I
have coded such of Java program. It uses eng.xml as a base file. When
loading base file and file to be tested, the program will check
whether both are well-formed. If files contain XML syntax errors this
routine will show the error lines and tries to tell error column too.


Other features:

Finds ID's which should be added
Finds ID's which are not needed anymore
Finds unknown attributes in elements
Finds unknown elements (means elements which are not supported)

The program is totally independent: it won't need any IzPack's classes
to compile or run.


Requirements

To run: Java 5.
To get into IDE: IDE which will recognise Java 5.


Running from command line

java -jar TestLangPacks.jar


Requesting on-line help

java -jar TestLangPacks.jar -?
   or
java -jar TestLangPacks.jar -h


Batch files

There are two batch files which can be used to get rid of too long
command line:

testlang.bat   The command file for Windows.
testlang.sh    The shell script for Linux/Unix.
