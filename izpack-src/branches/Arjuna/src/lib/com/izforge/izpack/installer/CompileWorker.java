/*
 *  $Id$
 *  IzPack
 *  Copyright (C) 2001-2003 Julien Ponge, Tino Schwarze
 *
 *  File :               CompilePanel.java
 *  Description :        A panel to compile files after installation
 *  Author's email :     julien@izforge.com
 *  Author's Website :   http://www.izforge.com
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.izforge.izpack.installer;

import com.izforge.izpack.util.FileExecutor;
import com.izforge.izpack.util.Debug;

import java.io.*;
import java.util.*;

import net.n3.nanoxml.*;

/**
 * This class does alle the work for compiling sources.
 *
 * It responsible for
 * <ul>
 * <li>parsing the compilation spec XML file
 * <li>collecting and creating all jobs
 * <li>doing the actual compilation
 * </ul>
 *
 * @author     Tino Schwarze
 * @created    May 2003
 */
public class CompileWorker implements Runnable
{
  /**  True if the compilation has been done. */
  private volatile boolean validated = false;

  /**  Compilation jobs */
  private ArrayList  jobs;

  /**  Name of resource for specifying compilation parameters. */
  private static final String SPEC_RESOURCE_NAME  = "CompilePanel.Spec.xml";

  private VariableSubstitutor vs;

  /**  We spawn a thread to perform compilation. */
  private Thread compilationThread;

  private XMLElement spec;

  private AutomatedInstallData idata;

  private CompileListener listener;

  private ArrayList compilerList;

  private String compilerToUse;

  private ArrayList compilerArgumentsList;

  private String compilerArgumentsToUse;

  /**
   *  The constructor.
   *
   * @param  idata    The installation data.
   * @param  listener The listener to notify of progress.
   */
  public CompileWorker(AutomatedInstallData idata, CompileListener listener) throws IOException
  {
    this.idata = idata;
    this.listener = listener;
    this.vs = new VariableSubstitutor(idata.getVariableValueMap());

    this.compilationThread = null;

    if (! readSpec ())
      throw new IOException ("Error reading compilation specification");
  }

  /** Return list of compilers to choose from. 
   *
   * @return ArrayList of String
   */
  public ArrayList getAvailableCompilers ()
  {
    return this.compilerList;
  }

  /** Set the compiler to use. */
  public void setCompiler (String compiler)
  {
    this.compilerToUse = compiler;
  }

  /** Get the compiler used. */
  public String getCompiler ()
  {
    return this.compilerToUse;
  }

  /** Return list of compiler arguments to choose from. 
   *
   * @return ArrayList of String
   */
  public ArrayList getAvailableArguments ()
  {
    return this.compilerArgumentsList;
  }

  /** Set the compiler arguments to use. */
  public void setCompilerArguments (String arguments)
  {
    this.compilerArgumentsToUse = arguments;
  }

  /** Get the compiler arguments used. */
  public String getCompilerArguments ()
  {
    return this.compilerArgumentsToUse;
  }

  /** Start the compilation in a separate thread. */
  public void startThread ()
  {
    this.compilationThread = new Thread (this, "compilation thread");
    this.compilationThread.start();
  }

  /** This is called when the compilation thread is activated. 
   *
   * Can also be called directly if asynchronous processing is not
   * desired.
   */
  public void run ()
  {
    collectJobs ();
    compileJobs ();
  }

  private boolean readSpec ()
  {
		InputStream input;
		try
		{
      // FIXME: use infrastructure from installer to get the resource
			//input = parent.getResource(SPEC_RESOURCE_NAME);
			input = ResourceManager.getInstance().getInputStream (SPEC_RESOURCE_NAME);
		}
		catch (Exception e)
		{
			e.printStackTrace();
      return false;
		}

    StdXMLParser parser = new StdXMLParser ();
    parser.setBuilder (new StdXMLBuilder ());
    parser.setValidator (new NonValidator ());
    
		try
		{
			parser.setReader (new StdXMLReader (input));
			
			this.spec = (XMLElement) parser.parse();
		}
		catch (Exception e)
		{
      System.out.println("Error parsing XML specification for compilation.");
			e.printStackTrace();
      return false;
		}

    if (! this.spec.hasChildren ())
      return false;

    this.compilerArgumentsList = new ArrayList ();
    this.compilerList = new ArrayList ();

    // read <global> information
    XMLElement global = this.spec.getFirstChildNamed ("global");

    // use some default values if no <global> section found
    if (global != null)
    {

      // get list of compilers
      XMLElement compilers = global.getFirstChildNamed ("compiler");

      if (compilers != null)
      {
        readChoices (compilers, this.compilerList);
      }

      XMLElement arguments = global.getFirstChildNamed ("arguments");

      if (arguments == null)
      {
        readChoices (arguments, this.compilerArgumentsList);
      }

    }

    // supply default values if no useful ones where found
    if (this.compilerList.size() == 0)
    {
      this.compilerList.add ("javac");
      this.compilerList.add ("jikes");
    }

    if (this.compilerArgumentsList.size () == 0)
    {
      this.compilerArgumentsList.add ("-O -g:none");
      this.compilerArgumentsList.add ("-O");
      this.compilerArgumentsList.add ("-g");
      this.compilerArgumentsList.add ("");
    }

    return true;
  }

 
  // helper function
  private void readChoices (XMLElement element, ArrayList result)
  {
    Vector choices = element.getChildrenNamed ("choice");

    if (choices == null)
      return;

    Iterator choice_it = choices.iterator();

    while (choice_it.hasNext ())
    {
      XMLElement choice = (XMLElement)choice_it.next();

      String value = choice.getAttribute ("value");

      if (value != null)
        result.add (value);
    }

  }


  /**
   * Parse the compilation specification file and create jobs.
   */
  public boolean collectJobs ()
  {
    XMLElement data = this.spec.getFirstChildNamed ("jobs");

    if (data == null)
      return false;

    // list of classpath entries
    ArrayList classpath = new ArrayList();

    this.jobs = new ArrayList();

    // we throw away the toplevel compilation job
    // (all jobs are collected in this.jobs)
    CompilationJob dummy = collectJobsRecursive (data, classpath);
    
    return true;
  }


  /** perform the actual compilation */
  public void compileJobs ()
  {
    // TODO: check whether compiler is valid
    ArrayList args = new ArrayList();
    StringTokenizer tokenizer = new StringTokenizer (this.compilerArgumentsToUse);

    while (tokenizer.hasMoreTokens ())
    {
      args.add (tokenizer.nextToken());
    }

    Iterator job_it = this.jobs.iterator();

    this.listener.startCompilation (this.jobs.size());
    int job_no = 0;

    while (job_it.hasNext())
    {
      CompilationJob job = (CompilationJob) job_it.next();
      
      this.listener.changeCompileJob (0, job.getSize(), job.getName(), job_no++);

      if (! job.perform (this.compilerToUse, args))
        break;
    }

    Debug.trace ("compilation finished.");
    this.listener.stopCompilation ();
  }

  
  private CompilationJob collectJobsRecursive (XMLElement node, ArrayList classpath)
  {
    Enumeration toplevel_tags = node.enumerateChildren ();
    ArrayList ourclasspath = (ArrayList)classpath.clone ();
    ArrayList files = new ArrayList();

    while (toplevel_tags.hasMoreElements ())
    {
      XMLElement child = (XMLElement)toplevel_tags.nextElement ();

      if (child.getName ().equals ("classpath"))
      {
        changeClassPath (ourclasspath, child);
      }
      else if (child.getName ().equals ("job"))
      {
        CompilationJob subjob = collectJobsRecursive (child, ourclasspath);
        if (subjob != null)
          this.jobs.add (subjob);
      }
      else if (child.getName().equals ("directory"))
      {
        String name = child.getAttribute ("name");

        if (name != null)
        {
          // substitute variables
          String finalname = this.vs.substitute (name, "plain");

          files.addAll (scanDirectory (new File (finalname)));
        }

      }
      else if (child.getName().equals ("file"))
      {
        String name = child.getAttribute ("name");

        if (name != null)
        {
          // substitute variables
          String finalname = this.vs.substitute (name, "plain");

          files.add (new File (finalname));
        }

      }
      else if (child.getName().equals ("packdepency"))
      {
        String name = child.getAttribute ("name");

        if (name == null)
        {
          System.out.println ("invalid compilation spec: <packdepency> without name attribute");
          return null;
        }

        // check whether the wanted pack was selected for installation
        Iterator pack_it = this.idata.selectedPacks.iterator();
        boolean found = false;

        while (pack_it.hasNext())
        {
          com.izforge.izpack.Pack pack = (com.izforge.izpack.Pack)pack_it.next();

          if (pack.name.equals (name))
          {
            found = true;
            break;
          }
        }

        if (! found)
        {
          Debug.trace ("skipping job because pack " + name + " was not selected.");
          return null;
        }

      }

    }

    if (files.size() > 0)
      return new CompilationJob (this.listener, (String)node.getAttribute ("name"), files, ourclasspath);

    return null;
  }

  /** helper: process a <code>&lt;classpath&gt;</code> tag. */
  private void changeClassPath (ArrayList classpath, XMLElement child)
  {
    String add = child.getAttribute ("add");
    if (add != null)
      classpath.add (this.vs.substitute (add, "plain"));

    String sub = child.getAttribute ("sub");
    if (sub != null)
    {
      int cpidx = -1;
      sub = this.vs.substitute (sub, "plain");

      do
      {
        cpidx = classpath.indexOf (sub);
        classpath.remove (cpidx);
      } 
      while (cpidx >= 0);

    }
  }

  /** helper: recursively scan given directory.
   * 
   * @return list of files found (might be empty)
   */
  private ArrayList scanDirectory (File path)
  {
    Debug.trace ("scanning directory " + path.getAbsolutePath());

    ArrayList result = new ArrayList ();

    if (! path.isDirectory ())
      return result;

    File[] entries = path.listFiles ();

    for (int i = 0; i < entries.length; i++)
    {
      File f = entries[i];

      if (f == null) continue;
      
      if (f.isDirectory ())
      {
        result.addAll (scanDirectory (f));
      }
      else if ((f.isFile()) && (f.getName().toLowerCase().endsWith (".java")))
      {
        result.add (f);
      }

    }

    return result;
  }

  /** a compilation job */
  private class CompilationJob
  {
    private CompileListener listener;
    private String    name;
    private ArrayList files;
    private ArrayList classpath;
    // XXX: figure that out (on runtime?)
    private static final int MAX_CMDLINE_SIZE = 4096;

    public CompilationJob (CompileListener listener, ArrayList files, ArrayList classpath)
    {
      this.listener = listener;
      this.name = null;
      this.files = files;
      this.classpath = classpath;
    }

    public CompilationJob (CompileListener listener, String name, ArrayList files, ArrayList classpath)
    {
      this.listener = listener;
      this.name = name;
      this.files = files;
      this.classpath = classpath;
    }

    public String getName ()
    {
      if (this.name != null)
        return this.name;

      return "";
    }

    public int getSize ()
    {
      return this.files.size();
    }

    public boolean perform (String compiler, ArrayList arguments)
    {
      Debug.trace ("starting job " + this.name);
      // we have some maximum command line length - need to count
      int cmdline_len = 0;

      // used to collect the arguments for executing the compiler
      LinkedList args = new LinkedList(arguments);

      Iterator arg_it = args.iterator();
      while (arg_it.hasNext ())
        cmdline_len += ((String)arg_it.next()).length()+1;

      // add compiler in front of arguments
      args.add (0, compiler);
      cmdline_len += compiler.length()+1;

      // construct classpath argument for compiler
      // - collect all classpaths
      StringBuffer classpath_sb = new StringBuffer();
      Iterator cp_it = this.classpath.iterator();
      while (cp_it.hasNext ())
      {
        String cp = (String)cp_it.next();
        if (classpath_sb.length() > 0)
          classpath_sb.append (File.pathSeparatorChar);
        classpath_sb.append (cp);
      }

      String classpath_str = classpath_sb.toString ();

      // - add classpath argument to command line
      args.add ("-classpath");
      cmdline_len = cmdline_len + 11;
      args.add (classpath_str);
      cmdline_len += classpath_str.length()+1;

      // remember how many arguments we have which don't change for the job
      int common_args_no = args.size();
      // remember how long the common command line is
      int common_args_len = cmdline_len;

      // used for execution
      FileExecutor executor = new FileExecutor ();
      String output[] = new String[2];

      // used for displaying the progress bar
      String jobfiles = "";
      int fileno = 0;
      int last_fileno = 0;

      // now iterate over all files of this job
      Iterator file_it = this.files.iterator();

      while (file_it.hasNext())
      {
        File f = (File)file_it.next();

        String fpath = f.getAbsolutePath();

        Debug.trace ("processing "+fpath);
        
        fileno++;
        jobfiles += f.getName() + " ";
        args.add (fpath);
        cmdline_len += fpath.length();

        // start compilation if maximum command line length reached
        if (cmdline_len >= MAX_CMDLINE_SIZE)
        {
          Debug.trace ("compiling " + jobfiles);

          // display useful progress bar (avoid showing 100% while still
          // compiling a lot)
          this.listener.progressCompile (last_fileno, jobfiles);
          last_fileno = fileno;

          int retval = executor.executeCommand ((String[])args.toArray(output), output);

          // update progress bar: compilation of fileno files done
          this.listener.progressCompile (fileno, jobfiles);

          if (retval != 0)
          {
            System.out.println ("failed.");
            System.out.println ("command line: ");
            int argidx = 0;
            Iterator it = args.iterator();
            while ((argidx < common_args_no) && (it.hasNext()))
            {
              System.out.print ((String)it.next()+" ");
              argidx++;
            }
            System.out.println (jobfiles);
            System.out.println ("stderr of command follows:");
            System.out.println (output[0]);
            System.out.println ("stdout of command follows:");
            System.out.println (output[1]);
            this.listener.errorCompile ("compilation failed.");
            return false;
          }

          // clean command line: remove files we just compiled
          for (int i = args.size()-1; i >= common_args_no; i--)
          {
            args.removeLast ();
          }

          cmdline_len = common_args_len;
          jobfiles = "";
        }

      }

      if (cmdline_len > common_args_len)
      {
        this.listener.progressCompile (last_fileno, jobfiles);

        int retval = executor.executeCommand ((String[])args.toArray(output), output);

        this.listener.progressCompile (fileno, jobfiles);

        if (retval != 0)
        {
          System.out.println ("failed.");
          System.out.println ("command line: ");
          int argidx = 0;
          Iterator it = args.iterator();
          while ((argidx < common_args_no) && (it.hasNext()))
          {
            System.out.print ((String)it.next()+" ");
            argidx++;
          }
          System.out.println (jobfiles);
          System.out.println ("stderr of command follows:");
          System.out.println (output[0]);
          System.out.println ("stdout of command follows:");
          System.out.println (output[1]);
          this.listener.errorCompile ("compilation failed.");
          return false;
        }
      }

      Debug.trace ("job "+this.name+" done (" + fileno + " files compiled)");

      return true;
    }

  }

}

