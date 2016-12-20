/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dns;

import Database.Database;
import Model.DNSRecord;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author thehaohcm
 */
public class DNSLookup {
    private static boolean flagGetMyIPAddress=false;
    private static InetAddress myIPAddress;
    
    private static boolean flagGetMyIPBroadCast=false;
    private static String myIPBroadCast;
    
    public static String getMyIPDNSReverse() {
        String ip="";
        try{
            String ip_t=DNSLookup.getMyIPAddress().toString().substring(1);
            String[] ip_t_arr=ip_t.split("\\.");
            for(int i=ip_t_arr.length-1;i>=0;i--){
                ip+=ip_t_arr[i]+".";
            }
            ip+="in-addr.arpa";
        }catch(Exception ex){
            
        }
        return ip;
    }
    
    public static String getMyLocalhostDNSReverse(){
        return "1.0.0.721.in-addr.arpa";
    }
    
    public static boolean checkIP(String s){
        if(s.trim().equals(""))
            return false;
        
        String[] arr = s.split("[.]");
        if (arr.length != 4)
            return false;
        for(int i = 0; i < arr.length; i++){
            try{
                int x = Integer.parseInt(arr[i]);
                if(i != 3){
                    if(x < 0 || x > 255)
                        return false; 
                } 
                else 
                    if(x < 1 || x > 255)
                        return false;
                
            }catch(Exception e){
                return false;
            }
        }
        return true;
    }
    
    public static boolean checkSuffixDomain(String suffixDomain){
        int length = suffixDomain.length();
//        if(length>5)
//            return false;
        suffixDomain = suffixDomain.toLowerCase();
        if(suffixDomain.charAt(0) != '.')
            return false;
        if(suffixDomain.charAt(length - 1) <97 || suffixDomain.charAt(length - 1) >122)
            return false;
        for(int i = 1; i < length-1; i++){                                                              //*********//
            if(suffixDomain.charAt(i) <97 || suffixDomain.charAt(i) >122)
                return false;
            if(suffixDomain.charAt(i) == '.' && suffixDomain.charAt(i+1) == '.')
                return false;
        }
        return true;
    }
    
    public static boolean checkDomain(String domain){
//        String reg;
//        reg = "^(([a-zA-Z]{1})|([a-zA-Z]{1}[a-zA-Z]{1})|([a-zA-Z]{1}[0-9]{1})|([0-9]{1}[a-zA-Z]{1})|([a-zA-Z0-9][a-zA-Z0-9-_]{1,61}[a-zA-Z0-9]))\\.([a-zA-Z]{2,6}|[a-zA-Z0-9-]{2,30}\\.[a-zA-Z]{2,3})$";
//        return domain.matches(reg);
        
        if(domain.trim().equals(""))
            return false;
        
        String[] arr = domain.split("[.]");
        if(arr.length==0)
            return false;
        
        for(String str:arr){
            str=str.toLowerCase();
            for(char c:str.toCharArray()){
                if((c<48||c>57)&&(c<97||c>122)&&c!=45)
                    return false;
            }
        }
        return true;
    }
    
    public static ArrayList<String> getIP(String domain) throws SQLException{
        Connection con = DriverManager.getConnection(Database.linkDatabaseServer, Database.username,Database.password);
        ArrayList ip=new ArrayList<String>();
        
        String query = "SELECT ip FROM DNSRecord WHERE domain='"+domain+"' ORDER BY RAND() LIMIT 5";
        PreparedStatement preparedStmt = con.prepareStatement(query);
        ResultSet rs=preparedStmt.executeQuery();
        while(rs.next()){
            ip.add(rs.getString("ip"));
        }
        return ip;
    }
    
    public static ArrayList<DNSRecord> getDNSRecords(Connection conn) throws SQLException{
        ArrayList<DNSRecord> arr=new ArrayList<DNSRecord>();
        if(conn==null)
            return arr;
       
        String query = "SELECT * FROM DNSRecord;";
        PreparedStatement preparedStmt = conn.prepareStatement(query);
        ResultSet rs=preparedStmt.executeQuery();
        while(rs.next()){
            arr.add(new DNSRecord(rs.getInt("id"),rs.getString("domain"),rs.getString("ip")));
        }
        return arr;
    }
    
    public static boolean updateDNSRecord(Connection conn,int id,String domain,String ip){
        try{
            if(conn==null)
                return false;
            String query = "UPDATE `dnsserver`.`dnsrecord` SET `domain`='"+domain+"', `ip`='"+ip+"' WHERE `id`="+id+";";
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.execute();
        }catch(Exception ex){
            return false;
        }
        return true;
    }
    
    public static String getDefaultGateWay() throws IOException{
        Process result=Runtime.getRuntime().exec("netstat -rn");
        
        BufferedReader output=new BufferedReader(new InputStreamReader(result.getInputStream()));
        String line=output.readLine();
        while(line!=null){
            if(line.contains("0.0.0.0"))
                break;
            line=output.readLine();
        }
        StringTokenizer st=new StringTokenizer(line);
        st.nextToken();
        st.nextToken();
        String gateway=st.nextToken();
        return gateway;
    }
    
    public static InetAddress getMyIPAddress() throws IOException {
        //đoạn thêm vào
        boolean flag=false;
        //end.
        if (flagGetMyIPAddress == false) {
            String command = "ipconfig";
            Process p = Runtime.getRuntime().exec(command);

            BufferedReader inn = new BufferedReader(new InputStreamReader(p.getInputStream()));
            Pattern pattern = Pattern.compile(".*IPv4 Address.*: (.*)");
            InetAddress ip, gateway;
            ip = InetAddress.getLocalHost();
            int c = 0;
            while (true) {
                String line = inn.readLine();

                if (line == null) 
                    break;
                
                //đoạn được thêm vào
                if(flag==true)
                    if (line.contains("adapter"))
                        flag = false;
                    else
                        continue;

                if (line.contains("adapter") && (line.contains("VMware") || line.contains("VirtualBox")))
                    flag = true;
                else 
                    flag = false;

                if (flag) 
                    continue;
                //end

                Matcher mm = pattern.matcher(line);
                if (mm.matches()) {
                    //if(c==2){
                    String ip_t = mm.group(1);
                    String[] ip_arr = ip_t.split("[(]");
                    //if (ip_arr[0].contains("192.168")) {
                        ip = InetAddress.getByName(ip_arr[0]);
                        myIPAddress=ip;
                        flagGetMyIPAddress=true;
                        return ip;
                    //}
                    //                }
                    //                c++;
                }
            }


            pattern = Pattern.compile(".*Default Gateway.*: (.*)");

            while (true) {
                String line = inn.readLine();

                if (line == null) {
                    break;
                }

                Matcher mm = pattern.matcher(line);
                if (mm.matches()) {
    //                if(c==2){
                    String ip_t = mm.group(1);
                    String[] ip_arr = ip_t.split("[(]");

                    //if (ip_arr[0].contains("192.168")) {
                    ip = InetAddress.getByName(ip_arr[0]);
                        myIPAddress=ip;
                        flagGetMyIPAddress=true;
                        return ip;
                    //}
    //                }
    //                c++;
                }
            }

    //        InetAddress defaultgateway=InetAddress.getLocalHost();
    //        if(!defaultgateway.getHostAddress().equals(null)||!defaultgateway.getHostAddress().equals(""))
    //            return ip;//return InetAddress.getLocalHost();
            //String ip;
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                if (iface.isLoopback() || !iface.isUp()) {
                    continue;
                }

                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                InetAddress addr = addresses.nextElement();
                myIPAddress=addr;
                flagGetMyIPAddress=true;
                return addr;
            }
            myIPAddress=InetAddress.getLocalHost();
            flagGetMyIPAddress=true;
            return myIPAddress;
            
        }else{
            return myIPAddress;
        }
    }
    
    public static void setMyIPBroadCast(String ipBroadCast){
        myIPBroadCast=ipBroadCast;
        flagGetMyIPBroadCast=true;
    }
    
    public static String getMyIPBroadCast(){
        if(!flagGetMyIPBroadCast){
            String myBroadCast="192.168.1.255";
            try{
                if(!flagGetMyIPBroadCast){
                    String myIP=DNSLookup.getMyIPAddress().toString();
                    myBroadCast=myIP.substring(1, myIP.lastIndexOf(".")+1)+"255";
                    flagGetMyIPBroadCast=true;
                }
            }
            catch(Exception ex){

            }
            myIPBroadCast=myBroadCast;
            flagGetMyIPBroadCast=true;
            return myBroadCast;
        }else{
            return myIPBroadCast;
        }
    }
    
    public static void setMyIPAddress(String IP) throws UnknownHostException{
        myIPAddress=InetAddress.getByName(IP);
        flagGetMyIPAddress=true;
    }
    
    public static boolean addDomain(String domain,String ip){
        try{
            Connection c=DriverManager.getConnection(Database.linkDatabaseServer, Database.username,Database.password);
            
            String query = "INSERT INTO `dnsserver`.`dnsrecord` (`domain`, `ip`) VALUES ('"+domain+"', '"+ip+"');";
            PreparedStatement preparedStmt = c.prepareStatement(query);
            preparedStmt.execute();
        }catch(Exception ex){
            return false;
        }
        return true;
    }
   
    public static boolean removeDomain(Connection conn,int id){
        try{
            if(conn==null)
                return false;
            String query = "DELETE FROM `dnsserver`.`dnsrecord` WHERE `id`="+id+";";
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            boolean rs=preparedStmt.execute();
        }catch(Exception ex){
            return false;
        }
        return true;
    }
    
    public static void clearAllRecord(){
        Connection con;
        try {
            con = DriverManager.getConnection(Database.linkDatabaseServer, Database.username, Database.password);
            String query = "DELETE FROM `dnsserver`.`dnsrecord`";
            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.executeUpdate();
            con.close();
        } catch (Exception ex) {

        }
    }
}
