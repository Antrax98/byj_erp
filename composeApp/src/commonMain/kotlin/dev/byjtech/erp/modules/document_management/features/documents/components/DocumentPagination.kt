package dev.byjtech.erp.modules.document_management.features.documents.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.byjtech.erp.modules.document_management.dto.DocumentSearchResponse

@Composable
fun DocumentPagination(
    searchResponse: DocumentSearchResponse,
    onPageChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    if (searchResponse.totalPages <= 1) return
    
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Información de la página actual
            Text(
                text = "Página ${searchResponse.page} de ${searchResponse.totalPages} • ${searchResponse.totalCount} documentos",
                style = MaterialTheme.typography.bodyMedium
            )
            
            // Controles de navegación
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Botón página anterior
                IconButton(
                    onClick = { onPageChange(searchResponse.page - 1) },
                    enabled = searchResponse.hasPreviousPage
                ) {
                    Icon(Icons.Default.KeyboardArrowLeft, "Página anterior")
                }
                
                // Números de página
                val startPage = maxOf(1, searchResponse.page - 2)
                val endPage = minOf(searchResponse.totalPages, searchResponse.page + 2)
                
                for (pageNum in startPage..endPage) {
                    if (pageNum == searchResponse.page) {
                        Button(
                            onClick = { },
                            modifier = Modifier.size(40.dp),
                            enabled = false
                        ) {
                            Text(
                                text = pageNum.toString(),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    } else {
                        OutlinedButton(
                            onClick = { onPageChange(pageNum) },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Text(pageNum.toString())
                        }
                    }
                }
                
                // Botón página siguiente
                IconButton(
                    onClick = { onPageChange(searchResponse.page + 1) },
                    enabled = searchResponse.hasNextPage
                ) {
                    Icon(Icons.Default.KeyboardArrowRight, "Página siguiente")
                }
            }
        }
    }
}
