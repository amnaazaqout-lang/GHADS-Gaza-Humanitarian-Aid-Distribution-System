package model;

public class AidType {

    private int aidTypeId;
    private String aidName;

    public AidType() {
    }

    public AidType(int aidTypeId, String aidName) {
        this.aidTypeId = aidTypeId;
        this.aidName = aidName;
    }

    public int getAidTypeId() {
        return aidTypeId;
    }

    public void setAidTypeId(int aidTypeId) {
        this.aidTypeId = aidTypeId;
    }

    public String getAidName() {
        return aidName;
    }

    public void setAidName(String aidName) {
        this.aidName = aidName;
    }
}