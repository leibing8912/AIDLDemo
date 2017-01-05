package cn.jianke.aidldemo.module.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import cn.jianke.aidldemo.R;
import cn.jianke.aidldemo.aidl.IUserInfoManager;
import cn.jianke.aidldemo.aidl.UserInfo;
import cn.jianke.aidldemo.aidl.UserInfoCallBack;
import cn.jianke.aidldemo.common.StringUtil;
import cn.jianke.aidldemo.module.adapter.UserInfoAdapter;

/**
 * @className: ClientActivity
 * @classDescription: 客户端页面（aidl）
 * @author: leibing
 * @createTime: 2017/1/5
 */
public class ClientActivity extends Activity implements View.OnClickListener{
    // 用户信息
    private ListView mUserInfoLv;
    // 用户名
    private EditText mUserNameEdt;
    // 用户年龄
    private EditText mUserAgeEdt;
    // 用户简介
    private EditText mUserIntroductionEdt;
    // 用户信息适配器
    private UserInfoAdapter mUserInfoAdapter;
    // 数据源
    private List<UserInfo> mData;
    // userinfo handler
    private UserInfoHandler mUserInfoHandler;
    // user info manager
    private  IUserInfoManager userInfoManager;
    // 服务连接
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            userInfoManager = IUserInfoManager.Stub.asInterface(iBinder);
            try{
                List<UserInfo> userInfos = userInfoManager.getUserInfoList();
                if (userInfos != null
                        && userInfos.size() != 0
                        && mUserInfoHandler != null){
                    Message msg = new Message();
                    msg.obj = userInfos;
                    mUserInfoHandler.sendMessage(msg);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 启动服务
        startService();
        // findView
        mUserNameEdt = (EditText) findViewById(R.id.edt_user_name);
        mUserAgeEdt = (EditText) findViewById(R.id.edt_user_age);
        mUserIntroductionEdt = (EditText) findViewById(R.id.edt_user_introduction);
        mUserInfoLv = (ListView) findViewById(R.id.lv_user_info);
        // onClick
        findViewById(R.id.btn_add_user_info).setOnClickListener(this);
        // init data
        mData = new ArrayList<>();
        // init userinfo handler
        mUserInfoHandler = new UserInfoHandler(this, new UserInfoHandler.HandlerCallBack() {
            @Override
            public void couldUpdateUI(List<UserInfo> userInfos) {
                if (mUserInfoAdapter != null){
                    mUserInfoAdapter.setData(userInfos);
                }
            }
        });
        // set Adapter
        mUserInfoAdapter = new UserInfoAdapter(this, mData);
        mUserInfoLv.setAdapter(mUserInfoAdapter);
    }

    /**
     * 启动服务
     * @author leibing
     * @createTime 2017/1/5
     * @lastModify 2017/1/5
     * @param
     * @return
     */
    private void startService() {
        Intent intent = new Intent();
        intent.setAction("cn.jianke.aidldemo.module.service.UserInfoManagerService");
        startService(intent);
        bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        // 解绑服务
        unbindService(mServiceConnection);
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_add_user_info:
                addNewUserInfo();
                break;
            default:
                break;
        }
    }
    
    /**
     * add new user info
     * @author leibing
     * @createTime 2017/1/5
     * @lastModify 2017/1/5
     * @param
     * @return
     */
    private void addNewUserInfo() {
        if (StringUtil.isEmpty(mUserNameEdt.getText().toString())){
            Toast.makeText(this, "user name is empty, please checkout it!",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtil.isEmpty(mUserAgeEdt.getText().toString())){
            Toast.makeText(this, "user age is empty, please checkout it!",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (!StringUtil.strIsNum(mUserAgeEdt.getText().toString())){
            Toast.makeText(this, "user age is not  digit, please checkout it!",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtil.isEmpty(mUserIntroductionEdt.getText().toString())){
            Toast.makeText(this, "user introduction is empty, please checkout it!",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (userInfoManager != null){
            UserInfo userInfo = new UserInfo(mUserNameEdt.getText().toString(),
                    Integer.parseInt(mUserAgeEdt.getText().toString()),
                    mUserIntroductionEdt.getText().toString());
            try {
                userInfoManager.addUserInfo(userInfo, new UserInfoCallBack() {
                    @Override
                    public void returnUserName(final String userName) throws RemoteException {
                        if (mUserInfoHandler != null){
                            mUserInfoHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ClientActivity.this,
                                            userName, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                    @Override
                    public IBinder asBinder() {
                        return null;
                    }
                });
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @interfaceName: UserInfoHandler
     * @interfaceDescription: UserInfo Handler
     * @author: leibing
     * @createTime: 2017/1/5
     */
    public static class UserInfoHandler extends Handler{
        // weak ref
        private WeakReference<Context> mContextWeakRef;
        // Handler CallBack
        private HandlerCallBack mHandlerCallBack;

        /**
         * Constructor
         * @author leibing
         * @createTime 2017/1/5
         * @lastModify 2017/1/5
         * @param context ref
         * @return
         */
        public UserInfoHandler(Context context, HandlerCallBack mHandlerCallBack){
            mContextWeakRef = new WeakReference<Context>(context);
            this.mHandlerCallBack = mHandlerCallBack;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg == null)
                return;
            if (msg.obj == null)
                return;
            try {
                if (mHandlerCallBack != null
                        && mContextWeakRef != null &&
                        mContextWeakRef.get() != null){
                    List<UserInfo> userInfos = (List<UserInfo>) msg.obj;
                    if (userInfos != null) {
                        mHandlerCallBack.couldUpdateUI(userInfos);
                    }
                }
            }catch (Exception ex){
            }
        }

        /**
         * @interfaceName: HandlerCallBack
         * @interfaceDescription:Handler CallBack
         * @author: leibing
         * @createTime: 2017/1/5
         */
        public interface HandlerCallBack{
            // can update ui
            void couldUpdateUI(List<UserInfo> userInfos);
        }
    }
}
