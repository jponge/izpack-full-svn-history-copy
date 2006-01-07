#pragma once
#include <string>
#include <vector>
#include <windows.h>

#include "Icon.h"
#include "ErrorCheck.h"

using namespace std;

class IconSet
{
public:
	IconSet(const char *filename);	

	int* getIcon(int index);

	UINT* getIconPixels()
	{
		return pixels;
	}

	UINT* getMaskPixels()
	{	
		return mask;
	}

	UINT  getNumIcons()
	{
		return numIcons;
	}

	int getMaxWidth()
	{
		return width;
	}

	int getMaxHeight()
	{
		return height;
	}

	void destroy();

private:
	void createMemBuffers();
	void fillBitmapHeader(BITMAP *bitmap, BITMAPINFOHEADER *header);
	void getIconDimensions();	

	void createIconsEXEDLL(const char *filename);
	void createIconsICO(const char *filename);

	int width, height;
	int numIcons;

	UINT *pixels;
	UINT *mask;

	vector<Icon> icons;
	HDC iconContext, maskContext;
	HBITMAP iconBitmap, maskBitmap;

	HGDIOBJ oldObjIcon, oldObjMask;
};
