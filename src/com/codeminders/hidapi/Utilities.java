/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeminders.hidapi;

import com.codeminders.hidapi.*;
import java.io.IOException;
import netscape.javascript.JSObject;

/**
 *
 * @author Hunter
 */
public class Utilities {

    private static final long READ_UPDATE_DELAY_MS = 50L;
    private static JSObject window;
    private static HIDManager manager;

    static {

        System.load("C:\\Users\\Hunter\\Documents\\NetBeansProjects\\HelloApplet\\jni\\hidapi-jni.dll");

    }
    // initialize ID vars
    static int VENDOR_ID;
    static int PRODUCT_ID;
    private static final int BUFSIZE = 2048;
    private static final int ORDER = 2;

    public static void main(String[] args) {
//        listDevices();
//        readDevice();
    }

    public void setWindow(JSObject win) {
        window = win;

        window.eval("console.log('window set');");
    }

    public String readDevice() {
        HIDDevice dev;
        String line = "";


        System.out.println("in read device");


        try {
//            this.testMethod("before list device");
            listDevices();
//            this.testMethod("after list device");
//            HIDManager hid_mgr = HIDManager.getInstance();
//            System.out.println(hid_mgr);
            System.out.println("vendor id: " + VENDOR_ID);
            System.out.println("product id: " + PRODUCT_ID);

            dev = manager.openById(VENDOR_ID, PRODUCT_ID, null);
            System.out.println(dev);
            System.err.print("Manufacturer: " + dev.getManufacturerString() + "\n");
            System.err.print("Product: " + dev.getProductString() + "\n");
            System.err.print("Serial Number: " + dev.getSerialNumberString() + "\n");
            try {
                byte[] buf = new byte[BUFSIZE];
                dev.enableBlocking();
                int count = 0;
                while (true) {
                    count++;
                    int n = dev.read(buf);

                    for (int i = 0; i < n; i++) {
                        int v = buf[i];
                        if (v < 0) {
                            v = v + 256;
                        }
                        String hs = Integer.toHexString(v);
                        if (v < 16) {
                            System.err.print("0");
                        }
                        System.out.print(hs + " ");
                        line += hs + " ";
                    }

                    if (window != null) {
                        window.eval("console.log('" + line + "');");
                    }

                    // JSObject win = JSObject.getWindow(this);
                    //win.call(functionName, new Object[] { argument });

                    if (window != null) {
                        window.eval("console.log('testing1');");
                    }
                    try {
                        if (window != null) {
                            window.eval("console.log('testing sleep');");
                        }
                        Thread.sleep(READ_UPDATE_DELAY_MS);
                    } catch (InterruptedException e) {
                        //Ignore
                        e.printStackTrace();
                    }
                }
            } finally {
                dev.close();
                manager.release();
                System.gc();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(line);

        return line;
    }

    /**
     * Static function to find the list of all the HID devices attached to the
     * system.
     */
    private static void listDevices() {
        try {
            String property = System.getProperty("java.library.path");
            System.err.println(property);

            manager = HIDManager.getInstance();
            HIDDeviceInfo[] devs = manager.listDevices();
            System.err.println("Devices:\n\n");
            for (int i = 0; i < Math.min(devs.length, ORDER); i++) {
                System.err.println("" + i + ".\t" + devs[i]);

                String info = devs[i].toString();

                // get vendor id
                VENDOR_ID = getId("vendor_id=", info);

                // get product id
                PRODUCT_ID = getId("product_id=", info);

                //System.out.println(VENDOR_ID);
                //System.out.println(PRODUCT_ID);

                System.err.println("---------------------------------------------\n");
            }
            System.gc();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    // method that's read the info coming in from the HID and stores it's ID's
    private static int getId(String id, String info) {

        int index = info.indexOf(id) + id.length();
        int n = info.charAt(index);

        String ID = "";
        boolean stop = false;

        while (!stop) {

            try {
                n = Integer.parseInt("" + info.charAt(index));
            } catch (Exception e) {
                stop = true;
            }

            if (!stop) {
                ID += n;
            }
            index++;
        }
        return Integer.parseInt(ID);
    }
}
