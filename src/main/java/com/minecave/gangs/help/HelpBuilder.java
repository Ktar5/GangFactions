package com.minecave.gangs.help;

/**
 * Created by Carter on 5/31/2015.
 */
public abstract class HelpBuilder {

    String message;

    public HelpBuilder(){

    }

    public abstract String build();

    @Override
    public String toString(){
        return build();
    }

}
