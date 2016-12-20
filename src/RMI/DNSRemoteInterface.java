/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author DELL
 */
public interface DNSRemoteInterface extends Remote{
    void xuLy(DNSRMIPacket packet) throws RemoteException; 
    
    void inSo(int a) throws RemoteException; 
}
