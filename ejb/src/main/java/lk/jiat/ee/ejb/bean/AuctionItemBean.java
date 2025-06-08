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
import lk.jiat.ee.core.dao.AuctionItemDAO;
import lk.jiat.ee.core.dao.BidDAO;
import lk.jiat.ee.core.dto.AuctionItemDTO;
import lk.jiat.ee.core.dto.BidDTO;
import lk.jiat.ee.core.util.JMSConstants;
import lk.jiat.ee.ejb.remote.AuctionItemService;

import java.util.List;

@Stateless
public class AuctionItemBean implements AuctionItemService {

    @Resource(lookup = JMSConstants.CONNECTION_FACTORY)
    private ConnectionFactory connectionFactory;

    @Resource(lookup = JMSConstants.AUCTION_TOPIC)
    private Topic auctionTopic;

    @Override
    public AuctionItemDTO createAuctionItem(AuctionItemDTO auction) {
        AuctionItemDTO createdAuction = AuctionItemDAO.auctionCreate(auction);
        if (createdAuction != null) {
            sendAuctionItemUpdate("CREATE", createdAuction);
        }
        return createdAuction;
    }

    @Override
    public List<AuctionItemDTO> getAllAuctionItems() {
        List<AuctionItemDTO> auctionItems = AuctionItemDAO.getAllAuctionItems();

        for (AuctionItemDTO auctionItem : auctionItems) {
            List<BidDTO> bidsForAuction = BidDAO.getBidsByAuctionItemId(auctionItem.getId());
            auctionItem.setBids(bidsForAuction);
        }
        return auctionItems;
    }

    @Override
    public AuctionItemDTO getAuctionItemById(int id) {
        AuctionItemDTO auctionItem = AuctionItemDAO.getAuctionItem(id);
        if (auctionItem != null) {
            List<BidDTO> bidsForAuctionItem = BidDAO.getBidsByAuctionItemId(id);
            auctionItem.setBids(bidsForAuctionItem);
        }
        return auctionItem;
    }

    @Override
    public void updateAuctionItem(int id, AuctionItemDTO auction) {
        sendAuctionItemUpdate("UPDATE", auction);
    }

    @Override
    public void deleteAuctionItem(int id) {
        AuctionItemDTO auction = AuctionItemDAO.getAuctionItem(id);
        if (auction != null) {
            // Implementation to delete auction
            sendAuctionItemUpdate("DELETE", auction);
        }
    }

    private void sendAuctionItemUpdate(String action, AuctionItemDTO auction) {
        try (Connection connection = connectionFactory.createConnection();
             Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
             MessageProducer producer = session.createProducer(auctionTopic)) {

            String messageText = String.format("%s|%d|%s|%d|%s|%s|%b",
                    action,
                    auction.getId(),
                    auction.getItemName(),
                    auction.getStartingPrice(),
                    auction.getStartTime(),
                    auction.getEndTime(),
                    auction.isActive());

            System.out.println("messageText: "+messageText);
            TextMessage message = session.createTextMessage(messageText);
            producer.send(message);

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}