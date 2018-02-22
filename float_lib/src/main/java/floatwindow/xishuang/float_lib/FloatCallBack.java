package floatwindow.xishuang.float_lib;

import floatwindow.xishuang.float_lib.view.FloatLayout;

/**
 * Author:xishuang
 * Date:2017.08.01
 * Des:暴露一些与悬浮窗交互的接口
 */
public interface FloatCallBack {
    void guideUser(int type);

    void show();

    void hide();

    void addObtainNumer();

    void setObtainNumber(int number);

    void setAnswers(String platFormChineseName, String sougo, String baidu, String uc);
    void setOnClickCloseListener(FloatLayout.OnClickCloseListener OnClickCloseListener);


}
