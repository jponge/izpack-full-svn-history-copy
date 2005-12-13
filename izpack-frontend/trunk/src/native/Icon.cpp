#include "icon.h"

Icon::Icon(HICON tIcon)
{	
	ICONINFO *iconInfo = new ICONINFO;
	BITMAP *bitmap = new BITMAP;

	checkResult(GetIconInfo(tIcon, iconInfo), "Error getting properties of an icon");     

	checkResult(GetObject(iconInfo->hbmColor, sizeof(BITMAP), bitmap), "Error getting bitmap header");

	//Fill the icon object for storage
	this->icon = tIcon;
	this->width = bitmap->bmWidth;
	this->height = bitmap->bmHeight;

	//Clean up
	delete bitmap, iconInfo;
}