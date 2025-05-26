package dev.byjtech.erp.core.moduleRoot.nav.home.nav.moduleList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ModuleListScreen(component: ModuleListComponent) {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text("TEXTO DEL CORE ADMIN ROOT")
        Button(onClick = { component.navTo("core")}){
            Text("to Core")
        }

        //ModuleGrid(modules = component.modulesMetadata, onClick = {component.navTo})

        val modulePairList = component.modulesMetadata.toList()
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 120.dp), // Se adapta a la pantalla
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(modulePairList) { (name, metadata) ->
                Button(onClick = { component.navTo(name) }){
                    Text(name)

                }
            }

        }

    }
}