/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chilaaasg4server;

import chilaadblib.championsleague;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 *
 * @author achila
 */
@WebServlet(name = "AddPlayerStatServlet", urlPatterns = {"/AddPlayerStat"})
public class AddPlayerStatServlet extends HttpServlet {
    
    private championsleague db;
    /** Initializes the servlet.
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        db = new championsleague("ChampionsLeague","ism6236","ism6236bo");
        
    }
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            PrintWriter  output = response.getWriter();
            String mid = request.getParameter("mid");
            String pid = request.getParameter("pid");
            int goals = Integer.parseInt(request.getParameter("goals"));
        
        response.setContentType( "text/html" );
            
          StringBuffer buf = new StringBuffer();
        buf.append( "<HTML><HEAD><TITLE>\n" );
        buf.append( "Add Player Stat\n" );
        buf.append( "</TITLE>\n" );
        buf.append("<link rel=\"stylesheet\" type =\"text/css\" href =\"servlet.css\" /> ");
        buf.append( "</HEAD><BODY>\n" );
        
        buf.append( "<H1>");
 

        String result= db.AddPlayerStat(mid,pid,goals);
        buf.append(result+"\n");
        
        buf.append("</H1>\n");
        buf.append("<a href=\"index.html\">Main Menu</a>");
        buf.append("</BODY></HTML>");
        output.println(buf.toString());
        output.close(); // close PrintWriter stream
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
