package lk.jiat.ee.ejb.remote;

import jakarta.annotation.Resource;
import jakarta.ejb.Remote;
import lk.jiat.ee.core.dto.BidDTO;

import java.util.List;

@Remote
public interface BidService {
    BidDTO createBid(BidDTO bid);
    List<BidDTO> getAllBids();
    BidDTO getBidById(int id);
    void updateBid(int id, BidDTO bid);
    void deleteBid(int id);
}
