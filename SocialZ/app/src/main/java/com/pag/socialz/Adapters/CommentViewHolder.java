package com.pag.socialz.Adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.pag.socialz.CustomViews.ExpandableTextView;
import com.pag.socialz.Listeners.OnObjectChangedListener;
import com.pag.socialz.Util.FormatterUtil;
import com.pag.socialz.Managers.ProfileManager;
import com.pag.socialz.Models.Comment;
import com.pag.socialz.Models.Profile;
import com.pag.socialz.R;

public class CommentViewHolder extends RecyclerView.ViewHolder{

    private final ImageView avatarImageView;
    private final ExpandableTextView commentTextView;
    private final TextView dateTextView;
    private final ProfileManager profileManager;
    private CommentAdapter.Callback callback;
    private Context context;

    public CommentViewHolder(View itemView,final CommentAdapter.Callback callback) {
        super(itemView);
        this.callback = callback;
        this.context = itemView.getContext();
        profileManager = ProfileManager.getInstance(itemView.getContext().getApplicationContext());

        avatarImageView = itemView.findViewById(R.id.avatarImageView);
        commentTextView = itemView.findViewById(R.id.commentText);
        dateTextView = itemView.findViewById(R.id.dateTextView);

        if (callback != null) {
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        callback.onLongItemClick(v, position);
                        return true;
                    }

                    return false;
                }
            });
        }
    }

    public void bindData(Comment comment){
        final String authorId = comment.getAuthorId();
        if(authorId != null){
            profileManager.getProfileSingleValue(authorId,createOnProfileChangeListener(commentTextView,avatarImageView,comment.getText()));
            commentTextView.setText(comment.getText());
            CharSequence date = FormatterUtil.getRelativeTimeSpanString(context,comment.getCreatedDate());
            dateTextView.setText(date);
            avatarImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callback.onAuthorClick(authorId,view);
                }
            });
        }
    }

    private OnObjectChangedListener<Profile> createOnProfileChangeListener(final ExpandableTextView expandableTextView, final ImageView avatarImageView, final String comment){
        return new OnObjectChangedListener<Profile>() {
            @Override
            public void onObjectChanged(Profile obj) {
                String userName = obj.getUsername();
                fillComment(userName,comment,expandableTextView);
                if(obj.getPhotoUrl() != null){
                    Glide.with(context)
                            .load(obj.getPhotoUrl())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .crossFade()
                            .into(avatarImageView);
                }
            }
        };
    }

    private void fillComment(String userName, String comment, ExpandableTextView commentTextView) {
        Spannable contentString = new SpannableStringBuilder(userName + "   " + comment);
        contentString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.highlight_text)),
                0, userName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        commentTextView.setText(contentString);
    }
}
