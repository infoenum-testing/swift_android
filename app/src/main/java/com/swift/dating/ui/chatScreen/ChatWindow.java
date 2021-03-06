package com.swift.dating.ui.chatScreen;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.swift.dating.R;
import com.swift.dating.callbacks.OnInAppInterface;
import com.swift.dating.callbacks.OnItemClickListener;
import com.swift.dating.callbacks.OnItemClickListenerType;
import com.swift.dating.model.requestmodel.DeluxeTokenCountModel;
import com.swift.dating.ui.chatScreen.adapter.ChatAdapter;
import com.swift.dating.ui.chatScreen.viewModel.ChatViewModel;
import com.swift.dating.model.requestmodel.ApplyTimeTokenRequest;
import com.swift.dating.model.requestmodel.MessageListRequestModel;
import com.swift.dating.common.CommonUtils;
import com.swift.dating.common.CommonDialogs;
import com.swift.dating.data.network.CallServer;
import com.swift.dating.data.network.Resource;
import com.swift.dating.model.BaseModel;
import com.swift.dating.model.ChatModel;
import com.swift.dating.model.ImageModel;
import com.swift.dating.model.requestmodel.ReportRequestModel;
import com.swift.dating.model.requestmodel.TimeTokenRequestModel;
import com.swift.dating.model.responsemodel.MessageListModel;
import com.swift.dating.model.responsemodel.ProfileOfUser;
import com.swift.dating.model.responsemodel.SuperLikeResponseModel;
import com.swift.dating.ui.base.BaseActivity;
import com.swift.dating.ui.homeScreen.HomeActivity;
import com.swift.dating.ui.homeScreen.viewmodel.HomeViewModel;
import com.swift.dating.ui.userCardScreen.UserCardActivity;
import com.swift.dating.websocket.WebSocketService;

import static com.swift.dating.common.AppConstants.LICENSE_KEY;

public class ChatWindow extends BaseActivity implements View.OnClickListener, OnItemClickListenerType,
        BillingProcessor.IBillingHandler, OnItemClickListener, OnInAppInterface, CommonDialogs.onClick, CommonDialogs.onProductConsume {

    private final ArrayList<ChatModel> chatList = new ArrayList<>();
    boolean isExpired;
    Long timeLeft = 0L;
    BroadcastReceiver chatReceiver;
    int API_CHAT = 1, API_LOAD_MORE = 2, API_SEND = 3;
    int id;
    ImageView ivBack;
    HomeViewModel homeViewModel;
    ChatViewModel chatViewModel;
    int selectedPosition;
    boolean fromOutSide;
    private Double price;
    private TextView tvCancel, tvUnMatch, tvReport, tvExpired, tvTime1;
    private TextView tvEditProfile;
    private EditText etMessage;
    private SimpleDraweeView ivUserImage, iv_userImage;
    private FloatingActionButton fab_send;
    private RecyclerView rv_chat;
    private ConstraintLayout parent;
    private LinearLayoutManager linearLayoutManager;
    private ImageView ivMenu, ivTime;
    private CoordinatorLayout bottomSheetGallery;
    private BottomSheetDialog mBottomSheetDialog;
    private ChatAdapter chatAdapter;
    private BillingProcessor bp;
    private ChatModel messageDetail;
    private String chatUserName;
    private String imageUrl;
    private int page = 1, blockByMe;
    private boolean showLoadMore;
    private boolean backPress;
    private String tokenSType;
    private String productId;
    private boolean isFromCard = false;
    private int tabPos = 3;
    private boolean isfromDirect = false;
    private boolean isFromSearch = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN, WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        this.getWindow().setStatusBarColor(this.getResources().getColor(R.color.primaryTextColor));
        setContentView(R.layout.activity_chat_window);
        initialize();
        implementListeners();
        id = getIntent().getIntExtra("id", 0);
        chatUserName = getIntent().getStringExtra("name");
        imageUrl = getIntent().getStringExtra("image");
        isExpired = getIntent().getBooleanExtra("isExpired", false);
        if (getIntent().hasExtra("timeLeft")) {
            timeLeft = getIntent().getLongExtra("timeLeft", 0);
        }
        if (getIntent().hasExtra("outside")) {
            fromOutSide = getIntent().getBooleanExtra("outside", false);
        }
        isFromCard = getIntent().getBooleanExtra("isFromCard", false);
        //fromOutSide = getIntent().getBooleanExtra("isFromCard", false);
        ivMenu.setVisibility(getIntent().getBooleanExtra("isFromCard", false) ? View.INVISIBLE : View.VISIBLE);
        if ((getIntent().hasExtra("tabPos"))) {
            tabPos = getIntent().getIntExtra("tabPos", 3);
        }
        if ((getIntent().hasExtra("isfromDirect"))) {
            isfromDirect = getIntent().getBooleanExtra("isfromDirect", false);
        }
        if ((getIntent().hasExtra("isFromSearch"))) {
            isFromSearch = getIntent().getBooleanExtra("isFromSearch", false);
        }
        clearNotification();
        subscribeModel();
        getData(getIntent());
        initialize();
        implementListeners();
        checkInternet(API_CHAT);
        registerBroadCast();
        initBillingProcess();
        sp.setChatUserId(id + "");
        chatViewModel.readStatusRequest(id);

        etMessage.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    if (chatList.size() > 0) {
                        rv_chat.scrollTo(0, chatList.size() - 1);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Lost the focus", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        chatList.clear();
        clearNotification();
        Bundle extra = intent.getExtras();
        if (extra != null) {
            if (extra.containsKey("id")) {
                id = getIntent().getIntExtra("id", 0);
            }
            if (extra.containsKey("image")) {
                imageUrl = getIntent().getStringExtra("image");
            }
            if (extra.containsKey("name")) {
                chatUserName = getIntent().getStringExtra("name");
                isExpired = false;
            }
            if (extra.containsKey("outside")) {
                fromOutSide = getIntent().getBooleanExtra("outside", false);
            }
            if (extra.containsKey("tabPos")) {
                tabPos = getIntent().getIntExtra("tabPos", 3);
            }
            if ((getIntent().hasExtra("isfromDirect"))) {
                isfromDirect = getIntent().getBooleanExtra("isfromDirect", false);
            }
        }
        checkInternet(API_CHAT);
        sp.setChatUserId(id + "");
        sp.setChatOpen(true);
    }

    /**
     * **  Method to Initialize Billing Process
     */
    private void initBillingProcess() {
        bp = new BillingProcessor(ChatWindow.this, LICENSE_KEY, this);
        bp.initialize();
    }

    /**
     * method to handle Api Response
     */
    private void subscribeModel() {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        chatViewModel = ViewModelProviders.of(this).get(ChatViewModel.class);
        homeViewModel.addDeluxeResponse().observe(this, new Observer<Resource<BaseModel>>() {
            @Override
            public void onChanged(Resource<BaseModel> resource) {
                if (resource == null)
                    return;
                switch (resource.status) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        hideLoading();
                        if (resource.data.getSuccess()) {
                            sp.saveDeluxe(true);
                            //setDeluxeData();
                           /* tvPremium.setVisibility(View.INVISIBLE);
                            tvUnlimitedView.setVisibility(View.INVISIBLE);*/
                        } else if (resource.code == 401) {
                            openActivityOnTokenExpire();
                        } else {
                            showSnackbar(ivUserImage, "Something went wrong");
                        }
                        break;
                    case ERROR:
                        hideLoading();
                        showSnackbar(ivUserImage, resource.message);
                        break;
                }

            }
        });
        homeViewModel.reportResponse().observe(this, new Observer<Resource<BaseModel>>() {

            @Override
            public void onChanged(@Nullable Resource<BaseModel> resource) {
                if (resource == null) {
                    return;
                }
                switch (resource.status) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        hideLoading();
                        hideKeyboard();
                        if (resource.data.getSuccess()) {
                            if (resource.data.getError() != null && resource.data.getError().getCode().contains("401")) {
                                openActivityOnTokenExpire();
                            } else {
                                showSnackbar(ivUserImage, "User successfully reported");
                            }
                        } else {
                            //showSnackBar(ivUserImage, resource.data.getMessage());
                            showSnackbar(ivUserImage, "User has already been reported.");
                        }
                        break;
                    case ERROR:
                        hideLoading();
                        showSnackbar(ivUserImage, resource.message);
                        break;
                }
            }
        });

        homeViewModel.unMatchResponse().observe(this, new Observer<Resource<BaseModel>>() {

            @Override
            public void onChanged(@Nullable Resource<BaseModel> resource) {
                if (resource == null) {
                    return;
                }
                switch (resource.status) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        hideLoading();
                        if (resource.data.getSuccess()) {
                            if (resource.data.getError() != null && resource.data.getError().getCode().contains("401")) {
                                openActivityOnTokenExpire();
                            } else {
                                bottomSheetGallery.setVisibility(View.GONE);
                                onBackPressed();
                            }
                        } else {
                            showSnackbar(ivUserImage, resource.message);
                        }
                        break;
                    case ERROR:
                        hideLoading();
                        showSnackbar(ivUserImage, resource.message);
                        break;
                }
            }
        });

        chatViewModel.readStatusResponse().observe(this, new Observer<Resource<BaseModel>>() {

            @Override
            public void onChanged(@Nullable Resource<BaseModel> resource) {
                if (resource == null) {
                    return;
                }
                switch (resource.status) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        hideLoading();
                        if (resource.data.getSuccess()) {
                            if (resource.data.getError() != null && resource.data.getError().getCode().contains("401")) {
                                openActivityOnTokenExpire();
                            } else {
                                Log.e("TAG", "onChanged: "+backPress );
                                if (backPress) {
                                    if (isFromSearch) {
                                        finish();
                                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                    } else  if (isFromCard) {
                                        //Log.e("TAG", "onChanged: isFromCard 338 " );
                                        //setResult(9898);
                                        finish();
                                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                    } else {
                                        Intent intent = new Intent(ChatWindow.this, HomeActivity.class);
                                        intent.putExtra("replace", tabPos);//3);
                                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                        startActivity(intent);
                                        finishAffinity();
                                    }
                                }
                            }
                        } else {
                            if (backPress) {
                                if (isFromSearch) {
                                    finish();
                                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                } else  if (isFromCard) {
                                    //Log.e("TAG", "onChanged: isFromCard 356 " );
                                    //setResult(9898);
                                    finish();
                                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                } else {
                                    Intent intent = new Intent(ChatWindow.this, HomeActivity.class);
                                    intent.putExtra("replace", tabPos);//3);
                                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                    startActivity(intent);
                                    finishAffinity();
                                }
                            }
                        }
                        break;
                    case ERROR:
                        hideLoading();
                        showSnackbar(ivUserImage, resource.message);
                        Log.e("TAG", "onChanged: "+backPress );
                        if (backPress) {
                            if (isFromSearch) {
                                finish();
                                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                            } else  if (isFromCard) {
                                //Log.e("TAG", "onChanged: isFromCard"+backPress );
                               // setResult(9898);
                                finish();
                                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                            } else {
                                Intent intent = new Intent(ChatWindow.this, HomeActivity.class);
                                intent.putExtra("replace", 3);
                                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                startActivity(intent);
                                finishAffinity();
                            }
                        }
                        break;
                }
            }
        });

        chatViewModel.chatMessageResponse().observe(this, new Observer<Resource<MessageListModel>>() {

            @Override
            public void onChanged(@Nullable Resource<MessageListModel> resource) {
                if (resource == null) {
                    return;
                }
                switch (resource.status) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        hideLoading();
                        hideKeyboard();
                        if (resource.data.getSuccess()) {
                            if (resource.data.getError() != null && resource.data.getError().getCode().contains("401")) {
                                openActivityOnTokenExpire();
                            } else {
                                chatList.addAll(resource.data.getChatModels());
                                /*if (chatList.size()==0)
                                    ivMenu.setVisibility(View.GONE);
                                else {
                                    ivMenu.setVisibility(View.VISIBLE);
                                }*/
                                chatAdapter = new ChatAdapter(ChatWindow.this, chatList,
                                        CallServer.BaseImage + imageUrl, ChatWindow.this);
                                rv_chat.setAdapter(chatAdapter);
                                rv_chat.scrollToPosition(chatList.size() - 1);
                                if (chatList.size() > 0) {
                                    tvExpired.setVisibility(View.GONE);
                                    tvTime1.setVisibility(View.GONE);
                                    iv_userImage.setVisibility(View.GONE);
                                }
                            }
                        } else {
                            showSnackbar(ivUserImage, "User Already Reported");
                        }
                        break;
                    case ERROR:
                        hideLoading();
                        showSnackbar(ivUserImage, resource.message);
                        break;
                }
            }
        });

        homeViewModel.timeTokenResponse().observe(this, new Observer<Resource<SuperLikeResponseModel>>() {
            @Override
            public void onChanged(@Nullable Resource<SuperLikeResponseModel> resource) {
                if (resource == null) {
                    return;
                }
                switch (resource.status) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        hideLoading();
                        if (resource.data.isSuccess()) {
                            Gson gson = new Gson();
                            String user = sp.getUser();
                            ProfileOfUser obj = gson.fromJson(user, ProfileOfUser.class);
                            Log.e("TAG", "onChanged: " + resource.data.getTotalTimeTokens());
                            obj.setTimeTokenCount(resource.data.getTotalTimeTokens() - 1);
                            sp.saveUserData(obj, sp.getProfileCompleted());
                            homeViewModel.useTimeToken(new ApplyTimeTokenRequest(id,
                                    24, 1));
                        } else if (resource.code == 401) {
                            openActivityOnTokenExpire();
                        } else {
                            showSnackbar(ivUserImage, "Something went wrong");
                        }
                        break;
                    case ERROR:
                        hideLoading();
                        showSnackbar(ivUserImage, resource.message);
                        break;
                }
            }
        });

        homeViewModel.useTimeTokenResponse().observe(this, new Observer<Resource<BaseModel>>() {

            @Override
            public void onChanged(@Nullable Resource<BaseModel> resource) {
                if (resource == null) {
                    return;
                }
                switch (resource.status) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        hideLoading();
                        if (resource.data.getSuccess()) {
                            if (resource.data.getError() != null && resource.data.getError().getCode().contains("401")) {
                                openActivityOnTokenExpire();
                            } else {
                                Gson gson = new Gson();
                                String user = sp.getUser();
                                ProfileOfUser obj = gson.fromJson(user, ProfileOfUser.class);
                                obj.setTimeTokenCount(obj.getTimeTokenCount() - 1);
                                sp.saveUserData(obj, sp.getProfileCompleted());
                                isExpired = false;
                                ivTime.setVisibility(View.INVISIBLE);
                            }
                        }
                        break;
                    case ERROR:
                        hideLoading();
                        showSnackbar(ivUserImage, resource.message);
                        break;
                }
            }
        });
    }

    /**
     * clear notification from stack
     */
    ///TODO uncomment
    private void clearNotification() {

    }

    /**
     * get data from intent bundle
     */
    private void getData(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null)
            messageDetail = (ChatModel) extras.getSerializable("messageDetail");
        else
            onBackPressed();
    }

    /**
     * initialize all the views
     */
    private void initialize() {
        tvEditProfile = findViewById(R.id.tv_editProfile);
        ivUserImage = findViewById(R.id.ivUserImage);
        iv_userImage = findViewById(R.id.iv_userImage);
        tvExpired = findViewById(R.id.tvTime);
        tvTime1 = findViewById(R.id.tvTime1);
        tvReport = findViewById(R.id.tvReport);
        tvUnMatch = findViewById(R.id.tvUnMatch);
        if (isfromDirect) {
            tvUnMatch.setText("Block");
        } else {
            tvUnMatch.setText("Unmatch");
        }
        tvCancel = findViewById(R.id.tvcancel);
        rv_chat = findViewById(R.id.rv_chat);
        parent = findViewById(R.id.parent);
        etMessage = findViewById(R.id.et_message);
        fab_send = findViewById(R.id.fab_send);
        ivBack = findViewById(R.id.ivback);
        ivMenu = findViewById(R.id.ivmenu);
        ivTime = findViewById(R.id.ivTime);
        bottomSheetGallery = findViewById(R.id.bottomSheetGallery);


        tvEditProfile.setText(chatUserName);
        CommonUtils.setImageUsingFresco(ivUserImage, CallServer.BaseImage + imageUrl, 10);
        CommonUtils.setImageUsingFresco(iv_userImage, CallServer.BaseImage + imageUrl, 4);


        linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        rv_chat.setLayoutManager(linearLayoutManager);

        if (isExpired) {
            ivTime.setVisibility(View.VISIBLE);
            iv_userImage.setVisibility(View.GONE);
        } else {
            ivTime.setVisibility(View.INVISIBLE);
            if (timeLeft > 0) {
                int hours = (int) (timeLeft / 60);
                int min = (int) (timeLeft % 60);
                tvExpired.setVisibility(View.VISIBLE);
                iv_userImage.setVisibility(View.VISIBLE);
                tvTime1.setVisibility(View.VISIBLE);
                if (hours > 1) {
                    tvExpired.setText("Time left: " + hours + " hours");
                } else if (hours == 1) {
                    tvExpired.setText("Time left: " + hours + " hour");
                } else {
                    tvExpired.setText("Time left: " + min + " minutes");
                }
            } else {
                iv_userImage.setVisibility(View.GONE);
            }
        }

    }

    /**
     * implement all listeners
     */
    private void implementListeners() {
        ivBack.setOnClickListener(this);
        ivUserImage.setOnClickListener(this);
//        iv_userImage.setOnClickListener(this);

        tvEditProfile.setOnClickListener(this);
        fab_send.setOnClickListener(this);
        ivMenu.setOnClickListener(this);
        tvReport.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
        tvUnMatch.setOnClickListener(this);
        rv_chat.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                int firstVisiblePosition = linearLayoutManager.findFirstVisibleItemPosition();
                if (firstVisiblePosition == 0 && showLoadMore)
                    checkInternet(API_LOAD_MORE);
            }
        });

        rv_chat.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            if (bottom < oldBottom) {
                rv_chat.postDelayed(() -> rv_chat.scrollToPosition(chatList.size() - 1), 100);
            }
        });

        etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                rv_chat.scrollToPosition(chatList.size() - 1);
            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                fab_send.setEnabled(s.toString().trim().length() > 0);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    /**
     * check internet before hitting api
     *
     * @param apiType tells which API is to call
     */
    private void checkInternet(int apiType) {
        if (isNetworkConnected()) {
            if (apiType == API_CHAT) {
                showLoading();
                page = 1;
                chatViewModel.chatMessageRequest(new MessageListRequestModel(sp.getUserId(), id + ""));
            } else if (apiType == API_LOAD_MORE) {
                showLoading();
                page++;
                //              chatViewModel.chatMessageRequest(new MessageListRequestModel(sp.getUserId(), id +""));
            } else if (apiType == API_SEND) {
                if (isExpired) {
                    hideKeyboard();
                    openDialog();
                } else {
                    Gson gson = new Gson();
                    String json = sp.getUser();
                    ProfileOfUser obj = gson.fromJson(json, ProfileOfUser.class);

                    String jsonImage = sp.getUserImage();
                    Type type = new TypeToken<List<ImageModel>>() {
                    }.getType();
                    List<ImageModel> imageList = gson.fromJson(jsonImage, type);
                    if (!isFromCard)
                        WebSocketService.getInstance().sendMessage(ChatWindow.this, sp.getUserId(), id + "", etMessage.getText().toString().trim(), imageList.get(0).getImageUrl(), obj.getName());
                    else {
                        if (obj.getDirectMessageCount() > 0 || sp.getDeluxe()) {
                            if (!sp.getDeluxe()) {
                                obj.setDirectMessageCount(obj.getDirectMessageCount() - 1);
                                sp.saveUserData(obj, null);
                            }
                            WebSocketService.getInstance().sendMessage(ChatWindow.this, sp.getUserId(), id + "", etMessage.getText().toString().trim(), imageList.get(0).getImageUrl(), obj.getName());
                        } else {
                            CommonDialogs.alertDialogDailyDelux(this, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    CommonDialogs.dismiss();
                                    CommonDialogs.DeluxePurChaseDialog(ChatWindow.this, ChatWindow.this);
                                }
                            });
                        }
                    }
                    tvExpired.setVisibility(View.GONE);
                    tvTime1.setVisibility(View.GONE);
                    iv_userImage.setVisibility(View.GONE);
                }
            }
        } else

            showSnackbar(ivBack, ChatWindow.this.getResources().getString(R.string.no_internet_error));

    }

    private void openDialog() {
        Gson gson = new Gson();
        String user = sp.getUser();
        ProfileOfUser obj = gson.fromJson(user, ProfileOfUser.class);
        if (obj.getTimeTokenCount() > 0) {
            CommonDialogs.alertDialogTwoButtons(ChatWindow.this, this, "This match has expired. Do you want to use a time token" +
                    " to add another 24 hours and enable chatting?");
        } else {
            CommonDialogs.TimeTokenPurChaseDialog(ChatWindow.this, ChatWindow.this);
            //CommonDialogs.purchaseDialog(ChatWindow.this, "Time Tokens", "This match has expired and you are out of Time Tokens. To extend time and chat, buy more tokens below.", this);
        }
    }

    @Override
    protected void onResume() {
        isChatScreen = true;
        super.onResume();
    }

    @Override
    protected void onPause() {
        isChatScreen = false;
        super.onPause();

    }

    /**
     * register broadcast for message received
     */
    private void registerBroadCast() {

        IntentFilter chatFilter = new IntentFilter();
        chatFilter.addAction("BGChat");

        // for date created
        chatReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getStringExtra("action");

                ChatModel chatModel = (ChatModel) intent.getSerializableExtra("chatModel");
                if (action.equalsIgnoreCase("0")) { // for date created
                    if (chatList != null && chatAdapter != null) {
                      if (chatList.isEmpty()){
                          ChatWindow.this.setResult(9898);
                      }
                        chatList.add(chatModel);
                        tvExpired.setVisibility(View.GONE);
                        tvTime1.setVisibility(View.GONE);
                        iv_userImage.setVisibility(View.GONE);
                        rv_chat.scrollToPosition(chatList.size() - 1);
                        chatAdapter.updateReceiptsList(chatList);
                    }
                }

                if (chatModel.getFrom_id().equalsIgnoreCase(sp.getUserId()))
                    etMessage.setText("");

            }
        };

        registerReceiver(chatReceiver, chatFilter);

        sp.setChatUserId(id + "");
        sp.setChatOpen(true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.iv_back:
            case R.id.ivback:
                if (bottomSheetGallery.getVisibility() == View.VISIBLE) {
                    bottomSheetGallery.setVisibility(View.GONE);
                } else {
                    onBackPressed();
                }
                break;
            case R.id.tv_yes:
                CommonDialogs.dismiss();
                Log.e("TAG", "onClick: " + id);
                Gson gson = new Gson();
                String json = sp.getUser();
                ProfileOfUser obj = gson.fromJson(json, ProfileOfUser.class);
                if (obj.getTimeTokenCount() > 0) {
                    homeViewModel.useTimeToken(new ApplyTimeTokenRequest(id, 24, 1));
                } else {
                    CommonDialogs.TimeTokenPurChaseDialog(mActivity, this);
                }

                break;
            case R.id.ivmenu:
                bottomSheetGallery.setVisibility(View.VISIBLE);
                break;
            case R.id.tvcancel:
            case R.id.view:
                hideBottomSheet();
                bottomSheetGallery.setVisibility(View.GONE);
                break;
            case R.id.tvReport:
                bottomSheetGallery.setVisibility(View.GONE);
                //reportDialog();
                // reportDialogNew();
                CommonDialogs.reportDialogNew(this, this);
                break;
            case R.id.tvUnMatch:
                bottomSheetGallery.setVisibility(View.GONE);
                Dialog dialog = new Dialog(mActivity);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.dialog_two_button);
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Window window = dialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.show();
                TextView tv_message = dialog.findViewById(R.id.tv_message);
                TextView tv_yes = dialog.findViewById(R.id.tv_yes);
                TextView tv_no = dialog.findViewById(R.id.tv_no);
                if (isfromDirect) {
                    tv_message.setText("Are you sure you want to block this user? They will not be able to send you any more messages.");
                } else
                    tv_message.setText("Are you sure you want to unmatch this user? Unmatching them will also block them from contacting you.");

                tv_yes.setOnClickListener(v -> {
                    dialog.dismiss();
                    showLoading();
                    homeViewModel.unMatchRequest(String.valueOf(id));
                });
                tv_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                break;

            case R.id.ivUserImage:
            case R.id.tv_editProfile:
                //if (!isFromCard)
                startActivity(new Intent(ChatWindow.this, UserCardActivity.class)
                        .putExtra("userid", id));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;

            case R.id.fab_send:
                if (!TextUtils.isEmpty(etMessage.getText().toString())) {
                    checkInternet(API_SEND);

                }
                break;

        }
    }
    /*

     */

    /**
     * Method for report Dialog
     */
    private void reportDialog() {
        Dialog dialog = new Dialog(mActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_edit_text);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView tv_message = dialog.findViewById(R.id.tv_message);
        EditText etReason = dialog.findViewById(R.id.et_reason);
        ImageView ivcross = dialog.findViewById(R.id.ivcross);
        TextView tv_ok = dialog.findViewById(R.id.btn_ok);
        tv_message.setText("Reason to Report User");
        ivcross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboardFromView(etReason);
                dialog.dismiss();
            }
        });
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(etReason.getText().toString())) {
                    showLoading();
                    hideKeyboardFromView(etReason);
                    dialog.dismiss();
                    homeViewModel.reportRequest(new ReportRequestModel(id, etReason.getText().toString()));
                    CommonDialogs.dismiss();
                } else {
                    Toast.makeText(ChatWindow.this, "Please enter the reason for report", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(chatReceiver);
        super.onDestroy();
    }

    /**
     * Method for report Dialog
     *//*

    private void reportDialogNew() {
        Dialog dialog = new Dialog(mActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_report);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout[] lls=new LinearLayout[6];
        ((TextView)dialog.findViewById(R.id.tv_photo)).setLayerType(View.LAYER_TYPE_SOFTWARE,null);
        MaskFilter mm=  ((TextView)dialog.findViewById(R.id.tv_photo)).getPaint().setMaskFilter(null);
        ((TextView)dialog.findViewById(R.id.tv_photo)).getPaint().setMaskFilter(mm);
        lls[0]=dialog.findViewById(R.id.ll_photo);
        lls[1]=dialog.findViewById(R.id.ll_content);
        lls[2]=dialog.findViewById(R.id.ll_age);
        lls[3]=dialog.findViewById(R.id.ll_stolen);
        lls[4]=dialog.findViewById(R.id.ll_behave);
        lls[5]=dialog.findViewById(R.id.ll_other);
        lls[0].setBackground(mActivity.getDrawable(R.drawable.img_rectangle_outline));
        lls[0].getBackground().setColorFilter(mActivity.getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        for (int i = 0; i < lls.length; i++) {
            int finalI = i;
            lls[i].setOnClickListener(v -> {
                for (int j = 0; j < lls.length; j++) {
                    lls[j].setBackground(mActivity.getDrawable(R.drawable.rounded_sheet));
                    lls[j].getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                }
                lls[finalI].setBackground(mActivity.getDrawable(R.drawable.img_rectangle_outline));
                lls[finalI].getBackground().setColorFilter(mActivity.getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                if (finalI==5)
                    reportDialog();
            });
        }
        Button btn_submit = dialog.findViewById(R.id.btn_submit);
        ImageView img_cancel = dialog.findViewById(R.id.img_cancel);
        btn_submit.setOnClickListener(v -> dialog.dismiss());
        img_cancel.setOnClickListener(v -> dialog.dismiss());
    }
*/
    @Override
    public void onBackPressed() {
        hideKeyboardFromView(etMessage);
        backPress = true;
        isChatScreen = false;
        LocalBroadcastManager.getInstance(this).unregisterReceiver(chatReceiver);
        if (chatList.size() > 0) {
            chatViewModel.readStatusRequest(id);
        } else {
            super.onBackPressed();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            //overridePendingTransition(R.anim.nothing, R.anim.slide_out_down);
        }
    }

    @Override
    public void OnItemClick(int position, int type) {

    }

    /*
     *** Method to Hide bottom sheet
     */
    private void hideBottomSheet() {
        if (mBottomSheetDialog != null && mBottomSheetDialog.isShowing())
            mBottomSheetDialog.dismiss();
    }

    @Override
    public void OnItemClick(int position) {
        ivUserImage.performClick();
    }

    @Override
    public void OnItemClick(int position, int type, String id) {
        selectedPosition = position;
        bp.purchase(ChatWindow.this, id);
    }

    @Override
    public void onBillingInitialized() {
        /*
         * Called when BillingProcessor was initialized and it's ready to purchase
         */
        if (CommonDialogs.vipTokenPriceList.size() == 0 || CommonDialogs.timeTokenPriceList.size() == 0 || CommonDialogs.crushTokenPriceList.size() == 0 || CommonDialogs.PremiumPriceList.size() == 0 || CommonDialogs.DeluxePriceList.size() == 0) {
            CommonDialogs.onBillingInitialized(bp);
        }
        CommonDialogs.setBilling(bp);
    }

    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {
        /*
         * Called when requested PRODUCT ID was successfully purchased
         */

       /* bp.consumePurchase(productId);
        double price = 2.99;
        if (productId.equalsIgnoreCase("time_token_3")) {
            price = 1.99;
        } else if (productId.equalsIgnoreCase("time_token_1")) {
            price = 0.99;
        }*/
        Log.e("TAG", "onProductPurchased: " + details);
        /*
         * Called when requested PRODUCT ID was successfully purchased
         */
        Toast.makeText(this, "Item Purchased", Toast.LENGTH_LONG).show();
        showLoading();
        if (tokenSType.equalsIgnoreCase("timeToken")) {
            bp.consumePurchase(productId);
            homeViewModel.addTimeToken(new TimeTokenRequestModel(selectedPosition, price));
        } else if (tokenSType.equalsIgnoreCase("DeluxePurChase")) {
            Toast.makeText(this, "Item Purchased", Toast.LENGTH_LONG).show();
            bp.consumePurchase(productId);
            homeViewModel.addDeluxeRequest(new DeluxeTokenCountModel("2", productId,
                    price,
                    selectedPosition,
                    details.purchaseInfo.purchaseData.orderId,
                    details.purchaseInfo.purchaseData.purchaseToken, CommonUtils.getDateForPurchase(details), details.purchaseInfo.signature,
                    details.purchaseInfo.purchaseData.purchaseState.toString()));
        }
        // homeViewModel.addTimeToken(new TimeTokenRequestModel(selectedPosition, price));


        Log.e("purchase success", details.purchaseInfo.responseData);
    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {
        /*
         * Called when some error occurred. See Constants class for more details
         *
         * Note - this includes handling the case where the user canceled the buy dialog:
         * errorCode = Constants.BILLING_RESPONSE_RESULT_USER_CANCELED
         */
    }

    @Override
    public void onPurchaseHistoryRestored() {
        /*
         * Called when purchase history was restored and the list of all owned PRODUCT ID's
         * was loaded from Google Play
         */
    }

    @Override
    public void callDefaultDialog(int pos) {
        if (pos == 5)
            reportDialog();
        else {
            showLoading();
            hideKeyboard();
            switch (pos) {
                case 0:
                    homeViewModel.reportRequest(new ReportRequestModel(id, "Inappropriate Photos"));
                    break;
                case 1:
                    homeViewModel.reportRequest(new ReportRequestModel(id, "Inappropriate Content"));
                    break;
                case 2:
                    homeViewModel.reportRequest(new ReportRequestModel(id, "Inappropriate Behaviour"));
                    break;
                case 3:
                    homeViewModel.reportRequest(new ReportRequestModel(id, "Stolen Photo"));
                    break;
                case 4:
                    homeViewModel.reportRequest(new ReportRequestModel(id, "Under 18"));
                    break;
            }
        }
    }

    @Override
    public void onClickToken(String tokenType, int tokensNum, int selectedPos) {
        tokenSType = tokenType;
        selectedPosition = tokensNum;
        if (tokenType.equalsIgnoreCase("timeToken")) {
            price = CommonDialogs.timeTokenPriceList.get(selectedPos).getPriceValue();
            productId = CommonDialogs.timeTokenArr[selectedPos];
            //homeViewModel.addTimeToken(new TimeTokenRequestModel(tokensNum, price));
            bp.purchase(this, productId);
        } else if (tokenType.equalsIgnoreCase("DeluxePurChase")) {
            price = CommonDialogs.DeluxePriceList.get(selectedPos).getPriceValue();
            productId = CommonDialogs.DeluxeArr[selectedPos];
            bp.subscribe(mActivity, productId);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);
    }
}
