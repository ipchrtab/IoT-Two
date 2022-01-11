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

public class SmokeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private SmokeFragmentListener listener;
    Slider sliderSmoke;
    SwitchMaterial switchSmoke;

    public SmokeFragment() {
        // Required empty public constructor
    }

    public interface SmokeFragmentListener {
        void onInputSensorSmoke(String sensorSmoke);
    }

    public static SmokeFragment newInstance(String param1, String param2) {
        SmokeFragment fragment = new SmokeFragment();
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
        return inflater.inflate(R.layout.fragment_smoke, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        sliderSmoke = (Slider) getView().findViewById(R.id.sliderSmoke);
        sliderSmoke.addOnSliderTouchListener(touchListener);
        switchSmoke = (SwitchMaterial) getView().findViewById(R.id.switchSmoke);
        switchSmoke.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    float sensor1 = sliderSmoke.getValue();
                    String sensorSmoke = String.valueOf(sensor1);
                    listener.onInputSensorSmoke(sensorSmoke);
                } else {
                    String sensorSmoke = null;
                    listener.onInputSensorSmoke(sensorSmoke);
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
            if (switchSmoke.isChecked()) {
                float sensor1 = slider.getValue();
                String sensorSmoke = String.valueOf(sensor1);
                listener.onInputSensorSmoke(sensorSmoke);
            } else {
                String sensorSmoke = null;
                listener.onInputSensorSmoke(sensorSmoke);
            }
        }
    };

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof SmokeFragmentListener) {
            listener = (SmokeFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement SmokeFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

}