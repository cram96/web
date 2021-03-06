package Aplicacion;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

/**
 *
 * @author chuki
 */
public class LoginChat extends HttpServlet {

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
            throws ServletException, IOException, SQLException, NoSuchAlgorithmException {
        ServletContext aplicacion = getServletContext();
        int contador = (int) aplicacion.getAttribute("contador_mensajes");
        ArrayList<String> usuarios_conectados = (ArrayList) aplicacion.getAttribute("usuarios_conectados");
        response.setContentType("text/html;charset=UTF-8");
        HttpSession sesion = request.getSession();
        String usuario = request.getParameter("username");
        String contrasena = request.getParameter("password");
        DataBaseHandler bd = new DataBaseHandler();
        String contrasena_cifrada = bd.hash(contrasena).toLowerCase();
        //Para forzar excepciones
        //Integer.parseInt("dewrftgbyhnu");
        ResultSet rs = bd.loginear(usuario, contrasena_cifrada);
        if (rs.next()) {

            ArrayList<String> lista_mensajes = new ArrayList();
            sesion.setAttribute("contador", contador);
            usuarios_conectados.add(usuario);
            aplicacion.setAttribute("usuarios_conectados", usuarios_conectados);

            Cookie[] listaCookies = request.getCookies();
            Cookie galletaSelectora = null;

            if (listaCookies != null) {
                for (Cookie galleta : listaCookies) {
                    if (galleta.getName().equals("Contador")) {
                        galletaSelectora = galleta;
                        galletaSelectora.setValue(Integer.toString(Integer.parseInt(galleta.getValue()) + 1));
                    }
                }
            }
            if (galletaSelectora == null) {
                galletaSelectora = new Cookie("Contador", "1");
            }
            response.addCookie(galletaSelectora);
            RequestDispatcher rd = getServletContext().getNamedDispatcher("ChatDisplay");
            rd.forward(request, response);
        } else {
            RequestDispatcher rd = getServletContext().getNamedDispatcher("LoginDisplay");
            rd.forward(request, response);
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
        try {
            processRequest(request, response);

        } catch (SQLException ex) {
            Logger.getLogger(LoginChat.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(LoginChat.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        try {
            processRequest(request, response);

        } catch (SQLException ex) {
            Logger.getLogger(LoginChat.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(LoginChat.class.getName()).log(Level.SEVERE, null, ex);
        }
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
