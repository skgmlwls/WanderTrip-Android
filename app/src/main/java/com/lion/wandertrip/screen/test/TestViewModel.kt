package com.lion.wandertrip.screen.test

import android.content.Context
import androidx.lifecycle.ViewModel
import com.lion.wandertrip.TripApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class TestViewModel @Inject constructor(
    @ApplicationContext context : Context,
) : ViewModel() {
    val tripApplication = context.applicationContext as TripApplication



}