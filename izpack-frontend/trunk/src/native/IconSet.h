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
	IconSet(string filename);	

	void getIcon(int index);

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

	int getWidth()
	{
		return width;
	}

	int getHeight()
	{
		return height;
	}

	void destroy();

private:
	void createMemBuffers();
	void fillBitmapHeader(BITMAP *bitmap, BITMAPINFOHEADER *header);
	void getIconDimensions();	

	int width, height;
	int numIcons;

	UINT *pixels;
	UINT *mask;

	vector<Icon> icons;
	HDC iconContext, maskContext;
	HBITMAP iconBitmap, maskBitmap;

	HGDIOBJ oldObjIcon, oldObjMask;
};
