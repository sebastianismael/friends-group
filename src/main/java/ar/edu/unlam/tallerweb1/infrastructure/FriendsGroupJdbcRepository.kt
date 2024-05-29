package ar.edu.unlam.tallerweb1.infrastructure

import ar.edu.unlam.tallerweb1.domain.FriendsGroupRepository
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

class FriendsGroupJdbcRepository(dataSource: DataSource) : JdbcRepository(dataSource), FriendsGroupRepository {
    override fun getMembers(group: FriendsGroup): List<User> {
        return searchInTransaction(
            { connection: Connection, groupId: String ->
                val found= mutableListOf<User>()
                val sql = "select * from user where friends_group_id = ?"
                val ps = prepareStatement(connection, sql)
                    .withLong(1, groupId.toLong())
                val users = executeQuery(ps)
                while (next(users))
                    found.add(User(getLong("id", users), getString("name", users)))
                found
            },
            group.id.toString())
    }

    override fun getGroupOf(user: User): FriendsGroup? {
        return findInTransaction(
            { connection: Connection, username: String ->
                val ps = prepareStatement(connection, "select friends_group_id from user where name = ?")
                    .withString(1, username)
                val rs = executeQuery(ps)
                if (next(rs)) {
                    val groupId = getLong("friends_group_id", rs)
                    val sql = "select * from friends_group where id = ?"
                    val psGroup = prepareStatement(connection, sql)
                        .withLong(1, groupId)
                    val rsGroup = executeQuery(psGroup)
                    if (next(rsGroup))
                        return@findInTransaction FriendsGroup(groupId, getString("name", rsGroup))
                }
                return@findInTransaction null
            },
            user.name)
    }

}

