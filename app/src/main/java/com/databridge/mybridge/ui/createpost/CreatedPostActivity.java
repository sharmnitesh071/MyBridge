package com.databridge.mybridge.ui.createpost;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.databridge.mybridge.R;

public class CreatedPostActivity extends AppCompatActivity implements View.OnClickListener {

    private CreatepostbottomFragment createpostbottomFragment;
    AppCompatTextView tvChooseAudien, tvChooseWhoReply;
    private EveryoneFragment everyone;
    private EveryonereplyFragment everyonefragement;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_created_post);
        tvChooseAudien = findViewById(R.id.tvChooseAudien);
        tvChooseWhoReply = findViewById(R.id.tvChooseWhoReply);
        tvChooseAudien.setOnClickListener(this);
        tvChooseWhoReply.setOnClickListener(this);

        createpostbottomFragment = new CreatepostbottomFragment();
        Bundle args = new Bundle();
        createpostbottomFragment.setArguments(args);
        createpostbottomFragment.show(getSupportFragmentManager(), createpostbottomFragment.getTag());

      /*  createpostbottomFragment = new CreatepostbottomFragment();
        Bundle args = new Bundle();
        createpostbottomFragment.setArguments(args);
        createpostbottomFragment.show(getSupportFragmentManager(), createpostbottomFragment.getTag());
*/

    }

    @Override
    public void onClick(View v) {
        if (v == tvChooseAudien) {
            everyone = new EveryoneFragment();
            Bundle args = new Bundle();
            everyone.setArguments(args);
            everyone.show(getSupportFragmentManager(), everyone.getTag());
        }
        if (v == tvChooseWhoReply) {
            everyonefragement = new EveryonereplyFragment();
            Bundle args = new Bundle();
            everyonefragement.setArguments(args);
            everyonefragement.show(getSupportFragmentManager(), everyonefragement.getTag());
        }
    }
}