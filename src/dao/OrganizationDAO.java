package dao;

import model.Organization;
import utils.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrganizationDAO {
    
    public List<Organization> getAllOrganizations() {
        List<Organization> organizations = new ArrayList<>();
        String sql = "SELECT * FROM organizations";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Organization org = new Organization();
                org.setOrgId(rs.getInt("org_id"));
                org.setOrgName(rs.getString("org_name"));
                org.setOrgType(rs.getString("org_type"));
                org.setOrgContact(rs.getString("org_contact"));
                organizations.add(org);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return organizations;
    }
    
    public boolean addOrganization(Organization org) {
        String sql = "INSERT INTO organizations (org_name, org_type, org_contact) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, org.getOrgName());
            pstmt.setString(2, org.getOrgType());
            pstmt.setString(3, org.getOrgContact());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateOrganization(Organization org) {
        String sql = "UPDATE organizations SET org_name = ?, org_type = ?, org_contact = ? WHERE org_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, org.getOrgName());
            pstmt.setString(2, org.getOrgType());
            pstmt.setString(3, org.getOrgContact());
            pstmt.setInt(4, org.getOrgId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteOrganization(int orgId) {
        String sql = "DELETE FROM organizations WHERE org_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, orgId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}