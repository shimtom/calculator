package com.example.shimtom.calculator;

import android.app.Activity;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends Activity implements View.OnClickListener {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 表示用TextView
        mTextView = (TextView) findViewById(R.id.display);

        // Button
        mButton = new Button[mId.length];


        // Buttonの取り込みとイベントのはりつけ
        for (int i = 0; i < mId.length; i++) {
            // buttonを取り込む
            mButton[i] = (Button) findViewById(mId[i]);
            // buttonのイベント処理
            mButton[i].setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        // 押されたボタンがどのボタンかを判定
        for (int i = 0; i < mId.length; i++) {
            if (view.equals(mButton[i])) {
                inputKey = i;

                switch (i) {
                    case KEY_CLEAR:
                        input = InputCalculator.CLEAR;
                        break;
                    case KEY_POINT:
                        input = InputCalculator.POINT;
                        break;
                    case KEY_EQUAL:
                        input = InputCalculator.EQUAL;
                        break;
                    case KEY_PLUS:
                    case KEY_MINUS:
                    case KEY_MULTIPLY:
                    case KEY_DIVIDE:
                        input = InputCalculator.OPERATOR;
                        break;
                    default:
                        input = InputCalculator.NUMBER;
                }
                switch (state) {
                    case EMPTY:
                        Empty();
                        break;
                    case ONE_NUM:
                        One_Num();
                        break;
                    case ONE_OPE:
                        One_Ope();
                        break;
                    case TWO_NUM:
                        Two_Num();
                        break;
                    case ANSWER:
                        Answer();
                        break;
                    case TEMP_NUM:
                        Temp_Num();
                        break;
                }
                break;
            }
        }
    }

    void Empty() {
        switch (input) {
            case CLEAR:
                InputClear();
                state = CalculatorState.EMPTY;
                break;
            case NUMBER:
                A = inputKey;
                OutPut(A);
                state = CalculatorState.ONE_NUM;
                break;
            case OPERATOR:
                SwitchOperator();
                state = CalculatorState.ONE_OPE;
                break;
            case EQUAL:
                state = CalculatorState.ANSWER;
                break;
            case POINT:
                A = 0.0;
                mTextView.setText("0.");
                state = CalculatorState.ONE_NUM;
                break;
        }

    }

    void One_Num() {
        String nowValue;
        switch (input) {
            case CLEAR:
                InputClear();
                state = CalculatorState.EMPTY;
                break;
            case NUMBER:
                nowValue = mTextView.getText().toString();
                if (nowValue.equals("0")) {
                    A = inputKey;
                } else {
                    nowValue = nowValue + inputKey;
                    A = Double.parseDouble(nowValue);
                }

                if(nowValue.contains(".")) {
                    mTextView.setText(String.valueOf(A));
                }else{
                    mTextView.setText(String.valueOf((int)A));
                }
                state = CalculatorState.ONE_NUM;
                break;
            case OPERATOR:
                SwitchOperator();
                state = CalculatorState.ONE_OPE;
                break;
            case EQUAL:
                state = CalculatorState.ANSWER;
                break;
            case POINT:
                nowValue = mTextView.getText().toString();
                if (!nowValue.contains(".")) {
                    nowValue = nowValue + ".";
                    mTextView.setText(nowValue);
                }
                state = CalculatorState.ONE_NUM;
                break;
        }
    }

    void One_Ope() {
        switch (input) {
            case CLEAR:
                InputClear();
                state = CalculatorState.EMPTY;
                break;
            case NUMBER:
                B = inputKey;
                OutPut(B);
                state = CalculatorState.TWO_NUM;
                break;
            case OPERATOR:
                SwitchOperator();
                state = CalculatorState.ONE_OPE;
                break;
            case EQUAL:
                if (operator.equals(Operator.DIVIDE) && A == 0) {
                    error = true;
                } else {
                    A = operator.apply(A, A);
                    OutPut(A);
                }
                state = CalculatorState.ANSWER;
                break;
            case POINT:
                B = 0.0;
                mTextView.setText("0.");
                state = CalculatorState.TWO_NUM;
                break;
        }
    }

    void Two_Num() {
        String nowValue;
        switch (input) {
            case CLEAR:
                InputClear();
                state = CalculatorState.EMPTY;
                break;
            case NUMBER:
                nowValue = mTextView.getText().toString();
                nowValue = nowValue + inputKey;
                B = Double.parseDouble(nowValue);

                if(nowValue.contains(".")) {
                    mTextView.setText(String.valueOf(B));
                }else{
                    mTextView.setText(String.valueOf((int)B));
                }
                state = CalculatorState.TWO_NUM;
                break;
            case OPERATOR:
                if (operator.equals(Operator.DIVIDE) && B == 0) {
                    error = true;
                } else {
                    A = operator.apply(A, B);
                    OutPut(A);
                    SwitchOperator();
                }
                state = CalculatorState.ONE_OPE;
                break;
            case EQUAL:
                if (operator.equals(Operator.DIVIDE) && B == 0) {
                    error = true;
                } else {
                    A = operator.apply(A, B);
                    OutPut(A);
                }
                state = CalculatorState.ANSWER;
                break;
            case POINT:
                nowValue=mTextView.getText().toString();
                if(!nowValue.contains(".")){
                    nowValue=nowValue+".";
                    mTextView.setText(nowValue);
                }
                state = CalculatorState.TWO_NUM;
                break;
        }
    }

    void Answer() {
        switch (input) {
            case CLEAR:
                InputClear();
                state = CalculatorState.EMPTY;
                break;
            case NUMBER:
                A = inputKey;
                OutPut(A);
                state = CalculatorState.TEMP_NUM;
                break;
            case OPERATOR:
                SwitchOperator();
                state = CalculatorState.ONE_OPE;
                break;
            case EQUAL:
                if (operator.equals(Operator.DIVIDE) && A == 0) {
                    error = true;
                } else {
                    A = operator.apply(A, A);
                    OutPut(A);
                }
                state = CalculatorState.ANSWER;
                break;
            case POINT:
                A = 0.0;
                mTextView.setText("0.");
                state = CalculatorState.TEMP_NUM;
                break;
        }
    }

    void Temp_Num() {
        String nowValue;
        switch (input) {
            case CLEAR:
                InputClear();
                state = CalculatorState.EMPTY;
                break;
            case NUMBER:
                nowValue = mTextView.getText().toString();
                if (nowValue.equals("0")) {
                    A = inputKey;
                } else {
                    nowValue = nowValue + inputKey;
                    A = Double.parseDouble(nowValue);
                }

                OutPut(A);
                state = CalculatorState.TEMP_NUM;
                break;
            case OPERATOR:
                SwitchOperator();
                state = CalculatorState.ONE_OPE;
                break;
            case EQUAL:
                if (operator.equals(Operator.DIVIDE) && B == 0) {
                    error = true;
                } else {
                    A = operator.apply(A, B);
                    OutPut(A);
                }
                state = CalculatorState.ANSWER;
                break;
            case POINT:
                nowValue = mTextView.getText().toString();
                if (!nowValue.contains(".")) {
                    nowValue = nowValue + ".";
                    mTextView.setText(nowValue);
                }
                state = CalculatorState.TEMP_NUM;
                break;
        }
    }

    private void SwitchOperator(){
        switch (inputKey) {
            case KEY_PLUS:
                operator = Operator.PLUS;
                break;
            case KEY_MINUS:
                operator = Operator.MINUS;
                break;
            case KEY_MULTIPLY:
                operator = Operator.MULTIPLY;
                break;
            case KEY_DIVIDE:
                operator = Operator.DIVIDE;
                break;
        }
    }

    private void InputClear() {
        A = 0;
        B = 0;
        operator = Operator.OTHER;
        inputKey = KEY_0;
        state = CalculatorState.EMPTY;
        mTextView.setText("0");
    }

    private void OutPut(double n){

        if((n - Math.floor(n))==0) {
            mTextView.setText(String.valueOf((int)n));
        }else{
            mTextView.setText(String.valueOf(n));
        }
    }

    TextView mTextView;
    Button mButton[];

    int inputKey = 0;
    double A = 0;
    double B = 0;
    boolean error = false;
    CalculatorState state = CalculatorState.EMPTY;
    InputCalculator input = InputCalculator.CLEAR;
    Operator operator = Operator.OTHER;


    private final int KEY_0 = 0;
    private final int KEY_1 = 1;
    private final int KEY_2 = 2;
    private final int KEY_3 = 3;
    private final int KEY_4 = 4;
    private final int KEY_5 = 5;
    private final int KEY_6 = 6;
    private final int KEY_7 = 7;
    private final int KEY_8 = 8;
    private final int KEY_9 = 9;
    private final int KEY_POINT = 10;
    private final int KEY_PLUS = 11;
    private final int KEY_MINUS = 12;
    private final int KEY_MULTIPLY = 13;
    private final int KEY_DIVIDE = 14;
    private final int KEY_EQUAL = 15;
    private final int KEY_CLEAR = 16;
    private final int KEY_NUM = 17;

    private int mId[] = {
            R.id.button0, R.id.button1, R.id.button2, R.id.button3,
            R.id.button4, R.id.button5, R.id.button6,
            R.id.button7, R.id.button8, R.id.button9,
            R.id.buttonPoint,
            R.id.buttonPlus, R.id.buttonMinus, R.id.buttonMultiply, R.id.buttonDivide,
            R.id.buttonEqual, R.id.buttonClear};


    enum CalculatorState {EMPTY, ONE_NUM, ONE_OPE, TWO_NUM, ANSWER, TEMP_NUM}

    enum InputCalculator {NUMBER, OPERATOR, EQUAL, POINT, CLEAR}

    enum Operator {
        PLUS {
            double apply(double x, double y) {
                return x + y;
            }
        },
        MINUS {
            double apply(double x, double y) {
                return x - y;
            }
        },
        MULTIPLY {
            double apply(double x, double y) {
                return x * y;
            }
        },
        DIVIDE {
            double apply(double x, double y) {
                return x / y;
            }
        },
        OTHER{
            double apply(double x, double y){
                return x;
            }
        };

        abstract double apply(double x, double y);
    }
}
