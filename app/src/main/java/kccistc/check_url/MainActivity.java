package kccistc.check_url;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.app.AlertDialog;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText input_url;
    //EditText input_ip
    //EditText input_port;
    Button check_button;
    String url;
    Uri link_data; //링크를 클릭하고 연결프로그램으로 열었을 시 url 전달
    String update_text;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input_url = (EditText) findViewById(R.id.input_url);
        //input_ip = (EditText) findViewById(R.id.input_ip);
        //input_port = (EditText) findViewById(R.id.input_port);
        //input_ip.setText("3.0.9.29");
        //input_port.setText("5001");
        check_button = (Button) findViewById(R.id.check_button);
        check_button.setOnClickListener(this);

        Intent intent = getIntent();
        update_text = intent.getStringExtra("result");


        link_data = this.getIntent().getData();
        if (link_data != null && link_data.isHierarchical()) { //만약 url을 직접 입력하지 않고 클릭해서 연결프로그램으로 앱을 열었을 시.
            String uri = this.getIntent().getDataString();
            Log.i("url_from_user", "Deep link clicked " + uri);
            input_url.setText(uri); // url 입력창에 자동입력해주기.
        }
    }

    public void onClick(View view) {
        if (view.getId() == R.id.check_button) {
            url = input_url.getText().toString();
            if (url.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*")) { // 한글이 포함된 url이 입력되면 걸러주기
                AlertDialog.Builder invalid_url = new AlertDialog.Builder(MainActivity.this);
                invalid_url.setTitle("유효하지 않은 URL 입니다.");
                invalid_url.setMessage("한글이 포함된 URL은 분석할 수 없어요.");
                invalid_url.show();
            } else if (url.isEmpty()) {
                AlertDialog.Builder invalid_url = new AlertDialog.Builder(MainActivity.this);
                invalid_url.setTitle("유효하지 않은 URL 입니다.");
                invalid_url.setMessage("URL을 입력해주세요.");
                invalid_url.show();
            } else {
                Intent intent1 = new Intent(this, CheckStateActivity.class);
                intent1.putExtra("url", input_url.getText().toString());
                startActivity(intent1);
                finish();
            }


        }
    }
}
