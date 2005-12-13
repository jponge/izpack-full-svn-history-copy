#pragma once
#include <windows.h>
#include "ErrorCheck.h"

class Icon
{
public:
	Icon(HICON tIcon);

	HICON getIcon() { return icon; }
	int getWidth() { return width; }
	int getHeight() { return height; }

private:	
	HICON icon;
	int width;
	int height;
};
