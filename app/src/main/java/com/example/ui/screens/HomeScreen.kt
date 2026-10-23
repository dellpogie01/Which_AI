package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AiRepository
import com.example.model.AiModel
import com.example.ui.MainViewModel
import java.text.NumberFormat

@Composable
fun HomeScreen(viewModel: MainViewModel, onModelClick: (AiModel) -> Unit, searchMode: Boolean = false) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val filteredModels by viewModel.filteredModels.collectAsState()
    val bookmarkedIds by viewModel.bookmarkedIds.collectAsState()
    val compareList by viewModel.compareList.collectAsState()
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(searchMode) { if (searchMode) focusRequester.requestFocus() }
    val numberFormat = remember { NumberFormat.getIntegerInstance() }
    Column(Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = searchQuery, onValueChange = viewModel::setSearchQuery,
            modifier = Modifier.fillMaxWidth().focusRequester(focusRequester).testTag("search_input"),
            placeholder = { Text("Search ${AiRepository.aiModels.size} AI tools", maxLines = 1, overflow = TextOverflow.Ellipsis) },
            leadingIcon = { Icon(Icons.Default.Search, "Search") },
            trailingIcon = { if (searchQuery.isNotEmpty()) IconButton(onClick = { viewModel.setSearchQuery("") }) { Icon(Icons.Default.Close, "Clear search") } },
            shape = RoundedCornerShape(16.dp), singleLine = true
        )
        Spacer(Modifier.height(8.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(AiRepository.categories) { category ->
                val count = if (category == "All") AiRepository.aiModels.size else AiRepository.aiModels.count { it.category == category }
                FilterChip(selected = selectedCategory == category, onClick = { viewModel.selectCategory(category) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                        selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    label = { Text("$category (${numberFormat.format(count)})") }, modifier = Modifier.testTag("category_chip_$category"))
            }
        }
        Text("${numberFormat.format(filteredModels.size)} AI tools", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(vertical = 8.dp))
        if (filteredModels.isEmpty()) {
            Column(Modifier.fillMaxWidth().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("No matching tools", style = MaterialTheme.typography.titleMedium)
                Text("Try another search or category.", style = MaterialTheme.typography.bodyMedium)
                TextButton(onClick = { viewModel.setSearchQuery(""); viewModel.selectCategory("All") }) { Text("Reset filters") }
            }
        }
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp), contentPadding = PaddingValues(bottom = 16.dp), modifier = Modifier.weight(1f).testTag("ai_model_list")) {
            items(filteredModels, key = { it.id }) { model ->
                val compared = compareList.any { it.id == model.id }
                Card(Modifier.fillMaxWidth().clickable { onModelClick(model) }.testTag("ai_card_${model.id}"), shape = RoundedCornerShape(20.dp)) {
                    Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            Text(model.emoji, fontSize = 28.sp)
                            Spacer(Modifier.width(12.dp))
                            Column(Modifier.weight(1f)) {
                                Text(model.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Text(model.category, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                            }
                            IconButton(onClick = { viewModel.toggleBookmark(model.id) }, modifier = Modifier.testTag("bookmark_btn_${model.id}")) {
                                Icon(if (model.id in bookmarkedIds) Icons.Default.Bookmark else Icons.Default.BookmarkBorder, "Save ${model.name}",
                                    tint = if (model.id in bookmarkedIds) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                        Text(model.whatItDoes, style = MaterialTheme.typography.bodyMedium)
                        Text("Best for: ${model.bestFor.firstOrNull().orEmpty()}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                        OutlinedButton(onClick = { viewModel.addToCompare(model) }, enabled = !compared,
                            modifier = Modifier.fillMaxWidth().testTag("compare_add_btn_${model.id}")) {
                            Text(if (compared) "Comparing" else "+ Compare")
                        }
                    }
                }
            }
        }
    }
}
