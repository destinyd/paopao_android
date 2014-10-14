package com.realityandapp.paopao_customer;

import android.app.ActivityManager;
import android.app.Application;
import android.content.*;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;
import com.easemob.EMCallBack;
import com.easemob.chat.*;
import com.easemob.exceptions.EaseMobException;
import com.easemob.util.EMLog;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.realityandapp.paopao_customer.models.test.im.DbOpenHelper;
import com.realityandapp.paopao_customer.models.test.im.IMUser;
import com.realityandapp.paopao_customer.models.test.im.IMUserDao;
import com.realityandapp.paopao_customer.utils.im.PreferenceUtils;
import com.realityandapp.paopao_customer.views.im.ChatActivity;
import com.realityandapp.paopao_customer.views.im.MainActivity;

import java.util.*;

/**
 * Created by dd on 14-9-18.
 */
public class PaopaoCustomerApplication extends Application {
    private static Context applicationContext;
    private static PaopaoCustomerApplication instance;

    // login user name
    public final String PREF_USERNAME = "username";
    private String userName = null;
    // login password
    private static final String PREF_PWD = "pwd";
    private String password = null;
    private Map<String, IMUser> contactList;
    /**
     * 当前用户nickname,为了苹果推送不是userid而是昵称
     */
    public static String currentUserNick = "";

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        applicationContext = this.getApplicationContext();

        init_image_config();

        init_im();
//        register_receive();
//        login();
    }

    private void register_receive() {
        NewMessageBroadcastReceiver msgReceiver = new NewMessageBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(EMChatManager.getInstance().getNewMessageBroadcastAction());
        intentFilter.setPriority(3);
        registerReceiver(msgReceiver, intentFilter);

    }

    // 消息提示需要自己写 因为需要在非Chat界面也有提示
    private class NewMessageBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //消息id
            String msgId = intent.getStringExtra("msgid");
            //发消息的人的username(userid)
            String msgFrom = intent.getStringExtra("from");
            //消息类型，文本，图片，语音消息等,这里返回的值为msg.type.ordinal()。
            //所以消息type实际为是enum类型
            int msgType = intent.getIntExtra("type", 0);
            //消息body，为一个json字符串
            String msgBody = intent.getStringExtra("body");
            Log.d("main", "new message id:" + msgId + " from:" + msgFrom + " type:" + msgType + " body:" + msgBody);

            //更方便的方法是通过msgId直接获取整个message
            EMMessage message = EMChatManager.getInstance().getMessage(msgId);

        }
    }

    private void init_im() {

        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        System.out.println("processAppName:" + processAppName);
        // 如果使用到百度地图或者类似启动remote service的第三方库，这个if判断不能少
        if (processAppName == null || processAppName.equals("")) {
            // workaround for baidu location sdk
            // 百度定位sdk，定位服务运行在一个单独的进程，每次定位服务启动的时候，都会调用application::onCreate
            // 创建新的进程。
            // 但环信的sdk只需要在主进程中初始化一次。 这个特殊处理是，如果从pid 找不到对应的processInfo
            // processName，
            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }
        applicationContext = this;
        instance = this;
        EMChat.getInstance().setDebugMode(true);
        // 初始化环信SDK,一定要先调用init()
        EMChat.getInstance().init(applicationContext);
        Log.d("EMChat Demo", "initialize EMChat SDK");
        // debugmode设为true后，就能看到sdk打印的log了

        // 获取到EMChatOptions对象
        EMChatOptions options = EMChatManager.getInstance().getChatOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        // todo true don't need accept
        options.setAcceptInvitationAlways(true);
        // 设置收到消息是否有声音提示，默认为true
        options.setNoticeBySound(PreferenceUtils.getInstance(applicationContext).getSettingMsgSound());
        // 设置收到消息是否震动 默认为true
        options.setNoticedByVibrate(PreferenceUtils.getInstance(applicationContext).getSettingMsgVibrate());
        // 设置语音消息播放是否设置为扬声器播放 默认为true
        options.setUseSpeaker(PreferenceUtils.getInstance(applicationContext).getSettingMsgSpeaker());
        // 设置notification消息点击时，跳转的intent为自定义的intent
        options.setOnNotificationClickListener(new OnNotificationClickListener() {

            @Override
            public Intent onNotificationClick(EMMessage message) {
                Intent intent = new Intent(applicationContext, ChatActivity.class);
                EMMessage.ChatType chatType = message.getChatType();
                if (chatType == EMMessage.ChatType.Chat) { // 单聊信息
                    intent.putExtra("userId", message.getFrom());
                    intent.putExtra("chatType", ChatActivity.CHATTYPE_SINGLE);
                } else { // 群聊信息
                    // message.getTo()为群聊id
                    intent.putExtra("groupId", message.getTo());
                    intent.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
                }
                return intent;
            }
        });

        // 设置收到消息是否有新消息通知，默认为true
        options.setNotificationEnable(PreferenceUtils.getInstance(applicationContext).getSettingMsgNotification());
        options.setShowNotificationInBackgroud(true);

        // 没有作用
//		// 取消注释，app在后台，有新消息来时，状态栏的消息提示换成自己写的
//        options.setNotifyText(new OnMessageNotifyListener() {
//
//            @Override
//            public String onNewMessageNotify(EMMessage message) {
//                System.out.println("notify onNewMessageNotify message:" + message);
//                // 可以根据message的类型提示不同文字(可参考微信或qq)，demo简单的覆盖了原来的提示
//                return "你的好基友" + message.getFrom() + "发来了一条消息哦";
//            }
//
//            @Override
//            public String onLatestMessageNotify(EMMessage message, int fromUsersNum, int messageNum) {
//                System.out.println("notify onLatestMessageNotify message:" + message);
//                System.out.println("notify onLatestMessageNotify fromUsersNum:" + fromUsersNum);
//                System.out.println("notify onLatestMessageNotify messageNum:" + messageNum);
//                return fromUsersNum + "个基友，发来了" + messageNum + "条消息";
//            }
//
//            @Override
//            public String onSetNotificationTitle(EMMessage message) {
//                //修改标题
//                return "环信notification";
//            }
//
//
//        });

        // 设置一个connectionlistener监听账户重复登陆
        EMChatManager.getInstance().addConnectionListener(new MyConnectionListener());
    }

    private void login() {
        final String username = "test1";
        final String password = "123456";
        // 调用sdk登陆方法登陆聊天服务器
        EMChatManager.getInstance().login(username, password, new EMCallBack() {

            @Override
            public void onSuccess() {
                // 登陆成功，保存用户名密码
                PaopaoCustomerApplication.getInstance().setUserName(username);
                PaopaoCustomerApplication.getInstance().setPassword(password);
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, final String message) {
                Toast.makeText(getApplicationContext(), "登录失败: " + message, 0).show();
            }
        });
//        try {
//            EMContactManager.getInstance().addContact("test1", "add");
//        } catch (EaseMobException e) {
//            e.printStackTrace();
//        }


    }

    private void init_image_config() {
        DisplayImageOptions options;
        ImageLoaderConfiguration config;

        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

        config = new ImageLoaderConfiguration.Builder(getApplicationContext())

                // 设置缓存图片的宽度跟高度
                .memoryCacheExtraOptions(480, 800)
                .diskCacheExtraOptions(480, 800, null)

                        // 通过 LruMemoryCache 实现缓存机制
//                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
//                .memoryCacheSize(2 * 1024 * 1024)

                        // 限制缓存文件数量百分比
                .memoryCacheSizePercentage(13)

                .diskCacheSize(50 * 1024 * 1024)

                        // 硬盘缓存文件数量
                .diskCacheFileCount(100)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .defaultDisplayImageOptions(options)


                .build();

        ImageLoader.getInstance().init(config);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public static Context get_context() {
        return applicationContext;
    }

    public static PaopaoCustomerApplication getInstance() {
        return instance;
    }

    // List<String> list = new ArrayList<String>();
    // list.add("1406713081205");
    // options.setReceiveNotNoifyGroup(list);

    /**
     * 获取内存中好友user list
     *
     * @return
     */
    public Map<String, IMUser> getContactList() {
        if (getUserName() != null && contactList == null) {
            IMUserDao dao = new IMUserDao(applicationContext);
            // 获取本地好友user list到内存,方便以后获取好友list
            contactList = dao.getContactList();
        }
        return contactList;
    }

    /**
     * 设置好友user list到内存中
     *
     * @param contactList
     */
    public void setContactList(Map<String, IMUser> contactList) {
        this.contactList = contactList;
    }

    public void setStrangerList(Map<String, IMUser> List) {

    }

    /**
     * 获取当前登陆用户名
     *
     * @return
     */
    public String getUserName() {
        if (userName == null) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
            userName = preferences.getString(PREF_USERNAME, null);
        }
        return userName;
    }

    /**
     * 获取密码
     *
     * @return
     */
    public String getPassword() {
        if (password == null) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
            password = preferences.getString(PREF_PWD, null);
        }
        return password;
    }

    /**
     * 设置用户名
     *
     * @param username
     */
    public void setUserName(String username) {
        if (username != null) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
            SharedPreferences.Editor editor = preferences.edit();
            if (editor.putString(PREF_USERNAME, username).commit()) {
                userName = username;
            }
        }
    }

    /**
     * 设置密码 下面的实例代码 只是demo，实际的应用中需要加password 加密后存入 preference 环信sdk
     * 内部的自动登录需要的密码，已经加密存储了
     *
     * @param pwd
     */
    public void setPassword(String pwd) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        SharedPreferences.Editor editor = preferences.edit();
        if (editor.putString(PREF_PWD, pwd).commit()) {
            password = pwd;
        }
    }

    /**
     * 退出登录,清空数据
     */
    public void logout() {
        // 先调用sdk logout，在清理app中自己的数据
        EMChatManager.getInstance().logout();
        DbOpenHelper.getInstance(applicationContext).closeDB();
        // reset password to null
        setPassword(null);
        setContactList(null);

    }

    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(info.processName, PackageManager.GET_META_DATA));
                    // Log.d("Process", "Id: "+ info.pid +" ProcessName: "+
                    // info.processName +"  Label: "+c.toString());
                    // processName = c.toString();
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }

    class MyConnectionListener implements ConnectionListener {
        @Override
        public void onReConnecting() {
        }

        @Override
        public void onReConnected() {
        }

        @Override
        public void onDisConnected(String errorString) {
//            if (errorString != null && errorString.contains("conflict")) {
//                Intent intent = new Intent(applicationContext, MainActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra("conflict", true);
//                startActivity(intent);
//            }

        }

        @Override
        public void onConnecting(String progress) {

        }

        @Override
        public void onConnected() {
        }
    }
}
