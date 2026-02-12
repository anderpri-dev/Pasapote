package com.anderpri.pasapote.ui.screens

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import com.anderpri.pasapote.platform.PlatformBackHandler
import com.anderpri.pasapote.resources.Res
import com.anderpri.pasapote.resources.album_filled
import com.anderpri.pasapote.resources.album_outline
import com.anderpri.pasapote.resources.app_name
import com.anderpri.pasapote.resources.ezarpenak
import com.anderpri.pasapote.resources.konpartsen_lista
import com.anderpri.pasapote.resources.list
import com.anderpri.pasapote.resources.map_filled
import com.anderpri.pasapote.resources.map_outline
import com.anderpri.pasapote.resources.mapa
import com.anderpri.pasapote.resources.menu
import com.anderpri.pasapote.resources.pasapote
import com.anderpri.pasapote.resources.pasapotea
import com.anderpri.pasapote.resources.settings_filled
import com.anderpri.pasapote.resources.settings_outline
import com.anderpri.pasapote.ui.state.DrawerTitleState
import com.anderpri.pasapote.ui.viewmodel.DrawerTitleViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
fun AppDrawer(
    navController: NavHostController,
    drawerTitleState: DrawerTitleState = koinViewModel<DrawerTitleViewModel>().drawerTitleState,
    content: @Composable (PaddingValues) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    PlatformBackHandler(enabled = drawerState.isOpen) {
        scope.launch { drawerState.close() }
    }

    val titleRes by drawerTitleState.title.collectAsState()

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.CenterHorizontally)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalPlatformContext.current)
                                .data(Res.getUri("files/ic_launcher-playstore.png"))
                                .build(),
                            contentDescription = null,
                            placeholder = painterResource(Res.drawable.pasapote),
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
                            stringResource(Res.string.app_name).uppercase(),
                            style = MaterialTheme.typography.titleLarge
                        )
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    val currentRoute = navController.currentDestination?.route

                    // Pasapotea
                    CustomDrawerItem(
                        textRes = Res.string.pasapotea,
                        iconRes = Res.drawable.album_outline,
                        currentIconRes = Res.drawable.album_filled,
                        route = "home",
                        currentRoute = currentRoute,
                    ) {
                        onClickItem(
                            navController = navController,
                            route = "home",
                            closeDrawer = { scope.launch { if (drawerState.isOpen) drawerState.close() } }
                        )
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    // Mapa
                    CustomDrawerItem(
                        textRes = Res.string.mapa,
                        iconRes = Res.drawable.map_outline,
                        currentIconRes = Res.drawable.map_filled,
                        route = "map",
                        currentRoute = currentRoute,
                    ) {
                        onClickItem(
                            navController = navController,
                            route = "map",
                            closeDrawer = { scope.launch { if (drawerState.isOpen) drawerState.close() } }
                        )
                    }

                    // Lista
                    CustomDrawerItem(
                        textRes = Res.string.konpartsen_lista,
                        iconRes = Res.drawable.list,
                        currentIconRes = Res.drawable.list,
                        route = "list",
                        currentRoute = currentRoute,
                    ) {
                        onClickItem(
                            navController = navController,
                            route = "list",
                            closeDrawer = { scope.launch { if (drawerState.isOpen) drawerState.close() } }
                        )
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    // Ezarpenak
                    CustomDrawerItem(
                        textRes = Res.string.ezarpenak,
                        iconRes = Res.drawable.settings_outline,
                        currentIconRes = Res.drawable.settings_filled,
                        route = "settings",
                        currentRoute = currentRoute
                    ) {
                        onClickItem(
                            navController = navController,
                            route = "settings",
                            closeDrawer = { scope.launch { if (drawerState.isOpen) drawerState.close() } }
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
                        titleRes?.let {
                            Text(
                                stringResource(it),
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
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
                                painterResource(Res.drawable.menu),
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
    textRes: StringResource,
    iconRes: DrawableResource,
    currentIconRes: DrawableResource,
    route: String,
    currentRoute: String?,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        label = {
            Text(
                stringResource(textRes),
                style = MaterialTheme.typography.titleSmall
            )
        },
        selected = currentRoute == route,
        icon = {
            if (currentRoute == route) Icon(painterResource(currentIconRes), contentDescription = null)
            else Icon(painterResource(iconRes), contentDescription = null)
        },
        onClick = { onClick() },
    )
}

fun onClickItem(navController: NavHostController, route: String, closeDrawer: () -> Job) {
    navController.navigate(route) {
        popUpTo("home") { inclusive = route == "home" }
        launchSingleTop = true
        restoreState = true
    }
    closeDrawer()
}
