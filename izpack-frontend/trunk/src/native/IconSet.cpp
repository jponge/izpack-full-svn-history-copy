#include "iconset.h"
#include <iostream>
#include <vector>
#include <fstream>

#include "IconFileLoader.h"

#include <string.h>

using namespace std;

//Load icons into *icons from the filename file, or throw an exception if there's an error
IconSet::IconSet(const char *filename)
{
	if (filename == NULL)
		throw string("Filename is invalid");
	
	char *lowerFilename = strlwr( strdup(filename) );

	//If not a .ICO file
	if (strstr(lowerFilename, "ico") == NULL)
	{
		createIconsEXEDLL(filename);
	}
	else
	{
		createIconsICO(filename);
	}

	if (numIcons <= 0)
		throw string("No icons in file");
	
	//Determine the largest our canvas needs to be
	getIconDimensions();

    createMemBuffers();
}

//Free all of our buffers, release the DIB and DC
void IconSet::destroy()
{
	SelectObject(iconContext, oldObjIcon);	
	SelectObject(maskContext, oldObjMask);

	DeleteObject(iconBitmap);
	DeleteObject(maskBitmap);

	DeleteDC(iconContext);	
	DeleteDC(maskContext);

	//Delete the icon resources
	vector<Icon>::const_iterator start = icons.begin();
	vector<Icon>::const_iterator end = icons.end();

		width = height = 0;

	while (start != end)
	{
		Icon icon = *start;
		DestroyIcon( icon.getIcon() );

		start++;
	}

	pixels = NULL;
	mask = NULL;
}

void IconSet::createIconsEXEDLL(const char *filename)
{
	numIcons = ExtractIconEx(filename, -1, NULL, NULL, 1);

	//Make icon store height and width
	icons =  vector<Icon>();	

	HICON *tmpIcons = new HICON[numIcons];

	//Load the large icons into an array
	numIcons = ExtractIconEx(filename, 0, tmpIcons, NULL, numIcons);

	for (int i = 0; i < numIcons; i++)
	{
		icons.push_back(Icon(tmpIcons[i]));		
	}

	if (numIcons > 0)
		delete tmpIcons;
}

void IconSet::createIconsICO(const char *filename)
{
	IconFileLoader iconLoader(filename);

	numIcons = iconLoader.getNumIcons();

	for (int i = 0; i < numIcons; i++)
	{
		icons.push_back(Icon(iconLoader.getIcon(i)));
	}
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

int* IconSet::getIcon(int index)
{
	if (index < 0 || index > numIcons)
		throw string("Icon index not in range");

	BOOL result;
	//Clear the DC
	memset(pixels, 0x0, width * height * sizeof(UINT));	
	memset(mask, 0x0, width * height * sizeof(UINT));	

	Icon icon = icons[index];

	//Paint the icon onto the DC		
	result = DrawIconEx(iconContext, 0, 0, icon.getIcon(), icon.getWidth(), icon.getHeight(), 0, NULL, DI_NORMAL); 	
	checkResult(result, "Failed to draw icon into memory");

	//Paint the icon onto the DC		
	result = DrawIconEx(maskContext, 0, 0, icon.getIcon(), icon.getWidth(), icon.getHeight(), 0, NULL, DI_MASK); 
	checkResult(result, "Failed to draw icon alpha mask into memory");

	//pixels and mask now contain the data
	int *dimensions = new int[2];
	dimensions[0] = icon.getWidth();
	dimensions[1] = icon.getHeight();

	return dimensions;
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

