package ar.edu.unlam.tallerweb1.delivery.api

import ar.edu.unlam.tallerweb1.delivery.Controller
import ar.edu.unlam.tallerweb1.domain.usecases.GetBalance
import javax.servlet.http.HttpServletRequest

class ApiShowBalance(private val getBalance: GetBalance) : Controller {
    override operator fun invoke(request: HttpServletRequest) = getBalance(request.getParameter("user"))
}
