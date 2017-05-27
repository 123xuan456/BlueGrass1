package com.reeching.bluegrass;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.reeching.bean.HuaLangShowing;
import com.reeching.utils.HttpApi;

import java.util.Calendar;

public class XiadaActivity extends AppCompatActivity {
private TextView tvtype,tvpeople,tvtime,tvstate,tvremark;
    private String[] type = new String[] { "检查", "核查" };
    private String[] typeids = new String[] { "0", "1" };
    private String[] status = new String[] { "已完成", "进行中" };
    private String[] statusids = new String[] { "1", "0" };
    private HuaLangShowing.Infos infos;
    private String  typeid,persionid,statusid;
    private String[] persion,persionids;
    private DatePickerDialog dpdialog;
    private Button btn;
    private TextView tvback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xiada);
        tvtype= (TextView) findViewById(R.id.xiada_type);
        tvpeople= (TextView) findViewById(R.id.xiada_people);
        tvtime= (TextView) findViewById(R.id.xiada_time);
        tvstate= (TextView) findViewById(R.id.xiada_state);
        tvremark= (TextView) findViewById(R.id.xiada_remark);
        btn= (Button) findViewById(R.id.xiada_report);
        tvback= (TextView) findViewById(R.id.comeback);
        infos= (HuaLangShowing.Infos) getIntent().getSerializableExtra("info");

        tvtype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               AlertDialog.Builder builder = new AlertDialog.Builder(XiadaActivity.this);
                builder.setTitle("请选择任务类型");
                builder.setSingleChoiceItems(type, 0,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                tvtype.setText(type[which]);
                                typeid = typeids[which];
                                dialog.dismiss();
                            }
                        });
                builder.show();
            }
        });
        initdata();
        tvtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDate();
            }
        });

        tvpeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(XiadaActivity.this);
                builder.setTitle("请选择任务接收人");
                builder.setSingleChoiceItems(persion, 0,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                tvpeople.setText(persion[which]);
                                persionid = persionids[which];
                                dialog.dismiss();
                            }
                        });
                builder.show();
            }
        });
        tvstate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(XiadaActivity.this);
                builder.setTitle("请选择状态");
                builder.setSingleChoiceItems(status, 0,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                tvstate.setText(status[which]);
                                statusid = statusids[which];
                                dialog.dismiss();
                            }
                        });
                builder.show();
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn.setClickable(false);
                if (!tvstate.getText().toString().equals("")&&!tvtime.getText().toString().equals("")
                        &&!tvpeople.getText().toString().equals("")&&!tvtype.getText().toString().equals("")){
                    xiada();
                }else{
                    Toast.makeText(XiadaActivity.this,"请填写必选信息",Toast.LENGTH_SHORT).show();
                    btn.setClickable(true);
                }
            }
        });
        tvback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XiadaActivity.this.finish();
            }
        });
    }
    private void initDate() {

        Calendar calendar = Calendar.getInstance();
        int year1 = calendar.get(Calendar.YEAR);
        int month1 = calendar.get(Calendar.MONTH);
        int day1 = calendar.get(Calendar.DAY_OF_MONTH);
        dpdialog = new DatePickerDialog(XiadaActivity.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker arg0, int year, int month,
                                          int day) {

                        tvtime.setText(year + "-" + (month + 1) + "-" + day);

                    }
                }, year1, month1, day1);

        dpdialog.show();

    }

    private void initdata(){
    HttpUtils hu=new HttpUtils();
   hu.send(HttpMethod.POST, HttpApi.ip + HttpApi.xiadapeople, new RequestCallBack<String >() {
       @Override
       public void onSuccess(ResponseInfo<String> responseInfo) {
           JSONObject JSONObject= JSON.parseObject(responseInfo.result);
           if (JSONObject.getString("result").equals("1")){
               JSONArray array=JSONObject.getJSONArray("infos");
               persion=new String[array.size()];
               persionids=new String[array.size()];
               if (array.size()>0){
                   for (int i=0;i<array.size();i++){
                       JSONObject obj=array.getJSONObject(i);
                       persion[i]=obj.getString("name");
                       persionids[i]=obj.getString("id");
                   }
               }
           }

       }

       @Override
       public void onFailure(HttpException e, String s) {

       }
   });
}
    private void xiada(){
        HttpUtils hu=new HttpUtils();
        RequestParams params=new RequestParams();
        params.addQueryStringParameter("galleryId",infos.getGalleryId());
        params.addQueryStringParameter("exhibitionId",infos.getId());
        params.addQueryStringParameter("taskType",typeid);
        params.addQueryStringParameter("taskReceiverId",persionid);
        params.addQueryStringParameter("planTime",tvtime.getText().toString());
        params.addQueryStringParameter("taskStatus",statusid);
        params.addQueryStringParameter("createBy",BaseApplication.getInstance()
                .getId());
        params.addQueryStringParameter("remarks",tvremark.getText().toString());
        hu.send(HttpMethod.POST, HttpApi.ip + HttpApi.taskxiada, params, new RequestCallBack<String >() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                JSONObject json=JSON.parseObject(responseInfo.result);
                if(json.getString("result").equals("1")){
                    Toast.makeText(XiadaActivity.this,"下达成功",Toast.LENGTH_SHORT).show();
                }else if(json.getString("result").equals("2")){
                    Toast.makeText(XiadaActivity.this,"任务已被下达过",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(XiadaActivity.this,"下达失败",Toast.LENGTH_SHORT).show();
                }
                btn.setClickable(true);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                btn.setClickable(true);
            }
        });

    }
}
