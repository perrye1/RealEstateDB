// AuthenticateServlet.java

package edu.nku.csc450.realEstate.web.servlet;

import edu.nku.csc450.realEstate.web.listener.MysqlContextListener;
import edu.nku.csc450.realEstate.web.model.Person;
import edu.nku.csc450.realEstate.web.repository.PersonRepository;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet(urlPatterns = {"/authenticate"})
public class AuthenticateServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		PersonRepository prepo = new PersonRepository(MysqlContextListener.getDataSource());
		String action = req.getParameter("action");

		// called by app register() function
		if(action.equals("register")){
			String e_mail = req.getParameter("e_mail");
			String f_name = req.getParameter("f_name");
			String l_name = req.getParameter("l_name");
			String u_name= req.getParameter("u_name").toLowerCase();
			// calls PersonRepository findPerson() method
			Person p = prepo.findPerson(u_name);
			// register a person that does not exist
			if (p.getUName().equals("")) {
				// calls PersonRepository savePerson() method
				prepo.savePerson(e_mail, f_name, l_name, u_name);
				ImmutableMap<String,String> responseMap = ImmutableMap.<String, String>builder()
						.put("result", "registerSuccess")
						.build();
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				String json = gson.toJson(responseMap);

				resp.setContentType("application/json");
				resp.getWriter().write(json);
				resp.flushBuffer();

				session.setAttribute("AUTHENTICATED", "true");
				session.setAttribute("e_mail", e_mail);
				session.setAttribute("f_name", f_name);
				session.setAttribute("l_name", l_name);
				session.setAttribute("u_name", u_name);

			} else {
				// do not register existing user, auto login
				ImmutableMap<String,String> responseMap = ImmutableMap.<String, String>builder()
						.put("result", "alreadyRegistered")
						.build();
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				String json = gson.toJson(responseMap);

				resp.setContentType("application/json");
				resp.getWriter().write(json);
				resp.flushBuffer();

				session.setAttribute("AUTHENTICATED", "true");
				session.setAttribute("e_mail", e_mail);
				session.setAttribute("f_name", f_name);
				session.setAttribute("l_name", l_name);
				session.setAttribute("u_name", u_name);
			}
		// called by app login() function
		} else if (action.equals("login")) {
			String u_name = req.getParameter("u_name").toLowerCase();
			// calls PersonRepository findPerson() method
			Person p = prepo.findPerson(u_name);

			if (p.getUName().equals("")) {
				// user does not exist
				ImmutableMap<String,String> responseMap = ImmutableMap.<String, String>builder()
						.put("result", "notRegistered")
						.build();
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				String json = gson.toJson(responseMap);

				resp.setContentType("application/json");
				resp.getWriter().write(json);
				resp.flushBuffer();
			} else {
				// user exists
				// results to app login() funtion
				System.out.println("login was called by "+u_name);
				ImmutableMap<String,String> responseMap = ImmutableMap.<String, String>builder()
						.put("result", "loginSuccess")
						.build();
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				String json = gson.toJson(responseMap);

				resp.setContentType("application/json");
				resp.getWriter().write(json);
				resp.flushBuffer();

				session.setAttribute("AUTHENTICATED", "true");
				session.setAttribute("e_mail", p.getEMail());
				session.setAttribute("f_name", p.getFName());
				session.setAttribute("l_name", p.getLName());
				session.setAttribute("u_name", p.getUName());
			}
		} else if (action.equals("logout")) {

			ImmutableMap<String,String> responseMap = ImmutableMap.<String, String>builder()
				.put("result", "logoutSuccess")
				.build();
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String json = gson.toJson(responseMap);

			resp.setContentType("application/json");
			resp.getWriter().write(json);
			resp.flushBuffer();

			session.setAttribute("AUTHENTICATED", "false");
		}
	}
}