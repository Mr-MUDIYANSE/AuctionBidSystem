package lk.jiat.ee.ejb.remote;

import lk.jiat.ee.core.dto.AuctionDTO;

public interface AuctionService {
    AuctionDTO createAuction(AuctionDTO auction);
}
