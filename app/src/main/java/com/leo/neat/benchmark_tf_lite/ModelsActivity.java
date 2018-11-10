package com.leo.neat.benchmark_tf_lite;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ModelsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_models);

        //TODO convert to arrays of radio buttons
        // TODO make a class for each model display
        final RadioButton model1Radio = findViewById(R.id.model_1_rbtn);
        final RadioButton model2Radio = findViewById(R.id.model_2_rbtn);
        TextView model1Pref = findViewById(R.id.model_1_performance_txt);
        TextView model2Pref = findViewById(R.id.model_2_performance_txt);
        final SharedPreferences sharedPref = getApplicationContext()
                .getSharedPreferences("com.leo.neat.benchmark_tf_lite", Context.MODE_PRIVATE);

        String model1_pref = sharedPref.getString(TFLiteModelValues.STANDARD_MODEL_FILENAME,"0.0");
        String toSet = "Performance: " + model1_pref + " ips";
        model1Pref.setText(toSet);
        String model2_pref = sharedPref.getString(TFLiteModelValues.ANDO_MODEL_FILENAME,"0.0");
        toSet = "Performance: " + model2_pref + " ips";
        model2Pref.setText(toSet);

        int currentModel = sharedPref.getInt(TFLiteModelValues.CURRENT_MODEL,0);
        if(currentModel == 0)
        {
            model1Radio.setChecked(true);
            model2Radio.setChecked(false);
        }else if(currentModel == 1)
        {
            model2Radio.setChecked(true);
            model1Radio.setChecked(false);
        }

        model1Radio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    model2Radio.setChecked(false);
                    sharedPref.edit().putInt(TFLiteModelValues.CURRENT_MODEL, 0).apply();
                    finish();
                }
            }
        });

        model2Radio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    model1Radio.setChecked(false);
                    sharedPref.edit().putInt(TFLiteModelValues.CURRENT_MODEL, 1).apply();
                    finish();
                }
            }
        });

    }
}
