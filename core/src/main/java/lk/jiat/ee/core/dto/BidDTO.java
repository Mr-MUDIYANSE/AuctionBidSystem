package lk.jiat.ee.core.dto;

import java.io.Serializable;

public class BidDTO implements Serializable {
    private int id;
    private UserDTO userId;
    private AuctionItemDTO auctionId;
    private int price;

    public BidDTO() {
    }

    public BidDTO(int is, UserDTO userId, AuctionItemDTO auctionId, int price) {
        this.id = is;
        this.userId = userId;
        this.auctionId = auctionId;
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

    public AuctionItemDTO getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(AuctionItemDTO auctionId) {
        this.auctionId = auctionId;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
