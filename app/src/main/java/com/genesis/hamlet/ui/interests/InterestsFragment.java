package com.genesis.hamlet.ui.interests;


import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.RelativeLayout;


import com.genesis.hamlet.R;
import com.genesis.hamlet.data.models.interests.Interests;
import com.genesis.hamlet.data.models.interests.MyInterests;
import com.genesis.hamlet.ui.users.UsersFragment;
import com.genesis.hamlet.util.BaseFragmentInteractionListener;
import com.genesis.hamlet.util.KeyboardUtil;
import com.genesis.hamlet.util.mvp.BaseView;
import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class InterestsFragment extends BaseView implements InterestsContract.View {

    //private Interests interests;
    private InterestsContract.Presenter presenter;
    private BaseFragmentInteractionListener fragmentInteractionListener;

    EditText etTitle;
    EditText etNickName;
    EditText etDescription;
    LinearLayout llCheckboxesList;
    CheckBox cbUseProfile;

    Button done;
    Button exit;

    public InterestsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new InterestsPresenter(this);
        //interests = Interests.getInstance();
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_interests, container, false);

        cbUseProfile = (CheckBox) view.findViewById(R.id.cbUserProfile);

        done = (Button) view.findViewById(R.id.btnSave);
        done.setEnabled(false);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getActivity().onBackPressed();
                fragmentInteractionListener.showFragment(UsersFragment.class, null, new Bundle(), false);
            }
        });

        //exit = (Button) view.findViewById(R.id.btnExit);
//        exit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //getActivity().finish();
//            }
//        });

        llCheckboxesList = (LinearLayout) view.findViewById(R.id.checkboxList);
        etTitle = (EditText) view.findViewById(R.id.etTitle);

        etTitle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                etDescription.setFocusableInTouchMode(false);
                etTitle.setFocusableInTouchMode(true);
                return false;
            }
        });

        etTitle.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    KeyboardUtil.hideSoftKeyboard(getActivity());
                    etTitle.setFocusable(false);
                    return true;
                }
                return false;
            }
        });

        etTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString();
                MyInterests.getInstance().setIntroTitle(text);
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkNewItemForEmptyValues();
            }
        });
        etNickName = (EditText) view.findViewById(R.id.etNickName);
        etNickName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString();
                MyInterests.getInstance().setNickName(text);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        setNickName();
        cbUseProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNickName();
            }
        });

        etDescription = (EditText) view.findViewById(R.id.etDescription);
        etDescription.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                etTitle.setFocusableInTouchMode(false);
                etDescription.setFocusableInTouchMode(true);
                return false;
            }
        });

        etDescription.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    KeyboardUtil.hideSoftKeyboard(getActivity());
                    etDescription.setFocusable(false);
                    return true;
                }

                return false;
            }
        });
        etDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString();
                MyInterests.getInstance().setIntroDetail(text);
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkNewItemForEmptyValues();
            }
        });
        initializeCheckBoxes();
        updateView();

        return view;
    }

    @Override
    public Interests getInterests() {
        return MyInterests.getInstance();
    }

    @Override
    public void resetForm() {
        Interests interests = getInterests();
        interests.setNickName("");
        interests.setIntroTitle("");
        interests.setIntroDetail("");
        interests.clearInterests();
        initializeCheckBoxes();

        updateView();
    }

    void updateView() {
        etDescription.setText(getInterests().getIntroDetail());
        etTitle.setText(getInterests().getIntroTitle());
        etNickName.setText(getInterests().getNickName());

        int m = Interests.getInterestTopics().length;
        CheckBox checkBox;
        for (int i= 0; i<m; i++) {
            checkBox = getCheckBox(i); //(CheckBox) llCheckboxesList.getChildAt(i);
            boolean isSelected = getInterests().getInterest(i);
            checkBox.setChecked(isSelected);
        }
    }

    private CheckBox getCheckBox(int n) {
        int q = ((LinearLayout) llCheckboxesList.getChildAt(0)).getChildCount();
        int i = n/q;
        int j = n%q;
        return (CheckBox) ((LinearLayout) llCheckboxesList.getChildAt(i)).getChildAt(j);
    }

    void onBoxClicked (boolean interested, int position) {
        MyInterests.getInstance().setInterest(interested, position);
        KeyboardUtil.hideSoftKeyboard(getActivity());
        etTitle.setFocusable(false);
        etDescription.setFocusable(false);
    }

    void initializeCheckBoxes() {
        int m = Interests.getInterestTopics().length;
        int p = llCheckboxesList.getChildCount();
        int q = ((LinearLayout) llCheckboxesList.getChildAt(0)).getChildCount();
        CheckBox checkBox;
        for (int i= 0; i<p; i++)
            for (int j = 0; j< q; j ++) {
                int n = i * q + j;
                checkBox = getCheckBox(n); //CheckBox) ((LinearLayout) llCheckboxesList.getChildAt(i)).getChildAt(j);
            if (n < m) {
                checkBox.setText(Interests.getInterestTopics()[n]);
                checkBox.setVisibility(View.VISIBLE);
                checkBox.setChecked(false);
                final int position = n;
                checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((CheckBox) v).isChecked();
                        onBoxClicked(((CheckBox) v).isChecked(),  position);
                    }
                });
            }
            else {
                checkBox.setVisibility(View.GONE);
            }
        }
    }

    private void setNickName() {
        etNickName.setEnabled(!cbUseProfile.isChecked());
        if (cbUseProfile.isChecked()) {
            etNickName.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
            MyInterests.getInstance().setNickName(etNickName.getText().toString());
            MyInterests.getInstance().setProfileUrl(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString());
        }
        else {
            etNickName.setInputType(InputType.TYPE_CLASS_TEXT);
            //// TODO: 10/26/17 add a preset profile image - null for now
            MyInterests.getInstance().setProfileUrl(null);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        fragmentInteractionListener.setTitle("Set Up Interests");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentInteractionListener = (BaseFragmentInteractionListener) context;
    }

    @Override
    public void onDetach() {
        fragmentInteractionListener = null;
        MyInterests.getInstance().setChanged(true);
        super.onDetach();
    }


    void checkNewItemForEmptyValues(){

        String newTitle= etTitle.getText().toString();
        String newDescription = etDescription.getText().toString();

        GradientDrawable gradientDrawable = (GradientDrawable) etTitle.getBackground();
        GradientDrawable gradientDrawable1 = (GradientDrawable) etDescription.getBackground();

        if(newTitle.equals("")){
            done.setEnabled(false);
            done.setTextColor(ContextCompat.getColor(getContext(),R.color.colorLightGrey));
            gradientDrawable.setStroke(4, ContextCompat.getColor(getContext(),R.color.colorBlue));

        } else{
            done.setEnabled(true);
            done.setTextColor(ContextCompat.getColor(getContext(),R.color.colorWhite));
            gradientDrawable.setStroke(4, ContextCompat.getColor(getContext(),R.color.colorGrey));
            //etTitle.setBackground();
        }
        if(newDescription.equals("")){
            done.setEnabled(false);
            done.setTextColor(ContextCompat.getColor(getContext(),R.color.colorLightGrey));
            gradientDrawable1.setStroke(4, ContextCompat.getColor(getContext(),R.color.colorBlue));

        } else{
            done.setEnabled(true);
            done.setTextColor(ContextCompat.getColor(getContext(),R.color.colorWhite));
            gradientDrawable1.setStroke(4, ContextCompat.getColor(getContext(),R.color.colorGrey));
            //etTitle.setBackground();
        }
    }
}
