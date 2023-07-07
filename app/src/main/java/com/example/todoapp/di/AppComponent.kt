package com.example.todoapp.di

import android.content.Context
import androidx.room.Room
import com.example.todoapp.database.TodoItemsDatabase
import com.example.todoapp.model.TodoItem
import com.example.todoapp.model.TodoItemsRepository
import com.example.todoapp.model.TodoItemsRepositoryImpl
import com.example.todoapp.model.UpdateDataWorker
import com.example.todoapp.retrofit.ApiEntity
import com.example.todoapp.retrofit.ApiResponseSerializer
import com.example.todoapp.retrofit.NetworkSchedulerService
import com.example.todoapp.retrofit.TodoItemSerializer
import com.example.todoapp.retrofit.TodoItemsApiService
import com.example.todoapp.utils.DataSynchronizer
import com.example.todoapp.view.CreateEditFragment
import com.example.todoapp.view.MainActivity
import com.example.todoapp.view.TodoListFragment
import com.google.gson.GsonBuilder
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Scope
import javax.inject.Singleton

@Scope
annotation class AppScope

@AppScope
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(createEditFragment: CreateEditFragment)
    fun inject(todoListFragment: TodoListFragment)
    fun inject(networkSchedulerService: NetworkSchedulerService)
    fun inject(updateDataWorker: UpdateDataWorker)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): Builder
        fun build(): AppComponent
    }
}

@Module(includes = [NetworkModule::class, AppBindModule::class])
class AppModule{

    @Provides
    fun provideDatabase(context: Context): TodoItemsDatabase {
        return Room.databaseBuilder(
            context,
            TodoItemsDatabase::class.java,
            "todoItemsDatabase"
        ).build()
    }
}

@Module
class NetworkModule{
    @Provides
    fun provideTodoItemService(): TodoItemsApiService {
        return Retrofit.Builder()
            .baseUrl("https://beta.mrdekk.ru/todobackend/")
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .registerTypeAdapter(TodoItem::class.java, TodoItemSerializer())
                        .registerTypeAdapter(ApiEntity::class.java, ApiResponseSerializer())
                        .create()))
            .client(OkHttpClient.Builder().addInterceptor { chain ->
                val modifiedRequest = chain.request().newBuilder()
                    .header("Authorization", "Bearer agouara")
                    .build()
                chain.proceed(modifiedRequest)
            }.build())
            .build()
            .create(TodoItemsApiService::class.java)
    }
}

@Module
interface AppBindModule {
    @Binds
    @AppScope
    fun bindTodoItemsRepository(
        todoItemsRepositoryImpl: TodoItemsRepositoryImpl
    ): TodoItemsRepository
}