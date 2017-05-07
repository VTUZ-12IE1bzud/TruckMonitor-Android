package ru.annin.truckmonitor.data.repository

import ru.annin.truckmonitor.data.KeyStoreException
import ru.annin.truckmonitor.data.keystore.KeyStoreService
import ru.annin.truckmonitor.data.repository.KeyStoreRepository.token

/**
 * Репозиторий ключей.
 * Для инициализации репозитория, необходимо вызвать метод initialization() в Application.
 * Если во время записи/удаления данных будет получена ошибка, будет выбрашенно исключение KeyStoreException.
 *
 * @property token Токен пользователя.
 *
 * @author Pavel Annin.
 */
object KeyStoreRepository {

    // Configuration
    private val TOKEN_ALIAS: String = "token"

    // Component's
    private lateinit var keyStore: KeyStoreService

    // Properties
    var token: String
        get() = keyStore.retrieveSecretKey(TOKEN_ALIAS) ?: throw KeyStoreException("Error retrieve token")
        set(value) = keyStore.run {
            val result = addSecretKey(TOKEN_ALIAS, value)
            if (!result) {
                throw KeyStoreException("Error save token")
            }
        }

    /**
     * Инициализация репозитория.
     * Необходимо вызвать в Application.
     */
    @JvmStatic fun initialization(keyStore: KeyStoreService) {
        this.keyStore = keyStore
    }

    /** Удалить токен пользователя. */
    fun deleteToken() {
        val result = keyStore.deleteEntryByAlias(TOKEN_ALIAS)
        if (!result) {
            throw KeyStoreException("Error delete token")
        }
    }
}