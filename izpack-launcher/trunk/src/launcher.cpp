/* Copyright (c) 2004 Julien Ponge - All rights reserved.
 * Some windows 98 debugging done by Dustin Sacks.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */

#include "launcher.h"

#ifdef __UNIX__
  #include <stdlib.h>
  #include <map>
  #include <list>
#endif


/*
 * Helper function to run an external program. It takes care of the subtule
 * differences between the OS-specific implementations.
 *
 * @param cmd The command to run.
 * @return <code>true</code> in case of a success, <code>false</code> otherwise.
 */
bool run_external(wxString cmd)
{
#ifdef __WINDOWS__
  int code = wxExecute(cmd, wxEXEC_SYNC);
  return (code == 0);
#else
  int code = wxExecute(cmd);
  return (code >= 0);
#endif
}

LauncherApp::LauncherApp()
  : wxApp()
{
  APPLICATION_NAME = wxString("Izpack-Launcher"); 
  completed = false;
  
  SetAppName(_( APPLICATION_NAME ));
  loadParams();
}

LauncherApp::~LauncherApp()
{

}

void LauncherApp::loadParams()
{
  cfgName = wxString( ".autorun.ini" );
  
  wxFileInputStream in( cfgName );
  wxFileConfig cfg( in );

  cfg.Read( "jar",      &paramsJar, wxEmptyString);
  cfg.Read( "jre",      &paramsJre, wxEmptyString);
  cfg.Read( "download", &paramsDownload, wxEmptyString);

#ifdef __WIN32__
  wxString group = "/win32";
#endif

#ifdef __LINUX__
  wxString group = "/linux";
#endif

#ifdef __SOLARIS__
  wxString group = "/solaris";
#endif

#ifdef __BSD__
  wxString group = "/bsd";
#endif

#ifdef __APPLE__
  wxString group = "/mac";
#endif

  cfg.SetPath(group);
  cfg.Read("jar", &paramsJar);
  cfg.Read("jre", &paramsJre);
  cfg.Read("download", &paramsDownload);

  if (paramsJar == wxEmptyString )
  {
    error(_("The configuration file '") + cfgName + _("' does not contain a jar file entry."));
  }
}

void LauncherApp::error(const wxString &msg)
{
  wxMessageDialog dlg(0, msg, _(APPLICATION_NAME), wxOK | wxICON_ERROR);
  dlg.ShowModal();
  exit(1);
}

bool LauncherApp::OnInit()
{
  locale.Init();
  locale.AddCatalog("launcher");

  if (searchJRE())
  {
    runJRE();
    exit(0);
  }
  else
  {
    FailureDialog dlg(paramsJre != wxEmptyString,
                      paramsDownload != wxEmptyString, APPLICATION_NAME );
    dlg.Centre();
    while (!completed)
    {
      if (dlg.ShowModal() != wxID_OK) return false;
      switch (dlg.getUserAction())
      {
      case MANUAL:   manualLaunch(); break;
      case PROVIDED: jreInstall();   break;
      case INTERNET: netDownload();  break;
      default: break;
      }
    }
  }
  return false;
}

bool LauncherApp::searchJRE()
{
#ifdef __WINDOWS__
  // Windows[tm] registry lookup
  wxString baseKey = "HKEY_LOCAL_MACHINE\\SOFTWARE\\JavaSoft"
                     "\\Java Runtime Environment\\";
  wxRegKey bKey(baseKey);
  if (bKey.Exists())
  {
    wxString version;
    if (bKey.QueryValue("CurrentVersion", version))
    {
      if (version != "1.1")
      {
        wxRegKey vKey(baseKey + version);
        wxString home;
        if (vKey.QueryValue("JavaHome", home))
        {
          javaExecPath = home + "\\bin\\javaw";
          return true;
        }
      }
    }
  }
#endif

  // Try to use JAVA_HOME
  char* envRes = getenv("JAVA_HOME");
  if (envRes)
  {
#ifdef __WINDOWS__
    javaExecPath = wxString(envRes) + "\\bin\\javaw";
#else
    javaExecPath = wxString(envRes) + "/bin/java";
#endif
    if (wxFileExists(javaExecPath)) return true;
  }

#ifndef __WINDOWS__
  /* Let's try to launch just 'java'.
   * We don't do that on Win32 because it will display an error
   * message if it fails. Anyway if we get here, we must not have
   * a well installed JRE ...
   */
  if (wxExecute("java -version", wxEXEC_SYNC) == 0)
  {
    javaExecPath = "java";
    return true;
  }
#endif

  // Failure
  return false;
}

void LauncherApp::runJRE()
{
  if (!wxFile::Exists(paramsJar))
  {
    error(_("The jar-file in the configuration file '" + cfgName + "' does exist."));
  }

  wxString cmd = javaExecPath + wxString(" -jar ") + paramsJar;
  if (!run_external(cmd))
  {
    error(_("The jar-file '" + paramsJar + "' could not be executed." ));
  }

  completed = true;
}

void LauncherApp::manualLaunch()
{
  wxString java = wxFileSelector(_("Please choose a 'java' executable."));

  if (java.empty())
  {
    return;
  }
  else
  {
    javaExecPath = java;
    runJRE();
  }
}

void LauncherApp::jreInstall()
{
#ifdef __WINDOWS__
  if (!run_external(paramsJre))
#else
  if (!wxShell(paramsJre))
#endif
  {
    error(_("The JRE could not be setup."));
  }
  searchJRE();
  runJRE();
  completed = true;
}

void LauncherApp::netDownload()
{
  wxString browser;

#ifdef __WINDOWS__
  // We use the default browser.
  browser = "rundll32 url.dll,FileProtocolHandler ";
#endif

#ifdef __UNIX__
  // We try some browsers and use the first successful one.
  std::list<std::pair<wxString, wxString> > commands;
  std::pair<wxString, wxString> cmd;
  cmd.first = "konqueror "; cmd.second = "konqueror -v";
  commands.push_back(cmd);
  cmd.first = "mozilla -splash "; cmd.second = "mozilla -v";
  commands.push_back(cmd);
  cmd.first = "firefox -splash "; cmd.second = "firefox -v";
  commands.push_back(cmd);
  cmd.first = "firebird -splash "; cmd.second = "firebird -v";
  commands.push_back(cmd);
  cmd.first = "opera "; cmd.second = "opera -v";
  commands.push_back(cmd);
  cmd.first = "netscape "; cmd.second = "netscape -v";
  commands.push_back(cmd);
  std::list<std::pair<wxString, wxString> >::iterator it;
  for (it = commands.begin(); it != commands.end(); ++it)
  {
    if (wxExecute(it->second, wxEXEC_SYNC) == 0)
    {
      browser = it->first;
      break;
    }
  }
#endif

  if (run_external(browser + paramsDownload))
  {
    completed = true;
  }
  else
  {
    error(_("Could not find a web browser."));
  }
}

IMPLEMENT_APP(LauncherApp)
