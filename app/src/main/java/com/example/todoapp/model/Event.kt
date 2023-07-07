package com.example.todoapp.model

/**
 * Обертка для события, которая позволяет передавать и обрабатывать события однократно.
 *
 * @param T Тип данных, передаваемых в событии.
 * @property content Содержимое события.
 * @property hasBeenHandled Флаг, указывающий, было ли событие уже обработано.
 */
open class Event<out T>(private val content: T) {

    var hasBeenHandled = false
        private set

    /**
     * Получает содержимое события, если оно еще не было обработано. После первого обращения
     * возвращает null при последующих обращениях.
     *
     * @return Содержимое события или null, если событие уже было обработано.
     */
    fun getContentIfNotHandled(): T? = if (hasBeenHandled) null else {
        hasBeenHandled = true
        content
    }

    /**
     * Получает содержимое события, не изменяя его состояние. Может использоваться для просмотра
     * содержимого события без его обработки.
     *
     * @return Содержимое события.
     */
    fun peekContent(): T = content
}