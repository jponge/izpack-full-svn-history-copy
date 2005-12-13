#include "izpack_frontend_view_win32_NativeIconAccessor.h"
#include "IconSet.h"
#include <string>

using namespace std;

IconSet *icons;
jclass clazz;
jmethodID constructor;

/*
 * Class:     NativeIcon
 * Method:    initializeIconSet
 * Signature: (Ljava/lang/String;)Z
 */
JNIEXPORT jboolean JNICALL Java_izpack_frontend_view_win32_NativeIconAccessor_initializeIconSet(JNIEnv *env, jobject obj, jstring sfilename)
{
	const char *filename = env->GetStringUTFChars(sfilename, false);

	icons = new IconSet(string(filename));

	clazz = env->FindClass("izpack/frontend/view/win32/NativeIcon");

	constructor = env->GetMethodID(clazz, "<init>", "(Ljava/nio/ByteBuffer;Ljava/nio/ByteBuffer;)V");

	env->ReleaseStringUTFChars(sfilename, filename);

	return 0;
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
	jobject iconBuffer = env->NewDirectByteBuffer(icons->getIconPixels(), icons->getWidth() * icons->getHeight() * sizeof(UINT));
	jobject maskBuffer = env->NewDirectByteBuffer(icons->getMaskPixels(), icons->getWidth() * icons->getHeight() * sizeof(UINT));

	jobject nativeIcon = env->NewObject(clazz, constructor, iconBuffer, maskBuffer);

	return nativeIcon;
}

/*
 * Class:     izpack_frontend_view_win32_NativeIconAccessor
 * Method:    getNativeIcon
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_izpack_frontend_view_win32_NativeIconAccessor_getNativeIcon(JNIEnv *env, jobject obj, jint index)
{		
	icons->getIcon(index);
}
