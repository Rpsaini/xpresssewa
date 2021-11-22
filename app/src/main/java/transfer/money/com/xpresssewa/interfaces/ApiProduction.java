package transfer.money.com.xpresssewa.interfaces;


import android.content.Context;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import transfer.money.com.xpresssewa.BaseActivity;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class ApiProduction {
    // private static final String BASE_URL = "https://api.stackexchange.com/2.2/";
    private final Context context;

    private ApiProduction(Context context) {
        this.context = context;
    }

    public static ApiProduction getInstance(Context context) {
        return new ApiProduction(context);
    }

    private Retrofit provideRestAdapter() {


        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        return new Retrofit.Builder()
                .baseUrl(BaseActivity.baseurl)
                .client(OkHttpProduction.getOkHttpClient(context, true))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())

                .build();
    }

    public <S> S provideService(Class<S> serviceClass)
    {
        return provideRestAdapter().create(serviceClass);
    }
}