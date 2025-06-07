package lk.jiat.ee.ejb.mdb;

import com.google.gson.JsonObject;
import jakarta.ejb.ActivationConfigProperty;
import jakarta.ejb.MessageDriven;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.TextMessage;
import lk.jiat.ee.core.dto.AuctionItemDTO;
import lk.jiat.ee.core.util.JMSConstants;
import lk.jiat.ee.ejb.ws.AuctionItemWebSocketEndpoint;

@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "jakarta.jms.Topic"),
        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = JMSConstants.AUCTION_TOPIC),
        @ActivationConfigProperty(propertyName = "connectionFactoryLookup", propertyValue = JMSConstants.CONNECTION_FACTORY),
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge")
})
public class AuctionItemNotificationMDB implements MessageListener {

    @Override
    public void onMessage(Message message) {
        try {
            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                String messageText = textMessage.getText();

                if (messageText.startsWith("CREATE|") || messageText.startsWith("UPDATE|") || messageText.startsWith("DELETE|")) {
                    messageText = messageText.replaceAll("\\]\\]+$", "");

                    String[] parts = messageText.split("\\|");
                    String action = parts[0];
                    int id = Integer.parseInt(parts[1]);
                    String itemName = parts[2];
                    int startingPrice = Integer.parseInt(parts[3]);
                    int currentPrice = Integer.parseInt(parts[4]);
                    long startTime = Long.parseLong(parts[5]);
                    long endTime = Long.parseLong(parts[6]);
                    boolean active = Boolean.parseBoolean(parts[7]);

                    AuctionItemDTO auction = new AuctionItemDTO(id, itemName, startingPrice, currentPrice, startTime, endTime, active);

                    switch (action) {
                        case "CREATE":
                            processAuctionCreation(auction);
                            break;
                        case "UPDATE":
                            processAuctionUpdate(auction);
                            break;
                        case "DELETE":
                            processAuctionDeletion(auction);
                            break;
                    }
                } else {
                    System.err.println("Unsupported message format: " + messageText);
                }

            }
        } catch (JMSException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    JsonObject auctionJson = new JsonObject();
    JsonObject jsonResponse = new JsonObject();

    private void processAuctionCreation(AuctionItemDTO auction) {
        auctionJson.addProperty("id", auction.getId());
        auctionJson.addProperty("itemName", auction.getItemName());
        auctionJson.addProperty("startingPrice", auction.getStartingPrice());
        auctionJson.addProperty("currentPrice", auction.getCurrentPrice());
        auctionJson.addProperty("startTime", auction.getStartTime());
        auctionJson.addProperty("endTime", auction.getEndTime());
        auctionJson.addProperty("active", auction.isActive());

        jsonResponse.addProperty("status", "success");
        jsonResponse.addProperty("message", "Auction created successfully.");
        jsonResponse.add("auction", auctionJson);

        System.out.println("jsonResponse: " + jsonResponse);

        AuctionItemWebSocketEndpoint.broadcast(jsonResponse.toString());
    }

    private void processAuctionUpdate(AuctionItemDTO auction) {
        System.out.println("Processing AuctionUpdate Called:" + auction);
    }

    private void processAuctionDeletion(AuctionItemDTO auction) {
        System.out.println("Processing AuctionDeletion Called:" + auction);
    }

}
