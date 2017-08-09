package com.orbitmines.api;

import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 6-8-2017
*/
public class ScrollerList<T> implements Cloneable {

    private List<T> list;
    private int index;

    public ScrollerList(List<T> list) {
        this.list = list;
        this.index = 0;
    }

    public T next() {
        index++;

        if (list.size() == index + 1)
            index = 0;

        return list.get(index);
    }

    public T get() {
        return list.get(index);
    }
}
