


package com.swiftdating.app.ui.homeScreen.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.MaskFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.billingclient.api.Purchase;
import com.google.android.material.tabs.TabLayout;
import com.swiftdating.app.R;
import com.swiftdating.app.common.CommonDialogs;
import com.swiftdating.app.common.CommonUtils;
import com.swiftdating.app.common.SubscriptionResponse;
import com.swiftdating.app.data.network.ApiCall;
import com.swiftdating.app.data.network.ApiCallback;
import com.swiftdating.app.model.requestmodel.PremiumTokenCountModel;
import com.swiftdating.app.model.responsemodel.User;
import com.swiftdating.app.model.responsemodel.WhoLikedYouReponce;
import com.swiftdating.app.ui.base.BaseActivity;
import com.swiftdating.app.ui.base.BaseFragment;
import com.swiftdating.app.ui.homeScreen.HomeActivity;
import com.swiftdating.app.ui.homeScreen.adapter.LikesImagesAdapter;
import com.swiftdating.app.ui.homeScreen.viewmodel.HomeViewModel;
import com.swiftdating.app.ui.userCardScreen.UserCardActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LikesFrament extends BaseFragment implements CommonDialogs.onProductConsume, ApiCallback.GetListWhoLikedYouCallback, ApiCallback.GetUserDislikedListCallback, BaseActivity.MyProfileResponse, LikesImagesAdapter.OnLikeUserClick, BaseActivity.OnPurchaseListener {
    public static boolean showSencondChance = false;
    public static int MyLikeCount;
    private static RecyclerView recycle;
    private static int totalItemsViewed = 0;
    boolean onBtnClick = false;
    HomeViewModel homeViewModel;
    double price;
    String productId, tokenSType;
    int selectedPosition;
    int size;
    int myIntentReqCode;
    GridLayoutManager manager;
    private TabLayout tab_like;
    private final String TAG = LikesFrament.class.getSimpleName();
    private TextView tv_subscribe, tv_no_result;
    private RelativeLayout rlNoResult;
    private ImageView ivNoResult;
    private String[] subscribe_txts, btn_txt;
    private Button btn_see_people;
    private LinearLayout llRootView;
    private List<User> list = new ArrayList<>(), disLikelist = new ArrayList<>();
    private int pagecount, loadMoreRange = 19, posAdapter;
    private boolean IsLoadMore, scrollDown = false;
    private ProgressDialog mProgressDialog;
    private boolean isSencondPressed = false;
    private int oldFirstPos = -1, oldLastPos = -1;
    private boolean isFromOnCreate = false;
    private int LikedUser, disliked = -1;
    private boolean iscallApiforlike = false;

    public LikesFrament() {

    }

    public static void setRecycleToTop() {
        recycle.scrollToPosition(0);
        totalItemsViewed = 0;
    }

    public void setLoadMore(boolean loadMore) {
        IsLoadMore = loadMore;
    }

    @Override
    public int getLayoutId() {
        return R.layout.likes_fragment;
    }

    public void showLoading() {
        if (mProgressDialog == null || !mProgressDialog.isShowing())
            mProgressDialog = CommonUtils.showLoadingDialog(getActivity());
    }

    /**
     * * Method to hide Loader
     */
    public void hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    private void setAdapter(List<User> list) {
        if (mActivity != null)
            recycle.setAdapter(new LikesImagesAdapter(mActivity, list,onBtnClick, this, this, getBaseActivity().sp));
    }

    @Override
    public void onPause() {
        super.onPause();
        if (list != null)
            ((HomeActivity) getActivity()).LikeUserlist = list;
        if (disLikelist != null) {
            ((HomeActivity) getActivity()).DisLikeUserlist = disLikelist;
        }
        getBaseActivity().isLikeScreen = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        mActivity = (BaseActivity) getActivity();
        getBaseActivity().isLikeScreen = true;
        //((HomeActivity) Objects.requireNonNull(getActivity())).setToolbarWithTitle("Likes");
        ((HomeActivity) getActivity()).mToolbar.setVisibility(View.GONE);
    }
         /*CommonDialogs dialog = CommonDialogs.DeluxePurChaseDialog(getContext(), this);
        dialog.setOnDeluxeContinuebtn(this);*/

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG, "onViewCreated: LikesFragment");
        mActivity = (BaseActivity) getActivity();
        getBaseActivity().isLikeScreen = true;
        if (getBaseActivity().isNetworkConnected())
            initViews(view);
        else {
            getBaseActivity().showSnackbar(view, "Please connect to internet");
        }
    }

    private void initViews(View view) {
        iscallApiforlike = false;
        setLoadMore(true);
        getBaseActivity().getMyProfile(this);
        pagecount = 1;
        isFromOnCreate = true;
        recycle = view.findViewById(R.id.recycle);
        tv_no_result = view.findViewById(R.id.tv_no_result);
        rlNoResult = view.findViewById(R.id.rlNoResult);
        ivNoResult = view.findViewById(R.id.ivNoResult);
        tv_subscribe = view.findViewById(R.id.tv_subscribe);
        btn_see_people = view.findViewById(R.id.btn_see_people);
        if (getBaseActivity().sp.getPremium()) {
            setPremiumData();
        } else {
            Log.e(TAG, "initViews: " + onBtnClick);
        }
        if (getBaseActivity().sp.getNoOfLikes().getUsers() == 0) {
            recycle.setVisibility(View.GONE);
            tv_no_result.setVisibility(View.VISIBLE);
            rlNoResult.setVisibility(View.VISIBLE);
            tv_no_result.setText("New likes will appear here. You don't have any likes right now.");
        }
        if (!((HomeActivity) getActivity()).LikeUserlist.isEmpty()) {
            if (getBaseActivity().sp.getNoOfLikes().getUsers() > 0) {
                list = ((HomeActivity) getActivity()).LikeUserlist;
                setAdapter(list);
            } else {
                if (!((HomeActivity) getActivity()).LikeUserlist.isEmpty())
                    ((HomeActivity) getActivity()).LikeUserlist.clear();
            }
        } else if (getBaseActivity().sp.getNoOfLikes().getUsers() > 0) {
            showLoading();
        }
        if (!((HomeActivity) getActivity()).DisLikeUserlist.isEmpty()) {
            disLikelist = ((HomeActivity) getActivity()).DisLikeUserlist;
        }
        initBillingProcess();
        tab_like = view.findViewById(R.id.tab_like);

        tv_no_result.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        MaskFilter mm = tv_no_result.getPaint().setMaskFilter(null);
        tv_no_result.getPaint().setMaskFilter(mm);

        llRootView = view.findViewById(R.id.llRootView);
        manager = new GridLayoutManager(getContext(), 3);
        recycle.setLayoutManager(manager);
        recycle.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    GridLayoutManager manager = (GridLayoutManager) recyclerView.getLayoutManager();
                    if (IsLoadMore && recyclerView.getAdapter().getItemCount() > loadMoreRange) {
                        if (manager != null && manager.findLastCompletelyVisibleItemPosition() == list.size() - 1) {
                            pagecount++;
                            callApi(pagecount);
                            setLoadMore(false);
                        }
                    }
                }
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!getBaseActivity().sp.getPremium()) {
                    int currFirstPos = manager.findFirstCompletelyVisibleItemPosition();
                    int currLastPos = manager.findLastCompletelyVisibleItemPosition();
                    if (oldFirstPos == -1) {
                        totalItemsViewed += currLastPos - currFirstPos + 1;
                    } else {
                        if (dy > 0) {
                            totalItemsViewed += Math.abs(currLastPos - oldLastPos);
                        } else {
                            if (totalItemsViewed != 0)
                                totalItemsViewed -= Math.abs(oldLastPos - currLastPos);
                        }
                    }
                    oldLastPos = currLastPos;
                    oldFirstPos = currFirstPos;
                    Log.e("totalItemsViewed", "onScrolled  " + totalItemsViewed + " %6 = " + totalItemsViewed % 6);
                    scrollDown = dy <= 0;
                    if (manager.getItemCount() > 6 && !scrollDown &&  totalItemsViewed != 0 && totalItemsViewed % 6 == 0) {
                        totalItemsViewed = 0;
                        CommonDialogs.PremuimPurChaseDialog(getContext(), LikesFrament.this, getBaseActivity().sp);
                        recyclerView.scrollToPosition(0);
                    }
                }
            }
        });
        subscribe_txts = new String[]{getString(R.string.likes_subscribe_deluxe_txt), getString(R.string.likes_subscribe_profile_deluxe_txt)};
        btn_txt = new String[]{getString(R.string.like_btn_who_like_txt), getString(R.string.like_btn_skip_txt)};
        btn_see_people.setOnClickListener(this::onClick);

        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);

        homeViewModel.addPremiumResponse().observe(getViewLifecycleOwner(), resource -> {
            if (resource == null) return;
            switch (resource.status) {
                case LOADING:
                    break;
                case SUCCESS:
                    getBaseActivity().hideLoading();
                    if (resource.data.getSuccess()) {
                      /*  onBtnClick = true;
                        tv_subscribe.setVisibility(View.GONE);
                        btn_see_people.setVisibility(View.GONE);
                        recycle.setAdapter(new LikesImagesAdapter(getContext(), list, onBtnClick));*/
                        getBaseActivity().sp.savePremium(true);
                        setPremiumData();
                        ((LikesImagesAdapter) Objects.requireNonNull(recycle.getAdapter())).setUnlock(onBtnClick);
                        recycle.getAdapter().notifyDataSetChanged();
                       /* tvPremium.setVisibility(View.INVISIBLE);
                        tvUnlimitedView.setVisibility(View.INVISIBLE);*/
                    } else if (resource.code == 401) {
                        getBaseActivity().openActivityOnTokenExpire();
                    } else {
                        getBaseActivity().showSnackbar(llRootView, "Something went wrong");
                    }
                    break;
                case ERROR:
                    getBaseActivity().hideLoading();
                    getBaseActivity().showSnackbar(llRootView, resource.message);
                    break;
            }
        });

        setupTabIcons();
        /*if (isShowDirect) {
            tabCount = 1;
            Objects.requireNonNull(tab_msg.getTabAt(tabCount)).select();
            isShowDirect = false;
            tabCount = 0;
        }*/
        if (showSencondChance) {
            tab_like.getTabAt(1).select();
            showSencondChance = false;
        }
    }

    private void callApi(int count) {
        showLoading();
        ApiCall.getListWhoLikedYou(getBaseActivity().sp.getToken(), "" + count, this);
    }

    private void callDislikedApi() {
        showLoading();
        ApiCall.getUserDislikedList(getBaseActivity().sp.getToken(), this);
    }

    private void onClick(View view) {
        getBaseActivity().sp.setDialogOpen(true);
        CommonDialogs.PremuimPurChaseDialog(getContext(), this, getBaseActivity().sp);
    }

    /**
     * **  Method to Initialize Billing Process
     */
    private void initBillingProcess() {
        /*bp = new BillingProcessor(getActivity(), LICENSE_KEY, this);
        bp.initialize();*/
    }

    private void setupTabIcons() {
        String txt;
        String txt2;
        if (getBaseActivity().sp.getNoOfLikes().getUsers() == 1) {
            txt = getBaseActivity().sp.getNoOfLikes().getUsers() + " Person";
            txt2 = txt + "\nLikes You";
        } else {
            int cout = getBaseActivity().sp.getNoOfLikes().getUsers();
            txt = cout > 99 ? "99+ People" : cout + " People";
            txt2 = txt + "\nLike You";
        }
        SpannableString spannable = new SpannableString(txt2);
        spannable.setSpan(new ForegroundColorSpan(Color.RED), 0, txt.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tab_like.addTab(tab_like.newTab().setText(spannable));
        tab_like.addTab(tab_like.newTab().setText("Second\nChance"));
        tab_like.getTabAt(0).select();
        tab_like.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                isSencondPressed = tab.getPosition() == 1;
                /*if (tv_subscribe.getVisibility()==View.GONE){
                    tv_subscribe.setVisibility(View.VISIBLE);
                    btn_see_people.setVisibility(View.VISIBLE);
                }*/
                if (tab.getPosition() == 1) {
                    if (isListEmpty(disLikelist)
                            && (disliked == -1 || disliked > 0) ||
                            getBaseActivity().sp.getDislikeApi()) {
                        recycle.setVisibility(View.GONE);
                        rlNoResult.setVisibility(View.VISIBLE);
                        ivNoResult.setImageResource(R.drawable.second_chance_img);
                        tv_no_result.setText("");
                        callDislikedApi();
                    } else {
                        if (disliked == 0) {
                            recycle.setVisibility(View.GONE);
                            tv_no_result.setVisibility(View.VISIBLE);
                            rlNoResult.setVisibility(View.VISIBLE);
                            ivNoResult.setImageResource(R.drawable.second_chance_img);
                            tv_no_result.setText("You don't have any unseen skipped profiles right now.");
                        } else {
                            rlNoResult.setVisibility(View.GONE);
                            recycle.setVisibility(View.VISIBLE);
                            setAdapter(disLikelist);
                        }
                    }

                } else {
                    if (isListEmpty(list) && LikedUser > 0) {
                        pagecount = 1;
                        recycle.setVisibility(View.GONE);
                        tv_no_result.setText("");
                        callApi(pagecount);
                    } else if (iscallApiforlike) {
                        iscallApiforlike = false;
                        pagecount = 1;
                        recycle.setVisibility(View.GONE);
                        tv_no_result.setText("");
                        callApi(pagecount);
                    } else {
                        if (LikedUser == 0) {
                            tv_no_result.setVisibility(View.VISIBLE);
                            rlNoResult.setVisibility(View.VISIBLE);
                            ivNoResult.setImageResource(R.drawable.no_like_img);
                            recycle.setVisibility(View.GONE);
                            tv_no_result.setText("New likes will appear here. You don't have any likes right now.");
                        } else {
                            rlNoResult.setVisibility(View.GONE);
                            recycle.setVisibility(View.VISIBLE);
                            setAdapter(list);
                        }
                    }
                    /*if (recycle.getVisibility() == View.GONE) {
                        recycle.setVisibility(View.VISIBLE);
                    }*/
                }
                tv_subscribe.setText(subscribe_txts[tab.getPosition()]);
                btn_see_people.setText(btn_txt[tab.getPosition()]);
                //setAdapter(list);
                // recycle.setAdapter(new LikesImagesAdapter(getContext(), list, onBtnClick));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private boolean isListEmpty(List<User> list) {
        return list == null || list.size() == 0;
    }

/*    @Override
    public void OnClickContinue() {
        setPremiumData();
    }*/

    void setPremiumData() {
        Log.e(TAG, "setPremiumData: " + onBtnClick);
        onBtnClick = true;
        tv_subscribe.setVisibility(View.GONE);
        btn_see_people.setVisibility(View.GONE);
        // recycle.getAdapter().notifyDataSetChanged();
        //setAdapter(list);
        //recycle.setAdapter(new LikesImagesAdapter(getContext(), list, onBtnClick));
    }

    @Override
    public void onClickToken(String tokenType, int tokensNum, int selectedPos) {
        tokenSType = tokenType;
        selectedPosition = tokensNum;
        if (tokenType.equalsIgnoreCase("PremiumPurchase")) {
            price = CommonDialogs.PremiumPriceList.get(selectedPos).getPriceValue();
            productId = CommonDialogs.PremiumArr[selectedPos];
            if (fragClient!=null&&fragClient.isReady()){
                setOnPurchaseListener(LikesFrament.this);
                fragClient.launchBillingFlow(mActivity,getBillingFlowParam(CommonDialogs.PremiumSkuList.get(selectedPos)));
            }
            /*bp.subscribe(getActivity(), productId);*/
        }
    }

  /*  @Override
    public void onProductPurchased(String productId, TransactionDetails details) {

    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {
    }

    @Override
    public void onBillingInitialized() {

    }*/

    @Override
    public void onSuccessWhoLikedYou(WhoLikedYouReponce response) {
        // getBaseActivity().showSnackBar(llRootView, response.getMessage());
      /*  if (getBaseActivity() != null)
            getBaseActivity().hideLoading();
        else {
            ((BaseActivity) getActivity()).hideLoading();
        }*/
        hideLoading();
        if (list == null)
            list = new ArrayList<>();

        if (list.size() == 0) {
            if (response.getUsers() != null && response.getUsers().size() == 0) {
                recycle.setVisibility(View.GONE);
                tv_no_result.setVisibility(View.VISIBLE);
                rlNoResult.setVisibility(View.VISIBLE);
                ivNoResult.setImageResource(R.drawable.no_like_img);
                tv_no_result.setText("New likes will appear here. You don't have any likes right now.");
            } else {
                rlNoResult.setVisibility(View.GONE);
                recycle.setVisibility(View.VISIBLE);
                list = response.getUsers();
                if (isFromOnCreate) {
                    isFromOnCreate = false;
                    new Handler().postDelayed(() -> setAdapter(list), 200);
                } else {
                    setAdapter(list);
                }
            }
        } else {
            setLoadMore(response.getUsers().size() == loadMoreRange + 1);
            size = list.size();
            list.addAll(response.getUsers());
            recycle.getAdapter().notifyItemRangeInserted(size, list.size());
        }
    }

    @Override
    public void onError(String error) {
        getBaseActivity().hideLoading();
        if (isSencondPressed) {
            disliked = 0;
            recycle.setVisibility(View.GONE);
            tv_no_result.setText("You don't have any unseen skipped profiles right now.");
        } else {
            if (list.size() == 0) {
                recycle.setVisibility(View.GONE);
                tv_no_result.setText("New likes will appear here. You don't have any likes right now.");
            } else {
                setLoadMore(false);
                getBaseActivity().showSnackbar(llRootView, "You don't have any likes now.");
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mActivity = (BaseActivity) getActivity();
        if (requestCode == 111 && resultCode == Activity.RESULT_OK) {
            new Handler().postDelayed(() -> setLikeCount((getBaseActivity().sp.getNoOfLikes().getUsers() - 1)), 1000);
            if (recycle.getAdapter() != null) {
                list.remove(posAdapter);
                recycle.getAdapter().notifyItemRemoved(posAdapter);
                if (list.size() == 0) {
                    recycle.setVisibility(View.GONE);
                    tv_no_result.setVisibility(View.VISIBLE);
                    rlNoResult.setVisibility(View.VISIBLE);
                    ivNoResult.setImageResource(R.drawable.no_like_img);
                    tv_no_result.setText("New likes will appear here. You don't have any likes right now.");
                }
                //getBaseActivity().getMyProfile(this);
            }
        } else if (requestCode == 222 && resultCode == Activity.RESULT_OK) {
            //new Handler().postDelayed(() -> setLikeCount((getBaseActivity().sp.getNoOfLikes().getUsers() - 1)), 1000);
            if (recycle.getAdapter() != null) {
                disLikelist.remove(posAdapter);
                recycle.getAdapter().notifyItemRemoved(posAdapter);
                if (disLikelist.size() == 0) {
                    recycle.setVisibility(View.GONE);
                    tv_no_result.setVisibility(View.VISIBLE);
                    rlNoResult.setVisibility(View.VISIBLE);
                    ivNoResult.setImageResource(R.drawable.second_chance_img);
                    tv_no_result.setText("You don't have any unseen skipped profiles right now.");
                }
                // getBaseActivity().getMyProfile(this);
            }
        }/* else if (!bp.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }*/
    }


    @Override
    public void onSuccessDislikedList(WhoLikedYouReponce response) {
        hideLoading();
        try {
            getBaseActivity().sp.setDislikeApi(false);
            if (response.getUsers() == null || response.getUsers().size() == 0) {
                disliked = 0;
                recycle.setVisibility(View.GONE);
                tv_no_result.setVisibility(View.VISIBLE);
                rlNoResult.setVisibility(View.VISIBLE);
                ivNoResult.setImageResource(R.drawable.second_chance_img);
                tv_no_result.setText("You don't have any unseen skipped profiles right now.");
            } else {
                recycle.setVisibility(View.VISIBLE);
                tv_no_result.setVisibility(View.GONE);
                rlNoResult.setVisibility(View.GONE);
                disLikelist = response.getUsers();
                disliked = disLikelist.size();
                setAdapter(disLikelist);
            }
        } catch (Exception e) {
            if (tv_no_result != null) {
                tv_no_result.setVisibility(View.VISIBLE);
                ivNoResult.setImageResource(R.drawable.second_chance_img);
                rlNoResult.setVisibility(View.VISIBLE);
                tv_no_result.setText("You don't have any unseen skipped profiles right now.");
            }
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorNoDeluxe(String errorMessage) {
        recycle.setVisibility(View.GONE);
        tv_no_result.setText(errorMessage);
        // getBaseActivity().showSnackBar(llRootView, errorMessage);
        hideLoading();
    }

    @Override
    public void setProfileData() {
        LikedUser = getBaseActivity().sp.getNoOfLikes() != null && getBaseActivity().sp.getNoOfLikes().getUsers() > 0 ? getBaseActivity().sp.getNoOfLikes().getUsers() : 0;
        if (LikedUser > 0) {
            if (MyLikeCount != LikedUser || ((HomeActivity) getActivity()).LikeUserlist.isEmpty()) {
                if (!isSencondPressed) {
                    iscallApiforlike = false;
                    getBaseActivity().sp.savePreviousLikeCount(LikedUser);
                    pagecount = 1;
                    callApi(pagecount);
                } else {
                    iscallApiforlike = true;
                }
            } else {
                hideLoading();
            }
        } else {
            hideLoading();
            if (!((HomeActivity) getActivity()).LikeUserlist.isEmpty())
                ((HomeActivity) getActivity()).LikeUserlist.clear();
            recycle.setVisibility(View.GONE);
            tv_no_result.setVisibility(View.VISIBLE);
            ivNoResult.setImageResource(R.drawable.no_like_img);
            rlNoResult.setVisibility(View.VISIBLE);
            tv_no_result.setText("New likes will appear here. You don't have any likes right now.");
        }
        if (getBaseActivity() != null) {
            String txt;
            String txt2;
            if (getBaseActivity().sp.getNoOfLikes().getUsers() == 1) {
                txt = getBaseActivity().sp.getNoOfLikes().getUsers() + " Person";
                txt2 = txt + "\nLikes You";
            } else {
                int cout = getBaseActivity().sp.getNoOfLikes().getUsers();
                txt = cout > 99 ? "99+ People" : cout + " People";
                txt2 = txt + "\nLike You";
            }
            SpannableString spannable = new SpannableString(txt2);
            spannable.setSpan(new ForegroundColorSpan(Color.RED), 0, txt.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tab_like.getTabAt(0).setText(spannable);
        }
    }

    public void setLikeCount(int count) {
        LikedUser = count;
        if (getBaseActivity() != null) {
            String txt;
            String txt2;
            if (count == 1) {
                txt = "" + count + " Person";
                txt2 = txt + "\nLikes You";
            } else {
                //int cout = getBaseActivity().sp.getNoOfLikes().getUsers();
                txt = count > 99 ? "99+ People" : count + " People";
                txt2 = txt + "\nLike You";
            }
            SpannableString spannable = new SpannableString(txt2);
            spannable.setSpan(new ForegroundColorSpan(Color.RED), 0, txt.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tab_like.getTabAt(0).setText(spannable);
            Log.e(TAG, "setProfileData: " + count + "  txt " + txt2);
        }
    }

    @Override
    public void onLikeItemClick(int pos) {
        posAdapter = pos;
        Intent intent = new Intent(mContext, UserCardActivity.class);
        if (tab_like.getSelectedTabPosition() == 0) {
            if (list.size() > pos)
                intent.putExtra("userid", list.get(pos).getId());
            showSencondChance = false;
            myIntentReqCode = 111;
        } else {
            showSencondChance = true;
            if (disLikelist.size() > pos)
                intent.putExtra("userid", disLikelist.get(pos).getId());
            myIntentReqCode = 222;
        }
        intent.putExtra("tabPos", 1);
        intent.putExtra("isfromLike", true);
        ((HomeActivity) mContext).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        if (intent.hasExtra("userid")) {
            startActivityForResult(intent, myIntentReqCode);
        }
        /*context.startActivity(new Intent(context, UserCardActivity.class).
        putExtra("userid", list.get(position).getId()).
        putExtra("tabPos", 1).putExtra("isfromLike", true));
        ((HomeActivity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);*/
    }

    @Override
    public void OnSuccessPurchase(Purchase purchase) {
        if (tokenSType.equalsIgnoreCase("PremiumPurchase")) {
            Toast.makeText(getContext(), "Item Purchased", Toast.LENGTH_LONG).show();
            if (fragClient!=null&&fragClient.isReady()){
                mActivity.showLoading();
                fragClient.acknowledgePurchase(getAcknowledgeParams(purchase.getPurchaseToken()),
                        (billingResult) -> homeViewModel.addPremiumRequest(new PremiumTokenCountModel("1",
                                productId,
                                price,
                                Integer.parseInt(productId.split("_")[2]),
                                purchase.getOrderId(),
                                purchase.getPurchaseToken(),
                                CommonUtils.getDateForPurchase(purchase.getPurchaseTime()),
                                purchase.getSignature(),
                                BaseActivity.purchaseState)));
            }
            Log.e(TAG, "purchase success PremiumPurChase: " + purchase);
        }
    }

    @Override
    public void OnGetPurchaseDetail(SubscriptionResponse body) {

    }
}
