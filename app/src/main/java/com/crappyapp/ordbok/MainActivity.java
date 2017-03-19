package com.crappyapp.ordbok;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.ListViewAutoScrollHelper;
//import android.support.v7.widget.SearchView;
import android.widget.SearchView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.common.collect.ArrayListMultimap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Do this on the start of all onCreate methods
        // Replacing the layout with the appropriate layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the toolbar instance and set support action bar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get instance of floating action button at bottom of xml and listen/respond
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Get the navigation drawer instance and set it up for toggling
        // Note that the draw layout includes the navigationView
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle); // obviously listen for toggle
        toggle.syncState();

        // Get instance of navigation bar view
        // Got to listen for this in addition to the drawer
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        // list view and search view instances



        String [] keywords = {"Bob", "Smith", "John", "Gary", "Terry", "William", "Lucas"};
        ListView lv = (ListView)   findViewById(R.id.listView1);
        SearchView sv = (SearchView) findViewById(R.id.searchView1);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, keywords);
        lv.setAdapter(adapter);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });


        // Load the dictionary
        File cacheFile = null;
        RandomAccessFile dictionary = null;

        try {
            cacheFile = createCacheFile(getApplicationContext(),
                    R.raw.dictionary, "delete-me-please");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            dictionary = new RandomAccessFile(cacheFile, "r");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Globals g = (Globals)getApplication();
        ArrayListMultimap<String, Long> keymap = g.getData();
        boolean check_word = true;
        if (check_word) {
            System.out.println("Dictionary loaded");
            //long fileptrpos = -1;
            String lookup = "sær"; //"a-";
            List<Word> wordlist = new ArrayList<>();
            Word aword = null;
            for (long fileptrpos : keymap.get(lookup)) {
                //fileptrpos = (long) pos;
                System.out.println("Found at position " + fileptrpos);
                try {
                    aword = new Word(dictionary, fileptrpos);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                wordlist.add(aword);
            }

            System.out.println("You looked for the word: " + lookup + "\n");
            System.out.println("The following matches were found:\n");
            for (Word w : wordlist) {
                System.out.println(w);
            }
            //dictionary.close();
            //cacheFile.delete();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_lookup) {
            // Handle the camera action
            Toast.makeText(this, "Look up word!", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_wordLists) {
            Toast.makeText(this, "View/Learn word lists", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_share) {
            Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private File createCacheFile(Context context, int resourceId, String filename)
            throws IOException {
        File cacheFile = new File(context.getCacheDir(), filename);

        if (cacheFile.createNewFile() == false) {
            cacheFile.delete();
            cacheFile.createNewFile();
        }

        // from: InputStream to: FileOutputStream.
        InputStream inputStream = context.getResources().openRawResource(resourceId);
        FileOutputStream fileOutputStream = new FileOutputStream(cacheFile);

        byte[] buffer = new byte[1024 * 512];
        int count;
        while ((count = inputStream.read(buffer)) != -1) {
            fileOutputStream.write(buffer, 0, count);
        }

        fileOutputStream.close();
        inputStream.close();

        return cacheFile;
    }

}
