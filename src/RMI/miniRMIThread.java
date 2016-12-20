/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RMI;

import Enum.DNSEnum;
import GUI.DNSApplication;
import dns.DNSLookup;
import dns.DNSPacket;
import dns.DNSThread;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author DELL
 */
//Hoạt động trên Root Server, có trách nhiệm gửi RMI (DNSRMIPacket) về đúng SlaveServer dựa trên suffix domain đã cài đặt trước
public class miniRMIThread implements Runnable {

    final int sizeByte = 560;
    DNSThread mainThread;
    byte[] receive;
    DNSApplication app;
    InetAddress ip;
    int port;
    String DNSServer;
    String DNSName;
    DatagramSocket mainSocket;
    String DNSServer_Com = "", DNSServer_Net = "", DNSServer_Org = "";
    String[][] arrIP;
    DatagramSocket server;

    public miniRMIThread(DNSApplication app, DNSThread mainThread, DatagramSocket socket, byte[] receive, InetAddress ip, int port, String[][] arrIP) {
        this.app = app;
        this.mainThread = mainThread;
        this.receive = new byte[sizeByte];
        this.receive = receive;
        this.ip = ip;
        this.port = port;
        this.DNSServer = mainThread.getDNSServer();
        this.DNSName = mainThread.getDNSName();
        this.mainSocket = socket;
        this.arrIP = arrIP;
    }

    @Override
    public void run() {
        byte[] send;
        DatagramPacket sender, receiver;
        DNSPacket packet;
        InetAddress ipDNS, ipDNS_t;

        try {
            String ipSlave = "";
            //chú ý
            ipDNS = InetAddress.getByName(DNSServer);
            ipDNS_t = InetAddress.getByName(DNSServer);

            send = new byte[sizeByte];
            server = new DatagramSocket();
            receiver = new DatagramPacket(receive, receive.length);
            ArrayList ip_domain = new ArrayList<String>();
            packet = new DNSPacket(receive);
//            packet.getStringArr();
            int transactionID = packet.transactionID;
            if (packet.isReverse && packet.domain.contains(DNSLookup.getMyIPDNSReverse()))//Neu la goi DNS Reverse//packet.domain.contains("in-addr.arpa"))
            {
                send = new byte[sizeByte];//110];
                try {
                    send = DNSPacket.getPacketDNSReverse(packet.transactionID, packet.domain, DNSName);
//                         DNSPacket dnspacket=new DNSPacket(send);
//                         System.out.println("Noi dung goi DNS Reverse gui ve client, port: "+port);
//                         System.out.println(dnspacket.toString());
//                         dnspacket.getStringArr();
                    for (int i = 0; i < 2; i++) {
                        sender = new DatagramPacket(send, send.length, ip, port);
                        mainThread.sendPacket(sender);
                    }
                    app.getInfo(DNSLookup.getMyIPAddress(), ip, DNSEnum.RootServerToSlaveServer);

                    //chua cap nhat thong tin tren GUI
                } catch (Exception ex) {
//                    System.out.println("Da co loi xay ra khi doc DNS Reverse");
                }
            } else {
                String domain = packet.domain;
                String suffixDomain = domain.substring(domain.lastIndexOf("."));

                boolean flag = false;
                //String ipForOtherDomain="";

                for (int i = 0; i < arrIP.length; i++) { //Tìm hậu tố domain và gửi về
                    if (arrIP[i][1].equals(suffixDomain)) {
//                        send = new byte[sizeByte];//110];
//                        send = receiver.getData();
//                        sender = new DatagramPacket(send, send.length, InetAddress.getByName(arrIP[i][0]), 53);
//                        ipDNS = InetAddress.getByName(arrIP[i][0]);
//                        ipDNS_t = InetAddress.getByName(arrIP[i][0]);
//                        //sender.setData(send);
//                        server.send(sender);
//                        flag = true;
//                        break;
                        try
                        {
                            ipSlave=arrIP[i][0];
                            Remote lookup = Naming.lookup("rmi://" + ipSlave + ":1099/guiRMIDNS");
                            DNSRemoteInterface myremote = (DNSRemoteInterface) lookup;
                            myremote.inSo(10);
                            DNSRMIPacket temp = new DNSRMIPacket(ip, port,receive, domain);
                            myremote.xuLy(temp);
                        } catch (NotBoundException ex) {
                            Logger.getLogger(ClientRMI.class.getName()).log(Level.SEVERE, null, ex + "******...1");
                        } catch (MalformedURLException ex) {
                            Logger.getLogger(ClientRMI.class.getName()).log(Level.SEVERE, null, ex + "******...2");
                        } catch (RemoteException ex) {
                            Logger.getLogger(ClientRMI.class.getName()).log(Level.SEVERE, null, ex + "*****...3");
                        }
                        System.out.println("tong: ");
                    }
                }

            }
        } catch (Exception ex) {
            System.out.println("da co loi");
        }
    }
}
