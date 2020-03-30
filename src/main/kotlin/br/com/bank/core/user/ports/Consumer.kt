package br.com.bank.core.user.ports

interface Consumer<T> {

    fun consumeMessage(action: (T) -> Unit)
}
