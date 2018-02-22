package floatwindow.xishuang.float_lib.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import floatwindow.xishuang.float_lib.FloatActionController;
import floatwindow.xishuang.float_lib.R;

/**
 * Author:xishuang
 * Date:2017.08.01
 * Des:悬浮窗的布局
 */
public class FloatLayout extends FrameLayout {
  private final WindowManager mWindowManager;
  private final ImageView mIVClose;
  //private final DraggableFlagView mDraggableFlagView;
  private final TextView mTVSougoDaAn;
  private final TextView mTVBaiduDaAn;
  private final TextView mTVUCDaAn;
  private final TextView mTVPlatform;
  private long startTime;
  private float mTouchStartX;
  private float mTouchStartY;
  private boolean isclick;
  private WindowManager.LayoutParams mWmParams;
  private Context mContext;
  private long endTime;
  private OnClickCloseListener mOnClickCloseListener;

  public FloatLayout(Context context) {
    this(context, null);
    mContext = context;
  }

  public FloatLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
    mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    LayoutInflater.from(context).inflate(R.layout.float_littlemonk_layout, this);
    //浮动窗口按钮
    mIVClose = (ImageView) findViewById(R.id.iv_close);
    mIVClose.setOnClickListener(new OnClickListener() {
      @Override public void onClick(View v) {
        if (mOnClickCloseListener != null) {
          mOnClickCloseListener.onClickClose();
        }
        FloatActionController.getInstance().stopMonkServer(getContext());
      }
    });
    //mDraggableFlagView = (DraggableFlagView) findViewById(R.id.main_dfv);
    //mDraggableFlagView.setOnDraggableFlagViewListener(new DraggableFlagView.OnDraggableFlagViewListener() {
    //    @Override
    //    public void onFlagDismiss(DraggableFlagView view) {
    //        //小红点消失的一些操作
    //    }
    //});
    mTVPlatform = (TextView) findViewById(R.id.tv_welcome);
    mTVSougoDaAn = (TextView) findViewById(R.id.tv_sougo_da_an);
    mTVBaiduDaAn = (TextView) findViewById(R.id.tv_baidu_da_an);
    mTVUCDaAn = (TextView) findViewById(R.id.tv_uc_da_an);
    FloatActionController.getInstance().setObtainNumber(1);
  }

  @Override public boolean onTouchEvent(MotionEvent event) {
    // 获取相对屏幕的坐标，即以屏幕左上角为原点
    int x = (int) event.getRawX();
    int y = (int) event.getRawY();
    //下面的这些事件，跟图标的移动无关，为了区分开拖动和点击事件
    int action = event.getAction();
    switch (action) {
      case MotionEvent.ACTION_DOWN:
        startTime = System.currentTimeMillis();
        mTouchStartX = event.getX();
        mTouchStartY = event.getY();
        break;
      case MotionEvent.ACTION_MOVE:
        //图标移动的逻辑在这里
        float mMoveStartX = event.getX();
        float mMoveStartY = event.getY();
        // 如果移动量大于3才移动
        if (Math.abs(mTouchStartX - mMoveStartX) > 3 && Math.abs(mTouchStartY - mMoveStartY) > 3) {
          // 更新浮动窗口位置参数
          mWmParams.x = (int) (x - mTouchStartX);
          mWmParams.y = (int) (y - mTouchStartY);
          mWindowManager.updateViewLayout(this, mWmParams);
          return false;
        }
        break;
      case MotionEvent.ACTION_UP:
        endTime = System.currentTimeMillis();
        //当从点击到弹起小于半秒的时候,则判断为点击,如果超过则不响应点击事件
        if ((endTime - startTime) > 0.1 * 1000L) {
          isclick = false;
        } else {
          isclick = true;
        }
        break;
    }
    //响应点击事件
    //if (isclick) {
    //  Toast.makeText(mContext, "我是大傻叼", Toast.LENGTH_SHORT).show();
    //}
    return true;
  }

  /**
   * 将小悬浮窗的参数传入，用于更新小悬浮窗的位置。
   *
   * @param params 小悬浮窗的参数
   */
  public void setParams(WindowManager.LayoutParams params) {
    mWmParams = params;
  }

  /**
   * 设置小红点显示
   */
  public void setDragFlagViewVisibility(int visibility) {
    //mDraggableFlagView.setVisibility(visibility);
  }

  /**
   * 设置小红点数量
   */
  public void setDragFlagViewText(int number) {
    //mDraggableFlagView.setText(number + "");
  }

  /**
   * 更新答案结果
   */
  public void setAnswers(String platFormChineseName, String sougo, String baidu, String uc) {

    if (TextUtils.isEmpty(sougo)) {
      sougo = "";
    }
    if (TextUtils.isEmpty(baidu)) {
      baidu = "";
    }
    if (TextUtils.isEmpty(uc)) {
      uc = "";
    }
    if (TextUtils.isEmpty(platFormChineseName)) {
      mTVPlatform.setText(R.string.aolai_tip);
    } else {
      mTVPlatform.setText("百年奥莱答题助手，欢迎您！您当前正在" + platFormChineseName + "答题");
    }
    mTVSougoDaAn.setText(sougo);
    mTVBaiduDaAn.setText(baidu);
    mTVUCDaAn.setText(uc);
  }

  public interface OnClickCloseListener {
    void onClickClose();
  }

  public void setOnClickCloseListener(OnClickCloseListener pOnClickCloseListener) {
    mOnClickCloseListener = pOnClickCloseListener;
  }
}
