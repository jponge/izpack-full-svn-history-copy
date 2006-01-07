#include "ErrorCheck.h"

//Make sure the result != 0, if not, throw an exception so there's no messy ifs

void checkResult(BOOL result, char *errorMessage)
{
	if (!result)
	{
		LPVOID msgBuf;
		FormatMessage( 
			FORMAT_MESSAGE_ALLOCATE_BUFFER | 
			FORMAT_MESSAGE_FROM_SYSTEM | 
			FORMAT_MESSAGE_IGNORE_INSERTS,
			NULL,
			GetLastError(),
			MAKELANGID(LANG_NEUTRAL, SUBLANG_DEFAULT), // Default language
			(LPTSTR) &msgBuf,
			0,
			NULL 
		);

		if (errorMessage == NULL)
			errorMessage = &"";

		string userErrorMessage(errorMessage);
		string sysErrorMessage((LPTSTR) msgBuf);

		LocalFree(msgBuf);

		string message(userErrorMessage + ": " + sysErrorMessage);
		
		throw message;
	}
}
