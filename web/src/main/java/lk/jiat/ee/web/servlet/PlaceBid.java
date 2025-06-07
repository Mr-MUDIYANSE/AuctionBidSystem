package lk.jiat.ee.web.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.jiat.ee.core.dto.AuctionItemDTO;
import lk.jiat.ee.core.dto.BidDTO;
import lk.jiat.ee.core.dto.UserDTO;
import lk.jiat.ee.ejb.remote.AuctionItemService;
import lk.jiat.ee.ejb.remote.BidService;
import lk.jiat.ee.ejb.remote.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/place-bid")
public class PlaceBid extends HttpServlet {

    @EJB
    private UserService userService;

    @EJB
    private AuctionItemService auctionService;

    @EJB
    private BidService bidService;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        BufferedReader reader = request.getReader();
        Gson gson = new Gson();
        BidDTO bidDTO = gson.fromJson(reader, BidDTO.class);

        System.out.println("Bid create called");

        JsonObject jsonResponse = new JsonObject();
        JsonArray errors = new JsonArray();

        int userId = bidDTO.getUserId() != null ? bidDTO.getUserId().getId() : 0;
        int auctionID = bidDTO.getAuctionId() != null ? bidDTO.getAuctionId().getId() : 0;
        int price = bidDTO.getPrice();

        if (userId <= 0) {
            errors.add("Invalid or missing user ID.");
        }
        if (auctionID <= 0) {
            errors.add("Invalid or missing auction ID.");
        }
        if (price <= 0) {
            errors.add("Price must be greater than 0.");
        }

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        if (errors.size() > 0) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonResponse.addProperty("status", "error");
            jsonResponse.add("errors", errors);
        } else {
            UserDTO user = userService.getUserById(userId);
            AuctionItemDTO auction = auctionService.getAuctionById(auctionID);

            if (user == null) {
                errors.add("User not found.");
            }
            if (auction == null) {
                errors.add("Auction not found.");
            }

            if (errors.size() > 0) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                jsonResponse.addProperty("status", "error");
                jsonResponse.add("errors", errors);
            } else {
                BidDTO bid = bidService.createBid(bidDTO);
                if (true){

                    JsonObject bidJson = new JsonObject();
                    bidJson.addProperty("bid_id", bid.getId());
                    bidJson.addProperty("user_id", String.valueOf(bid.getUserId().getId()));
                    bidJson.addProperty("auction_id", String.valueOf(bid.getAuctionId().getId()));
                    bidJson.addProperty("price", bid.getPrice());

                    JsonObject userJson = new JsonObject();
                    userJson.addProperty("id", user.getId());
                    userJson.addProperty("username", user.getUsername());
                    userJson.addProperty("email", user.getEmail());

                    JsonObject auctionJson = new JsonObject();
                    auctionJson.addProperty("id", auction.getId());
                    auctionJson.addProperty("item_name", auction.getItemName());
                    auctionJson.addProperty("start_price", auction.getStartingPrice());
                    auctionJson.addProperty("current_price", auction.getCurrentPrice());
                    auctionJson.addProperty("start_time", auction.getStartTime());
                    auctionJson.addProperty("end_time", auction.getEndTime());
                    auctionJson.addProperty("is_active", auction.isActive());

                    response.setStatus(HttpServletResponse.SC_OK);
                    jsonResponse.addProperty("status", "success");
                    jsonResponse.addProperty("message", "Bid placed successfully.");
                    jsonResponse.add("bid", bidJson);
                    jsonResponse.add("user", userJson);
                    jsonResponse.add("auction", auctionJson);
                }else {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    jsonResponse.addProperty("status", "error");
                    jsonResponse.addProperty("message", "Bid created failed.");
                }
            }
        }

        out.print(jsonResponse);
        out.flush();
    }
}