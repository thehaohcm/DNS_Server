/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

/**
 *
 * @author DELL
 */
public class ClearAllRecordThread implements Runnable {

    @Override
    public void run() {
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
