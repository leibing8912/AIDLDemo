package cn.jianke.aidldemo.aidl;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * @className: UserInfo
 * @classDescription: 用户信息（实现Parcelable序列化，用于binder跨进程通信）
 * @author: leibing
 * @createTime: 2017/1/5
 */

public class UserInfo implements Parcelable{
    // 用户名标识
    public final static String USRR_NAME = "user_name";
    // 用户年龄标识
    public final static String USER_AGE = "user_age";
    // 用户简介标识
    public final static String USER_INTRODUCTION = "user_introduction";
    // 用户名
    private String userName;
    // 用户年龄
    private int userAge;
    // 用户简介
    private String userIntroduction;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserAge() {
        return userAge;
    }

    public void setUserAge(int userAge) {
        this.userAge = userAge;
    }

    public String getUserIntroduction() {
        return userIntroduction;
    }

    public void setUserIntroduction(String userIntroduction) {
        this.userIntroduction = userIntroduction;
    }

    /**
     * Constructor
     * @author leibing
     * @createTime 2017/1/5
     * @lastModify 2017/1/5
     * @param userName 用户名
     * @param userAge 用户年龄
     * @param userIntroduction 用户简介
     * @return
     */
    public UserInfo(String userName, int userAge, String userIntroduction){
        this.userName = userName;
        this.userAge = userAge;
        this.userIntroduction = userIntroduction;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // 构造Bundle
        Bundle bundle = new Bundle();
        // 用户年龄
        bundle.putInt(USER_AGE, this.getUserAge());
        // 用户名
        bundle.putString(USRR_NAME, this.getUserName());
        // 用户简介
        bundle.putString(USER_INTRODUCTION, this.getUserIntroduction());
        // 存入数据
        dest.writeBundle(bundle);
    }

    protected UserInfo(Parcel in) {
        // 读取数据
        Bundle bundle = in.readBundle();
        if (bundle != null){
            // 用户年龄
            setUserAge(bundle.getInt(USER_AGE));
            // 用户名
            setUserName(bundle.getString(USRR_NAME));
            // 用户简介
            setUserIntroduction(bundle.getString(USER_INTRODUCTION));
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel in) {
            return new UserInfo(in);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };
}
