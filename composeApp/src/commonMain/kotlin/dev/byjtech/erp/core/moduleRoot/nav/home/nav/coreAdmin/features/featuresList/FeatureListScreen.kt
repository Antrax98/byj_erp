package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.featuresList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.byjtech.erp.common.ComponentConfig

@Composable
fun FeatureListScreen (component: FeatureListComponent) {
    val state by component.state.collectAsState()

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val dummyNames = listOf(
            "users", "roles", "company",
            "Botón 4", "Botón 5", "Botón 6"
        )

//        Text("Lista de Features")
//
//        LazyVerticalGrid(
//            columns = GridCells.Adaptive(minSize = 120.dp),
//            contentPadding = PaddingValues(16.dp),
//            horizontalArrangement = Arrangement.spacedBy(12.dp),
//            verticalArrangement = Arrangement.spacedBy(12.dp),
//            //modifier = Modifier.fillMaxSize()
//        ) {
//            items(dummyNames) { name ->
//                Button(
//                    onClick = { /*NADA*/ },
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    Text(name)
//                }
//            }
//        }

//        val flattenedButtons = component.buttonsMap.flatMap { (moduleName, featuresMap) ->
//            featuresMap.map { (featureName, buttonMetadata) ->
//                Triple(moduleName, featureName, buttonMetadata)
//            }
//        }
//
//        LazyVerticalGrid(
//            columns = GridCells.Adaptive(minSize = 120.dp),
//            contentPadding = PaddingValues(16.dp),
//            horizontalArrangement = Arrangement.spacedBy(12.dp),
//            verticalArrangement = Arrangement.spacedBy(12.dp),
//        ) {
//            items(flattenedButtons) { (moduleName, featureName, buttonMetadata) ->
//                Button(
//                    onClick = {
//                        val config = ComponentConfig(moduleName, featureName)
//                        component.navTo(config)
//                    },
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    Text(buttonMetadata.displayName)
//                }
//            }
//        }

        val moduleFeaturesMap = component.buttonsMap

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
        ) {
            moduleFeaturesMap.forEach { (moduleName, featuresMap) ->
                item {
                    Text(
                        text = moduleName,
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.titleLarge
                    )
                    LazyRow {
                        featuresMap.forEach { (featureName, buttonMetadata) ->
                            item {
                                Button(onClick = {component.navTo(ComponentConfig(moduleName, featureName))}){
                                    Text(buttonMetadata.displayName)
                                }
                            }
                        }
                    }
                    HorizontalDivider(thickness = 1.dp)
                }
            }
        }
    }
}