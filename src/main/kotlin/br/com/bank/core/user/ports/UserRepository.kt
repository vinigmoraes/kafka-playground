package br.com.bank.core.user.ports

import br.com.bank.core.user.User

interface UserRepository {

    fun save(user: User)

    fun findById(id: String) : User?
}
