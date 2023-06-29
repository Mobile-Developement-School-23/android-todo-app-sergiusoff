package com.example.todoapp.retrofit

import com.example.todoapp.model.TodoItem
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

class ApiResponseSerializer : JsonSerializer<ApiResponse>, JsonDeserializer<ApiResponse> {

    override fun serialize(src: ApiResponse, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val jsonObject = JsonObject()
        jsonObject.addProperty("status", src.status)
        jsonObject.add("element", context.serialize(src.element, TodoItem::class.java))
        jsonObject.addProperty("revision", src.revision)
        return jsonObject
    }

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): ApiResponse {
        val jsonObject = json.asJsonObject
        val status = jsonObject.get("status").asString
        val element = context.deserialize<TodoItem>(jsonObject.get("element"), TodoItem::class.java)
        val revision = jsonObject.get("revision").asInt
        return ApiResponse(status, element, revision)
    }
}