package lk.jiat.ee.core.dao;

import lk.jiat.ee.core.dto.BidDTO;

import java.util.ArrayList;
import java.util.List;

public class BidDAO {
    private static final List<BidDTO> bids = new ArrayList<>();
    private static int idCounter = 1;

    public static BidDTO getBid(int id) {
        return bids.stream()
                .filter(bid -> bid.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public static List<BidDTO> getAllBids() {
        return new ArrayList<>(bids);
    }

    public static BidDTO bidCreate(BidDTO bid) {
        bid.setId(idCounter++);
        bids.add(bid);
        return bid;
    }
}
