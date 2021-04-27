import java.io.IOException;
import java.io.PrintWriter;
import java.security.Principal;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "MyFirstServlet", urlPatterns = {"/MyFirstServlet"})
public class MyFirstServlet extends HttpServlet{
    private static long serialVersionUID = -1915463532411657451L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
         
        throw new NullPointerException();
         
        //String name = request.getParameter("username");

        //response.getWriter().println("Hello " + name + "!");

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

        String name = request.getParameter("username");
        throw new NullPointerException();

        // response.getWriter().println("Hello " + name + "!");

    }
}
