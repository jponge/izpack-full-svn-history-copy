This is an attempt to port the current IZPack Ant build to Maven.

To build

1. Run 'mvn install -DskipTests=true'

To work  with Eclipse


1. run 'mvn eclipse:create-workspace -Pconfigure-workspace -N'

2. run mvn eclipse:eclipse

3. Start eclipse and point it to this directory as it workspace

4. In Eclipse, import all maven sub projects which already has eclipse configuration created in step 2


good luck

