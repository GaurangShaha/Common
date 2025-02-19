package android.artisan.networking.retrofit.remote.call

import android.artisan.foundation.helper.executeSafely
import android.artisan.foundation.model.Result
import android.artisan.foundation.model.Result.Failure
import android.artisan.networking.retrofit.remote.mapper.toInternetConnectionExceptionOrSelf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.awaitResponse
import java.lang.reflect.Type

@Suppress("TooGenericExceptionCaught")
internal class ResultWithBodyCall<T : Any>(
    private val proxy: Call<T>,
    private val resultRawType: Type,
    private val coroutineScope: CoroutineScope,
) : Call<Result<T?>> {

    override fun enqueue(callback: Callback<Result<T?>>) {
        coroutineScope.launch {
            try {
                val response = proxy.awaitResponse()
                callback.onResponse(
                    this@ResultWithBodyCall,
                    Response.success(response.toResult(resultRawType))
                )
            } catch (e: Exception) {
                coroutineContext.ensureActive()
                callback.onResponse(
                    this@ResultWithBodyCall,
                    Response.success(Failure(e.toInternetConnectionExceptionOrSelf()))
                )
            }
        }
    }

    override fun execute(): Response<Result<T?>> =
        runBlocking(coroutineScope.coroutineContext) {
            try {
                Response.success(proxy.execute().toResult(resultRawType))
            } catch (e: Exception) {
                coroutineContext.ensureActive()
                Response.success(Failure(e.toInternetConnectionExceptionOrSelf()))
            }
        }

    override fun clone(): Call<Result<T?>> =
        ResultWithBodyCall(proxy.clone(), resultRawType, coroutineScope)

    override fun request(): Request = proxy.request()

    override fun timeout(): Timeout = proxy.timeout()

    override fun isExecuted(): Boolean = proxy.isExecuted

    override fun isCanceled(): Boolean = proxy.isCanceled

    override fun cancel() = proxy.cancel()

    @Suppress("UNCHECKED_CAST")
    private suspend fun <T : Any?> Response<T>.toResult(resultRawType: Type): Result<T?> {
        return executeSafely {
            if (isSuccessful) {
                if (resultRawType == Unit::class.java) {
                    Unit as T
                } else {
                    body()
                }
            } else {
                throw HttpException(this)
            }
        }
    }
}
