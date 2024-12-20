package ru.prokdo.quotonia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.prokdo.quotonia.models.DataStoreManager
import ru.prokdo.quotonia.models.Quote
import ru.prokdo.quotonia.network.QuotableApiService
import ru.prokdo.quotonia.ui.theme.QuotoniaTheme

data class UserProfile(val username: String, val favorites: MutableList<Quote>)

enum class SearchType {
    AUTHOR,
    QUERY
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuotoniaTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    QuotesApp(DataStoreManager(LocalContext.current))
                }
            }
        }
    }

    @Composable
    fun QuotesApp(dataStoreManager: DataStoreManager) {
        var selectedTabIndex by remember { mutableIntStateOf(0) }
        var selectedQuoteId by remember { mutableStateOf<String?>(null) }
        var isUserLoggedIn by remember { mutableStateOf(false) }
        var userProfile by remember { mutableStateOf<UserProfile?>(null) }
        var favorites by remember { mutableStateOf<List<Quote>>(emptyList()) }

        val tabs = if (isUserLoggedIn) {
            listOf("Search", "Profile")
        } else {
            listOf("Search", "Sign in / Sign Up")
        }

        val coroutineScope = rememberCoroutineScope()

        LaunchedEffect(userProfile) {
            userProfile?.let {
                dataStoreManager.getFavorites(it.username).collect { favoriteQuotes ->
                    favorites = favoriteQuotes
                }
            }
        }

        Column {
            TabRow(selectedTabIndex = selectedTabIndex) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title) }
                    )
                }
            }

            when (tabs[selectedTabIndex]) {
                "Search" -> SearchQuotesScreen(
                    onQuoteClick = { quoteId ->
                        if (quoteId.isNotEmpty()) {
                            selectedQuoteId = quoteId
                        }
                    },
                    onAddFavorite = { quote ->
                        userProfile?.username?.let { username ->
                            coroutineScope.launch {
                                dataStoreManager.addFavorite(username, quote)
                                dataStoreManager.getFavorites(username).collect { favoriteQuotes ->
                                    favorites = favoriteQuotes
                                }
                            }
                        }
                    },
                    onRemoveFavorite = { quote ->
                        userProfile?.username?.let { username ->
                            coroutineScope.launch {
                                dataStoreManager.removeFavorite(username, quote)
                                dataStoreManager.getFavorites(username).collect { favoriteQuotes ->
                                    favorites = favoriteQuotes
                                }
                            }
                        }
                    },
                    isFavorite = { quote -> favorites.contains(quote) }
                )

                "Sign in / Sign Up" -> SignScreen(
                    onLogin = { profile ->
                        userProfile = profile
                        isUserLoggedIn = true
                        selectedTabIndex = 0
                    },
                    onRegister = { profile ->
                        userProfile = profile
                        isUserLoggedIn = true
                        selectedTabIndex = 0
                    }
                )

                "Profile" -> userProfile?.let { profile ->
                    ProfileScreen(
                        userProfile = profile,
                        onLogout = {
                            isUserLoggedIn = false
                            userProfile = null
                            favorites = emptyList()
                        },
                        onAddFavorite = { quote ->
                            userProfile?.username?.let { username ->
                                coroutineScope.launch {
                                    dataStoreManager.addFavorite(username, quote)
                                    dataStoreManager.getFavorites(username).collect { favoriteQuotes ->
                                        favorites = favoriteQuotes
                                    }
                                }
                            }
                        },
                        onRemoveFavorite = { quote ->
                            userProfile?.username?.let { username ->
                                coroutineScope.launch {
                                    dataStoreManager.removeFavorite(username, quote)
                                    dataStoreManager.getFavorites(username).collect { favoriteQuotes ->
                                        favorites = favoriteQuotes
                                    }
                                }
                            }
                        },
                        onQuoteClick = { quoteId ->
                            if (quoteId.isNotEmpty()) {
                                selectedQuoteId = quoteId
                            }
                        },
                        dataStoreManager = dataStoreManager
                    )
                }
            }
        }

        selectedQuoteId?.let { quoteId ->
            QuoteDetailsScreen(
                quoteId = quoteId,
                onBack = { selectedQuoteId = null }
            )
        }
    }

    @Composable
    fun SearchQuotesScreen(
        onQuoteClick: (String) -> Unit,
        onAddFavorite: (Quote) -> Unit,
        onRemoveFavorite: (Quote) -> Unit,
        isFavorite: (Quote) -> Boolean
    ) {
        var query by remember { mutableStateOf("") }
        var searchType by remember { mutableStateOf<SearchType?>(null) }
        var quotes by remember { mutableStateOf<List<Quote>>(emptyList()) }
        var isLoading by remember { mutableStateOf(false) }
        var errorMessage by remember { mutableStateOf<String?>(null) }

        val retrofit = Retrofit.Builder()
            .baseUrl("http://api.quotable.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(QuotableApiService::class.java)

        fun performSearch() {
            if (query.isNotEmpty()) {
                isLoading = true
                errorMessage = null
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val quoteResponse = when (searchType) {
                            SearchType.AUTHOR -> service.searchQuotesByAuthor(author = query)
                            SearchType.QUERY -> service.searchQuotesByQuery(query = query)
                            null -> null
                        }
                        if (quoteResponse != null) quotes = quoteResponse.results
                    } catch (e: Exception) {
                        errorMessage = "An error occurred while fetching quotes. Error: ${e.message}"
                    } finally {
                        isLoading = false
                    }
                }
            }
            else {
                isLoading = true
                errorMessage = null
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        quotes = service.getRandomQuotes()
                    } catch (e: Exception) {
                        errorMessage = "An error occurred while fetching quotes. Error: ${e.message}"
                    } finally {
                        isLoading = false
                    }
                }
            }
        }

        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {

            TextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("Search Quotes") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = searchType == SearchType.AUTHOR,
                    onClick = {
                        searchType = when (searchType) {
                            SearchType.AUTHOR -> null
                            else -> SearchType.AUTHOR
                        }
                    }
                )
                Text(text = "By Author")

                Spacer(modifier = Modifier.width(16.dp))

                RadioButton(
                    selected = searchType == SearchType.QUERY,
                    onClick = {
                        searchType = when (searchType) {
                            SearchType.QUERY -> null
                            else -> SearchType.QUERY
                        }
                    }
                )
                Text(text = "By Query")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { performSearch() },
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            ) {
                Text("Search")
            }

            if (isLoading) {
                LoadingScreen()
            }
            else {
                errorMessage?.let {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(it, color = MaterialTheme.colorScheme.error)
                }

                if (quotes.isNotEmpty()) {
                    LazyColumn {
                        items(quotes.size) { index ->
                            QuoteItem(
                                quote = quotes[index],
                                onClick = {
                                    onQuoteClick(quotes[index]._id)
                                },
                                onAddFavorite = { onAddFavorite(quotes[index]) },
                                onRemoveFavorite = { onRemoveFavorite(quotes[index]) },
                                isFavorite = isFavorite(quotes[index])
                            )
                        }
                    }
                } else {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }

    @Composable
    fun QuoteDetailsScreen(quoteId: String, onBack: () -> Unit) {
        var quoteDetails by remember { mutableStateOf<Quote?>(null) }
        var isLoading by remember { mutableStateOf(true) }
        var errorMessage by remember { mutableStateOf<String?>(null) }

        val retrofit = Retrofit.Builder()
            .baseUrl("http://api.quotable.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(QuotableApiService::class.java)

        LaunchedEffect(quoteId) {
            isLoading = true
            errorMessage = null
            try {
                if (quoteId.isNotEmpty()) {
                    val details = service.getQuoteDetails(quoteId)
                    quoteDetails = details
                } else {
                    errorMessage = "Invalid quote ID"
                }
            } catch (e: Exception) {
                errorMessage = "An error occurred: ${e.message}"
            } finally {
                isLoading = false
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            } else {
                if (quoteDetails != null) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = quoteDetails!!.content,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        Text(
                            text = "Author: ${quoteDetails!!.author}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        if (quoteDetails!!.tags.isNotEmpty()) {
                            Text(
                                text = "Tags: ${quoteDetails!!.tags.joinToString(", ")}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = onBack,
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        ) {
                            Text("Back")
                        }
                    }
                } else {
                    Text(
                        text = errorMessage ?: "Error loading quote details.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }

    @Composable
    fun LoadingScreen() {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator()
        }
    }

    @Composable
    fun QuoteItem(
        quote: Quote,
        onClick: (String) -> Unit,
        onAddFavorite: (Quote) -> Unit,
        onRemoveFavorite: (Quote) -> Unit,
        isFavorite: Boolean
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clickable(onClick = { onClick(quote._id) })
        ) {
            Text(
                text = quote.content,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Text(
                text = "- ${quote.author}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            IconButton(onClick = {
                if (isFavorite) {
                    onRemoveFavorite(quote)
                } else {
                    onAddFavorite(quote)
                }
            }) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites"
                )
            }
        }
    }

    @Composable
    fun SignScreen(onLogin: (UserProfile) -> Unit, onRegister: (UserProfile) -> Unit) {
        var username by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var confirmPassword by remember { mutableStateOf("") }
        var errorMessage by remember { mutableStateOf<String?>(null) }
        var userExists by remember { mutableStateOf<Boolean?>(null) }

        val dataStoreManager = DataStoreManager(LocalContext.current)
        val coroutineScope = rememberCoroutineScope()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = if (userExists == null) "Enter username" else if (userExists == true) "Sign In" else "Sign Up", style = MaterialTheme.typography.titleLarge)

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Enter username") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (userExists == true) {
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Enter password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = {
                    coroutineScope.launch {
                        try {
                            val storedPassword = dataStoreManager.getUserProfile(username).first().second
                            if (password == storedPassword) {
                                onLogin(UserProfile(username = username, favorites = mutableListOf()))
                            } else {
                                errorMessage = "Invalid username or password"
                            }
                        } catch (e: Exception) {
                            errorMessage = "An error occurred: ${e.message}"
                        }
                    }
                }) {
                    Text("Submit")
                }

            } else if (userExists == false) {
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Enter password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirm password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = {
                    if (password.isEmpty() || confirmPassword.isEmpty()) {
                        errorMessage = "Both password fields are required"
                    } else if (password != confirmPassword) {
                        errorMessage = "Passwords do not match"
                    } else {
                        coroutineScope.launch {
                            try {
                                dataStoreManager.saveUserProfile(username, password)
                                onRegister(UserProfile(username, mutableListOf()))
                            } catch (e: Exception) {
                                errorMessage = "An error occurred: ${e.message}"
                            }
                        }
                    }
                }) {
                    Text("Submit ")
                }
            }

            errorMessage?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(it, color = MaterialTheme.colorScheme.error)
            }

            LaunchedEffect(username) {
                if (username.isNotEmpty()) {
                    try {
                        userExists = dataStoreManager.isUserExists(username)
                    } catch (e: Exception) {
                        userExists = null
                        errorMessage = "An error occurred: ${e.message}"
                    }
                } else {
                    userExists = null
                }
            }
        }
    }

    @Composable
    fun ProfileScreen(
        userProfile: UserProfile,
        onLogout: () -> Unit,
        onAddFavorite: (Quote) -> Unit,
        onRemoveFavorite: (Quote) -> Unit,
        dataStoreManager: DataStoreManager,
        onQuoteClick: (String) -> Unit
    ) {
        var favorites by remember { mutableStateOf<List<Quote>>(emptyList()) }
        var isLoading by remember { mutableStateOf(true) }

        LaunchedEffect(userProfile.username) {
            try {
                dataStoreManager.getFavorites(userProfile.username).collect { favoriteQuotes ->
                    favorites = favoriteQuotes
                    isLoading = false
                }
            } catch (e: Exception) {
                e.printStackTrace()
                isLoading = false
            }
        }

        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Profile: ${userProfile.username}",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (isLoading) {
                LoadingScreen()
            } else {
                Text("Favorites")

                if (favorites.isEmpty()) {
                    Text("No favorite quotes.")
                } else {
                    LazyColumn {
                        items(favorites.size) { index ->
                            val quote = favorites[index]
                            QuoteItem(
                                quote = quote,
                                onClick = { onQuoteClick(quote._id) },
                                onAddFavorite = { onAddFavorite(quote) },
                                onRemoveFavorite = { onRemoveFavorite(quote) },
                                isFavorite = true
                            )
                        }
                    }
                }

                Button(
                    onClick = onLogout,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                ) {
                    Text("Logout")
                }
            }
        }
    }
}