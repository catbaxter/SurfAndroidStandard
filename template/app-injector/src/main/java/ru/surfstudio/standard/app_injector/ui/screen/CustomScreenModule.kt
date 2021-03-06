package ru.surfstudio.standard.app_injector.ui.screen

import dagger.Module
import dagger.Provides
import ru.surfstudio.android.core.ui.navigation.Route
import ru.surfstudio.android.dagger.scope.PerScreen

/**
 * базовый класс для дополнительного модуля конкретного экрана (обычно используется для поставки
 * параметров, переданных на экран при старте в презентер, в этом случае параметры передаются обурнутые в Route)
 *
 * @param <R>
</R> */
@Module
abstract class CustomScreenModule<out R : Route>(private val route: R) {

    @Provides
    @PerScreen
    fun provideRoute(): R {
        return route
    }
}