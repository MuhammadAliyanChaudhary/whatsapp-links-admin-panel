package com.macdev.admingroupspanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class GroupsActivity extends AppCompatActivity {


    private List<ModelGroups> listGroups;
    private RecyclerView recycleGroups;
    private FirebaseFirestore db;

    SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);



        db = FirebaseFirestore.getInstance();
        swipeRefreshLayout = findViewById(R.id.refreshLayout);



        recycleGroups = findViewById(R.id.recycler_view_groups);
        recycleGroups.setHasFixedSize(true);
        recycleGroups.setLayoutManager(new LinearLayoutManager(this));



        AdapterGroups adapter_unNotify = new AdapterGroups(listGroups, getApplicationContext());



        listGroups = new ArrayList<>();






        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {



                listGroups.clear();
                adapter_unNotify.notifyDataSetChanged();

                fetchGroups();

            }
        });



        fetchGroups();












    }


    private void fetchGroups() {



        swipeRefreshLayout.setRefreshing(true);

        db.collection("Groups").orderBy("CreatedDate", Query.Direction.DESCENDING).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {


                                ModelGroups modelGroups = new ModelGroups(

                                        document.getString("GroupImage"),
                                        document.getString("GroupName"),
                                        document.getString("GroupCategory"),
                                        document.getString("GroupLink"));

                                listGroups.add(modelGroups);


                            }

                            AdapterGroups adapter_unNotify = new AdapterGroups(listGroups, getApplicationContext());
                            recycleGroups.setAdapter(adapter_unNotify);
                            swipeRefreshLayout.setRefreshing(false);
//                            progressDialog.dismiss();


                        } else {
                            swipeRefreshLayout.setRefreshing(false);
//                            progressDialog.dismiss();

                            Toast.makeText(GroupsActivity.this, "Error", Toast.LENGTH_SHORT).show();


                        }
                    }
                });


    }
}