package com.binghe

import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.r2dbc.core.awaitOneOrNull

class MemberCoroutineRepository(private val client: DatabaseClient) {

//    suspend fun findById(id: Long): Member? =
//        client
//            .sql("SELECT * FROM members WHERE id = :id")
//            .bind("id", id)
//            .mapValue(Member.class)
//            .awaitOneOrNull()
}

//class UserRepository(private val client: DatabaseClient) {
//
//	suspend fun count(): Long =
//		client.execute().sql("SELECT COUNT(*) FROM users")
//			.asType<Long>().fetch().awaitOne()
//
//	fun findAll(): Flow<User> =
//		client.select().from("users").asType<User>().fetch().flow()
//
//	suspend fun findOne(id: String): User? =
//		client.execute()
//			.sql("SELECT * FROM users WHERE login = :login")
//			.bind("login", id).asType<User>()
//			.fetch()
//			.awaitOneOrNull()
//
//	suspend fun deleteAll() =
//		client.execute().sql("DELETE FROM users").await()
//
//	suspend fun save(user: User) =
//		client.insert().into<User>().table("users").using(user).await()
//
//	suspend fun init() {
//		client.execute().sql("CREATE TABLE IF NOT EXISTS users (login varchar PRIMARY KEY, firstname varchar, lastname varchar);").await()
//		deleteAll()
//		save(User("smaldini", "Stéphane", "Maldini"))
//		save(User("sdeleuze", "Sébastien", "Deleuze"))
//		save(User("bclozel", "Brian", "Clozel"))
//	}
//}