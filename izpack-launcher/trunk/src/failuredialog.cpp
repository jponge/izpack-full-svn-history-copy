/* Copyright (c) 2004 Julien Ponge - All rights reserved.
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

#include "failuredialog.h"

FailureDialog::FailureDialog(const bool &enableJREInstall,
                             const bool &enableNetDownload)
  : wxDialog(0, -1, "IzPack launcher", wxDefaultPosition, wxDefaultSize,
             wxDEFAULT_DIALOG_STYLE | wxRESIZE_BORDER)
{
  userAction     = INTERNET;
  canInstallJRE  = enableJREInstall;
  canDownloadJRE = enableNetDownload;

  buildUI();
}

FailureDialog::~FailureDialog()
{

}

void FailureDialog::buildUI()
{
  // Inits
  wxString explanationMsg = "The launcher could not find a Java[tm] runtime "
                            "environment on this system.";
  sizer = new wxBoxSizer(wxVERTICAL);

  // Widgets

  explanationText = new wxStaticText(this, -1, explanationMsg, wxDefaultPosition);
  sizer->Add(explanationText, 0, wxALIGN_LEFT | wxALL, 10);

  wxString rlabels[] = {
    "manually specify a 'java' executable.",
    "install the Java[tm] runtime environment provided by the software package.",
    "download it from the Internet."
  };
  optionsBox = new wxRadioBox(this, -1, "I want to ...", wxDefaultPosition,
                              wxDefaultSize, 3, rlabels, 1, wxRA_SPECIFY_COLS);
  optionsBox->SetSelection(0);
  sizer->Add(optionsBox, 1, wxGROW | wxALL, 10);

  okButton = new wxButton(this, wxID_OK, "Ok");
  okButton->SetFocus();
  sizer->Add(okButton, 0, wxALIGN_CENTER_VERTICAL | wxALIGN_CENTER_HORIZONTAL
                          | wxALL, 10);

  optionsBox->Enable(1, canInstallJRE);
  optionsBox->Enable(2, canDownloadJRE);

  // Sizer
  SetSizer(sizer);
  sizer->SetSizeHints(this);
}

bool FailureDialog::Validate()
{
  switch (optionsBox->GetSelection())
  {
  case 0: userAction = MANUAL;   break;
  case 1: userAction = PROVIDED; break;
  case 2: userAction = INTERNET; break;
  default: break;
  }
  return true;
}
