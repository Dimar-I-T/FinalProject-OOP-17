package com.dimar.frontend.services;

import com.badlogic.gdx.Gdx;
import okhttp3.*;

import java.io.IOException;

public class BackendService {
    private static final boolean IS_DEV = false;
    private static final String DEV_URL = "http://localhost:8081";
    private static final String PROD_URL =
        "https://floor-is-lava-backend-206920707557.europe-west1.run.app";
    private final String BASE_URL;
    private static final OkHttpClient client = new OkHttpClient();
    public BackendService() {
        BASE_URL = IS_DEV ? DEV_URL : PROD_URL;
    }

    public interface RequestCallback {
        void onSuccess(String response);
        void onError(String error);
    }

    public void createPlayer(String username, String password, RequestCallback callback) {
        String json = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";
        RequestBody body = RequestBody.create(
            json, MediaType.parse("application/json")
        );
        Request request = new Request.Builder()
            .url(BASE_URL + "/auth/signup")
            .post(body)
            .build();

        send(request, callback);
    }

    public void loginPlayer(String username, String password, RequestCallback callback) {
        String json = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";
        RequestBody body = RequestBody.create(
            json, MediaType.parse("application/json")
        );
        Request request = new Request.Builder()
            .url(BASE_URL + "/auth/login")
            .post(body)
            .build();

        send(request, callback);
    }

    public void getPlayer(String token, RequestCallback callback) {
        Request request = new Request.Builder()
            .url(BASE_URL + "/api/players/me")
            .get()
            .addHeader("Authorization", "Bearer " + token)
            .build();

        send(request, callback);
    }

    public void getLeaderboard(int limit, RequestCallback callback) {
        Request request = new Request.Builder()
            .url(BASE_URL + "/api/players/leaderboard/high-score?limit=" + limit)
            .get()
            .build();

        send(request, callback);
    }

    public void submitScore(String playerId, int score, int coins, RequestCallback callback) {
        String json = String.format(
            "{\"playerId\":\"%s\",\"value\":%d,\"coinsCollected\":%d}",
            playerId, score, coins
        );
        RequestBody body = RequestBody.create(
            json, MediaType.parse("application/json")
        );
        Request request = new Request.Builder()
            .url(BASE_URL + "/api/scores")
            .post(body)
            .build();

        send(request, callback);
    }

    public void pingBackend() {
        Request request = new Request.Builder()
            .url(BASE_URL + "/api/info")
            .get()
            .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                response.close();
            }
        });
    }

    private void send(Request request, RequestCallback callback) {
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body().string());
                } else {
                    callback.onError("HTTP " + response.code());
                }
            }
        });
    }
}
