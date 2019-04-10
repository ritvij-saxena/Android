package co.rj.saxena.notes;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


public class MyViewHolder extends RecyclerView.ViewHolder{


    public TextView title;
    public TextView note;
    public TextView timestamp;

    public MyViewHolder(@NonNull View view) {
        super(view);

       note = view.findViewById(R.id.notes);
       title = view.findViewById(R.id.title);
       timestamp = view.findViewById(R.id.timestamp);
    }
}
