package com.kisita.caritas;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements PublishFragment.OnPublishInteractionListener, BottomNavigationView.OnNavigationItemSelectedListener, InvestigatorFragment.OnInvestigatorInteractionListener {

    private final static String TAG      = "MainActivity";

    private final static String SECTIONS = "sections";

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private ArrayList<Section> mSections =  new ArrayList<>();

    /* UI button
     */


    private SectionPagerAdapter mSectionPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        //
        if(savedInstanceState != null){
            mSections = (ArrayList<Section>) savedInstanceState.getSerializable(SECTIONS);
        }else{
            populateSections();
        }
        // Create the adapter that will return a fragment for each section of the survey
        mSectionPagerAdapter = new SectionPagerAdapter(getSupportFragmentManager(), mSections);
        mViewPager.setAdapter(mSectionPagerAdapter);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
    }

    /**
     * @return Survey's sections and questions from json file
     * @throws IOException
     */
    @NonNull
    private String readSurveyFromResources() throws IOException {
        StringBuilder surveyJson = new StringBuilder();
        InputStream rawCategories = getResources().openRawResource(R.raw.survey);
        BufferedReader reader = new BufferedReader(new InputStreamReader(rawCategories));
        String line;
        Log.i(TAG,"Reading line ...");
        while ((line = reader.readLine()) != null) {
            Log.i(TAG,"New line  : "+line);
            surveyJson.append(line);
        }
        return surveyJson.toString();
    }

    // Create array of sections
    private void populateSections() {
        JSONArray jsonSurvey = null;
        JSONArray jsonQuestions = null;
        JSONObject section;
        JSONObject question;
        JSONArray  values   = null;
        Section sec;
        String s;

        try {
            jsonSurvey = new JSONArray(readSurveyFromResources());
            for (int i = 0; i < jsonSurvey.length(); i++) {
                // Get section
                section = jsonSurvey.getJSONObject(i);
                Log.i(TAG,"Section name is : "+ section.getString("name"));
                sec     = new Section(section.getString("name"));

                jsonQuestions = section.getJSONArray("questions");

                Question  q = null;
                for(int k = 0 ; k < jsonQuestions.length() ; k++){
                    question = jsonQuestions.getJSONObject(k);
                    // Get Question object
                    q  = new Question(question.getString("text"));
                    q.setEntryType(question.getString("type"));
                    values = question.getJSONArray("values");
                    q.addChoice("");
                    for(int j = 1; j <= values.length() ; j++){
                        //Log.i(TAG,"value "+ j +" : "+ values.get(j));
                        q.addChoice(values.get(j-1).toString());
                    }
                    sec.addNewQuestion(q);
                }
                mSections.add(sec);
            }
        }catch (JSONException e){
            //TODO Exceptions handling
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
        //printSections();
    }

    public void printSections(){
        for(Section s : mSections){
            Log.i(TAG,s.getName());
            for(Question q : s.getQuestions()){
                Log.i(TAG,q.getQuestion());
                for (String c : q.getChoices()){
                    Log.i(TAG,c);
                }
            }
        }
    }


    public void printFinalSections(){
        for(Section s : mSections){
            Log.i(TAG,s.getName());
            for(Question q : s.getQuestions()){
                Log.i(TAG,q.getQuestion());
                Log.i(TAG,"-->" + q.getChoice());
            }
        }
    }

    @Override
    public void onPublishInteraction() {
        Log.i(TAG,"Publish pressed");
        //printFinalSections();
        //
        if(!checkRequiredFields()){
            return;
        }
        String key = getDb("survey").push().getKey();
        Map<String, Object> childUpdates = new HashMap<>();
        int i = 1;
        int j = 1;

        for(Section s : mSections){
            childUpdates.put(getUid() + "/" + key + "/section_"+j+"/name",s.getName());
            for(Question q : s.getQuestions()){
                childUpdates.put(getUid() + "/" + key +  "/section_"+j+"/question"+ i +"/text" , q.getQuestion());
                childUpdates.put(getUid() + "/" + key +  "/section_"+j+"/question"+ i +"/choice" , q.getChoice());
                i++;
            }
            j++;
        }
        getDb("survey").updateChildren(childUpdates);
    }

    public DatabaseReference getDb(String reference) {
        return FirebaseDatabase.getInstance().getReference(reference);
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int i;
        mViewPager.setEnabled(false);
        switch (item.getItemId()) {
            case R.id.navigation_home:
                mViewPager.setCurrentItem(0);
                //Log.i(TAG,"Home selected...");
                return true;
            case R.id.navigation_previous:
                i = mViewPager.getCurrentItem();
                mViewPager.setCurrentItem(i-1);
                //Log.i(TAG,"questions selected...");
                return true;
            case R.id.navigation_next:
                switchRight();
                return true;
            case R.id.navigation_publish:
                mViewPager.setCurrentItem(mSections.size()+1);
                //Log.i(TAG,"notifications selected...");
                return true;
        }
        return false;
    }

    @Override
    public void onInvestigatorInteraction() {

    }

    public void switchRight(){
        int i = mViewPager.getCurrentItem(); // i give the section

        if(checkRequiredFieldsInSection(i)){
            mViewPager.setCurrentItem(i+1);
        }
    }

    public boolean checkRequiredFields(){
        for(int index  = 1 ; index < mSections.size() ; index++ ){
            if(checkRequiredFieldsInSection(index)){
                return true;
            }
        }
        return false;
    }

    public boolean checkRequiredFieldsInSection(int index){
        if(index > 0 && index < mSections.size() + 1){
            for(Question q  : mSections.get(index-1).getQuestions()){
                Log.i(TAG,"Question : "+q.getQuestion()+" - choice is  : " + q.getChoice());
                if(q.getChoice().equalsIgnoreCase("")){
                    Log.i(TAG,"Question : "+q.getQuestion()+" - choice is  : " + q.getChoice());
                    Toast.makeText(MainActivity.this, R.string.mandatory_fields,
                            Toast.LENGTH_LONG).show();
                    return false;
                }
            }
        }
        return true;
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Log.i(TAG,"onSaveInstanceState");
        savedInstanceState.putSerializable(SECTIONS,mSections);
        super.onSaveInstanceState(savedInstanceState);
    }
}
