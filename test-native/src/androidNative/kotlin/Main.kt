import kotlinx.cinterop.CPointer
import kotlinx.cinterop.convert
import kotlinx.cinterop.invoke
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.pointed
import kotlinx.cinterop.toKString
import kotlinx.cinterop.toKStringFromUtf8
import platform.android.ANDROID_LOG_ERROR
import platform.android.JNIEnvVar
import platform.android.__android_log_write
import platform.android.jobject
import platform.android.jstring


@CName("Java_com_example_app_HomeActivity_logError")
fun loge(env: CPointer<JNIEnvVar>, obj: jobject, message: jstring) {
    memScoped {
        val byteArray = env.pointed.pointed?.GetStringUTFChars?.invoke(env, message, null)
        logErrorWrapped("${byteArray?.toKStringFromUtf8()}")
    }
}

actual fun logError(message: String) {
    __android_log_write(ANDROID_LOG_ERROR.convert(), "android-test-native", message)
}