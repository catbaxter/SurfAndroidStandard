package ru.surfstudio.android.firebase.sample.ui.screen.push.data

import kotlinx.android.synthetic.main.activity_data_push.*
import ru.surfstudio.android.core.mvp.activity.BaseRenderableActivityView
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.android.firebase.sample.R
import ru.surfstudio.android.firebase.sample.ui.base.configurator.CustomActivityScreenConfigurator
import javax.inject.Inject

/**
 * Экран, который будет открыт для пушей с данными
 */
class DataPushActivityView : BaseRenderableActivityView<DataPushScreenModel>() {

    @Inject
    internal lateinit var presenter: DataPushPresenter

    override fun createConfigurator(): CustomActivityScreenConfigurator = DataPushScreenConfigurator(intent)

    override fun getContentView(): Int = R.layout.activity_data_push

    override fun getPresenters(): Array<CorePresenter<*>> = arrayOf(presenter)

    override fun getScreenName(): String = "DataPushActivityView"

    override fun renderInternal(screenModel: DataPushScreenModel) {
        notification_tv.text = screenModel.notification?.previewText
    }
}