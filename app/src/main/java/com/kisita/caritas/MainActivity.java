package com.kisita.caritas;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

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

public class MainActivity extends AppCompatActivity implements PublishFragment.OnPublishInteractionListener {

    private final static String TAG = "MainActivity";
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private ArrayList<Section> Sections =  new ArrayList<>();

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

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        //
        populateSections();
        // Create the adapter that will return a fragment for each section of the survey
        mSectionPagerAdapter = new SectionPagerAdapter(getSupportFragmentManager(),Sections);
        mViewPager.setAdapter(mSectionPagerAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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

                    values = question.getJSONArray("values");
                    for(int j = 0; j < values.length() ; j++){
                        //Log.i(TAG,"value "+ j +" : "+ values.get(j));
                        q.addChoice(values.get(j).toString());
                    }
                    sec.addNewQuestion(q);
                }
                Sections.add(sec);
            }
        }catch (JSONException e){
            //TODO Exceptions handling
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
        printSections();
    }

    public void printSections(){
        for(Section s : Sections){
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
        for(Section s : Sections){
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
        String key = getDb("survey").push().getKey();
        Map<String, Object> childUpdates = new HashMap<>();
        int i = 1;
        int j = 1;

        for(Section s : Sections){
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
        return FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
    }
}
