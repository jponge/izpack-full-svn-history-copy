#include "izpack_frontend_view_win32_NativeIconAccessor.h"
#include "IconSet.h"
#include <string>

using namespace std;

IconSet *icons;
jclass clazz, expClazz;
jmethodID constructor;

/*
 * Class:     NativeIcon
 * Method:    initializeIconSet
 * Signature: (Ljava/lang/String;)Z
 */
JNIEXPORT void JNICALL Java_izpack_frontend_view_win32_NativeIconAccessor_initializeIconSet(JNIEnv *env, jobject obj, jstring sfilename)
{		
	const char *filename = env->GetStringUTFChars(sfilename, NULL);	

	clazz	 = env->FindClass("izpack/frontend/view/win32/NativeIcon");
	expClazz = env->FindClass("izpack/frontend/view/win32/NativeIconException");

	constructor = env->GetMethodID(clazz, "<init>", "(Ljava/nio/ByteBuffer;Ljava/nio/ByteBuffer;II)V");

	try
	{		
		icons = new IconSet(filename);	
	}
	catch (string message)
	{
		env->ThrowNew(expClazz, message.c_str());
	}	

	env->ReleaseStringUTFChars(sfilename, filename);
}

/*
 * Class:     NativeIcon
 * Method:    destroyIconSet
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_izpack_frontend_view_win32_NativeIconAccessor_destroyIconSet(JNIEnv *, jobject)
{	
	icons->destroy();
	delete icons;
}

/*
 * Class:     NativeIcon
 * Method:    getNumIcons
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_izpack_frontend_view_win32_NativeIconAccessor_getNumIcons(JNIEnv *env, jobject)
{
	return icons->getNumIcons();	
}

/*
 * Class:     izpack_frontend_view_win32_NativeIconAccessor
 * Method:    getNativeBuffers
 * Signature: ()LNativeIcon;
 */
JNIEXPORT jobject JNICALL Java_izpack_frontend_view_win32_NativeIconAccessor_getNativeBuffers(JNIEnv *env, jobject obj)
{		
	jobject iconBuffer = env->NewDirectByteBuffer(icons->getIconPixels(), icons->getMaxWidth() * icons->getMaxHeight() * sizeof(UINT));
	jobject maskBuffer = env->NewDirectByteBuffer(icons->getMaskPixels(), icons->getMaxWidth() * icons->getMaxHeight() * sizeof(UINT));

	if (iconBuffer != NULL && maskBuffer != NULL)
	{
		//The references go "bad" or something
		clazz	 = env->FindClass("izpack/frontend/view/win32/NativeIcon");
		constructor = env->GetMethodID(clazz, "<init>", "(Ljava/nio/ByteBuffer;Ljava/nio/ByteBuffer;II)V");

		jobject nativeIcon = env->NewObject(clazz, constructor, iconBuffer, maskBuffer, icons->getMaxWidth(), icons->getMaxHeight());

		if (nativeIcon != NULL)
			return nativeIcon;
		else
			env->ThrowNew(expClazz, "Unable to create native icon");
	}
	else
	{
		env->ThrowNew(expClazz, "Unable to allocate NIO buffers");
	}

	return NULL;
}

/*
 * Class:     izpack_frontend_view_win32_NativeIconAccessor
 * Method:    getNativeIcon
 * Signature: (I)[I
 */
JNIEXPORT jintArray JNICALL Java_izpack_frontend_view_win32_NativeIconAccessor_getNativeIcon(JNIEnv *env, jobject obj, jint index)
{		
	int *dimensions = NULL;

	try
	{
		dimensions = icons->getIcon(index);
	}
	catch (string message)
	{
		//I think this makes the native code return here
		//Nothing is initialized yet
		env->ThrowNew(expClazz, message.c_str());
	}	
	
	jintArray javaDimensions = env->NewIntArray(2);
	env->SetIntArrayRegion(javaDimensions, 0, 2, (const jint*) dimensions);

	delete dimensions;

	return javaDimensions;
}
