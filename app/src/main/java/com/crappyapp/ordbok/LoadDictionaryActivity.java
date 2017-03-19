package com.crappyapp.ordbok;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.os.SystemClock.sleep;

public class LoadDictionaryActivity extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    // Load the dictionary
    static protected Multimap load(File file) throws IOException {
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(file));
            @SuppressWarnings("unchecked")
            Multimap tmp = (HashMultimap) ois.readObject();
            ois.close();
            return tmp;
        } catch (Exception e) {
            throw new IOException();
        }
    }


    /**
     * Copies raw resource to a cache file.
     *
     * @return File reference to cache file.
     * @throws IOException
     */
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_dictionary);


        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    // Start a video
                    VideoView videoView = (VideoView) findViewById(R.id.VideoView);
                    String uriPath = "android.resource://" + getPackageName() + "/" + R.raw.intro;
                    Uri uri = Uri.parse(uriPath);
                    videoView.setVideoURI(uri);
                    videoView.start();

                    Globals g = (Globals)getApplication();
                    g.readData();
                    ArrayListMultimap<String, Long> keymap = g.getData();


                    // Load the dictionary
                    File cacheFile = createCacheFile(getApplicationContext(),
                            R.raw.dictionary, "delete-me-please");
                    RandomAccessFile dictionary = new RandomAccessFile(cacheFile, "r");


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
                            aword = new Word(dictionary, fileptrpos);
                            wordlist.add(aword);
                        }

                        System.out.println("You looked for the word: " + lookup + "\n");
                        System.out.println("The following matches were found:\n");
                        for (Word w : wordlist) {
                            System.out.println(w);
                        }
                        dictionary.close();
                        cacheFile.delete();
                    }
                    sleep(3000);
                    //ArrayListMultimap<String, Long> keymap2 = ArrayListMultimap.create(keymap);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    //intent.putExtra("keymap", keymap);
                    //intent.putExtra("uri", uriPath);
                    startActivity(intent);
                    finish();
                    //} catch (InterruptedException e) {
                    //    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("LoadDictionary Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
