package br.com.instituto_federal.utrain.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "https://wger.de/api/v2/";
    private static Retrofit retrofit = null;

    public static WgerApiService getClient() {
        if (retrofit == null) {
            // ✅ 1. Cria um intercetor para logging
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            // Define o nível de logging para BODY, que mostra a resposta completa
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            // ✅ 2. Cria um cliente OkHttp e adiciona o intercetor
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(logging);

            // ✅ 3. Constrói o Retrofit com o cliente personalizado
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build()) // Adiciona o cliente com logging ao Retrofit
                    .build();
        }
        return retrofit.create(WgerApiService.class);
    }
}
