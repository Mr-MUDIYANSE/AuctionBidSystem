package lk.jiat.ee.web.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jakarta.ejb.EJB;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.jiat.ee.core.dto.AuctionDTO;
import lk.jiat.ee.ejb.remote.AuctionService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/auction/create")
public class CreateAuction extends HttpServlet {

    @EJB
    @Inject
    private AuctionService auctionService;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BufferedReader reader = request.getReader();
        Gson gson = new Gson();
        AuctionDTO auctionDTO = gson.fromJson(reader, AuctionDTO.class);

        JsonObject jsonResponse = new JsonObject();
        JsonArray errors = new JsonArray();
        String title = auctionDTO.getTitle();

        if (title.isEmpty()) {
            errors.add("Auction title cannot be empty");
        }

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        if (errors.size() > 0) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonResponse.addProperty("status", "error");
            jsonResponse.add("errors", errors);
        } else {
            AuctionDTO auction = auctionService.createAuction(auctionDTO);

            JsonObject auctionJson = new JsonObject();
            auctionJson.addProperty("auction_id", auction.getId());
            auctionJson.addProperty("title", auction.getTitle());

            response.setStatus(HttpServletResponse.SC_OK);
            jsonResponse.addProperty("status", "success");
            jsonResponse.addProperty("message", "Auction created successfully.");
            jsonResponse.add("auction", auctionJson);
        }
        out.print(jsonResponse);
        out.flush();
    }
}
