package RMI;


import java.io.Serializable;
import java.net.InetAddress;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author thehaohcm
 */
public class DNSRMIPacket implements Serializable{
    InetAddress address;
    int port;
    byte[] DNSPacket;
    String domain;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
    
    public DNSRMIPacket(InetAddress address,int port,byte[] DNSPacket, String domain){
        this.address=address;
        this.port=port;
        this.DNSPacket=DNSPacket;
        this.domain = domain;
    }
    
    public InetAddress getAddress(){
        return address;
    }
    
    public int getPort(){
        return port;
    }
    
    public byte[] getDNSPacket(){
        return DNSPacket;
    }
}
