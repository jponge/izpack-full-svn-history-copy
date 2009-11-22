This is an attempt to port the current IZPack Ant build to Maven. 
At the first stage, it contains only Maven POMs which automatically 
fetch izpack source to maven source tree following Maven build structure.
The source is fetched only happpens once.  If this project ever materialized,
the fetched source code will be check back into source control.

To build
==========

  * Run 'mvn install'


To work  with Eclipse
======================


   1. Create eclipse workspace
   
      * mvn install -Pconfigure-workspace -N
   
      * mvn eclipse:create-workspace -Pconfigure-workspace -N
   
      Must be in this order,and run only once.
      

   2. Generate Eclipse projects 
   
      * mvn eclipse:eclipse

      Run once only until there is depdenency changes


   3. Start eclipse and point it to this directory as its workspace

   4. In Eclipse, import all maven sub projects which already has eclipse configuration created in step 2
   

To clean
========

   * Run 'mvn clean' which only remove all the generated output under target directory
   
   * Run 'mvn clean -Pclean-scm' to remove all the fetched source


good luck

