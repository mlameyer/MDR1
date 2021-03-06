/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

/**
 *
 * @author Administrator
 */
public class Template39 
{
    private final String tableName;
    private final ArrayList colData;
    private final Properties prop;
    private final boolean option;

    public Template39(String tableName, ArrayList colData, Properties prop, boolean option) 
    {
        this.tableName = tableName;
        this.colData = colData;
        this.prop = prop;
        this.option = option;
    }

    public void execute() 
    {
        GetConnection connect = new GetConnection();
        connect.setJDBC_CONNECTION_URL(prop.getProperty("JDBC_CONNECTION_URL"));
        connect.setMySQLDB_URL(prop.getProperty("MySQLDB_URL"));
        connect.setMySQLUser(prop.getProperty("MySQLUser"));
        connect.setMySQLPassword(prop.getProperty("MySQLPassword"));
        
        Connection conn = connect.getConnection();
        PreparedStatement stmt = null;
        PreparedStatement seqnumInsert = null;
        
        UnixtoStandardTime time = new UnixtoStandardTime();
        BigDecimal tag52 = time.convert(colData.get(1).toString());
        BigDecimal tag60 = time.convert(colData.get(4).toString());
        
        try 
        {
            if(option)
            {
                seqnumInsert = conn.prepareStatement("INSERT INTO `cme_market_datafeed`.`sequencenumbers_options`(`Seq_Num_34`,`Sending_Time_52`,`TempId`)VALUES(?,?,?)");
            }
            else
            {
                seqnumInsert = conn.prepareStatement("INSERT INTO `cme_market_datafeed`.`sequencenumbers_futures`(`Seq_Num_34`,`Sending_Time_52`,`TempId`)VALUES(?,?,?)");
            }
            seqnumInsert.setInt(1, Integer.parseInt(colData.get(0).toString()));
            seqnumInsert.setBigDecimal(2, tag52);
            seqnumInsert.setInt(3, Integer.parseInt(colData.get(2).toString()));
            seqnumInsert.executeUpdate();
            
            conn.setAutoCommit(false);
            stmt = conn.prepareStatement(tableName);
            
            for(int i = 7; i < colData.size(); i = i + 5)
            {
                stmt.setInt(1, Integer.parseInt(colData.get(0).toString()));
                stmt.setBigDecimal(2, tag52);
                stmt.setInt(3, Integer.parseInt(colData.get(2).toString()));
                stmt.setString(4, colData.get(3).toString());
                stmt.setBigDecimal(5, tag60);
                stmt.setString(6, colData.get(5).toString());
                stmt.setString(7, colData.get(6).toString());
                
                if(colData.get(i + 0) == null)//
                {
                    stmt.setInt(8, java.sql.Types.VARCHAR);//price
                    stmt.setInt(9, java.sql.Types.INTEGER);//#oforders
                    stmt.setInt(10, java.sql.Types.INTEGER);//pricelevel
                    stmt.setInt(11, java.sql.Types.INTEGER);//upaction
                    stmt.setInt(12, java.sql.Types.INTEGER);//entrytype
                }
                else
                {
                    stmt.setString(8, colData.get(i + 0).toString());
                    stmt.setInt(9, Integer.parseInt(colData.get(i + 1).toString()));
                    stmt.setInt(10, Integer.parseInt(colData.get(i + 2).toString()));
                    stmt.setInt(11, Integer.parseInt(colData.get(i + 3).toString()));
                    stmt.setInt(12, Integer.parseInt(colData.get(i + 4).toString()));
                }
                
                stmt.addBatch();
                //System.out.println(stmt);
            }
            
            stmt.executeBatch();
            conn.commit();
        } 
        catch (SQLException ex) 
        {
            System.out.println("Class database.Template32 error: " + ex);
        }
        finally
        {
            try 
            {
                seqnumInsert.close();
                stmt.close();
                conn.close();
            } 
            catch (SQLException ex) 
            {
                System.out.println("Class database.Template32 error: " + ex);
            }
        }
    }
    
}
