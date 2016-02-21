package com.getirhackathon;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.getirhackathon.activity.DetailFragment;
import com.getirhackathon.activity.DrawerFragment;
import com.getirhackathon.activity.LoginActivity;
import com.getirhackathon.activity.ProductsFragment;
import com.getirhackathon.activity.SepetFragment;
import com.getirhackathon.activity.TakipFragment;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Ack;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity implements DrawerFragment.FragmentDrawerListener{

    private static String TAG = MainActivity.class.getSimpleName();

    private DrawerFragment drawerFragment;
    private Toolbar mToolbar;
    private SharedPreferences pref;
    private static Socket mSocket;
    private ProgressDialog dialog;
    {
        try {
            mSocket = IO.socket("https://getir-hackathon.herokuapp.com/");
        } catch (URISyntaxException e) {}
    }
    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
/*                        JSONObject data = (JSONObject) args[0];
                        try {
                            if(data.getBoolean("success")){
                                App.getInstance().addToSepet(Integer.parseInt(data.getString("product_id")),
                                                        Integer.parseInt(data.getString("order_count")));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
  */                      Log.d("SOCKET:INFO", args[0] + "");

                    }
                });
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSocket.on("status",onNewMessage);
        mSocket.on("order_message",onNewMessage);
        mSocket.connect();
        dialog = new ProgressDialog(this);
        //Assign toolbar..
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        //Set the toolbar as default actionbar
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Navigation drawer menü..
        drawerFragment = (DrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        //Check credentials..
        pref = this.getSharedPreferences(
                "GETIR", Context.MODE_PRIVATE);
        if(pref.getString("token",null)==null)
            startActivityForResult(new Intent(MainActivity.this, LoginActivity.class),1);

        //Display the first navigation drawer view on app launch
        displayView(0);
    }

    public void attemptSend(String message) {
        dialog.setCancelable(false);
        dialog.setIndeterminate(true);
        dialog.show();
        if (TextUtils.isEmpty(message))
            return;
        mSocket.emit("order", message, new Ack() {
            @Override
            public void call(Object... args) {
                Log.d("SOCKET:IO",args[0]+"");
            }
        });

    }

    private void displayView(int position) {

        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                fragment = new ProductsFragment();
                title = getString(R.string.title_products);
                break;
            case 1:
                fragment = new SepetFragment();
                title = getString(R.string.title_sepet);
                break;
            case 2:
                fragment = new TakipFragment();
                title = getString(R.string.title_takip);
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment).addToBackStack("frag");
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }



    @Override
    public void onBackPressed() {
        Toast.makeText(this ,"adgadg" ,Toast.LENGTH_SHORT).show();
        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getFragmentManager().popBackStack();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            if(resultCode== Activity.RESULT_OK){
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("token", "123456");
                editor.commit();
                //fetchProducts();
            }
        }
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        Log.d("INFO","seleected");
        displayView(position);
    }

    public void switchContent(DetailFragment frag) {
        frag.show(getFragmentManager(),"blur");
        /*
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.show(id, fragment, fragment.toString());
        ft.addToBackStack(null);
        ft.commit();
    */
    }
}
