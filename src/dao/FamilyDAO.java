package dao;

import model.Family;
import utils.DatabaseConnection;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FamilyDAO {
    
    public List<Family> getAllFamilies() {
        List<Family> families = new ArrayList<>();
        String sql = "SELECT * FROM families";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Family family = new Family();
                family.setFamilyId(rs.getInt("family_id"));
                family.setNationalId(rs.getString("national_id"));
                family.setHeadName(rs.getString("head_name"));
                family.setPhone(rs.getString("phone"));
                family.setAddress(rs.getString("address"));
                family.setVulnerabilityLevel(rs.getString("vulnerability_level"));
                family.setRegistrationDate(rs.getDate("registration_date").toLocalDate());
                families.add(family);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return families;
    }
    
    // Check if family exists by national ID
    public boolean familyExists(String nationalId) {
        String sql = "SELECT COUNT(*) FROM families WHERE national_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, nationalId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean addFamily(Family family) {
        String sql = "INSERT INTO families (national_id, head_name, phone, address, vulnerability_level, registration_date) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, family.getNationalId());
            pstmt.setString(2, family.getHeadName());
            pstmt.setString(3, family.getPhone());
            pstmt.setString(4, family.getAddress());
            pstmt.setString(5, family.getVulnerabilityLevel());
            pstmt.setDate(6, Date.valueOf(family.getRegistrationDate()));
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateFamily(Family family) {
        String sql = "UPDATE families SET national_id = ?, head_name = ?, phone = ?, address = ?, vulnerability_level = ?, registration_date = ? WHERE family_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, family.getNationalId());
            pstmt.setString(2, family.getHeadName());
            pstmt.setString(3, family.getPhone());
            pstmt.setString(4, family.getAddress());
            pstmt.setString(5, family.getVulnerabilityLevel());
            pstmt.setDate(6, Date.valueOf(family.getRegistrationDate()));
            pstmt.setInt(7, family.getFamilyId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteFamily(int familyId) {
        String sql = "DELETE FROM families WHERE family_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, familyId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Get statistics for dashboard
    public int getTotalFamilies() {
        String sql = "SELECT COUNT(*) FROM families";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    public void updateAidStatus(int familyId, boolean b) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}