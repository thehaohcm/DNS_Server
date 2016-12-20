/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import BroadCast.ReceivePacketBroadCastThread;
import Database.Database;
import Model.DNSRecord;
import Enum.DNSEnum;
import dns.DNSLookup;
import dns.DNSThread;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;
import java.io.File;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author thehaohcm
 */
public class DNSApplication extends javax.swing.JFrame {

    /**
     * Creates new form DNSApplication
     */
    Thread mainThread=null;
    Connection conn=null;
    static InetAddress myIP=null;
    
    DNSThread dnsThread=null; //DNSThread dnsThread=null;
    
    Thread receiveBroadCastThread=null;
    ReceivePacketBroadCastThread receiveBroadCast=null;
    
    public DNSApplication() {
        initComponents();
        
        centreWindow(this);

        //Xóa toàn bộ record trong bảng dnsrecord
        try{
            conn = DriverManager.getConnection(Database.linkDatabaseServer, Database.username,Database.password);
            String query = "SELECT ip FROM DNSRecord";
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            ResultSet rs = preparedStmt.executeQuery();
            if (rs.next()) {
                int confirmed = JOptionPane.showConfirmDialog(this,"Bạn có muốn xóa toàn bộ record trong bảng DNSRecord?", "Xác nhận xóa record",JOptionPane.YES_NO_OPTION);
                if (confirmed == JOptionPane.YES_OPTION)
                    DNSLookup.clearAllRecord();
            }
        }catch(Exception ex){
            
        }
        
        try{
            if(conn!=null)
                conn.close();
            conn = DriverManager.getConnection(Database.linkDatabaseServer, Database.username,Database.password);
            getDNSRecord();
        }catch(Exception ex){
            JOptionPane.showMessageDialog(this, "Không thể kết nối đến cơ sở dữ liệu, bạn không thể truy cập vào phần mềm");
            System.exit(0);
        }
        
        dnsThread=new DNSThread(this);
        mainThread=new Thread(dnsThread);
        mainThread.start();
        
        startThreadReceiveBroadCast();
        
        TableColumnModel colMdl1 = jTable1.getColumnModel();
        colMdl1.getColumn(0).setPreferredWidth(1);
        
        TableColumnModel colMdl2 = jTable2.getColumnModel();
        colMdl2.getColumn(1).setPreferredWidth(45);
        colMdl2.getColumn(2).setPreferredWidth(45);
        
        try{
            this.myIP=DNSLookup.getMyIPAddress();
            jLabel2.setText("Địa chỉ IP: "+myIP.toString().substring(1));
            jLabel3.setText("DNS Server: "+dnsThread.getDNSServerAddress());
            
        }catch(Exception ex){
            jLabel2.setText("Địa chỉ IP: Không thể lấy địa chỉ IP của máy");
        }
    }
    
    public void centreWindow(Window frame) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
    }
    
    public void getDNSRecord() throws SQLException{
        ArrayList<DNSRecord> arr=new ArrayList<DNSRecord>();
        arr=DNSLookup.getDNSRecords(conn);
        if(arr.size()>0){
            String col[] = {"ID","Domain","IP Address"};
            DefaultTableModel tableModel = new DefaultTableModel(col, 0); //số 0 là giá trị hàng hiện tại
            jTable1.setModel(tableModel);
            
            for(DNSRecord record:arr){
                Object[] objs = {record.getID(),record.getDomain(),record.getIP()};
                tableModel.addRow(objs);
            }
            jTable1.changeSelection(jTable1.getRowCount() - 1, 0, false, false);
        }
    }
    
    public void getInfo(InetAddress sourceIp,InetAddress destinationIp, DNSEnum en){//,int k){//InetAddress sourceIp){
        try{
            String content="";
            InetAddress dnsserver = InetAddress.getByName(dnsThread.getDNSServerAddress());
//            System.out.println("sourceIp: "+sourceIp);
//            System.out.println("DNSServer: "+dnsserver);
//            System.out.println("myIP: "+myIP);
            
            if(sourceIp.equals(dnsserver)){
                if(destinationIp.equals(myIP))
                    content="Đã nhận từ DNS Server";
            }else if(sourceIp.equals(myIP)){
                if(destinationIp.equals(dnsserver))
                    content="Đã gửi đến DNS Server";
                else
                    content="Đã gửi về Client";
            }else if(en.equals(DNSEnum.RootServerToSlaveServer)){
                if(dnsThread.statusFlagRMIforRootServer()){
                    content="Đã gửi RMI cho Slave Server ";
                }else if(dnsThread.statusFlagRMIfortSlaveServer()){
                    content="Đã nhận RMI từ Root Server";
                }
            }
            else if(en.equals(DNSEnum.ReceiveNameFromSlaveServer)){
                content="Đã nhận trả lời từ Slave Server";
            }
            else if(en.equals(DNSEnum.RootServerSendBroadCast)){
                content="Đã gửi gói kiểm tra cho toàn bộ mạng";
            }
            else if(en.equals(DNSEnum.SendNameToRootServer)){
                content="Đã gửi tên cho Root Server";
            }
            else if(en.equals(DNSEnum.SlaveServerReceiveBroadCast)){
                content="Đã nhận gói BroadCast từ Root Server";
            }
            else{
                content="Đã nhận từ Client";
            }
//            if(sourceIp.equals(dnsserver))
//                content="Đã nhận từ DNS server.";
//            else if(sourceIp.equals(myIP))
//                content="Đã nhận từ Client.";
//            else{
//                if(en.equals(DNSEnum.ServerToClient))
//                    content="Đã gửi về Client.";
//                else if(en.equals(DNSEnum.ClientToServer))
//                    content="Đã gửi đến DNS Server";
//                else
//                    content="bla bla bla";
//            }
            if(sourceIp.getAddress().length!=0){
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	   //get current date time with Date()
                //Date date = new Date();
                Calendar cal = Calendar.getInstance();
	   //System.out.println(dateFormat.format(cal.getTime()));
           
                DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
                model.addRow(new Object[]{dateFormat.format(cal.getTime()),sourceIp.toString().substring(1),destinationIp.toString().substring(1), content});
                jTable2.changeSelection(jTable2.getRowCount() - 1, 0, false, false);
            }
        }catch(Exception e){
            
        }
    }
    
    public void setChangeIP(){
        try{
            this.myIP=DNSLookup.getMyIPAddress();
            jLabel2.setText("Địa chỉ IP: "+myIP.toString().substring(1));
            
        }
        catch(Exception ex){
                
        }
    }
    
    public void changeDNSServer(String dnsname){
        jLabel3.setText("DNS Server: "+dnsname);
    }
    
    public void stopThreadReceiveBroadCast(){
        if(receiveBroadCast!=null||receiveBroadCastThread!=null){
            receiveBroadCast.closeConnect();
            receiveBroadCastThread.stop();
            receiveBroadCast=null;
            receiveBroadCastThread=null;
        }
    }
    
    public void startThreadReceiveBroadCast(){
        receiveBroadCast=new ReceivePacketBroadCastThread(this,dnsThread);
        receiveBroadCastThread=new Thread(receiveBroadCast);
        receiveBroadCastThread.start();
    }
    
    public boolean checkStatusThreadReceiveBroadCast(){
        if(receiveBroadCast!=null||receiveBroadCastThread!=null)
            return true;
        return false;
    }
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        popupMenu1 = new java.awt.PopupMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        jLabel1 = new javax.swing.JLabel();
        jTabbedPane4 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItem1 = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        jMenuItem5 = new javax.swing.JMenuItem();

        popupMenu1.setLabel("popupMenu1");

        jMenuItem2.setText("jMenuItem2");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Domain Name System Management Application");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabel1.setText("Server đã mở cổng 53 - DNS");

        jTabbedPane4.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "ID", "Domain", "IP Address"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jButton6.setText("Thêm tên miền");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setText("Sửa tên miền");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setText("Xóa tên miền");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton4.setText("Cập nhật");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jButton6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton8)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(1, 1, 1)))
                .addContainerGap())
        );

        jTabbedPane4.addTab("Bảng dữ liệu Domain", jPanel1);

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Thời gian", "Địa chỉ IP nguồn", "Địa chỉ IP đích", "Hành động"
            }
        ));
        jScrollPane2.setViewportView(jTable2);

        jButton1.setText("Lưu file text...");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton5.setText("Xóa tất cả");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 558, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5))
                .addContainerGap())
        );

        jTabbedPane4.addTab("Nhật ký hoạt động", jPanel2);

        jButton2.setText("Thoát khỏi chương trình");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Chỉnh sửa DNS");
        jButton3.setToolTipText("");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel2.setText("Địa chỉ IP:");

        jLabel3.setText("DNS Server: 8.8.8.8");

        jMenu1.setText("File");

        jMenuItem4.setText("Lưu file text nhật ký hoạt động");
        jMenu1.add(jMenuItem4);

        jMenuItem3.setText("Chỉnh sửa DNS...");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);
        jMenu1.add(jSeparator1);

        jMenuItem1.setText("Giới thiệu về chương trình");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);
        jMenu1.add(jSeparator2);

        jMenuItem5.setText("Thoát khỏi chương trình");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem5);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jTabbedPane4, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton3))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton2)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addGap(7, 7, 7)
                .addComponent(jTabbedPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        int confirmed = JOptionPane.showConfirmDialog(this, "Bạn có thật sự muốn xóa?","Xác nhận",JOptionPane.YES_NO_OPTION);
        if(confirmed == JOptionPane.YES_OPTION){
            int selectedRowIndex = jTable1.getSelectedRow();
            if(selectedRowIndex>-1){
                int id=(int)jTable1.getModel().getValueAt(selectedRowIndex, 0);
                try {
                    boolean check=DNSLookup.removeDomain(conn, id);
                    if(check){
                        jButton4.doClick();
                    }else{
                        JOptionPane.showMessageDialog(this, "Không thể xóa thành công nội dung trong cơ sở dữ liệu");
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Không thể xóa thành công nội dung trong cơ sở dữ liệu");
                }
            }
        }
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        ChangeDNS changfr=new ChangeDNS(this,true,this,dnsThread);
        changfr.setVisible(true);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        if(jTable1.getRowCount()==0)
        {
            JOptionPane.showMessageDialog(null, "Không có bảng giá trị nào hiện thời");
            return;
        }
        JFileChooser fileChooser = new JFileChooser();
        FileFilter filter = new FileNameExtensionFilter("Text File (*.txt)", "txt"); 
        fileChooser.setFileFilter(filter);
        //Component modalToComponent;
        int rs=fileChooser.showSaveDialog(this);//showSaveDialog(this);
        if (rs==JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String fname = file.getAbsolutePath();
            if(!fname.contains("."))
            {
                file=new File(file+".txt");
            }
            PrintWriter writer;
            try {
                writer = new PrintWriter(file, "UTF-8");
            
                for(int i=0;i<jTable2.getRowCount();i++){
                    for(int j=0;j<jTable2.getColumnCount();j++){
                        Object selectedObject = (Object) jTable2.getModel().getValueAt(i, j);
                        writer.write(selectedObject.toString()+"\t");
                    }
                    writer.println();
                }
                writer.close();
                JOptionPane.showMessageDialog(this, "Đă ghi thành công vào file "+file.toString());
            }catch(Exception ex){
                JOptionPane.showMessageDialog(this, "Không thể ghi vào file "+file.toString());
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        AddDomain addfrm=new AddDomain(this,true,this);
        addfrm.setVisible(true);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        int selectedRowIndex = jTable1.getSelectedRow();
        if(selectedRowIndex>-1){
            int id=(int)jTable1.getModel().getValueAt(selectedRowIndex, 0);
            String Domain = (String) jTable1.getModel().getValueAt(selectedRowIndex, 1);
            String IP=(String)jTable1.getModel().getValueAt(selectedRowIndex, 2);
            AddDomain addfrm=new AddDomain(this,true,this,conn,id,Domain,IP);
            addfrm.setVisible(true);
        }
        
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        //System.exit(0);
        int confirmed = JOptionPane.showConfirmDialog(this,"Bạn có thật sự muốn thoát?", "Xác nhận trước khi thoát",JOptionPane.YES_NO_OPTION);
        if (confirmed == JOptionPane.YES_OPTION)
            System.exit(0);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        
    }//GEN-LAST:event_formWindowClosing

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        try{
            getDNSRecord();
        }catch(Exception ex){
            JOptionPane.showMessageDialog(this, "Đã có lỗi xảy ra, không thể cập nhật cơ sở dữ liệu");
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
        model.setRowCount(0);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        // TODO add your handling code here:
        //System.exit(0);
        int confirmed = JOptionPane.showConfirmDialog(this,"Bạn có thật sự muốn thoát?", "Xác nhận trước khi thoát",JOptionPane.YES_NO_OPTION);
        if (confirmed == JOptionPane.YES_OPTION)
        System.exit(0);
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        About aboutfrm=new About(this,true);
        aboutfrm.setVisible(true);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // TODO add your handling code here:
        jButton3.doClick();
    }//GEN-LAST:event_jMenuItem3ActionPerformed

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
            java.util.logging.Logger.getLogger(DNSApplication.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DNSApplication.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DNSApplication.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DNSApplication.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DNSApplication().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JTabbedPane jTabbedPane4;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private java.awt.PopupMenu popupMenu1;
    // End of variables declaration//GEN-END:variables
}
