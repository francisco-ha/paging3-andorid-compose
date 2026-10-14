package com.example.paging3_andorid_compose.presentationUi

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.example.paging3_andorid_compose.data.RMRepository
import com.example.paging3_andorid_compose.presentationUi.model.CharacterModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

@HiltViewModel
class RickListViewModel @Inject constructor(val repository: RMRepository): ViewModel(){
    val characters: Flow<PagingData<CharacterModel>> = repository.getAllCharacters()
}