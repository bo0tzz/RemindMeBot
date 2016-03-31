package me.bo0tzz.remindmebot.gson;

import com.google.common.collect.TreeMultiset;
import com.google.gson.InstanceCreator;

import java.lang.reflect.Type;

/**
 * Created by boet on 31-3-2016.
 */
public class TreeMultisetInstanceCreator implements InstanceCreator<TreeMultiset> {
    @Override
    public TreeMultiset createInstance(Type type) {
        return TreeMultiset.create();
    }
}
