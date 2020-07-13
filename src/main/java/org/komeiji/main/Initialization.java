package org.komeiji.main;

import org.komeiji.commands.miscellaneous.Miscellaneous;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

public class Initialization {
    public static void initialize() throws InvocationTargetException, IllegalAccessException {
        ArrayList<Method> commands = new ArrayList<>();
        Set<Class<?>> classes = new Reflections("org.komeiji.commands").getSubTypesOf(Object.class);

        for (Class c : classes)
            Collections.addAll(commands, c.getDeclaredMethods());


    }
}
