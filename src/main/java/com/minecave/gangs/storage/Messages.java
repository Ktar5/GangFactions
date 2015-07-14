package com.minecave.gangs.storage;

import com.minecave.gangs.Gangs;
import com.minecave.gangs.util.StringUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by Carter on 5/27/2015.
 */
public class Messages {

    //Don't hurt me ;~~;
    private static Map<String, String> messages = new HashMap<>();
    private static Map<String, List<String>> helps = new HashMap<>();

    public static void loadMessages(){
        for(String key : Gangs.getInstance().getMessages().getConfig().getKeys(true)) {
            /*if(Gangs.getInstance().getMessages().getConfig().get){

            }*/
            messages.put(key, StringUtil.colorString(Gangs.getInstance().getMessages().getConfig().getString(key)));
        }
        for(String key : Gangs.getInstance().getHelpConfig().getConfig().getKeys(false)){
            helps.put(key.toLowerCase(), StringUtil.colorList(Gangs.getInstance().getHelpConfig().getConfig().getStringList(key)));
        }
    }

    public static List<String> getHelp(String key){
        if(helps.containsKey(key.toLowerCase())){
            return helps.get(key.toLowerCase());
        }else{
            return null;
        }
    }

    public static String get(String key){
        if(!messages.containsKey(key)) {
            String s ="This_value_doesnt_exist_and_doesnt_have_any_arguments";
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
                String s = "This_value_doesnt_exist_and_has_the_following_arguments";
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
                    s = s.replaceAll(Pattern.quote(values[n - 1]), values[n]);
                    n += 2;
                }
                return s;
            }
        }
    }


}
