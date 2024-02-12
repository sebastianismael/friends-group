package ar.edu.unlam.tallerweb1.delivery

import javax.servlet.http.HttpServletRequest

interface Controller {
    operator fun invoke(request: HttpServletRequest): Any?
}
