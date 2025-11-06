package com.example.senai.sp.testecalendario.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CalendarioViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CalendarioViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CalendarioViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
