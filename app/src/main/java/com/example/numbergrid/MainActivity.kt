package com.example.numbergrid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.numbergrid.ui.theme.NumberGridTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NumberGridTheme {

                InfiniteNumberGrid()
            }
        }
    }
}


@Composable
fun InfiniteNumberGrid() {
    val numbers = remember { mutableStateListOf<Int>() }

    LaunchedEffect(Unit) {
        numbers.addAll(1..30)
    }

    val gridState = rememberLazyGridState()

    // Detect when user scrolls near bottom and load more
    LaunchedEffect(gridState) {
        snapshotFlow {
            val visibleItemCount = gridState.layoutInfo.visibleItemsInfo.size
            val firstVisibleIndex = gridState.firstVisibleItemIndex
            firstVisibleIndex + visibleItemCount
        }.collectLatest { lastVisible ->
            if (lastVisible >= numbers.size) {
                //small delay
                delay(800)
                val start = numbers.lastOrNull() ?: 0
                val newNumbers = ((start + 1)..(start + 30))
                numbers.addAll(newNumbers)
            }
        }
    }

    Column {

        Button(
            onClick = {
                numbers.clear()
                numbers.addAll(1..30)
            },
            modifier = Modifier
                .padding(8.dp)
        ) {
            Text("Reset to 1–30")
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            state = gridState,
            modifier = Modifier
        ) {
            items(numbers.size) { index ->
                Text(numbers[index].toString())
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NumberGridTheme {
        InfiniteNumberGrid()
    }
}