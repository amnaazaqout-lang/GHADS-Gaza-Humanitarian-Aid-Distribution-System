package model;

import java.time.LocalDate;

public class AidDistribution {
    private int distId;
    private int familyId;
    private int orgId;
    private int userId;
    private String aidType;
    private LocalDate distributionDate;
    
    // For display with names
    private String familyName;
    private String orgName;
    private String userName;
    
    public AidDistribution() {}
    
    public AidDistribution(int distId, int familyId, int orgId, int userId, String aidType, LocalDate distributionDate) {
        this.distId = distId;
        this.familyId = familyId;
        this.orgId = orgId;
        this.userId = userId;
        this.aidType = aidType;
        this.distributionDate = distributionDate;
    }
    
    // Getters and Setters
    public int getDistId() { return distId; }
    public void setDistId(int distId) { this.distId = distId; }
    
    public int getFamilyId() { return familyId; }
    public void setFamilyId(int familyId) { this.familyId = familyId; }
    
    public int getOrgId() { return orgId; }
    public void setOrgId(int orgId) { this.orgId = orgId; }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public String getAidType() { return aidType; }
    public void setAidType(String aidType) { this.aidType = aidType; }
    
    public LocalDate getDistributionDate() { return distributionDate; }
    public void setDistributionDate(LocalDate distributionDate) { this.distributionDate = distributionDate; }
    
    public String getFamilyName() { return familyName; }
    public void setFamilyName(String familyName) { this.familyName = familyName; }
    
    public String getOrgName() { return orgName; }
    public void setOrgName(String orgName) { this.orgName = orgName; }
    
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
}