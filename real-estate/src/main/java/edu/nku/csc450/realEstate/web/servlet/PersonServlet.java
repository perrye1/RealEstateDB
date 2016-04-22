// PersonServlet.java

package edu.nku.csc450.realEstate.web.servlet;

import edu.nku.csc450.realEstate.web.listener.MysqlContextListener;
import edu.nku.csc450.realEstate.web.model.Person;
import edu.nku.csc450.realEstate.web.repository.PersonRepository;
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

@WebServlet(urlPatterns = {"/people"})
public class PersonServlet extends HttpServlet {

	PersonRepository prepo;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		prepo = new PersonRepository(MysqlContextListener.getDataSource());
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String action = req.getParameter("action");

		// called by app getPeople() function
		if (action.equals("findAll")) {
			// calls PlayerRepository findAll method
			List<Person> people = prepo.findAll();
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String json = gson.toJson(people);

			resp.setContentType("application/json");
			resp.getWriter().write(json);
			resp.flushBuffer();

		// called by app getCurrentPerson() function
		} else if (action.equals("getCurrentPerson")) {
			HttpSession session = req.getSession(false);
			String u_name = (String) session.getAttribute("u_name");
			// calls PersonRepository findPerson method
			Person p = prepo.findPerson(u_name);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String json = gson.toJson(p);

			resp.setContentType("application/json");
			resp.getWriter().write(json);
			resp.flushBuffer();

		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		String action = req.getParameter("action");

		if (action.equals("promoteUser")) {
			int p_id = Integer.parseInt(req.getParameter("p_id"));
			prepo.promotePerson(p_id);
		}

	}
}