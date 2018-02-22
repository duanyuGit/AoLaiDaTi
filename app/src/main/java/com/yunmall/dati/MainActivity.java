package com.yunmall.dati;

import android.app.Dialog;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.baidu.mobstat.StatService;
import com.yunmall.dati.util.AnswersHelper;
import floatwindow.xishuang.float_lib.FloatActionController;
import floatwindow.xishuang.float_lib.permission.FloatPermissionManager;
import floatwindow.xishuang.float_lib.view.FloatLayout;

/**
 * 答题主界面
 */
public class MainActivity extends BaseSettingsActivity {

  private Dialog mTipsDialog;
  private MainFragment mMainFragment;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    StatService.start(this);

    String versionName = "";
    try {
      PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
      versionName = " v" + info.versionName;
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }

    setTitle(getString(R.string.app_name) + versionName);

    IntentFilter filter = new IntentFilter();
    filter.addAction(Config.ACTION_DA_TI_SERVICE_CONNECT);
    filter.addAction(Config.ACTION_DA_TI_SERVICE_DISCONNECT);
    registerReceiver(mBroadcastReceiver, filter);
  }

  @Override protected boolean isShowBack() {
    return false;
  }

  @Override public Fragment getSettingsFragment() {
    mMainFragment = new MainFragment();
    return mMainFragment;
  }

  private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
    @Override public void onReceive(Context context, Intent intent) {
      if (isFinishing()) {
        return;
      }
      String action = intent.getAction();
      Log.d("MainActivity", "receive-->" + action);
      if (Config.ACTION_DA_TI_SERVICE_CONNECT.equals(action)) {
        if (mTipsDialog != null) {
          mTipsDialog.dismiss();
        }
      } else if (Config.ACTION_DA_TI_SERVICE_DISCONNECT.equals(action)) {
        showOpenAccessibilityServiceDialog();
      }
    }
  };

  @Override protected void onResume() {
    super.onResume();
    if (DaTiService.isRunning()) {
      if (mTipsDialog != null) {
        mTipsDialog.dismiss();
      }
    } else {
      Toast.makeText(this, "您还没有开启辅助服务哦，赶紧开启吧少年～", Toast.LENGTH_LONG).show();
    }
  }

  @Override protected void onPause() {
    super.onPause();
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    try {
      unregisterReceiver(mBroadcastReceiver);
    } catch (Exception e) {
    }
    mTipsDialog = null;
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {

    MenuItem item = menu.add(0, 0, 1, R.string.open_service_button);
    item.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER);

    MenuItem notifyitem = menu.add(0, 3, 2, R.string.dati_page);
    notifyitem.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER);
    //
    MenuItem about = menu.add(0, 4, 4, R.string.about_title);
    about.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER);

    return super.onCreateOptionsMenu(menu);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case 0:
        openAccessibilityServiceSettings();
        DaTiApplication.eventStatistics(this, "menu_service");
        break;
      case 3:
        startDaTiPage();
        break;
      case 4:
        startActivity(new Intent(this, AboutMeActivity.class));
        DaTiApplication.eventStatistics(this, "menu_about");
        break;
    }
    return super.onOptionsItemSelected(item);
  }

  public void startDaTiPage() {
    DaTiApplication.eventStatistics(MainActivity.this, "float_window");
    boolean isPermission = FloatPermissionManager.getInstance().applyFloatWindow(MainActivity.this);
    //有对应权限或者系统版本小于7.0
    if (isPermission || Build.VERSION.SDK_INT < 24) {
      //开启悬浮窗
      FloatActionController.getInstance().startMonkServer(MainActivity.this);
      DaTiApplication.getHandler().postDelayed(new Runnable() {
        @Override public void run() {
          //启动服务需要时间，不然点击时间捕捉不了
          FloatActionController.getInstance()
              .setOnClickCloseListener(new FloatLayout.OnClickCloseListener() {
                @Override public void onClickClose() {
                  AnswersHelper.stopDynamicPoll();
                  DaTiApplication.setZhiBoType(-1);
                }
              });
        }
      }, 500);

      AnswersHelper.startDynamicPoll();
    }
  }

  /** 显示未开启辅助服务的对话框 */
  private void showOpenAccessibilityServiceDialog() {
    if (mTipsDialog != null && mTipsDialog.isShowing()) {
      return;
    }
    View view = getLayoutInflater().inflate(R.layout.dialog_tips_layout, null);
    view.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        openAccessibilityServiceSettings();
      }
    });
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle(R.string.open_service_title);
    builder.setView(view);
    builder.setPositiveButton(R.string.open_service_button, new DialogInterface.OnClickListener() {
      @Override public void onClick(DialogInterface dialog, int which) {
        openAccessibilityServiceSettings();
      }
    });
    mTipsDialog = builder.show();
  }

  /** 打开辅助服务的设置 */
  private void openAccessibilityServiceSettings() {
    try {
      Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
      startActivity(intent);
      Toast.makeText(this, R.string.tips, Toast.LENGTH_LONG).show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static class MainFragment extends Fragment {

    @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
      return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);
      view.findViewById(R.id.tv_service).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          ((MainActivity) getActivity()).showOpenAccessibilityServiceDialog();
          DaTiApplication.eventStatistics(getActivity(), "open_service");
        }
      });
      view.findViewById(R.id.tv_open_window).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          ((MainActivity) getActivity()).startDaTiPage();
        }
      });

      view.findViewById(R.id.iv_banner).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          Intent intent = new Intent();
          intent.setAction("android.intent.action.VIEW");
          Uri content_url = Uri.parse(Config.DOWNLOAD_URL);
          intent.setData(content_url);
          startActivity(intent);
        }
      });
    }
  }
}
