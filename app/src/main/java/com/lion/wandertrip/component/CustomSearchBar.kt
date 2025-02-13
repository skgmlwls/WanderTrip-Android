package com.lion.a02_boardcloneproject.component

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    searchValue:MutableState<String>,
    placeholder:String = "검색어를 입력해주세요",
    trailingIcon:ImageVector? = null,
    trailingIconOnClick: () -> Unit = {},
    searchResultComposable : @Composable () -> Unit,
    contentComposable: @Composable () -> Unit,
) {

    var expandedValue by rememberSaveable {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ){
        // SearchBar
        SearchBar(
            modifier = Modifier.align(Alignment.TopCenter),
            inputField = {
                SearchBarDefaults.InputField(
                    placeholder = {
                        Text(text = placeholder)
                    },
                    // 키보드의 엔터키를 누르면 동작하는 부분
                    onSearch = {
                        
                    },
                    expanded = expandedValue,
                    onExpandedChange = {
                        expandedValue = it
                        searchValue.value = ""
                    },
                    query = searchValue.value,
                    onQueryChange = {
                        searchValue.value = it
                    },
                    leadingIcon = {
                        Crossfade(
                            expandedValue,
                            label = "",
                            animationSpec = tween(120),
                        ) {
                            if(it){
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = null,
                                    modifier = Modifier.clickable {
                                        expandedValue = false
                                        searchValue.value = ""
                                    }
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null
                                )
                            }
                        }
                    },
                    trailingIcon = {
                        Crossfade(
                            expandedValue,
                            label = "",
                            animationSpec = tween(120),
                        ) {
                            if(it){
                                Icon(
                                    imageVector = Icons.Filled.Clear,
                                    contentDescription = null,
                                    modifier = Modifier.clickable {
                                        searchValue.value = ""
                                    }
                                )
                            } else {
                                if(trailingIcon != null) {
                                    Icon(
                                        imageVector = trailingIcon,
                                        contentDescription = null,
                                        modifier = Modifier.clickable {
                                            trailingIconOnClick()
                                        }
                                    )
                                }
                            }
                        }
                    },
                )
            },
            expanded = expandedValue,
            onExpandedChange = {
                expandedValue = it
            }
        ) {
            // 검색 결과를 구성해주는 부분
            searchResultComposable()
        }

        // 검색바 하단의 본문 부분
        // contentComposable()
        Column(
            modifier = Modifier.padding(top = 120.dp)
        ) {
            contentComposable()
        }
    }
}
