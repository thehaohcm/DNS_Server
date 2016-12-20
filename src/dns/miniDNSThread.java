/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dns;

import Enum.DNSEnum;
import GUI.DNSApplication;
import AutoOff.AutoOff;
import AutoOff.AutoOffInterface;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

/**
 *
 * @author thehaohcm
 */

//Su dung trong Phan Tan DNS Server - Slave
public class miniDNSThread implements Runnable,AutoOffInterface {

    final int sizeByte = 560;
    DNSThread mainThread;
    byte[] receive;
    DNSApplication app;
    InetAddress ip;
    int port;
    String DNSServer;
    String DNSName;
    DatagramSocket mainSocket;
    String DNSServer_Com="",DNSServer_Net="",DNSServer_Org="";
    String[][] arrIP;
    DatagramSocket server;
    
    public miniDNSThread(DNSApplication app, DNSThread mainThread,DatagramSocket socket, byte[] receive, InetAddress ip, int port,String[][] arrIP) {
        this.app = app;
        this.mainThread = mainThread;
        this.receive = new byte[sizeByte];
        this.receive = receive;
        this.ip = ip;
        this.port = port;
        this.DNSServer=mainThread.getDNSServer();
        this.DNSName=mainThread.getDNSName();
        this.mainSocket=socket;
        this.arrIP=arrIP;
    }

    @Override
    public void run() {
        
        byte[] send;
        DatagramPacket sender,receiver;
        DNSPacket packet;
        InetAddress ipDNS,ipDNS_t;
        //DNSPacket dnsPacket;
        try {
            //chú ý
            ipDNS=InetAddress.getByName(DNSServer);
            ipDNS_t=InetAddress.getByName(DNSServer);
            
            send=new byte[sizeByte];
            server=new DatagramSocket();
            receiver=new DatagramPacket(receive, receive.length);
            ArrayList ip_domain = new ArrayList<String>();
            packet=new DNSPacket(receive);
//            packet.getStringArr();
            int transactionID=packet.transactionID;
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
                        app.getInfo(DNSLookup.getMyIPAddress(),ip, DNSEnum.ServerToClient);
                    }

                    //chua cap nhat thong tin tren GUI
                } catch (Exception ex) {
//                        System.out.println("Da co loi xay ra khi doc DNS Reverse");
                }

                //nhan goi tin
    //                receive = new byte[sizeByte];
    //                receiver = new DatagramPacket(receive, receive.length);
    //                server.receive(receiver);
//                app.getInfo(receiver.getAddress(), DNSEnum.ClientToServer);
                //ipDNS_t=receiver.getAddress();
            } else {
//                if ((ip_domain = DNSLookup.getIP(packet.domain)).size() > 0) {
//                    //gui goi tin DNS ve client neu tim thay trong csdl
////                    System.out.println("Tim thay "+ip_domain.size()+" trong csdl");
//                    send = new byte[sizeByte];//110];
////                    try{
////                        System.out.println("Noi dung goi DNS gui ve client, port: "+port);
//                    send = DNSPacket.getPacketDNSResponse(transactionID, packet.authority, packet.domain, ip_domain); //ip_domain
////                        DNSPacket dnspacket=new DNSPacket(send); //co loi
////                        System.out.println("Noi dung goi DNS gui cho client");
////                        System.out.println(dnspacket.toString());
////                        dnspacket.getStringArr();
////                    }catch(Exception ex){
////                        System.out.println("Da co loi xay ra1");
////                    }
//
//                    for (int i = 0; i < 2; i++) {
//                        sender = new DatagramPacket(send, send.length, ip, port);
//                        //sender.setData(send); //chu y, co the la nguyen nhan cua cac loi xay ra;
//                        mainThread.sendPacket(sender);
//                        app.getInfo(DNSLookup.getMyIPAddress(),ip, DNSEnum.ServerToClient); //Gui ve Client
//                    }
//
//                    //nhan goi tin
//                    receive = new byte[sizeByte];
//                    receiver = new DatagramPacket(receive, receive.length);
//                    server.receive(receiver);
////                    System.out.println("Da nhan goi tin tu"+receiver.getAddress().toString()+" - port: "+receiver.getPort());
////                    app.getInfo(receiver.getAddress(), DNSEnum.ClientToServer);
//                    //dnsPacket = new DNSPacket(receive);
//                    //ipDNS_t=receiver.getAddress();
//                } else {
//                    //gui den server google neu khong tim thay trong csdl
//                    send = new byte[sizeByte];//110];
//                    send = receiver.getData();
//                    sender = new DatagramPacket(send, send.length, InetAddress.getByName(DNSServer), 53);
//                    //sender.setData(send);
//                    server.send(sender);
////                    System.out.println("Da gui tin den Server DNS: "+DNSServer);
//                    app.getInfo(DNSLookup.getMyIPAddress(),InetAddress.getByName(DNSServer), DNSEnum.ClientToServer);
//
//                    ipDNS_t = ipDNS;
//                    while (ipDNS_t.equals(ipDNS)) { //neu DNS tiep tuc gui ve, ta tiep tuc gui ve Client
//
////                        System.out.println("********************************************");
//                        //nhan tu server google
//                        receive = new byte[sizeByte];//110];
//                        receiver = new DatagramPacket(receive, receive.length);
//                        server.receive(receiver);
//                        app.getInfo(receiver.getAddress(),DNSLookup.getMyIPAddress(), DNSEnum.ServerToClient);
//                        ipDNS_t = receiver.getAddress();
//                        if (!ipDNS_t.equals(ipDNS)) {
//                            break;
//                        }
////                        System.out.println("Da nhan tin tu Server DNS: "+DNSServer);
//                        //phan tich goi tin tu server DNS gui ve
//
//                        
//
////                            try{
//                        //dnsPacket = new DNSPacket(receive);
////                                System.out.println("Noi dung nhan tu Server: "+dnsPacket.toString());
////                                dnsPacket.getStringArr();
////                                System.out.println("\n");
//
////                            }catch(Exception ex){
////                                System.out.println("Khong the doc noi dung cua goi DNS");
////                            }
////                            try{
////                                System.out.println("Noi dung nhan tu Server: "+dnsPacket.toString());
////                                dnsPacket.getStringArr();
////                                System.out.println("\n");
////                            }catch(Exception ex){
////                                System.out.println("Khong the doc noi dung cua goi DNS");
////                            }
//                        //gui ve client
//                        send = new byte[sizeByte];//110];
//                        send = receiver.getData();
//                        sender = new DatagramPacket(send, send.length, ip, port);
//                        sender.setData(send);
//                        mainThread.sendPacket(sender);
//                        app.getInfo(DNSLookup.getMyIPAddress(),ip, DNSEnum.ServerToClient);
////                        System.out.println("Da gui ve Client: "+ip+" - port: "+port);
//
//                        //tiep tuc nhan goi tin (co the cua Client hoac cua DNS)
//                        receive = new byte[sizeByte];//110];
//                        receiver = new DatagramPacket(receive, receive.length);
//                        server.receive(receiver);
//                        ipDNS_t = receiver.getAddress();
//
//                        //dnsPacket = new DNSPacket(receive);
////                        System.out.println("Noi dung nhan tu Server: " + dnsPacket.toString());
////                        dnsPacket.getStringArr();
////                        System.out.println("\n");
//                    }
//                }
    
                String domain=packet.domain;
                String suffixDomain=domain.substring(domain.lastIndexOf("."));
 
                boolean flag=false;
                //String ipForOtherDomain="";
                
                for(int i=0;i<arrIP.length;i++){ //Tìm hậu tố domain và gửi về
                    if(arrIP[i][1].equals(suffixDomain))
                    {
                        send = new byte[sizeByte];//110];
                        send = receiver.getData();
                        sender = new DatagramPacket(send, send.length, InetAddress.getByName(arrIP[i][0]), 53);
                        ipDNS=InetAddress.getByName(arrIP[i][0]);
                        ipDNS_t=InetAddress.getByName(arrIP[i][0]);
                        //sender.setData(send);
                        server.send(sender);
                        flag=true;
                        break;
                    }
//                    else if(arrIP[i][1].equals("other"))
//                        ipForOtherDomain=arrIP[i][0];
                }
                
//                if(flag==false&&!ipForOtherDomain.equals("")) //tra ve Other nếu khác đuôi 
//                {
//                    send = new byte[sizeByte];//110];
//                    send = receiver.getData();
//                    sender = new DatagramPacket(send, send.length, InetAddress.getByName(ipForOtherDomain), 53);
//                    ipDNS=InetAddress.getByName(ipForOtherDomain);
//                    ipDNS_t=InetAddress.getByName(ipForOtherDomain);
//                    //sender.setData(send);
//                    server.send(sender);
//                }



//                if(flag==false){
//                    //Khong tim thay Suffix domain phu hop trong mang
//                    //Tao thread miniThread1 gui ve 
//                    miniDNSThread1 miniThreadToServer=new miniDNSThread1(app, mainThread, mainSocket, receive, ip, port);
//                    Thread miniThread=new Thread(miniThreadToServer);
//                    
//                    Thread autoOffThread=new Thread(new AutoOff(miniThread,miniThreadToServer, 1000));
//                    miniThread.start();
//                    autoOffThread.start();
//                }
                
                
                //trường hợp cũ - kiểm tra từng ip trùng với domain thì gửi DNS cho ip đó
//                if(domain.endsWith(".com")){
//                     send = new byte[sizeByte];//110];
//                     send = receiver.getData();
//                     sender = new DatagramPacket(send, send.length, InetAddress.getByName(DNSServer_Com), 53);
//                     //sender.setData(send);
//                     server.send(sender);
//                }
//                else if(domain.endsWith(".net")){
//                    send = new byte[sizeByte];//110];
//                     send = receiver.getData();
//                     sender = new DatagramPacket(send, send.length, InetAddress.getByName(DNSServer_Net), 53);
//                     //sender.setData(send);
//                     server.send(sender);
//                }else if(domain.endsWith(".org")){
//                    send = new byte[sizeByte];//110];
//                     send = receiver.getData();
//                     sender = new DatagramPacket(send, send.length, InetAddress.getByName(DNSServer_Org), 53);
//                     //sender.setData(send);
//                     server.send(sender);
//                }
                
                ipDNS_t = ipDNS;
                    while (ipDNS_t.equals(ipDNS)) { //neu DNS tiep tuc gui ve, ta tiep tuc gui ve Client

//                        System.out.println("********************************************");
                        //nhan tu server google
                        receive = new byte[sizeByte];//110];
                        receiver = new DatagramPacket(receive, receive.length);
                        server.receive(receiver);
                        app.getInfo(receiver.getAddress(),DNSLookup.getMyIPAddress(), DNSEnum.ServerToClient);
                        ipDNS_t = receiver.getAddress();
                        if (!ipDNS_t.equals(ipDNS)) {
                            break;
                        }
//                        System.out.println("Da nhan tin tu Server DNS: "+DNSServer);
                        //phan tich goi tin tu server DNS gui ve

                        

//                            try{
                        //dnsPacket = new DNSPacket(receive);
//                                System.out.println("Noi dung nhan tu Server: "+dnsPacket.toString());
//                                dnsPacket.getStringArr();
//                                System.out.println("\n");

//                            }catch(Exception ex){
//                                System.out.println("Khong the doc noi dung cua goi DNS");
//                            }
//                            try{
//                                System.out.println("Noi dung nhan tu Server: "+dnsPacket.toString());
//                                dnsPacket.getStringArr();
//                                System.out.println("\n");
//                            }catch(Exception ex){
//                                System.out.println("Khong the doc noi dung cua goi DNS");
//                            }
                        //gui ve client
                        send = new byte[sizeByte];//110];
                        send = receiver.getData();
                        sender = new DatagramPacket(send, send.length, ip, port);
                        sender.setData(send);
                        mainThread.sendPacket(sender);
                        app.getInfo(DNSLookup.getMyIPAddress(),ip, DNSEnum.ServerToClient);
//                        System.out.println("Da gui ve Client: "+ip+" - port: "+port);

                        //tiep tuc nhan goi tin (co the cua Client hoac cua DNS)
                        receive = new byte[sizeByte];//110];
                        receiver = new DatagramPacket(receive, receive.length);
                        server.receive(receiver);
                        ipDNS_t = receiver.getAddress();

                        //dnsPacket = new DNSPacket(receive);
//                        System.out.println("Noi dung nhan tu Server: " + dnsPacket.toString());
//                        dnsPacket.getStringArr();
//                        System.out.println("\n");
                    }
            }

        } catch (Exception ex) {
            //System.out.println("Da co loi xay ra. Chuong trinh khong the gui den ip " + ip.getHostAddress() + " port: " + port);
            closeConnect();
        }

    }

    @Override
    public void closeConnect() {
        if (server != null) {
            server.close();
            server = null;
        }
    }

}
