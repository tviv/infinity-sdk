package com.foxifinder.infinity.sdk.utils;

import android.annotation.SuppressLint;

import java.util.HashMap;
import java.util.Map;

public class AwatingTwoKeysStack<T> {

    private class ItemStruct {
        boolean reusable;
        T item;

        ItemStruct(T item, boolean reusable) {
            this.item = item;
            this.reusable = reusable;
        }
    }


    private Map<String, Integer> mIdsByTypes = new HashMap<>();
    @SuppressLint("UseSparseArrays")
    private Map<Integer, ItemStruct> mItemArray = new HashMap<>();

    public void push(String type, int id, T item, boolean reusable) {
        Integer oldId = mIdsByTypes.get(type);
        if (oldId != null) mItemArray.remove(oldId);

        mIdsByTypes.put(type, id);
        mItemArray.put(id, new ItemStruct(item, reusable));
    }

    public T pop(int id) {
        T result = null;
        ItemStruct struct = mItemArray.get(id);
        if (struct != null) {
            result = struct.item;
            if (!struct.reusable) mItemArray.remove(id);
        }

        return result;
    }

}


