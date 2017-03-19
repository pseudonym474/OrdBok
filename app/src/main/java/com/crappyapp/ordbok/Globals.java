package com.crappyapp.ordbok;

import android.app.Application;

import com.google.common.collect.ArrayListMultimap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by TIKE on 12.03.2017.
 */

public class Globals extends Application {

    private ArrayListMultimap<String, Long> keymap;

    public ArrayListMultimap<String, Long> getData(){
        return this.keymap;
    }

    public void readData() throws IOException {
        // Load keymap from assets
        this.keymap = ArrayListMultimap.create();
        //Multimap keymap = HashMultimap.create();
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(getResources().getAssets().open("keymap.txt")));
        String line = reader.readLine();
        while (line != null) {
            String[] bits  =  line.split("@");
            String[] ptrs  = bits[1].replace("[","").replace("]","").split(",");
            for (String ptr : ptrs){
                this.keymap.put(bits[0].trim(), Long.parseLong(ptr.trim()));
            }
            line = reader.readLine();
        }
    }
}
