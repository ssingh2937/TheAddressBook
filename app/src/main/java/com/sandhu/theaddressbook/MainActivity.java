package com.sandhu.theaddressbook;

import androidx.annotation.MainThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sandhu.theaddressbook.Models.ContactListModel;
import com.sandhu.theaddressbook.adapters.ContactListAdapter;
import com.sandhu.theaddressbook.adapters.DatabaseAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    RecyclerView contactList;
    FloatingActionButton addContactBtn;
    DatabaseAdapter databaseAdapter;
    SearchView searchView;
    ArrayList<ContactListModel> contactArrList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();

        databaseAdapter = new DatabaseAdapter(this);
        contactArrList = new ArrayList<ContactListModel>();

        contactList = findViewById(R.id.contact_list);
        addContactBtn = findViewById(R.id.add_contact_fab);
        searchView = findViewById(R.id.search_view);

        addContactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddContactActivity.class);
                startActivity(intent);

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                contactArrList = databaseAdapter.searchData(newText);
                contactArrList.clear();
                ContactListAdapter contactListAdapter = new ContactListAdapter(MainActivity.this, contactArrList);
                contactList.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                contactList.setAdapter(contactListAdapter);
                return true;
            }
        });

        contactArrList = databaseAdapter.getData();

        ContactListAdapter contactListAdapter = new ContactListAdapter(this, contactArrList);
        contactList.setLayoutManager(new LinearLayoutManager(this));
        contactList.setAdapter(contactListAdapter);

        //Toast.makeText(this, databaseAdapter.getData(), Toast.LENGTH_SHORT).show();

    }
}