/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 *
 * @author thehaohcm
 */
public class PacketUDP {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        DatagramSocket server;
        DatagramPacket sender,receiver;
        byte[] receive,send;
        String content="abc";
        int port=1995;
        try{
            server=new DatagramSocket(1995);
            send=new byte[560];
            send=content.getBytes();
            sender=new DatagramPacket(send, send.length, InetAddress.getByName("192.168.40.210"), port);
            server.send(sender);
            
            System.out.println("Gia tri: "+sender.getAddress().toString());
            
            receive=new byte[560];
            receiver=new DatagramPacket(receive, receive.length);
            
            server.receive(receiver);
            System.out.println("ip: "+receiver.getAddress());
            System.out.println("port: "+receiver.getPort());
            
        }catch(Exception ex){
            
        }
    }
    
}
