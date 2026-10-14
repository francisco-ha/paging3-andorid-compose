package com.example.paging3_andorid_compose.presentationUi


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.paging3_andorid_compose.R
import com.example.paging3_andorid_compose.presentationUi.model.CharacterModel
import kotlin.let


/**
 * Al igualar hiltViewModel inyecta automáticamente el ViewModel gestionando su ciclo de vida.
 * Asigna por defecto la instancia del ViewModel provista y retenida por Hilt.
 */
@Composable
fun RickListScreen(rickListViewModel: RickListViewModel = hiltViewModel()){

    // Convierte el Flow<PagingData<CharacterModel>> emitido por el ViewModel en un estado
    // observable por Compose (LazyPagingItems). Cualquier cambio en el flujo re-compondrá la vista.
    val characters= rickListViewModel.characters.collectAsLazyPagingItems()
    Box(modifier = Modifier.fillMaxSize()){
        Image(
            painter = painterResource(
                id = R.drawable.background),
            contentDescription = "background",
            contentScale = ContentScale.Crop,// Escala la imagen recortando excesos para llenar toda la pantalla
            modifier = Modifier.fillMaxSize()
        )
    }
    when{
        //carga inicial por primera vez (la lista está totalmente vacía)
        characters.loadState.refresh is LoadState.Loading && characters.itemCount == 0 -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                CircularProgressIndicator(
                    modifier = Modifier.size(64.dp),
                    color = Color.White
                )
            }
        }
        //Estado vacio. La petición terminó con éxito pero el servidor devolvió 0 resultados
        characters.loadState.refresh is LoadState.NotLoading && characters.itemCount == 0 -> {
            Text(text = "No hay personajes")
        }

        //Falla el internet
        characters.loadState.hasError  -> {
            Box(modifier = Modifier.fillMaxSize().background(Color.Red), contentAlignment = Alignment.Center) {
                Text(text = "Ha ocurrido un error")
            }
        }
        //Hay elementos listos para mostrar
        else -> {
            CharacterList(characters)
            // Paginación continua (Append): el usuario llegó al final y se descargan más elementos
            if (characters.loadState.append is LoadState.Loading){
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                    CircularProgressIndicator(
                        modifier = Modifier.size(64.dp),
                        color = Color.White
                    )
                }
            }
        }

    }


}

/**
 * Representa la lista desplazable verticalmente (equivalente moderno a RecyclerView).
 */
@Composable
fun CharacterList(characters: LazyPagingItems<CharacterModel>) {
    LazyColumn {
        // items(count): genera dinámicamente las celdas visibles según se desplaza la pantalla
        items(characters.itemCount){
            characters[it]?.let { characterModel ->
                ItemList(characterModel)
            }
        }
    }
    /*LazyColumn {
        // La extensión .items de Paging maneja los índices y solicitudes de forma interna y segura
        items(
            items = characters,
            key = { character -> character.id } // Evita re-renderizados usando el ID único
        ) { characterModel ->
            characterModel?.let {
                ItemList(it)
            }
        }
    }*/
}

/**
 * Tarjeta individual para cada personaje con imagen asíncrona y degradado inferior.
 */
@Composable
fun ItemList(characterModel: CharacterModel){
    Box(
        modifier = Modifier
            .padding(24.dp)
            .clip(RoundedCornerShape(24))
            .border(2.dp, Color.Green, shape = RoundedCornerShape(0,24,0,24))
            .fillMaxWidth()
            .height(250.dp),
        contentAlignment = Alignment.BottomCenter
    ){
        AsyncImage(
            model = characterModel.image,
            contentDescription = "characterImage",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier.fillMaxWidth()
                .height(60.dp)
                .background(Brush.verticalGradient(
                    listOf(Color.Black.copy(alpha = 0f)
                        ,Color.Red.copy(alpha = 6f)
                        ,Color.Yellow.copy(alpha = 1f))
                )),
            contentAlignment = Alignment.Center
        ){
            Text(text = characterModel.name, color = Color.White, fontSize = 14.sp)
        }
    }
}