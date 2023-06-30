package com.example.todoapp.retrofit

/**
 * sealed класс, представляющий результат сетевого запроса.
 *
 * @param T Тип данных, возвращаемых в случае успеха.
 */
sealed class NetworkResult<out T> {
    /**
     * Класс-наследник, представляющий успешный результат сетевого запроса.
     *
     * @property data Данные, полученные в результате запроса.
     */
    data class Success<T>(val data: T) : NetworkResult<T>()

    /**
     * Класс-наследник, представляющий ошибку при выполнении сетевого запроса.
     *
     * @property errorMessage Сообщение об ошибке.
     * @property exception Исключение, связанное с ошибкой.
     */
    data class Error(val errorMessage: String, val exception: Throwable) : NetworkResult<Nothing>()
}