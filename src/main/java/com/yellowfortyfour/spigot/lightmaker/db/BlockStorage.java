package com.yellowfortyfour.spigot.lightmaker.db;

import com.yellowfortyfour.spigot.lightmaker.exceptions.NoPendingButtonException;
import com.yellowfortyfour.spigot.lightmaker.exceptions.NoSuchPlayerException;

import org.bukkit.plugin.Plugin;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.Material;

import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.io.File;


public class BlockStorage extends SqLite {
  private HashMap<UUID, ArrayList<Integer>> pendingButtons = new HashMap<>();

  private final String sqlCreateButtonTable = "CREATE TABLE if not exists buttons (\n"
                                            + "BUTTON_ID  STRING UNIQUE ON CONFLICT FAIL NOT NULL ON CONFLICT FAIL PRIMARY KEY,\n"
                                            + "LOCATION_X INTEGER NOT NULL ON CONFLICT IGNORE,\n"
                                            + "LOCATION_Y INTEGER NOT NULL ON CONFLICT FAIL,\n"
                                            + "LOCATION_Z INTEGER NOT NULL ON CONFLICT FAIL,\n"
                                            + "WORLD      STRING  NOT NULL ON CONFLICT FAIL,\n"
                                            + "PLAYER     INTEGER );";

  private final String sqlCreateBulbTable = "CREATE TABLE if not exists bulbs (\n"
                                            + "BUTTON_ID STRING NOT NULL ON CONFLICT FAIL,\n"
                                            + "BULB_ID   INTEGER NOT NULL ON CONFLICT FAIL);";

  

  public BlockStorage(Plugin plugin, File dbFile) {
    super(dbFile);
    createNewTable(sqlCreateButtonTable);
    createNewTable(sqlCreateBulbTable);
  }

  /**
   * This function stash a list of bublb ids on a player for later use when the button is placed.
   * N.B. Will replace old "command" without any notice!
   * 
   * TODO: check that bulbs are valid? Return/throw error if not?
   * 
   */
  public void createButton(UUID playerId, ArrayList<Integer> bulbs) {
    if(!bulbs.isEmpty())
      pendingButtons.put(playerId, bulbs);
  }

  public Boolean finalizeButton(UUID playerId, Block block) throws NoSuchPlayerException, NoPendingButtonException {
    ArrayList<Integer> bulbs = pendingButtons.remove(playerId);
    if(bulbs == null) {
      // i.e. we do NOT have pending button to finalize. How did we end up here?
      throw new NoSuchPlayerException("Player id: " + playerId.toString() + " has no pending buttion creation.");    
    }
    
    Material blockMaterial = block.getType();
    
    if(blockMaterial != Material.STONE_BUTTON && blockMaterial != Material.WOOD_BUTTON)
      throw new NoPendingButtonException("Block of type " + blockMaterial + " not suppored");
    
    Location blockPosition = block.getLocation();
    String blockWorld = block.getWorld().getName();
    String buttonId = UUID.randomUUID().toString();

    insertButtonIntoDb(buttonId, blockPosition, blockWorld, playerId.toString());

    for(Integer b: bulbs) {
      insertBublIntoDb(buttonId, b);
    }
    return true;
  }

  public Boolean hasPendingButton(UUID playerId)
  {
    return pendingButtons.containsKey(playerId);
  }

  private void insertButtonIntoDb(String buttonId, Location blockPosition, String world, String playerId) {
    getButtonIdsAtPos(blockPosition, world).forEach(b -> {
      deleteButton(b);
    });
      
    String sql = "REPLACE INTO buttons(BUTTON_ID, LOCATION_X, LOCATION_Y, LOCATION_Z, WORLD, PLAYER) VALUES(?,?,?,?,?,?)";
    
    try (PreparedStatement pstmt = connectDb().prepareStatement(sql)) {
        pstmt.setString(1, buttonId);
        pstmt.setInt(2, blockPosition.getBlockX());
        pstmt.setInt(3, blockPosition.getBlockY());
        pstmt.setInt(4, blockPosition.getBlockZ());
        pstmt.setString(5, world);
        pstmt.setString(6, playerId);
        pstmt.executeUpdate();
    } catch (SQLException e) {
        System.out.println(e.getMessage());
    }

  }

  private void insertBublIntoDb(String buttonId, Integer bulbId){
    String sql = "REPLACE INTO bulbs(BUTTON_ID, BULB_ID) VALUES(?,?)";
    
    try (PreparedStatement pstmt = connectDb().prepareStatement(sql)) {
        pstmt.setString(1, buttonId);
        pstmt.setInt(2, bulbId);
        pstmt.executeUpdate();
    } catch (SQLException e) {
        System.out.println(e.getMessage());
    }
  }

  public ArrayList<String> getBulbsIdsAtPos(Location location, String world){
    String sql = "select bulbs.BULB_ID from buttons inner join bulbs on buttons.BUTTON_ID = bulbs.BUTTON_ID \n"
      + "where buttons.WORLD = ? and buttons.LOCATION_X = ? and buttons.LOCATION_Y = ? and buttons.LOCATION_Z = ? ";
    ArrayList<String> bulbIds = new ArrayList<>();
    try(PreparedStatement pstmt  = connectDb().prepareStatement(sql)){
      pstmt.setString(1, world);
      pstmt.setInt(2, location.getBlockX());
      pstmt.setInt(3, location.getBlockY());
      pstmt.setInt(4, location.getBlockZ());

      ResultSet rs  = pstmt.executeQuery();
  
      // loop through the result set
      while (rs.next()) {
        bulbIds.add(rs.getString("BULB_ID"));
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return bulbIds;
  }

  public ArrayList<String> getButtonIdsAtPos(Location location, String world){
    String sql = "select button_id from buttons where WORLD = ? and LOCATION_X = ? and LOCATION_Y = ? and LOCATION_Z = ?";
    ArrayList<String> buttonIds = new ArrayList<>();

    try(PreparedStatement pstmt  = connectDb().prepareStatement(sql)){
            // set the value
            pstmt.setString(1, world);
            pstmt.setInt(2, location.getBlockX());
            pstmt.setInt(3, location.getBlockY());
            pstmt.setInt(4, location.getBlockZ());

            ResultSet rs  = pstmt.executeQuery();
        
        // loop through the result set
        while (rs.next()) {
            buttonIds.add(rs.getString("button_id"));
        }
    } catch (SQLException e) {
        System.out.println(e.getMessage());
    }
    return buttonIds;
  }

  public ArrayList<String> getButtonIds(String world){
    String sql = "select button_id from buttons where WORLD = ?";
    ArrayList<String> buttonList = new ArrayList<>();

    try(PreparedStatement pstmt  = connectDb().prepareStatement(sql)){
        pstmt.setString(1, world);

        ResultSet rs  = pstmt.executeQuery();
        
        while (rs.next()) {
          buttonList.add(rs.getString("button_id"));
        }
    } catch (SQLException e) {
        System.out.println(e.getMessage());
    }
    return buttonList;
  }

  public HashMap<String, Integer> getButtonLocation(String world, String buttonId) {
    String sql = "select LOCATION_X, LOCATION_Y, LOCATION_Z from buttons where BUTTON_ID = ? and WORLD = ?";
    HashMap<String, Integer> location = new HashMap<>();

    try(PreparedStatement pstmt  = connectDb().prepareStatement(sql)){
        // set the value
        pstmt.setString(1, buttonId);
        pstmt.setString(2, world);

        ResultSet rs  = pstmt.executeQuery();

        // loop through the result set
        int x = rs.getInt("LOCATION_X");
        int y = rs.getInt("LOCATION_Y");
        int z = rs.getInt("LOCATION_Z");
        location.put("x", x);
        location.put("y", y);
        location.put("z", z);
    } catch (SQLException e) {
        System.out.println(e.getMessage());
    }
    return location;
  }


  public void deleteBulbsAtButton(String buttonId) {
    String sql = "DELETE FROM bulbs WHERE BUTTON_ID = ?";

    try (PreparedStatement pstmt = connectDb().prepareStatement(sql)) {
        pstmt.setString(1, buttonId);
        pstmt.executeUpdate();

    } catch (SQLException e) {
        System.out.println(e.getMessage());
    }

  }
  public void deleteButton(String buttonId) {
    deleteBulbsAtButton(buttonId);

    String sql = "DELETE FROM buttons WHERE BUTTON_ID = ?";

    try (PreparedStatement pstmt = connectDb().prepareStatement(sql)) {
        pstmt.setString(1, buttonId);
        pstmt.executeUpdate();

    } catch (SQLException e) {
        System.out.println(e.getMessage());
    }
  }
  
}