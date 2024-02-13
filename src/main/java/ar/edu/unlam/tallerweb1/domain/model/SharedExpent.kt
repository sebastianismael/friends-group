package ar.edu.unlam.tallerweb1.domain.model

import java.time.LocalDateTime

class SharedExpent(
    val owner: User,
    val amount: Double,
    val detail: String?,
    val date: LocalDateTime,
    var friendsGroup: FriendsGroup?
) {
    var id: Long? = null
        private set

    constructor(
        id: Long?,
        owner: User,
        amount: Double,
        detail: String?,
        date: LocalDateTime,
        friendsGroup: FriendsGroup?
    ) : this(owner, amount, detail, date, friendsGroup) {
        this.id = id
        this.friendsGroup = friendsGroup
    }

    val payer: String
        get() = owner.name
}
