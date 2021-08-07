package com.swiftdating.app.ui.questionListScreen;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Arrays;

import com.swiftdating.app.R;
import com.swiftdating.app.callbacks.OnItemClickListener;
import com.swiftdating.app.model.FlexModel;
import com.swiftdating.app.ui.answerNowScreen.AnswerNowActivity;
import com.swiftdating.app.ui.base.BaseActivity;
import com.swiftdating.app.ui.editProfileScreen.adapter.QuestionListAdapter;

public class QuestionListActivity extends BaseActivity implements View.OnClickListener, OnItemClickListener {
    int pos = -1;
    ArrayList<FlexModel> QuestionList = new ArrayList<>();
    RecyclerView rvQuestions;
    QuestionListAdapter adapter;
    ImageView ivBack;
    String questionNum = "",selectedQues = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_list);
        getQuestionList();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }


    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((mMessageReceiver),
                new IntentFilter("MyData"));
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

    /**
     *  Method to Set Question List
     */
    private void getQuestionList() {
        if(getIntent().getExtras().containsKey("isQuestion1")) {
            questionNum = getIntent().getExtras().getString("isQuestion1");
            selectedQues = getIntent().getExtras().getString("ques");
        }else{
            onBackPressed();
            showToast("Something Went Wrong");
        }
        ivBack = findViewById(R.id.ivback);
        rvQuestions = findViewById(R.id.rvQuestions);
        ArrayList<String> list = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.questionArray)));
        for (int i = 0; i < list.size(); i++) {
            QuestionList.add(new FlexModel(list.get(i)));
            if (!TextUtils.isEmpty(selectedQues) && selectedQues.equalsIgnoreCase(list.get(i))) {
                QuestionList.get(i).setChecked(true);
                pos = i;
            }
        }

        adapter = new QuestionListAdapter(QuestionListActivity.this, QuestionList);
        adapter.setOnItemClickListener(this);
        rvQuestions.setAdapter(adapter);
        ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == ivBack) {
            onBackPressed();
        }
    }

    @Override
    public void OnItemClick(int position) {
        if (pos != -1) {
            QuestionList.get(pos).setChecked(false);
        }
        pos = position;
        QuestionList.get(position).setChecked(true);
        adapter.notifyDataSetChanged();
        Intent intent = new Intent(this, AnswerNowActivity.class);
        intent.putExtra("Question", QuestionList.get(position).getName());
        intent.putExtra("isQuestion1", questionNum);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }
}
