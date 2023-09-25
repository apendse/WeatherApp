package com.aap.compose.weatherapp.screens.home

import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import com.aap.compose.weatherapp.R
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherSearchBar(onSearchCallback: (query: String)->Unit ) {
    var query by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }
    SearchBar(
        modifier = Modifier.fillMaxWidth(),
        query = query,
        onQueryChange = { query = it },
        onSearch = { newQuery ->
            active = false
            println("NewQuery $newQuery")
            onSearchCallback(query)
        },
        active = active,
        onActiveChange = { active = it },
        placeholder = {
            Text(
                text =  stringResource(id = R.string.search_hint), color = Color.Gray
            )
        },
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = "search")

        }) {

    }
}