package ru.surfstudio.android.standarddialog.sample.ui.screen.main

import android.os.Bundle
import android.os.PersistableBundle
import android.support.annotation.IdRes
import androidx.core.widget.toast
import kotlinx.android.synthetic.main.activity_main.*

import ru.surfstudio.android.core.mvp.activity.BaseRenderableActivityView
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.android.sample.dagger.ui.base.configurator.DefaultActivityScreenConfigurator
import ru.surfstudio.android.standarddialog.sample.R
import javax.inject.Inject

/**
 * Вью главного экрана
 */
class MainActivityView : BaseRenderableActivityView<MainScreenModel>() {
    override fun getScreenName(): String  = "MainActivity"

    @Inject
    internal lateinit var presenter: MainPresenter

    @IdRes
    override fun getContentView(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?,
                          persistentState: PersistableBundle?,
                          viewRecreated: Boolean) {
        super.onCreate(savedInstanceState, persistentState, viewRecreated)
        first_btn.setOnClickListener { presenter.showFirstDialog() }
        second_btn.setOnClickListener { presenter.showSecondDialog() }
    }

    override fun renderInternal(screenModel: MainScreenModel) {}

    override fun getPresenters(): Array<CorePresenter<*>> = arrayOf(presenter)

    override fun createConfigurator(): DefaultActivityScreenConfigurator = MainScreenConfigurator(intent)

    fun showMessage(message: String) {
        toast(message)
    }
}
