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

LauncherApp::LauncherApp()
  : wxApp()
{
  completed = false;
  
  SetAppName(_("IzPack launcher"));
  loadParams();
}

LauncherApp::~LauncherApp()
{

}

void LauncherApp::loadParams()
{
  wxFileInputStream in("launcher.ini");
  wxFileConfig cfg(in);

  cfg.Read("jar", &params["jar"], wxEmptyString);
  cfg.Read("jre", &params["jre"], wxEmptyString);
  cfg.Read("download", &params["download"], wxEmptyString);

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
  cfg.Read("jar", &params["jar"]);
  cfg.Read("jre", &params["jre"]);
  cfg.Read("download", &params["download"]);

  if (params["jar"] == wxEmptyString)
  {
    error(_("The configuration file is broken."));
  }
}

void LauncherApp::error(const wxString &msg)
{
  wxMessageDialog dlg(0, msg, _("IzPack launcher"), wxOK | wxICON_ERROR);
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
    FailureDialog dlg(params["jre"] != wxEmptyString,
                      params["download"] != wxEmptyString);
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
      wxRegKey vKey(baseKey + version);
      wxString home;
      if (vKey.QueryValue("JavaHome", home))
      {
        javaExecPath = home + "\\bin\\javaw";
        return true;
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
    return true;
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
  if (!wxFile::Exists(params["jar"]))
  {
    error(_("There is no installer to launch."));
  }

  /* On win98 using an ASYNC wxExecute call retuns a negative return value.
   * So the wxExecute call is SYNC. This means that flow of control will not 
   * return until the other program (the java installer) has finished.
   *
   * On win98 this SYNC call returns 0 on success.
   */
  wxString cmd = javaExecPath + wxString(" -jar ") + params["jar"];        
#ifdef __WINDOWS__
  int code = wxExecute(cmd, wxEXEC_SYNC);
#else
  int code = wxExecute(cmd);
#endif
  if(code < 0)
  {
    error(_("The installer launch failed.")); 
  }  

  completed = true;
}

void LauncherApp::manualLaunch()
{
  wxString java = wxFileSelector(_("Please choose a 'java' executable"));
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
  if (wxExecute(params["jre"]) <= 0)
  {
    error(_("Could not launch the JRE installation process."));
  }
#else
  if (!wxShell(params["jre"]));
  {
    error(_("Could not launch the JRE installation process."));
  }
#endif

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

#ifdef __WINDOWS__
  // On win98 this wxExecute call returns 0 on success
  if (wxExecute(browser + params["download"], wxEXEC_SYNC) == 0)
#else
  if (wxExecute(browser + params["download"]) >= 0)
#endif
  {
    completed = true;
  }
  else
  {
    error(_("Could not find a web browser."));
  }
}

IMPLEMENT_APP(LauncherApp)
