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
                    long startTime = Long.parseLong(parts[4]);
                    long endTime = Long.parseLong(parts[5]);
                    boolean active = Boolean.parseBoolean(parts[6]);

                    AuctionItemDTO auction = new AuctionItemDTO(id, itemName, startingPrice, startTime, endTime, active);

                    switch (action) {
                        case "CREATE":
                            processAuctionCreation(action,auction);
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


    private void processAuctionCreation(String action, AuctionItemDTO auctionItem) {
        JsonObject auctionJson = new JsonObject();
        JsonObject jsonResponse = new JsonObject();

        auctionJson.addProperty("action", action);
        auctionJson.addProperty("id", auctionItem.getId());
        auctionJson.addProperty("itemName", auctionItem.getItemName());
        auctionJson.addProperty("startingPrice", auctionItem.getStartingPrice());
        auctionJson.addProperty("startTime", auctionItem.getStartTime());
        auctionJson.addProperty("endTime", auctionItem.getEndTime());
        auctionJson.addProperty("active", auctionItem.isActive());

        jsonResponse.addProperty("status", "success");
        jsonResponse.addProperty("message", "Auction item created successfully.");
        jsonResponse.add("auction_item", auctionJson);

        System.out.println("jsonResponse: " + jsonResponse);

        AuctionItemWebSocketEndpoint.broadcast(jsonResponse.toString());
    }

    private void processAuctionUpdate(AuctionItemDTO auction) {
        System.out.println("Processing Auction Item Update Called:" + auction);
    }

    private void processAuctionDeletion(AuctionItemDTO auction) {
        System.out.println("Processing AuctionDeletion Called:" + auction);
    }

}
