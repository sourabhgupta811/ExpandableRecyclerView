package com.example.smoothcardanimation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<ExpandModel> arrayList = new ArrayList<>();
        for (int i = 0; i < 10; i++)
            arrayList.add(new ExpandModel());
        //recyclerView is passes to achieve expand/collapse functionality correctly.
        adapter = new RecyclerViewAdapter(recyclerView);
        adapter.setData(arrayList);
        recyclerView.setAdapter(adapter);
    }

    public void addItem(View view) {
        adapter.addItem(2);
    }

    public void deleteItem(View view) {
        adapter.deleteItem(2);
    }
}
