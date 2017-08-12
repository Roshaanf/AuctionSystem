package roshaan.auctionsystem.StructuresForDb;

/**
 * Created by Roshaann 2.7 gpa on 03/08/2017.
 */

public class AdStructure {

    String startDate;
    String endDate;
    String imageUri;
    String category;
    String title;
    String description;
    String initialBid;
    String userKey;
    String pushId;

    public AdStructure() {
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
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

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInitialBid() {
        return initialBid;
    }

    public void setInitialBid(String initialBid) {
        this.initialBid = initialBid;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public AdStructure(String startDate, String endDate, String imageUri, String category, String title, String description, String initialBid, String userKey,
    String pushId) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.imageUri = imageUri;
        this.category = category;
        this.title = title;
        this.description = description;
        this.initialBid = initialBid;
        this.userKey = userKey;
        this.pushId=pushId;

    }

    Boolean compare(AdStructure second){

        if(second.category.equals(category)&&
                second.description.equals(this.description)&&
                second.endDate.equals(this.endDate)&&
                second.title.equals(this.title)&&
                second.startDate.equals(this.startDate)&&
                second.imageUri.equals(this.imageUri)){

            return true;
        }

        return false;
    }
}
