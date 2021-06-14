package com.pulkitgangwar.chatapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private ArrayList<User> users = null;
    Context parentContext;

    public UserAdapter(ArrayList<User> users) {
        this.users = users;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        parentContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_user_box_view,parent,false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = users.get(position);
        holder.email.setText(user.getEmail());
        holder.name.setText(user.getName());
        Picasso.get().load(user.getImageUri()).into(holder.userProfileImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parentContext,ChatActivity.class);
                intent.putExtra("name",user.getName());
                intent.putExtra("email",user.getEmail());
                intent.putExtra("userProfileImage",user.getImageUri());
                intent.putExtra("uid", FirebaseAuth.getInstance().getUid());
                parentContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class UserViewHolder extends  RecyclerView.ViewHolder{
        TextView email,name;
        CircleImageView userProfileImage;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            email = itemView.findViewById(R.id.userbox__email);
            name = itemView.findViewById(R.id.userbox__name);
            userProfileImage = itemView.findViewById(R.id.userbox__userprofile);
        }
    }
}
