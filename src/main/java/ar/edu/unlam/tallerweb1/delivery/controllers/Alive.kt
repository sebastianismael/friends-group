package ar.edu.unlam.tallerweb1.delivery.controllers

import ar.edu.unlam.tallerweb1.delivery.Controller
import javax.servlet.http.HttpServletRequest

class Alive : Controller {
    override fun invoke(request: HttpServletRequest) = "=)"

}
