package com.example.myhealthydiet.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myhealthydiet.ui.theme.BrandOrange
import com.example.myhealthydiet.ui.theme.Black

val FieldShape = RoundedCornerShape(8.dp)

@Composable
fun BrandTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    supportingText: @Composable (() -> Unit)? = null,
    singleLine: Boolean = true,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier.fillMaxWidth(),
        shape = FieldShape,
        singleLine = singleLine,
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
        trailingIcon = trailingIcon,
        isError = isError,
        supportingText = supportingText,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = BrandOrange,
            unfocusedBorderColor = BrandOrange,
            focusedLabelColor = BrandOrange,
            unfocusedLabelColor = Color(0xFF757575),
            cursorColor = BrandOrange,
            errorBorderColor = MaterialTheme.colorScheme.error,
        )
    )
}

@Composable
fun BrandButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = BrandOrange,
            contentColor = Black,
            disabledContainerColor = BrandOrange.copy(alpha = 0.4f),
            disabledContentColor = Black.copy(alpha = 0.4f),
        )
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
        )
    }
}

@Composable
fun SectionTitle(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium.copy(
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = Black,
        ),
        modifier = modifier,
    )
}