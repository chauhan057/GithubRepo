package com.example.myapplication;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "repositories")
public class RepositoryEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String owner;
    private String repoName;
    private String description;

    public RepositoryEntity(String owner, String repoName, String description) {
        this.owner = owner;
        this.repoName = repoName;
        this.description = description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getOwner() {
        return owner;
    }

    public String getRepoName() {
        return repoName;
    }

    public String getDescription() {
        return description;
    }
}

