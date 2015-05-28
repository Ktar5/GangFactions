package com.minecave.gangs.storage;

import com.minecave.gangs.Gangs;
import com.minecave.gangs.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Carter on 5/27/2015.
 */
public class Messages {

    //Don't hurt me ;~~;
    private static Map<String, String> messages = new HashMap<>();

    public static void loadMessages(){
        for(String key : Gangs.messages.getConfig().getKeys(false)) {
            messages.put(key, StringUtil.colorString(Gangs.messages.getConfig().getString(key)));
        }
    }

    public static String get(String key){
        return messages.get(key);
    }

    //for "values" pattern is like this:
    // variable, replacer, variable, replacer, variable replacer, etc...
    public static String get(String key, String... values){
        if(values.length % 2 != 0){
            return "Error while trying to get string " + key + "//n A variable doesn't have a pair";
        }else{
            String string = get(key);
            int n = 1;
            while(n <= values.length){
                string.replaceAll(values[n-1], values[n]);
                n += 2;
            }
            return string;
        }
    }


}
