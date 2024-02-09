package ar.edu.unlam.tallerweb1.delivery

import ar.edu.unlam.tallerweb1.delivery.Controllers.Companion.resolve
import ar.edu.unlam.tallerweb1.domain.exceptions.BusinessException
import com.google.gson.Gson
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.IOException
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR

class ApiDispatcherServlet : HttpServlet() {
    private val parser = Gson()
    @Throws(IOException::class)
    override fun doGet(request: HttpServletRequest, response: HttpServletResponse) = doAction(request, response)

    @Throws(IOException::class)
    override fun doPost(request: HttpServletRequest, response: HttpServletResponse) = doAction(request, response)

    @Throws(IOException::class)
    private fun doAction(request: HttpServletRequest, response: HttpServletResponse) {
        try {
            val json = parser.toJson(resolve(request.requestURI)!!.invoke(request))
            doResponse(response, json)
        } catch (e: BusinessException) {
            logger.error(e.message, e)
            response.sendError(SC_INTERNAL_SERVER_ERROR, e.message)
        } catch (e: Exception) {
            logger.error(e.message, e)
            response.sendError(SC_INTERNAL_SERVER_ERROR)
        }
    }

    @Throws(IOException::class)
    private fun doResponse(response: HttpServletResponse, json: String) {
        response.contentType = "application/json"
        val printWriter = response.writer
        printWriter.println(json)
        printWriter.close()
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(ApiDispatcherServlet::class.java)
    }
}
