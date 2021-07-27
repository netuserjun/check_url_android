package kccistc.check_url;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.LeadingMarginSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import java.util.ArrayList;

public class ResultPopActivity extends Activity {

    TextView txtText;
    TextView txtText2;
    ArrayList report = new ArrayList();
    String ip = "";

    static SpannableString createIndentedText(String text, int marginFirstLine, int marginNextLines) {
        SpannableString result=new SpannableString(text);
        result.setSpan(new LeadingMarginSpan.Standard(marginFirstLine, marginNextLines),0,text.length(),0);
        return result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.result_pop);

        //UI 객체생성
        txtText = (TextView) findViewById(R.id.txtText);
        txtText2 = (TextView) findViewById(R.id.txtText2);

        //데이터 가져오기
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String[] V_name = bundle.getStringArray("V_name");
        String[] M_type = bundle.getStringArray("M_type");
        report = bundle.getStringArrayList("report");

        Log.w("popup_V_name", String.valueOf(V_name));
        Log.w("popup_M_type", String.valueOf(M_type));
        Log.w("popup_report", String.valueOf(report));

        if(V_name != null) {
            String brief = "68개 백신 중 " +"<b>" + (V_name.length - 1)+"개"+"</b>" +"가 탐지한 결과입니다. \n";
            txtText.setText(Html.fromHtml(brief));
            Log.w("popup_V_name", String.valueOf(V_name));
            for (int i = 1; i < V_name.length; i++) {
                if (i == (V_name.length - 1)) {
                    txtText2.append("백신 이름  :  " + V_name[i].substring(2, V_name[i].length() - 2) + "\n" + "탐지 항목  :  " + M_type[i].substring(2, M_type[i].length() - 2) );
                }else{
                    txtText2.append("백신 이름  :  " + V_name[i].substring(2, V_name[i].length() - 1) + "\n" + "탐지 항목  :  " + M_type[i].substring(2, M_type[i].length() - 1) + "\n\n");
                }
            }
        }else if(report != null) {

            String[] report2 = report.toString().split(",");

            for (int i = 0; i < report2.length; i++) {

                if (report2[i].contains("IP :")) {
                    ip = report2[i].substring(5);
                    String text1 = "<b>" + report2[report2.length - 1].substring(1, report2[report2.length - 1].length() - 1) + "</b>" + "<br />";
                    txtText.setText(Html.fromHtml(text1));
                    String text2 = "해당 URL이 연결되는 서버 혹은 경유지의 " + "<b>" + "IP 주소" + "</b>" + "는 " + "<b>" + ip + "</b>" + " 입니다. <br /><br />";
                    txtText2.append(Html.fromHtml(text2)); //ip 주소 / ip
                } else if (report2[i].contains("Country_")) {
                    String country_code = report2[i].substring(16);
                    if (country_code.contains("알 수 없음")) {
                        String text3 = "해당 IP의 " + "<b>" + "국가코드" + "</b>" + "에 대한 " + "<b>" + "검색 결과가 없습니다" + "</b>" + ".";
                        txtText2.append(Html.fromHtml(text3)); // 국가코드 / 검색결과가 없습니다
                    } else {
                        String text4 = "해당 IP의 " + "<b>" + "국가코드" + "</b>" + "는 " + "<b>" + country_code + "</b>" + " 입니다.";
                        txtText2.append(Html.fromHtml(text4)); // 국가코드 / cc
                    }
                } else if(report2[i].contains("미검출")){
                    String txtText_content = "해당 URL은 클릭 시 <br />"+"<b>"+"파일이 다운로드"+"</b>"+"됩니다.<br /><br />";
                    String txtText2_content ="";
                    txtText.setText(Html.fromHtml(txtText_content));
                    txtText.setGravity(Gravity.CENTER);
                    for(int j=2;j<report2.length; j++){
                        if(report2[j].contains("세부 사항")){
                            txtText2_content = report2[j];
                            txtText.append(Html.fromHtml(txtText2_content));

                        }else if(j == report2.length-1){
                            txtText2_content = report2[j].substring(0,report2[j].length()-1);
                            txtText2.append(createIndentedText(txtText2_content,0,35));
                        }else if(report2[j].contains("일치하는 항목")){
                            txtText2_content = report2[j] + "\n";
                            Log.w("popup_testlog", String.valueOf(report2[j]));
                            txtText2.append(createIndentedText(txtText2_content,0,35));
                        }else {
                            //txtText2.setGravity(Gravity.LEFT);
                            txtText2_content = report2[j] + "\n";
                            txtText2.append(txtText2_content);
                        }
                    }
                }else if(report2[i].contains("웹페이지")){
                    String txtText_content = "해당 URL은 클릭 시 <br />"+"<b>"+"웹페이지로 연결"+"</b>"+"됩니다.<br />";
                    String txtText2_content ="";
                    txtText.setText(Html.fromHtml(txtText_content));
                    txtText.setGravity(Gravity.CENTER);
                    for(int j=1;j<report2.length; j++) {
                        if (j == report2.length - 1) {
                            txtText2_content = report2[j].substring(0, report2[j].length() - 1);
                            txtText2.append(Html.fromHtml(txtText2_content));
                        } else {
                            txtText2_content = report2[j]+"<br />";
                            txtText2.append(Html.fromHtml(txtText2_content));
                        }
                    }
                }
            }
        }
    }

    //확인 버튼 클릭
    public void mOnClose(View v) {
        //데이터 전달하기
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("result", "다른 URL 검사하기");
        startActivity(intent);

        //액티비티(팝업) 닫기
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }
}