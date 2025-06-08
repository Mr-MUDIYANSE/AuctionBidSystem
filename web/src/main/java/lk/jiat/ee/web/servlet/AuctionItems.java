package lk.jiat.ee.web.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import jakarta.ejb.EJB;
import jakarta.json.bind.annotation.JsonbTypeInfo;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.jiat.ee.core.dto.AuctionItemDTO;
import lk.jiat.ee.ejb.remote.AuctionItemService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/auction/item")
public class AuctionItems extends HttpServlet {

    @EJB
    @Expose(serialize = false)
    private transient AuctionItemService auctionService;

    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String idParam = request.getParameter("id");
        PrintWriter out = response.getWriter();

        if (idParam != null) {
            try {
                int id = Integer.parseInt(idParam);
                AuctionItemDTO auction = auctionService.getAuctionItemById(id);

                if (auction != null) {
                    JsonObject jsonResponse = new JsonObject();
                    jsonResponse.addProperty("status", "success");
                    jsonResponse.add("auction", gson.toJsonTree(auction));
                    out.print(gson.toJson(jsonResponse));
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    JsonObject errorResponse = new JsonObject();
                    errorResponse.addProperty("status", "error");
                    errorResponse.addProperty("message", "Auction not found.");
                    out.print(gson.toJson(errorResponse));
                }

            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                JsonObject errorResponse = new JsonObject();
                errorResponse.addProperty("status", "error");
                errorResponse.addProperty("message", "Invalid ID format.");
                out.print(gson.toJson(errorResponse));
            }

        } else {
            // Return all auction Items
            List<AuctionItemDTO> auctionItems = auctionService.getAllAuctionItems();

            JsonObject jsonResponse = new JsonObject();
            jsonResponse.addProperty("status", "success");
            jsonResponse.add("auction_items", gson.toJsonTree(auctionItems));
            out.print(gson.toJson(jsonResponse));
        }

        out.flush();
    }
}
