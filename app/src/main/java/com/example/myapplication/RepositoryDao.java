package com.example.myapplication;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface RepositoryDao {
    @Insert
    void insertRepository(RepositoryEntity repository);

    @Query("SELECT * FROM repositories")
    List<RepositoryEntity> getAllRepositories();
}
