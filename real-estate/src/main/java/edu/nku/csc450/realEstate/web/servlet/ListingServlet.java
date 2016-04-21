// ListingServlet.java

package edu.nku.csc450.realEstate.web.servlet;

import edu.nku.csc450.realEstate.web.listener.MysqlContextListener;
import edu.nku.csc450.realEstate.web.model.Listing;
import edu.nku.csc450.realEstate.web.repository.ListingRepository;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(urlPatterns = {"/listing"})
public class ListingServlet extends HttpServlet {

	ListingRepository lrepo;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		lrepo = new ListingRepository(MysqlContextListener.getDataSource());
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String action = req.getParameter("action");

		// called by app getAllListings() function
		if (action.equals("findAll")) {
			// calls ListingRepository findAll method
			List<Listing> listings = lrepo.findAll();
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String json = gson.toJson(listings);

			resp.setContentType("application/json");
			resp.getWriter().write(json);
			resp.flushBuffer();
			
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		String action = req.getParameter("action");

		if (action.equals("search")) {
			int min_price = Integer.parseInt(req.getParameter("min_price"));
			int max_price = Integer.parseInt(req.getParameter("max_price"));
			String city = req.getParameter("city");
			String state = req.getParameter("state");
			int zip = Integer.parseInt(req.getParameter("zip"));
			int pool = Integer.parseInt(req.getParameter("pool"));

			List<Listing> listings = lrepo.search(min_price,max_price,city,state,zip,pool);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String json = gson.toJson(listings);

			resp.setContentType("application/json");
			resp.getWriter().write(json);
			resp.flushBuffer();

		}else if (action.equals("add")){
			int s_id = Integer.parseInt(req.getParameter("s_id"));
			int agent_id = Integer.parseInt(req.getParameter("agent_id"));
			int list_price = Integer.parseInt(req.getParameter("list_price"));
			String address = req.getParameter("address");
			String city = req.getParameter("city");
			String state = req.getParameter("state");
			int zip = Integer.parseInt(req.getParameter("zip"));
			int pool = Integer.parseInt(req.getParameter("pool"));

			lrepo.add(s_id,agent_id,list_price,address,city,state,zip,pool);

			ImmutableMap<String,String> responseMap = ImmutableMap.<String, String>builder()
						.put("result", "addSuccess")
						.build();
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				String json = gson.toJson(responseMap);

				resp.setContentType("application/json");
				resp.getWriter().write(json);
				resp.flushBuffer();

		}else if (action.equals("updateInfo")){
			int t_id = Integer.parseInt(req.getParameter("t_id"));
			int list_price = Integer.parseInt(req.getParameter("list_price"));
			String address = req.getParameter("address");
			String city = req.getParameter("city");
			String state = req.getParameter("state");
			int zip = Integer.parseInt(req.getParameter("zip"));
			int pool = Integer.parseInt(req.getParameter("pool"));

			lrepo.updateInfo(t_id,list_price,address,city,state,zip,pool);

			ImmutableMap<String,String> responseMap = ImmutableMap.<String, String>builder()
						.put("result", "updateSuccess")
						.build();
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				String json = gson.toJson(responseMap);

				resp.setContentType("application/json");
				resp.getWriter().write(json);
				resp.flushBuffer();

		}else if (action.equals("updateSold")){
			int t_id = Integer.parseInt(req.getParameter("t_id"));
			int b_id = Integer.parseInt(req.getParameter("b_id"));
			int sold_price = Integer.parseInt(req.getParameter("sold_price"));

			lrepo.updateSold(t_id,b_id,sold_price);

			ImmutableMap<String,String> responseMap = ImmutableMap.<String, String>builder()
						.put("result", "updateSuccess")
						.build();
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				String json = gson.toJson(responseMap);

				resp.setContentType("application/json");
				resp.getWriter().write(json);
				resp.flushBuffer();

		}else if(action.equals("retrieveImage")){
			int t_id = Integer.parseInt(req.getParameter("t_id"));
			String encodedImage = lrepo.retrieveImage(t_id);
			System.out.println("retrieveImage called: " + encodedImage);

			ImmutableMap<String,String> responseMap = ImmutableMap.<String, String>builder()
						.put("image", encodedImage)
						.build();
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				String json = gson.toJson(responseMap);

				resp.setContentType("application/json");
				resp.getWriter().write(json);
				resp.flushBuffer();
		}

	}
}