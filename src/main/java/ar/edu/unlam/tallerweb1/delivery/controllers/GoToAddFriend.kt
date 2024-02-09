package ar.edu.unlam.tallerweb1.delivery.controllers

import ar.edu.unlam.tallerweb1.delivery.Controller
import javax.servlet.http.HttpServletRequest

class GoToAddFriend : Controller {
    override fun invoke(request: HttpServletRequest) =
        """<div>
            <form id="add-friend" action="add-friend" method="POST"><br><
                input id="friend" name="friend" type="text" value=""/><br><br>
                <input id="user" name="user" type="hidden" value="${request.getParameter("user")}"/>
                <button Type="Submit"/>Agregar Amigo</button>
            </form>
        </div>"""

}
