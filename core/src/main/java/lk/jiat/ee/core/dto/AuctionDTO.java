package lk.jiat.ee.core.dto;

import java.io.Serializable;
import java.util.Date;

public class AuctionDTO implements Serializable {
    private int id;
    private String itemName;
    private int startingPrice;
    private int currentPrice;
    private long startTime;
    private long endTime;
    private boolean active;

    public AuctionDTO() {
    }

    public AuctionDTO(int id, String itemName, int startingPrice, int currentPrice, long startTime, long endTime, boolean active) {
        this.id = id;
        this.itemName = itemName;
        this.startingPrice = startingPrice;
        this.currentPrice = currentPrice;
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

    public int getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(int currentPrice) {
        this.currentPrice = currentPrice;
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
}
