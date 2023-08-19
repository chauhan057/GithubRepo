package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class AddRepo extends AppCompatActivity {
    EditText ownerET;
    EditText repoET;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_repo);
        ownerET=findViewById(R.id.ownerEditText);
        repoET=findViewById(R.id.repoNameEditText);
        btn=findViewById(R.id.addButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String owner = ownerET.getText().toString();
                String repo = repoET.getText().toString();
                GitHubApiService apiService = ApiClient.getClient().create(GitHubApiService.class);
                Call<Repository> call = apiService.getRepository(owner, repo);

                call.enqueue(new Callback<Repository>() {
                    @Override
                    public void onResponse(Call<Repository> call, Response<Repository> response) {
                        Repository repositoryResponse = response.body();
                        Toast.makeText(AddRepo.this, "successful", Toast.LENGTH_LONG).show();
                        String repositoryName = repositoryResponse.getName();
                        String repositoryDescription = repositoryResponse.getDescription();
                        ownerET.setText("");
                        repoET.setText("");
                        RepositoryEntity repositoryEntity = new RepositoryEntity(owner, repositoryName, repositoryDescription);
                        new InsertRepositoryTask().execute(repositoryEntity);
                        Intent intent=new Intent();
                        setResult(2,intent);
                        finish();//finishing activity
                    }

                    @Override
                    public void onFailure(Call<Repository> call, Throwable t) {
                        // ... onFailure code ...
                        Toast.makeText(AddRepo.this,"Failed"+ t.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                        finish();//finishing activity
                    }
                });
            }
        });
    }

    private class InsertRepositoryTask extends AsyncTask<RepositoryEntity, Void, Void> {
        @Override
        protected Void doInBackground(RepositoryEntity... repositoryEntities) {
            try {
                AppDatabase appDatabase = ((MyApplication) getApplication()).getAppDatabase();
                RepositoryDao repositoryDao = appDatabase.repositoryDao();
                repositoryDao.insertRepository(repositoryEntities[0]);
            } catch (Exception e) {
                Log.e("InsertRepositoryTask", "Error inserting repository", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(AddRepo.this, "Repository Added Successfully.", Toast.LENGTH_LONG).show();
        }
    }
}
