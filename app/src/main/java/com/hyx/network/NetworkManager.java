package com.hyx.network;

import android.annotation.SuppressLint;
import android.os.Build;
import android.util.Log;

import com.hyx.network.converter.LJCConverterFactory;
import com.hyx.network.converter.LaiJCHttpLoggingInterceptor;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * created by ${nishuideyu}
 * 2019/9/4
 * <p>
 * <p>
 * 建议每一个模块创建一个{@link NetworkManager}的子类  和一个类似于{@link NetworkApi}的类
 * 具体请查看{@link APIManager}该示例类
 */
public abstract class NetworkManager<T> {

    private static final String TAG = NetworkManager.class.getSimpleName();

    private static SSLSocketFactory socketFactory;

    private static X509TrustManager trustManager;

    protected T createNetworkApi(String baseURL) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(LJCConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(createOkHttpClient())
                .build();
        return retrofit.create(getApiClass());
    }

    protected OkHttpClient createOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        SSLSocketFactory socketFactory = createSSLSocketFactory();

        if (socketFactory != null) {
            //忽略https证书验证
            builder.sslSocketFactory(socketFactory, trustManager);
            builder.hostnameVerifier((hostname, session) -> true);
        }

        builder.connectTimeout(getConnectTimeout(), TimeUnit.SECONDS);
        builder.readTimeout(getReadTimeout(), TimeUnit.SECONDS);
        builder.writeTimeout(getWriteTimeout(), TimeUnit.SECONDS);
        builder.addInterceptor(createLoggingInterceptor());
        return builder.build();
    }

    @SuppressLint("TrustAllX509TrustManager")
    private SSLSocketFactory createSSLSocketFactory() {
        if (socketFactory == null) {
            trustManager = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            };

            try {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
                    SSLContext sslContext = SSLContext.getInstance("SSL");
                    sslContext.init(null, new TrustManager[]{trustManager}, new SecureRandom());
                    socketFactory = sslContext.getSocketFactory();
                } else {
                    socketFactory = new TLSSocketFactory();
                }
            } catch (Exception e) {
                trustManager = null;
                Log.e(TAG, "" + e.toString());
            }

        }

        return socketFactory;
    }

    public abstract void reset();

    protected Interceptor createLoggingInterceptor() {
        LaiJCHttpLoggingInterceptor loggingInterceptor = new LaiJCHttpLoggingInterceptor(log -> Log.d(TAG, log));
        loggingInterceptor.setLevel(LaiJCHttpLoggingInterceptor.Level.BODY);
        return loggingInterceptor;
    }

    protected long getConnectTimeout() {
        return 15;
    }

    protected long getReadTimeout() {
        return 15;
    }

    protected long getWriteTimeout() {
        return 15;
    }

    protected abstract Class<T> getApiClass();

}
