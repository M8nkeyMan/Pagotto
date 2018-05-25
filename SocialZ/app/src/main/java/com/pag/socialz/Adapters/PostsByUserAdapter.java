package com.pag.socialz.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pag.socialz.Activities.BaseActivity;
import com.pag.socialz.Listeners.OnDataChangedListener;
import com.pag.socialz.Managers.LikeManager;
import com.pag.socialz.Managers.PostManager;
import com.pag.socialz.Models.Post;
import com.pag.socialz.R;
import com.pag.socialz.Adapters.PostViewHolder;

import java.util.List;

public class PostsByUserAdapter extends BasePostAdapter {

    public static final String TAG = PostsByUserAdapter.class.getSimpleName();

    private String userId;
    private CallBack callback;

    public PostsByUserAdapter (BaseActivity activity, String userId) {
        super(activity);
        this.userId = userId;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.post_item_list_view, parent, false);
        return new PostViewHolder(view, createOnClickListener(), false);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((PostViewHolder)holder).bindData(postList.get(position));
    }

    public interface CallBack {
        void onItemClick(Post post, View view);
        void onPostsListChanged(int postsCount);
        void onPostLoadingCanceled();
    }

    private PostViewHolder.OnClickListener createOnClickListener(){
        return  new PostViewHolder.OnClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                if(callback != null){
                    selectedPostPosition = position;
                    callback.onItemClick(getItemByPosition(position),view);
                }
            }

            @Override
            public void onLikeClick(LikeManager likeController, int position) {
                Post post = getItemByPosition(position);
                likeController.handleLikeClickAction(activity,post);
            }

            @Override
            public void onAuthorClick(int position, View view) {

            }
        };
    }

    private void setList(List<Post> posts){
        postList.clear();
        postList.addAll(posts);
        notifyDataSetChanged();
    }

    public void loadPosts(){
        if(!activity.hasInternetConnection()){
            activity.showSnackBar(R.string.internet_connection_failed);
            callback.onPostLoadingCanceled();
            return;
        }
        OnDataChangedListener<Post> onPostsDataChangedListener = new OnDataChangedListener<Post>() {
            @Override
            public void onListChanged(List<Post> list) {
                setList(list);
                callback.onPostsListChanged(list.size());
            }
        };
        PostManager.getInstance(activity).getPostsListByUser(onPostsDataChangedListener, userId);
    }

    public void removeSelectedPost(){
        postList.remove(selectedPostPosition);
        callback.onPostsListChanged(postList.size());
        notifyItemChanged(selectedPostPosition);
    }

    public void setCallback(CallBack callback){
        this.callback = callback;
    }
}
