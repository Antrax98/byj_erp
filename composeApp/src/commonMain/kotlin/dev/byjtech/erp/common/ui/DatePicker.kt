package dev.byjtech.erp.common.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.datetime.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerField(
    value: LocalDate?,
    onValueChange: (LocalDate?) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    supportingText: @Composable (() -> Unit)? = null,
    minDate: LocalDate? = null,  // Nueva propiedad para fecha mínima
    maxDate: LocalDate? = null   // Nueva propiedad para fecha máxima
) {
    var showDatePicker by remember { mutableStateOf(false) }
    
    OutlinedTextField(
        value = value?.toString() ?: "",
        onValueChange = { }, // No permitir edición de texto
        label = { Text(label) },
        modifier = modifier.clickable { showDatePicker = true },
        readOnly = true,
        trailingIcon = {
            IconButton(onClick = { showDatePicker = true }) {
                Icon(Icons.Default.ArrowDropDown, contentDescription = "Seleccionar fecha")
            }
        },
        isError = isError,
        supportingText = supportingText,
        colors = OutlinedTextFieldDefaults.colors(
            disabledTextColor = MaterialTheme.colorScheme.onSurface,
            disabledBorderColor = MaterialTheme.colorScheme.outline,
            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    )
    
    if (showDatePicker) {
        DatePickerDialog(
            selectedDate = value,
            onDateSelected = { selectedDate ->
                onValueChange(selectedDate)
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false },
            minDate = minDate,  // Pasar parámetros de validación
            maxDate = maxDate
        )
    }
}

@Composable
private fun DatePickerDialog(
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit,
    minDate: LocalDate? = null,
    maxDate: LocalDate? = null
) {
    // Fecha actual simplificada - usando la fecha actual real
    val currentYear = 2025
    val currentMonth = 7  // Julio
    val currentDay = 25   // Actualizado a la fecha actual
    
    var displayedMonth by remember { mutableStateOf(selectedDate?.monthNumber ?: currentMonth) }
    var displayedYear by remember { mutableStateOf(selectedDate?.year ?: currentYear) }
    
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .width(320.dp)
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // Header con navegación de mes/año
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        if (displayedMonth == 1) {
                            displayedMonth = 12
                            displayedYear--
                        } else {
                            displayedMonth--
                        }
                    }) {
                        Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Mes anterior")
                    }
                    
                    Text(
                        text = "${getMonthName(displayedMonth)} $displayedYear",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    IconButton(onClick = {
                        if (displayedMonth == 12) {
                            displayedMonth = 1
                            displayedYear++
                        } else {
                            displayedMonth++
                        }
                    }) {
                        Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Mes siguiente")
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Días de la semana
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    val dayNames = listOf("Dom", "Lun", "Mar", "Mié", "Jue", "Vie", "Sáb")
                    dayNames.forEach { dayName ->
                        Text(
                            text = dayName,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Grid de días simplificado
                val daysInMonth = getDaysInMonth(displayedMonth, displayedYear)
                val firstDayOfWeek = getFirstDayOfWeek(displayedMonth, displayedYear)
                
                LazyVerticalGrid(
                    columns = GridCells.Fixed(7),
                    modifier = Modifier.height(240.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Espacios vacíos antes del primer día del mes
                    items(firstDayOfWeek) {
                        Spacer(modifier = Modifier.size(32.dp))
                    }
                    
                    // Días del mes
                    items(daysInMonth) { day ->
                        // Validar que la fecha sea válida antes de crear LocalDate
                        val isValidDate = try {
                            LocalDate(displayedYear, displayedMonth, day)
                            true
                        } catch (e: Exception) {
                            false
                        }
                        
                        if (isValidDate) {
                            val date = LocalDate(displayedYear, displayedMonth, day)
                            val isSelected = selectedDate == date
                            val isToday = (displayedYear == currentYear && displayedMonth == currentMonth && day == currentDay)
                            
                            // Validar restricciones de fecha mínima y máxima
                            val isDateEnabled = when {
                                minDate != null && date < minDate -> false
                                maxDate != null && date > maxDate -> false
                                else -> true
                            }
                            
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        when {
                                            isSelected -> MaterialTheme.colorScheme.primary
                                            isToday && isDateEnabled -> MaterialTheme.colorScheme.primaryContainer
                                            else -> Color.Transparent
                                        }
                                    )
                                    .border(
                                        width = if (isToday && !isSelected && isDateEnabled) 1.dp else 0.dp,
                                        color = MaterialTheme.colorScheme.primary,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .clickable(enabled = isDateEnabled) { 
                                        if (isDateEnabled) {
                                            onDateSelected(date) 
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = day.toString(),
                                    color = when {
                                        !isDateEnabled -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                                        isSelected -> MaterialTheme.colorScheme.onPrimary
                                        isToday -> MaterialTheme.colorScheme.primary
                                        else -> MaterialTheme.colorScheme.onSurface
                                    },
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        } else {
                            // Espacio vacío para fechas inválidas
                            Spacer(modifier = Modifier.size(32.dp))
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Botones de acción
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancelar")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(onClick = {
                        try {
                            val todayDate = LocalDate(currentYear, currentMonth, currentDay)
                            // Validar que la fecha de hoy está dentro de los límites permitidos
                            val isTodayValid = when {
                                minDate != null && todayDate < minDate -> false
                                maxDate != null && todayDate > maxDate -> false
                                else -> true
                            }
                            if (isTodayValid) {
                                onDateSelected(todayDate)
                            }
                        } catch (e: Exception) {
                            // Si no se puede crear la fecha de hoy, usar la fecha seleccionada actual o una fecha válida
                            val fallbackDate = selectedDate ?: LocalDate(2025, 1, 1)
                            onDateSelected(fallbackDate)
                        }
                    }, enabled = run {
                        // Habilitar el botón "Hoy" solo si la fecha actual está dentro de los límites
                        try {
                            val todayDate = LocalDate(currentYear, currentMonth, currentDay)
                            when {
                                minDate != null && todayDate < minDate -> false
                                maxDate != null && todayDate > maxDate -> false
                                else -> true
                            }
                        } catch (e: Exception) {
                            false
                        }
                    }) {
                        Text("Hoy")
                    }
                }
            }
        }
    }
}

private fun getMonthName(month: Int): String {
    return when (month) {
        1 -> "Enero"
        2 -> "Febrero"
        3 -> "Marzo"
        4 -> "Abril"
        5 -> "Mayo"
        6 -> "Junio"
        7 -> "Julio"
        8 -> "Agosto"
        9 -> "Septiembre"
        10 -> "Octubre"
        11 -> "Noviembre"
        12 -> "Diciembre"
        else -> "Mes"
    }
}

private fun getDaysInMonth(month: Int, year: Int): Int {
    return when (month) {
        1, 3, 5, 7, 8, 10, 12 -> 31
        4, 6, 9, 11 -> 30
        2 -> if (isLeapYear(year)) 29 else 28
        else -> 30
    }
}

private fun isLeapYear(year: Int): Boolean {
    return year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)
}

private fun getFirstDayOfWeek(month: Int, year: Int): Int {
    // Algoritmo de Zeller para calcular el día de la semana
    val adjustedMonth = if (month < 3) month + 12 else month
    val adjustedYear = if (month < 3) year - 1 else year
    val century = adjustedYear / 100
    val yearOfCentury = adjustedYear % 100
    
    val dayOfWeek = (1 + (13 * (adjustedMonth + 1)) / 5 + yearOfCentury + yearOfCentury / 4 + century / 4 - 2 * century) % 7
    
    // Convertir resultado de Zeller (0=sábado) a nuestro formato (0=domingo)
    return when (dayOfWeek) {
        0 -> 6  // Sábado -> 6
        1 -> 0  // Domingo -> 0
        2 -> 1  // Lunes -> 1
        3 -> 2  // Martes -> 2
        4 -> 3  // Miércoles -> 3
        5 -> 4  // Jueves -> 4
        6 -> 5  // Viernes -> 5
        else -> 0
    }
}
