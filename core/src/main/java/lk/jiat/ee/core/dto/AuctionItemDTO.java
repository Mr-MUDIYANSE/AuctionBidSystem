package lk.jiat.ee.core.dto;

import java.io.Serializable;
import java.util.List;

public class AuctionItemDTO implements Serializable {
    private int id;
    private String itemName;
    private int startingPrice;
    private long startTime;
    private long endTime;
    private boolean active;
    private List<BidDTO> bids;

    public AuctionItemDTO() {
    }

    public AuctionItemDTO(int id, String itemName, int startingPrice, long startTime, long endTime, boolean active) {
        this.id = id;
        this.itemName = itemName;
        this.startingPrice = startingPrice;
        this.startTime = startTime;
        this.endTime = endTime;
        this.active = active;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getStartingPrice() {
        return startingPrice;
    }

    public void setStartingPrice(int startingPrice) {
        this.startingPrice = startingPrice;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<BidDTO> getBids() {
        return bids;
    }

    public void setBids(List<BidDTO> bids) {
        this.bids = bids;
    }
}
