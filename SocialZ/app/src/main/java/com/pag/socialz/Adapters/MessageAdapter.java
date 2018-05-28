package com.pag.socialz.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.pag.socialz.Activities.MainActivity;
import com.pag.socialz.Enums.ItemType;
import com.pag.socialz.Models.Message;
import com.pag.socialz.R;

import java.util.LinkedList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final String TAG = MessageAdapter.class.getSimpleName();

    protected List<Message> messageList = new LinkedList<>();
    protected MainActivity activity;
    protected int selectedMessagePosition = -1;
    private long lastLoadedItemCreatedDate;
    private Callback callback;
    private boolean isLoading = false;
    private boolean isMoreDataAvailable = true;

    public MessageAdapter(MainActivity activity){
        this.activity = activity;
    }

    public interface Callback {
        void onListLoadingFinished();
        void onCanceled(String message);
    }

    protected void cleanSelectedPostInformation(){
        selectedMessagePosition = -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(position >= getItemCount() && isMoreDataAvailable && !isLoading){
            android.os.Handler mHandler = activity.getWindow().getDecorView().getHandler();
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if(activity.hasInternetConnection()){
                        isLoading = true;
                        messageList.add(new Message(ItemType.LOAD));
                        loadNext(lastLoadedItemCreatedDate -1);
                    }else{
                        activity.showFloatButtonRelatedSnackBar(R.string.internet_connection_failed);
                    }
                }
            });
            if (getItemViewType(position) != ItemType.LOAD.getTypeCode()) {
                ((MessageViewHolder) holder).bindData(messageList.get(position));
            }
        }

    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    private void loadNext(final long nextItemCreatedDate) {

    }
}
