/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CheckAlive;

import AutoOff.AutoOffInterface;
import dns.DNSLookup;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 *
 * @author thehaohcm
 */
public class BroadCastForCheckAliveThread implements Runnable,AutoOffInterface{
    
    CheckAliveThread mainThread;
    int port=1995;
    String ipBroadCast=DNSLookup.getMyIPBroadCast();
    DatagramSocket server;
    int numOfPacket=6;
    
    public BroadCastForCheckAliveThread(CheckAliveThread mainThread){
        this.mainThread=mainThread;
        mainThread.refreshArrIP();
    }

    @Override
    public void run() {
        byte[] send=new byte[560];
        byte[] receive=new byte[560];
        
        DatagramPacket sender;
        DatagramPacket receiver;
        try{
            server=new DatagramSocket();
            String content="getName";
            send=content.getBytes();
            sender=new DatagramPacket(send, send.length,InetAddress.getByName(ipBroadCast),port);
            System.out.println("*************************************Giá trị Ip BroadCast: "+ipBroadCast);
            for(int i = 0; i < numOfPacket; i++){
                server.send(sender);
            }
            while(true){
                receiver=new DatagramPacket(receive, receive.length);
                server.receive(receiver);
                if(!receiver.getAddress().equals(DNSLookup.getMyIPAddress())){
                    receive=receiver.getData();
                    String name=new String(receive,0,receive.length);
                    String ip=receiver.getAddress().getHostAddress();
                    System.out.println("Ten may tinh: "+name+" - ip: "+ip);

                    mainThread.addIPtoArr(ip);
                }
            }
        }catch(Exception ex){
            System.out.println("Da co loi xay ra, khong the gui BroadCast kiem tra su ton tai cua cac server");
            closeConnect();
        }
     
    }

    @Override
    public void closeConnect() {
        if(server!=null){
            server.close();
            server=null;
        }
    }
    
}
