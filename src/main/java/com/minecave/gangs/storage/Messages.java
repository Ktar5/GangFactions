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
        for(String key : Gangs.getInstance().getMessages().getConfig().getKeys(false)) {
            messages.put(key, StringUtil.colorString(Gangs.getInstance().getMessages().getConfig().getString(key)));
        }
    }

    public static String get(String key){
        if(!messages.containsKey(key)) {
            String s ="This_value_doesn't_exist,_and_doesn't_have_any_arguments";
            Gangs.getInstance().getMessages().set(key, s, true);
            return s;
        }
        return messages.get(key);
    }

    //for "values" pattern is like this:
    // variable, replacer, variable, replacer, variable replacer, etc...
    public static String get(String key, String... values){
        if(values.length % 2 != 0){
            return "Error while trying to get string " + key + "\n A variable doesn't have a pair";
        }else{
            if(!messages.containsKey(key)) {
                String s = "This_value_doesn't_exist,_and_has_the_following_arguments:";
                int n = 1;
                while(n <= values.length){
                    s += "_" + values[n-1];
                    n += 2;
                }
                Gangs.getInstance().getMessages().set(key, s, true);
                return "Generated default!";
            }else {
                String s = messages.get(key);
                int n = 1;
                while (n <= values.length) {
                    s.replaceAll(values[n - 1], values[n]);
                    n += 2;
                }
                return s;
            }
        }
    }


}
