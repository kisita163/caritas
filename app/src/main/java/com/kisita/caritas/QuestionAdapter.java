package com.kisita.caritas;

import android.content.Context;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/*
 * Created by HuguesKi on 01-12-17.
 */

public class QuestionAdapter extends RecyclerView.Adapter< QuestionAdapter.ViewHolder>{

    private final static String  TAG = "QuestionAdapter";
    private ArrayList<Question> mQuestions;
    private Context             mContext;

    public QuestionAdapter(Context context, ArrayList<Question> questions) {
        this.mContext   = context;
        this.mQuestions = questions;
    }

    @Override
    public QuestionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.question_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(QuestionAdapter.ViewHolder holder, int position) {
        Question d = mQuestions.get(position);
        holder.mQuestion.setText(position + 1 + ". " + d.getQuestion());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, d.getChoices());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.mValues.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        if(mQuestions == null)
            return 0;
        //Log.i(TAG,"Questions size is  : " + mQuestions.size() );
        return mQuestions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mQuestion;
        public final AppCompatSpinner mValues;


        public ViewHolder(View view) {
            super(view);
            mView     = view;
            mQuestion = (TextView) view.findViewById(R.id.question);
            mValues   = (AppCompatSpinner)view.findViewById(R.id.values);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mQuestion.getText() + "'";
        }
    }
}
