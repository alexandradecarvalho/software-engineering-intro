#### Alexandra Carvalho - 9336

----------------------------------------------------------------------------------------------------------------

EXERCÍCIO 1



RUNNING TOMCAT:
alexis@Jarvis:~/Documents/apache-tomcat-9.0.39/bin$ ./startup.sh 
(...)
Tomcat started.



SOURCE CODE FOR REQUEST PARAMETER EXAMPLE:
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
public class RequestParamExample extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
    {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Request Parameters Example</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h3>Request Parameters Example</h3>");
        out.println("Parameters in this request:<br>");
        String firstName = request.getParameter("firstname");
        String lastName = request.getParameter("lastname");
        if (firstName != null || lastName != null) {
            out.println("First Name:");
            out.println(" = " + HTMLFilter.filter(firstName) + "<br>");
            out.println("Last Name:");
            out.println(" = " + HTMLFilter.filter(lastName));
        } else {
            out.println("No Parameters, Please enter some");
        }
        out.println("<P>");
        out.print("<form action=\"");
        out.print("RequestParamExample\" ");
        out.println("method=POST>");
        out.println("First Name:");
        out.println("<input type=text size=20 name=firstname>");
        out.println("<br>");
        out.println("Last Name:");
        out.println("<input type=text size=20 name=lastname>");
        out.println("<br>");
        out.println("<input type=submit>");
        out.println("</form>");
        out.println("</body>");
        out.println("</html>");
    }
    public void doPost(HttpServletRequest request, HttpServletResponse res)
    throws IOException, ServletException
    {
        doGet(request, response);
    }
}



CREATE A MAVEN WEB-BASED PROJECT:
mvn archetype:generate -DarchetypeGroupId=org.apache.maven.archetypes -DarchetypeArtifactId=maven-archetype-webapp -DarchetypeVersion=1.4

BUILD PROJECT:
mvn install



Confirm that you have a .war file in project-folder/target.
This is your application packaged as a Web ARchive.



http://localhost:8080/ss-programing-servlets/ mostra "HELLO WORLD!"



EVIDENCE OF SUCCESSFUL DEPLOYMENT IN SERVER LOG:
alexis@Jarvis:~/Documents/apache-tomcat-9.0.39$ tail logs/catalina.out 
		(...)
22-Oct-2020 09:49:12.791 INFO [http-nio-8080-exec-5] org.apache.catalina.startup.HostConfig.deployWAR Deploying web application archive [/home/alexis/Documents/apache-tomcat-9.0.39/webapps/ss-programing-servlets.war]
22-Oct-2020 09:49:12.864 INFO [http-nio-8080-exec-5] org.apache.catalina.startup.HostConfig.deployWAR Deployment of web application archive [/home/alexis/Documents/apache-tomcat-9.0.39/webapps/ss-programing-servlets.war] has finished in [73] ms



CONFIGURE INTELLIJ FOR TOMCAT: https://mkyong.com/intellij/intellij-idea-run-debug-web-application-on-tomcat/



SERVLET CONTAINER: um servidor web usa o protocolo HTTP para transferir informação. Quando um user escreve um URL, o servidor envia-lhe a página correspondente. Por vezes, o cliente quer ler a página web com base no seu input, e não apenas uma página estática, que é o que o servidor consegue dar. Então, o SERVLET CONTAINER é uma parte do servidor que interage com os servlets através de Java para gerar dinamicamente as páginas web (no lado do servidor). ref: https://dzone.com/articles/what-servlet-container

-----------------------------------------------------------------------------------------------------------------------------------------------------------

EXERCÍCIO 3

# Whitelabel Error Page

This application has no explicit mapping for /error, so you are seeing this as a fallback.

Thu Oct 29 10:37:59 WET 2020

There was an unexpected error (type=Not Found, status=404).



An EMBEDDED HTTP SERVER runs inside the same process space as the application. So, the application is responsible for starting and configuring the server. PROS: server versions can be tested against each other, just like any other dependency; we have more control over how the web server behaves; and we deploy one single objct only. HOWEVER, the application has to be designed around the API of the server, making it harder to change servers later; there's to much dependency (many from the web server); you can't group multiple applications behind one server without a proxy; and a single uncaught exception is enough to take down the entire application server.

On the other side, a STAND ALONE APPLICATION SERVER runs separately from the application but is responsible for loading it and for forwarding requests to it. This server is configured using separate config files. PROS: it's easier to deploy updates and more flexible - makes it easy to switch servers later on. These servers can't be harmed by app errors. HOWEVER, it's a trickier development environment and it has a complex deployment



Model-View-Controller: When we access an url, the browser sends an HTTP  request to the server, which runs the web application. This request is received and processed by a class responsible for handling the request and receiving the data from the browser. Then, the Controller sends the data to the Model, which encapsules all of the data and executes all calculations and validations and accesses the DB. The result of these operations is returned to the Controller, which returns the name of the View and the data that the View needs to render the page. This View processes the data and transoforms the result into HTML, which is returned to the browser



STARTER-THYMELEAF: Starter for building MVC web applications using Thymeleaf views

STARTER-WEB: Starter for building web, including RESTful, applications using Spring MVC. Uses Tomcat as the default embedded container

STARTER-TEST: Starter for testing Spring Boot applications with libraries including JUnit, Hamcrest and Mockito



@SpringBootApplication = @Configuration + @EnableAutoConfiguration + @ComponentScan