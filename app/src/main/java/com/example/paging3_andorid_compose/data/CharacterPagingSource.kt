package com.example.paging3_andorid_compose.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.paging3_andorid_compose.presentationUi.model.CharacterModel
import jakarta.inject.Inject
import retrofit2.HttpException
import java.io.IOException


/**
 * PagingSource es la fuente de datos única para Paging 3.
 * Define de dónde y cómo se cargan fragmentos de datos (páginas).
 *
 * Parámetros genéricos:
 * - Key (Int): Tipo de identificador usado para pedir la página (en este caso el número de página: 1, 2, 3...).
 * - Value (CharacterModel): El tipo de dato final ya transformado que consumirá la UI en Compose.
 *
 * @property api Servicio de Retrofit inyectado por Hilt/Dagger para realizar las solicitudes HTTP.
 */
//PagingSource<Int(es el paginado ), CharacterModel(Modelo a recuperar)>
class CharacterPagingSource @Inject constructor(private  val api: RMApiService):
    PagingSource<Int, CharacterModel>() {

    /**
     * Define qué página recargar cuando la lista se invalida (por ejemplo, tras un pull-to-refresh).
     *
     * IMPORTANTE: 'state.anchorPosition' representa el índice del elemento en la lista donde
     * estaba el usuario (ej. elemento #25), NO el número de página.
     * La fórmula estándar busca la página más cercana a esa posición y calcula su clave correspondiente.
     */
    override fun getRefreshKey(state: PagingState<Int, CharacterModel>): Int? {
        return state.anchorPosition
    }

    /**
     * Función suspendida que ejecuta la carga asíncrona de un bloque de datos.
     * Paging 3 la invoca automáticamente en un hilo secundario cuando el usuario hace scroll.
     *
     * @param params Contiene la clave solicitada (params.key) y el tamaño de página configurado.
     */
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharacterModel> {
        return try {
            // Si params.key es null, significa que es la primera llamada (carga inicial) -> arrancamos en la página 1
            val page = params.key ?: 1

            // 1. Llamada a la API de Rick and Morty
            val response = api.getCharacters(page)
            val charaters = response.results

            // 2. Cálculo de página anterior:
            // La API de Rick and Morty comienza en la página 1. Si estamos en la 1, no hay página previa (null).
            val prevKey = if (page > 1) page - 1 else null
            // 3. Cálculo de página siguiente:
            // Si 'response.information.next' es null, el backend ya no tiene más datos -> devolvemos null para frenar el paginado.
            val nextKey = if (response.information.next != null) page +1 else null

            LoadResult.Page(
                data = charaters.map{it.toPresentation()},
                prevKey = prevKey,
                nextKey = nextKey
            )
        }catch (exception: IOException){
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
        // CAPTURA CRÍTICA: Aquí atrapamos el error HTTP 429 de Retrofit
        // Evita que la app muera y le notifica a Paging3 que hubo un error en el servidor
        LoadResult.Error(exception)
    }

    }
}