This is an attempt to port the current IZPack Ant build to Maven.

To build

1. Run 'mvn install -DskipTests=true'

To work  with Eclipse


1. run 

   * Run mvn install -Pconfigure-workspace -N
   
   * mvn eclipse:create-workspace -Pconfigure-workspace -N
   
   Must be in this order,and run only once to setup eclipse workspace

2. run 'mvn eclipse:eclipse'

   Run once only until there is depdenency changes

3. Start eclipse and point it to this directory as it workspace

4. In Eclipse, import all maven sub projects which already has eclipse configuration created in step 2


good luck

