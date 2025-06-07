package lk.jiat.ee.core.dao;

import lk.jiat.ee.core.dto.AuctionItemDTO;

import java.util.ArrayList;
import java.util.List;

public class AuctionItemDAO {
    private static final List<AuctionItemDTO> auctions = new ArrayList<>();
    private static int idCounter = 1;

    public static AuctionItemDTO getAuction(int id) {
        return auctions.stream()
                .filter(auction -> auction.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public static List<AuctionItemDTO> getAllAuctions() {
        return new ArrayList<>(auctions);
    }

    public static AuctionItemDTO auctionCreate(AuctionItemDTO auction) {
        auction.setId(idCounter++);
        auctions.add(auction);
        return auction;
    }

}
