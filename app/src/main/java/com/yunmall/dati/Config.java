package com.yunmall.dati;

import android.content.Context;
import android.content.SharedPreferences;

public class Config {

  public static final String ACTION_DA_TI_SERVICE_DISCONNECT =
      "com.yunmall.dati.ACCESSBILITY_DISCONNECT";
  public static final String ACTION_DA_TI_SERVICE_CONNECT =
      "com.yunmall.dati.ACCESSBILITY_CONNECT";


  public static final String PREFERENCE_NAME = "config";
  public static final String BASE_AO_LAI_URL = "https://www.bainianaolai.com";
  public static final String SOHU_BASE_URL = "https://h-ss.sohu.com";
  public static final String SOHU_GET_QUESTION_URL = "/hotspot/millionHero/getQuestion";
  public static final String DOWNLOAD_URL = "https://www.bainianaolai.com/download/androidImgLinkDownloadPage/channel/name";



  /**
   * 今日头条问题ID
   */
  public static final String TODAY_NEWS_QUESTION_ID = "com.ss.android.ugc.aweme:id/title";
  /**
   * 今日头条答案ID
   */
  public static final String TODAY_NEWS_ANSWER_ID = "com.ss.android.article.news:id/a68";

  /**
   * 映客问题ID
   */
  public static final String YING_KE_QUESTION_ID = "com.meelive.ingkee:id/tv_question";
  /**
   * 映客答案ID
   */
  public static final String YING_KE_NEWS_ANSWER_ID = "com.meelive.ingkee:id/tv_answer";

  /**
   * 芝士超人问题ID
   */
  public static final String ZHI_SHI_CHAO_REN_KE_QUESTION_ID = "com.inke.trivia:id/tv_question";
  /**
   * 芝士超人答案ID
   */
  public static final String ZHI_SHI_CHAO_REN_ANSWER_ID = "com.inke.trivia:id/tv_answer";


  private static Config current;

  public static synchronized Config getConfig(Context context) {
    if (current == null) {
      current = new Config(context.getApplicationContext());
    }
    return current;
  }

  private SharedPreferences preferences;
  private Context mContext;

  private Config(Context context) {
    mContext = context;
    preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
  }

}
