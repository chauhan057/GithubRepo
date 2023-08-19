package com.example.myapplication;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.util.Consumer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<RepositoryEntity> repositoryList;
    private RepositoryAdapter repositoryAdapter;
    public RecyclerView recyclerView;
    Button emptyButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView=findViewById(R.id.recyclerView);
        emptyButton = findViewById(R.id.emptyButton);
        new LoadRepositoryTask().execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_add_repo) {
            Intent intent = new Intent(this, AddRepo.class);
            startActivityForResult(intent,2);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(requestCode==2){
            new LoadRepositoryTask().execute();
            if (repositoryList==null) {
                recyclerView.setVisibility(View.GONE);
                emptyButton.setVisibility(View.VISIBLE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                emptyButton.setVisibility(View.GONE);
            }

        }
    }
    private void shareRepository(RepositoryEntity repositoryEntity) {
        String shareText = "Check out this GitHub repository: " +
                "https://github.com/" + repositoryEntity.getOwner() + "/" + repositoryEntity.getRepoName();

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "GitHub Repository");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        startActivity(Intent.createChooser(shareIntent, "Share via"));
    }


    private class LoadRepositoryTask extends AsyncTask<Void, Void, List<RepositoryEntity>> {
        @Override
        protected List<RepositoryEntity> doInBackground(Void... voids) {
            AppDatabase appDatabase = ((MyApplication) getApplication()).getAppDatabase();
            RepositoryDao repositoryDao = appDatabase.repositoryDao();
            return repositoryDao.getAllRepositories();
        }


        @Override
        protected void onPostExecute(List<RepositoryEntity> repositories) {
            repositoryList = repositories;
            repositoryAdapter = new RepositoryAdapter(repositoryList, new Consumer<RepositoryEntity>() {
                @Override
                public void accept(RepositoryEntity repositoryEntity) {
                    String url = "https://github.com/" + repositoryEntity.getOwner() + "/" + repositoryEntity.getRepoName();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                }
            }, new Consumer<RepositoryEntity>() {
                @Override
                public void accept(RepositoryEntity repositoryEntity) {
                    shareRepository(repositoryEntity);
                }
            });
            if (repositoryList.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                emptyButton.setVisibility(View.VISIBLE);
                emptyButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this, AddRepo.class);
                        startActivityForResult(intent,2);
                    }
                });
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                emptyButton.setVisibility(View.GONE);
            }

            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            recyclerView.setAdapter(repositoryAdapter);
        }
    }
}
