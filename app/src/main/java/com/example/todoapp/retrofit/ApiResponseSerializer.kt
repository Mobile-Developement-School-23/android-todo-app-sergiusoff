package com.example.todoapp.retrofit

import android.util.Log
import com.example.todoapp.model.TodoItem
import com.google.gson.JsonArray
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

class ApiResponseSerializer : JsonSerializer<ApiEntity>, JsonDeserializer<ApiEntity> {

    override fun serialize(src: ApiEntity, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val jsonObject = JsonObject()
        jsonObject.addProperty("status", src.status)

        if (src.element != null) {
            jsonObject.add("element", context.serialize(src.element, TodoItem::class.java))
        } else if (src.list != null) {
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

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): ApiEntity {
        val jsonObject = json.asJsonObject
        val status = jsonObject.get("status").asString
        val revision = jsonObject.get("revision").asInt
        val element = if (jsonObject.has("element")) {
            context.deserialize<TodoItem>(jsonObject.get("element"), TodoItem::class.java)
        } else {
            null
        }
        val list = if (jsonObject.has("list")) {
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