package dev.byjtech.erp.modules.document_management.features.documents.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import dev.byjtech.erp.modules.document_management.domain.model.DocumentStatus
import dev.byjtech.erp.modules.document_management.request.DocumentSearchRequest
import dev.byjtech.erp.modules.document_management.request.SortDirection
import dev.byjtech.erp.modules.document_management.utils.getStatusDisplayName
import kotlinx.datetime.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentSearchFilters(
    searchRequest: DocumentSearchRequest,
    onSearchRequestChange: (DocumentSearchRequest) -> Unit,
    onSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }
    
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Búsqueda básica
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = searchRequest.searchText ?: "",
                    onValueChange = { text ->
                        onSearchRequestChange(searchRequest.copy(searchText = text.takeIf { it.isNotBlank() }))
                    },
                    label = { Text("Búsqueda general") },
                    placeholder = { Text("Buscar en documentos...") },
                    modifier = Modifier.weight(1f)
                )
                
                Button(
                    onClick = onSearch
                ) {
                    Text("Buscar")
                }
                
                TextButton(
                    onClick = { isExpanded = !isExpanded }
                ) {
                    Text(if (isExpanded) "Menos filtros" else "Más filtros")
                }
            }
            
            // Filtros avanzados
            if (isExpanded) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        // Número de documento
                        OutlinedTextField(
                            value = searchRequest.documentNumber ?: "",
                            onValueChange = { number ->
                                onSearchRequestChange(searchRequest.copy(documentNumber = number.takeIf { it.isNotBlank() }))
                            },
                            label = { Text("Número") },
                            modifier = Modifier.width(150.dp)
                        )
                    }
                    
                    item {
                        // Estado
                        var statusExpanded by remember { mutableStateOf(false) }
                        ExposedDropdownMenuBox(
                            expanded = statusExpanded,
                            onExpandedChange = { statusExpanded = !statusExpanded },
                            modifier = Modifier.width(150.dp)
                        ) {
                            OutlinedTextField(
                                value = searchRequest.status?.let { getStatusDisplayName(it) } ?: "Todos",
                                onValueChange = { },
                                readOnly = true,
                                label = { Text("Estado") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = statusExpanded) },
                                modifier = Modifier.menuAnchor()
                            )
                            ExposedDropdownMenu(
                                expanded = statusExpanded,
                                onDismissRequest = { statusExpanded = false }
                            ) {
                                DropdownMenuItem(
                                    onClick = {
                                        onSearchRequestChange(searchRequest.copy(status = null))
                                        statusExpanded = false
                                    },
                                    text = { Text("Todos") }
                                )
                                DocumentStatus.entries.forEach { status ->
                                    DropdownMenuItem(
                                        onClick = {
                                            onSearchRequestChange(searchRequest.copy(status = status))
                                            statusExpanded = false
                                        },
                                        text = { Text(getStatusDisplayName(status)) }
                                    )
                                }
                            }
                        }
                    }
                    
                    item {
                        // Moneda
                        OutlinedTextField(
                            value = searchRequest.currency ?: "",
                            onValueChange = { currency ->
                                onSearchRequestChange(searchRequest.copy(currency = currency.takeIf { it.isNotBlank() }))
                            },
                            label = { Text("Moneda") },
                            modifier = Modifier.width(120.dp)
                        )
                    }
                    
                    item {
                        // Monto mínimo
                        OutlinedTextField(
                            value = searchRequest.minAmount?.toString() ?: "",
                            onValueChange = { amount ->
                                val doubleAmount = amount.toDoubleOrNull()
                                onSearchRequestChange(searchRequest.copy(minAmount = doubleAmount))
                            },
                            label = { Text("Monto mín.") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.width(120.dp)
                        )
                    }
                    
                    item {
                        // Monto máximo
                        OutlinedTextField(
                            value = searchRequest.maxAmount?.toString() ?: "",
                            onValueChange = { amount ->
                                val doubleAmount = amount.toDoubleOrNull()
                                onSearchRequestChange(searchRequest.copy(maxAmount = doubleAmount))
                            },
                            label = { Text("Monto máx.") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.width(120.dp)
                        )
                    }
                }
                
                // Filtros adicionales con checkboxes
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = searchRequest.isOverdue == true,
                            onCheckedChange = { checked ->
                                onSearchRequestChange(searchRequest.copy(isOverdue = if (checked) true else null))
                            }
                        )
                        Text(
                            text = "Solo documentos vencidos",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = searchRequest.includeInactive == true,
                            onCheckedChange = { checked ->
                                onSearchRequestChange(searchRequest.copy(includeInactive = if (checked) true else null))
                            }
                        )
                        Text(
                            text = "Incluir documentos desactivados",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                
                // Botones de acción
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
                ) {
                    TextButton(
                        onClick = {
                            onSearchRequestChange(
                                DocumentSearchRequest(
                                    page = 1,
                                    pageSize = searchRequest.pageSize,
                                    sortBy = searchRequest.sortBy,
                                    sortDirection = searchRequest.sortDirection
                                )
                            )
                        }
                    ) {
                        Text("Limpiar filtros")
                    }
                    
                    Button(
                        onClick = onSearch
                    ) {
                        Text("Aplicar filtros")
                    }
                }
            }
        }
    }
}
