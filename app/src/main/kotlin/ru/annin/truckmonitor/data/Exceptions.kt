package ru.annin.truckmonitor.data

/**
 * @author Pavel Annin.
 */

/** Исключение вызванное во время выполнения API запроса. */
class ApiException(val isNetworkException: Boolean = false,
                   val code: Int? = null,
                   message: String? = null) : RuntimeException(message)

/** Исключение нет подключения к интернету. */
class NoInternetConnectionException : RuntimeException()

/** Сервер недоступен. */
class ServerNotAvailableException : RuntimeException()

/** Не правильный логин / пароль. */
class InvalidEmailPasswordException : RuntimeException()

/** Исключение в KeyStore. */
class KeyStoreException(message: String? = null) : RuntimeException(message)