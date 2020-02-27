/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Language;

import nahon.comm.tool.languange.LanguageHelper;
import nahon.comm.tool.languange.LanguageNode.Language;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author jiche
 */
public class LanguageHelperTest {

    public LanguageHelperTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        LanguageHelper.getIntance().SetText("Text", Language.Chinese, "中文");
        LanguageHelper.getIntance().SetText("Text", Language.English, "英文");
        LanguageHelper.getIntance().SetText("Text", Language.Japanese, "日文");
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getText method, of class LanguageHelper.
     */
    @Test
    public void testGetText() throws Exception {
        System.out.println("getText");
        String name = "Text";

        LanguageHelper.getIntance().SetLanguage(Language.Chinese);
        String expResult = LanguageHelper.getIntance().GetText(name);
        assertEquals(expResult, "中文");

        LanguageHelper.getIntance().SetLanguage(Language.English);
        expResult = LanguageHelper.getIntance().GetText(name);
        assertEquals(expResult, "英文");

        LanguageHelper.getIntance().SetLanguage(Language.Japanese);
        expResult = LanguageHelper.getIntance().GetText(name);
        assertEquals(expResult, "日文");
        // TODO review the generated test code and remove the default call to fail.
    }
}
