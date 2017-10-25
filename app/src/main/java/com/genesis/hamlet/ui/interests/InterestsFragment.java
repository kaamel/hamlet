package com.genesis.hamlet.ui.interests;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.genesis.hamlet.R;
import com.genesis.hamlet.data.models.interests.Interests;
import com.genesis.hamlet.util.BaseFragmentInteractionListener;
import com.genesis.hamlet.util.mvp.BaseView;


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

        llCheckboxesList = (LinearLayout) view.findViewById(R.id.checkboxList);
        etTitle = (EditText) view.findViewById(R.id.etTitle);
        etTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString();
                Interests.getInstance().setIntroTitle(text);
            }

            @Override
            public void afterTextChanged(Editable s) {

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
                Interests.getInstance().setNickName(text);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etDescription = (EditText) view.findViewById(R.id.etDescription);
        etDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString();
                Interests.getInstance().setIntroDetail(text);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        initializeCheckBoxes();
        updateView();

        return view;
    }

    @Override
    public Interests getInterests() {
        return Interests.getInstance();
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
            checkBox = (CheckBox) llCheckboxesList.getChildAt(i);
            boolean isSelected = getInterests().getInterest(i);
            checkBox.setChecked(isSelected);
        }
    }

    void onBoxClicked (boolean interested, int position) {
        Interests.getInstance().setInterest(interested, position);
    }

    void initializeCheckBoxes() {
        int m = Interests.getInterestTopics().length;
        int n = llCheckboxesList.getChildCount();
        CheckBox checkBox;
        for (int i= 0; i<n; i++) {
            checkBox = (CheckBox) llCheckboxesList.getChildAt(i);
            if (i<m) {
                checkBox.setText(Interests.getInterestTopics()[i]);
                checkBox.setVisibility(View.VISIBLE);
                checkBox.setChecked(false);
                final int position = i;
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

    @Override
    public void onDetach() {
        Interests.getInstance().setChanged(true);
        super.onDetach();
    }
}
