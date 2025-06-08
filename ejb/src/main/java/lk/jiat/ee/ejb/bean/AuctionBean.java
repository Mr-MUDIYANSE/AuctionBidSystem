package lk.jiat.ee.ejb.bean;

import jakarta.ejb.Lock;
import jakarta.ejb.LockType;
import jakarta.ejb.Singleton;
import lk.jiat.ee.core.dao.AuctionDAO;
import lk.jiat.ee.core.dto.AuctionDTO;
import lk.jiat.ee.ejb.remote.AuctionService;

@Singleton
public class AuctionBean implements AuctionService {
    @Override
    @Lock(LockType.WRITE)
    public AuctionDTO createAuction(AuctionDTO auction) {
        return AuctionDAO.auctionCreate(auction);
    }
}
