package com.system.cutline.Dialogs;

import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.system.cutline.Core.BaseActivity;
import com.system.cutline.R;
import com.system.cutline.Services.WriteHistoryRunnable;
import com.system.cutline.Utils.KeyboardUtil;
import com.system.cutline.Utils.PrefsConstants;
import com.system.cutline.Utils.PrefsUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class QuestionsDialog extends BaseActivity
        implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    private EditText mText;
    private RadioGroup textType, questionType;
    private RadioButton question1, question2, question3, question4, question5, other;

    private String[] englishQuestions = {"What is ", "Why ", "Where ", "When ", "Explain "};
    private String[] arabicQuestions = {"ماذا ", "لماذا ", "أين ", "متى ", "اشرح "};

    private String txtCopied = "";

    private ExecutorService mThreadPool = Executors.newSingleThreadExecutor();
    private char questionMark = '?';

    private TextView edit, done;
    private Button cancel, ok;

    @Override
    protected int initLayout() {
        return R.layout.questions_dialog;
    }

    @Override
    protected String getSharedPreferencesName() {
        return PrefsUtil.QUESTIONS_DIALOG_PREFS_NAME;
    }

    @Override
    protected void initPrefs() {

    }

    @Override
    protected void initViews() {
        txtCopied = getIntent().getStringExtra("txt");
        mText = findViewById(R.id.textValue);
        textType = findViewById(R.id.question_or_answer);
        questionType = findViewById(R.id.questions_group);
        question1 = findViewById(R.id.question_1);
        question2 = findViewById(R.id.question_2);
        question3 = findViewById(R.id.question_3);
        question4 = findViewById(R.id.question_4);
        question5 = findViewById(R.id.question_5);
        other = findViewById(R.id.question_other);

        edit = findViewById(R.id.edit);
        done = findViewById(R.id.done);
        cancel = findViewById(R.id.cancel_btn);
        ok = findViewById(R.id.ok_btn);
    }

    @Override
    protected void onViewCreated() {
        mText.setText(txtCopied);
        edit.setOnClickListener(this);
        done.setOnClickListener(this);
        ok.setOnClickListener(this);
        cancel.setOnClickListener(this);
        if (PrefsUtil.getString(PrefsConstants.LANGUAGE).equals(PrefsConstants.ENGLISH)) {
            question1.setText(englishQuestions[0]);
            question2.setText(englishQuestions[1]);
            question3.setText(englishQuestions[2]);
            question4.setText(englishQuestions[3]);
            question5.setText(englishQuestions[4]);
            questionMark = '?';
        } else if (PrefsUtil.getString(PrefsConstants.LANGUAGE).equals(PrefsConstants.ARABIC)) {
            question1.setText(arabicQuestions[0]);
            question2.setText(arabicQuestions[1]);
            question3.setText(arabicQuestions[2]);
            question4.setText(arabicQuestions[3]);
            question5.setText(arabicQuestions[4]);
            questionMark = '؟';
        }

        textType.setOnCheckedChangeListener(this);
        mText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    onDoneClicked(v);
                    return true;
                }
                return false;
            }
        });
        questionType.setOnCheckedChangeListener(this);
        /*mText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                PrefsUtil.saveString(PrefsUtil.QUESTIONS_DIALOG_PREFS_NAME, OrientationConstants.INPUT_TEXT, s.toString());
            }
        });*/
    }

    private void onEditClicked(View view) {
        mText.setEnabled(true);
        mText.requestFocus();
        KeyboardUtil.showSoftKeyboard(mText);
        findViewById(R.id.done).setVisibility(View.VISIBLE);
        findViewById(R.id.edit).setVisibility(View.GONE);
    }

    private void onDoneClicked(View view) {
        KeyboardUtil.hideSoftKeyboard(QuestionsDialog.this);
        mText.setEnabled(false);
        findViewById(R.id.done).setVisibility(View.GONE);
        findViewById(R.id.edit).setVisibility(View.VISIBLE);
    }

    private void onCancelClicked(View view) {
        PrefsUtil.clear(PrefsUtil.QUESTIONS_DIALOG_PREFS_NAME);
        QuestionsDialog.this.finish();
    }

    private void onOKClicked(View view) {
        boolean isQuestion = textType.getCheckedRadioButtonId() == R.id.question;
        WriteHistoryRunnable runnable = new WriteHistoryRunnable(mText.getText(), isQuestion);
        runnable.setOnCompleteWritingListener(new WriteHistoryRunnable.OnCompleteWritingListener() {
            @Override
            public void onComplete() {
                PrefsUtil.clear(PrefsUtil.QUESTIONS_DIALOG_PREFS_NAME);
                QuestionsDialog.this.finish();
            }
        });
        mThreadPool.execute(runnable);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (group.equals(textType)) {
            switch (checkedId) {
                case R.id.question:
                    findViewById(R.id.questions_Layout).setVisibility(View.VISIBLE);
                    break;
                case R.id.answer:
                    findViewById(R.id.questions_Layout).setVisibility(View.GONE);
                    break;
            }
            // PrefsUtil.saveInt(PrefsUtil.QUESTIONS_DIALOG_PREFS_NAME, OrientationConstants.TYPE_CHECKED, checkedId);
        } else if (group.equals(questionType)) {
            if (checkedId == R.id.question_other)
                mText.setText(txtCopied);
            else {
                RadioButton button = findViewById(checkedId);
                mText.setText(button.getText().toString() + txtCopied + " " + questionMark);
            }
            // PrefsUtil.saveInt(PrefsUtil.QUESTIONS_DIALOG_PREFS_NAME, OrientationConstants.QUESTION_CHECKED, checkedId);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.equals(edit))
            onEditClicked(v);
        else if (v.equals(done))
            onDoneClicked(v);
        else if (v.equals(ok))
            onOKClicked(v);
        else if (v.equals(cancel))
            onCancelClicked(v);
    }
}
