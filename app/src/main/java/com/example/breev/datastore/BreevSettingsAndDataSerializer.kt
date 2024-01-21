package com.example.breev.datastore

import androidx.datastore.core.Serializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

@Suppress("BlockingMethodInNonBlockingContext")
object BreevSettingsAndDataSerializer: Serializer<BreevSettingsAndData> {
    override val defaultValue: BreevSettingsAndData
        get() = BreevSettingsAndData()

    override suspend fun readFrom(input: InputStream): BreevSettingsAndData {
        return try {
            Json.decodeFromString(
                deserializer = BreevSettingsAndData.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: SerializationException) {
            e.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: BreevSettingsAndData, output: OutputStream) {
        output.write(
            Json.encodeToString(
                serializer = BreevSettingsAndData.serializer(),
                value = t
            ).encodeToByteArray()
        )
    }
}

@Suppress("BlockingMethodInNonBlockingContext")
object BreevSettingsSerializer: Serializer<BreevSettings> {
    override val defaultValue: BreevSettings
        get() = BreevSettings()

    override suspend fun readFrom(input: InputStream): BreevSettings {
        return try {
            Json.decodeFromString(
                deserializer = BreevSettings.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: SerializationException) {
            e.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: BreevSettings, output: OutputStream) {
        output.write(
            Json.encodeToString(
                serializer = BreevSettings.serializer(),
                value = t
            ).encodeToByteArray()
        )
    }
}