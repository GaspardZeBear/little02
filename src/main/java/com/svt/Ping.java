package com.svt;

import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.Integer.parseInt;
import java.util.Date;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicInteger;
import org.json.simple.JSONObject;
 

@WebServlet(name = "ping", urlPatterns = {"/ping"})

public class Ping extends HttpServlet {
	private static final long serialVersionUID = 1L;
    @EJB
    PingBean ping;

    private static AtomicInteger atomicCount = new AtomicInteger();
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int counterWhenIn=atomicCount.incrementAndGet();
        Date d=new Date();
        long d1=System.currentTimeMillis();
        int delay=0;
        int length=0;
        if ( request.getParameter("delay") != null) {
                delay=parseInt(request.getParameter("delay"));
        }
        if ( request.getParameter("length") != null) {
                length=parseInt(request.getParameter("length"));
        }
        JSONObject pingResp=ping.ping(d1,delay, length);
        long d2=System.currentTimeMillis();
        long duration=d2-d1;
        InetAddress ip;
        String hostname;
        try {
            ip = InetAddress.getLocalHost();
            hostname = ip.getHostName();
         } catch (UnknownHostException e) {
            hostname="Exception";
        }
        response.setContentType("application/json;charset=UTF-8");
        
        int counterWhenOut=atomicCount.getAndDecrement();
        try { 
        	PrintWriter out = response.getWriter();
            JSONObject j=new JSONObject();
            String clazz=this.getClass().getSimpleName();
            j.put("className",clazz);
            j.put(clazz+".timestamp",d.toString());
            j.put(clazz+".hostname",hostname);
            j.put(clazz+".atomicCountIn",counterWhenIn);
            j.put(clazz+".atomicCountOut",counterWhenOut);
            j.put(clazz+".duration",duration);
            j.put(clazz+".beanResp",pingResp);
            out.println(j);
        } finally {
        	
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
