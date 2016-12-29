/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CheckAlive;

import GUI.DNSApplication;
import AutoOff.AutoOff;
import Enum.DNSEnum;
import dns.DNSLookup;
import dns.DNSThread;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JOptionPane;

/**
 *
 * @author thehaohcm
 */
public class CheckAliveThread implements Runnable { //kiểm tra giá trị mảng arrOtherServer truyền vào với size()>0 mới start thread này

    DNSApplication app;
    DNSThread mainThread;
    String[][] arrServer;
    String ipBroadCast;

    int port;
    
    // Sau mỗi 60s, hệ thống tự động gửi gói DNS BroadCast kiểm tra sự tồn tại của các Slave Server
    int timeForCheckingAlive=60000;

    HashMap<String, String> arrIP;

    public CheckAliveThread(DNSApplication app, DNSThread mainThread, String[][] arrServer) {
        this.app = app;
        this.mainThread = mainThread;
        this.arrServer = arrServer;
        this.ipBroadCast = DNSLookup.getMyIPBroadCast();
        this.port = 1995;

        arrIP = new HashMap<String, String>();
    }
    
    public void refreshArrIP(){
        arrIP=new HashMap<String,String>();
    }

    public void addIPtoArr(String ip) {
        System.out.println("Nhan gia tri ip: " + ip);
        arrIP.put(ip, "");
    }

    private void checkAndUpdate() {
        //String[][] Other = new String[0][2];
        if (arrIP.size() > 0) { //sau khi broadcast lai hệ thống -> mới + cũ
            ArrayList<Integer> arrMissSuffixDomain = new ArrayList<Integer>(); //chứa giá trị index của các phần tử chết trong mảng arrServer (không có Suffix Domain)
            //HashMap<String,String> arrIP_t = new HashMap<String,String>();
            ArrayList<String> arrIP_t=new ArrayList<String>();
            for(String ip:arrIP.keySet()){
                arrIP_t.add(ip);
            }

            for (int i = 0; i < arrServer.length; i++) {
                boolean flag = false;
                for (String ip : arrIP_t) {
                    if (arrServer[i][0].equals(ip)) {
                        arrIP_t.remove(ip);
                        flag = true;
                        break;
                    }
                }
                for (int j = i - 1; j >= 0; j--) {                                                          // Moi update khuc nay.
                    if (arrServer[i][0].equals(arrServer[j][0])) {
                        flag = false;
                        break;
                    }
                }
                if (!flag) {
                    
                    arrMissSuffixDomain.add(i);
                }
            }
            if(arrIP_t.size() > 0){
                if(arrIP_t.size()>= arrMissSuffixDomain.size()){
                    int i = 0;
                    for (int suffixDomainIndex : arrMissSuffixDomain) {
                        for ( ; i < arrIP_t.size(); i++) {
                              if (i != suffixDomainIndex) {
                                arrServer[suffixDomainIndex][0] = arrIP_t.get(i);
                                break;
                            }
                        }
                    }
                 }else{
                    int j = 0;
                    int size = arrMissSuffixDomain.size();
                    while (size > 0) {
                        for (int i = 0; i < arrIP_t.size(); i++) {
                            arrServer[arrMissSuffixDomain.get(j)][0] = arrIP_t.get(i);
                            size--;
                            j++;
                        }
                    }
                }                
            }else{
                for (int suffixDomainIndex : arrMissSuffixDomain) {
                    for (int i = 0; i < arrServer.length; i++) {
                        if (i != suffixDomainIndex) {
                            arrServer[suffixDomainIndex][0] = arrServer[i][0];
                            break;
                        }
                    }
                }       
            }
            /*Doan code nguyen thuy*/
            //arrIP_t chứa giá trị các phần tử mới
//            if (arrMissSuffixDomain.size() > 0) {//xuất hiền các hậu tố domain không còn server quản lý trong mảng arrMissSuffixDomain
//                if (arrIP_t.size() > 0) {//có máy thừa nằm trong mảng arrIP_t
//                    if (arrMissSuffixDomain.size() >= arrIP_t.size()) {//không đủ máy server để đáp ứng cho các hậu tố domain               
//                        int j = 0;
//                        int size = arrMissSuffixDomain.size();
//                        System.out.println("Size: " + size);
//                        while(size > 0){
//                            for(int i = 0; i < arrIP_t.size(); i++){
//                                arrServer[arrMissSuffixDomain.get(j)][0] = arrIP_t.get(i);
//                                size--;
//                                   if(size <= 0)
//                                    break;
//                                j++;
//                            }
//                        }
//                        System.out.println("Danh sach arrIp: ");
//                        for(int i = 0 ; i < arrServer.length; i++){
//                            System.out.println("ip: " + arrServer[i][0]);
//                        }
//                     
//                    } else { //đủ hoặc dư máy
//                        int count = 0;
//                        for (int suffixDomainIndex : arrMissSuffixDomain) {
//                            System.out.println("Gia tri phan tu 0 truoc khi lay: " + arrIP_t.get(count));
//                            arrServer[suffixDomainIndex][0] = arrIP_t.get(count);
//                            System.out.println("Gia tri phan tu 0 sau khi lay: " + arrIP_t.get(count));
//                            //  arrIP_t.remove(0);
//                            count++;
//                            System.out.println("Gia tri phan tu 0 sau khi remove phan tu 0: " + arrIP_t.get(count));
//                        }
//                        int cl = arrIP_t.size() - count - 1;
//                        //if (arrIP_t.size() > 0) {//các máy dư
//                        if(cl > 0){
//                            Other = new String[cl][2];//][arrIP_t.size()][2];
//                            for (int i = 0; i < cl; i++){// arrIP_t.size(); i++) {
//                                Other[i][0] = arrIP_t.get(count);
//                               // Other[i][1] = "";
//                                count++;
//                            }
//                        }
//                    }
//                } else { //không có máy để đáp ứng -> lấy máy thuộc phần tử đầu tiên của màng arrServer để cùng hoạt động trên nhiều hậu tố domain
//                    for (int suffixDomainIndex : arrMissSuffixDomain) {
//                        for (int i = 0; i < arrServer.length; i++) {
//                            if (i != suffixDomainIndex) {
//                                arrServer[suffixDomainIndex][0] = arrServer[i][0];
//                                break;
//                            }
//                        }
//                    }
//                }
//            }else{         
//                if(arrIP_t.size() < 0){
//                    System.out.println("Server chet het va khong tim thay bat ky mot server nao dang ton tai");
//                    mainThread.disableFlagDistributedServer();
//                    //////////////////
//                    return;
//                }
//                else{
//                    int j = 0;
//                    int size = arrMissSuffixDomain.size();
//                    while (size > 0) {
//                        for (int i = 0; i < arrIP_t.size(); i++) {
//                            arrServer[arrMissSuffixDomain.get(j)][0] = arrIP_t.get(i);
//                            size--;
//                            j++;
//                        }
//                    }
//                }
//                     
//            }
//            mainThread.enableFlagDistributedServer();
            //mainThread.setArrIP(arrServer, Other);
//        } else {//Không tìm thấy server nào hoạt động trên hệ thống khi broadcast
//           // JOptionPane.showMessageDialog(app, "Không tìm thấy bất cứ một server nào tồn tại");
//            System.out.println("Khong tim thay bat ky mot server nao dang ton tai");
//            mainThread.disableFlagDistributedServer();
//            //return;
//        }
//            /*Doan code cua tao moi sua. Chua check lai.*/
            int size_chet = arrMissSuffixDomain.size();
            int size_songdu = arrIP_t.size();
            System.out.println("size chet: "+size_chet+" size_songdu: "+size_songdu);
            
            
            
            
//            if (size_chet > 0 && size_songdu >= 0) {
//                if (size_chet > size_songdu) {
//                    if(size_songdu == 0){
//                        for (int suffixDomainIndex : arrMissSuffixDomain) {
//                            for (int i = 0; i < arrServer.length; i++) {
//                                if (i != suffixDomainIndex) {
//                                    arrServer[suffixDomainIndex][0] = arrServer[i][0];
//                                    break;
//                                }
//                            }
//                        }
//                    }else{
//                        int i = 0;
//                    while (size_chet > 0) {
//                        arrServer[arrMissSuffixDomain.get(size_chet - 1)][0] = arrIP_t.get(i++);
//                        if (i == size_songdu) {
//                            i = 0;
//                        }
//                        size_chet--;
//                    }
//                    }
//                    
//                } else if (size_chet <= size_songdu) {
//                    for (int i = 0; i < size_chet; i++) {
//                        arrServer[arrMissSuffixDomain.get(i)][0] = arrIP_t.get(i);
//                    }
//                }
//                mainThread.enableFlagDistributedServer();
//                mainThread.setArrIP(arrServer, Other);
//            }
//                 else if (size_chet > 0 && size_songdu <= 0) {
//                for (int suffixDomainIndex : arrMissSuffixDomain) {
//                    for (int i = 0; i < arrServer.length; i++) {
//                        if (i != suffixDomainIndex) {
//                            arrServer[suffixDomainIndex][0] = arrServer[i][0];
//                            break;
//                        }
//                    }
//                }
//                mainThread.enableFlagDistributedServer();
//                mainThread.setArrIP(arrServer, Other);
//            } else {
//                System.out.println("Server chet het va khong tim thay bat ky mot server nao dang ton tai");
//                mainThread.disableFlagDistributedServer();
//                return;
//            }
        } else {//Không tìm thấy server nào hoạt động trên hệ thống khi broadcast
            //JOptionPane.showMessageDialog(app, "Không tìm thấy bất cứ một server nào tồn tại");
            System.out.println("Khong tim thay bat ky mot server nao dang ton tai");
            mainThread.disableFlagDistributedServer();
            return;
        }
        
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(timeForCheckingAlive);
                
                BroadCastForCheckAliveThread checkAlive = new BroadCastForCheckAliveThread(this);
                Thread broadCastThread = new Thread(checkAlive);
                broadCastThread.start();

                Thread autoOffThread = new Thread(new AutoOff(broadCastThread, checkAlive, 1000));
                autoOffThread.start();
                
                app.getInfo(DNSLookup.getMyIPAddress(), InetAddress.getByName(DNSLookup.getMyIPBroadCast()),DNSEnum.RootServerSendBroadCast);

                Thread.sleep(1200);
                checkAndUpdate();
                mainThread.setArrIPUpdate(arrServer);

            } catch (Exception ex) {

            }
        }
    }

}
