
package cn.jianke.aidldemo.aidl;
import cn.jianke.aidldemo.aidl.UserInfo;
import cn.jianke.aidldemo.aidl.UserInfoCallBack;

// 用户信息管理 add by leibing 2017/1/5
interface IUserInfoManager {
      // 获取用户信息列表
      List<UserInfo> getUserInfoList();
      // 添加用户信息
      void addUserInfo(in UserInfo userInfo,in UserInfoCallBack icallback);
}
