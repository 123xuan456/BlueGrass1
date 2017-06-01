package com.reeching.bluegrass;

import android.app.Application;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.reeching.bean.AllHualangInfo.Infos;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;

import java.util.ArrayList;
import java.util.List;

public class BaseApplication extends Application {
    private static BaseApplication application;
    private List<Infos> info;
    private String loginName;
    private String murl;
    private String versonnum = 1.1 + "";//发布新版本后，来此处更改为对应版本号，必改，否则无限更新。
    private boolean hasnet, initflag;
    private String id;
    private JSONObject obj;
    private String quanxian = "上报用户";


    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        application = this;

        info = new ArrayList<Infos>();
//       CrashHandler handler = CrashHandler.getInstance();
//       handler.init(getApplicationContext()); //在Appliction里面设置我们的异常处理器为UncaughtExceptionHandler处理器
        final PushAgent mPushAgent = PushAgent.getInstance(this);
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                Log.d("dkey", deviceToken);

            }

            @Override
            public void onFailure(String s, String s1) {

            }

        });

    }

    public String getId() {
        return id;
    }

    public boolean isInitflag() {
        return initflag;
    }

    public void setInitflag(boolean initflag) {
        this.initflag = initflag;
    }

    public JSONObject getObj() {
        return obj;
    }

    public void setObj(JSONObject obj) {
        this.obj = obj;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMurl() {
        return murl;
    }

    public void setMurl(String murl) {
        this.murl = murl;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getVersonnum() {
        return versonnum;
    }

    public void setVersonnum(String versonnum) {
        this.versonnum = versonnum;
    }

    public static BaseApplication getInstance() {
        return application;
    }

    public List<Infos> getInfo() {
        return info;
    }

    public void setInfo(List<Infos> info) {
        this.info = info;
    }

    public boolean isHasnet() {
        return hasnet;
    }

    public void setHasnet(boolean hasnet) {
        this.hasnet = hasnet;
    }

    public String getQuanxian() {
        return quanxian;
    }

    public void setQuanxian(String quanxian) {
        this.quanxian = quanxian;
    }
}
