package ru.annin.truckmonitor.data.keystore

import android.content.Context
import ru.annin.truckmonitor.utils.Analytic
import java.io.File
import java.security.KeyStore
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

/**
 * Сервис инкапсулирующий в себя работу с хранилищем ключей.
 *
 * @author Pavel Annin.
 */
class KeyStoreService(private val context: Context) {

    // Configuration
    private val TYPE: String = KeyStore.getDefaultType()
    private val HMAC_ALGORITHM = "HmacSHA256"
    private val FILE_NAME: String = "truckmonitor.keystore"

    // Properties
    private val keyStore: KeyStore

    init {
        keyStore = KeyStore.getInstance(TYPE)
        val file: File = context.getFileStreamPath(FILE_NAME)
        if (file.exists()) {
            // Файл существует
            val inputStream = context.openFileInput(FILE_NAME)
            inputStream.use { inputStream -> keyStore.load(inputStream, null) }
        } else {
            // Файла не существует
            keyStore.load(null)
        }
    }

    /** Добавить секретный ключ. */
    fun addSecretKey(alias: String, password: String): Boolean = keyStore.run {
        val secretKey: SecretKey = SecretKeySpec(password.toByteArray(), HMAC_ALGORITHM)
        val entry = KeyStore.SecretKeyEntry(secretKey)
        setEntry(alias, entry, null)
        return save()
    }

    /** Получить секретный ключ. */
    fun retrieveSecretKey(alias: String): String? = keyStore.run {
        if (containsAlias(alias)) {
            val entry: KeyStore.Entry = getEntry(alias, null)
            if (entry is KeyStore.SecretKeyEntry) {
                val encoded: ByteArray = entry.secretKey.encoded
                return encoded.toString()
            }
        }
        return null
    }

    /** Удалить ключ по Alias. */
    fun deleteEntryByAlias(alias: String): Boolean = keyStore.run {
        if (containsAlias(alias)) {
            deleteEntry(alias)
            return save()
        }
        return false
    }

    /** Удалить все данные. */
    fun deleteAll(): Boolean = keyStore.run {
        val enumeration = aliases()
        while (enumeration.hasMoreElements()) {
            val alias = enumeration.nextElement()
            deleteEntry(alias)
        }
        return save()
    }

    /** Сохранить изменения в хранилище. */
    fun save(): Boolean {
        val output = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE)
        try {
            keyStore.store(output, null)
            return true
        } catch (error: Exception) {
            Analytic.error(error)
            return false
        } finally {
            output.close()
        }
    }
}