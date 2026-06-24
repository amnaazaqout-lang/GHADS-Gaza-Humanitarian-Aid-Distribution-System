package model;

import java.time.LocalDate;

public class Family {
    private int familyId;
    private String nationalId;
    private String headName;
    private String phone;
    private String address;
    private String vulnerabilityLevel; // HIGH, MEDIUM, LOW
    private LocalDate registrationDate;
    
    public Family() {}
    
    public Family(int familyId, String nationalId, String headName, String phone, String address, 
                  String vulnerabilityLevel, LocalDate registrationDate) {
        this.familyId = familyId;
        this.nationalId = nationalId;
        this.headName = headName;
        this.phone = phone;
        this.address = address;
        this.vulnerabilityLevel = vulnerabilityLevel;
        this.registrationDate = registrationDate;
    }
    
    // Getters and Setters
    public int getFamilyId() { return familyId; }
    public void setFamilyId(int familyId) { this.familyId = familyId; }
    
    public String getNationalId() { return nationalId; }
    public void setNationalId(String nationalId) { this.nationalId = nationalId; }
    
    public String getHeadName() { return headName; }
    public void setHeadName(String headName) { this.headName = headName; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public String getVulnerabilityLevel() { return vulnerabilityLevel; }
    public void setVulnerabilityLevel(String vulnerabilityLevel) { this.vulnerabilityLevel = vulnerabilityLevel; }
    
    public LocalDate getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDate registrationDate) { this.registrationDate = registrationDate; }

    public void setHeadOfFamily(String text) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void setMemberCount(int parseInt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void setRegion(String text) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getHeadOfFamily() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Object getMemberCount() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getRegion() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean isReceivedAid() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void setReceivedAid(boolean b) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}