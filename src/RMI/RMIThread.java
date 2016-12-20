/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RMI;

import GUI.DNSApplication;
import dns.DNSThread;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author thehaohcm
 */
//Thread được khởi tạo và chạy RMI khi phần mềm được khởi động
//Thread thực thi trên Slave Server
//Thread sẽ chờ Root Server gửi gói RMI để tạo một miniRMIThread 
public class RMIThread implements Runnable {

    DNSThread mainThread;
    DNSApplication app;

    public RMIThread(DNSApplication app, DNSThread mainThread) {
        this.app = app;
        this.mainThread = mainThread;
    }
    public RMIThread() {
    }
    @Override
    public void run() {
        //sau khi gui ten may cho Server root, he thong tu dong bat RMI
        try {
            XuLyTuXa xuLyRMI = new XuLyTuXa(app, mainThread);
            //DNSRemoteServer obj = new DNSRemoteServer();
            LocateRegistry.createRegistry(1099);
            Naming.rebind("rmi://localhost/guiRMIDNS", xuLyRMI);
            System.out.println("Dang cho client goi ham RMI DNS Packet...");
            //mainThread.enabledFlagRMIforSlaveServer();
        } catch (RemoteException ex) {
            
        } catch (MalformedURLException ex) {
            Logger.getLogger(RMIThread.class.getName()).log(Level.SEVERE, null, ex + ".......");
        }
         
        catch (Exception ex) {
            System.out.println("Da co loi xay ra khi mo RMI tren Slave Server");
        }
    }

}
