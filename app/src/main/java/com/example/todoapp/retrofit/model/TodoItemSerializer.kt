package com.example.todoapp.retrofit.model

import com.example.todoapp.model.Importance
import com.example.todoapp.model.TodoItem
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type
import java.util.Date
import java.util.UUID

/**
 * Сериализатор и десериализатор для класса [TodoItem].
 */
class TodoItemSerializer : JsonSerializer<TodoItem>, JsonDeserializer<TodoItem> {

    /**
     * Сериализует объект [TodoItem] в формат JSON.
     *
     * @param src Объект [TodoItem] для сериализации.
     * @param typeOfSrc Тип исходного объекта.
     * @param context Контекст сериализации JSON.
     * @return Элемент JSON, представляющий сериализованный объект [TodoItem].
     */
    override fun serialize(
        src: TodoItem?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        val json = JsonObject()
        src?.let { item ->
            json.addProperty("id", item.id.toString()) // Сериализация идентификатора
            json.addProperty("text", item.text) // Сериализация текста задачи
            json.addProperty("importance", item.importance.toString().lowercase()) // Сериализация приоритета
            json.addProperty("deadline", item.deadline?.time) // Сериализация дедлайна
            json.addProperty("done", item.isDone) // Сериализация статуса завершения задачи
            json.addProperty("color", "#FFFFFF") // Сериализация цвета
            json.addProperty("created_at", item.creationDate.time) // Сериализация даты создания
            json.addProperty("changed_at", item.lastModificationDate.time) // Сериализация даты последнего изменения
            json.addProperty("last_updated_by", "Usov_S") // Сериализация информации о последнем обновлении
        }
        return json
    }

    /**
     * Десериализует объект [TodoItem] из формата JSON.
     *
     * @param json Элемент JSON, представляющий сериализованный объект [TodoItem].
     * @param typeOfT Тип целевого объекта.
     * @param context Контекст десериализации JSON.
     * @return Десериализованный объект [TodoItem].
     */
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): TodoItem {
        val jsonObject = json?.asJsonObject
        val id = try {
            UUID.fromString(jsonObject?.get("id")?.asString) // Десериализация идентификатора
        } catch (e: IllegalArgumentException) {
            UUID.randomUUID()
        }

        val text = jsonObject?.get("text")?.asString ?: "" // Десериализация текста задачи
        val importance = Importance.valueOf(jsonObject?.get("importance")?.asString?.uppercase() ?: "LOW") // Десериализация приоритета
        val deadline = jsonObject?.get("deadline")?.asLong?.let { Date(it) } // Десериализация дедлайна
        val isDone = jsonObject?.get("done")?.asBoolean ?: false // Десериализация статуса завершения задачи
        val creationDate = Date(jsonObject?.get("created_at")?.asLong ?: 0) // Десериализация даты создания
        val lastModificationDate = Date(jsonObject?.get("changed_at")?.asLong ?: 0) // Десериализация даты последнего изменения
        return TodoItem(
            id,
            text,
            importance,
            deadline,
            isDone,
            creationDate,
            lastModificationDate
        )
    }
}