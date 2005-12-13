#include "ErrorCheck.h"

//Make sure the result != 0, if not, throw an exception so there's no messy ifs
//Use error code
void checkResult(BOOL result, char *errorMessage)
{
	if (!result)
	{
		cout << errorMessage << " Code: " << GetLastError() << endl;
		throw string(errorMessage);
	}
}
