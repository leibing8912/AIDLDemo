package cn.jianke.aidldemo.module.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import cn.jianke.aidldemo.aidl.IUserInfoManager;
import cn.jianke.aidldemo.aidl.UserInfo;
import cn.jianke.aidldemo.aidl.UserInfoCallBack;

/**
 * @className: UserInfoManagerService
 * @classDescription: 用户信息管理服务
 * @author: leibing
 * @createTime: 2017/1/5
 */
public class UserInfoManagerService extends Service{
    // 用户信息列表
    private CopyOnWriteArrayList<UserInfo> userInfos = new CopyOnWriteArrayList<>();
    // 服务端定义Binder类（IUserInfoManager.Stub）
    private Binder mBinder = new IUserInfoManager.Stub(){
        @Override
        public List<UserInfo> getUserInfoList() throws RemoteException {
            return userInfos;
        }

        @Override
        public void addUserInfo(UserInfo userInfo, UserInfoCallBack icallback) throws RemoteException {
            if(userInfo != null){
                userInfos.add(userInfo);
                icallback.returnUserName(userInfo.getUserName());
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        userInfos.add(new UserInfo("张三", 25, "IT码农"));
        userInfos.add(new UserInfo("李四", 28, "销售经理"));
        userInfos.add(new UserInfo("王五", 21, "客服人员"));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }
}
