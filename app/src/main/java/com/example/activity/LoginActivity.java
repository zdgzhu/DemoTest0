package com.example.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.activity.base.BaseActivity;
import com.example.demotest.R;
import com.example.jpush.PushMessageActivity;
import com.example.manager.DialogManager;
import com.example.manager.UserManager;
import com.example.module.PushMessage;
import com.example.module.user.User;
import com.example.network.http.RequestCenter;
import com.example.network.mina.MinaService;
import com.example.okhttp.listener.DisposeDataListener;
import com.example.view.associatemail.MailBoxAssociateTokenizer;
import com.example.view.associatemail.MailBoxAssociateView;

public class LoginActivity extends BaseActivity implements View.OnClickListener{

    //自定义登录广播的action
    public static final String LOGIN_ACTION = "com.imooc.action.LOGIN_ACTION";

    /**
     * UI
     */
    private MailBoxAssociateView mUserNameAssociateView;
    private EditText mPasswordView;
    private TextView mLoginView;
    private ImageView mQQLoginView;//用来实现QQ登陆

    /**
     * data
     */
    private PushMessage mPushMessage;//推送过来的消息
    private boolean fromPush; //是否从推送到此页面

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_layout);

        initData();
        initView();


    }

    //主要是推送 PushManager
    private void initData() {
        Intent intent = getIntent();
        if (intent.hasExtra("pushMessage")) {
            mPushMessage = (PushMessage) intent.getSerializableExtra("pushMessage");
        }
        fromPush = intent.getBooleanExtra("fromPush", false);


    }

    private void initView() {
        mUserNameAssociateView = (MailBoxAssociateView) findViewById(R.id.associate_email_input);
        mPasswordView = (EditText) findViewById(R.id.login_input_password);
        mLoginView = (TextView) findViewById(R.id.login_button);
        mLoginView.setOnClickListener(this);

        String[] recommandMailBox = getResources().getStringArray(R.array.recommand_mailBox);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.item_associate_mail_list, R.id.tv_recommend_mail, recommandMailBox);
        mUserNameAssociateView.setAdapter(adapter);
        //设置输入多少字符之后就提示，默认值是2
//        mUserNameAssociateView.setThreshold(2);
        //setTokenizer(MultiAutoCompleteTextView.Tokenizer t)：用户正在输入时，tokenizer设置将用于确定文本相关范围内
        mUserNameAssociateView.setTokenizer(new MailBoxAssociateTokenizer());

        mQQLoginView = (ImageView) findViewById(R.id.iv_login_logo);
        mQQLoginView.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.login_button:
                login();
                break;

            case R.id.iv_login_logo:
                //获取第三方用户授权信息
                break;


        }

    }

    /**
     * 用户信息存入数据库，以使让用户一打开应用就是一个登陆过的状态
     */
    private void insertUserInfoIntoDB() {
    }


    /**
     * 发送登陆请求
     */
    private void login() {
        String userName = mUserNameAssociateView.getText().toString().trim();
        String password = mPasswordView.getText().toString().trim();
        if (TextUtils.isEmpty(userName)) {
            return;
        }
        if (TextUtils.isEmpty(password)) {
            return;
        }

        DialogManager.getInstance().showProgressDialog(this);
        RequestCenter.login(userName, password, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                DialogManager.getInstance().dismissProgressDialog();
                /**
                 * 这个部分可以封装起来，封装为到一个登陆流程类中
                 */
                User user = (User) responseObj;
                // 保存当前用户单例对象
                UserManager.getInstance().setUser(user);
                connectToServer();//长连接
                sendLoginBroadcast();//发送广播
                /**
                 * 还应该将用户信息存入数据库，这样可以保证用户打开应用后总是登陆状态
                 * 只有用户手动退出登陆时候，将用户数据从数据库中删除。
                 */
                insertUserInfoIntoDB();
                if (fromPush) {
                    Intent intent = new Intent(LoginActivity.this, PushMessageActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("pushMessage", mPushMessage);
                    startActivity(intent);
                }
                finish();//销毁当前登陆页面
            }

            @Override
            public void onFailure(Object reasonObj) {
                DialogManager.getInstance().dismissProgressDialog();
            }
        });


    }

    //启动长连接
    private void connectToServer() {
        Intent service = new Intent(LoginActivity.this, MinaService.class);
        startService(service);

    }

    //向整个应该发送登陆广播
    private void sendLoginBroadcast() {
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(LOGIN_ACTION));

    }








}
