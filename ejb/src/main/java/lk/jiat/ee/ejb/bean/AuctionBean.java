package lk.jiat.ee.ejb.bean;

import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;
import jakarta.jms.Connection;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSException;
import jakarta.jms.MessageProducer;
import jakarta.jms.Session;
import jakarta.jms.TextMessage;
import jakarta.jms.Topic;
import lk.jiat.ee.core.dao.AuctionDAO;
import lk.jiat.ee.core.dto.AuctionDTO;
import lk.jiat.ee.core.util.JMSConstants;
import lk.jiat.ee.ejb.remote.AuctionService;

import java.util.List;

@Stateless
public class AuctionBean implements AuctionService {

    @Resource(lookup = JMSConstants.CONNECTION_FACTORY)
    private ConnectionFactory connectionFactory;

    @Resource(lookup = JMSConstants.AUCTION_TOPIC)
    private Topic auctionTopic;

    @Override
    public AuctionDTO createAuction(AuctionDTO auction) {
        AuctionDTO createdAuction = AuctionDAO.auctionCreate(auction);
        if (createdAuction != null) {
            sendAuctionUpdate("CREATE", createdAuction);
        }
        return createdAuction;
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
        sendAuctionUpdate("UPDATE", auction);
    }

    @Override
    public void deleteAuction(int id) {
        AuctionDTO auction = AuctionDAO.getAuction(id);
        if (auction != null) {
            // Implementation to delete auction
            sendAuctionUpdate("DELETE", auction);
        }
    }

    private void sendAuctionUpdate(String action, AuctionDTO auction) {
        try (Connection connection = connectionFactory.createConnection();
             Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
             MessageProducer producer = session.createProducer(auctionTopic)) {

            String messageText = String.format("%s|%d|%s|%d|%d|%s|%s|%b",
                    action,
                    auction.getId(),
                    auction.getItemName(),
                    auction.getStartingPrice(),
                    auction.getCurrentPrice(),
                    auction.getStartTime(),
                    auction.getEndTime(),
                    auction.isActive());

            TextMessage message = session.createTextMessage(messageText);
            producer.send(message);

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}