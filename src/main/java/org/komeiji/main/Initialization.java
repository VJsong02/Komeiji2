package org.komeiji.main;

import org.komeiji.commands.miscellaneous.Miscellaneous;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Initialization {
    public static void initialize() throws InvocationTargetException, IllegalAccessException {
        Method[] methods = Miscellaneous.class.getDeclaredMethods();
        for (Method m : methods)
            m.invoke("");
    }
}
