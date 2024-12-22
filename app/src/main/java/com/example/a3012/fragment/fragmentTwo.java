package com.example.a3012.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a3012.R;
import com.example.a3012.activitys.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragmentTwo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragmentTwo extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragmentTwo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragmentTwo.
     */
    // TODO: Rename and change types and number of parameters
    public static fragmentTwo newInstance(String param1, String param2) {
        fragmentTwo fragment = new fragmentTwo();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_two, container, false);

        Button button_reg = view.findViewById(R.id.reg_button);
        button_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Navigation.findNavController(v).navigate(R.id.action_fragmentTwo_to_fragmentOne);
                EditText password1 = view.findViewById(R.id.password1);
                EditText password2 = view.findViewById(R.id.password2);

                String pass1 = password1.getText().toString();
                String pass2 = password2.getText().toString();

                // בדיקה אם הסיסמאות תואמות
                if (pass1.equals(pass2)) {
                    if(pass1.length()<6) {
                        Toast.makeText(getContext(), "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();

                    }
                    else{
                        // הסיסמאות תואמות - אפשר להמשיך עם הרישום
                        MainActivity mainActivity = (MainActivity) getActivity();
                        mainActivity.reg(v); // הרישום ב-activity
                        mainActivity.addDATA(); // הוספת הנתונים למערכת
                    }
                }
                else {
                    // הסיסמאות לא תואמות - הצגת הודעת שגיאה
                    Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                }
            }




        });
        return view;}
}
