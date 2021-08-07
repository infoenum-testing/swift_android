package com.swiftdating.app.ui.homeScreen.fragment;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.swiftdating.app.callbacks.OnItemClickListenerType;
import com.swiftdating.app.common.CommonDialogs;
import com.swiftdating.app.data.network.CallServer;
import com.swiftdating.app.model.requestmodel.DeluxeTokenCountModel;
import com.swiftdating.app.ui.base.BaseActivity;
import com.swiftdating.app.ui.chatScreen.ChatWindow;
import com.swiftdating.app.common.CommonUtils;
import com.swiftdating.app.data.preference.SharedPreference;
import com.swiftdating.app.model.responsemodel.ChatByUser;
import com.swiftdating.app.model.responsemodel.ChatListModel;
import com.swiftdating.app.model.responsemodel.ImageForUser;
import com.swiftdating.app.model.responsemodel.ProfileOfUser;
import com.swiftdating.app.ui.base.BaseFragment;
import com.swiftdating.app.R;
import com.swiftdating.app.common.swipemenulistview.SwipeMenuCreator;
import com.swiftdating.app.common.swipemenulistview.SwipeMenuItem;
import com.swiftdating.app.common.swipemenulistview.SwipeMenuListView;
import com.swiftdating.app.data.network.Resource;
import com.swiftdating.app.model.BaseModel;
import com.swiftdating.app.model.requestmodel.ReportRequestModel;
import com.swiftdating.app.model.responsemodel.MatchListResponseModel;
import com.swiftdating.app.ui.homeScreen.HomeActivity;
import com.swiftdating.app.ui.homeScreen.adapter.NewMatchesAdapter;
import com.swiftdating.app.ui.homeScreen.adapter.ScheduleSwipeAdapter;
import com.swiftdating.app.ui.homeScreen.viewmodel.HomeViewModel;
import com.swiftdating.app.ui.homeScreen.viewmodel.MatchListViewModel;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.blurry.Blurry;

import static com.swiftdating.app.common.AppConstants.LICENSE_KEY;

public class MatchFragment extends BaseFragment implements OnItemClickListenerType, View.OnClickListener, CommonDialogs.onClick, CommonDialogs.onPurchaseDeluxe, CommonDialogs.onProductConsume, BillingProcessor.IBillingHandler, BaseActivity.MyProfileResponse {

    private static final String TAG = "MatchFragment";
    public static boolean isShowDirect = false;
    boolean isDeluxeBtn = false;
    double price;
    String productId, tokenSType;
    int selectedPosition;
    private CircleImageView sdv_picture;
    private ConstraintLayout cl_new_matches, cl_schedule_matches, cons_dummy/*, direct_cl*/;
    private RecyclerView rv_new_matches;
    private SwipeMenuListView sml_schedule_matches;
    private MatchListViewModel matchListViewModel;
    private HomeViewModel homeViewModel;
    private TextView tvProfileReject, tv_direct_msg, tv_new_matches, tv_twentyLiks, tv_NoNewMatched, tv_NoMessage, tv_schedule_matches;
    private NewMatchesAdapter newMatchesAdapter;
    private ScheduleSwipeAdapter scheduleMatchesAdapter;
    private ArrayList<MatchListResponseModel.Match> matchesList = new ArrayList<>();
    private ArrayList<ChatListModel.ChatList> scheduleMatchList = new ArrayList<>();
    private ArrayList<ChatListModel.ChatList> scheduleMatchDirectList = new ArrayList<>();
    private ArrayList<Integer> idList = new ArrayList<>();
    private ArrayList<Integer> idListDirect = new ArrayList<>();
    private int pos = 0;
    private ConstraintLayout constraint_verify;
    private BillingProcessor bp;
    private int pageNumber = 1, offset;
    private boolean showLoadMore;
    private TabLayout tab_msg;
    private Button btn_direct_msg, direct_unread_count_btn, match_unread_count_btn;
    private boolean isDirect = false;
    private int tabCount = 0;
    private RelativeLayout rv_reject;
    /**
     * **  This is the handler that will manager to process the broadcast intent
     */
    private BroadcastReceiver mMessageReceiver2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int id = intent.getIntExtra("id", 0);
            String messageSent = intent.getStringExtra("messageSent");
            String image = intent.getStringExtra("image");
            String date = (String) DateFormat.format("yyyy-MM-dd HH:mm:ss", new Date());
            if (idList.contains(id)) {
                int pos = idList.indexOf(id);
                scheduleMatchList.get(idList.indexOf(id)).setChatByUser(new ChatByUser(messageSent, date, Integer.parseInt(new SharedPreference(context).getUserId())));
                idList.remove(pos);
                idList.add(0, id);
                scheduleMatchList.add(0, scheduleMatchList.get(pos));
                scheduleMatchList.remove(pos + 1);
                scheduleMatchesAdapter.notifyDataSetChanged();
            } else if (idListDirect.contains(id)) {
                int pos = idListDirect.indexOf(id);
                scheduleMatchDirectList.get(idListDirect.indexOf(id)).setChatByUser(new ChatByUser(messageSent, date, Integer.parseInt(new SharedPreference(context).getUserId())));
                idListDirect.remove(pos);
                //idListDirect.add(0, id);
                scheduleMatchList.add(0, scheduleMatchDirectList.get(pos));
                scheduleMatchDirectList.remove(pos);
                scheduleMatchesAdapter.notifyDataSetChanged();
            }else {
                int pos = 0;
                if (matchesList.size() > 0) {
                    for (int i = 0; i < matchesList.size(); i++)
                        if (matchesList.get(i).getId().equals(id)) pos = i;
                    ArrayList<ImageForUser> imageForUsers = new ArrayList<>();
                    imageForUsers.add(new ImageForUser(matchesList.get(pos).getImageForUserImageUrl()));
                    ChatByUser chat = new ChatByUser(messageSent, date, Integer.parseInt(new SharedPreference(context).getUserId()));
                    scheduleMatchList.add(0, new ChatListModel.ChatList(new ProfileOfUser(matchesList.get(pos).getProfileOfUserName()), imageForUsers, chat));
                    idList.add(0, matchesList.get(pos).getId());
                    //  cl_schedule_matches.setVisibility(View.VISIBLE);
                    scheduleMatchesAdapter.notifyDataSetChanged();
                    matchesList.remove(pos);
                    newMatchesAdapter.notifyDataSetChanged();
                    if (matchesList.size() == 0) {
                        //cl_new_matches.setVisibility(View.GONE);
                        tv_NoNewMatched.setVisibility(cons_dummy.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
                    }
                }
            }
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.fragment_matches;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {/*
        getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        super.onViewCreated(view, savedInstanceState);
        mActivity = (BaseActivity) getActivity();
        if (getBaseActivity().isNetworkConnected()) {
            getBaseActivity().getMyProfile(this);
            subscribeModel();
            initialize(view);
            createSwipeMenu();
            implementListeners();
            initBillingProcess();
        } else {
            getBaseActivity().showSnackbar(view, "Please connect to internet");
        }*/
    }

    private void initBillingProcess() {
        bp = new BillingProcessor(getActivity(), LICENSE_KEY, this);
        bp.initialize();
    }

    /***
     *
     *  Method to Handle api Response
     */
    private void subscribeModel() {
        matchListViewModel = ViewModelProviders.of(this).get(MatchListViewModel.class);
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        matchListViewModel.matchListResponse().observe(this, new Observer<Resource<MatchListResponseModel>>() {
            @Override
            public void onChanged(@Nullable Resource<MatchListResponseModel> resource) {
                if (resource == null) {
                    return;
                }
                switch (resource.status) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        if (matchesList.size() > 0) {
                            matchesList.clear();
                        }
                        newMatchesAdapter.notifyDataSetChanged();
                        if (resource.data.getSuccess()) {
                            setData(resource.data);
                        } else if (resource.data.getError() != null && resource.data.getError().getCode().equalsIgnoreCase("401")) {
                            getBaseActivity().openActivityOnTokenExpire();
                            getBaseActivity().hideLoading();
                        } else {
                            getBaseActivity().hideLoading();
                            if (pageNumber == 1) {
                                if (isDirect) {
                                    if (scheduleMatchDirectList == null || scheduleMatchDirectList.size() == 0) {
                                        sml_schedule_matches.setVisibility(View.GONE);
                                        tv_NoMessage.setVisibility(View.VISIBLE);
                                    } else {
                                        sml_schedule_matches.setVisibility(View.VISIBLE);
                                        tv_NoMessage.setVisibility(View.GONE);
                                    }
                                } else {
                                    if (scheduleMatchList == null || scheduleMatchList.size() == 0) {
                                        sml_schedule_matches.setVisibility(View.GONE);
                                        tv_NoMessage.setVisibility(View.VISIBLE);
                                    } else {
                                        sml_schedule_matches.setVisibility(View.VISIBLE);
                                        tv_NoMessage.setVisibility(View.GONE);
                                    }
                                }
                            }
                        }
                        break;
                    case ERROR:
                        getBaseActivity().hideLoading();
                        if (getBaseActivity().sp.getVerified().equalsIgnoreCase("Yes"))
                            getBaseActivity().showSnackbar(rv_new_matches, resource.message);
                        break;
                }
            }
        });

        matchListViewModel.chatListResponse().observe(this, new Observer<Resource<ChatListModel>>() {
            @Override
            public void onChanged(@Nullable Resource<ChatListModel> resource) {
                if (resource == null) {
                    return;
                }

                switch (resource.status) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        if (resource.data.getSuccess()) {
                            List<ChatListModel.ChatList> chatList = resource.data.getChatList();
                            if (scheduleMatchDirectList.size() > 0) {
                                scheduleMatchDirectList.clear();
                            }
                            if (scheduleMatchList.size() > 0) {
                                scheduleMatchList.clear();
                            }
                            for (int i = 0; i < chatList.size(); i++) {
                                ChatListModel.ChatList list = chatList.get(i);
                                if (list.getIsDirectChat() != null && list.getIsDirectChat().equalsIgnoreCase("YES")) {
                                    scheduleMatchDirectList.add(list);
                                } else {
                                    scheduleMatchList.add(list);
                                }
                                //idList.add(list.getId());
                            }
                            int count = 0;
                            if (scheduleMatchList.size() > 0) {
                                for (int i = 0; i < scheduleMatchList.size(); i++) {
                                    if (scheduleMatchList.get(i).getUnreadMessages() > 0) {
                                        count += scheduleMatchList.get(i).getUnreadMessages();
                                    }
                                }
                            }
                            if (count > 0) {
                                match_unread_count_btn.setVisibility(View.VISIBLE);
                                match_unread_count_btn.setText("" + count);
                            } else {
                                match_unread_count_btn.setVisibility(View.GONE);
                            }
                            count = 0;
                            if (scheduleMatchDirectList.size() > 0) {
                                for (int i = 0; i < scheduleMatchDirectList.size(); i++) {
                                    if (scheduleMatchDirectList.get(i).getUnreadMessages() > 0) {
                                        count += scheduleMatchDirectList.get(i).getUnreadMessages();
                                    }
                                }
                            }
                            if (count > 0) {
                                direct_unread_count_btn.setVisibility(View.VISIBLE);
                                direct_unread_count_btn.setText("" + count);
                            } else {
                                direct_unread_count_btn.setVisibility(View.GONE);
                            }
                            //scheduleMatchList.addAll(resource.data.getChatList());
                            for (int i = 0; i < scheduleMatchList.size(); i++) {
                                idList.add(scheduleMatchList.get(i).getId());
                            } for (int i = 0; i < scheduleMatchDirectList.size(); i++) {
                                idListDirect.add(scheduleMatchDirectList.get(i).getId());
                            }
                            if (resource.data.getChatList().size() > 0) {
                                cl_schedule_matches.setVisibility(View.VISIBLE);
                                scheduleMatchesAdapter.notifyDataSetChanged();
                            }

                        } else if (resource.data.getError() != null && resource.data.getError().getCode().equalsIgnoreCase("401")) {
                            getBaseActivity().openActivityOnTokenExpire();
                        } else {
                            //  cl_schedule_matches.setVisibility(View.GONE);
                        }
                        matchListViewModel.getMatchListRequest(pageNumber + "");
                        break;
                    case ERROR:
                        getBaseActivity().showSnackbar(rv_new_matches, resource.message);
                        matchListViewModel.getMatchListRequest(pageNumber + "");
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
                        getBaseActivity().hideLoading();
                        if (resource.data.getSuccess()) {
                            if (resource.data.getError() != null && resource.data.getError().getCode().contains("401")) {
                                getBaseActivity().openActivityOnTokenExpire();
                            } else {
                                if (!isDirect) {
                                    scheduleMatchList.remove(pos);
                                    if (scheduleMatchList.size() == 0) {
                                        sml_schedule_matches.setVisibility(View.GONE);
                                        tv_NoMessage.setVisibility(View.VISIBLE);
                                        if (matchesList.size() == 0) {
                                            tv_NoNewMatched.setVisibility(cons_dummy.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
                                            // direct_cl.setVisibility(View.GONE);
                                        }
                                    }
                                } else {
                                    scheduleMatchDirectList.remove(pos);
                                    if (scheduleMatchDirectList.size() == 0) {
                                        sml_schedule_matches.setVisibility(View.GONE);
                                        tv_NoMessage.setVisibility(View.VISIBLE);
                                        //direct_cl.setVisibility(View.VISIBLE);
                                    }
                                }
                                scheduleMatchesAdapter.notifyDataSetChanged();
                            }
                        } else {
                            getBaseActivity().showSnackbar(rv_new_matches, resource.message);
                        }
                        break;
                    case ERROR:
                        getBaseActivity().hideLoading();
                        getBaseActivity().showSnackbar(rv_new_matches, resource.message);
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
                        getBaseActivity().hideLoading();
                        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (imm != null) {
                            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                        }
                        if (resource.data.getSuccess()) {
                            if (resource.data.getError() != null && resource.data.getError().getCode().contains("401")) {
                                getBaseActivity().openActivityOnTokenExpire();
                            } else {
                                showSnackBar(rv_new_matches, "User successfully reported");
                            }
                        } else {
                            // showSnackBar(rv_new_matches, "User Already Reported");
                            showSnackBar(rv_new_matches, "User has already been reported.");
                        }
                        break;
                    case ERROR:
                        getBaseActivity().hideLoading();
                        getBaseActivity().showSnackbar(rv_new_matches, resource.message);
                        break;
                }
            }
        });

        homeViewModel.addDeluxeResponse().observe(this, new Observer<Resource<BaseModel>>() {
            @Override
            public void onChanged(Resource<BaseModel> resource) {
                if (resource == null)
                    return;
                switch (resource.status) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        getBaseActivity().hideLoading();
                        if (resource.data.getSuccess()) {
                            getBaseActivity().sp.saveDeluxe(true);
                            setDeluxeData();
                           /* tvPremium.setVisibility(View.INVISIBLE);
                            tvUnlimitedView.setVisibility(View.INVISIBLE);*/
                        } else if (resource.code == 401) {
                            getBaseActivity().openActivityOnTokenExpire();
                        } else {
                            getBaseActivity().showSnackbar(rv_new_matches, "Something went wrong");
                        }
                        break;
                    case ERROR:
                        getBaseActivity().hideLoading();
                        getBaseActivity().showSnackbar(rv_new_matches, resource.message);
                        break;
                }
            }
        });

    }

    @Override
    public void onResume() {
      /*  mActivity = (BaseActivity) getActivity();
        ((HomeActivity) Objects.requireNonNull(getActivity())).mToolbar.setVisibility(View.GONE);
        if (getBaseActivity().isNetworkConnected()) {
            try {
                (Objects.requireNonNull(getContext())).registerReceiver(mMessageReceiver2, new IntentFilter("unique_name"));
            } catch (NullPointerException ignored) {
            }
            getBaseActivity().isMatchScreen = true;
            pageNumber = 1;
            //matchesList.clear();
            scheduleMatchList.clear();
            scheduleMatchDirectList.clear();
            // cl_schedule_matches.setVisibility(View.GONE);
            // cl_new_matches.setVisibility(View.GONE);
            tv_NoNewMatched.setVisibility(cons_dummy.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
            // ((HomeActivity) getActivity()).setToolbarWithTitle("Messages");
            if (new SharedPreference(getContext()).getVerified().equalsIgnoreCase("Yes")) {
                constraint_verify.setVisibility(View.GONE);
                rv_reject.setVisibility(View.GONE);
                getBaseActivity().showLoading();
                matchListViewModel.getChatListRequest(getBaseActivity().sp.getToken());
            } else {
                if (getBaseActivity().sp.isRejected()) {
                    rv_reject.setVisibility(View.VISIBLE);
                    constraint_verify.setVisibility(View.GONE);
                } else {
                    constraint_verify.setVisibility(View.VISIBLE);
                    rv_reject.setVisibility(View.GONE);
                }
            }
        }*/
        super.onResume();
    }

    @Override
    public void onPause() {
     /*   LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mMessageReceiver2);
        getBaseActivity().isMatchScreen = false;*/
        super.onPause();
    }

    /**
     * Method to set Data
     */
    private void setData(MatchListResponseModel model) {
        if (model.getMatches().size() > 0) {
            matchesList.addAll(model.getMatches());
            tv_NoNewMatched.setVisibility(View.GONE);
        } else {
            tv_NoNewMatched.setVisibility(cons_dummy.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        }
        if (isDirect) {
            if (scheduleMatchDirectList == null || scheduleMatchDirectList.size() == 0) {
                sml_schedule_matches.setVisibility(View.GONE);
                tv_NoMessage.setVisibility(View.VISIBLE);
            } else {
                sml_schedule_matches.setVisibility(View.VISIBLE);
                tv_NoMessage.setVisibility(View.GONE);
            }
        } else {
            if (scheduleMatchList == null || scheduleMatchList.size() == 0) {
                sml_schedule_matches.setVisibility(View.GONE);
                tv_NoMessage.setVisibility(View.VISIBLE);
            } else {
                sml_schedule_matches.setVisibility(View.VISIBLE);
                tv_NoMessage.setVisibility(View.GONE);
            }
            if (matchesList == null || matchesList.size() == 0) {
                tv_NoNewMatched.setVisibility(cons_dummy.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
            } else tv_NoNewMatched.setVisibility(View.GONE);
        }
        newMatchesAdapter.notifyDataSetChanged();
        scheduleMatchesAdapter.notifyDataSetChanged();
        getBaseActivity().hideLoading();
    }

    private void setLikesCount(boolean isLoadImage) {
        if (getBaseActivity() != null && getBaseActivity().sp != null && getBaseActivity().sp.getNoOfLikes() != null && getBaseActivity().sp.getNoOfLikes().getUsers() > 0 && !getBaseActivity().sp.getDeluxe()) {
            if (isLoadImage && getBaseActivity().sp.getNoOfLikes().getUsersImage() != null && getBaseActivity().sp.getNoOfLikes().getUsersImage().size() > 0) {
                Glide.with(mContext).asBitmap().load(CallServer.BaseImage + getBaseActivity().sp.getNoOfLikes().getUsersImage().get(0).getImageUrl()).into(new CustomTarget<Bitmap>((int) mContext.getResources().getDimension(R.dimen._60sdp), (int) mContext.getResources().getDimension(R.dimen._60sdp)) {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Blurry.with(mContext).sampling(5).radius(10).from(resource).into(sdv_picture);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
            }
            int likes = ((BaseActivity) mContext).sp.getNoOfLikes().getUsers();
            tv_twentyLiks.setText(likes > 99 ? "99+ Likes" : likes == 1 ? likes + " Like" : likes + " Likes");
            cons_dummy.setVisibility(View.VISIBLE);
        } else {
            cons_dummy.setVisibility(View.GONE);
        }
    }

    /**
     * initialize all the views
     */
    private void initialize(View view) {
        tv_schedule_matches = view.findViewById(R.id.tv_schedule_matches);
        tv_NoNewMatched = view.findViewById(R.id.tv_NoNewMatched);
        rv_reject = view.findViewById(R.id.rv_reject);
        tv_NoMessage = view.findViewById(R.id.tv_NoMessage);
        cons_dummy = view.findViewById(R.id.cons_dummy);
        tv_twentyLiks = view.findViewById(R.id.tv_twentyLiks);
        sdv_picture = view.findViewById(R.id.sdv_picture);
        tv_twentyLiks.setOnClickListener(v -> {
            //((HomeActivity) mContext).setCurrentTabFragment(1);
            ((HomeActivity) mContext).replaceLikeTabIcon(1);
        });
        sdv_picture.setOnClickListener(v -> {
            //((HomeActivity) mContext).setCurrentTabFragment(1);
            ((HomeActivity) mContext).replaceLikeTabIcon(1);
        });
        direct_unread_count_btn = view.findViewById(R.id.direct_unread_count_btn);
        match_unread_count_btn = view.findViewById(R.id.match_unread_count_btn);
        btn_direct_msg = view.findViewById(R.id.btn_direct_msg);
//        btn_direct_msg.setOnClickListener(this);
        tv_new_matches = view.findViewById(R.id.tv_new_matches);
        tv_direct_msg = view.findViewById(R.id.tv_direct_msg);
        tab_msg = view.findViewById(R.id.tab_msg);
        cl_new_matches = view.findViewById(R.id.cl_new_matches);
        constraint_verify = view.findViewById(R.id.constraint_verify);
        tvProfileReject = view.findViewById(R.id.tvProfileReject);
        cl_schedule_matches = view.findViewById(R.id.cl_schedule_matches);
        rv_new_matches = view.findViewById(R.id.rv_new_matches);
        sml_schedule_matches = view.findViewById(R.id.rv_schedule_matches);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_new_matches.setLayoutManager(linearLayoutManager);
        newMatchesAdapter = new NewMatchesAdapter(mActivity, matchesList);//, this);
        newMatchesAdapter.setOnItemClickListener(this);
        rv_new_matches.setAdapter(newMatchesAdapter);
        scheduleMatchesAdapter = new ScheduleSwipeAdapter(mActivity, scheduleMatchList, MatchFragment.this);
        scheduleMatchesAdapter.setOnItemClickListener(this);
        sml_schedule_matches.setAdapter(scheduleMatchesAdapter);
        rv_new_matches.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                int lastVisiblePosition = linearLayoutManager.findLastVisibleItemPosition();
                if (lastVisiblePosition == matchesList.size() - 1 && matchesList.size() % 20 == 0) {
                    pageNumber++;
                    showLoadMore = true;
                    matchListViewModel.getMatchListRequest(pageNumber + "");
                }
            }
        });
        setupTabIcons();
        if (isShowDirect) {
            tabCount = 1;
            Objects.requireNonNull(tab_msg.getTabAt(tabCount)).select();
            isShowDirect = false;
            tabCount = 0;
        }
        isDeluxeBtn = getBaseActivity().sp.getDeluxe();
        if (getBaseActivity().sp.getDeluxe()) {
            setDeluxeData();
        }
        setLikesCount(true);
    }

    private void setupTabIcons() {
        tab_msg.addTab(tab_msg.newTab().setText("Matched\nMessages"));
        tab_msg.addTab(tab_msg.newTab().setText("Direct\nMessages"));
        Objects.requireNonNull(tab_msg.getTabAt(tabCount)).select();
        tab_msg.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                isDirect = tab.getPosition() == 1;
                scheduleMatchesAdapter = tab.getPosition() == 0 ? new ScheduleSwipeAdapter(mActivity, scheduleMatchList, MatchFragment.this) : new ScheduleSwipeAdapter(mActivity, scheduleMatchDirectList, MatchFragment.this);
                if (!isDeluxeBtn) {
                    if (tab.getPosition() == 0) {
                        setLikesCount(false);
                    } else {
                        cons_dummy.setVisibility(View.GONE);
                    }
//                    scheduleMatchesAdapter.isBlurr = tab.getPosition() == 1;
//                    btn_direct_msg.setVisibility(tab.getPosition() == 0 ? View.GONE : View.VISIBLE);
                } else {
                    cons_dummy.setVisibility(View.GONE);
                    btn_direct_msg.setVisibility(View.GONE);
//                    scheduleMatchesAdapter.isBlurr = false;
                }
                tv_NoMessage.setText(tab.getPosition() == 0 ? "No new messages yet." : "No new direct messages yet.");
                cl_new_matches.setVisibility(tab.getPosition() == 1 ? View.GONE : View.VISIBLE);
                tv_direct_msg.setVisibility(tab.getPosition() == 0 ? View.GONE : View.VISIBLE);
                rv_new_matches.setVisibility(tab.getPosition() == 1 ? View.GONE : View.VISIBLE);
                tv_schedule_matches.setVisibility(tab.getPosition() == 1 ? View.GONE : View.VISIBLE);
                tv_new_matches.setVisibility(tab.getPosition() == 1 ? View.GONE : View.VISIBLE);
                if (isDirect){
                    if (scheduleMatchDirectList==null||scheduleMatchDirectList.size()==0){
                        sml_schedule_matches.setVisibility(View.GONE);
                        tv_NoMessage.setVisibility(View.VISIBLE);
                    }else {
                        sml_schedule_matches.setVisibility(View.VISIBLE);
                        tv_NoMessage.setVisibility(View.GONE);
                    }
                }else {
                    if (scheduleMatchList==null||scheduleMatchList.size()==0){
                        sml_schedule_matches.setVisibility(View.GONE);
                        tv_NoMessage.setVisibility(View.VISIBLE);
                    }else {
                        sml_schedule_matches.setVisibility(View.VISIBLE);
                        tv_NoMessage.setVisibility(View.GONE);
                    }
                }
                sml_schedule_matches.setAdapter(scheduleMatchesAdapter);
                scheduleMatchesAdapter.setOnItemClickListener(MatchFragment.this);
                scheduleMatchesAdapter.notifyDataSetChanged();
                //getBaseActivity().showLoading();
                createSwipeMenu();
                //matchListViewModel.getChatListRequest(getBaseActivity().sp.getToken());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    /**
     * implement all listeners
     */
    private void implementListeners() {
        sml_schedule_matches.setOnMenuItemClickListener((position, menu, index) -> {
            switch (index) {
                case 0:
                    CommonDialogs.reportDialogNew(mActivity, MatchFragment.this);
                    pos = position;
                    break;
                case 1:
                    pos = position;
                    Dialog dialog1 = new Dialog(getBaseActivity());
                    dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    Objects.requireNonNull(dialog1.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog1.setContentView(R.layout.dialog_two_button);
                    dialog1.setCancelable(false);
                    dialog1.setCanceledOnTouchOutside(false);
                    dialog1.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    dialog1.show();
                    TextView tv_message1 = dialog1.findViewById(R.id.tv_message);
                    TextView tv_yes = dialog1.findViewById(R.id.tv_yes);
                    TextView tv_no = dialog1.findViewById(R.id.tv_no);
                    if (isDirect)
                        tv_message1.setText("Are you sure you want to block this user? They will not be able to send you any more messages.");
                    else
                        tv_message1.setText("Are you sure you want to unmatch this user? Unmatching them will also block them from contacting you.");

                    tv_yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog1.dismiss();
                            getBaseActivity().showLoading();
                            if (isDirect) {
                                homeViewModel.unMatchRequest(String.valueOf(scheduleMatchDirectList.get(position).getId()));
                            } else {
                                homeViewModel.unMatchRequest(String.valueOf(scheduleMatchList.get(position).getId()));
                            }

                        }
                    });
                    tv_no.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog1.dismiss();
                        }
                    });
                    break;
            }
            return false;
        });
    }

    /**
     * create a MenuCreator
     */
    private void createSwipeMenu() {

        SwipeMenuCreator creator = menu -> {

            // create "report" item
            SwipeMenuItem item1 = new SwipeMenuItem(mActivity);
            item1.setBackground(new ColorDrawable(ContextCompat.getColor(mActivity, R.color.yellow)));
            item1.setWidth(dp2px(90));
            item1.setTitle("Report");
            item1.setTitleSize(12);
            item1.setTitleColor(Color.WHITE);
            menu.addMenuItem(item1);

            // create "unmatch" item
            SwipeMenuItem item2 = new SwipeMenuItem(mActivity);
            item2.setBackground(new ColorDrawable(ContextCompat.getColor(mActivity, R.color.red)));
            item2.setWidth(dp2px(90));
            if (isDirect)
                item2.setTitle("Block");
            else
                item2.setTitle("Unmatch");
            item2.setTitleSize(12);
            item2.setTitleColor(Color.WHITE);
            menu.addMenuItem(item2);

        };

        // set creator
        sml_schedule_matches.setMenuCreator(creator);
    }

    /**
     * convert DP to Pixel
     */
    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    @Override
    public void OnItemClick(int position, int type) {
        if (type == 5) {
            // tv_direct_msg.setVisibility(View.GONE);
            btn_direct_msg.setVisibility(View.GONE);
//            scheduleMatchesAdapter.isBlurr = false;
            sml_schedule_matches.setAdapter(scheduleMatchesAdapter);
            scheduleMatchesAdapter.notifyDataSetInvalidated();
            //isDeluxeBtn = true;
            //// newMatchesAdapter.isUnBlurr = true;
            newMatchesAdapter.notifyDataSetChanged();
        } else if (type == 2) {
            if (isDirect) {
//                if (isDeluxeBtn){
                    if (scheduleMatchDirectList.size() > 0) {
                        isShowDirect = true;
                        startActivityForResult(new Intent(mContext, ChatWindow.class).putExtra("id", scheduleMatchDirectList.get(position).getId())
                                .putExtra("scheduleist", scheduleMatchDirectList.get(position).toString())
                                .putExtra("name", scheduleMatchDirectList.get(position).getProfileOfUser().getName())
                                .putExtra("tabPos", 3)
                                .putExtra("isExpired", false)
                                .putExtra("isfromDirect", true)
                                .putExtra("image", scheduleMatchDirectList.get(position).getImageForUser().get(0).getImageUrl()), 10000);
                        getBaseActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                /*}else {
                    CommonDialogs.DeluxePurChaseDialog(getContext(), this);
                }*/
            } else {
                if (scheduleMatchList.size() > 0) {
                    isShowDirect = false;
                    startActivityForResult(new Intent(mContext, ChatWindow.class).putExtra("id", scheduleMatchList.get(position).getId())
                            .putExtra("scheduleist", scheduleMatchList.get(position).toString())
                            .putExtra("name", scheduleMatchList.get(position).getProfileOfUser().getName())
                            .putExtra("tabPos", 3)
                            .putExtra("isExpired", false)
                            .putExtra("isfromDirect", false)
                            .putExtra("image", scheduleMatchList.get(position).getImageForUser().get(0).getImageUrl()), 10000);
                    getBaseActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            }
           /* else {
                startActivity(new Intent(mContext, ChatWindow.class));
            }*/
            //  getActivity().overridePendingTransition(R.anim.slide_in_top, R.anim.nothing);

        } else {
            if (position < matchesList.size() && matchesList.size() > 0) {
                long expire = CommonUtils.stringToDate(matchesList.get(position).getMatchForUserCalltimerExpiry().replace("T", " ").split("\\.")[0]);
                long server = CommonUtils.stringToDate(matchesList.get(position).getMatchForUserServerTime().replace("T", " ").split("\\.")[0]);
                boolean isExpired = false;
                long timeLeft = 0;
                if (server >= expire) {
                    isExpired = true;
                    timeLeft = 0;
                } else {
                    timeLeft = expire - server;
                }
                isShowDirect = false;
                startActivityForResult(new Intent(mContext, ChatWindow.class).putExtra("id", matchesList.get(position).getId())
                        .putExtra("name", matchesList.get(position).getProfileOfUserName())
                        .putExtra("matchesList", matchesList.get(position).toString())
                        .putExtra("tabPos", 3)
                        .putExtra("isExpired", isExpired)
                        .putExtra("hitApi", true)
                        .putExtra("timeLeft", timeLeft)
                        .putExtra("image", matchesList.get(position).getImageForUserImageUrl()), 10000);
                getBaseActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            } /*else {
                startActivity(new Intent(mContext, ChatWindow.class));
            }*/
            // getActivity().overridePendingTransition(R.anim.slide_in_top, R.anim.nothing);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_direct_msg:
                getBaseActivity().sp.setDialogOpen(true);
                CommonDialogs.DeluxePurChaseDialog(getContext(), this);
                /*CommonDialogs dialogs = CommonDialogs.DeluxePurChaseDialog(getContext(),this);
                dialogs.setOnDeluxeContinuebtn(this);*/
                break;

        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);

    }

    void setDeluxeData() {
        cons_dummy.setVisibility(View.GONE);
        //tv_direct_msg.setVisibility(View.GONE);
        btn_direct_msg.setVisibility(View.GONE);
//        scheduleMatchesAdapter.isBlurr = false;
        sml_schedule_matches.setAdapter(scheduleMatchesAdapter);
        //scheduleMatchesAdapter=new ScheduleSwipeAdapter(mActivity, scheduleMatchDirectList,MatchFragment.this);
        scheduleMatchesAdapter.notifyDataSetInvalidated();
        //isDeluxeBtn = true;
        //// newMatchesAdapter.isUnBlurr = true;
        newMatchesAdapter.notifyDataSetChanged();
    }

    @Override
    public void callDefaultDialog(int pos) {
        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_edit_text);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView tv_message = dialog.findViewById(R.id.tv_message);
        ImageView ivcross = dialog.findViewById(R.id.ivcross);
        EditText etReason = dialog.findViewById(R.id.et_reason);
        Button tv_ok = dialog.findViewById(R.id.btn_ok);
        tv_message.setText(this.getResources().getString(R.string.reasontoReport));
        ivcross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                dialog.dismiss();
            }
        });


        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(etReason.getText().toString())) {
                    getBaseActivity().showLoading();
                    getBaseActivity().hideKeyboard();
                    // pos = position;
                    if (!isDirect)
                        homeViewModel.reportRequest(new ReportRequestModel(scheduleMatchList.get(pos).getId(), etReason.getText().toString()));
                    else
                        homeViewModel.reportRequest(new ReportRequestModel(scheduleMatchDirectList.get(pos).getId(), etReason.getText().toString()));
                    dialog.dismiss();
                } else {
                    Toast.makeText(getContext(), "Please enter the reason for report", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void OnClickContinue() {
        setDeluxeData();
    }

    @Override
    public void onClickToken(String tokenType, int tokensNum, int selectedPos) {
        tokenSType = tokenType;
        selectedPosition = tokensNum;
        if (tokenType.equalsIgnoreCase("DeluxePurChase")) {
            price = CommonDialogs.DeluxePriceList.get(selectedPos).getPriceValue();
            productId = CommonDialogs.DeluxeArr[selectedPos];
            bp.subscribe(mActivity, productId);
        }
    }

    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {
        Log.e(TAG, "onProductPurchased: " + details + "\n" + productId);
        if (tokenSType.equalsIgnoreCase("DeluxePurChase")) {
            Toast.makeText(getContext(), "Item Purchased", Toast.LENGTH_LONG).show();
            bp.consumePurchase(productId);
            mActivity.showLoading();
            homeViewModel.addDeluxeRequest(new DeluxeTokenCountModel("2", productId,
                    price,
                    selectedPosition,
                    details.purchaseInfo.purchaseData.orderId,
                    details.purchaseInfo.purchaseData.purchaseToken, CommonUtils.getDateForPurchase(details), details.purchaseInfo.signature,
                    details.purchaseInfo.purchaseData.purchaseState.toString()));
        }
    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {
        Log.e(TAG, "onBillingError: " + errorCode);
    }

    @Override
    public void onBillingInitialized() {

    }

    @Override
    public void setProfileData() {
        setLikesCount(true);
       /* if (getBaseActivity() != null && newMatchesAdapter != null) {
            newMatchesAdapter.notifyDataSetChanged();
        }*/
    }
}

