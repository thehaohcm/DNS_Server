/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dns;

import Enum.DNSEnum;
import CheckAlive.CheckAliveThread;
import GUI.DNSApplication;
import RMI.RMIThread;
import RMI.miniRMIDNSThread;
import RMI.miniRMIThread;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import javax.swing.JOptionPane;

/**
 *
 * @author thehaohcm
 */
public class DNSThread implements Runnable {

    final int sizeByte = 560;//548;//1111;
    String DNSServer = "8.8.8.8";//"208.67.222.220";
    String DNSName = "NguyenTheHao.TranManhCam.TranHoangPhiAnh.HuynhTanHoang";
    String ipDNSReverse = DNSLookup.getMyIPDNSReverse();
    private DNSApplication app;
    InetAddress ipDNS, ipDNS_t;
    DatagramPacket sender, receiver;
    DatagramSocket server;
    String DNSServer_Com = "", DNSServer_Net = "", DNSServer_Org = "";

    String[][] arrIP;
    String[][] OtherIP;

    Thread checkAliveThread;
    CheckAliveThread checkAlive;

    boolean flagDistributedServer = false;
    boolean flagRMIforRootServer = false;
    boolean flagRMIforSlaveServer = false;
    
    RMIThread RMI;
    Thread RMIThread;
    

    public DNSThread(DNSApplication app) {
        this.app = app;
        arrIP = new String[2][2];
    }

    public void setArrIP(String[][] arrIP, String[][] OtherIP) {
        this.arrIP = arrIP;
        this.OtherIP = OtherIP;

        //if (this.arrIP.length > 0) {
        checkAlive();
        //}

    }
    
    public void setArrIPUpdate(String[][] arrIP){
        this.arrIP=arrIP;
    }

    public void checkAlive() {
        if (checkAliveThread != null) {
            checkAliveThread.stop();
            checkAliveThread = null;
            checkAlive = null;
        }
        //if(arrIP.length>0){
        checkAlive = new CheckAliveThread(app, this, arrIP);
        checkAliveThread = new Thread(checkAlive);
        checkAliveThread.start();
        //}

    }

    public String getDNSServerAddress() {
        return DNSServer;
    }

    public void setDNSServerAddress(String ipDNS) {
        try {
            this.DNSServer = ipDNS;
            this.ipDNS = InetAddress.getByName(DNSServer);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(app, "Đã có lỗi xảy ra", "Lỗi", JOptionPane.ERROR);
        }
    }

    public String getDNSName() {
        return DNSName;
    }

    public void enableFlagDistributedServer() {
        flagDistributedServer = true;
    }

    public void disableFlagDistributedServer() {
        flagDistributedServer = false;
    }

    public boolean statusFlagDistributedServer() {
        return flagDistributedServer;
    }

    public void enabledFlagRMIforRootServer() {
        flagRMIforRootServer = true;
    }

    public void disableFlagRMIforRootServer() {
        flagRMIforRootServer = false;
    }

    public boolean statusFlagRMIforRootServer() {
        return flagRMIforRootServer;
    }

    public void enabledFlagRMIforSlaveServer() {
        flagRMIforSlaveServer = true;
    }

    public void disableFlagRMIforSlaveServer() {
        flagRMIforSlaveServer = false;
    }

    public boolean statusFlagRMIfortSlaveServer() {
        return flagRMIforSlaveServer;
    }

    public void setDNSName(String DNSName) {
        this.DNSName = DNSName;
    }

    public DatagramPacket getReceiver() {
        return receiver;
    }

    public String getDNSServer() {
        return DNSServer;
    }

    public DatagramSocket getMainSocket() {
        return server;
    }

    public void sendPacket(DatagramPacket sender) throws IOException {
        server.send(sender);
    }

    @Override
    public void run() {

        startRMIThread();
        
        byte[] send, receive;
        
        try {
            //khoi tao cac bien
            ipDNS = InetAddress.getByName(DNSServer);
            ipDNS_t = InetAddress.getByName(DNSServer);

            //Tao server, mo port 53
            server = new DatagramSocket(53);

            //lay dia chi gateway cua may
//            String gateway=DNSLookup.getDefaultGateWay();
//            System.out.println("Default GateWay: "+gateway);
            //send=new byte[sizeByte];
            //nhan goi DNS tu client
            while (true) {
//                System.out.println("****************************************************");
                receive = new byte[sizeByte];//110];
                receiver = new DatagramPacket(receive, receive.length);
                server.receive(receiver);
                app.getInfo(receiver.getAddress(), DNSLookup.getMyIPAddress(), DNSEnum.ClientToServer);

                if (flagDistributedServer == true) {
                    if (flagRMIforRootServer == false) {
                        miniDNSThread mini = new miniDNSThread(app, this, server, receive, receiver.getAddress(), receiver.getPort(), arrIP);
                        Thread miniThread = new Thread(mini);
                        miniThread.start();
                        //                    Thread manaThread = new Thread(new AutoOff(miniThread,mini,1000));
                        //                    manaThread.start();
                    } else {
                        //Trường hợp gọi RMI đến các Slave
                        miniRMIThread mini=new miniRMIThread(app, this, server, receive, receiver.getAddress(), receiver.getPort(), arrIP);
                        Thread miniThread=new Thread(mini);
                        miniThread.start();

                    }
                } else {
                    //Tạo thread gửi đến DNS Google
                    miniDNSThread1 mini = new miniDNSThread1(app, this, server, receive, receiver.getAddress(), receiver.getPort());
                    Thread miniThread1 = new Thread(mini);
                    miniThread1.start();
//                    Thread manaThread = new Thread(new AutoOff(miniThread1,mini,1000));
//                    manaThread.start();
                }

            }
        } catch (Exception e) {
//            System.out.println("Da co loi xay ra");
            JOptionPane.showMessageDialog(null, "Đã có lỗi xảy ra. Chỉ một chương trình và một port 53 được thực thi cùng một lúc. Bạn vui lòng kiểm tra lại", "Đã có lỗi xảy ra", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }
    
    public void stopRMIThread(){
        try {
            if(RMI!=null){
                RMIThread.stop();
                RMIThread=null;
                RMI=null;
            }
        } catch (Exception e) {
//            System.out.println("Khong the xoa Thread RMI");
        }
    }
    
    public void startRMIThread(){
        RMI=new RMIThread(app, this);
        RMIThread=new Thread(RMI);
        RMIThread.start();
    }

}
