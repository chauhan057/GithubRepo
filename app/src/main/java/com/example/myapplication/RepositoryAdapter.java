package com.example.myapplication;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.util.Consumer;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class RepositoryAdapter extends RecyclerView.Adapter<RepositoryAdapter.ViewHolder> {
    private List<RepositoryEntity> repositories;
    private Consumer<RepositoryEntity> onItemClick,onShareClick;

    public interface OnItemClickListener {
        void onItemClick(RepositoryEntity repositoryEntity);
    }

    private OnItemClickListener onItemClickListener;

    public RepositoryAdapter(List<RepositoryEntity> repositories,Consumer<RepositoryEntity> onItemClick,Consumer<RepositoryEntity> onShareClick) {
        this.repositories = repositories;
        this.onItemClick=onItemClick;
        this.onShareClick=onShareClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_repo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RepositoryEntity repository = repositories.get(position);
        holder.repoNameTextView.setText(repository.getRepoName());
        holder.descTV.setText(repository.getDescription());
        holder.shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onShareClick != null) {
                    onShareClick.accept(repository);
                }
            }
        });
        // Set other views' data here if needed
    }

    @Override
    public int getItemCount() {
        return repositories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView repoNameTextView;
        TextView descTV;

        Button shareButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && onItemClick != null) {
                        onItemClick.accept(repositories.get(position));
                    }
                }
            });
            repoNameTextView = itemView.findViewById(R.id.repoNameTextView);
            descTV=itemView.findViewById(R.id.repoDescriptionTextView);
            shareButton=itemView.findViewById(R.id.shareButton);
            // Initialize other views here if needed
        }
    }
}

