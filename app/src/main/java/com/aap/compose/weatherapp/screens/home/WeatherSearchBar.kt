package com.aap.compose.weatherapp.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.unit.dp
import com.aap.compose.weatherapp.R
import com.aap.compose.weatherapp.data.GeoLocationData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherSearchBar(recentEntries: List<GeoLocationData>, onRecentSearchClick: (location: GeoLocationData) -> Unit, onSearchCallback: (query: String) -> Unit) {
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
                text = stringResource(id = R.string.search_hint), color = Color.Gray
            )
        },
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = stringResource(id = R.string.search_icon_content_desc))

        },
        trailingIcon = {
            Icon(
                modifier = Modifier.clickable {
                    if (active) {
                        if (query.isNotEmpty()) {
                            query = ""
                        } else {
                            active = false
                        }
                    }
                },
                imageVector = Icons.Default.Close,
                contentDescription = stringResource(id = R.string.search_close_icon_content_desc)
            )
        }) {
        recentEntries.forEach {
            Row(modifier = Modifier
                .padding(all = 14.dp)
                .clickable {
                    onRecentSearchClick(it)
                    active = false
                }) {
                Icon(imageVector = Icons.Default.History, contentDescription = "search")
                val rowTitle = it.buildRowTitle()
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = rowTitle)
            }
        }
    }
}

private fun GeoLocationData.buildRowTitle(): String {
    val sb = StringBuilder(name)
    if (isCurrentLocation) {
        return sb.toString()
    }
    if (state != null) {
        sb.append(", ").append(state)
    }
    if (country.isNotEmpty() && country != state) {
        sb.append(", ").append(country)
    }
    return sb.toString()
}