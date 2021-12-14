package com.example.whiskers;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CardFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "image";
    private static final String ARG_PARAM2 = "title";
    private static final String ARG_PARAM3 = "location";
    private static final String ARG_PARAM4 = "description";



    // TODO: Rename and change types of parameters
    private String mImage;
    private String mTitle;
    private String mLocation;
    private String mDescription;


    public CardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param mImage Parameter 1.
     * @param mTitle Parameter 2.
     * @param mLocation Parameter 3.
     * @param mDescription Parameter 4.
     * @return A new instance of fragment CardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CardFragment newInstance(String mImage, String mTitle, String mLocation, String mDescription) {
        CardFragment fragment = new CardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, mImage);
        args.putString(ARG_PARAM2, mTitle);
        args.putString(ARG_PARAM3, mLocation);
        args.putString(ARG_PARAM4, mDescription);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mImage = getArguments().getString(ARG_PARAM1);
            mTitle = getArguments().getString(ARG_PARAM2);
            mLocation = getArguments().getString(ARG_PARAM3);
            mDescription = getArguments().getString(ARG_PARAM4);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_card, container, false);
    }
}