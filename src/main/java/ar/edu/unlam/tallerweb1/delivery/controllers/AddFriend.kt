package ar.edu.unlam.tallerweb1.delivery.controllers

import ar.edu.unlam.tallerweb1.delivery.Controller
import ar.edu.unlam.tallerweb1.delivery.HtmlStrings.Companion.FRIEND_ADDED
import ar.edu.unlam.tallerweb1.domain.FriendsGroupService
import javax.servlet.http.HttpServletRequest

class AddFriend(private val friendsGroupService: FriendsGroupService) : Controller {
    override fun invoke(request: HttpServletRequest): String {
        val user = request!!.getParameter("user")
        val friend = request.getParameter("friend")
        friendsGroupService.addFriendsToGroup(user, friend)
        return FRIEND_ADDED
    }
}
