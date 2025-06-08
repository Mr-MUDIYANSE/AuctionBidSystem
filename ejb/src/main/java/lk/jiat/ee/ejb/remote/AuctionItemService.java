package lk.jiat.ee.ejb.remote;

import jakarta.ejb.Remote;
import lk.jiat.ee.core.dto.AuctionItemDTO;

import java.util.List;

@Remote
public interface AuctionItemService {
    AuctionItemDTO createAuctionItem(AuctionItemDTO auction);
    List<AuctionItemDTO> getAllAuctionItems();
    AuctionItemDTO getAuctionItemById(int id);
    void updateAuctionItem(int id, AuctionItemDTO auction);
    void deleteAuctionItem(int id);
}
