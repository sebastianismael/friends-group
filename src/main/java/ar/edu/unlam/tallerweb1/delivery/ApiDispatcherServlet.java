package ar.edu.unlam.tallerweb1.delivery;

import ar.edu.unlam.tallerweb1.domain.exceptions.BusinessException;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static ar.edu.unlam.tallerweb1.delivery.Controllers.resolve;
import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

public class ApiDispatcherServlet extends HttpServlet {

    private Gson parser = new Gson();
    private static Logger logger = LoggerFactory.getLogger(ApiDispatcherServlet.class);
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doAction(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doAction(request, response);
    }

    private void doAction(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try{
            String json = parser.toJson(resolve(request.getRequestURI()).invoke(request));
            doResponse(response, json);
        } catch(BusinessException e){
            logger.error(e.getMessage(), e);
            response.sendError(SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            response.sendError(SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void doResponse(HttpServletResponse response, String json) throws IOException {
        response.setContentType("application/json");
        PrintWriter printWriter = response.getWriter();
        printWriter.println(json);
        printWriter.close();
    }
}
