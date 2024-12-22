package com.example.a3012.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.Fragment;

import com.example.a3012.R;
import com.example.a3012.activitys.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragmentOne#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragmentOne extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_EMAIL = "email";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String getArgEmail;


    public fragmentOne() {
        // Required empty public constructor
    }



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragmentOne.
     */
    // TODO: Rename and change types and number of parameters
    public static fragmentOne newInstance(String param1, String param2,String email) {
        fragmentOne fragment = new fragmentOne();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_EMAIL, email);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            getArgEmail=getArguments().getString(ARG_EMAIL);
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_one, container, false);
        Button button1 = view.findViewById(R.id.buttonRegister);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {// v-> button , view->layout
                Navigation.findNavController(v).navigate(R.id.action_fragmentOne_to_fragmentTwo);

            }
        });

        Button button2 = view.findViewById(R.id.buttonLogin);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity =(MainActivity) getActivity();

                mainActivity.login(v);
                //Navigation.findNavController(v).navigate(R.id.action_fragmentOne_to_fragmentthree);
            }


        });
        return view;
    }


}