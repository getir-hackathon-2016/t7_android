package com.getirhackathon;

import android.app.Activity;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.getirhackathon.activity.DetailFragment;
import com.getirhackathon.activity.DrawerFragment;
import com.getirhackathon.activity.ProductsFragment;
import com.getirhackathon.activity.SepetFragment;
import com.getirhackathon.activity.TakipFragment;

public class MainActivity extends AppCompatActivity implements DrawerFragment.FragmentDrawerListener{

    private static String TAG = MainActivity.class.getSimpleName();

    private DrawerFragment drawerFragment;
    private Toolbar mToolbar;
    private SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        pref = this.getSharedPreferences(
                "GETIR", Context.MODE_PRIVATE);
        if(pref.getString("token",null)==null)
            startActivityForResult(new Intent(MainActivity.this, LoginActivity.class),1);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        drawerFragment = (DrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        // display the first navigation drawer view on app launch
        displayView(0);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
