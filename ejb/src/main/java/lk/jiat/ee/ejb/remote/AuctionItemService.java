package lk.jiat.ee.ejb.remote;

import jakarta.ejb.Remote;
import lk.jiat.ee.core.dto.AuctionItemDTO;

import java.util.List;

@Remote
public interface AuctionItemService {
    AuctionItemDTO createAuction(AuctionItemDTO auction);
    List<AuctionItemDTO> getAllAuctions();
    AuctionItemDTO getAuctionById(int id);
    void updateAuction(int id, AuctionItemDTO auction);
    void deleteAuction(int id);
}
