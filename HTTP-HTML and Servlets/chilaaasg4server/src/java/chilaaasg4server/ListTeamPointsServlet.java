/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chilaaasg4server;

import chilaadblib.championsleague;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author achila
 */
@WebServlet(name = "ListTeamPointsServlet", urlPatterns = {"/ListTeamPoints"})
public class ListTeamPointsServlet extends HttpServlet {
    
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
            /* TODO output your page here. You may use following sample code. */
             String tid = request.getParameter("tid");
        

        List<String> l = db.ListTeamPoints(tid);
        
        out.println( "<HTML><HEAD><TITLE>\n" );
        out.println( "Team Points\n" );
        out.println( "</TITLE>\n" );
        out.println("<link rel=\"stylesheet\" type =\"text/css\" href =\"servlet.css\" /> ");
        out.println( "</HEAD><BODY>\n" );
        
        out.println( "<pre class=\"data\">");
        for (int i=0; i < l.size(); i++)
            out.println(l.get(i)+ "<br>");
        out.println("</pre> <p Class =\"links\">");
        out.println("<a href=\"index.html\">Main Menu</a>"
                + "</p>");
        out.println( "</BODY></HTML>" );
      
        out.close(); 
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
