package site.caikun.kunmusic.http

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

abstract class Network constructor(
    private val baseURL: String
) {

    private val retrofitHashMap: HashMap<String, Retrofit> = HashMap()
    private val timeOut: Long = 30


    /**
     * 创建Okhttp客户端
     */
    private fun createOkhttpClient(): OkHttpClient {
        val okHttpClient: OkHttpClient.Builder = OkHttpClient.Builder()
            .connectTimeout(timeOut, TimeUnit.SECONDS)
            .readTimeout(timeOut, TimeUnit.SECONDS)
            .writeTimeout(timeOut, TimeUnit.SECONDS)

            //添加日志拦截器
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))

        //添加自定义拦截器
        initInterceptor()?.also {
            okHttpClient.addInterceptor(it)
        }
        return okHttpClient.build()
    }

    /**
     * 构建Retrofit对象
     */
    protected fun getRetrofit(apiService: Class<*>): Retrofit {
        return if (retrofitHashMap[baseURL + apiService.name] == null) {
            val retrofit = Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .client(createOkhttpClient())
                .build()
            retrofitHashMap[baseURL + apiService.name] = retrofit
            retrofit
        } else {
            retrofitHashMap[baseURL + apiService.name]!!
        }
    }

    /**
     * 自定义拦截器
     */
    abstract fun initInterceptor(): Interceptor?
}