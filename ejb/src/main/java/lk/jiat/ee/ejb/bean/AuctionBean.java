package lk.jiat.ee.ejb.bean;

import jakarta.ejb.Stateless;
import lk.jiat.ee.core.dao.AuctionDAO;
import lk.jiat.ee.core.dto.AuctionDTO;
import lk.jiat.ee.ejb.remote.AuctionService;

import java.util.List;

@Stateless
public class AuctionBean implements AuctionService {
    @Override
    public AuctionDTO createAuction(AuctionDTO auction) {
        return AuctionDAO.auctionCreate(auction);
    }

    @Override
    public List<AuctionDTO> getAllAuctions() {
        return AuctionDAO.getAllAuctions();
    }

    @Override
    public AuctionDTO getAuctionById(int id) {
        return AuctionDAO.getAuction(id);
    }

    @Override
    public void updateAuction(int id, AuctionDTO auction) {

    }

    @Override
    public void deleteAuction(int id) {

    }
}
