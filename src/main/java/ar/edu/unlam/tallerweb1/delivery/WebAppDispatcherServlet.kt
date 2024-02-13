package ar.edu.unlam.tallerweb1.delivery

import ar.edu.unlam.tallerweb1.delivery.Controllers.Companion.resolve
import ar.edu.unlam.tallerweb1.delivery.HtmlStrings.GENERIC_ERROR
import ar.edu.unlam.tallerweb1.domain.exceptions.BusinessException
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import java.io.IOException
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class WebAppDispatcherServlet : HttpServlet() {
    private val logger: Logger = getLogger(WebAppDispatcherServlet::class.java)

    @Throws(IOException::class)
    override fun doGet(request: HttpServletRequest, response: HttpServletResponse) = doAction(request, response)

    @Throws(IOException::class)
    override fun doPost(request: HttpServletRequest, response: HttpServletResponse) = doAction(request, response)

    @Throws(IOException::class)
    private fun doAction(request: HttpServletRequest, response: HttpServletResponse) = try {
        val results = resolve(request.requestURI)!!.invoke(request).toString()
        doResponse(response, results)
    } catch (e: BusinessException) {
        logger.error(e.message, e)
        doResponse(response, e.message)
    } catch (e: Exception) {
        logger.error(e.message, e)
        doResponse(response, GENERIC_ERROR)
    }

    @Throws(IOException::class)
    private fun doResponse(response: HttpServletResponse, body: String?) {
        response.contentType = "text/html"
        val printWriter = response.writer
        printWriter.println("<html><body>")
        printWriter.println(body)
        printWriter.println("</body></html>")
        printWriter.close()
    }
}
