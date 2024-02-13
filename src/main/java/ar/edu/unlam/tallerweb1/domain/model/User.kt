package ar.edu.unlam.tallerweb1.domain.model

class User {
    var id: Long? = null
        private set
    var name: String
        private set
    var friendsGroup: FriendsGroup? = null

    constructor(id: Long?, name: String) {
        this.id = id
        this.name = name
    }

    constructor(name: String) {
        this.name = name
    }

    fun hasFriendGroup() = this.friendsGroup != null
}
