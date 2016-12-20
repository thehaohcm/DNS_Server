/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import dns.DNSPacket;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 *
 * @author thehaohcm
 */
public class mainDNS {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic herr
        DatagramSocket socket;
        byte send[],receive[];
        DatagramPacket sender,receiver = null;
        
        try{
            socket=new DatagramSocket();
            String a="[B@2cdca13c";
            send=a.getBytes();
            sender=new DatagramPacket(send, send.length, InetAddress.getByName("192.168.169.3"), 53);
            socket.send(sender);
            
            socket.receive(receiver);
            receive=receiver.getData();
            DNSPacket dp=new DNSPacket(receive);
            System.out.println("noi dung goi DNS Response: "+dp.toString());
            
        }catch(Exception e){
            
        }
        
    }
    
}
