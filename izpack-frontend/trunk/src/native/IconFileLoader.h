#pragma once
#include <windows.h>
#include <string>

#include <fstream>
#include <iostream>

#include <vector>

#include "IconFileStructs.h"

using namespace std;

class IconFileLoader
{
public:
	IconFileLoader(const char *filename);
	~IconFileLoader(void);	

	int getNumIcons();
	
	HICON getIcon(int index);
	
private:
	vector<ICONDIRENTRY> icons;
	HANDLE iconFile;

	int numIcons;

	HICON createIconFromFile(ICONDIRENTRY icon);
};
