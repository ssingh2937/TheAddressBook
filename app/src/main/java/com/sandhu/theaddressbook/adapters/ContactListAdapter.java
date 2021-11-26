package com.sandhu.theaddressbook.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sandhu.theaddressbook.ContactDetailsActivity;
import com.sandhu.theaddressbook.Models.ContactListModel;
import com.sandhu.theaddressbook.R;

import java.util.ArrayList;
import java.util.List;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ViewHolder> {
    Context context;
    ArrayList<ContactListModel> contactList;
    DatabaseAdapter databaseAdapter;

    public ContactListAdapter(Context context, ArrayList<ContactListModel> contactList) {
        this.context = context;
        this.contactList = contactList;
        databaseAdapter = new DatabaseAdapter(context);
        //contactList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.contact_list_cell, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nameText.setText(contactList.get(position).getFirstName() + " " + contactList.get(position).getLastName());
        holder.phoneText.setText(contactList.get(position).getPhone());

        try{
            byte[] imageByteArray = Base64.decode(contactList.get(position).getImage(), Base64.DEFAULT);
            Glide.with(context).asBitmap()
                    .load(imageByteArray)
                    .into(holder.profileImage);
        } catch (Exception e){
            Log.e("onCreate: ", e.getMessage());
        }

        /* try{
            Bitmap bitmap = BitmapFactory.decodeByteArray(contactList.get(position).getImage(), 0, contactList.get(position).getImage().length);
            Glide.with(context).load(bitmap).into(holder.profileImage);
        } catch (Exception e){
            Log.e("onBindViewHolder: ", e.getMessage());
        }*/


    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameText, phoneText;
        ImageView profileImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.contact_name);
            phoneText = itemView.findViewById(R.id.contact_phone);
            profileImage = itemView.findViewById(R.id.contact_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ContactDetailsActivity.class);
                    Bundle bundle = intent.getExtras();
                    intent.putExtra("first_name", contactList.get(getAdapterPosition()).getFirstName());
                    intent.putExtra("last_name", contactList.get(getAdapterPosition()).getLastName());
                    intent.putExtra("phone", contactList.get(getAdapterPosition()).getPhone());
                    intent.putExtra("email", contactList.get(getAdapterPosition()).getEmail());
                    try{
                        bundle.putByteArray("image", contactList.get(getAdapterPosition()).getImage());
                    } catch (Exception e){
                        Log.e( "onClick: ", e.getMessage());
                    } finally {

                    }

                    context.startActivity(intent);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    new AlertDialog.Builder(context)
                            .setTitle("Delete contact")
                            .setMessage("Are you sure you want to delete this contact?")

                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //delete the contact
                                    databaseAdapter.delete(contactList.get(getAdapterPosition()).getID());
                                }
                            })

                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_menu_delete)
                            .show();
                    return true;
                }
            });
        }
    }
}
