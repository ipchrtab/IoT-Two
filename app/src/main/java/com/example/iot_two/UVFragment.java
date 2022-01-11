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

public class UVFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private UVFragmentListener listener;
    Slider sliderUV;
    SwitchMaterial switchUV;

    public UVFragment() {
        // Required empty public constructor
    }

    public interface UVFragmentListener {
        void onInputSensorUV(String sensorUV);
    }

    public static UVFragment newInstance(String param1, String param2) {
        UVFragment fragment = new UVFragment();
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
        return inflater.inflate(R.layout.fragment_uv, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        sliderUV = (Slider) getView().findViewById(R.id.sliderUV);
        sliderUV.addOnSliderTouchListener(touchListener);
        switchUV = (SwitchMaterial) getView().findViewById(R.id.switchUV);
        switchUV.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    float sensor4 = sliderUV.getValue();
                    String sensorUV = String.valueOf(sensor4);
                    listener.onInputSensorUV(sensorUV);
                } else {
                    String sensorUV = null;
                    listener.onInputSensorUV(sensorUV);
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
            if (switchUV.isChecked()) {
                float sensor4 = slider.getValue();
                String sensorUV = String.valueOf(sensor4);
                listener.onInputSensorUV(sensorUV);
            } else {
                String sensorUV = null;
                listener.onInputSensorUV(sensorUV);
            }
        }
    };

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof UVFragmentListener) {
            listener = (UVFragmentListener) context;
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