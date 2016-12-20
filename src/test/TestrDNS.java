/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import dns.DNSLookup;
import dns.DNSPacket;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

/**
 *
 * @author thehaohcm
 */
public class TestrDNS {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
         DatagramSocket server;
        String DNSServer="8.8.4.4";//208.67.220.220";
        byte[] send,receive;
        DatagramPacket sender,receiver;
        
        try {
            
            //Tao server, mo port 53
            server=new DatagramSocket(53);
            
            receive=new byte[110];
            receiver=new DatagramPacket(receive,receive.length);
            server.receive(receiver);
            DNSPacket packet;
            int transactionID;

            packet = new DNSPacket(receive);
            System.out.println("Domain: " + packet.domain);
            System.out.print("Noi dung goi DNS: ");
            System.out.println(packet.toString());
            packet.getStringArr();
            transactionID = packet.transactionID;

            send = new byte[110];
            send = receive;
            DNSPacket dnspacket = new DNSPacket(send); //co loi
            System.out.println("Noi dung goi DNS gui cho client");
            System.out.println(dnspacket.toString());
            packet.getStringArr();
            
//            while(true){
//                System.out.println("****************************************************");
//                
//                DNSPacket packet=new DNSPacket(receive);
//                System.out.println("Domain: "+packet.domain);
//                System.out.print("Noi dung goi DNS: ");
//                System.out.println(packet.toString());
//                packet.getStringArr();
//                int transactionID=packet.transactionID;
//                
//                InetAddress ip=receiver.getAddress();
//                int port=receiver.getPort();
//                System.out.println("Nhan goi DNS tu IP: "+ip.toString()+" port: "+port);
//                
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

                    
                    //ipDNS_t=ipDNS;
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
                //}
                //System.out.println("*******************************************************");
            //}
        } catch (Exception e) {
            System.out.println("Da co loi xay ra");
        }
    }
    
}
