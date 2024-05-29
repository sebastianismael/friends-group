package ar.edu.unlam.tallerweb1.infrastructure

import ar.edu.unlam.tallerweb1.domain.UserRepository
import ar.edu.unlam.tallerweb1.domain.model.FriendsGroup
import ar.edu.unlam.tallerweb1.domain.model.User
import ar.edu.unlam.tallerweb1.infrastructure.utils.DataSource
import ar.edu.unlam.tallerweb1.infrastructure.utils.JdbcUtil.executeQuery
import ar.edu.unlam.tallerweb1.infrastructure.utils.JdbcUtil.getLong
import ar.edu.unlam.tallerweb1.infrastructure.utils.JdbcUtil.getString
import ar.edu.unlam.tallerweb1.infrastructure.utils.JdbcUtil.next
import ar.edu.unlam.tallerweb1.infrastructure.utils.JdbcUtil.prepareStatement
import ar.edu.unlam.tallerweb1.infrastructure.utils.withLong
import ar.edu.unlam.tallerweb1.infrastructure.utils.withString
import java.sql.Connection

class UserJdbcRepostory(dataSource: DataSource) : JdbcRepository(dataSource), UserRepository {
    override fun save(user: User) =
        executeInTransaction { connection: Connection ->
            val sql = "INSERT INTO user (name) VALUES (?)"
            val ps = prepareStatement(connection, sql)
                .withString(1, user.name)
                .execute()
        }

    override fun saveWithFriendGroup(user: User) =
            executeInTransaction { connection: Connection ->
            val sql = "INSERT INTO user (name, friends_group_id) VALUES (?,?)"
            val ps = prepareStatement(connection, sql)
                .withLong(2, user.friendsGroup?.id!!)
                .withString(1, user.name)
                .execute()
        }

    override fun findByName(name: String) =
        findInTransaction({ connection: Connection, username: String ->
            val sql = "select * from user where name = ?"
            val ps = prepareStatement(connection, sql)
                .withString(1, username)
            val users = executeQuery(ps)
            if (next(users)) {
                val user = User(getLong("id", users), getString("name", users))
                user.friendsGroup = FriendsGroup(getLong("friends_group_id", users), "")
                return@findInTransaction user
            }
            return@findInTransaction null
        }, name)
}
