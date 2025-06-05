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
import lk.jiat.ee.core.dao.UserDAO;
import lk.jiat.ee.core.dto.AuctionDTO;
import lk.jiat.ee.core.dto.UserDTO;
import lk.jiat.ee.ejb.remote.AuctionService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

@WebServlet("/auction/create")
public class CreateAuction extends HttpServlet {

    @EJB
    AuctionService auctionService;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BufferedReader reader = request.getReader();
        Gson gson = new Gson();
        AuctionDTO auctionDTO = gson.fromJson(reader, AuctionDTO.class);

        System.out.println("Auction create called");

        JsonObject jsonResponse = new JsonObject();
        JsonArray errors = new JsonArray();

        String itemName = auctionDTO.getItemName();
        int startPrice = auctionDTO.getStartingPrice();
        int currentPrice = auctionDTO.getCurrentPrice();
        long startTime = auctionDTO.getStartTime();
        long endTime = auctionDTO.getEndTime();
        boolean isActive = auctionDTO.isActive();

        if (itemName == null || itemName.trim().isEmpty()) {
            errors.add("Item name is required.");
        }

        if (startPrice <= 0) {
            errors.add("Starting price must be greater than 0.");
        }

        if (currentPrice <= 0) {
            errors.add("Current price must be greater than 0.");
        }

//        if (startTime.isEmpty()) {
//            errors.add("Start time is required.");
//        }
//
//        if (endTime.isEmpty()) {
//            errors.add("End time is required.");
//        }

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        if (errors.size() > 0) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonResponse.addProperty("status", "error");
            jsonResponse.add("errors", errors);
        } else {
            AuctionDTO auction = auctionService.createAuction(auctionDTO);

            if (auction != null) {
                JsonObject auctionJson = new JsonObject();
                auctionJson.addProperty("id", auction.getId());
                auctionJson.addProperty("item_name", auction.getItemName());
                auctionJson.addProperty("start_price", auction.getStartingPrice());
                auctionJson.addProperty("current_price", auction.getCurrentPrice());
                auctionJson.addProperty("start_time", auction.getStartTime());
                auctionJson.addProperty("end_time", auction.getEndTime());
                auctionJson.addProperty("isActive", String.valueOf(isActive));

                response.setStatus(HttpServletResponse.SC_OK);
                jsonResponse.addProperty("status", "success");
                jsonResponse.addProperty("message", "Auction created successful.");
                jsonResponse.add("auction", auctionJson);
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                jsonResponse.addProperty("status", "error");
                jsonResponse.addProperty("message", "Auction created failed.");
            }
        }

        out.print(jsonResponse);
        out.flush();
    }
}
