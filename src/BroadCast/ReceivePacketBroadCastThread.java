/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BroadCast;

import Enum.DNSEnum;
import GUI.DNSApplication;
import RMI.XuLyTuXa;
import dns.DNSLookup;
import dns.DNSThread;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author thehaohcm
 */
public class ReceivePacketBroadCastThread implements Runnable {

    DatagramSocket server;
    DNSApplication app;
    DNSThread mainThread;

    public ReceivePacketBroadCastThread(DNSApplication app, DNSThread mainThread) {
        this.app = app;
        this.mainThread = mainThread;
    }

    @Override
    public void run() {
        byte[] send = new byte[560];
        byte[] receive = new byte[560];

        DatagramPacket sender;
        DatagramPacket receiver;
        InetAddress ip;
        int port;
        int numOfPacket = 6;
        while (true) {
            try {
                server = new DatagramSocket(1995);
                System.out.println("Da mo port 1995");

                receiver = new DatagramPacket(receive, receive.length);
                server.receive(receiver);
                ip = receiver.getAddress();
                port = receiver.getPort();
                receive = receiver.getData();
                //String content = new String(receive, 0, receive.length);

                String content = InetAddress.getLocalHost().getHostName();
                send = content.getBytes();
                sender = new DatagramPacket(send, send.length, ip, port);
                for (int i = 0; i < numOfPacket; i++) {
                    server.send(sender);
                }
                app.getInfo(ip, DNSLookup.getMyIPAddress(), DNSEnum.SlaveServerReceiveBroadCast);
                app.getInfo(DNSLookup.getMyIPAddress(), ip, DNSEnum.SendNameToRootServer);
                //System.out.println("Da gui ten may cho may chu thanh cong. " + content);

            } catch (Exception ex) {
                System.out.println("Da co loi xay ra, khong the gui ten cua may cho may chu");
                closeConnect();
                System.out.println("thread da tat");
            }
        }
//        System.out.println("Da tat thread");
    }

    public void closeConnect() {
        if (server != null) {
            server.close();
            server = null;
        }
    }

}
