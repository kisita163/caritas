package com.kisita.caritas;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by HuguesKi on 01-12-17.
 *
 * A placeholder fragment containing a simple view.
 */

public class PlaceholderFragment extends Fragment {

    private  static final String TAG = "PlaceholderFragment";
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_QUESTIONS = "section_questions";

    public PlaceholderFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     * @param sectionQuestions
     */
    public static PlaceholderFragment newInstance(Section sectionQuestions) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_SECTION_QUESTIONS,(Serializable) sectionQuestions);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Section sectionQuestions = (Section) getArguments().getSerializable(ARG_SECTION_QUESTIONS);
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        RecyclerView recList = rootView.findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        StaggeredGridLayoutManager llm = new StaggeredGridLayoutManager(1,
                StaggeredGridLayoutManager.VERTICAL);

        recList.setLayoutManager(llm);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recList.getContext(),
                DividerItemDecoration.VERTICAL);
        recList.addItemDecoration(dividerItemDecoration);

        QuestionAdapter adapter = new QuestionAdapter(getContext(),sectionQuestions.getQuestions());
        recList.setAdapter(adapter);

        //printQuestions(sectionQuestions.getQuestions());

        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        textView.setText(sectionQuestions.getName());

        return rootView;
    }

    private void printQuestions(ArrayList<Question> questions){
        for(Question q : questions){
            //Log.i(TAG,q.getQuestion());
        }
    }
}
