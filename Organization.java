package model;

/**
 * Model class representing an Organization in the GHADS system.
 */
public class Organization {
    private int orgId;
    private String orgName;
    private String orgType;
    private String orgContact;

    // المشيد الافتراضي (Constructor)
    public Organization() {
    }

    // مشيد يحتوي على جميع الحقول
    public Organization(int orgId, String orgName, String orgType, String orgContact) {
        this.orgId = orgId;
        this.orgName = orgName;
        this.orgType = orgType;
        this.orgContact = orgContact;
    }

    // --- Getters and Setters المعدلة لتطابق الـ DAO تماماً ---

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgType() {
        return orgType;
    }

    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }

    public String getOrgContact() {
        return orgContact;
    }

    public void setOrgContact(String orgContact) {
        this.orgContact = orgContact;
    }

    // دالة toString لتسهيل طباعة بيانات الكائن عند الحاجة
    @Override
    public String toString() {
        return orgName; // تعيد الاسم ليظهر بشكل صحيح داخل الـ ComboBox أو الجداول
    }
}