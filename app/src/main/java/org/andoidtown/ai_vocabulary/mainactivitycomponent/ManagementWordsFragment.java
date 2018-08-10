package org.andoidtown.ai_vocabulary.mainactivitycomponent;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.andoidtown.ai_vocabulary.R;
import org.andoidtown.ai_vocabulary.addwordcomponent.AddWordGruopActivity;
import org.andoidtown.ai_vocabulary.wordlistviewcomponent.WordGroupListViewActivity;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ManagementWordsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ManagementWordsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ManagementWordsFragment extends Fragment {

    Button showWordGroupListButton;
    Button showAddWGListViewButton;
    Button deleteAllWordButton;
    SQLiteDatabase db;
    public ManagementWordsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_management_words, container, false);

        db = getActivity().openOrCreateDatabase("vocabularyDataBase", Context.MODE_PRIVATE,null);
        showWordGroupListButton = rootView.findViewById(R.id.showWordsListButton);
        showWordGroupListButton.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), WordGroupListViewActivity.class);
                startActivity(intent);
            }
        });
        showAddWGListViewButton = rootView.findViewById(R.id.addWordButton);
        showAddWGListViewButton.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getActivity(), AddWordGruopActivity.class);
                startActivity(intent);
            }
        });
        deleteAllWordButton = rootView.findViewById(R.id.button_manage_deleteAllDB);
        deleteAllWordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    db.execSQL("drop table if exists word");
                    db.execSQL("drop table if exists word_group");
                    db.execSQL("drop table if exists word_test");
                } catch (Exception ex)
                {
                    Log.d("sqlerror",ex.toString());
                }
            }
        });
        return rootView;
    }


}