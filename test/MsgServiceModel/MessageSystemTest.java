package MsgServiceModel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import nahon.comm.un.message.MessageClient;
import nahon.comm.un.message.MessageService;
import nahon.comm.un.message.MessageException;
import nahon.comm.un.message.Action;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jiche
 */
public class MessageSystemTest {

    public static final int SimplePrint = 0x00;
    public static final int CanelCase = 0x01;
    public static final int Exception = 0x02;

    MessageClient Receiver;
    MessageClient Sender;

    private boolean PrintOutPut(Object... args) {
        String output = "Print:";
        for (Object in : args) {
            output += in;
        }
        System.out.println(output);
        return true;
    }

    public MessageSystemTest() throws MessageException {
        Receiver = MessageService.CreateMessageClient("0x00");
        Receiver.RegisterAction(CanelCase, new Action() {
            @Override
            public Object ExcuteCMD(Object... args) throws Exception {
                TimeUnit.SECONDS.sleep(100);
                return PrintOutPut(args);
            }
        });
        Receiver.RegisterAction(SimplePrint, new Action() {
            @Override
            public Object ExcuteCMD(Object... args) throws Exception {
                return PrintOutPut(args);
            }
        });
        Receiver.RegisterAction(Exception, new Action() {
            @Override
            public Object ExcuteCMD(Object... args) throws Exception {
                throw new Exception("ExcuteException");
            }
        });
        Sender = MessageService.CreateMessageClient("0x01");
    }

    @BeforeClass

    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
//        MessageSystem.ShutDownCMDSystem();
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of CreateCMDService method, of class CMDSystem.
     */
    @Test
    public void testUnknowCMDTest() {
        System.out.println("testUnknowCMD");

        try {
            Future result = Sender.SenderMessage(Receiver.ServiceID, 0xFF, "SimplePrintTest");
            if ((Boolean) result.get() == true) {
                System.out.println("testPrintCMDTest:finish");
            }
            fail("testPrintCMD:failed");
        } catch (ExecutionException ex) {
            System.out.println(ex.getMessage());
            System.out.println("testPrintCMD:finish");

        } catch (InterruptedException ex) {
            Logger.getLogger(MessageSystemTest.class
                    .getName()).log(Level.SEVERE, null, ex);
            fail(
                    "testPrintCMD:failed");
        } catch (MessageException ex) {
            Logger.getLogger(MessageSystemTest.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of CreateCMDService method, of class CMDSystem.
     */
    @Test
    public void testSimplePrintCMDTest() {
        System.out.println("testPrintCMDTest");

        try {
            Future result = Sender.SenderMessage(Receiver.ServiceID, SimplePrint, "SimplePrintTest");
            if ((Boolean) result.get() == true) {
                System.out.println("testPrintCMDTest:finish");
            }
        } catch (InterruptedException | ExecutionException ex) {
            System.out.println(ex.getMessage());
            fail("testPrintCMDTest:failed");

        } catch (MessageException ex) {
            Logger.getLogger(MessageSystemTest.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of CreateCMDService method, of class CMDSystem.
     */
    @Test
    public void testCannelCMDTest() {
        System.out.println("testCannelCMDTest");

        try {
            Future result = Sender.SenderMessage(Receiver.ServiceID, CanelCase);
            TimeUnit.MILLISECONDS.sleep(100);
            result.cancel(true);
            result.get();

        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(MessageSystemTest.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (CancellationException ex) {
            System.out.println(ex);
            System.out.println("testCannelCMDTest:finish");
            return;

        } catch (MessageException ex) {
            Logger.getLogger(MessageSystemTest.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        fail("testCannelCMDTest:failed");
    }

    /**
     * Test of CreateCMDService method, of class CMDSystem.
     */
    @Test
    public void testExcuteExceptionTest() {
        System.out.println("testExcuteExceptionTest");

        try {
            Future result = Sender.SenderMessage(Receiver.ServiceID, Exception);
            result.get();

        } catch (InterruptedException ex) {
            Logger.getLogger(MessageSystemTest.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            System.out.println(ex.getMessage());
            System.out.println("testExcuteExceptionTest:finish");
            return;

        } catch (MessageException ex) {
            Logger.getLogger(MessageSystemTest.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        fail("testCannelCMDTest:failed");
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of CreateCMDService method, of class CMDSystem.
     */
    @Test
    public void testShutDownTest() {
        System.out.println("testShutDownTest");
        try {
            Future result = Sender.SenderMessage(Receiver.ServiceID, Exception);
            result.get();

        } catch (InterruptedException ex) {
            Logger.getLogger(MessageSystemTest.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            System.out.println(ex.getMessage());
            System.out.println("testShutDownTest:finish");
            return;

        } catch (MessageException ex) {
            Logger.getLogger(MessageSystemTest.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        fail("testShutDownTest:failed");
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of CreateCMDService method, of class CMDSystem.
     */
    @Test
    public void testTimeoutTest() {
        System.out.println("testTimeoutTest");
        Future result = null;
        try {
            result = Sender.SenderMessage(Receiver.ServiceID, CanelCase);
            if ((Boolean) result.get(100, TimeUnit.MILLISECONDS) == true) {
                fail("testTimeoutTest:failed");
            }
        } catch (InterruptedException | ExecutionException ex) {
            System.out.println(ex.getMessage());
            fail("testTimeoutTest:failed");
        } catch (TimeoutException ex) {
            System.out.println(ex);
            result.cancel(true);
            System.out.println("testTimeoutTest:finish");

        } catch (MessageException ex) {
            Logger.getLogger(MessageSystemTest.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }
}
