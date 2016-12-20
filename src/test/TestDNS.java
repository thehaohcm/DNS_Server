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

/**
 *
 * @author thehaohcm
 */
public class TestDNS {

    /**
     * @param args the command line arguments
     */
    static String DNSServer = "192.168.1.1";//"208.67.220.220";
    static String DNSName = "anhHao.deptrai.khoaito.loxobac";
    String ipDNSReverse = DNSLookup.getMyIPDNSReverse();

    public static void main(String[] args) {
        // TODO code application logic here
        int t_i = 0;
        DatagramSocket server;

        byte[] send, receive;
        DatagramPacket sender, receiver;

        try {
            //khoi tao cac bien
            final InetAddress ipDNS = InetAddress.getByName(DNSServer);
            InetAddress ipDNS_t = InetAddress.getByName(DNSServer);

            //Tao server, mo port 53
            server = new DatagramSocket(53);

            //lay dia chi gateway cua may
            //send=new byte[1024];
            //nhan goi DNS tu client
            while (true) {
                receive = new byte[110];
                receiver = new DatagramPacket(receive, receive.length);
                server.receive(receiver);
                DNSPacket packet = new DNSPacket(receive);
//                System.out.println("Domain: " + packet.domain);
//                System.out.print("Noi dung goi DNS: ");
//                System.out.println(packet.toString());
//                packet.getStringArr();
                int transactionID = packet.transactionID;

                InetAddress ip = receiver.getAddress();
                int port = receiver.getPort();

                send = new byte[110];
                send = receiver.getData();
                sender = new DatagramPacket(send, send.length, InetAddress.getByName(DNSServer), 53);
                //sender.setData(send);
                server.send(sender);
                //System.out.println("Da gui tin den Server DNS: " + DNSServer);

                ipDNS_t = ipDNS;
                while (ipDNS_t.equals(ipDNS)) { //neu DNS tiep tuc gui ve, ta tiep tuc gui ve Client

                    //System.out.println("********************************************");
                    //nhan tu server google
                    receive = new byte[110];
                    receiver = new DatagramPacket(receive, receive.length);
                    server.receive(receiver);
                    //GUI.DNSApplication.getInfo(receiver.getAddress(), -1);
                    ipDNS_t = receiver.getAddress();
                    if (!ipDNS_t.equals(ipDNS)) {
                        break;
                    }
                    //System.out.println("Da nhan tin tu Server DNS: " + DNSServer);
                    //phan tich goi tin tu server DNS gui ve

                    DNSPacket dnsPacket;
                    dnsPacket = new DNSPacket(receive);
//                    System.out.println("Noi dung nhan tu Server: " + dnsPacket.toString());
//                    dnsPacket.getStringArr();
                   // System.out.println("\n");
                    send = new byte[110];
                    send = receiver.getData();
                    sender = new DatagramPacket(send, send.length, ip, port);
                    sender.setData(send);
                    server.send(sender);
                    receive = new byte[110];
                    receiver = new DatagramPacket(receive, receive.length);
                    server.receive(receiver);
                }
            }
            }catch(Exception ex){
            
            }
    }

        }
