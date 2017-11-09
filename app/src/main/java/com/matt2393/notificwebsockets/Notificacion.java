package com.matt2393.notificwebsockets;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class Notificacion extends Service {
    public Notificacion() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("SOCKET","service");
        OkHttpClient cli=new OkHttpClient();

        ///colocar la ip del servidor socket
        Request req=new Request.Builder()
                .url("ws://194.168.0.1:8080")
                .build();

        WebSocket webSocket=cli.newWebSocket(req, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                super.onOpen(webSocket, response);
                Log.e("SOCKET","se conecto al socket");
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                super.onMessage(webSocket, text);
                Log.e("MESS",text);

                try {
                    JSONObject json=new JSONObject(text);
                    NotificationCompat.Builder nf=new NotificationCompat.Builder(Notificacion.this,"1")
                            .setContentTitle(json.getString("titulo"))
                            .setContentText(json.getString("mensaje"));

                    NotificationManager mn= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    mn.notify(1,nf.build());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                super.onMessage(webSocket, bytes);
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                super.onClosing(webSocket, code, reason);
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                super.onClosed(webSocket, code, reason);
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                super.onFailure(webSocket, t, response);
                Log.e("fallo","error");
            }
        });

        cli.dispatcher().executorService().shutdown();

        return super.onStartCommand(intent, flags, startId);
    }
}
