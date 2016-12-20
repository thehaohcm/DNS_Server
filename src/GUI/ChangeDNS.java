/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import BroadCast.BroadCastThread;
import ConceptListBox.Concept;
import AutoOff.AutoOff;
import dns.DNSLookup;
import dns.DNSThread;
import java.awt.event.ItemEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;

/**
 *
 * @author thehaohcm
 */
public class ChangeDNS extends javax.swing.JDialog {

    /**
     * Creates new form ChangeD
     */
    String ipDNS;
    String DNSName;
    DNSApplication app;
    DNSThread dnsThread;//DNSThread dnsThread
    
    BroadCastThread broadCast;
    Thread broadCastThread;
    
    AutoOff autoOffBroadCast;
    Thread autoOffBroadCastThread;
    
    HashMap <String, String> arrIP;
    HashMap<String,String> arrNamePC;
    String chooseIP;
    String broadCastIP;
    
    boolean flagCheck=false;
    boolean flagOpenPort=true;
    
    public ChangeDNS(java.awt.Frame parent, boolean modal, DNSApplication app,DNSThread dnsThread) { //DNSThread dnsThread
        super(parent, modal);
        initComponents();
        
        this.setLocationRelativeTo(app);
        
        NameOfComputerlbl.setText("");
        
        this.app=app;
        this.dnsThread=dnsThread;
        this.ipDNS=dnsThread.getDNSServerAddress();
        this.DNSName=dnsThread.getDNSName();
        jTextField1.setText(ipDNS);
        jTextField2.setText(DNSName);
        
        broadCastIPTxt.setText(DNSLookup.getMyIPBroadCast());
        
        flagOpenPort=app.checkStatusThreadReceiveBroadCast();
        checkBoxOpenPort.setState(flagOpenPort);
        
        try{
            IPAddressTxt.setText(DNSLookup.getMyIPAddress().toString().substring(1));
        }catch(Exception ex){
            
        }
        
        arrIP=new HashMap<String,String>();
        arrNamePC=new HashMap<String,String>();
        
        if(IPComboBox.getItemCount()>0)
            IPComboBox.removeAllItems();
    }
    
    public ChangeDNS(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(parent);
    }
    
    public void getNameIPofComputer(String ip,String name){
        
        //IPComboBox.addItem(ip);
        //Concept temp = new Concept("",nameOfPC);
        arrIP.put(ip, "");
        arrNamePC.put(ip, name);
    }
    
    private void showInfo(Concept concept){
        System.out.println("SuffixDomain: "+concept.getSuffixDomain()+" - Value: "+concept.getNameOfPC());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        IPAddressTxt = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        checkBoxOpenPort = new java.awt.Checkbox();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        broadCastIPTxt = new javax.swing.JTextField();
        broadCastButton = new javax.swing.JButton();
        IPComboBox = new javax.swing.JComboBox<>();
        saveButton = new javax.swing.JButton();
        domainSuffixTxt = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        NameOfComputerlbl = new javax.swing.JTextField();
        rMIcheckBox = new javax.swing.JCheckBox();
        jButton5 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Thay đổi DNS Server"));

        jLabel1.setText(" DNS Server gốc:");

        jLabel2.setText("Tên máy chủ DNS: ");

        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        jLabel3.setText("IP của máy:");

        jLabel8.setText("Mở Port 1995:");

        checkBoxOpenPort.setLabel("Mở Port 1995 - nhận phân tán DNS");
        checkBoxOpenPort.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                checkBoxOpenPortItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3)
                    .addComponent(jLabel8)
                    .addComponent(jLabel2))
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(checkBoxOpenPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(IPAddressTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(27, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(IPAddressTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(checkBoxOpenPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addContainerGap(46, Short.MAX_VALUE))
        );

        jButton1.setText("Thay đổi");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Hủy bỏ");
        jButton2.setActionCommand("");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton1)
                        .addGap(18, 18, 18)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap())
        );

        jTabbedPane1.addTab("DNS Server", jPanel2);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Cài đặt hệ thống phân tán DNS"));

        broadCastButton.setText("BroadCast");
        broadCastButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                broadCastButtonActionPerformed(evt);
            }
        });

        IPComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                IPComboBoxItemStateChanged(evt);
            }
        });
        IPComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IPComboBoxActionPerformed(evt);
            }
        });

        saveButton.setText("Lưu");
        saveButton.setEnabled(false);
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        jLabel4.setText("IP Broadcast:");

        jLabel5.setText("IP:");

        jLabel6.setText("Tên máy:");

        jLabel7.setText("Hậu tố Domain:");

        NameOfComputerlbl.setEditable(false);
        NameOfComputerlbl.setText("jTextField3");
        NameOfComputerlbl.setSelectedTextColor(new java.awt.Color(240, 240, 240));

        rMIcheckBox.setText("Sử dụng RMI");
        rMIcheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rMIcheckBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel4)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(IPComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(domainSuffixTxt)
                    .addComponent(NameOfComputerlbl, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE)
                    .addComponent(broadCastIPTxt)
                    .addComponent(rMIcheckBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(broadCastButton, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(saveButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(24, 24, 24))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(broadCastIPTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(broadCastButton)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(IPComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(NameOfComputerlbl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(domainSuffixTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(saveButton))
                .addGap(18, 18, 18)
                .addComponent(rMIcheckBox)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButton5.setText("Cài đặt");
        jButton5.setEnabled(false);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton3.setText("Thoát");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Reset");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(21, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(62, 62, 62)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton5)
                    .addComponent(jButton3)
                    .addComponent(jButton4))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Hệ thống phân tán DNS", jPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        if(jTextField1.getText().trim().equals("")||jTextField2.getText().trim().equals("")||IPAddressTxt.getText().trim().equals("")){
            JOptionPane.showMessageDialog(this,"Bạn vui lòng nhập đầy đủ các thông tin");
            return;
        }else if(!DNSLookup.checkIP(jTextField1.getText())||!DNSLookup.checkIP(IPAddressTxt.getText()))
        {
            JOptionPane.showMessageDialog(this,"Địa chỉ IP không hợp lệ. Bạn vui lòng nhập lại");
            return;
        }
        try{
            if(!jTextField1.getText().equals(this.ipDNS)||!jTextField2.getText().equals(this.DNSName)||!IPAddressTxt.getText().equals(DNSLookup.getMyIPAddress().toString().substring(1))){
                dnsThread.setDNSServerAddress(jTextField1.getText());
                dnsThread.setDNSName(jTextField2.getText());
                app.changeDNSServer(jTextField1.getText());
                DNSLookup.setMyIPAddress(IPAddressTxt.getText());
                
                app.setChangeIP();
                //JOptionPane.showMessageDialog(null,"Đã thay đổi thành công địa chỉ DNS");
            }
            if(flagOpenPort!=checkBoxOpenPort.getState()){
                if(checkBoxOpenPort.getState())
                    app.startThreadReceiveBroadCast();
                else
                    app.stopThreadReceiveBroadCast();
                
                flagOpenPort=checkBoxOpenPort.getState();
            }
            dispose();
        }catch(Exception ex){
            JOptionPane.showMessageDialog(this,"Đã có lỗi xảy ra, xin vui lòng xem lại địa chỉ IP của máy");
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void broadCastButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_broadCastButtonActionPerformed
        // TODO add your handling code here:
        if(broadCastIPTxt.getText().trim().equals("")){
            JOptionPane.showMessageDialog(this,"Bạn vui lòng nhập vào địa chỉ BroadCast");
        }else if(!DNSLookup.checkIP(broadCastIPTxt.getText())){
            JOptionPane.showMessageDialog(this,"Địa chỉ BroadCast không hợp lệ. Bạn vui lòng nhập lại địa chỉ BroadCast");
        }else{
            try{
                arrIP=new HashMap<String,String>();
                arrNamePC=new HashMap<String,String>();

                IPComboBox.removeAllItems();
                broadCastIP=broadCastIPTxt.getText();
                broadCast=new BroadCastThread(app,broadCastIP, this);
                broadCastThread=new Thread(broadCast);
                broadCastThread.start();

                autoOffBroadCast=new AutoOff(broadCastThread,broadCast,1000);
                autoOffBroadCastThread=new Thread(autoOffBroadCast);
                autoOffBroadCastThread.start();

                try{
                    Thread.sleep(2000);
                    for(String IPstr:arrIP.keySet()){
                        IPComboBox.addItem(IPstr);
                    }
                    if(IPComboBox.getItemCount()>0){
                        chooseIP=IPComboBox.getItemAt(0);
                        domainSuffixTxt.setText(arrIP.get(chooseIP).toString());
                        NameOfComputerlbl.setText(arrNamePC.get(chooseIP).toString());
                        saveButton.setEnabled(true);
                    }
                }
                catch(Exception ex){

                }
            }catch(Exception ex){
                JOptionPane.showMessageDialog(app, "Đã có lỗi xảy ra khi BroadCast đến toàn bộ máy trong mạng");
            }
        }
    }//GEN-LAST:event_broadCastButtonActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        if(flagCheck==true){
            String[][] OtherIP,arrDomainIP;
            int size = 0,sizeOther=0;
            boolean flagOther = false;
            for(String temp:arrIP.values()){
                if(!temp.equals("")){ //Các máy có giá trị suffix domain
                    size++;
                }
                else{
                    if (flagOther == false) { //nếu có một máy tính (đầu tiên) có trị rỗng (không có giá trị), ta đặt là Other
                        String tempkey = arrIP.keySet().toString();
                        arrIP.put(tempkey, "other");
                        flagOther = true;
                        size++;
                    }
                    else{ //Các máy rỗng tiếp theo (nếu có) sẽ được dự trữ trong mảng Other
                        sizeOther++;
                    }
                }
            }


            arrDomainIP=new String[size][2];
            OtherIP=new String[sizeOther][2];
            int i=0,iother=0;


            for(Map.Entry<String,String> entry:arrIP.entrySet()){
                String key=entry.getKey();
                String value=entry.getValue();
                if(!value.equals("")||value!=null){
                    arrDomainIP[i][0]=key;
                    arrDomainIP[i][1]=value;
                    i++;
                }else{
                    OtherIP[i][0]=key;
                    OtherIP[i][1]=value;
                    iother++;
                }
            }


            //dnsThread.goihamtuxa(arrDomainIP, OtherIP);
            //app.stopThreadReceiveBroadCast();
            this.flagOpenPort=false;
            this.checkBoxOpenPort.setState(false);

            dnsThread.setArrIP(arrDomainIP, OtherIP);
            dnsThread.enableFlagDistributedServer();
            if(rMIcheckBox.isSelected()){
                dnsThread.stopRMIThread();
                dnsThread.enabledFlagRMIforRootServer();
            }
            else{
                dnsThread.startRMIThread();
                dnsThread.disableFlagRMIforRootServer();
            }
            this.dispose();
    //        for(Concept temp : arrIP.values()){
    //            if(!temp.getSuffixDomain().equals(""))
    //                size++;
    //            else{
    //                if(flagOther == false){
    //                    temp.setSuffixDomain("other");
    //                    OtherIP=arrIP.keySet().toString();
    //                    flagOther=true;
    //                    size++;
    //                }
    //            }     
    //        }
    //        
    //        String[][] arrIp=new String[size][2];
    //        int i = 0;
    //        for(Concept temp : arrIP.values()){
    //            if(!temp.getSuffixDomain().equals("")){
    //                arrIp[i][0] = arrIP.keySet().toString();
    //                arrIp[i][1] = temp.getSuffixDomain();
    //                i++;
    //            }
    //        }
    //        mainThread.setArrIP(arrIp,OtherIP);
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        // TODO add your handling code here:
        if(this.domainSuffixTxt.getText().trim().equals(""))
            JOptionPane.showMessageDialog(this, "Bạn vui lòng nhập vào hậu tố domain (VD: .com, .net, .org,...)");
        else if(!DNSLookup.checkSuffixDomain(domainSuffixTxt.getText()))
            JOptionPane.showMessageDialog(this,"Bạn vui lòng nhập lại hậu tố domain (VD: .com, .net, .org,...");
        else{
            
            arrIP.put(chooseIP,this.domainSuffixTxt.getText());
            flagCheck=true;
            jButton5.setEnabled(true);
        }
    }//GEN-LAST:event_saveButtonActionPerformed

    private void IPComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_IPComboBoxActionPerformed
        // TODO add your handling code here:
//        chooseIP = this.IPComboBox.getSelectedItem().toString();
//        Concept temp = arrIP.get(chooseIP);
//        String value = temp.getValue();
//        if(value!=null||!value.equals("")){
//            this.domainSuffixTxt.setText(value);
//            System.out.println("Gia tri : " + value);
//        }
//        if (temp.getSuffixDomain() != null) {
//            System.out.println("suffiedomain: " + temp.getSuffixDomain());
//        }
    }//GEN-LAST:event_IPComboBoxActionPerformed

    private void IPComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_IPComboBoxItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            chooseIP=IPComboBox.getSelectedItem().toString(); 
            
            domainSuffixTxt.setText(arrIP.get(chooseIP));
            
            
            NameOfComputerlbl.setText(arrNamePC.get(chooseIP));
            
            //Concept concept=(Concept)arrIP.get(chooseIP);
            
            //showInfo(concept);
            //System.out.println("kda: "+concept.toString());
            
//            if(concept.getSuffixDomain()==null||concept.getSuffixDomain().equals(""))
//                domainSuffixTxt.setText("");
//            else
                //domainSuffixTxt.setText(concept.getSuffixDomain());
            //Concept t=(Concept)temp;
//            System.out.println("Concept: "+t.getSuffixDomain()+" - "+t.getValue());
//            chooseIP = this.IPComboBox.getSelectedItem().toString();
//            Concept temp = arrIP.get(chooseIP);
//            String value = temp.getValue();
//            if (value != null || !value.equals("")) {
//                this.domainSuffixTxt.setText(value);
//                System.out.println("Gia tri : " + value);
//            }
//            if (temp.getSuffixDomain() != null) {
//                System.out.println("suffiedomain: " + temp.getSuffixDomain());
//            }
        }
    }//GEN-LAST:event_IPComboBoxItemStateChanged

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        dnsThread.disableFlagDistributedServer();
        JOptionPane.showMessageDialog(app, "Đã loại bỏ hệ thống phân tán DNS");
        dispose();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void checkBoxOpenPortItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_checkBoxOpenPortItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_checkBoxOpenPortItemStateChanged

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void rMIcheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rMIcheckBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rMIcheckBoxActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ChangeDNS.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ChangeDNS.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ChangeDNS.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ChangeDNS.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ChangeDNS dialog = new ChangeDNS(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField IPAddressTxt;
    private javax.swing.JComboBox<String> IPComboBox;
    private javax.swing.JTextField NameOfComputerlbl;
    private javax.swing.JButton broadCastButton;
    private javax.swing.JTextField broadCastIPTxt;
    private java.awt.Checkbox checkBoxOpenPort;
    private javax.swing.JTextField domainSuffixTxt;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JCheckBox rMIcheckBox;
    private javax.swing.JButton saveButton;
    // End of variables declaration//GEN-END:variables
}
