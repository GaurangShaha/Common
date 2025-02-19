package android.artisan.networking.retrofit.remote

import android.artisan.foundation.model.Result
import android.artisan.networking.retrofit.remote.adapter.ResultWithBodyCallAdapter
import android.artisan.networking.retrofit.remote.adapter.ResultWithResponseCallAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

public class ResultCallAdapterFactory private constructor(
    private val coroutineScope: CoroutineScope,
) : CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit,
    ): CallAdapter<*, *>? {
        if (getRawType(returnType) != Call::class.java) {
            return null
        }

        val callType = getParameterUpperBound(0, returnType as ParameterizedType)
        val rawType = getRawType(callType)
        if (rawType != Result::class.java) {
            return null
        }

        val resultType = getParameterUpperBound(0, callType as ParameterizedType)
        val resultRawType = getRawType(resultType)

        return if (resultRawType == Response::class.java) {
            val responseType = getParameterUpperBound(0, resultType as ParameterizedType)
            ResultWithResponseCallAdapter(
                resultType = responseType,
                coroutineScope = coroutineScope
            )
        } else {
            ResultWithBodyCallAdapter(
                resultType = resultType,
                resultRawType = resultRawType,
                coroutineScope = coroutineScope
            )
        }
    }

    public companion object {
        public fun create(
            coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO),
        ): ResultCallAdapterFactory = ResultCallAdapterFactory(coroutineScope = coroutineScope)
    }
}
