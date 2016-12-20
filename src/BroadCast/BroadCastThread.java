/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BroadCast;

import GUI.ChangeDNS;
import AutoOff.AutoOffInterface;
import Enum.DNSEnum;
import GUI.DNSApplication;
import dns.DNSLookup;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 *
 * @author thehaohcm
 */
public class BroadCastThread implements Runnable,AutoOffInterface{
    private ChangeDNS app;
    private String ipBroadCast;
    private int port=1995;
    DatagramSocket server;
    
    int timeForCheckingAlive=60000;
    int numOfPacket=6;
    DNSApplication mainApp;
    
    public BroadCastThread(DNSApplication mainApp,String ipBroadCast,ChangeDNS app){
        this.mainApp=mainApp;
        this.ipBroadCast=ipBroadCast;
        this.app=app;
        System.out.println("BroadCast: "+ipBroadCast);
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
            for(int i = 0; i < numOfPacket; i++){
                sender=new DatagramPacket(send, send.length,InetAddress.getByName(ipBroadCast),port);
                server.send(sender);
            }
            while(true){
                receiver=new DatagramPacket(receive, receive.length);
                server.receive(receiver);
                if(!receiver.getAddress().equals(DNSLookup.getMyIPAddress())){
                    receive=receiver.getData();
                    String name=new String(receive,0,receive.length);
                    String ip=receiver.getAddress().getHostAddress();
                //if(ip.equals(DNSLookup.getMyIPAddress().toString().substring(1))){
                    System.out.println("Ten may tinh: "+name+" - ip: "+ip);

                    app.getNameIPofComputer(ip,name);
                    mainApp.getInfo(InetAddress.getByName(ip), DNSLookup.getMyIPAddress(), DNSEnum.ReceiveNameFromSlaveServer);
                //}
                }
            }
        }catch(Exception ex){
            System.out.println("Da co loi xay ra, khong the gui BroadCast");
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
