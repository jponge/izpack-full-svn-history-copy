/*
 * Created on Apr 6, 2006
 * 
 * $Id: AllTests.java Feb 8, 2004 izpack-frontend
 * Copyright (C) 2005 Andy Gombos
 * 
 * File : AllTests.java 
 * Description : TODO Add description
 * Author's email : gumbo@users.berlios.de
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tests.izpack.frontend.view.stages.configure.panels;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests
{

    public static void main(String[] args)
    {
        junit.swingui.TestRunner.run(AllTests.class);
    }

    public static Test suite()
    {
        TestSuite suite = new TestSuite(
                        "Test for test.izpack.frontend.view.stages.configure.panels");
        //$JUnit-BEGIN$
        suite.addTestSuite(HTMLInfoTest.class);
        suite.addTestSuite(LicenseTest.class);
        suite.addTestSuite(HTMLLicenseTest.class);
        suite.addTestSuite(InfoTest.class);
        suite.addTestSuite(ShortcutPanelTest.class);
        suite.addTestSuite(XInfoTest.class);
        suite.addTestSuite(JDKPathTest.class);
        //$JUnit-END$
        return suite;
    }

}
