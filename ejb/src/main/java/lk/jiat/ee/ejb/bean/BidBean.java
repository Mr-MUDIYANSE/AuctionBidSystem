package lk.jiat.ee.ejb.bean;

import jakarta.ejb.Stateful;
import lk.jiat.ee.core.dao.BidDAO;
import lk.jiat.ee.core.dto.BidDTO;
import lk.jiat.ee.ejb.remote.BidService;

import java.util.List;

@Stateful
public class BidBean implements BidService {
    @Override
    public BidDTO createBid(BidDTO bid) {
        return BidDAO.bidCreate(bid);
    }

    @Override
    public List<BidDTO> getAllBids() {
        return BidDAO.getAllBids();
    }

    @Override
    public BidDTO getBidById(int id) {
        return BidDAO.getBid(id);
    }

    @Override
    public void updateBid(int id, BidDTO bid) {

    }

    @Override
    public void deleteBid(int id) {

    }
}
