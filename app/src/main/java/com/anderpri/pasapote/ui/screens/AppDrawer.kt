package com.anderpri.pasapote.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.transformations
import coil3.transform.RoundedCornersTransformation
import com.anderpri.pasapote.R
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDrawer(
    navController: NavHostController,
    content: @Composable (PaddingValues) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    BackHandler(enabled = drawerState.isOpen) {
        scope.launch { drawerState.close() }
    }

    var titleResId by rememberSaveable { mutableIntStateOf(R.string.app_name) }

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .verticalScroll(rememberScrollState())
                ) {

                    //Spacer(Modifier.height(12.dp))

                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.CenterHorizontally)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data("file:///android_asset/ic_launcher-playstore.png")
                                .transformations(RoundedCornersTransformation(80f))
                                .build(),
                            contentDescription = null,
                            placeholder = painterResource(R.drawable.pasapote),
                            modifier = Modifier
                                .padding(top = 16.dp, bottom = 8.dp)
                                .clip(CircleShape)
                                .size(100.dp)
                                .border(
                                    width = 2.dp,
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    shape = CircleShape
                                )
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            stringResource(R.string.app_name).uppercase(),
                            style = MaterialTheme.typography.titleLarge
                        )
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    // Items of the drawer
                    val currentRoute = navController.currentDestination?.route

                    // Pasapotea
                    CustomDrawerItem(
                        textResId = R.string.pasapotea,
                        iconResId = R.drawable.album_outline,
                        currentIconResId = R.drawable.album_outline,
                        route = "home",
                        currentRoute = currentRoute,
                    ) {
                        onClickItem(
                            navController = navController,
                            route = "home",
                            setTitle = { titleResId = R.string.pasapotea },
                            closeDrawer = { scope.launch { if (drawerState.isOpen) drawerState.close() } }
                        )
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    // Mapa
                    CustomDrawerItem(
                        textResId = R.string.mapa,
                        iconResId = R.drawable.map_outline,
                        currentIconResId = R.drawable.map_filled,
                        route = "map",
                        currentRoute = currentRoute,
                    ) {
                        onClickItem(
                            navController = navController,
                            route = "map",
                            setTitle = { titleResId = R.string.mapa },
                            closeDrawer = { scope.launch { if (drawerState.isOpen) drawerState.close() } }
                        )
                    }

                    // Lista
                    CustomDrawerItem(
                        textResId = R.string.konpartsen_lista,
                        iconResId = R.drawable.list,
                        currentIconResId = R.drawable.list,
                        route = "list",
                        currentRoute = currentRoute,
                    ) {
                        onClickItem(
                            navController = navController,
                            route = "list",
                            setTitle = { titleResId = R.string.konpartsen_lista },
                            closeDrawer = { scope.launch { if (drawerState.isOpen) drawerState.close() } }
                        )
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    // Ezarpenak
                    CustomDrawerItem(
                        textResId = R.string.ezarpenak,
                        iconResId = R.drawable.settings_outline,
                        currentIconResId = R.drawable.settings_filled,
                        route = "settings",
                        currentRoute = currentRoute
                    ) {
                        onClickItem(
                            navController = navController,
                            route = "settings",
                            setTitle = { titleResId = R.string.ezarpenak },
                            closeDrawer = { scope.launch { if (drawerState.isOpen) drawerState.close() }}
                        )
                    }
                }
            }
        },
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            stringResource(titleResId),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                when {
                                    drawerState.isClosed -> drawerState.open()
                                    else -> drawerState.close()
                                }
                            }
                        }) {
                            Icon(
                                Icons.Default.Menu,
                                contentDescription = "Menu",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        ) { innerPadding ->
            content(innerPadding)
        }
    }
}

@Composable
fun CustomDrawerItem(
    textResId: Int,
    iconResId: Int,
    currentIconResId: Int,
    route: String,
    currentRoute: String?,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        label = {
            Text(
                stringResource(textResId),
                style = MaterialTheme.typography.titleSmall
            )
        },
        selected = currentRoute == route,
        icon = {
            if (currentRoute == route) Icon(painterResource(currentIconResId), contentDescription = null)
            else Icon(painterResource(iconResId), contentDescription = null)
        },
        onClick = {
            onClick()
        },
    )
}

fun onClickItem(navController: NavHostController, route: String, setTitle: () -> Unit, closeDrawer: () -> Job) {
    navController.navigate(route) {
        popUpTo("home") { inclusive = route == "home" }
        launchSingleTop = true
        restoreState = true
    }
    setTitle()
    closeDrawer()
}