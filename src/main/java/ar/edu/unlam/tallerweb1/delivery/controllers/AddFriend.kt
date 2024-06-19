package ar.edu.unlam.tallerweb1.delivery.controllers

import ar.edu.unlam.tallerweb1.delivery.Controller
import ar.edu.unlam.tallerweb1.delivery.HtmlStrings.FRIEND_ADDED
import ar.edu.unlam.tallerweb1.domain.usecases.AddFriendsToGroup
import javax.servlet.http.HttpServletRequest

class AddFriend(private val addFriendsToGroup: AddFriendsToGroup) : Controller {
    override fun invoke(request: HttpServletRequest): String {
        val user = request.getParameter("user")
        val friend = request.getParameter("friend")
        addFriendsToGroup(user, friend)
        return FRIEND_ADDED
    }
}
