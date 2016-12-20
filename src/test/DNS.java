/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import dns.DNSLookup;
import dns.DNSPacket;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 *
 * @author thehaohcm
 */
public class DNS {

    /**
     * @param args the command line arguments
     */    
    public static void main(String[] args) throws SQLException {
        // TODO code application logic here
        DatagramSocket server;
        String DNSServer="8.8.4.4";//208.67.220.220";
        byte[] send,receive;
        DatagramPacket sender,receiver;
        
        try {
            //khoi tao cac bien
            final InetAddress ipDNS=InetAddress.getByName(DNSServer);
            InetAddress ipDNS_t=InetAddress.getByName(DNSServer);
            ArrayList ip_domain=null;
            
            //Tao server, mo port 53
            server=new DatagramSocket(53);
            
            //lay dia chi gateway cua may
            String gateway=DNSLookup.getDefaultGateWay();
            System.out.println("Default GateWay: "+gateway);
            
            //send=new byte[1024];
            //nhan goi DNS tu client
            receive=new byte[110];
            receiver=new DatagramPacket(receive,receive.length);
            server.receive(receiver);
            
//            while(true){
                System.out.println("****************************************************");
                
                DNSPacket packet=new DNSPacket(receive);
                System.out.println("Domain: "+packet.domain);
                System.out.print("Noi dung goi DNS: ");
                System.out.println(packet.toString());
                packet.getStringArr();
                int transactionID=packet.transactionID;
                
                InetAddress ip=receiver.getAddress();
                int port=receiver.getPort();
                System.out.println("Nhan goi DNS tu IP: "+ip.toString()+" port: "+port);
                
                ip_domain=new ArrayList<String>();
                
                if(packet.domain.contains("in-addr.arpa")) //dns reverse
                {
                    System.out.println("Noi dung goi DNS Reverse");
                    DNSPacket packet1=new DNSPacket(receive);
                     System.out.println("Domain: "+packet1.domain);
                    System.out.print("Noi dung goi DNS: ");
                    System.out.println(packet1.toString());
                    packet1.getStringArr();
                    
//                    send=null;
                    //System.out.println("Da den day");
                    //System.out.println("Address: "+InetAddress.getLocalHost().getHostName());
//                    send=DNSPacket.getPacketDNSReverse(packet.domain,"hao.deptrai.bigkhoai");
//                    System.out.println("Da lam den day");
//                    sender=new DatagramPacket(send,send.length,ip,port);
//                    System.out.println("kfae");
//                    //sender.setData(send); //chu y, co the la nguyen nhan cua cac loi xay ra;
//                    server.send(sender);
//                    System.out.println("Gui goi DNS Reverse ve cho Client");
                }
//                else 
//                if((ip_domain=DNSLookup.getIP(packet.domain)).size()>0){
//                    //gui goi tin DNS ve client neu tim thay trong csdl
//                    System.out.println("Tim thay "+ip_domain.size()+" goi tin trong csdl");
//                    send=new byte[110];
//                    send=DNSPacket.getPacketDNSResponse(transactionID,packet.domain,ip_domain); //ip_domain
//                    DNSPacket dnspacket=new DNSPacket(send); //co loi
//                    System.out.println("Noi dung goi DNS gui cho client");
//                    System.out.println(dnspacket.toString());
//                    dnspacket.getStringArr();
//                    
//                    for(int i=0;i<5;i++){
//                        sender=new DatagramPacket(send, send.length,ip,port);
//                        //sender.setData(send); //chu y, co the la nguyen nhan cua cac loi xay ra;
//                        server.send(sender);
//                    }
//                    
//                    //nhan goi tin
//                    receive=new byte[1024];
//                    receiver=new DatagramPacket(receive,receive.length);
//                    server.receive(receiver);
//                    //ipDNS_t=receiver.getAddress();
//                }
//                else{
//                    //gui den server google neu khong tim thay trong csdl
//                    send=new byte[110];
//                    send=receiver.getData();
//                    sender=new DatagramPacket(send, send.length,InetAddress.getByName(DNSServer),53);
//                    //sender.setData(send);
//                    server.send(sender);
//                    System.out.println("Da gui tin den Server DNS: "+DNSServer);
//
//                    ipDNS_t=ipDNS;
//                    while(ipDNS_t.equals(ipDNS)){ //neu DNS tiep tuc gui ve, ta tiep tuc gui ve Client
//
//                        System.out.println("********************************************");
//                        //nhan tu server google
//                        receive=new byte[110];
//                        receiver=new DatagramPacket(receive,receive.length);
//                        server.receive(receiver);
//                        ipDNS_t=receiver.getAddress();
//                        if(!ipDNS_t.equals(ipDNS))
//                            break;
//                        System.out.println("Da nhan tin tu Server DNS: "+DNSServer);
//                        //phan tich goi tin tu server DNS gui ve
//                        DNSPacket dnsPacket=new DNSPacket(receive);
//                        try{
//                            System.out.println("Noi dung goi tu Server: "+dnsPacket.toString());
//                            System.out.println("\n");
//                            dnsPacket.getStringArr();
//                        }catch(Exception ex){
//                            System.out.println("Khong the doc noi dung cua goi DNS");
//                        }
//                        
//                        //gui ve client
//                        send=new byte[110];
//                        send=receiver.getData();
//                        sender=new DatagramPacket(send,send.length,ip, port);
//                        sender.setData(send);
//                        server.send(sender);
//                        System.out.println("Da gui ve Client: "+ip+" - port: "+port);
//
//                        //tiep tuc nhan goi tin (co the cua Client hoac cua DNS
//                        receive=new byte[110];
//                        receiver=new DatagramPacket(receive,receive.length);
//                        server.receive(receiver);
//                        ipDNS_t=receiver.getAddress();
//                    }
//                }
                System.out.println("*******************************************************");
            //}
        } catch (Exception e) {
            System.out.println("Da co loi xay ra");
        }
    }
    
}
