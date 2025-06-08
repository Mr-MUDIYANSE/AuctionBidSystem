package lk.jiat.ee.core.dao;

import lk.jiat.ee.core.dto.AuctionItemDTO;

import java.util.ArrayList;
import java.util.List;

public class AuctionItemDAO {
    private static final List<AuctionItemDTO> auctionItems = new ArrayList<>();
    private static int idCounter = 1;

    public static AuctionItemDTO getAuctionItem(int id) {
        return auctionItems.stream()
                .filter(auction -> auction.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public static List<AuctionItemDTO> getAllAuctionItems() {
        return new ArrayList<>(auctionItems);
    }

    public static AuctionItemDTO auctionCreate(AuctionItemDTO auctionItem) {
        auctionItem.setId(idCounter++);
        auctionItems.add(auctionItem);
        return auctionItem;
    }

}
