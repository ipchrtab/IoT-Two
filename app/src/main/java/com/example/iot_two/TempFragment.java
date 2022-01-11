package com.example.iot_two;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import com.google.android.material.slider.Slider;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class TempFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private TempFragmentListener listener;
    Slider sliderTemp;
    SwitchMaterial switchTemp;

    public TempFragment() {
        // Required empty public constructor
    }

    public interface TempFragmentListener {
        void onInputSensorTemp (String sensorTemp);
    }

    public static TempFragment newInstance(String param1, String param2) {
        TempFragment fragment = new TempFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_temp, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        sliderTemp = (Slider) getView().findViewById(R.id.sliderTemp);
        sliderTemp.addOnSliderTouchListener(touchListener);
        switchTemp = (SwitchMaterial) getView().findViewById(R.id.switchTemp);
        switchTemp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    float sensor3 = sliderTemp.getValue();
                    String sensorTemp = String.valueOf(sensor3);
                    listener.onInputSensorTemp(sensorTemp);
                } else {
                    String sensorTemp = null;
                    listener.onInputSensorTemp(sensorTemp);
                }
            }
        });
    }

    private Slider.OnSliderTouchListener touchListener = new Slider.OnSliderTouchListener() {

        @Override
        public void onStartTrackingTouch(Slider slider) {
        }

        @Override
        public void onStopTrackingTouch(Slider slider) {
            if (switchTemp.isChecked()) {
                float sensor3 = slider.getValue();
                String sensorTemp = String.valueOf(sensor3);
                listener.onInputSensorTemp(sensorTemp);
            } else {
                String sensorTemp = null;
                listener.onInputSensorTemp(sensorTemp);
            }
        }
    };

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof TempFragmentListener) {
            listener = (TempFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement TempFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

}