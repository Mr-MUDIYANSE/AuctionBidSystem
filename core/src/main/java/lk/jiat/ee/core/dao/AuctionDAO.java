package lk.jiat.ee.core.dao;

import lk.jiat.ee.core.dto.AuctionDTO;

import java.util.ArrayList;
import java.util.List;

public class AuctionDAO {
    private static final List<AuctionDTO> auctions = new ArrayList<>();
    private static int idCounter = 1;

    public static AuctionDTO getAuction(int id) {
        return auctions.stream()
                .filter(auction -> auction.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public static List<AuctionDTO> getAllAuctions() {
        return new ArrayList<>(auctions);
    }

    public static AuctionDTO auctionCreate(AuctionDTO auction) {
        auction.setId(idCounter++);
        auctions.add(auction);
        return auction;
    }

}
