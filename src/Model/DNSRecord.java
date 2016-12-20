/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author thehaohcm
 */
public class DNSRecord {
    int id;
    String domain;
    String ip;
    
    public DNSRecord(int id,String domain,String ip){
        this.id=id;
        this.domain=domain;
        this.ip=ip;
    }
    
    public int getID(){
        return id;
    }
    
    public String getDomain(){
        return domain;
    }
    
    public String getIP(){
        return ip;
    }
}
