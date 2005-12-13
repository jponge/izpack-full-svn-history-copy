#include "iconset.h"
#include <iostream>
#include <vector>
#include <fstream>
#include "3dshade.h"

using namespace std;

//Load icons into *icons from the filename file, or throw an exception if there's an error
IconSet::IconSet(string filename)
{
	numIcons = ExtractIconEx(filename.c_str(), -1, NULL, NULL, 1);

	//Make icon store height and width
	icons =  vector<Icon>();	

	HICON *tmpIcons = new HICON[numIcons];

	//Load the large icons into an array
	numIcons = ExtractIconEx(filename.c_str(), 0, tmpIcons, NULL, numIcons);

	for (int i = 0; i < numIcons; i++)
	{
		icons.push_back(Icon(tmpIcons[i]));
	}
	
	getIconDimensions();

	delete tmpIcons;

    createMemBuffers();
}

//Free all of our buffers, release the DIB and DC
void IconSet::destroy()
{
	DeleteObject(iconBitmap);

	SelectObject(iconContext, oldObjIcon);	

	DeleteDC(iconContext);	
}

//Loop through all the icons, and figure out the maximum width and height	
void IconSet::getIconDimensions()
{	
	vector<Icon>::const_iterator start = icons.begin();
	vector<Icon>::const_iterator end = icons.end();

		width = height = 0;

	while (start != end)
	{
		Icon icon = *start;

		if (icon.getWidth() > width)
			width = icon.getWidth();

		if (icon.getHeight() > height)
			height = icon.getHeight();		

		start++;
	}	
}

void IconSet::getIcon(int index)
{
	BOOL result;
	//Clear the DC
	memset(pixels, 0x0, width * height * sizeof(UINT));	
	memset(mask, 0x0, width * height * sizeof(UINT));	

	//Paint the icon onto the DC		
	result = DrawIconEx(iconContext, 0, 0, icons[index].getIcon(), 0, 0, 0, NULL, DI_NORMAL); 
	checkResult(result, "Failed to draw icon into memory");

	//Paint the icon onto the DC		
	result = DrawIconEx(maskContext, 0, 0, icons[index].getIcon(), 0, 0, 0, NULL, DI_MASK); 
	checkResult(result, "Failed to draw icon alpha mask into memory");

	//pixels and mask now contain the data
}

//Create a memory DC of the screen and a DIB for painting
void IconSet::createMemBuffers()
{		
	BITMAPV5HEADER header;
	
	//Fill in the header so we create the right DIB
	memset(&header, 0, sizeof(BITMAPV5HEADER));
	header.bV5Size = sizeof(BITMAPV5HEADER);
	header.bV5Compression = BI_RGB;
	header.bV5Planes = 1;
	header.bV5BitCount = 32;
	header.bV5Width = width;
	header.bV5Height = height;

	//Set alpha masks
    header.bV5RedMask   =  0x00FF0000;
    header.bV5GreenMask =  0x0000FF00;
    header.bV5BlueMask  =  0x000000FF;
    header.bV5AlphaMask =  0xFF000000; 	

	header.bV5CSType = LCS_WINDOWS_COLOR_SPACE;
	header.bV5Intent = LCS_GM_GRAPHICS;

	//Create actual icon stuff
	iconContext = CreateCompatibleDC(NULL);
	checkResult(iconContext != NULL, "Unable to get a DC");

	iconBitmap = CreateDIBSection(iconContext, (BITMAPINFO *) &header, DIB_RGB_COLORS, (void**) &pixels, NULL, 0);
	checkResult(iconBitmap != NULL, "Unable to create a DIB for drawing");

	DIBSECTION *dib = new DIBSECTION;
	GetObject(iconBitmap, sizeof(*dib), dib);

	oldObjIcon = SelectObject(iconContext, iconBitmap);		

	//Create mask stuff
	maskContext = CreateCompatibleDC(NULL);
	checkResult(maskContext != NULL, "Unable to get a mask DC");

	maskBitmap = CreateDIBSection(maskContext, (BITMAPINFO *) &header, DIB_RGB_COLORS, (void**) &mask, NULL, 0);
	checkResult(maskBitmap != NULL, "Unable to create a mask DIB for drawing");

	DIBSECTION *dibMask = new DIBSECTION;
	GetObject(maskBitmap, sizeof(*dibMask), dibMask);

	oldObjMask = SelectObject(maskContext, maskBitmap);		

	delete dib, dibMask;
}

