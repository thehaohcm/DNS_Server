/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import BroadCast.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 *
 * @author thehaohcm
 */
public class ReceivePakcetBroadCastThread{

    public static void main(String[] arg) {
        byte[] send=new byte[560];
        byte[] receive=new byte[560];
        DatagramSocket server;
        DatagramPacket sender;
        DatagramPacket receiver;
        InetAddress ip;
        int port;
        try{
            server=new DatagramSocket(1995); 
            while(true){  
                receiver=new DatagramPacket(receive, receive.length);
                server.receive(receiver);
                ip=receiver.getAddress();
                port=receiver.getPort();
                receive=receiver.getData();
                String content=new String(receive,0,receive.length);


                content=InetAddress.getLocalHost().getHostName();
                send=content.getBytes();
                sender=new DatagramPacket(send, send.length,ip,port);
                server.send(sender);
                System.out.println("Da gui ten may cho may chu thanh cong. "+content);
            }
        }catch(Exception ex){
            System.out.println("Da co loi xay ra, khong the gui ten cua may cho may chu");
        }
        
    }
    
}
