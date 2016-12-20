/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import AutoOff.AutoOff;
import GUI.DNSApplication;
import dns.DNSLookup;
import dns.DNSPacket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author DELL
 */
public class UpdateDatabaseThread implements Runnable{
    DNSPacket packet;
    String[] ipAddress;
    String ip,domain;
    DNSApplication app;
    
    public UpdateDatabaseThread(DNSApplication app,byte[] receive){
        this.app=app;
        this.packet=new DNSPacket(receive);
    }
    
    @Override
    public void run() {
        this.domain=packet.domain;
        this.ipAddress=packet.ipAddress;
        if(ipAddress[0]==null||ipAddress[0].equals(""))
            return;
        try{
            Connection con = DriverManager.getConnection(Database.linkDatabaseServer, Database.username,Database.password);
            
//            String query = "SELECT ip FROM DNSRecord WHERE ip='" + ipAddress[0] + "'";
//            PreparedStatement preparedStmt = con.prepareStatement(query);
//            ResultSet rs = preparedStmt.executeQuery();
//            if (rs.next()) {
//                con.close();
//                return;
//            }
                    
            for(String ip:ipAddress){
                try{
                    String query = "SELECT ip FROM DNSRecord WHERE ip='"+ip+"'";
                    PreparedStatement preparedStmt = con.prepareStatement(query);
                    ResultSet rs=preparedStmt.executeQuery();
                    if(rs.next()){
                        continue;
                    }
                    if(DNSLookup.checkIP(ip)==true){
                        query = "INSERT INTO `dnsserver`.`dnsrecord` (`domain`, `ip`) VALUES ('"+domain+"', '"+ip+"')";
                        preparedStmt = con.prepareStatement(query);
                        preparedStmt.executeUpdate();
                    }
                }catch(Exception ex){

                }
            }
            con.close();
            app.getDNSRecord();
        }catch(Exception ex){
            
        }
    }
    
    
}