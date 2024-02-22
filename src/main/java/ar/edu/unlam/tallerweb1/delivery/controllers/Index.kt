package ar.edu.unlam.tallerweb1.delivery.controllers

import ar.edu.unlam.tallerweb1.delivery.Controller
import javax.servlet.http.HttpServletRequest

class Index : Controller {
    override fun invoke(request: HttpServletRequest) =
        """<div>
            <a href=/create-schema">Crear Schema</a></br>
            <a href="/load-data">Cargar datos</a></br>
            <a href="/clean-data">Borrar datos</a></br>
            <a href="/list-expenses?user=Ana">Listar</a></br>
        </div>"""
}
