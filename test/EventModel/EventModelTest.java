/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package EventModel;

import nahon.comm.event.NEvent;
import nahon.comm.event.NEventCenter;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import nahon.comm.event.NEventListener;

/**
 *
 * @author jiche
 */
public class EventModelTest {

    NEventCenter<Integer> instance = new NEventCenter();
    private int value = 0;

    public void SetResult(int value) {
        this.value = value;
    }

    public EventModelTest() {
    }

    @BeforeClass
    public static void setUpClass() {
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
     * Test of RegeditListener method, of class NEventCenter.
     */
    @Test
    public void testRegeditListener() {
        System.out.println("regeditListener");

        int Event = Math.round(100);

        NEventListener Linstance = new NEventListener() {
            @Override
            public void recevieEvent(NEvent Event) {
                SetResult((Integer)Event.GetEvent());
            }
        };

        instance.RegeditListener(Linstance);
        instance.CreateEvent(Event);
    }

    /**
     * Test of RemoveListenner method, of class NEventCenter.
     */
    @Test
    public void testRemoveListenner() {
        System.out.println("removeListenner");

        int Event = 10;

        NEventListener Linstance = new NEventListener() {
            @Override
            public void recevieEvent(NEvent Event) {
                SetResult((Integer)Event.GetEvent());
            }
        };

        instance.RegeditListener(Linstance);
        instance.RemoveListenner(Linstance);
        instance.CreateEvent(Event);
        assertEquals(this.value == Event, false);
    }
}
