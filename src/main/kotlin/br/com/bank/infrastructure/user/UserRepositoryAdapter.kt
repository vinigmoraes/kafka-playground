package br.com.bank.infrastructure.user

import br.com.bank.core.user.User
import br.com.bank.core.user.ports.UserRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.mongodb.BasicDBObject
import com.mongodb.MongoClient
import com.mongodb.MongoClientURI
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.FindOneAndReplaceOptions
import org.bson.Document

class UserRepositoryAdapter(
    private val mapper: ObjectMapper
) : UserRepository {

    private val client = MongoClient(MongoClientURI("mongodb://root:root@localhost"))
        .getDatabase("usersservicedb")
        .getCollection("users")

    override fun save(user: User) {
        Document.parse(mapper.writeValueAsString(user)).also { client.insertOne(it) }
    }

    override fun update(user: User) {
        val options = FindOneAndReplaceOptions().upsert(true)
        val query = BasicDBObject().append("id", user.id.toString())
        val updateObject = Document.parse(mapper.writeValueAsString(user))

        client.findOneAndReplace(query, updateObject, options)
    }

    override fun findById(id: String): User? {
        val document = client.find(eq("id", id))
            .firstOrNull() ?: return null

        return mapper.readValue(document.toJson(), User::class.java)
    }

    override fun findByCpf(cpf: String): User? {
        val document = client.find(eq("cpf", cpf))
            .firstOrNull() ?: return null

        return mapper.readValue(document.toJson(), User::class.java)
    }
}
