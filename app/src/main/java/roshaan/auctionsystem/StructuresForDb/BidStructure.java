package roshaan.auctionsystem.StructuresForDb;

/**
 * Created by Roshaann 2.7 gpa on 05/08/2017.
 */

public class BidStructure {

    String userId;
    String adId;
    String bid;
    String userEmail;
    String startDate;
    String endDate;

    public BidStructure() {
    }

    public BidStructure(String userId, String adId, String bid,String userEmail,String startDate,String endDate) {
        this.userId = userId;
        this.adId = adId;
        this.bid = bid;
        this.userEmail=userEmail;
        this.startDate=startDate;
        this.endDate=endDate;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }
}
