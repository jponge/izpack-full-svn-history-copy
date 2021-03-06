/*
 *  $Id$
 *  COIOSHelper
 *  Copyright (C) 2005 Klaus Bartz
 *
 *  File :               WinLibEnv.cxx
 *  Description :        Source file with environment classes for exception handling etc.
 *                       
 *  Author's email :     bartzkau@users.berlios.de
 *  Website :            http://www.izforge.com
 * 
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */


#include "jni.h"
#include <windows.h>
#include "WinLibEnv.h"

// --------------------------------------------------------------------------
// A "lookup table" to  get full name and signature Id for a short exception name
// The last element of this array must contain a type Id < 1 else an SigSegV will
// be thrown.
// --------------------------------------------------------------------------
ExceptionNameRecord WinLibEnv::ExceptionNameMap[] =
{ 
	ExceptionNameRecord("Exception", "java/lang/Exception", 1, 1 ),
	ExceptionNameRecord("NativeLibException", "com/coi/tools/os/win/NativeLibException", 2, 2 ),
	ExceptionNameRecord("OutOfMemoryError", "java/lang/OutOfMemoryError", 1, 1 ),
	ExceptionNameRecord("IllegalArgumentException", "java/lang/IllegalArgumentException", 1, 1 ),
	ExceptionNameRecord("NullPointerException", "java/lang/NullPointerException", 1, 1 ),
	ExceptionNameRecord("", "", -1, -1 )
};

// --------------------------------------------------------------------------
// A "lookup table" to  get signature from an Id which is given in ExceptionNameMap
// --------------------------------------------------------------------------
char *WinLibEnv::ExceptionSignatureMap[] =
{
	"()V",
	"(Ljava/lang/String;)V",
	"(IILjava/lang/String;Ljava/lang/String;)V"
};

// --------------------------------------------------------------------------
// Ctor of environment object.
// --------------------------------------------------------------------------
WinLibEnv::WinLibEnv(JNIEnv *env, jobject obj)
{
	jniEnv	= env;
	jniObj	= obj;
	initialize();
	reset();
}


// --------------------------------------------------------------------------
// Destructor of environment object.
// --------------------------------------------------------------------------
WinLibEnv::~WinLibEnv()
{
	reset();
}

// --------------------------------------------------------------------------
// Reset the environment object to a defined state; delete all allocated 
// strings.
// --------------------------------------------------------------------------
void WinLibEnv::reset()
{
	if( win32ErrorText )
		delete [] win32ErrorText;
	if( winLibErrorText )
		delete [] winLibErrorText;
	for( int i = 0; i < STD_ARRAY_LENGTH; ++i)
		if( args[i] )
			delete [] args[i];
	initialize();

}

// --------------------------------------------------------------------------
// Initialize the environment object to a defined state.
// --------------------------------------------------------------------------
void WinLibEnv::initialize()
{
	winLibErrorText = win32ErrorText = NULL;
	status	= WLES_INITIALIZED;
	win32Error = 0;
	winLibError	= 0;
	currentArg = 0;
	for( int i = 0; i < STD_ARRAY_LENGTH; ++i)
		args[i] = 0;

}



// --------------------------------------------------------------------------
// Return a deep copy of the environment object.
// --------------------------------------------------------------------------
WinLibEnv *WinLibEnv::clone()
{
	WinLibEnv *ret = new WinLibEnv(jniEnv, jniObj);
	ret->takeAcross( this );
	ret->win32Error = win32Error;
	ret->winLibError = winLibError;
	ret->status = status;
	if( winLibErrorText != NULL )
		ret->setError( winLibErrorText, exceptionTypeName);
	if( win32ErrorText )
	{
		ret->win32ErrorText = new char[ strlen(win32ErrorText) + 2];
		strcpy( ret->win32ErrorText,win32ErrorText );
	}
	for( int i = 0; i < STD_ARRAY_LENGTH; ++i)
	{
		if(  args[i] )
		{
			ret->args[i] = new char[ strlen(args[i]) + 2];
			strcpy( ret->args[i], args[i] );
		}
	}
	return( ret);
}


// --------------------------------------------------------------------------
// Take all settings of the given environment object in this one.
// At first this method calls reset to clean up this object.
// --------------------------------------------------------------------------
void WinLibEnv::takeAcross( WinLibEnv *from)
{
	reset();
	win32Error = from->win32Error;
	winLibError = from->winLibError;
	status = from->status;
	if( from->winLibErrorText != NULL )
		setError( from->winLibErrorText, from->exceptionTypeName);
	else
		winLibErrorText = NULL;
	if( from->win32ErrorText )
	{
		win32ErrorText = new char[ strlen(from->win32ErrorText) + 2];
		strcpy( win32ErrorText,from->win32ErrorText );
	}
	else
		win32ErrorText = NULL;
	for( int i = 0; i < STD_ARRAY_LENGTH; ++i)
	{
		if(  from->args[i] )
		{
			args[i] = new char[ strlen(from->args[i]) + 2];
			strcpy( args[i], from->args[i] );
		}
	}
}

// --------------------------------------------------------------------------
// Sets an error. Previos existent string will be deleted.
// The given messages will be copied.
// --------------------------------------------------------------------------
void WinLibEnv::setError( char *err, char *errType )
{
	status = WLES_ERROR;
	exceptionTypeName = errType;
	if( winLibErrorText )
		delete [] winLibErrorText;
	winLibErrorText = new char[ strlen(err) + 2];
	strcpy( winLibErrorText, err );

}

// --------------------------------------------------------------------------
// Adds a copy of the given string into the internal argument array.
// Only 16 arguments are supported, more will be ignored.
// --------------------------------------------------------------------------
void WinLibEnv::addArg( const char *arg )
{
	if( currentArg > STD_ARRAY_LENGTH || ! arg)
		return;
	args[currentArg] = new char[ strlen(arg) + 2];
	strcpy( args[currentArg++], arg );
}


// --------------------------------------------------------------------------
// Gets the error message of the current system error from the OS and
// stores it in the internal member win32ErrorText.
// --------------------------------------------------------------------------
void WinLibEnv::getOSMessage()
{
	LPVOID lpMsgBuf;
	if (!FormatMessage( 
		FORMAT_MESSAGE_ALLOCATE_BUFFER | 
		FORMAT_MESSAGE_FROM_SYSTEM | 
		FORMAT_MESSAGE_IGNORE_INSERTS,
		NULL,
		win32Error,
		MAKELANGID(LANG_NEUTRAL, SUBLANG_DEFAULT), // Default language
		(LPTSTR) &lpMsgBuf,
		0,
		NULL ))
	{
	   // Handle the error.
	   return;
	}
	if( win32ErrorText )
		delete [] win32ErrorText;
	win32ErrorText = new char[ strlen((LPTSTR) lpMsgBuf) + 2];
	strcpy( win32ErrorText, (LPTSTR) lpMsgBuf );
	// Free the buffer.
	LocalFree( lpMsgBuf );	
	
}

//----------------------------------------------------------------------
// Verifies whether one of the given java objects are a null object or not.
// If a null object is detected, a NullPointerException will be set as
// error.
//----------------------------------------------------------------------
jboolean WinLibEnv::verifyNullObjects(jobject obj1, jobject obj2, jobject obj3, jobject obj4)
{
	if( obj1 == NULL || obj2 == NULL || obj3 == NULL || obj4 == NULL )
	{
		setError( "", "NullPointerException" );
		return( false );
	}
	return( true );
}



/*
 * Methode which handles previosly setted errors.
 * If no error was setted, it returns immediately.
 * If an error was setted, the choosen throw object will be created and
 * send to Java via JNIEnv->Throw.
 * This method should be called at end of every JNI function.
 */
jboolean WinLibEnv::verifyAndThrowAtError()
{
	jclass clazz = NULL;
	jmethodID mid = NULL;
	jthrowable throwObj = NULL;
	if( status < WLES_WARNING )
		return( false );
	if( status == WLES_WARNING )
	// TODO: impl warnings pending
		return( false );
	while( status == WLES_ERROR ) // what else ??
	{
		// 1. What exception should be thrown ??
		ExceptionNameRecord *exNameRec = getExceptionNameRecord( exceptionTypeName );
		jstring jliberr = NULL;
		
		int	typeId = exNameRec->getTypeId();
		// 2. Get the class of the exception.
		if( (clazz = jniEnv->FindClass(exNameRec->getLongName()) ) == NULL )
			break;	// Oops ... exception not found; forgotten to load a "custom" exception into VM ??
		// 3. Get the constructor of the exception.
		if( (mid = jniEnv->GetMethodID( clazz,
				"<init>", ExceptionSignatureMap[ exNameRec->getSignatureId() ]) ) == NULL )
			break;
		// 4. Transform given error messages to Java.
		if( winLibErrorText )
			jliberr = jniEnv->NewStringUTF( winLibErrorText );
		else
			jliberr = jniEnv->NewStringUTF( "" );
		// 5. Handle exception dependant to the type id. Create throw object.
		// This is the place to add more different exceptions. It is not necessary
		// to define a different type id for every exception, else only for one which has
		// a different signature or a different calling convention. E.g. all exceptions which
		// will be created with one message string uses type 1. Only WinLibEnv::ExceptionNameMap
		// should be adapted then to the new exception.
		switch( typeId )
		{
		case 0:	// supports signature "()V"
			throwObj = (jthrowable) jniEnv->NewObject( clazz, mid );
			break;
		case 1:	// supports signature "(Ljava/lang/String;)V"
			throwObj = (jthrowable) jniEnv->NewObject( clazz, mid,  jliberr );
			break;
		case 2: // supports signature "(IILjava/lang/String;Ljava/lang/String;)V"
			{	// Initial this is the case for our own exception NativeLibException.
				jstring jwin32 = NULL;
				jstring jarg = NULL;
				jmethodID midAdd = NULL;
				jvalue argVals[1];
				if( win32Error )
					getOSMessage();
				if( win32ErrorText )
					jwin32 = jniEnv->NewStringUTF( win32ErrorText );
				throwObj = (jthrowable) jniEnv->NewObject( clazz, mid, winLibError, 
					win32Error, jliberr, jwin32 );
				// Set additional arguments to the throw object if exist.
				midAdd = jniEnv->GetMethodID( clazz, "addArgument", "(Ljava/lang/String;)V"  );
				for( int i = 0; i < currentArg; ++i )
				{
					if( args[i] )
						jarg = jniEnv->NewStringUTF( args[i] );
					else
						jarg = NULL;
					if( jarg == NULL )
						continue;
					argVals[0].l = jarg;
					jniEnv->CallVoidMethodA( throwObj, midAdd, argVals);
				}
			}
			break;
		default:
			break;
		}
		// 6. Last step here ... throw it ...
		// Attention!! after Throw we are not up up and away ... it is a Java throw, not a C++ throw.
		// It is recomanded to leave a native method after Throw as fast as possible; but do not
		// forget to clean up C++ part.
		if( throwObj )
			jniEnv->Throw( throwObj );
		return( true );
	}
	while( clazz == NULL || mid == NULL || throwObj == NULL )
	{
		if( (clazz = jniEnv->FindClass("java/lang/Exception") ) == NULL )
			break;
		jniEnv->ThrowNew( clazz, "Internal error in exception handling of WinLibEnv at creating special exception object.");
		return( true );
	}
	return( true ); // Should never be ... only possible if class Exception not found.
	
	
}

// --------------------------------------------------------------------------
// Returns a ExceptionNameRecord for the given short exception name.
// --------------------------------------------------------------------------
ExceptionNameRecord *WinLibEnv::getExceptionNameRecord( char *exName )
{
	
	for( int i = 0; i < 100; ++i )
	{
		if( ExceptionNameMap[i].getTypeId()  < 0 )
			return( &ExceptionNameMap[0] );
		if( ! strcmp( exName, ExceptionNameMap[i].getShortName() ) )
			return( &ExceptionNameMap[i] );
	}
	return( &ExceptionNameMap[0] ); // Should never be ...
}



