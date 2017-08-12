package roshaan.auctionsystem.StructuresForDb;

/**
 * Created by Roshaann 2.7 gpa on 09/08/2017.
 */

public class WinnerUserStructure {

    String bidAmount;
    String title;
    String image;
    String description;

    public WinnerUserStructure() {
    }

    public WinnerUserStructure(String bidAmount, String title, String image, String description) {
        this.bidAmount = bidAmount;
        this.title = title;
        this.image = image;
        this.description = description;
    }

    public String getBidAmount() {
        return bidAmount;
    }

    public void setBidAmount(String bidAmount) {
        this.bidAmount = bidAmount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
