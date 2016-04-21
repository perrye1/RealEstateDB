//UploadServlet.java


package edu.nku.csc450.realEstate.web.servlet;

import java.io.*;

import javax.servlet.annotation.*;
import javax.servlet.ServletConfig;
import javax.servlet.http.*;
import javax.servlet.ServletException;
import edu.nku.csc450.realEstate.web.repository.ListingRepository;
import edu.nku.csc450.realEstate.web.listener.MysqlContextListener;

@MultipartConfig
@WebServlet(urlPatterns = {"/upload"})
public class UploadServlet extends HttpServlet {

	ListingRepository lrepo;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		lrepo = new ListingRepository(MysqlContextListener.getDataSource());
	}


	@Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	String test = req.getParameter("t_id");
    	System.out.println("upload called, t_id is " + test);
    	int t_id = Integer.parseInt(test);

	    Part filePart = req.getPart("file"); // Retrieves <input type="file" name="file">
	    String fileName = filePart.getSubmittedFileName();
	    InputStream fileContent = filePart.getInputStream();
	    lrepo.uploadImage(t_id, fileContent);
    }
}
