package com.example.andrioid.pracainzynierska.view;


import android.content.Context;
import android.content.Intent;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.andrioid.pracainzynierska.model.Project;
import com.example.andrioid.pracainzynierska.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.List;

import static com.example.andrioid.pracainzynierska.view.TaskListActivity.PROJECT_PATH;


public class ProjectAdapter extends FirestoreRecyclerAdapter<Project, ProjectAdapter.ProjectHolder> {
    private OnItemClickListener listener;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */


    Context context;

    public ProjectAdapter(@NonNull FirestoreRecyclerOptions<Project> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ProjectHolder holder, int position, @NonNull Project model) {
        holder.textViewTitle.setText(String.valueOf(model.getTitle()));
        holder.textViewDescription.setText(String.valueOf(model.getDescription()));
        holder.textViewPriority.setText(String.valueOf(model.getPriority()));
    }

    @NonNull
    @Override
    public ProjectHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.project_in_recycle, viewGroup, false);
        return new ProjectHolder(view);
    }

    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }


    class ProjectHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        TextView textViewDescription;
        TextView textViewPriority;
        ImageView imageViewIntent;
        CardView cardView;

        public ProjectHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cv);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            textViewPriority = itemView.findViewById(R.id.text_view_priority);
            imageViewIntent = itemView.findViewById(R.id.iv_intent);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    FirebaseFirestore.getInstance().collection("ProjectList").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            int position = getAdapterPosition();
                            Project project = getItem(position);

                            List<Project> projectList = queryDocumentSnapshots.toObjects(Project.class);
                            for (Project projectOb : projectList) {
                                if (projectOb.getId() == project.getId()) {
                                    int itemPositon = projectList.indexOf(projectOb);
                                    DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(itemPositon);
                                    Intent taskIntent = new Intent(context, TaskListActivity.class);
                                    taskIntent.putExtra(PROJECT_PATH, documentSnapshot.getReference().getPath());
                                    context.startActivity(taskIntent);
                                    if (position != RecyclerView.NO_POSITION && listener != null) {
                                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                                    }

                                }
                            }

                        }
                    });

                }
            });

            imageViewIntent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseFirestore.getInstance().collection("ProjectList").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            int position = getAdapterPosition();
                            Project project = getItem(position);
                            Calendar beginTime = Calendar.getInstance();
                            beginTime.set(project.getYear(),project.getMonth(),project.getDay(),project.getHour(),project.getMinute());
                            Calendar endTime = Calendar.getInstance();
                            endTime.set(project.geteYear(),project.geteMonth(),project.geteDay(),project.geteHour(),project.geteMinute());
                            Intent intent = new Intent(Intent.ACTION_INSERT)
                                    .setData(CalendarContract.Events.CONTENT_URI)
                                    .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                                    .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                                    .putExtra(CalendarContract.Events.TITLE, project.getTitle())
                                    .putExtra(CalendarContract.Events.DESCRIPTION, project.getDescription());
                            context.startActivity(intent);
                        }
                    });
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}
