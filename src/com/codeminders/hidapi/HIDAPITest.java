package com.codeminders.hidapi;

import java.applet.Applet;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import netscape.javascript.*;

/**
 * This class demonstrates enumeration, reading and getting notifications when a
 * HID device is connected/disconnected.
 */
public class HIDAPITest extends Applet {

    private static final long READ_UPDATE_DELAY_MS = 50L;
    private static JSObject window;

    static {

        System.load("C:\\Users\\Hunter\\Documents\\NetBeansProjects\\HelloApplet\\jni\\hidapi-jni.dll");

    }
    // initialize ID vars
    static int VENDOR_ID = Utilities.VENDOR_ID;
    static int PRODUCT_ID = Utilities.PRODUCT_ID;
    private static final int BUFSIZE = 2048;
    private static final int ORDER = 2;
    private static Utilities util;

    /**
     * @param args input strings value.
     */
    public void init() {

        System.out.println("before read device");
        util = new Utilities();
        util.readDevice();
        System.out.println("after read device");
    }

    // creates a js window
    public String setWindow() {
        try {
            window = JSObject.getWindow(this);
            window.eval("console.log('test222222');");
            util.setWindow(window);
        } catch (JSException jse) {
        }

        return "yay";
    }

    // method that tests the javascript comm
    public String testMethod(String text) {
        try {
            window = JSObject.getWindow(this);

            window.eval("console.log('" + text + "');");
        } catch (JSException jse) {
        }
        if (text == null) {
            text = "";
        }
        return text;
    }

    // method that returns the device name 
    public String deviceName() {
        if (VENDOR_ID == 1334) {
            return "4600";
        } else if (VENDOR_ID == 3118) {
            return "1900";
        } else {
            return "";
        }
    }
}
