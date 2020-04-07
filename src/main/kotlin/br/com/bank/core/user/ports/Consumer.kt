package br.com.bank.core.user.ports

interface Consumer<K, V> {

    fun consumeMessage(action: (V) -> Unit)
}
