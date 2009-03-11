Current Usage
-------------
A checkout from svn is done automatically in the generate-sources phase when the src/main/java is not found. 

Just use the normal maven goals like package and install to build the sdk module.

You could do changes to sources under src/main/java which won't be overwritten.