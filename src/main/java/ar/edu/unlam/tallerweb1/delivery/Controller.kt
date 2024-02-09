package ar.edu.unlam.tallerweb1.delivery

import javax.servlet.http.HttpServletRequest

interface Controller {
    fun invoke(request: HttpServletRequest): Any?
}
