package ru.surfstudio.android.easyadapter.sample.ui.screen.main

import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.easyadapter.sample.ui.screen.multitype.MultitypeListActivityRoute
import ru.surfstudio.android.easyadapter.sample.ui.screen.pagination.PaginationListActivityRoute
import javax.inject.Inject

/**
 * Презентер главного экрана
 */
@PerScreen
internal class MainPresenter @Inject constructor(basePresenterDependency: BasePresenterDependency,
                                                 private val activityNavigator: ActivityNavigator
) : BasePresenter<MainActivityView>(basePresenterDependency) {

    private val screenModel: MainScreenModel = MainScreenModel()

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        view.render(screenModel)
    }

    fun showMultitypeList() = activityNavigator.start(MultitypeListActivityRoute())

    fun showPagintationList() = activityNavigator.start(PaginationListActivityRoute())
}