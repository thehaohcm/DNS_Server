/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RMI;
import GUI.DNSApplication;
import dns.DNSThread;
import dns.miniDNSThread;
import java.io.Serializable;
import java.net.DatagramSocket;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author DELL
 */
public class XuLyTuXa extends UnicastRemoteObject implements DNSRemoteInterface,Serializable{
    DNSApplication app;
    DNSThread mainThread;
    DatagramSocket server;
    
    public XuLyTuXa(DNSApplication app,DNSThread mainThread) throws RemoteException{
        super();
        this.app=app;
        this.mainThread=mainThread;
    }
    
    public XuLyTuXa() throws RemoteException{
        super();
    }
    
    @Override
    public void xuLy(DNSRMIPacket packet) throws RemoteException {
        System.out.println("Da nhan tu root server.*********************************");
        System.out.println("Noi dung goi packet: ");
        System.out.println("dia chi IP: "+packet.address.toString());
        System.out.println("Cong: "+packet.port);
        Thread thread=new Thread(new miniRMIDNSThread(app, mainThread, packet.DNSPacket, packet.address, packet.port));
        thread.start();
    }

    @Override
    public void inSo(int a) throws RemoteException {
        System.out.println("********************************gia tri: "+a+"*********************");
    }
    
}
