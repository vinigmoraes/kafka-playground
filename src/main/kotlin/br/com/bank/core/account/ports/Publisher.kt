package br.com.bank.core.account.ports

interface Publisher<K, V> {

    fun sendMessage(key: K, value: V)
}
