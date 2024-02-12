package ar.edu.unlam.tallerweb1.delivery.api

import ar.edu.unlam.tallerweb1.delivery.Controller
import ar.edu.unlam.tallerweb1.domain.FriendsGroupServiceImpl
import javax.servlet.http.HttpServletRequest

class ApiShowBalance(private val friendsGroupService: FriendsGroupServiceImpl) : Controller {
    override fun invoke(request: HttpServletRequest) = friendsGroupService.getBalance(request.getParameter("user"))
}
