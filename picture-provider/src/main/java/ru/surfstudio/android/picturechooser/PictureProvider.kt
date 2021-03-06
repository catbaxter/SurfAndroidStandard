/*
  Copyright (c) 2018-present, SurfStudio LLC.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package ru.surfstudio.android.picturechooser

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import io.reactivex.Observable
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.core.ui.provider.ActivityProvider
import ru.surfstudio.android.picturechooser.exceptions.NoPermissionException

private const val DEFAULT_TEMP_FILE_NAME = "image.jpg"

/**
 * Поставляет изображения находящиеся на устройстве или с камеры.
 */
class PictureProvider constructor(
        val context: Context,
        activityNavigator: ActivityNavigator,
        activityProvider: ActivityProvider,
        private val cameraStoragePermissionChecker: PicturePermissionChecker) {

    private val cameraIntentHelper = CameraPictureProvider(activityNavigator, activityProvider.get())
    private val galleryPictureProvider = GalleryPictureProvider(activityNavigator, activityProvider.get())
    private val chooserPictureProvider = ChooserPictureProvider(activityNavigator, activityProvider.get())

    /**
     *  Запускает сторонее приложение камеры для получения изображения.
     *  @return Observable Uri изображения и угол поворота.
     */
    fun openCameraAndTakePhoto(noPermissionAction: () -> Unit = {}): Observable<CameraPictureProvider.CameraResult> {
        return checkPermissionAndPerform(
                cameraStoragePermissionChecker.checkCameraStoragePermission(),
                { cameraIntentHelper.startCameraIntent() },
                noPermissionAction
        )
    }

    //region Функции для выбора одного изображения из галереи
    /**
     *  Запускает сторонее приложение галереи для получения изображения.
     *  @return Observable путь до изображения (может вернуть пустое значение)
     */
    fun openGalleryAndGetPhoto(noPermissionAction: () -> Unit = {}): Observable<String> {
        return checkPermissionAndPerform(
                cameraStoragePermissionChecker.checkGalleryStoragePermission(),
                { galleryPictureProvider.openGalleryForSingleImage() },
                noPermissionAction)
    }

    /**
     *  Запускает сторонее приложение галереи для получения изображения.
     *  @return Observable Uri.toString() изображения.
     */
    fun openGalleryAndGetPhotoUri(noPermissionAction: () -> Unit = {}): Observable<String> {
        return checkPermissionAndPerform(
                cameraStoragePermissionChecker.checkGalleryStoragePermission(),
                { galleryPictureProvider.openGalleryForSingleImageUri() },
                noPermissionAction)
    }

    /**
     *  Запускает сторонее приложение галереи для получения изображения.
     *  @return Observable UriWrapper изображения.
     */
    fun openGalleryAndGetPhotoUriWrapper(noPermissionAction: () -> Unit = {}): Observable<UriWrapper> {
        return checkPermissionAndPerform(
                cameraStoragePermissionChecker.checkGalleryStoragePermission(),
                { galleryPictureProvider.openGalleryForSingleImageUriWrapper() },
                noPermissionAction)
    }
    //endregion

    //region Функции для выбора нескольких изображений из галереи
    /**
     *  Запускает сторонее приложение галереи для получения нескольких изображений.
     *  @return Observable списка путей к выбранным изображениям
     */
    fun openGalleryAndGetFewPhoto(noPermissionAction: () -> Unit = {}): Observable<List<String>> {
        return checkPermissionAndPerform(
                cameraStoragePermissionChecker.checkGalleryStoragePermission(),
                { galleryPictureProvider.openGalleryForMultipleImage() },
                noPermissionAction)
    }

    /**
     *  Запускает сторонее приложение галереи для получения нескольких изображений.
     *  @return Observable списка Uri.toString() выбранных изображений
     */
    fun openGalleryAndGetFewPhotoUri(noPermissionAction: () -> Unit = {}): Observable<List<String>> {
        return checkPermissionAndPerform(
                cameraStoragePermissionChecker.checkGalleryStoragePermission(),
                { galleryPictureProvider.openGalleryForMultipleImageUri() },
                noPermissionAction)
    }

    /**
     *  Запускает сторонее приложение галереи для получения нескольких изображений.
     *  @return Observable списка UriWrapper выбранных изображений
     */
    fun openGalleryAndGetFewPhotoUriWrapper(noPermissionAction: () -> Unit = {}): Observable<List<UriWrapper>> {
        return checkPermissionAndPerform(
                cameraStoragePermissionChecker.checkGalleryStoragePermission(),
                { galleryPictureProvider.openGalleryForMultipleImageUriWrapper() },
                noPermissionAction)
    }
    //endregion

    //region Функции для выбора одного изображения на устройстве
    /**
     *  Показ диалога выбора приложения для получения одного изображения.
     *  @return Observable путь до изображения (может вернуть пустое значение)
     */
    fun openImageChooserAndGetPhoto(message: String,
                                    noPermissionAction: () -> Unit = {}): Observable<String> {
        return checkPermissionAndPerform(
                cameraStoragePermissionChecker.checkGalleryStoragePermission(),
                { chooserPictureProvider.createChooserForSingleImage(message) },
                noPermissionAction)
    }

    /**
     *  Показ диалога выбора приложения для получения одного изображения.
     *  @return Observable Uri.toString() изображения.
     */
    fun openImageChooserAndGetPhotoUri(message: String,
                                       noPermissionAction: () -> Unit = {}): Observable<String> {
        return checkPermissionAndPerform(
                cameraStoragePermissionChecker.checkGalleryStoragePermission(),
                { chooserPictureProvider.createChooserForSingleImageUri(message) },
                noPermissionAction)
    }

    /**
     *  Показ диалога выбора приложения для получения одного изображения.
     *  @return Observable UriWrapper изображения.
     */
    fun openImageChooserAndGetPhotoUriWrapper(message: String,
                                              noPermissionAction: () -> Unit = {}): Observable<UriWrapper> {
        return checkPermissionAndPerform(
                cameraStoragePermissionChecker.checkGalleryStoragePermission(),
                { chooserPictureProvider.createChooserForSingleImageUriWrapper(message) },
                noPermissionAction)
    }

    /**
     *  Получение изображения с устройства с его сохранением во временный файл.
     *  Следует использовать для получения валидного пути к изображению,
     *  открытого из гугл диска или других облачных хранилищ
     *  @return Observable путь к созданному временному файлу
     */
    fun openImageChooserAndSavePhoto(message: String,
                                     fileName: String = DEFAULT_TEMP_FILE_NAME,
                                     noPermissionAction: () -> Unit = {}): Observable<String> {
        return openImageChooserAndGetPhotoUriWrapper(message, noPermissionAction)
                .map { uriWrapper ->
                    createTempBitmap(uriWrapper.uri, fileName)
                }
    }
    //endregion

    //region Функции для выбора нескольких изображений на устройстве
    /**
     *  Показ диалога выбора приложения для получения нескольких изображений.
     *  @return Observable списка путей к выбранным изображениям
     */
    fun openImageChooserAndGetFewPhoto(message: String,
                                       noPermissionAction: () -> Unit = {}): Observable<List<String>> {
        return checkPermissionAndPerform(
                cameraStoragePermissionChecker.checkGalleryStoragePermission(),
                { chooserPictureProvider.createChooserForMultipleImage(message) },
                noPermissionAction)
    }

    /**
     *  Показ диалога выбора приложения для получения нескольких изображений.
     *  @return Observable списка Uri.toString() выбранных изображений
     */
    fun openImageChooserAndGetFewPhotoUri(message: String,
                                          noPermissionAction: () -> Unit = {}): Observable<List<String>> {
        return checkPermissionAndPerform(
                cameraStoragePermissionChecker.checkGalleryStoragePermission(),
                { chooserPictureProvider.createChooserForMultipleImageUri(message) },
                noPermissionAction)
    }

    /**
     *  Показ диалога выбора приложения для получения нескольких изображений.
     *  @return Observable списка UriWrapper выбранных изображений
     */
    fun openImageChooserAndGetFewPhotoUriWrapper(message: String,
                                                 noPermissionAction: () -> Unit = {}): Observable<List<UriWrapper>> {
        return checkPermissionAndPerform(
                cameraStoragePermissionChecker.checkGalleryStoragePermission(),
                { chooserPictureProvider.createChooserForMultipleImageUriWrapper(message) },
                noPermissionAction)
    }
    //endregion

    /**
     * @return Uri всех изображений на устройстве
     */
    fun getAllStoragePhoto(): Observable<List<String>> {
        return Observable.create { subscriber ->
            val images = ArrayList<String>()
            val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val projection = arrayOf(MediaStore.MediaColumns.DATA,
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                    MediaStore.Images.Media.WIDTH,
                    MediaStore.Images.Media.HEIGHT)

            context.contentResolver
                    .query(uri,
                            projection,
                            null,
                            null,
                            MediaStore.MediaColumns.DATE_ADDED + " DESC")
                    .apply {
                        val columnIndexData = getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
                        val columnIndexWidth = getColumnIndex(MediaStore.MediaColumns.WIDTH)
                        val columnIndexHeight = getColumnIndex(MediaStore.MediaColumns.HEIGHT)
                        while (moveToNext()) {
                            val width = getInt(columnIndexWidth)
                            val height = getInt(columnIndexHeight)
                            //не добавляем некорректные изображения
                            if (width > 0 && height > 0) {
                                val absolutePathOfImage = getString(columnIndexData)
                                images.add(absolutePathOfImage)
                            }
                        }
                        close()
                    }
            subscriber.onNext(images)
            subscriber.onComplete()
        }
    }

    //region Вспомогательные функции
    /**
     * Функция, проверяющая разрешение на чтения из хранилища
     * и выполняющая различные действия в зависимости от его наличия или отсутствия
     */
    private fun <T> checkPermissionAndPerform(
            checkPermissionAction: Observable<Boolean>,
            hasPermissionAction: () -> Observable<T>,
            noPermissionAction: () -> Unit
    ): Observable<T> {
        return checkPermissionAction
                .flatMap { hasPermission ->
                    if (hasPermission) {
                        hasPermissionAction()
                    } else {
                        noPermissionAction()
                        Observable.error(NoPermissionException())
                    }
                }
    }

    /**
     * Функция, создающая временный файл для изображения по его Uri.
     * @return String путь к созданному временному файлу
     */
    private fun createTempBitmap(imageUri: Uri, tempFileName: String): String {
        val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
        return saveTempBitmap(bitmap, tempFileName, context).absolutePath
    }
    //endregion
}