package ru.prokdo.quotonia.models

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "users_data_store")

class DataStoreManager(private val context: Context) {
    private fun createUsernameKey(username: String) = stringPreferencesKey("username_$username")
    private fun createPasswordKey(username: String) = stringPreferencesKey("password_$username")

    private fun createFavoritesKey(username: String) = stringPreferencesKey("favorites_$username")

    suspend fun saveUserProfile(username: String, password: String) {
        context.dataStore.edit { preferences ->
            preferences[createUsernameKey(username)] = username
            preferences[createPasswordKey(username)] = password
        }
    }

    fun getUserProfile(username: String): Flow<Pair<String, String>> {
        return context.dataStore.data
            .map { preferences ->
                val savedUsername = preferences[createUsernameKey(username)] ?: ""
                val savedPassword = preferences[createPasswordKey(username)] ?: ""
                savedUsername to savedPassword
            }
    }

    suspend fun isUserExists(username: String): Boolean {
        val preferences = context.dataStore.data.first()
        return preferences[createUsernameKey(username)] != null
    }

    suspend fun saveFavorites(username: String, favorites: MutableList<Quote>) {
        val gson = Gson()
        val favoritesJson = gson.toJson(favorites)
        context.dataStore.edit { preferences ->
            preferences[createFavoritesKey(username)] = favoritesJson
        }
    }

    fun getFavorites(username: String): Flow<List<Quote>> {
        return context.dataStore.data
            .map { preferences ->
                val favoritesJson = preferences[createFavoritesKey(username)] ?: "[]"
                val gson = Gson()
                val quoteListType = object : com.google.gson.reflect.TypeToken<List<Quote>>() {}.type
                gson.fromJson<List<Quote>>(favoritesJson, quoteListType)
            }
    }

    suspend fun addFavorite(username: String, quote: Quote) {
        val currentFavorites = getFavorites(username).first().toMutableList()
        if (!currentFavorites.contains(quote)) {
            currentFavorites.add(quote)
            saveFavorites(username, currentFavorites)
        }
    }

    suspend fun removeFavorite(username: String, quote: Quote) {
        val currentFavorites = getFavorites(username).first().toMutableList()
        if (currentFavorites.contains(quote)) {
            currentFavorites.remove(quote)
            saveFavorites(username, currentFavorites)
        }
    }
}
