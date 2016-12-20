/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author thehaohcm
 */
public class TestIpAddr {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here

//	String command = "ipconfig";
//       Process p = Runtime.getRuntime().exec(command);
//
//       BufferedReader inn = new BufferedReader(new InputStreamReader(p.getInputStream()));
//       Pattern pattern = Pattern.compile(".*IPv4 Address.*: (.*)");
//       
//       int c=0;
//       while (true) {
//            String line = inn.readLine();
//
//	    if (line == null)
//	        break;
//
//	    Matcher mm = pattern.matcher(line);
//	    if (mm.matches()) {
//                if(c==2){
//                    String ip_t=mm.group(1);
//                    String[] ip_arr=ip_t.split("[(]");
//                    System.out.println(ip_arr[0]);
//                    break;
//                }
//                c++;
//	    }
//	}
//        String ip;
//    try {
//        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
//        while (interfaces.hasMoreElements()) {
//            NetworkInterface iface = interfaces.nextElement();
//            // filters out 127.0.0.1 and inactive interfaces
//            if (iface.isLoopback() || !iface.isUp())
//                continue;
//
//            Enumeration<InetAddress> addresses = iface.getInetAddresses();
//            //using(addresses.hasMoreElements()) {
//                InetAddress addr = addresses.nextElement();
//                ip = addr.getHostAddress();
//                //ip=ip.substring(ip.indexOf("192"));
//                System.out.println("ip: "+ip);
//            //}
//        }
        Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
        while (en.hasMoreElements()) {
            NetworkInterface ni = en.nextElement();
            System.out.println(" Display Name = " + ni.getDisplayName());

            List<InterfaceAddress> list;
            list = ni.getInterfaceAddresses();
            Iterator<InterfaceAddress> it = list.iterator();

            while (it.hasNext()) {
                InterfaceAddress ia = it.next();
                System.out.println(" Broadcast = " + ia.getBroadcast());
            }
        }
    }
}
