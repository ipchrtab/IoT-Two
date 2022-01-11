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

public class GasFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private GasFragmentListener listener;
    Slider sliderGas;
    SwitchMaterial switchGas;

    public GasFragment() {
        // Required empty public constructor
    }

    public interface GasFragmentListener {
        void onInputSensorGas(String sensorGas);
    }

    public static GasFragment newInstance(String param1, String param2) {
        GasFragment fragment = new GasFragment();
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
        return inflater.inflate(R.layout.fragment_gas, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        sliderGas = (Slider) getView().findViewById(R.id.sliderGas);
        sliderGas.addOnSliderTouchListener(touchListener);
        switchGas = (SwitchMaterial) getView().findViewById(R.id.switchGas);
        switchGas.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    float sensor2 = sliderGas.getValue();
                    String sensorGas = String.valueOf(sensor2);
                    listener.onInputSensorGas(sensorGas);
                } else {
                    String sensorGas = null;
                    listener.onInputSensorGas(sensorGas);
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
            if (switchGas.isChecked()) {
                float sensor2 = slider.getValue();
                String sensorGas = String.valueOf(sensor2);
                listener.onInputSensorGas(sensorGas);
            } else {
                String sensorGas = null;
                listener.onInputSensorGas(sensorGas);
            }
        }
    };

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof GasFragmentListener) {
            listener = (GasFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement GasFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

}