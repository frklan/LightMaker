package com.yellowfortyfour.spigot.lightmaker.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.File;

public abstract class SqLite {

  private final File dbFile;
  private Connection conn;

  public SqLite(File dbFile) {
    this.dbFile = dbFile;

    connectDb();
  }

  protected void disconnectDb() {
    try {
      if (conn != null) {
          conn.close();
          System.out.println("Connection to SQLite has been severed.");
      }
    } catch (SQLException ex) {
        System.out.println(ex.getMessage());
    }
  }

  protected Connection connectDb() {
    if(!isConnected()) {
      disconnectDb();
      this.conn = null;
      try {
          // db parameters
          String url = "jdbc:sqlite:" + dbFile;
          // create a connection to the database
          this.conn = DriverManager.getConnection(url);
          
          System.out.println("Connection to SQLite has been established.");
          
      } catch (SQLException e) {
          System.out.println(e.getMessage());
      }
    } 
    return this.conn;
  }

  protected Boolean isConnected(){
    Boolean isConnected = false;
    try {
      isConnected =  conn.isValid(1);
    } catch(Exception e){}
    
    return isConnected;
  }

  protected void createNewTable(String sql) {   
    try (Statement stmt = conn.createStatement()) {
        // create a new table
        stmt.execute(sql);
        System.out.println("Tabel created");
    } catch (SQLException e) {
        System.out.println(e.getMessage());
    }
  }
}