package com.example.todoapp.ui.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.todoapp.appComponent
import com.example.todoapp.ioc.CreateEditViewModelFactory
import com.example.todoapp.model.Importance
import com.example.todoapp.model.TodoItem
import com.example.todoapp.ui.stateholders.CreateEditViewModel
import com.example.todoapp.ui.view.compose.manage.CreateEditManageScreen
import com.example.todoapp.ui.view.compose.themes.CreateEditTheme
import com.example.todoapp.utils.navigator
import java.util.Date
import java.util.UUID
import javax.inject.Inject

class CreateEditFragment : Fragment() {
    private var tempItem: TodoItem? = null

    @Inject
    lateinit var factory: CreateEditViewModelFactory.Factory
    companion object {
        private const val ARG_MY_ITEM = "arg_my_int"
        fun newInstance(todoItem: TodoItem?): CreateEditFragment {
            val fragment = CreateEditFragment()
            val args = Bundle()
            args.putSerializable(ARG_MY_ITEM, todoItem)
            fragment.arguments = args
            return fragment
        }
    }

    private val createEditViewModel: CreateEditViewModel by viewModels {
        factory.create(tempItem?:TodoItem(UUID.randomUUID(), "", Importance.LOW, Date(), false, Date(), Date()))
    }

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            tempItem = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getSerializable(ARG_MY_ITEM, TodoItem::class.java)
            } else {
                it.getSerializable(ARG_MY_ITEM) as TodoItem?
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setContent {
                CreateEditTheme{
                    CreateEditManageScreen(
                        uiState = createEditViewModel.todoItem.collectAsState(),
                        onEvent = createEditViewModel::receiveEvent,
                        close = this@CreateEditFragment::onCancelPressed
                    )
                }
            }
        }
    }

    private fun onCancelPressed() {
        navigator().showList()
    }
}