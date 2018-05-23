package com.pag.socialz.Listeners;

import java.util.List;

public interface OnDataChangedListener<T> {
    public void onListChanged(List<T> list);
}
