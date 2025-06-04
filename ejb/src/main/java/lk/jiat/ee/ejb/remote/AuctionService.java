package lk.jiat.ee.ejb.remote;

import jakarta.ejb.Remote;
import lk.jiat.ee.core.dto.AuctionDTO;

import java.util.List;

@Remote
public interface AuctionService {
    AuctionDTO createAuction(AuctionDTO auction);
    List<AuctionDTO> getAllAuctions();
    AuctionDTO getAuctionById(int id);
    void updateAuction(int id, AuctionDTO auction);
    void deleteAuction(int id);
}
