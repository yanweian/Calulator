package com.yan.newcalulator;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.BounceInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * Created by Yan on 2016/9/11.
 */
public class MyCalculator extends Activity implements View.OnClickListener {
    private Button bt_0;
    private Button bt_1;
    private Button bt_2;
    private Button bt_3;
    private Button bt_4;
    private Button bt_5;
    private Button bt_6;
    private Button bt_7;
    private Button bt_8;
    private Button bt_9;
    private Button bt_minus;
    private Button bt_plus;
    private Button bt_divide;
    private Button bt_multiple;
    private Button bt_point;
    private Button bt_equal;
    private Button bt_del;
    private Button bt_clear;
    private Button bt_change;

    private EditText et;
    private EditText et_result;
    private boolean isonclick = false;
    private boolean isfirstdraw = true;
    private MediaPlayer mediaPlayer;
    private SoundPool soundPool;
    private int soundid;
    private ToggleButton mToggleButton;
    private boolean ismusic = true;
    private List<RelativeLayout> mRelativeLayouts;
    private int[] ids = {R.id.music_layout, R.id.history_layout, R.id.binary_layout};
    private float menu_x = 0;
    private boolean ismenu = false;

    private FrameLayout mFrameLayout_bb;
    private TextView mTextView_bb, mTextView_tensix, mTextView_eight, mTextView_two;
    private TextView mTextView_bin, mTextView_oct, mTextView_hex;
    private AlertDialog mAlertDialog_history;
    private AlertDialog.Builder mBuilder;
    private SQLiteDatabase mSQLiteDatabase;
    private DBHelper mDBHelper;
    private String dbname = "history.db";
    private List<History> mHistories;
    private ListView mListView;
    private MyAdapter mMyAdapter;
    private List<Button> buttons;
    private RelativeLayout mRelativeLayout_bb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainlayout);
//        初始化控件
        mTextView_bin = (TextView) findViewById(R.id.binvalue);
        mTextView_hex = (TextView) findViewById(R.id.hexvalue);
        mTextView_oct = (TextView) findViewById(R.id.octvalue);
        mFrameLayout_bb = (FrameLayout) findViewById(R.id.framebb);
        mTextView_eight = (TextView) findViewById(R.id.text_eight);
        mTextView_two = (TextView) findViewById(R.id.text_two);
        mTextView_tensix = (TextView) findViewById(R.id.text_tensix);
        mTextView_bb = (TextView) findViewById(R.id.textbb);
        mRelativeLayout_bb = (RelativeLayout) findViewById(R.id.relative);
        bt_0 = (Button) findViewById(R.id.bt_0);
        bt_1 = (Button) findViewById(R.id.bt_1);
        bt_2 = (Button) findViewById(R.id.bt_2);
        bt_3 = (Button) findViewById(R.id.bt_3);
        bt_4 = (Button) findViewById(R.id.bt_4);
        bt_5 = (Button) findViewById(R.id.bt_5);
        bt_6 = (Button) findViewById(R.id.bt_6);
        bt_7 = (Button) findViewById(R.id.bt_7);
        bt_8 = (Button) findViewById(R.id.bt_8);
        bt_9 = (Button) findViewById(R.id.bt_9);
        bt_plus = (Button) findViewById(R.id.bt_plus);
        bt_minus = (Button) findViewById(R.id.bt_minus);
        bt_multiple = (Button) findViewById(R.id.bt_mutiple);
        bt_divide = (Button) findViewById(R.id.bt_divide);
        bt_equal = (Button) findViewById(R.id.bt_equal);
        bt_point = (Button) findViewById(R.id.bt_point);
        bt_del = (Button) findViewById(R.id.bt_del);
        bt_clear = (Button) findViewById(R.id.bt_clear);
        bt_change = (Button) findViewById(R.id.bt_change);
        et = (EditText) findViewById(R.id.et);
        et_result = (EditText) findViewById(R.id.et_result);
        mDBHelper = new DBHelper(this, dbname);
        mSQLiteDatabase = mDBHelper.getWritableDatabase();
        mRelativeLayouts = new ArrayList<>();
        mHistories = new ArrayList<>();
        for (int i = 0; i < ids.length; i++) {
            RelativeLayout relativeLayout = (RelativeLayout) findViewById(ids[i]);
            mRelativeLayouts.add(relativeLayout);
        }
        soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
        soundid = soundPool.load(this, R.raw.b, 1);
        mToggleButton = (ToggleButton) findViewById(R.id.toggleButton);
        mToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ismusic = isChecked;
                if (ismusic) {
                    soundPool.play(soundid, 1, 1, 0, 0, 1);
                }
            }
        });
        et.setSelection(et.getText().length());
//        添加监视器
        bt_0.setOnClickListener(this);
        bt_1.setOnClickListener(this);
        bt_2.setOnClickListener(this);
        bt_3.setOnClickListener(this);
        bt_4.setOnClickListener(this);
        bt_5.setOnClickListener(this);
        bt_6.setOnClickListener(this);
        bt_7.setOnClickListener(this);
        bt_8.setOnClickListener(this);
        bt_9.setOnClickListener(this);
        bt_plus.setOnClickListener(this);
        bt_multiple.setOnClickListener(this);
        bt_minus.setOnClickListener(this);
        bt_equal.setOnClickListener(this);
        bt_point.setOnClickListener(this);
        bt_divide.setOnClickListener(this);
        bt_del.setOnClickListener(this);
        bt_clear.setOnClickListener(this);
        bt_change.setOnClickListener(this);
        buttons = new ArrayList<>();
        buttons.add(bt_minus);
        buttons.add(bt_point);
        buttons.add(bt_multiple);
        buttons.add(bt_plus);
        buttons.add(bt_equal);
        buttons.add(bt_divide);
        buttons.add(bt_change);
//        mediaPlayer = MediaPlayer.create(this, R.raw.a);
        //获取组件宽高，并将其固定
        ViewTreeObserver vto = et.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                int height = et.getMeasuredHeight();
                int width = et.getMeasuredWidth();
                int fbbh = mFrameLayout_bb.getMeasuredHeight();
                int fbbw = mFrameLayout_bb.getMeasuredWidth();
                menu_x = mRelativeLayouts.get(0).getMeasuredWidth();
                int bbh = mRelativeLayout_bb.getMeasuredHeight();
                if (bbh > 0 && height > 0 && width > 0 && isfirstdraw) {
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(fbbw, fbbh);
                    mFrameLayout_bb.setLayoutParams(layoutParams);
                    isfirstdraw = false;
                    et.setHeight(height);
                }
                Log.i("shape", "height:" + height + "  width:" + width + "  bbh:" + bbh + "  fbbh:" + fbbh);
                return true;
            }

        });
        initdialog();
    }

    private void initdialog() {
        mMyAdapter = new MyAdapter(this, mHistories);
        mBuilder = new AlertDialog.Builder(this);
        View v = LayoutInflater.from(this).inflate(R.layout.historylayout, null);
        mListView = (ListView) v.findViewById(R.id.history_listview);
        mListView.setAdapter(mMyAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                History history = mHistories.get(position);
                Resources resource = (Resources) getBaseContext().getResources();
                ColorStateList csl = (ColorStateList) resource.getColorStateList(R.color.coloreditor);
                et.setTextColor(csl);
                et_result.setAlpha(1.0f);
                et.setText(history.getQues());
                et_result.setHint(history.getResult());
                String et_string = et.getText().toString();
                if (et_string.length() > 12) {
                    et.setTextSize(30);
                } else if (et_string.length() > 8) {
                    et.setTextSize(40);
                } else {
                    et.setTextSize(50);
                }
                mAlertDialog_history.dismiss();

            }

        });
        mBuilder.setView(v);
        mBuilder.setTitle("历史记录");
        mAlertDialog_history = mBuilder.create();
        initdata();
    }

    private void initdata() {
        //异步连接数据库加载数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                Cursor cursor = mSQLiteDatabase.rawQuery("select ques,result from historytb", null);
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        History history = new History(cursor.getString(cursor.getColumnIndex("ques")),
                                cursor.getString(cursor.getColumnIndex("result")));
                        mHistories.add(history);
                    }
                }
            }
        }).start();
    }

//    private int allnum = 0;
//    private int pointnum = -1;

    @Override
    public void onClick(View v) {
        if (ismusic) {
            soundPool.play(soundid, 1, 1, 0, 0, 1);
        }
        Resources resource = (Resources) getBaseContext().getResources();
        ColorStateList csl = (ColorStateList) resource.getColorStateList(R.color.coloreditor);
        et.setTextColor(csl);
        et_result.setAlpha(1.0f);
        String et_string = et.getText().toString();
        if (et_string.length() > 12) {
            et.setTextSize(30);
        } else if (et_string.length() > 8) {
            et.setTextSize(40);
        } else {
            et.setTextSize(50);
        }
        //获取最后一个数字
        String lastnum=et_string;
        if(et_string.contains(" ")){
            String[] stringsnum = et_string.split(" ");
            Log.i("num",stringsnum.length+"   "+stringsnum[stringsnum.length-1]);
            lastnum=stringsnum[stringsnum.length-1];
        }
        switch (v.getId()) {
            case R.id.bt_0:
            case R.id.bt_1:
            case R.id.bt_2:
            case R.id.bt_3:
            case R.id.bt_4:
            case R.id.bt_5:
            case R.id.bt_6:
            case R.id.bt_7:
            case R.id.bt_8:
            case R.id.bt_9:
                if (isonclick == true) {
                    isonclick = false;
                    et_string = "";
                    et_result.setHint("");
                    et.setTextSize(50);
                }
                int pointlength=0;
                int len=lastnum.length();
                if(lastnum.contains(".")){
                    pointlength=lastnum.substring(lastnum.indexOf(".")).length();
                    len--;
                }
                if (len<15&&pointlength<11) {
                    if(lastnum.equals("0")){
                        et_string=et_string.substring(0, et_string.length() - 1);
                    }
                    et.setText(et_string + ((Button) v).getText());
                    if (et_string.contains(" ")) {
                        getresult();
                    }
                    if (isbb) {
                        bbgetresult();
                    }
                } else if (len>= 15) {
                    Toast.makeText(this, "已超过最大位数（15）", Toast.LENGTH_SHORT).show();
                } else if (pointlength >= 11) {
                    Toast.makeText(this, "小数点后最大位数为10位", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.bt_change:
                if (isonclick == true) {
                    isonclick = false;
                    et_string = "";
                    et_result.setHint("");
                }
                if (et_string == null || et_string.equals("")) {
//                    字符串为空
                    et.setText("(-");
                } else if (!et_string.contains(" ")) {
//                    只有一个数字
                    if (et_string.substring(0, 1).equals("-")) {
//                        是负数但没括号
                        et.setText(et_string.substring(1));
                    } else if (!et_string.contains("(")) {
//                        为正数，切换为负数
                        et.setText("(-" + et_string);
                    } else if (et_string.contains("(")) {
//                        为负数，切换为正数
                        et.setText(et_string.substring(et_string.indexOf("-") + 1));
                    }
                } else if (et_string.contains(" ")) {
//                    第二个数字
                    if (et_string.substring(et_string.length() - 1).equals(" ")) {
//                        还未输入数字，且没有负号，加上负号
                        et.setText(et_string + "(-");
                    } else if (et_string.substring(et_string.length() - 1).equals("-")) {
//                        还未输入数字，且有负号，去掉负号
                        et.setText(et_string.substring(0, et_string.length() - 2));
                    } else {
//                        已有数字
//                        以空格分隔
                        String[] s = et_string.split(" ");
                        String pre = "";
                        for (int i = 0; i < s.length - 1; i++) {
                            pre += s[i] + " ";
                        }
                        if (s[s.length - 1].contains("(")) {
//                            为负号，将s[2]切换为正数
                            et.setText(pre + s[s.length - 1].substring(2));
                        } else {
//                            为正数
                            et.setText(pre + "(-" + s[s.length - 1]);
                        }
                        getresult();
                    }
                }
                break;
            case R.id.bt_point:
                if (et_string == null || et_string.length() == 0 || et_string.substring(et_string.length() - 1).equals(" ") || et_string.substring(et_string.length() - 1).equals("-")) {
                    et.setText(et_string + "0.");
                } else {
                    if (!lastnum.contains(".")) {
                        et.setText(et_string + ".");
                    }
                }
                break;
            case R.id.bt_mutiple:
            case R.id.bt_plus:
            case R.id.bt_divide:
            case R.id.bt_minus:
                isonclick = false;
                if (et_string == null || et_string.length() == 0) {
//                    什么都没有无法输入
                } else {
                    // 最后一位为负号或符号，换符号
                    if (et_string.substring(et_string.length() - 1).equals(" ") || et_string.substring(et_string.length() - 1).equals("-")) {
                        et.setText(et_string.substring(0, et_string.lastIndexOf(" ")-2) + " " + ((Button) v).getText() + et_string.substring(et_string.lastIndexOf(" ")));

                    } else {
                        if(lastnum.substring(lastnum.length()-1).equals(".")){
                            et_string=et_string.substring(0,et_string.length()-1);
                        }
                        if (lastnum.contains("(")&&!lastnum.contains(")")) {
                            et_string = et_string + ")";
                        }
                        et.setText(et_string + " " + ((Button) v).getText() + " ");
                    }
                }
                break;
            case R.id.bt_equal:
                String result = et_result.getHint().toString();
                if (result.equals("")) {
                    return;
                }
                isonclick = true;
                //加入到数据库中
                histroy();
                if (result.length() > 14) {
                    et.setTextSize(30);
                } else if (result.length() > 8) {
                    et.setTextSize(40);
                } else {
                    et.setTextSize(50);
                }
                et.setAlpha(0f);
                et.setTextColor(Color.parseColor("#aaa000"));
                ObjectAnimator.ofFloat(et_result, "Alpha", 1.0f, 0f).setDuration(200).start();
                et.setText(result);
                ObjectAnimator.ofFloat(et, "Alpha", 0f, 1.0f).setDuration(300).start();
                et_result.setHint("");
                break;
            case R.id.bt_clear:
                et.setText("");
                et_result.setHint("");
                if (isbb) {
                    bbgetresult();
                }
                break;
            case R.id.bt_del:
                if (et_string == null || et_string.length() == 0) {
//                    没有字符，无法删除
                } else if (et_string.substring(et_string.length() - 1).equals(" ")) {
//                    最后一位为操作符，删除3位
                    et.setText(et_string.substring(0, et_string.length() - 3));
                } else if (et_string.length() > 1 && et_string.substring(et_string.length() - 2).equals("(-")) {
                    //将负号加括号删除
                    et.setText(et_string.substring(0, et_string.length() - 2));
                } else {
//                    正常删除一位
                    et.setText(et_string.substring(0, et_string.length() - 1));
                    et_string=et.getText().toString();
                    if (et_string.contains(" ") && !et_string.substring(et_string.length()-1).equals(" ")&&!et_string.substring(et_string.length()-1).equals("-")) {
                            getresult();
                    }
                    if(!et_string.contains(" ")){
                        et_result.setHint("");
                    }
                    if (isbb) {
                        bbgetresult();
                    }
                }
                break;
        }
        et.setSelection(et.getText().length());
    }

    public void getresult() {
        String s = et.getText().toString();
        //多项式计算
        String result = multigetresult(s);
        //格式控制
        String sr = result + "";
        if (sr.equals("Infinity")) {
            Toast.makeText(this, "结果超过最大范围", Toast.LENGTH_SHORT).show();
            return;
        }
        if (sr.contains("E")) {
            int index = sr.indexOf("E");
            String msr = sr.substring(0, index);
            String end = sr.substring(index);
            if (msr.length() > 10) {
                msr = msr.substring(0, 10);
                sr = msr.concat(end);
            }
        } else if (sr.contains(".")) {
            int index = sr.indexOf(".");
            if (sr.substring(index).length() > 11) {
                String msr = sr.substring(index, index + 11);
                sr = sr.substring(0, index).concat(msr);
            } else if (index > 4 && sr.length() > 15) {
                sr = sr.substring(0, 15);
            }
        }
        et_result.setHint(sr);
    }/*小数点后只有10位
    * 总位数有15位*/


    public void onClick_extra(View v) {
        switch (v.getId()) {
            case R.id.bt_choose:
                //加载动画
                startAnimator();
                break;
            case R.id.history_layout:
                mAlertDialog_history.show();
                startAnimator();
                break;
            case R.id.binary_layout:
                closebt();
                startAnimator();
                break;
            default:
                break;
        }
    }

    private void startAnimator() {
        if (ismenu == false) {
            ismenu = true;
            Log.i("caculator: ", (100 + 100 * 2) + "   300?");
            for (int i = 0; i < mRelativeLayouts.size(); i++) {
                mRelativeLayouts.get(i).setVisibility(View.VISIBLE);
                mRelativeLayouts.get(i).setTranslationX((0 - menu_x));
                ValueAnimator viewAnim = ObjectAnimator.ofFloat(mRelativeLayouts.get(i), "TranslationX", (0 - menu_x), 0f);
                viewAnim.setDuration(400 - (i * 120));
                viewAnim.setStartDelay(i * 200);
                viewAnim.setInterpolator(new OvershootInterpolator());
                viewAnim.start();
            }
        } else {
            ismenu = false;
            for (int i = 0; i < mRelativeLayouts.size(); i++) {
                mRelativeLayouts.get(i).setVisibility(View.VISIBLE);
                ValueAnimator viewAnim = ObjectAnimator.ofFloat(mRelativeLayouts.get(i), "TranslationX", 0f, (0 - menu_x));
                viewAnim.setDuration(400 - (i * 120));
                viewAnim.setStartDelay(i * 200);
                viewAnim.setInterpolator(new BounceInterpolator());
                viewAnim.start();
            }
        }
        //Log.i("translationx", menu_x+"");
    }

    private void histroy() {
        //将历史记录保存到数据库中
        ContentValues contentValues = new ContentValues();
        String ques = et.getText().toString();
        String result = et_result.getHint().toString();
        History history = new History(ques, result);
        contentValues.put("ques", ques);
        contentValues.put("result", result);
        mSQLiteDatabase.insert("historytb", null, contentValues);
        mHistories.add(history);
    }

    private void ohistroy(int index) {
        History history = mHistories.get(index);
        String ques = history.getQues();
        String result = history.getResult();
        et.setText(ques);
        et_result.setHint(result);
    }

    //关闭一些bt，并启用进制转换
    private boolean isbb = false;

    private void closebt() {
        if (!isbb) {
            isbb = true;
            mTextView_bb.setText("常规计算");
            mTextView_hex.setVisibility(View.VISIBLE);
            mTextView_oct.setVisibility(View.VISIBLE);
            mTextView_bin.setVisibility(View.VISIBLE);
            mTextView_tensix.setVisibility(View.VISIBLE);
            mTextView_eight.setVisibility(View.VISIBLE);
            mTextView_two.setVisibility(View.VISIBLE);
            for (int i = 0; i < buttons.size(); i++) {
                buttons.get(i).setClickable(false);
                buttons.get(i).setTextColor(Color.parseColor("#bcbcbc"));
            }
            et.setText("");
            et_result.setHint("");
        } else {
            isbb = false;
            mTextView_hex.setVisibility(View.GONE);
            mTextView_oct.setVisibility(View.GONE);
            mTextView_bin.setVisibility(View.GONE);
            mTextView_tensix.setVisibility(View.GONE);
            mTextView_eight.setVisibility(View.GONE);
            mTextView_two.setVisibility(View.GONE);
            mTextView_hex.setText("");
            mTextView_oct.setText("");
            mTextView_bin.setText("");
            mTextView_bb.setText("进制转换");
            for (int i = 0; i < buttons.size(); i++) {
                buttons.get(i).setClickable(true);
                buttons.get(i).setTextColor(Color.parseColor("#23f1d2"));
            }
        }
    }

    private void bbgetresult() {
        if (et.getText() == null) {
            return;
        }
        String ques = et.getText().toString();
        if (ques.length() == 0) {
            ques = "0";
        }
        long ten = Long.parseLong(ques);
        String two = Long.toBinaryString(ten);
        String tensix = Long.toHexString(ten);
        String eight = Long.toOctalString(ten);
        mTextView_bin.setText(two);
        mTextView_oct.setText(eight);
        mTextView_hex.setText(tensix.toUpperCase());
    }

    private String delete(String des, String news) {
        StringBuffer stringBuffer = new StringBuffer(des);
        while (stringBuffer.toString().contains(news)) {
            stringBuffer = stringBuffer.deleteCharAt(stringBuffer.indexOf(news));
        }
        return stringBuffer.toString();
    }

    private String op(String num1, String num2, String op) {
        double result = 0;
        double d1 = Double.parseDouble(num1);
        double d2 = Double.parseDouble(num2);
//        对于double的乘法运算的精度问题用bigdecimal解决
        BigDecimal dd1 = new BigDecimal(num1);
        BigDecimal dd2 = new BigDecimal(num2);
        switch (op) {
            case "+":
                result = dd1.add(dd2).doubleValue();
                break;
            case "-":
                result = dd1.min(dd2).doubleValue();
                break;
            case "*":
                result = dd1.multiply(dd2).doubleValue();
                break;
            case "/":
                if (d2 == 0) {
                    result = 0;
                    Toast.makeText(this, "存在被0除的错误，请检查计算式！", Toast.LENGTH_LONG).show();
                } else {
                    result = dd1.divide(dd2).doubleValue();
                }
                break;
            default:
                result = d1;
                break;
        }
        return result + "";
    }

    private String multigetresult(String ques) {
        String result = "";
        if (ques.length() == 0) {
            return result;
        }
        Queue<String> nums = new ArrayDeque<>();
        Queue<String> ops = new ArrayDeque<>();
        //将负号前的括号去掉
        ques = delete(ques, "(");
        ques = delete(ques, ")");
        //将元素分开
        String[] items = ques.split(" ");
        for (int i = 1; i < items.length; i = i + 2) {
            if (items[i].equals("+") || items[i].equals("-")) {
                //若操作符是+-
                nums.add(items[i - 1]);
                ops.add(items[i]);
            } else {
                //若操作符是 * , /
                items[i + 1] = op(items[i - 1], items[i + 1], items[i]);
            }
        }
        nums.add(items[items.length - 1]);

        result = nums.poll();
        while (!ops.isEmpty()) {
            String num2 = nums.poll();
            String op = ops.poll();
            result = op(result, num2, op);
            if (num2.equals("0") && op.equals("/")) {
                return "0";
            }
        }
        return result;
    }
}