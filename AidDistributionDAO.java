package dao;

import model.AidDistribution;
import utils.DatabaseConnection;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AidDistributionDAO {
    
    // Get all distributions with names
    public List<AidDistribution> getAllDistributions() {
        List<AidDistribution> distributions = new ArrayList<>();
        String sql = "SELECT ad.*, f.head_name as family_name, o.org_name, u.full_name as user_name " +
                     "FROM aid_distributions ad " +
                     "JOIN families f ON ad.family_id = f.family_id " +
                     "JOIN organizations o ON ad.org_id = o.org_id " +
                     "JOIN users u ON ad.user_id = u.user_id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                AidDistribution dist = new AidDistribution();
                dist.setDistId(rs.getInt("dist_id"));
                dist.setFamilyId(rs.getInt("family_id"));
                dist.setOrgId(rs.getInt("org_id"));
                dist.setUserId(rs.getInt("user_id"));
                dist.setAidType(rs.getString("aid_type"));
                dist.setDistributionDate(rs.getDate("distribution_date").toLocalDate());
                dist.setFamilyName(rs.getString("family_name"));
                dist.setOrgName(rs.getString("org_name"));
                dist.setUserName(rs.getString("user_name"));
                distributions.add(dist);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return distributions;
    }
    
    // Check duplicate aid within last 30 days
    public boolean hasReceivedAidWithin30Days(int familyId, int orgId, String aidType, LocalDate currentDate) {
        LocalDate thirtyDaysAgo = currentDate.minusDays(30);
        String sql = "SELECT COUNT(*) FROM aid_distributions WHERE family_id = ? AND org_id = ? AND aid_type = ? AND distribution_date >= ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, familyId);
            pstmt.setInt(2, orgId);
            pstmt.setString(3, aidType);
            pstmt.setDate(4, Date.valueOf(thirtyDaysAgo));
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Add new distribution
    public boolean addDistribution(AidDistribution dist) {
        String sql = "INSERT INTO aid_distributions (family_id, org_id, user_id, aid_type, distribution_date) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, dist.getFamilyId());
            pstmt.setInt(2, dist.getOrgId());
            pstmt.setInt(3, dist.getUserId());
            pstmt.setString(4, dist.getAidType());
            pstmt.setDate(5, Date.valueOf(dist.getDistributionDate()));
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Get families served by specific organization
    public int getFamiliesServedByOrg(int orgId) {
        String sql = "SELECT COUNT(DISTINCT family_id) FROM aid_distributions WHERE org_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, orgId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }
}