package kccistc.check_url;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.text.SpannableString;
import android.text.style.LeadingMarginSpan;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import android.widget.ImageView;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

public class CheckStateActivity extends AppCompatActivity {
    private Socket socket;  //소켓생성
    BufferedReader in;      //서버로부터 온 데이터를 읽는다.
    PrintWriter out;        //서버에 데이터를 전송한다.
    String data;            //
    String data2;
    String ip_number;
    int port_number;
    String url;
    Thread worker;
    TextView show_url;
    TextView server_state;
    ImageView state_image;
    Drawable image;
    Button report_button;
    ProgressBar circle;
    TextView txtResult;
    String[] V_name;
    String[] M_type;
    ArrayList report = new ArrayList();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.check_state);
        show_url = findViewById(R.id.get_url);
        state_image = findViewById(R.id.state_image);
        server_state = findViewById(R.id.server_state);
        ip_number = "118.67.133.72";
        port_number = 8085;
        //circle = findViewById(R.id.circle_progress);
        report_button = findViewById(R.id.result_button);


        final Intent intent = getIntent();
        url = intent.getStringExtra("url");
                /*
                runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                                show_url.setText(data);
                        }
                });
*/
        image = getResources().getDrawable(R.drawable.checking_url);
        state_image.setImageDrawable(image);
        // server_state.setText("파일의 해시값을 추출하고 있어요.");
        //server_state.setText("추출한 해시값을 알려진 악성코드와 비교하고 있어요.");
        //server_state.setText("이 파일은 악성코드로 판별됐어요.");
        //server_state.setText("확인을 위해 저희 기기에서 대신 파일을 다운로드하고 있어요.");
        //server_state.setText("파일의 용량이 너무 커서 다운로드 할 수 없어요.");
        //server_state.setText("알려진 악성코드에 포함되지 않는 파일이에요. 출처가 분명하지 않다면 설치하지 않으시길 추천드려요.");
        //server_state.setText("다운로드되는 파일이 존재하는 URL이에요.");
        server_state.setText("보내주신 URL을 확인하고 있어요.");
        //server_state.setText("유효하지 않거나 판단이 불가능한 URL이에요.");
        //server_state.setText("웹페이지로 연결되는 URL이에요.");


        // ip_number = intent.getStringExtra("ip");
        // port_number = Integer.parseInt(intent.getStringExtra("port"));


        worker = new Thread() {    //Thread 생성
            public void run() { //스레드 실행구문
                try {
                    socket = new Socket(ip_number, 8085); //소켓생성
                    out = new PrintWriter(socket.getOutputStream(), true); //데이터 전송시 stream 형태로 변환 전송
                    out.write(url);
                    out.flush();
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream())); //데이터 수신
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //소켓에서 데이터 수신

                try {
                    while (true) {
                        data = in.readLine(); // 서버에서 보내준 데이터를 String 형태로 data에 저장
                        Log.w("report_", String.valueOf(report));
                        Log.w("received_from_server5", String.valueOf(data));
                        if (data.contains("0000")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    image = getResources().getDrawable(R.drawable.too_large_to_download);
                                    state_image.setImageDrawable(image);
                                    server_state.setText("파일의 용량이 너무 커서 다운로드할 수 없어요.");
                                }
                            });
                        } else if (String.valueOf(data).contains("invalid_url")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    image = getResources().getDrawable(R.drawable.invalid_url);
                                    state_image.setImageDrawable(image);
                                    server_state.setText(String.valueOf("유효하지 않은 URL 입니다."));
                                    report_button.setVisibility(View.VISIBLE);
                                    report_button.setText("돌아가기");
                                }
                            });
                            //Log.w("received_from_server2", String.valueOf(data));
                        } else if (data.contains("1111")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    image = getResources().getDrawable(R.drawable.downloading);
                                    state_image.setImageDrawable(image);
                                    server_state.setText("저희 기기에서 대신 파일을 다운로드하고 있어요.");
                                }
                            });
                            //Log.w("received_from_server1", String.valueOf(data));
                        } else if (data.contains("바이러스토탈에서")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    image = getResources().getDrawable(R.drawable.hashing);
                                    state_image.setImageDrawable(image);
                                    server_state.setText("다운로드된 파일을 조사하고 있어요.");
                                }
                            });
                            //Log.w("received_from_server1", String.valueOf(data));
                        } else if (data.contains("3333")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    image = getResources().getDrawable(R.drawable.using_api);
                                    state_image.setImageDrawable(image);
                                    server_state.setText("알려진 악성코드와 해시값을 대조하고 있어요.");
                                }
                            });
                            //Log.w("received_from_server1", String.valueOf(data));
                        } else if (data.contains("4444")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    image = getResources().getDrawable(R.drawable.checking_file);
                                    state_image.setImageDrawable(image);
                                    server_state.setText("알려진 악성코드에 해당하지 않는 파일이에요.");
                                    report_button.setVisibility(View.VISIBLE);
                                }
                            });
                            //Log.w("received_from_server1", String.valueOf(data));
                        } else if (data.contains("5555") || data.contains("해시값_Detected")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    image = getResources().getDrawable(R.drawable.check_as_virus);
                                    state_image.setImageDrawable(image);
                                    server_state.setText("이 파일은 악성코드로 판별됐어요.");
                                    report_button.setVisibility(View.VISIBLE);
                                }
                            });
                            //Log.w("received_from_server2", String.valueOf(data));
                        } else if (data.contains("바이러스_검출")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    image = getResources().getDrawable(R.drawable.check_as_virus);
                                    state_image.setImageDrawable(image);
                                    server_state.setText("이 파일은 악성코드로 판별됐어요.");
                                    report_button.setVisibility(View.VISIBLE);
                                }
                            });
                            //Log.w("received_from_server1", String.valueOf(data));
                        } else if (data.contains("바이러스_미검출")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    image = getResources().getDrawable(R.drawable.checking_file);
                                    state_image.setImageDrawable(image);
                                    server_state.setText("알려진 악성코드에 해당하지 않는 파일이에요.");
                                    report_button.setVisibility(View.VISIBLE);
                                    report.add(data);
                                }
                            });
                            //Log.w("received_from_server1", String.valueOf(data));
                        } else if (String.valueOf(data).contains("알려진 위협이")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    image = getResources().getDrawable(R.drawable.no_virus);
                                    state_image.setImageDrawable(image);
                                    server_state.setText(String.valueOf(data));
                                    report.add(data);
                                    report_button.setVisibility(View.VISIBLE);
                                }
                            });
                            //Log.w("received_from_server2", String.valueOf(data));
                        } else if (String.valueOf(data).contains("대한 위협")) {
                                    report.add("알려진 위협이 존재하는 웹페이지입니다.");
                                    report.add(data);
                            //Log.w("received_from_server2", String.valueOf(data));
                        } else if (String.valueOf(data).contains("위협 유형")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    image = getResources().getDrawable(R.drawable.check_as_phishing);
                                    state_image.setImageDrawable(image);
                                    server_state.setText(String.valueOf("알려진 위협이 존재하는 웹페이지입니다."));
                                    report.add(data);
                                    report_button.setVisibility(View.VISIBLE);
                                }
                            });
                            //Log.w("received_from_server2", String.valueOf(data));
                        } else if (String.valueOf(data).contains("Country_")) {
                            report.add(data);
                        } else if (String.valueOf(data).contains("IP :")) {
                            report.add(data);
                        } else if (String.valueOf(data).contains("V_name")) {
                            V_name = data.split(",");
                            // Log.w("received_from_server2", String.valueOf(data));
                        } else if (String.valueOf(data).contains("M_type")) {
                            M_type = data.split(",");
                            //Log.w("received_from_server2", String.valueOf(data));
                        } else if (String.valueOf(data).contains("disconnect_in")) {
                            try {
                                socket.close(); //소켓을 닫는다.
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            // Log.w("received_from_server2", String.valueOf(data));
                        } else {
                            Log.w("received_from_server3", String.valueOf(data));
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        worker.start();  //onResume()에서 실행.*/
    }

    public void mOnPopupClick(View v) {
        if(report_button.getText()=="돌아가기"){
            Intent intent3 = new Intent(this, MainActivity.class);
            startActivityForResult(intent3, 2);
            finish();
        }else {
            Intent intent2 = new Intent(this, ResultPopActivity.class);
            Bundle bundle = new Bundle();
            bundle.putStringArray("V_name", V_name);
            bundle.putStringArray("M_type", M_type);
            bundle.putStringArrayList("report", report);
            intent2.putExtras(bundle);
            startActivityForResult(intent2, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                //데이터 받기
                String result = data.getStringExtra("result");
                txtResult.setText(result);
            }
        }
    }

    @Override
    public void onBackPressed() {

        try {
            socket.close(); //소켓을 닫는다.
            Toast.makeText(this.getApplicationContext(), "서버와 연결이 종료되었습니다.", Toast.LENGTH_SHORT).show();
            finish();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {  //앱 종료시
        super.onStop();
        try {
            socket.close(); //소켓을 닫는다.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}