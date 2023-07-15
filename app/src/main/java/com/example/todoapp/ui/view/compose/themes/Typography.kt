package com.example.todoapp.ui.view.compose.themes

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp


val customTypography = Typography(
    h1 = TextStyle(
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.3.sp
    ),
    h2 = TextStyle(
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.3.sp
    ),
    h3 = TextStyle(
        fontSize = 18.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.1.sp
    ),
    body1 = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal
    ),
    button = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal
    )
)