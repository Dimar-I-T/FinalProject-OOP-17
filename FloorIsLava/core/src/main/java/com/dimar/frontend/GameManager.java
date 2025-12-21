package com.dimar.frontend;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.dimar.frontend.observers.Observer;
import com.dimar.frontend.observers.ScoreManager;
import com.dimar.frontend.services.BackendService;

import java.util.ArrayList;
import java.util.List;

public class GameManager {
    private static GameManager instance;
    private final ScoreManager scoreManager;
    private boolean gameActive;
    private String authToken;
    private BackendService backendService;
    private String username;
    private String currentPlayerId = null;
    private int score = 0;
    private int coinsCollectedData = 0;
    private int coinsCollected = 0;

    private GameManager() {
        scoreManager = new ScoreManager();
        backendService = new BackendService();
        backendService.pingBackend();
        scoreManager.setScore(0);
        gameActive = false;
    }

    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }

        return instance;
    }

    public void startGame() {
        scoreManager.setScore(0);
        coinsCollected = 0;
        gameActive = true;
        System.out.println("Game Started!");
    }

    public void setScore(int distance) {
        if (gameActive) {
            scoreManager.setScore(distance);
        }
    }

    public String getAuthToken() {
        return this.authToken;
    }

    public void setAuthToken(String token) {
        authToken = token;
    }

    public interface UsernameCallback {
        void onFetched(String username, int skor, int coinsCollected, List<Leaderboard> leaderboardList);
    }

    public interface LoginCallback {
        void onSuccess(String token);
        void onError(String error);
    }

    public void loginPlayer(String username, String password, LoginCallback callback) {
        backendService.loginPlayer(username, password, new BackendService.RequestCallback() {
            @Override
            public void onSuccess(String response) {
                try {
                    JsonValue parse = new JsonReader().parse(response);
                    authToken = parse.getString("token");
                    Gdx.app.log("PLAYER", "Login berhasil, token: " + authToken);
                    callback.onSuccess(authToken);
                } catch (Exception e) {
                    Gdx.app.error("JSON_ERROR", "Gagal parsing token", e);
                    callback.onError(e.getMessage());
                }
            }

            @Override
            public void onError(String error) {
                Gdx.app.error("LOGIN_ERROR", error);
                callback.onError(error);
            }
        });
    }

    public void fetchUsername(UsernameCallback callback) {
        List<Leaderboard> leaderboardList = new ArrayList<>();
        backendService.getLeaderboard(5, new BackendService.RequestCallback() {
            @Override
            public void onSuccess(String response) {
                try {
                    JsonValue root = new JsonReader().parse(response);
                    for (JsonValue item : root) {
                        String username = item.getString("username", "guest");
                        int score = item.getInt("highScore", 0);
                        int coins = item.getInt("totalCoins", 0);
                        leaderboardList.add(new Leaderboard(username, score, coins));
                    }
                } catch (Exception e) {
                    username = "guest";
                    Gdx.app.error("ERROR", "Gagal parsing username", e);
                }
                callback.onFetched(username, score, coinsCollectedData, leaderboardList);
            }

            @Override
            public void onError(String error) {
                username = "guest";
                callback.onFetched("guest", scoreManager.getScore(), coinsCollected, leaderboardList);
                Gdx.app.error("ERROR", error);
            }
        });

        if (authToken == null) {
            callback.onFetched("guest", scoreManager.getScore(), coinsCollected, leaderboardList);
            return;
        }

        backendService.getPlayer(authToken, new BackendService.RequestCallback() {
            @Override
            public void onSuccess(String response) {
                try {
                    JsonValue json = new JsonReader().parse(response);
                    username = json.has("username") ? json.getString("username") : "guest";
                    currentPlayerId = json.has("playerId") ? json.getString("playerId") : null;
                    score = json.has("highScore") ? json.getInt("highScore") : 0;
                    coinsCollectedData = json.has("totalCoins") ? json.getInt("totalCoins") : 0;
                    Gdx.app.log("PLAYER", "Username didapat: " + username + " id: " + currentPlayerId);
                } catch (Exception e) {
                    username = "guest";
                    Gdx.app.error("ERROR", "Gagal parsing username", e);
                }
                callback.onFetched(username, score, coinsCollectedData, leaderboardList);
            }

            @Override
            public void onError(String error) {
                username = "guest";
                callback.onFetched("guest", scoreManager.getScore(), coinsCollected, leaderboardList);
                Gdx.app.error("ERROR", error);
            }
        });
    }

    public void registerPlayer(String username, String password, LoginCallback callback) {
        backendService.createPlayer(username, password, new BackendService.RequestCallback() {
            @Override
            public void onSuccess(String response) {
                Gdx.app.log("PLAYER", "Pendaftaran berhasil");
                loginPlayer(username, password, callback);
            }

            @Override
            public void onError(String error) {
                Gdx.app.error("REGISTER_ERROR", error);
                callback.onError(error);
            }
        });
    }

    public void endGame() {
        if (currentPlayerId == null) {
            Gdx.app.error("ERROR", "Cannot submit score");
            return;
        }

        int score = scoreManager.getScore();
        int coinsToSubmit = coinsCollected;
        backendService.submitScore(currentPlayerId, score, coinsToSubmit, new BackendService.RequestCallback() {
            @Override
            public void onSuccess(String response) {
                Gdx.app.log("SUCCESS", "Berhasil mensubmit score");
            }

            @Override
            public void onError(String error) {
                Gdx.app.error("ERROR", error);
            }
        });
    }

    public void addCoin() {
        coinsCollected++;
    }

    public int getCoinsCollected() {
        return this.coinsCollected;
    }

    public int getScore() {
        return scoreManager.getScore();
    }

    public void setCoinsCollected(int coins) {
        this.coinsCollected = coins;
    }

    public void addObserver(Observer observer) {
        scoreManager.addObserver(observer);
    }

    public void removeObserver(Observer observer) {
        scoreManager.removeObserver(observer);
    }
}
