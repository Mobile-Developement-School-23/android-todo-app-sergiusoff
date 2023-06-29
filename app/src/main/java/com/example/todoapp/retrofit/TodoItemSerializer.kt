package com.example.todoapp.retrofit

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

class TodoItemSerializer : JsonSerializer<TodoItem>, JsonDeserializer<TodoItem> {
    override fun serialize(
        src: TodoItem?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        val json = JsonObject()
        src?.let { item ->
            json.addProperty("id", item.id.toString())
            json.addProperty("text", item.text)
            json.addProperty("importance", item.importance.toString())
            json.addProperty("deadline", item.deadline?.time)
            json.addProperty("isDone", item.isDone)
            json.addProperty("creationDate", item.creationDate.time)
            json.addProperty("lastModificationDate", item.lastModificationDate.time)
        }
        return json
    }

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): TodoItem {
        val jsonObject = json?.asJsonObject
        val id = UUID.fromString(jsonObject?.get("id")?.asString)
        val text = jsonObject?.get("text")?.asString ?: ""
        val importance = Importance.valueOf(jsonObject?.get("importance")?.asString ?: "")
        val deadline = jsonObject?.get("deadline")?.asLong?.let { Date(it) }
        val isDone = jsonObject?.get("isDone")?.asBoolean ?: false
        val creationDate = Date(jsonObject?.get("creationDate")?.asLong ?: 0)
        val lastModificationDate =  Date(jsonObject?.get("lastModificationDate")?.asLong ?: 0)
        return TodoItem(id, text, importance, deadline, isDone, creationDate, lastModificationDate)
    }
}