package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.featuresList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.byjtech.erp.common.ButtonMetadata
import dev.byjtech.erp.common.ComponentConfig

@Composable
//fun FeatureListScreen (component: FeatureListComponent) {
//    val state by component.state.collectAsState()
//
//    Column(
//        verticalArrangement = Arrangement.Top,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//
//        val moduleFeaturesMap = component.buttonsMap
//
//        LazyColumn(
//            modifier = Modifier.fillMaxSize(),
//        ) {
//            moduleFeaturesMap.forEach { (moduleName, featuresMap) ->
//                item {
//                    Text(
//                        text = moduleName,
//                        modifier = Modifier.fillMaxWidth(),
//                        style = MaterialTheme.typography.titleLarge
//                    )
//                    LazyRow {
//                        featuresMap.forEach { (featureName, buttonMetadata) ->
//                            item {
//                                Button(onClick = {component.navTo(ComponentConfig(moduleName, featureName))}){
//                                    Text(buttonMetadata.displayName)
//                                }
//                            }
//                        }
//                    }
//                    HorizontalDivider(thickness = 1.dp)
//                }
//            }
//        }
//    }
//}
fun FeatureListScreen(component: FeatureListComponent) {
    val state by component.state.collectAsState()
    val moduleFeaturesMap = component.buttonsMap

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        moduleFeaturesMap.forEach { (moduleName, featuresMap) ->
            item {
                Column {
                    Text(
                        text = moduleName,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        style = MaterialTheme.typography.titleLarge
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(horizontal = 4.dp)
                    ) {
                        featuresMap.forEach { (featureName, buttonMetadata) ->
                            item {
                                FeatureButton(
                                    metadata = buttonMetadata,
                                    onClick = {
                                        component.navTo(ComponentConfig(moduleName, featureName))
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
                }
            }
        }
    }
}

@Composable
fun FeatureButton(metadata: ButtonMetadata, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
//        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        colors = CardDefaults.cardColors(containerColor = metadata.color ),
        modifier = Modifier
            .widthIn(min = 120.dp)
            .height(70.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Column {
                metadata.icon?.let {
                    Icon(
                        imageVector = it,
                        contentDescription = metadata.displayName,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
                Text(
                    text = metadata.displayName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(4.dp)
                )
            }

        }
    }
}
