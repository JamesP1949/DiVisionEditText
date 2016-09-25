package com.jamesp1949.divisionlib;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.util.AttributeSet;

/**
 * Created by JamesP949 on 2016/9/25.
 * 分割型输入框 占位符分隔 只限单字符分隔符
 */
public class DivisionEditText extends AppCompatEditText {
    //输入占位符前EditText中的文本
    private String inputAfterText;
    //是否重置了EditText的内容
    private boolean resetText;
    /**
     * 记录上一次输入框中的字符数
     */
    private int lastTextLen;
    /**
     * 默认占位分隔符
     */
    private String placeHolder;
    /**
     * 分割位置
     */
    private int placeIndex;

    public DivisionEditText(Context context) {
        super(context);
        init();
    }

    public DivisionEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = null;
        try {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.DivisionEditText);
            placeHolder = typedArray.getString(R.styleable.DivisionEditText_placeHolder);
            // 限单字符 为防误输 只取第一个字符
            placeHolder = String.valueOf(placeHolder.charAt(0));
            placeIndex = typedArray.getInteger(R.styleable.DivisionEditText_placeIndex, 0);
        } catch (NullPointerException e) {
            // 当用户不指定分隔符时 默认空字符
            placeHolder = "";
        } finally {
            typedArray.recycle();
        }


        init();
    }

    public DivisionEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                if (!resetText) {
                    //这里用s.toString()而不直接用s是因为如果用s，
                    // 那么，inputAfterText和s在内存中指向的是同一个地址，s改变了，
                    // inputAfterText也就改变了，那么占位符就失败了
                    inputAfterText = s.toString();
                }

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int
                    before, int count) {
                inputAfterText = s.toString();
                if (!resetText) { // 输入内容未被重置
                    /**
                     * 上一次的字符数小于当前输入框中的字符数
                     */
                    if (lastTextLen < inputAfterText.length()) {
                        /*
                        * 用户输入内容未做删减时的处理逻辑
                        * */
                        if (inputAfterText.length() == placeIndex
                                || inputAfterText.length() % (placeIndex + 1) == placeIndex) {
                            // 输入内容满足分割条件时 加入分隔符
                            inputAfterText += placeHolder;
                            resetText = true;
                            setText(inputAfterText);
                            invalidate();
                        } else if (inputAfterText.length() > 0 && inputAfterText.length() %
                                (placeIndex + 1) == 0) {
                            /*
                            *
                            * 用户输入内容删减时的处理 当输入内容再次满足分割条件时
                            * 将分隔符插入输入内容的倒数第二位并重置输入内容
                            * */
                            StringBuffer stringBuffer = new StringBuffer(inputAfterText);
                            stringBuffer.insert(stringBuffer.length() - 1, placeHolder);
                            inputAfterText = stringBuffer.toString();
                            resetText = true;
                            setText(inputAfterText);
                            invalidate();
                        }
                    }

                    CharSequence text = getText();
                    lastTextLen = text.length();

                    if (text instanceof Spannable) {
                        Spannable spanText = (Spannable) text;
                        Selection.setSelection(spanText, text.length());
                    }
                } else { // 输入内容重置
                    resetText = false;
//                    inputAfterText = s.toString();
                    lastTextLen = inputAfterText.length();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
