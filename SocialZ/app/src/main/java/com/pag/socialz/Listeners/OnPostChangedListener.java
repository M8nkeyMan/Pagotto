package com.pag.socialz.Listeners;

import com.pag.socialz.Models.Post;

public interface OnPostChangedListener {

    public void onObjectChanged(Post obj);

    public void onError(String errorText);
}
