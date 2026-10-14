package com.example.paging3_andorid_compose.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.paging3_andorid_compose.presentationUi.model.CharacterModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class RMRepository @Inject constructor(val api: RMApiService){
    companion object {

        // Tamaño de página preferido por Paging 3 para sus cálculos internos.
        const val MAX_ITEMS=10

        // Distancia de pre-carga (prefetch): cuántos elementos antes de llegar al final
        // de la lista se debe disparar la carga de la siguiente página.
        const val PREFETCH_ITEMS = 3
    }

    /**
     * Construye y expone el flujo paginado de personajes.
     *
     * @return Flow que emite bloques de PagingData<CharacterModel> listos para ser
     *         recolectados por la interfaz de usuario en Compose.
     */
    fun getAllCharacters(): Flow<PagingData<CharacterModel>>{
        //api.getCharacters()
        // PagingConfig: define las reglas de rendimiento y consumo de memoria del paginado
        return Pager(config = PagingConfig(
            pageSize = MAX_ITEMS,               // Cantidad de elementos por página
            prefetchDistance = PREFETCH_ITEMS,  // Carga anticipada antes de que el usuario llegue al final
            enablePlaceholders = false          // Desactiva tarjetas fantasma/vacías mientras se descarga la data
        ),
            // pagingSourceFactory: Lambda que DEBE crear una instancia NUEVA de CharacterPagingSource cada vez.
            // Cuando la lista se invalida (por ejemplo, con un pull-to-refresh), Paging 3 destruye
            // la fuente anterior y ejecuta esta lambda para crear una limpia.
            pagingSourceFactory = {
                CharacterPagingSource(api)
            }).flow // Convierte la configuración del Pager en un Flow reactivo
    }
}