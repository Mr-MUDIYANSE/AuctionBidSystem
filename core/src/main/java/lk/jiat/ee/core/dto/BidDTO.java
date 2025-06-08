package lk.jiat.ee.core.dto;

import java.io.Serializable;

public class BidDTO implements Serializable {
    private int id;
    private UserDTO userId;
    private  AuctionItemDTO auctionItemId;
    private int price;

    public BidDTO() {
    }

    public BidDTO(int id, UserDTO userId, AuctionItemDTO auctionItemId, int price) {
        this.id = id;
        this.userId = userId;
        this.auctionItemId = auctionItemId;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserDTO getUserId() {
        return userId;
    }

        public void setUserId(UserDTO userId) {
        this.userId = userId;
    }

    public AuctionItemDTO getAuctionItemId() {
        return auctionItemId;
    }

    public void setAuctionItemId(AuctionItemDTO auctionItemId) {
        this.auctionItemId = auctionItemId;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
