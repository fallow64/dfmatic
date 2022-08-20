package me.fallow64.util;

import me.fallow64.builder.values.VariableScope;
import me.fallow64.builder.values.impl.Variable;

import java.util.*;

public class RandomUtil {

    private static final Random random = new Random();
    private static final Stack<Integer> usedInts = new Stack<>();

    static {
        for (int i=1; i<1000000; i++) usedInts.add(i);
        Collections.shuffle(usedInts);
    }

    public static Random getRandom() {
        return random;
    }

    public static int randomUniqueInt() {
        return usedInts.pop();
    }

    public static String randomName() {
        return "$tmp" + randomUniqueInt();
    }

    public static Variable randomVar() {
        return new Variable(randomName(), VariableScope.LOCAL);
    }

}
