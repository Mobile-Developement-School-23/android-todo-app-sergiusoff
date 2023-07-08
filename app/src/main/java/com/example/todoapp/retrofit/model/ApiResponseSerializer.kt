package com.example.todoapp.retrofit.model

import com.example.todoapp.model.TodoItem
import com.google.gson.JsonArray
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

/**
 * Сериализатор и десериализатор для класса ApiEntity.
 * Используется для преобразования объекта ApiEntity в JSON и обратно.
 */
class ApiResponseSerializer : JsonSerializer<ApiEntity>, JsonDeserializer<ApiEntity> {

    /**
     * Сериализует объект ApiEntity в JSON.
     *
     * @param src Объект ApiEntity, который нужно сериализовать.
     * @param typeOfSrc Тип объекта src.
     * @param context Контекст сериализации JSON.
     * @return JSON-элемент, представляющий сериализованный объект ApiEntity.
     */
    override fun serialize(src: ApiEntity, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val jsonObject = JsonObject()
        jsonObject.addProperty("status", src.status)

        if (src.element != null) {
            // Сериализация одиночного элемента
            jsonObject.add("element", context.serialize(src.element, TodoItem::class.java))
        } else if (src.list != null) {
            // Сериализация списка элементов
            val listArray = JsonArray()
            for (item in src.list) {
                val itemJson = context.serialize(item, TodoItem::class.java)
                listArray.add(itemJson)
            }
            jsonObject.add("list", listArray)
        }
        jsonObject.addProperty("revision", src.revision)
        return jsonObject
    }

    /**
     * Десериализует JSON в объект ApiEntity.
     *
     * @param json JSON-элемент, который нужно десериализовать.
     * @param typeOfT Тип объекта, в который нужно десериализовать JSON.
     * @param context Контекст десериализации JSON.
     * @return Объект ApiEntity, полученный из десериализации JSON.
     */
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): ApiEntity {
        val jsonObject = json.asJsonObject
        val status = jsonObject.get("status").asString
        val revision = jsonObject.get("revision").asInt
        val element = if (jsonObject.has("element")) {
            // Десериализация одиночного элемента
            context.deserialize<TodoItem>(jsonObject.get("element"), TodoItem::class.java)
        } else {
            null
        }
        val list = if (jsonObject.has("list")) {
            // Десериализация списка элементов
            val listArray = jsonObject.getAsJsonArray("list")
            val itemList = mutableListOf<TodoItem>()
            for (itemJson in listArray) {
                val item = context.deserialize<TodoItem>(itemJson, TodoItem::class.java)
                itemList.add(item)
            }
            itemList
        } else {
            null
        }

        return ApiEntity(status, element, list, revision)
    }
}