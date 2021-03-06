package tests.izpack.frontend.view.stages.configure.panels;

import junit.framework.TestCase;

import org.w3c.dom.Element;

import utils.XML;
// JUnitDoclet end import

/**
* Generated by JUnitDoclet, a tool provided by
* ObjectFab GmbH under LGPL.
* Please see www.junitdoclet.org, www.gnu.org
* and www.objectfab.de for informations about
* the tool, the licence and the authors.
*/


public class HTMLInfoTest
// JUnitDoclet begin extends_implements
extends TestCase
// JUnitDoclet end extends_implements
{
  // JUnitDoclet begin class
  izpack.frontend.view.stages.configure.panels.HTMLInfo htmlinfo = null;
  // JUnitDoclet end class
  
  public HTMLInfoTest(String name) {
    // JUnitDoclet begin method HTMLInfoTest
    super(name);
    // JUnitDoclet end method HTMLInfoTest
  }
  
  public izpack.frontend.view.stages.configure.panels.HTMLInfo createInstance() throws Exception {
    // JUnitDoclet begin method testcase.createInstance
    return new izpack.frontend.view.stages.configure.panels.HTMLInfo();
    // JUnitDoclet end method testcase.createInstance
  }
  
  protected void setUp() throws Exception {
    // JUnitDoclet begin method testcase.setUp
    super.setUp();
    htmlinfo = createInstance();
    // JUnitDoclet end method testcase.setUp
  }
  
  protected void tearDown() throws Exception {
    // JUnitDoclet begin method testcase.tearDown
    htmlinfo = null;
    super.tearDown();
    // JUnitDoclet end method testcase.tearDown
  }
  
  public void testCreateXML() throws Exception {
    // JUnitDoclet begin method createXML
      
     assertNotNull(htmlinfo.createXML(XML.createDocument()));     
     
    // JUnitDoclet end method createXML
  }
  
  public void testInitFromXML() throws Exception {
    // JUnitDoclet begin method initFromXML
    // JUnitDoclet end method initFromXML
  }
  
  
  
  /**
  * JUnitDoclet moves marker to this method, if there is not match
  * for them in the regenerated code and if the marker is not empty.
  * This way, no test gets lost when regenerating after renaming.
  * Method testVault is supposed to be empty.
  */
  public void testVault() throws Exception {
    // JUnitDoclet begin method testcase.testVault
    // JUnitDoclet end method testcase.testVault
  }
  
  public static void main(String[] args) {
    // JUnitDoclet begin method testcase.main
    junit.textui.TestRunner.run(HTMLInfoTest.class);
    // JUnitDoclet end method testcase.main
  }
}
