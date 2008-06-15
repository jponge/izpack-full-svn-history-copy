package org.izpack.mojo;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.InterpolationFilterReader;

import com.izforge.izpack.compiler.CompilerConfig;

/**
 * Build an IzPack installer
 * 
 * @goal izpack
 * @phase package
 * @requiresDependencyResolution test
 * @version $Id:  $
 * @author Dan T. Tran
 * @author Miguel Griffa
 */
public class IzPackMojo
    extends AbstractMojo
{

    /**
     * IzPack descriptor file.  This plugin interpolates and saves this file to
     * a new location at ${izpackBasedir} and feed it to IzPack compiler
     * @parameter  default-value="${basedir}/src/izpack/install.xml"
     * @since alpha 1
     */
    private File descriptor;

    /**
     * IzPack base directory.  This is the recommended place to create
     * staging area as well.  Do not set it to any of the source tree.
     * @parameter default-value="${project.build.directory}/izpack"
     * @since alpha 1
     */
    private File izpackBasedir;

    /**
     * IzPack's kind argument.
     * @parameter expression="standard" default-value="standard"
     */
    private String kind;

    /**
     * Maven's file extension. 
     * @parameter default-value="jar"
     */
    private String fileExtension;

    /**
     * Internal Maven's project
     * @parameter expression="${project}"
     * @readonly
     */
    protected MavenProject project;

    /**
     * Maven component to install/deploy the installer(s)
     * 
     * @component
     * @readonly
     */
    private MavenProjectHelper projectHelper;

    /**
     * Dependencies and ${project.build.directory}/classes directory
     * @parameter expression="${project.compileClasspathElements}"
     */
    private List classpathElements;

    //////////////////////////////////////////////////////////////////////////////

    /**
     * Maven's classifier. Default to IzPack's kind when not given.  
     * Must be unique among Maven's executions
     */
    private String classifier;

    /**
     * The installer output file. Default to ${project.build.finalName)-classifier.fileExtension
     * Must be unique among Maven's executions
     */
    private File installerFile;

    public void execute()
        throws MojoExecutionException, MojoFailureException
    {
        init();

        buildInstaller();
    }

    private void init()
        throws MojoFailureException
    {
        classifier = this.kind;

        installerFile = new File( project.getBuild().getDirectory(), project.getBuild().getFinalName() + "-"
            + classifier + "." + fileExtension );

        File dir = installerFile.getParentFile();
        if ( !dir.exists() )
        {
            if ( !dir.mkdirs() )
            {
                throw new MojoFailureException( "Could not create directory " + dir );
            }
        }

        checkForDuplicateAttachArtifact();
    }

    private File preProcessConfigurationFile()
        throws MojoExecutionException
    {
        Properties p = project.getProperties();

        File interpolatedFile = new File( izpackBasedir, descriptor.getName() );

        FileWriter writer = null;

        FileReader fileReader = null;
        try
        {
            fileReader = new FileReader( descriptor );
            InterpolationFilterReader reader = new InterpolationFilterReader( fileReader, p );

            writer = new FileWriter( interpolatedFile );
            IOUtil.copy( reader, writer );
        }
        catch ( IOException e )
        {
            // All common causes to this should have been removed with previous checks.
            throw new MojoExecutionException( "Error while interpolating pkginfo.", e );
        }
        finally
        {
            IOUtil.close( writer );
            IOUtil.close( fileReader );
        }

        return interpolatedFile;
    }

    private void buildInstaller()
        throws MojoExecutionException
    {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader( getClassLoader( classLoader ) );

        try
        {
            String config = this.preProcessConfigurationFile().getAbsolutePath();
            String basedir = izpackBasedir.getAbsolutePath();

            CompilerConfig c = new CompilerConfig( config, basedir, kind, installerFile.getAbsolutePath() );

            c.executeCompiler();

            if ( !c.wasSuccessful() )
            {
                throw new MojoExecutionException( "IzPack compilation ERROR" );
            }
        }
        catch ( Exception ce )
        {
            throw new MojoExecutionException( "IzPack compilation ERROR", ce );
        }
        finally
        {
            if ( classLoader != null )
            {
                Thread.currentThread().setContextClassLoader( classLoader );
            }
        }

        projectHelper.attachArtifact( project, fileExtension, classifier, installerFile );
    }

    private void checkForDuplicateAttachArtifact()
        throws MojoFailureException
    {
        List attachedArtifacts = project.getAttachedArtifacts();

        Iterator iter = attachedArtifacts.iterator();

        while ( iter.hasNext() )
        {
            Artifact artifact = (Artifact) iter.next();
            if ( installerFile.equals( artifact.getFile() ) )
            {
                throw new MojoFailureException( "Duplicate installers found: " + installerFile );
            }
        }
    }

    private ClassLoader getClassLoader( ClassLoader classLoader )
        throws MojoExecutionException
    {
        List classpathURLs = new ArrayList();

        for ( int i = 0; i < classpathElements.size(); i++ )
        {
            String element = (String) classpathElements.get( i );
            try
            {
                File f = new File( element );
                URL newURL = f.toURI().toURL();
                classpathURLs.add( newURL );
                getLog().debug( "Added to classpath " + element );
            }
            catch ( Exception e )
            {
                throw new MojoExecutionException( "Error parsing classpath " + element + " " + e.getMessage() );
            }
        }

        URL[] urls = (URL[]) classpathURLs.toArray( new URL[classpathURLs.size()] );

        return new URLClassLoader( urls, classLoader );
    }

}
