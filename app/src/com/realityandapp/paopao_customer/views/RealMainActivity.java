package com.realityandapp.paopao_customer.views;

import android.app.AlertDialog;
import android.content.*;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.easemob.chat.*;
import com.easemob.util.HanziToPinyin;
import com.easemob.util.NetUtils;
import com.readystatesoftware.viewbadger.BadgeView;
import com.realityandapp.paopao_customer.IMConstant;
import com.realityandapp.paopao_customer.PaopaoCustomerApplication;
import com.realityandapp.paopao_customer.R;
import com.realityandapp.paopao_customer.models.User;
import com.realityandapp.paopao_customer.models.test.im.IMUser;
import com.realityandapp.paopao_customer.models.test.im.IMUserDao;
import com.realityandapp.paopao_customer.models.test.im.InviteMessage;
import com.realityandapp.paopao_customer.models.test.im.InviteMessgeDao;
import com.realityandapp.paopao_customer.views.base.PaopaoBaseIncludeDrawerActivity;
import com.realityandapp.paopao_customer.views.fragment.ShopsFragment;
import com.realityandapp.paopao_customer.views.im.ChatAllHistoryFragment;
import com.realityandapp.paopao_customer.widget.FontAwesomeButton;
import roboguice.inject.InjectView;

/**
 * Created by dd on 14-9-18.
 */
public class RealMainActivity extends PaopaoBaseIncludeDrawerActivity {
    @InjectView(R.id.fabtn_messages)
    FontAwesomeButton fabtn_messages;
    @InjectView(R.id.ll_shops)
    LinearLayout ll_shops;
    @InjectView(R.id.ll_messages)
    LinearLayout ll_messages;

    private ChatAllHistoryFragment chatHistoryFragment;
    private ShopsFragment shopsFragment;

    private Fragment[] fragments;
    private int index;
    private RelativeLayout[] tab_containers;
    // 当前fragment的index
    private int currentTabIndex;
    private NewMessageBroadcastReceiver msgReceiver;
    // 账号在别处登录
    private boolean isConflict = false;
    private BadgeView badge;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        init_views();
        init_im();
    }

    private void init_im() {
        inviteMessgeDao = new InviteMessgeDao(this);
        userDao = new IMUserDao(this);
        //这个fragment只显示好友和群组的聊天记录
//		chatHistoryFragment = new ChatHistoryFragment();
        //显示所有人消息记录的fragment
        shopsFragment = new ShopsFragment();
        chatHistoryFragment = new ChatAllHistoryFragment();
        fragments = new Fragment[]{shopsFragment, chatHistoryFragment};
        // 添加显示第一个fragment
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, chatHistoryFragment)
                .add(R.id.fragment_container, shopsFragment)
                .hide(chatHistoryFragment)
                .show(shopsFragment)
                .commit();

        // 注册一个接收消息的BroadcastReceiver
        msgReceiver = new NewMessageBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(EMChatManager.getInstance().getNewMessageBroadcastAction());
        intentFilter.setPriority(3);
        registerReceiver(msgReceiver, intentFilter);

        // 注册一个ack回执消息的BroadcastReceiver
        IntentFilter ackMessageIntentFilter = new IntentFilter(EMChatManager.getInstance()
                .getAckMessageBroadcastAction());
        ackMessageIntentFilter.setPriority(3);
        registerReceiver(ackMessageReceiver, ackMessageIntentFilter);

        // 注册一个离线消息的BroadcastReceiver
        IntentFilter offlineMessageIntentFilter = new IntentFilter(EMChatManager.getInstance()
                .getOfflineMessageBroadcastAction());
        registerReceiver(offlineMessageReceiver, offlineMessageIntentFilter);

        // setContactListener监听联系人的变化等
//		EMContactManager.getInstance().setContactListener(new MyContactListener());
        // 注册一个监听连接状态的listener
        EMChatManager.getInstance().addConnectionListener(new MyConnectionListener());
        // 注册群聊相关的listener
//		EMGroupManager.getInstance().addGroupChangeListener(new MyGroupChangeListener());
        // 通知sdk，UI 已经初始化完毕，注册了相应的receiver和listener, 可以接受broadcast了
        EMChat.getInstance().setAppInited();
    }

    /**
     * 初始化组件
     */
    private void init_views() {
//		unreadLabel = (TextView) findViewById(R.id.unread_msg_number);
//		unreadAddressLable = (TextView) findViewById(R.id.unread_address_number);
//		mTabs = new Button[3];
//		mTabs[0] = (Button) findViewById(R.id.btn_conversation);
//		mTabs[1] = (Button) findViewById(R.id.btn_address_list);
//		mTabs[2] = (Button) findViewById(R.id.btn_setting);
//		// 把第一个tab设为选中状态
//		mTabs[0].setSelected(true);

        findViewById(R.id.fabtn_messages).setOnClickListener(this);
        findViewById(R.id.fabtn_shops).setOnClickListener(this);
        badge = new BadgeView(this, fabtn_messages);
        badge.setTextSize(getResources().getDimensionPixelSize(R.dimen.cart_badge_text_size));
    }

    /**
     * button点击事件
     */
//	public void onTabClicked(View view) {
//		switch (view.getId()) {
//		case R.id.btn_conversation:
//			index = 0;
//			break;
////		case R.id.btn_address_list:
////			index = 1;
////			break;
////		case R.id.btn_setting:
////			index = 2;
////			break;
//        }
//		if (currentTabIndex != index) {
//			FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
//			trx.hide(fragments[currentTabIndex]);
//			if (!fragments[index].isAdded()) {
//				trx.add(R.id.fragment_container, fragments[index]);
//			}
//			trx.show(fragments[index]).commit();
//		}
//		mTabs[currentTabIndex].setSelected(false);
//		// 把当前tab设为选中状态
//		mTabs[index].setSelected(true);
//		currentTabIndex = index;
//	}
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 注销广播接收者
        try {
            unregisterReceiver(msgReceiver);
        } catch (Exception e) {
        }
        try {
            unregisterReceiver(ackMessageReceiver);
        } catch (Exception e) {
        }
        try {
            unregisterReceiver(offlineMessageReceiver);
        } catch (Exception e) {
        }

        if (conflictBuilder != null) {
            conflictBuilder.create().dismiss();
            conflictBuilder = null;
        }

    }

//	/**
//	 * 刷新未读消息数
//	 */
//	public void updateUnreadLabel() {
//		int count = getUnreadMsgCountTotal();
//		if (count > 0) {
//			unreadLabel.setText(String.valueOf(count));
//			unreadLabel.setVisibility(View.VISIBLE);
//		} else {
//			unreadLabel.setVisibility(View.INVISIBLE);
//		}
//	}

//	/**
//	 * 刷新申请与通知消息数
//	 */
//	public void updateUnreadAddressLable() {
//		runOnUiThread(new Runnable() {
//			public void run() {
//				int count = getUnreadAddressCountTotal();
//				if (count > 0) {
//					unreadAddressLable.setText(String.valueOf(count));
//					unreadAddressLable.setVisibility(View.VISIBLE);
//				} else {
//					unreadAddressLable.setVisibility(View.INVISIBLE);
//				}
//			}
//		});
//
//	}

    /**
     * 获取未读申请与通知消息
     *
     * @return
     */
    public int getUnreadAddressCountTotal() {
        int unreadAddressCountTotal = 0;
        if (PaopaoCustomerApplication.getInstance().getContactList().get(IMConstant.NEW_FRIENDS_USERNAME) != null)
            unreadAddressCountTotal = PaopaoCustomerApplication.getInstance().getContactList().get(IMConstant.NEW_FRIENDS_USERNAME)
                    .getUnreadMsgCount();
        return unreadAddressCountTotal;
    }

    /**
     * 获取未读消息数
     *
     * @return
     */
    public int getUnreadMsgCountTotal() {
        int unreadMsgCountTotal = 0;
        unreadMsgCountTotal = EMChatManager.getInstance().getUnreadMsgsCount();
        return unreadMsgCountTotal;
    }

    /**
     * 新消息广播接收者
     */
    private class NewMessageBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //主页面收到消息后，主要为了提示未读，实际消息内容需要到chat页面查看

            // 消息id
            String msgId = intent.getStringExtra("msgid");
            // 收到这个广播的时候，message已经在db和内存里了，可以通过id获取mesage对象
            EMMessage message =
                    EMChatManager.getInstance().getMessage(msgId);

            // 刷新bottom bar消息未读数
//			updateUnreadLabel();
//            if (currentTabIndex == 1) {
            // 当前页面如果为聊天历史页面，刷新此页面
            if (chatHistoryFragment != null) {
                chatHistoryFragment.refresh();
            }
//            } else if (currentTabIndex == 0) {
            refresh_unread_msgs_count();
//            }
            // 注销广播，否则在ChatActivity中会收到这个广播
            abortBroadcast();
        }
    }

    private void refresh_unread_msgs_count() {
        set_messages_count(EMChatManager.getInstance().getUnreadMsgsCount());
    }

    /**
     * 消息回执BroadcastReceiver
     */
    private BroadcastReceiver ackMessageReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String msgid = intent.getStringExtra("msgid");
            String from = intent.getStringExtra("from");
            EMConversation conversation = EMChatManager.getInstance().getConversation(from);
            if (conversation != null) {
                // 把message设为已读
                EMMessage msg = conversation.getMessage(msgid);
                if (msg != null) {
                    msg.isAcked = true;
                }
            }
            abortBroadcast();
        }
    };

    /**
     * 离线消息BroadcastReceiver
     * sdk 登录后，服务器会推送离线消息到client，这个receiver，是通知UI 有哪些人发来了离线消息
     * UI 可以做相应的操作，比如下载用户信息
     */
    private BroadcastReceiver offlineMessageReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String[] users = intent.getStringArrayExtra("fromuser");
            String[] groups = intent.getStringArrayExtra("fromgroup");
            if (users != null) {
                for (String user : users) {
                    System.out.println("收到user离线消息：" + user);
                }
            }
            if (groups != null) {
                for (String group : groups) {
                    System.out.println("收到group离线消息：" + group);
                }
            }
            abortBroadcast();
        }
    };

    private InviteMessgeDao inviteMessgeDao;
    private IMUserDao userDao;

//	/***
//	 * 好友变化listener
//	 *
//	 */
//	private class MyContactListener implements EMContactListener {
//
//		@Override
//		public void onContactAdded(List<String> usernameList) {
//			// 保存增加的联系人
//			Map<String, IMUser> localUsers = DemoApplication.getInstance().getContactList();
//			Map<String, IMUser> toAddUsers = new HashMap<String, IMUser>();
//			for (String username : usernameList) {
//				IMUser user = setUserHead(username);
//				// 暂时有个bug，添加好友时可能会回调added方法两次
//				if (!localUsers.containsKey(username)) {
//					userDao.saveContact(user);
//				}
//				toAddUsers.put(username, user);
//			}
//			localUsers.putAll(toAddUsers);
//			// 刷新ui
//			if (currentTabIndex == 1)
//				contactListFragment.refresh();
//
//		}
//
//
//		@Override
//		public void onContactDeleted(final List<String> usernameList) {
//			// 被删除
//			Map<String, IMUser> localUsers = DemoApplication.getInstance().getContactList();
//			for (String username : usernameList) {
//				localUsers.remove(username);
//				userDao.deleteContact(username);
//				inviteMessgeDao.deleteMessage(username);
//			}
//			runOnUiThread(new Runnable() {
//				public void run() {
//					//如果正在与此用户的聊天页面
//					if (ChatActivity.activityInstance != null && usernameList.contains(ChatActivity.activityInstance.getToChatUsername())) {
//						Toast.makeText(MainActivity.this, ChatActivity.activityInstance.getToChatUsername() + "已把你从他好友列表里移除", 1).show();
//						ChatActivity.activityInstance.finish();
//					}
//					updateUnreadLabel();
//				}
//			});
//			// 刷新ui
//			if (currentTabIndex == 1)
//				contactListFragment.refresh();
//
//		}
//
//		@Override
//		public void onContactInvited(String username, String reason) {
//			// 接到邀请的消息，如果不处理(同意或拒绝)，掉线后，服务器会自动再发过来，所以客户端不要重复提醒
//			List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();
//			for (InviteMessage inviteMessage : msgs) {
//				if (inviteMessage.getGroupId() == null && inviteMessage.getFrom().equals(username)) {
//					return;
//				}
//			}
//			// 自己封装的javabean
//			InviteMessage msg = new InviteMessage();
//			msg.setFrom(username);
//			msg.setTime(System.currentTimeMillis());
//			msg.setReason(reason);
//			Log.d(TAG, username + "请求加你为好友,reason: " + reason);
//			// 设置相应status
//			msg.setStatus(InviteMessage.InviteMesageStatus.BEINVITEED);
//			notifyNewIviteMessage(msg);
//
//		}
//
//		@Override
//		public void onContactAgreed(String username) {
//			List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();
//			for (InviteMessage inviteMessage : msgs) {
//				if (inviteMessage.getFrom().equals(username)) {
//					return;
//				}
//			}
//			// 自己封装的javabean
//			InviteMessage msg = new InviteMessage();
//			msg.setFrom(username);
//			msg.setTime(System.currentTimeMillis());
//			Log.d(TAG, username + "同意了你的好友请求");
//			msg.setStatus(InviteMessage.InviteMesageStatus.BEAGREED);
//			notifyNewIviteMessage(msg);
//
//		}
//
//		@Override
//		public void onContactRefused(String username) {
//			// 参考同意，被邀请实现此功能,demo未实现
//
//		}
//
//	}

    /**
     * 保存提示新消息
     *
     * @param msg
     */
//	private void notifyNewIviteMessage(InviteMessage msg) {
//		saveInviteMsg(msg);
//		// 提示有新消息
//		EMNotifier.getInstance(getApplicationContext()).notifyOnNewMsg();
//
//		// 刷新bottom bar消息未读数
//		updateUnreadAddressLable();
//		// 刷新好友页面ui
//		if (currentTabIndex == 1)
//			contactListFragment.refresh();
//	}

    /**
     * 保存邀请等msg
     *
     * @param msg
     */
    private void saveInviteMsg(InviteMessage msg) {
        // 保存msg
        inviteMessgeDao.saveMessage(msg);
        // 未读数加1
        IMUser user = PaopaoCustomerApplication.getInstance().getContactList().get(IMConstant.NEW_FRIENDS_USERNAME);
        user.setUnreadMsgCount(user.getUnreadMsgCount() + 1);
    }


    /**
     * set head
     *
     * @param username
     * @return
     */
    IMUser setUserHead(String username) {
        IMUser user = new IMUser();
        user.setUsername(username);
        String headerName = null;
        if (!TextUtils.isEmpty(user.getNick())) {
            headerName = user.getNick();
        } else {
            headerName = user.getUsername();
        }
        if (username.equals(IMConstant.NEW_FRIENDS_USERNAME)) {
            user.setHeader("");
        } else if (Character.isDigit(headerName.charAt(0))) {
            user.setHeader("#");
        } else {
            user.setHeader(HanziToPinyin.getInstance().get(headerName.substring(0, 1)).get(0).target.substring(
                    0, 1).toUpperCase());
            char header = user.getHeader().toLowerCase().charAt(0);
            if (header < 'a' || header > 'z') {
                user.setHeader("#");
            }
        }
        return user;
    }


    /**
     * 连接监听listener
     */
    private class MyConnectionListener implements ConnectionListener {

        @Override
        public void onConnected() {
            chatHistoryFragment.errorItem.setVisibility(View.GONE);
            refresh_unread_msgs_count();
        }

        @Override
        public void onDisConnected(String errorString) {
            if (errorString != null && errorString.contains("conflict")) {
                // 显示帐号在其他设备登陆dialog
                showConflictDialog();
            } else {
                chatHistoryFragment.errorItem.setVisibility(View.VISIBLE);
                if (NetUtils.hasNetwork(RealMainActivity.this))
                    chatHistoryFragment.errorText.setText("连接不到聊天服务器");
                else
                    chatHistoryFragment.errorText.setText("当前网络不可用，请检查网络设置");

            }
        }

        @Override
        public void onReConnected() {
            chatHistoryFragment.errorItem.setVisibility(View.GONE);
            refresh_unread_msgs_count();
        }

        @Override
        public void onReConnecting() {
        }

        @Override
        public void onConnecting(String progress) {
        }

    }

    /**
     * MyGroupChangeListener
     */
//	private class MyGroupChangeListener implements GroupChangeListener {
//
//		@Override
//		public void onInvitationReceived(String groupId, String groupName, String inviter, String reason) {
//			boolean hasGroup = false;
//			for(EMGroup group : EMGroupManager.getInstance().getAllGroups()){
//				if(group.getGroupId().equals(groupId)){
//					hasGroup = true;
//					break;
//				}
//			}
//			if(!hasGroup)
//				return;
//
//			// 被邀请
//			EMMessage msg = EMMessage.createReceiveMessage(Type.TXT);
//			msg.setChatType(ChatType.GroupChat);
//			msg.setFrom(inviter);
//			msg.setTo(groupId);
//			msg.setMsgId(UUID.randomUUID().toString());
//			msg.addBody(new TextMessageBody(inviter + "邀请你加入了群聊"));
//			// 保存邀请消息
//			EMChatManager.getInstance().saveMessage(msg);
//			// 提醒新消息
//			EMNotifier.getInstance(getApplicationContext()).notifyOnNewMsg();
//
//			runOnUiThread(new Runnable() {
//				public void run() {
//					updateUnreadLabel();
//					// 刷新ui
//					if (currentTabIndex == 0)
//						chatHistoryFragment.refresh();
//					if (CommonUtils.getTopActivity(MainActivity.this).equals(GroupsActivity.class.getName())) {
//						GroupsActivity.instance.onResume();
//					}
//				}
//			});
//
//		}
//
//		@Override
//		public void onInvitationAccpted(String groupId, String inviter, String reason) {
//
//		}
//
//		@Override
//		public void onInvitationDeclined(String groupId, String invitee, String reason) {
//
//		}
//
//		@Override
//		public void onUserRemoved(String groupId, String groupName) {
//			// 提示用户被T了，demo省略此步骤
//			// 刷新ui
//			runOnUiThread(new Runnable() {
//				public void run() {
//					try {
//						updateUnreadLabel();
//						if (currentTabIndex == 0)
//							chatHistoryFragment.refresh();
//						if (CommonUtils.getTopActivity(MainActivity.this).equals(GroupsActivity.class.getName())) {
//							GroupsActivity.instance.onResume();
//						}
//					} catch (Exception e) {
//						Log.e("###", "refresh exception " + e.getMessage());
//					}
//
//				}
//			});
//		}
//
//		@Override
//		public void onGroupDestroy(String groupId, String groupName) {
//			// 群被解散
//			// 提示用户群被解散,demo省略
//			// 刷新ui
//			runOnUiThread(new Runnable() {
//				public void run() {
//					updateUnreadLabel();
//					if (currentTabIndex == 0)
//						chatHistoryFragment.refresh();
//					if (CommonUtils.getTopActivity(MainActivity.this).equals(GroupsActivity.class.getName())) {
//						GroupsActivity.instance.onResume();
//					}
//				}
//			});
//
//		}
//
//		@Override
//		public void onApplicationReceived(String groupId, String groupName, String applyer, String reason) {
//			// 用户申请加入群聊
//			InviteMessage msg = new InviteMessage();
//			msg.setFrom(applyer);
//			msg.setTime(System.currentTimeMillis());
//			msg.setGroupId(groupId);
//			msg.setGroupName(groupName);
//			msg.setReason(reason);
//			Log.d(TAG, applyer + " 申请加入群聊：" + groupName);
//			msg.setStatus(InviteMesageStatus.BEAPPLYED);
//			notifyNewIviteMessage(msg);
//		}
//
//		@Override
//		public void onApplicationAccept(String groupId, String groupName, String accepter) {
//			//加群申请被同意
//			EMMessage msg = EMMessage.createReceiveMessage(Type.TXT);
//			msg.setChatType(ChatType.GroupChat);
//			msg.setFrom(accepter);
//			msg.setTo(groupId);
//			msg.setMsgId(UUID.randomUUID().toString());
//			msg.addBody(new TextMessageBody(accepter + "同意了你的群聊申请"));
//			// 保存同意消息
//			EMChatManager.getInstance().saveMessage(msg);
//			// 提醒新消息
//			EMNotifier.getInstance(getApplicationContext()).notifyOnNewMsg();
//
//			runOnUiThread(new Runnable() {
//				public void run() {
//					updateUnreadLabel();
//					// 刷新ui
//					if (currentTabIndex == 0)
//						chatHistoryFragment.refresh();
//					if (CommonUtils.getTopActivity(MainActivity.this).equals(GroupsActivity.class.getName())) {
//						GroupsActivity.instance.onResume();
//					}
//				}
//			});
//		}
//
//		@Override
//		public void onApplicationDeclined(String groupId, String groupName, String decliner, String reason) {
//			//加群申请被拒绝，demo未实现
//		}
//
//	}
    @Override
    protected void onResume() {
        super.onResume();
//        if (User.current() == null) {
//            startActivity(new Intent(this, SignInActivity.class));
//            return;
//        } else {
            shopsFragment.get_datas();
//        }
        if (!isConflict) {
//			updateUnreadLabel();
//			updateUnreadAddressLable();
            EMChatManager.getInstance().activityResumed();
        }

    }

    //    // 不给退出？
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            boolean b = moveTaskToBack(false);
//            System.out.println("b:" + b);
////            finish();
////            return true;
////            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    private AlertDialog.Builder conflictBuilder;
    private boolean isConflictDialogShow;

    /**
     * 显示帐号在别处登录dialog
     */
    private void showConflictDialog() {
        isConflictDialogShow = true;
        PaopaoCustomerApplication.getInstance().logout();

        if (!RealMainActivity.this.isFinishing()) {
            // clear up global variables
            try {
                if (conflictBuilder == null)
                    conflictBuilder = new android.app.AlertDialog.Builder(RealMainActivity.this);
                conflictBuilder.setTitle("下线通知");
                conflictBuilder.setMessage(R.string.connect_conflict);
                conflictBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        conflictBuilder = null;
                        finish();
//						startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        startActivity(new Intent(RealMainActivity.this, SignInActivity.class));

                    }
                });
                conflictBuilder.setCancelable(false);
                conflictBuilder.create().show();
                isConflict = true;
            } catch (Exception e) {
                Log.e("###", "---------color conflictBuilder error" + e.getMessage());
            }

        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (getIntent().getBooleanExtra("conflict", false) && !isConflictDialogShow)
            showConflictDialog();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabtn_messages:
//                Intent intent = new Intent(this, com.realityandapp.paopao_customer.views.im.MainActivity.class);
//                startActivity(intent);
                index = 1;
                break;
            case R.id.fabtn_shops:
                index = 0;
                refresh_unread_msgs_count();
                break;
            default:
                super.onClick(v);
                return;
        }
        if (currentTabIndex != index) {
            FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
            trx.hide(fragments[currentTabIndex]);
            if (!fragments[index].isAdded()) {
                trx.add(R.id.fragment_container, fragments[index]);
            }
            trx.show(fragments[index]).commit();

            toggle_actionbar(index == 0);
        }
        currentTabIndex = index;
    }

    private void toggle_actionbar(boolean b) {
        ll_messages.setVisibility(b ? View.VISIBLE : View.GONE);
        ll_shops.setVisibility(b ? View.GONE : View.VISIBLE);
        setTitle(b ? "你周围的餐厅" : "会话");
    }

    protected void set_messages_count(int count) {
        int show_count = count;
        if (show_count < 0)
            show_count = 0;
        if (show_count == 0)
            badge.hide();
        else {
            if (show_count > 99)
                badge.setText("99+");
            else
                badge.setText(String.valueOf(show_count));
            badge.show();
        }
    }
}