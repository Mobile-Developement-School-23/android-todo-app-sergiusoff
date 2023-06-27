package com.example.todoapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.example.todoapp.App
import com.example.todoapp.R
import com.example.todoapp.model.TodoItem
import com.example.todoapp.utils.Navigator
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * Основная активность приложения, реализующая навигацию между фрагментами и операции с элементами списка дел.
 */
class MainActivity : AppCompatActivity(), Navigator {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Настройка цвета системной панели
        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.back_primary)
        if (savedInstanceState == null) {
            // Добавление фрагмента TodoListFragment при первом создании активности
            supportFragmentManager.beginTransaction()
                .add(R.id.container, TodoListFragment())
                .commit()
        }
        // Обработка нажатия на плавающую кнопку "Добавить"
        findViewById<FloatingActionButton>(R.id.floatingActionButton).setOnClickListener {
            showDetails(-1)
        }
        // Скрытие плавающей кнопки "Добавить", если текущий фрагмент - CreateEditFragment (при пересоздании экрана)
        if (supportFragmentManager.findFragmentById(R.id.container) != null &&
            supportFragmentManager.findFragmentById(R.id.container) is CreateEditFragment){
            findViewById<FloatingActionButton>(R.id.floatingActionButton).hide()
        }
    }

    /**
     * Отображает фрагмент для создания/редактирования элемента списка дел.
     * @param n Индекс элемента, который нужно редактировать (-1 для создания нового элемента).
     */
    override fun showDetails(n: Int) {
        findViewById<FloatingActionButton>(R.id.floatingActionButton).hide()
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, CreateEditFragment.newInstance(n))
            .addToBackStack(null)
            .commit()
    }

    /**
     * Отображает список элементов и показывает плавающую кнопку "Добавить".
     */
    override fun showList() {
        findViewById<FloatingActionButton>(R.id.floatingActionButton).show()
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }
}