package com.pag.socialz.Listeners;

import com.pag.socialz.Models.PostListResult;

public interface OnPostListChangedListener<Post> {

    public void onListChanged(PostListResult result);

    void onCanceled(String message);
}
