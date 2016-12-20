/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dns;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.util.ArrayList;

/**
 *
 * @author thehaohcm
 */
public class DNSPacket {

    public boolean isReverse;
    public boolean isRequest;
    public byte[] packet;
    public int transactionID;
    public short flags, questions, answers, authority, additional;
    public String domain;
    public short recordType, class_a;
    public short[] fiels, type, class_b, addrlen;
    public int[] ttl;
    public String[] ipAddress;
    //public int lenIPAddress;

    public DNSPacket() {
        packet = null;
        transactionID = flags = questions = answers = authority = additional = recordType = class_a = -1;
        fiels = type = class_b = addrlen = null;
        ttl = null;
        domain = null;
        ipAddress = null;
    }

    public DNSPacket(byte[] packet) {
        try {
            this.isReverse = false;
            this.isRequest = true;
            this.packet = packet;
            DataInputStream din = new DataInputStream(new ByteArrayInputStream(packet));
            transactionID = din.readShort();

            flags = din.readShort();
            questions = din.readShort();
            answers = din.readShort();
            if(answers>0)
                isRequest=false;

//            if (answers > 4) //kiem tra lai dong nay
//            {
//                answers = 4; //kiem tra lai dong nay
//            }
//            System.out.println("answers: "+answers);
            //lenIPAddress = answers;

            authority = din.readShort();
            additional = din.readShort();
            domain = new String("");
            ipAddress = new String[answers];

            int recLen = 0;
            while ((recLen = din.readByte()) > 0) {
                byte[] record = new byte[recLen];

                for (int i = 0; i < recLen; i++) {
                    record[i] = din.readByte();
                }
                if (domain.equals("")) {
                    domain += new String(record, "UTF-8");
                } else {
                    domain += "." + new String(record, "UTF-8");
                }
            }

            recordType = din.readShort();
//            if(recordType==1)
//                isRequest=true;
            //else 
            if(recordType==12){
                isReverse=true;
            }
            class_a = din.readShort();
            fiels=new short[answers];
            type=new short[answers];
            class_b=new short[answers];
            ttl = new int[answers];
            addrlen = new short[answers];
            //isReverse=true;
            if(isRequest==false){
                try {
                    for (int i = 0; i < answers; i++) {
                        try{
                            fiels[i] = din.readShort();
                            type[i] = din.readShort();
                            class_b[i] = din.readShort();
                            ttl[i] = din.readShort();
                            ttl[i] += din.readShort();
                            addrlen[i] = din.readShort();
//                            System.out.println("address len: "+addrlen[i]);

                            
                            ipAddress[i] = String.format("%d", (din.readByte() & 0xFF));
                            for (int j = 1; j < addrlen[i]; j++) {
                                ipAddress[i] += "." + String.format("%d", (din.readByte() & 0xFF));
                            }
                            if(isReverse||type[i]==5||ipAddress[i].length()>15){ //type[i]==5 => reverse
                                String t=xuLyIPAddress(ipAddress[i]);
                                ipAddress[i]=t;
                            }
                            
                        }catch(Exception ex){
//                            System.out.println("Khong the tiep tuc lay Ip - Domain");
                            break;
                        }
                    }
                } catch (Exception ex) {
                    isRequest = true;
                }
            }
        } catch (Exception ex) {
//            System.out.println("Khong the doc tiep goi DNS");
        }
    }
    
    public String xuLyIPAddress(String ip){
        String domain = "";
        int size = 0, i, j;
        String []arr = ip.split("[.]");
//        System.out.println(arr.length);
        for(i = 0; i < arr.length - 1; i = i + size+1){
            size = Integer.parseInt(arr[i]);
            for(j = i + 1; j < size + i + 1; j++){
                int kt = Integer.parseInt(arr[j]);
                char c = (char) kt;
                domain += c;
            }
            if(j != (arr.length -1))
                domain+=".";
        }
        return domain;
    }

    public static byte[] getPacketDNSResponse(int TransactionID,int Authorize, String domain, ArrayList<String> ip_Arr) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        // *** Build a DNS Request Frame ****
        // Identifier: A 16-bit identification field generated by the device that creates the DNS query. 
        // It is copied by the server into the response, so it can be used by that device to match that 
        // query to the corresponding reply received from a DNS server. This is used in a manner similar 
        // to how the Identifier field is used in many of the ICMP message types.
        dos.writeShort(TransactionID);//dos.writeShort(0x0002);

        // Write Query Flags
        dos.writeShort(0x8180);

        // Question Count: Specifies the number of questions in the Question section of the message.
        dos.writeShort(0x0001);

        // Answer Record Count: Specifies the number of resource records in the Answer section of the message.
        dos.writeShort(ip_Arr.size());//dos.writeShort(0x0002);//0x0001

        // Authority Record Count: Specifies the number of resource records in the Authority section of 
        // the message. (“NS” stands for “name server”)
        //dos.writeShort(0x0000);
        dos.writeShort(0x00);

        // Additional Record Count: Specifies the number of resource records in the Additional section of the message.
        dos.writeShort(0x00);
        // TODO: write query
        String[] domainParts = domain.split("\\.");
        for (int i = 0; i < domainParts.length; i++) {
            byte[] domainBytes = domainParts[i].getBytes("UTF-8");
            dos.writeByte(domainBytes.length);
            dos.write(domainBytes);
        }

        // No more parts
        dos.writeByte(0x00);

        // Record Type 0x01 = A (Host Request)
        dos.writeShort(0x01); //0xC

        // Class 0x01 = IN - Internet | 0x00 = Reversed
        dos.writeShort(0x0001);

        for (int i = 0; i < ip_Arr.size(); i++) {
            String ip = ip_Arr.get(i);
            //Fiels - 4*4=16 bits //diem bat dau them vao addition neu domain co nhieu ip
            dos.writeShort(0xc00c);

            //Type - 16 bits
            dos.writeShort(0x0001);

            //Class - 16 bits
            dos.writeShort(0x0001);

            //TTL - 32 bits
            dos.writeShort(0x0000);
            dos.writeShort(0x112A);//112A
            
            //dos.writeByte(0x00);

            String[] ipParts = ip.split("\\.");
            //do dai IP
            dos.writeShort(ipParts.length);
            //Noi dungIP Address
//            for (int j = 0; j < ipParts.length; j += 2) {
//                short ipShort1 = Short.valueOf(ipParts[j]);
//                short ipShort2 = Short.valueOf(ipParts[j + 1]);
//                dos.writeShort((ipShort1 << 8) | ipShort2);
//            }

            for (int j = 0; j < ipParts.length; j ++) {
                short ipShort1 = Short.valueOf(ipParts[j]);
//                short ipShort2 = Short.valueOf(ipParts[j + 1]);
                dos.writeByte(ipShort1);
            }
        }
        byte[] dnsFrame;
        dnsFrame = baos.toByteArray();
        return dnsFrame;
    }

    public static byte[] getPacketDNSReverse(int transactionID,String domain, String ip) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        // *** Build a DNS Request Frame ****
        // Identifier: A 16-bit identification field generated by the device that creates the DNS query. 
        // It is copied by the server into the response, so it can be used by that device to match that 
        // query to the corresponding reply received from a DNS server. This is used in a manner similar 
        // to how the Identifier field is used in many of the ICMP message types.
        dos.writeShort(transactionID);

        // Write Query Flags
        dos.writeShort(0x8180);

        // Question Count: Specifies the number of questions in the Question section of the message.
        dos.writeShort(0x0001);

        // Answer Record Count: Specifies the number of resource records in the Answer section of the message.
        dos.writeShort(0x0001);

        // Authority Record Count: Specifies the number of resource records in the Authority section of 
        // the message. (“NS” stands for “name server”)
        //dos.writeShort(0x0000);
        dos.writeShort(0x00);

        // Additional Record Count: Specifies the number of resource records in the Additional section of the message.
        dos.writeShort(0x00);
        // TODO: write query
        //them vao 
        //dos.writeShort(0x0000);
        
        //write domain
        String[] domainParts = domain.split("\\.");
        for (int i = 0; i < domainParts.length; i++) {
            byte[] domainBytes = domainParts[i].getBytes("UTF-8");
            dos.writeByte(domainBytes.length);
            dos.write(domainBytes);
        }

        // No more parts
        dos.writeByte(0x00);
        //dos.writeByte(0x00);

        // Record Type 0x01 = A (Host Request)
        dos.writeShort(0x0C); //0xC

        // Class 0x01 = IN - Internet | 0x00 = Reversed
        dos.writeShort(0x0001);

        //Fiels - 4*4=16 bits
        dos.writeShort(0xc00c);

        //Type - 16 bits
        dos.writeShort(0x000c);//0x0006);

        //Class - 16 bits
        dos.writeShort(0x0001);

        //TTL - 32 bits
        dos.writeShort(0x2A30);
        dos.writeShort(0x00);
        //dos.writeShort(0x8af4);
        //dos.writeShort(0x0020);
        
        //Noi dungIP Address
        String[] ip_arr=ip.split("\\.");
        dos.writeShort(ip.length()-ip_arr.length+1);
        for (int i = 0; i < ip_arr.length; i++) {
           //dos.writeShort(ip_arr[i].length());
            byte[] ipBytes = ip_arr[i].getBytes("UTF-8");
            dos.writeByte(ipBytes.length);
            dos.write(ipBytes);
            String result=new String(ipBytes);
        }
        dos.writeShort(0x0000);
        byte[] dnsFrame;
        dnsFrame = baos.toByteArray();
        return dnsFrame;
    }

//    public byte[] getData() throws IOException { //tao goi DNS gui ve Client dia chi IP cua domain
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        DataOutputStream dos = new DataOutputStream(baos);
//
//        // *** Build a DNS Request Frame ****
//        // Identifier: A 16-bit identification field generated by the device that creates the DNS query. 
//        // It is copied by the server into the response, so it can be used by that device to match that 
//        // query to the corresponding reply received from a DNS server. This is used in a manner similar 
//        // to how the Identifier field is used in many of the ICMP message types.
//        dos.writeShort(0x1234);
//
//        // Write Query Flags
//        dos.writeShort(0x0100);
//
//        // Question Count: Specifies the number of questions in the Question section of the message.
//        dos.writeShort(0x0001);
//
//        // Answer Record Count: Specifies the number of resource records in the Answer section of the message.
//        //dos.writeShort(0x0000);
//        dos.writeShort(0x011);
//
//        // Authority Record Count: Specifies the number of resource records in the Authority section of 
//        // the message. (“NS” stands for “name server”)
//        dos.writeShort(0x0000);
//
//        // Additional Record Count: Specifies the number of resource records in the Additional section of the message.
//        dos.writeShort(0x0000);
//
//        // TODO: write query
//        String[] domainParts = domain.split("\\.");
//
//        for (int i = 0; i < domainParts.length; i++) {
//            byte[] domainBytes = domainParts[i].getBytes("UTF-8");
//            dos.writeByte(domainBytes.length);
//            dos.write(domainBytes);
//        }
//
//        // No more parts
//        dos.writeByte(0x00);
//
//        // Type 0x01 = A (Host Request)
//        dos.writeShort(0x0001);
//
//        // Class 0x01 = IN
//        dos.writeShort(0x0001);
//
//        byte[] dnsFrame = baos.toByteArray();
//
//        //System.out.println("Sending: " + dnsFrame.length + " bytes");
//        for (int i = 0; i < dnsFrame.length; i++) {
//            System.out.print("0x" + String.format("%x", dnsFrame[i]) + " ");
//        }
//        dos.writeByte(0x0000);
//        return dnsFrame;
//    }

    @Override
    public String toString() {

        String str = "";
        try {
            if (packet == null) {
                str = "Khong co thong tin gi ve goi DNS";
                return str;
            }
            str = "";
            str += "Transaction ID: " + this.transactionID + "\n";
            str += "Flags: " + this.flags + "\n";
            str += "Questions: " + this.questions + "\n";
            str += "Answers RRs: " + this.answers + "\n";
            str += "Authority RRs: " + this.authority + "\n";
            str += "Additional RRs: " + this.additional + "\n";
            str += "Domain: " + this.domain + "\n";
            str += "Record Type: " + this.recordType + "\n";
            str += "Class: " + this.class_a + "\n";

            if(this.isRequest==false){
//            for (int i = 0; i < this.lenIPAddress; i++) {
                for (int i = 0; i < this.answers; i++) {
                    try{
                        str += "Fiels[" + (int) i + "]: " + this.fiels[i] + "\n";
                        str += "Type[" + (int) i + "]: " + this.type[i] + "\n";
                        str += "Class[" + (int) i + "]: " + this.class_b[i] + "\n";
                        str += "TTL[" + (int) i + "]: " + this.ttl[i] + "\n";
                        str += "IP Address[" + (int) i + "]: " + this.ipAddress[i] + "\n";
                    }catch(Exception ex){
                        System.out.println("Khong the doc tiep dia chi IP - Domain");
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            str+=("Ko the doc noi dung");
        }
        return str;
    }

    public void getStringArr() {
        try{
            DatagramPacket dp = new DatagramPacket(packet, packet.length);
            for (int i = 0; i < dp.getLength(); i++) {
                System.out.print("0x" + String.format("%x", packet[i]) + " ");
            }
            System.out.println("\n"); 
        }catch(Exception ex){
            System.out.println("Khong the tiep tuc lay noi dung");
        }
    }
    
    public byte[] _getResponsePacket() throws IOException{
        try{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        // *** Build a DNS Request Frame ****
        // Identifier: A 16-bit identification field generated by the device that creates the DNS query. 
        // It is copied by the server into the response, so it can be used by that device to match that 
        // query to the corresponding reply received from a DNS server. This is used in a manner similar 
        // to how the Identifier field is used in many of the ICMP message types.
        dos.writeShort(this.transactionID);//dos.writeShort(0x0002);
        //System.out.println("TransactionID: "+TransactionID);

        // Write Query Flags
        dos.writeShort(this.flags);

        // Question Count: Specifies the number of questions in the Question section of the message.
        dos.writeShort(this.questions);

        // Answer Record Count: Specifies the number of resource records in the Answer section of the message.
        dos.writeShort(this.answers);//dos.writeShort(0x0002);//0x0001

        // Authority Record Count: Specifies the number of resource records in the Authority section of 
        // the message. (“NS” stands for “name server”)
        //dos.writeShort(0x0000);
        dos.writeShort(this.authority);

        // Additional Record Count: Specifies the number of resource records in the Additional section of the message.
        dos.writeShort(this.additional);
        // TODO: write query
        String[] domainParts = domain.split("\\.");
        for (int i = 0; i < domainParts.length; i++) {
            byte[] domainBytes = domainParts[i].getBytes("UTF-8");
            dos.writeByte(domainBytes.length);
            dos.write(domainBytes);
        }

        // No more parts
        dos.writeByte(0x00);

        // Record Type 0x01 = A (Host Request)
        dos.writeShort(this.recordType); //0xC

        // Class 0x01 = IN - Internet | 0x00 = Reversed
        dos.writeShort(this.class_a);

        for (int i = 0; i < this.answers; i++) {
            String ip = ipAddress[i];
            //Fiels - 4*4=16 bits //diem bat dau them vao addition neu domain co nhieu ip
            dos.writeShort(this.fiels[i]);

            //Type - 16 bits
            dos.writeShort(this.type[i]);

            //Class - 16 bits
            dos.writeShort(this.class_b[i]);

            //TTL - 32 bits
            dos.writeInt(234);
//            dos.writeShort(0x0000);
//            dos.writeShort(0x112A);//112A

            String[] ipParts = ip.split("\\.");
            //do dai IP
            dos.writeShort(ipParts.length);
            //Noi dungIP Address
            byte[] do_main=ipAddress[i].getBytes("UTF-8");
            dos.write(do_main);
//            int c=(int)ipParts[0].charAt(0);
//            if(this.type[i]==5){
//                for(int j=0;j<ipParts.length;j++){
////                    byte[] do_main=ipParts[j].getBytes("UTF-8");;
////                    dos.writeByte(ipParts[j].length());
//                   // dos.write(ipParts[j]);
//                }
//            }else{
//                for (int j = 0; j < ipParts.length; j += 2) {
//                    short ipShort1 = Short.valueOf(ipParts[j]);
//                    short ipShort2 = Short.valueOf(ipParts[j + 1]);
//                    dos.writeShort((ipShort1 << 8) | ipShort2);
//                }
//            }
        }
        byte[] dnsFrame;
        dnsFrame = baos.toByteArray();
        return dnsFrame;
        }catch(Exception ex){
            
        }
        return null;
    }
}
