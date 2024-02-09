package ar.edu.unlam.tallerweb1.delivery.controllers

import ar.edu.unlam.tallerweb1.delivery.Controller
import javax.servlet.http.HttpServletRequest

class GoToAddExpent : Controller {
    override fun invoke(request: HttpServletRequest) =
        """<div>
            <form id="add-expent" action="add-expent" method="POST"><br>
                Detalle: <input id="detail" name="detail" type="text" value=""/><br><br>
                Monto: <input id="amount" name="amount" type="text" value=""/>&euro;<br><br>
                <input id="user" name="user" type="hidden" value="${request.getParameter("user")}"/>
                <button Type="Submit"/>Agregar Gasto</button>
            </form>
        </div>"""
}
