package com.getirhackathon;

import android.app.Application;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.getirhackathon.model.Product;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by erkam on 20.02.2016.
 */
public class App extends Application {

    public static final String TAG = App.class.getSimpleName();
    private static App mInstance;
    private RequestQueue mRequestQueue;
    private HashMap<Product, Integer> sepet = new HashMap<>();
    private HashMap<Integer, Integer> lastOrder = new HashMap<>();
    private Location location;
    private boolean timerOn = false;

    public SharedPreferences getPref() {
        return pref;
    }

    private SharedPreferences pref;
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        pref = getSharedPreferences("GETIR",MODE_PRIVATE);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(50 * 1024 * 1024) // 50 Mb
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs() // Remove for release app
                .build();
        ImageLoader.getInstance().init(config);
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void addToSepet(Product product, int newCount) {

        if(!timerOn){
            timerOn=true;
            final Handler mHandler = new Handler() {
                public void handleMessage(Message msg) {
                    sepet.clear();
                    lastOrder.clear();
                    Toast.makeText(getApplicationContext(),"Zaman aşımından dolayı sepetiniz iptal edildi",Toast.LENGTH_LONG).show();
                    timerOn=false;
                }
            };
            Timer t = new Timer();
            t.schedule(new TimerTask() {
                @Override
                public void run() {
                        mHandler.obtainMessage(1).sendToTarget();
                    }
            },30000);

        }


        int count = sepet.get(product) != null ? sepet.get(product) : 0;
        Log.d("INFO",sepet.containsKey(product) + "");
        sepet.put(product, newCount);
        //TODO


    }

    public void addToSepet(int productId, int newCount) {
        int count = sepet.get(productId) != null ? sepet.get(productId) : 0;
        lastOrder.put(productId, newCount + count);

    }


    public HashMap<Product, Integer> getSepet() {
        return sepet;
    }


    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public static synchronized App getInstance() {
        return mInstance;
    }

}
