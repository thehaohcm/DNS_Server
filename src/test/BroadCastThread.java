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
public class BroadCastThread{
    
    public static void main(String[] arg) {
        String ipBroadCast="192.168.1.255";
        int port=1995;
        byte[] send=new byte[560];
        byte[] receive=new byte[560];
        DatagramSocket server;
        DatagramPacket sender;
        DatagramPacket receiver;
        try{
            server=new DatagramSocket();
            String content="getName";
            send=content.getBytes();
            sender=new DatagramPacket(send, send.length,InetAddress.getByName(ipBroadCast),port);
            server.send(sender);
            
            while(true){
                receiver=new DatagramPacket(receive, receive.length);
                server.receive(receiver);
                receive=receiver.getData();
                String name=new String(receive,0,receive.length);
                String ip=receiver.getAddress().getHostAddress();
                System.out.println("Ten may tinh: "+name+" - ip: "+ip);
                
                
            }
        }catch(Exception ex){
            System.out.println("Da co loi xay ra, khong the gui BroadCast");
        }
    }
    
}
