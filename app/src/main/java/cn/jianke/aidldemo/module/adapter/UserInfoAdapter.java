package cn.jianke.aidldemo.module.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;
import cn.jianke.aidldemo.R;
import cn.jianke.aidldemo.aidl.UserInfo;
import cn.jianke.aidldemo.common.StringUtil;

/**
 * @className: UserInfoAdapter
 * @classDescription: 用户信息适配器
 * @author: leibing
 * @createTime: 2017/1/5
 */
public class UserInfoAdapter extends BaseAdapter{
    // 布局
    private LayoutInflater mLayoutInflater;
    // 数据源
    private List<UserInfo> mData;

    /**
     * Constructor
     * @author leibing
     * @createTime 2017/1/5
     * @lastModify 2017/1/5
     * @param context ref
     * @param mData  数据源
     * @return
     */
    public UserInfoAdapter(Context context, List<UserInfo> mData){
        mLayoutInflater = LayoutInflater.from(context);
        this.mData = mData;
    }

    /**
     * set data
     * @author leibing
     * @createTime 2017/1/5
     * @lastModify 2017/1/5
     * @param mData
     * @return
     */
    public void setData(List<UserInfo> mData){
        this.mData = mData;
        UserInfoAdapter.this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData != null?mData.size():0;
    }

    @Override
    public Object getItem(int i) {
        return mData != null?mData.get(i):null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null){
            view = mLayoutInflater.inflate(R.layout.item_user_info, null);
            // init ViewHolder
            holder = new ViewHolder(view);
            // set view tag
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }

        // updateUi
        if (mData != null
                && mData.size() !=0
                && i< mData.size()){
            holder.updateUI(mData.get(i));
        }

        return view;
    }

    /**
     * @interfaceName: ViewHolder
     * @interfaceDescription: 视图容器
     * @author: leibing
     * @createTime: 2017/1/5
     */
    static class ViewHolder{
        // user name
        private TextView userNameTv;
        // user age
        private TextView userAgeTv;
        // user introduction
        private TextView userIntroductionTv;

        /**
         * Constructor
         * @author leibing
         * @createTime 2017/1/5
         * @lastModify 2017/1/5
         * @param view
         * @return
         */
        public ViewHolder(View view){
            // user name
            userNameTv = (TextView) view.findViewById(R.id.tv_user_name);
            // user age
            userAgeTv = (TextView) view.findViewById(R.id.tv_user_age);
            // user introduction
            userIntroductionTv = (TextView) view.findViewById(R.id.tv_user_introduction);
        }

        /**
         * update Ui
         * @author leibing
         * @createTime 2017/1/5
         * @lastModify 2017/1/5
         * @param userInfo
         * @return
         */
        public void updateUI(UserInfo userInfo){
            // update user name
            if (StringUtil.isNotEmpty(userInfo.getUserName())){
                userNameTv.setText(userInfo.getUserName());
            }
            // update user age
            userAgeTv.setText(userInfo.getUserAge() + "");
            // update user introduction
            if (StringUtil.isNotEmpty(userInfo.getUserIntroduction())){
                userIntroductionTv.setText(userInfo.getUserIntroduction());
            }
        }
    }
}
