import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GuestBookServlet extends HttpServlet {
	private ArrayList<String> authors = new ArrayList<>();
	private ArrayList<String> entries = new ArrayList<>();
	private static final long serialVersionUID = -3388076538168097844L;
	//private Connection conn;

	/** Constructor for our servlet, sets up the database connection
	 * 
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public GuestBookServlet() throws IllegalAccessException, InstantiationException, ClassNotFoundException, SQLException
	{
		authors.add("Alice");
		authors.add("Bob");
		authors.add("Chuck");
		entries.add("Hi!");
		entries.add("Cool guestbook!");
		entries.add("How does this guestbook work?");
		/*
        String url = "jdbc:mysql://hbgwebfe.hbg.psu.edu/";
        String driver = "com.mysql.jdbc.Driver";
        String userName = "tst221";
        String password = "2468";

        Class.forName(driver).newInstance();
        conn = DriverManager.getConnection(url + userName,userName,password);
		 */
	}

	/** Handle Get requests
	 * 
	 * @param req The request
	 * @param resp The response
	 * @throws IOException
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setBufferSize(8 * 1024); // 8K buffer
		resp.setContentType("text/html");

		if (req.getRequestURI().equalsIgnoreCase("/bin/display")) {
			try {
				writeCommentList(resp.getWriter());
			} catch (SQLException sqlE) {
				log("SQL Exception " + sqlE.getMessage());
				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);                
			}
		}
		else if (req.getRequestURI().equalsIgnoreCase("/bin/comment")) {
			writeCommentForm(resp.getWriter());
		}
		else if (req.getRequestURI().equalsIgnoreCase("/bin/reset")) {
			authors.clear();
			entries.clear();
			authors.add("Alice");
			authors.add("Bob");
			authors.add("Chuck");
			entries.add("Hi!");
			entries.add("Cool guestbook!");
			entries.add("How does this guestbook work?");
			/*
    	   try {

       	    Statement statement = conn.createStatement();
       	    statement.executeUpdate("Delete from GuestBook");
    	   }
    	   catch (Exception e) {
    		   System.out.println(e);
    	   }*/
			writeCommentForm(resp.getWriter());
		}
		else {
			log("NOT_FOUND: " + req.getRequestURL());            
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
		}   
	}

	private void writeHeader(PrintWriter out) {
		out.println("<HTML>");
		out.println("<HEAD><TITLE>My Guestbook!</TITLE></HEAD>");
		out.println("<BODY>");
	}

	private void writeFooter(PrintWriter out) {
		out.println("</BODY>");
		out.println("</HTML>");
	}

	private void writeCommentList(PrintWriter out) throws SQLException {
		writeHeader(out);

		out.println("Your comments are greatly appreciated!<br>");
		out.println("Here is what everyone said:<br>");

		/*
    Statement statement = conn.createStatement();
    ResultSet resultSet = statement.executeQuery("Select id, name, message from GuestBook order by id desc");
    while (resultSet.next())
    {
        out.println(resultSet.getString("name") + ": " + resultSet.getString("message") + "<br>");
    }        
		 */
		for (int i = Math.min(authors.size(), entries.size()) - 1; i >= 0; i--) {
			out.println(authors.get(i) + ": " + entries.get(i) + "<br>");
		}

		out.println("<P><A HREF=\"/bin/comment\">Leave a new comment</A>");

		writeFooter(out);
	}

	private void writeCommentForm(PrintWriter out) {
		writeHeader(out);

		out.println("Sign my guestbook!<br>");
		out.println("<form action=\"/bin/update\" method=\"POST\">");
		out.println("<input type=\"text\" name=\"name\">");
		out.println("<input type=\"text\" name=\"message\" size=\"40\">");
		out.println("<input type=\"submit\" value=\"Submit\" name=\"Submit\">");
		out.println("</form>");
		out.println("<P><A HREF=\"/bin/display\">Display the list of comments</A>");

		writeFooter(out);
	}

	/** Handle Post requests
	 * 
	 * @param req The request
	 * @param resp The response
	 * @throws IOException
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		resp.setBufferSize(8 * 1024); // 8K buffer
		resp.setContentType("text/html");

		//try
		{
			if (req.getRequestURI().equalsIgnoreCase("/bin/update"))
			{
				/*
            PreparedStatement ps = conn.prepareStatement("INSERT INTO GuestBook (name, message) VALUES (?,?)");

            ps.setString(1, req.getParameter("name"));
            ps.setString(2, req.getParameter("message"));

            ps.executeUpdate();
				 */
				authors.add(req.getParameter("name"));
				entries.add(req.getParameter("message"));
				writeCommentForm(resp.getWriter());
			}
			else
			{
				log("NOT_FOUND: " + req.getRequestURL());                    
				resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			}
		}
		/*catch (SQLException sqlE)
    {
        log("SQL Exception " + sqlE.getMessage());
        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);        
    }*/
	}

}
