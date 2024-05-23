package ar.edu.unlam.tallerweb1.delivery;

import ar.edu.unlam.tallerweb1.domain.exceptions.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static ar.edu.unlam.tallerweb1.delivery.Controllers.resolve;
import static org.slf4j.LoggerFactory.getLogger;

public class WebAppDispatcherServlet extends HttpServlet {

    private static Logger logger = getLogger(WebAppDispatcherServlet.class);
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doAction(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doAction(request, response);
    }

    private void doAction(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try{
            String results = resolve(request.getRequestURI()).invoke(request).toString();
            doResponse(response, results);
        } catch(BusinessException e){
            logger.error(e.getMessage(), e);
            doResponse(response, e.getMessage());
        } catch(Exception e){
            logger.error(e.getMessage(), e);
            doResponse(response, HtmlStrings.GENERIC_ERROR);
        }
    }

    private void doResponse(HttpServletResponse response, String body) throws IOException {
        response.setContentType("text/html");
        PrintWriter printWriter = response.getWriter();
        printWriter.println("<html><body>");
        printWriter.println(body);
        printWriter.println("</body></html>");
        printWriter.close();
    }
}
