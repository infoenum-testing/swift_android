package com.swift.dating.ui.homeScreen.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.swift.dating.R;
import com.swift.dating.common.CommonDialogs;
import com.swift.dating.common.CommonUtils;
import com.swift.dating.common.MyProgressDialog;
import com.swift.dating.data.network.ApiCall;
import com.swift.dating.data.network.ApiCallback;
import com.swift.dating.data.network.Resource;
import com.swift.dating.model.BaseModel;
import com.swift.dating.model.requestmodel.DeluxeTokenCountModel;
import com.swift.dating.model.requestmodel.FilterRequest;
import com.swift.dating.model.responsemodel.User;
import com.swift.dating.model.responsemodel.UserListResponseModel;
import com.swift.dating.model.responsemodel.WhoLikedYouReponce;
import com.swift.dating.ui.base.BaseActivity;
import com.swift.dating.ui.base.BaseFragment;
import com.swift.dating.ui.homeScreen.FilterActivity;
import com.swift.dating.ui.homeScreen.HomeActivity;
import com.swift.dating.ui.homeScreen.adapter.SearchUserAdapter;
import com.swift.dating.ui.homeScreen.viewmodel.HomeViewModel;

import static com.swift.dating.common.AppConstants.LICENSE_KEY;

public class SearchFragment extends BaseFragment implements CommonDialogs.onProductConsume, BillingProcessor.IBillingHandler, ApiCallback.GetSearchFilterCallback {
    private static final String TAG = "SearchFragment";
    private static RecyclerView recycle;
    private static int totalItemsViewed = 0;
    private final int loadMoreRange = 19;
    private HomeViewModel homeViewModel;
    private double price;
    private String productId, tokenSType;
    private int selectedPosition;
    private GridLayoutManager manager;
    public static final String SearchResponse = "searchResponse";
    public static final String FilterResponse = "filterResponse";
    public static final String MyPageIndex = "pageIndex";
    public static final String RecycleLastPos = "recycleLastPos";
    private Gson gson;
    private Button btn_search;
    private ImageView img_filter;
    private TextView tv_txt_deluxe;
    private LinearLayout llRootView;
    private BillingProcessor bp;
    private ArrayList<User> list;
    private boolean isDeluxe = false, scrollDown = false;
    private FilterRequest filterRequest;
    private int pageCount = 1;
    private MyProgressDialog mProgressDialog;
    private TextView tv_no_data;
    private boolean IsLoadMore, isFilterApply;
    private int posAdapter;
    private int oldFirstPos = -1;
    private int oldLastPos = -1;

    public static void setRecycleToTop() {
        recycle.scrollToPosition(0);
        totalItemsViewed = 0;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_search;
    }

    @Override
    public void onPause() {
        if (getBaseActivity() != null && getBaseActivity().sp.getDeluxe()) {
            getBaseActivity().sp.saveString(MyPageIndex, "" + pageCount);
            getBaseActivity().sp.saveString(RecycleLastPos, "" + posAdapter);
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mActivity = (BaseActivity) getActivity();
        ((HomeActivity) getActivity()).mToolbar.setVisibility(View.GONE);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mActivity = (BaseActivity) getActivity();
        Log.e(TAG, "onViewCreated: SearchFragment" );
        if (getBaseActivity().isNetworkConnected()) {
            initViews(view);
        } else {
            getBaseActivity().showSnackbar(view, "Please connect to internet");
        }
    }

    public void showLoading() {
        if (mProgressDialog == null || !mProgressDialog.isShowing()) {
            mProgressDialog = new MyProgressDialog(mActivity);
        }
    }

    public void hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    private void CallSearchFilterApi() {
        showLoading();
        HashMap<String, Object> map = new HashMap<>();
        map.put(filterRequest.getPageNumberKey(), pageCount);
        map.put(filterRequest.getLimitKey(), filterRequest.getLimit());
        map.put(filterRequest.getDistanceKey(), filterRequest.getDistance());
        map.put(filterRequest.getGenderKey(), filterRequest.getGender());
        map.put(filterRequest.getMaxAgePreferKey(), filterRequest.getMaxAgePrefer());
        map.put(filterRequest.getMinAgePreferKey(), filterRequest.getMinAgePrefer());
        map.put(filterRequest.getMaxHeightKey(), filterRequest.getMaxHeight());
        map.put(filterRequest.getMinHeightKey(), filterRequest.getMinHeight());
        if (filterRequest.getLookingFor() != null)
            map.put(filterRequest.getLookingForKey(), filterRequest.getLookingFor());
        //if (filterRequest.getMaxHeight() != null)

        // if (filterRequest.getMinHeight() != null)

        if (filterRequest.getKids() != null)
            map.put(filterRequest.getKidsKey(), filterRequest.getKids());
        if (filterRequest.getPolitical() != null)
            map.put(filterRequest.getPoliticalKey(), filterRequest.getPolitical());
        if (filterRequest.getReligion() != null)
            map.put(filterRequest.getReligionKey(), filterRequest.getReligion());
        if (filterRequest.getSmoke() != null)
            map.put(filterRequest.getSmokeKey(), filterRequest.getSmoke());
        if (filterRequest.getEducation() != null)
            map.put(filterRequest.getEducationKey(), filterRequest.getEducation());
        Log.e(TAG, "CallSearchFilterApi: " + map);
        ApiCall.getSearchFilterList(getBaseActivity().sp.getToken(), map, this);
    }

    private void setLoadMore(boolean loadMore) {
        IsLoadMore = loadMore;
    }

    private void setResponseData() {
        if (list.size() > 0) {
            recycle.setVisibility(View.VISIBLE);
            recycle.setAdapter(new SearchUserAdapter(mActivity, list, isDeluxe, SearchFragment.this));
            if (posAdapter != -1 && getBaseActivity().sp.getDeluxe() && posAdapter < list.size()) {
                manager.scrollToPosition(posAdapter);
            }
        } /*else {
            tv_no_data.setText("No profiles were found based on your criteria. Please edit your filters and try again.");
            recycle.setVisibility(View.GONE);
        }*/
    }

    private void initViews(View view) {
        gson = new Gson();
        //  setLoadMore(true);
        if (!TextUtils.isEmpty(getBaseActivity().sp.getMyString(MyPageIndex)) && getBaseActivity().sp.getDeluxe())
            pageCount = Integer.parseInt(getBaseActivity().sp.getMyString(MyPageIndex));
        else pageCount = 1;
        if (!TextUtils.isEmpty(getBaseActivity().sp.getMyString(RecycleLastPos)) && getBaseActivity().sp.getDeluxe())
            posAdapter = Integer.parseInt(getBaseActivity().sp.getMyString(RecycleLastPos));
        else posAdapter = -1;
       /* try {
            CommonUtils.jsonToMap("{\"phonetype\":\"N95\",\"cat\":\"WP\"}");
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        initBillingProcess();
        tv_no_data = view.findViewById(R.id.tv_no_data);
        tv_txt_deluxe = view.findViewById(R.id.tv_txt_deluxe);
        llRootView = view.findViewById(R.id.llRootView);
        btn_search = view.findViewById(R.id.btn_search);
        recycle = view.findViewById(R.id.recycle);
        img_filter = view.findViewById(R.id.img_filter);
        img_filter.setOnClickListener(this::onClick);
        manager = new GridLayoutManager(getContext(), 3);
        recycle.setLayoutManager(manager);
        btn_search.setOnClickListener(this::onClick);
        if (getBaseActivity().sp.getDeluxe()) setDeluxeData();
        else {
            if (getBaseActivity().sp.getFilterModel() != null) {
                getBaseActivity().sp.removeFilter();
                getBaseActivity().sp.removeKey(FilterResponse);
            }
        }
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        if (getBaseActivity().sp.getVerified().equalsIgnoreCase("Yes")) {
            recycle.setVisibility(View.GONE);
            if (getBaseActivity().sp.getFilterModel() == null) {
                isFilterApply = false;
                getListFromSp();
                if (list == null)
                    CallAllUserApi(pageCount);
                else {
                    setLoadMore(true);
                    setResponseData();
                }
            } else {
                isFilterApply = true;
                filterRequest = getBaseActivity().sp.getFilterModel();
                getListFromSp();
                if (list == null)
                    CallSearchFilterApi();
                else {
                    setLoadMore(true);
                    setResponseData();
                }
            }
        } else {
            recycle.setVisibility(View.GONE);
            tv_no_data.setText(getBaseActivity().sp.isRejected() ? getString(R.string.profileIdRejected) : getString(R.string.waitingList));
        }
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
                            getBaseActivity().sp.saveDeluxe(true);
                            setDeluxeData();
                            setLoadMore(true);
                            ((SearchUserAdapter) recycle.getAdapter()).setUnlock(isDeluxe);
                            recycle.getAdapter().notifyDataSetChanged();
                        } else if (resource.code == 401) {
                            getBaseActivity().openActivityOnTokenExpire();
                        } else {
                            getBaseActivity().showSnackbar(llRootView, "Something went wrong");
                        }
                        break;
                    case ERROR:
                        hideLoading();
                        getBaseActivity().showSnackbar(llRootView, resource.message);
                        break;
                }

            }
        });
        homeViewModel.userListAllResponse().observe(this, new Observer<Resource<UserListResponseModel>>() {
            @Override
            public void onChanged(Resource<UserListResponseModel> resource) {
                if (resource == null) {
                    return;
                }
                switch (resource.status) {
                    case SUCCESS:
                        hideLoading();
                        list = resource.data.getUsers();
                        if (list != null && list.size() > 0) {
                            recycle.setVisibility(View.VISIBLE);
                            recycle.setAdapter(new SearchUserAdapter(mActivity, list, isDeluxe, SearchFragment.this));
                        } else {
                            // tv_no_data.setText("No User Found");
                            tv_no_data.setText("No profiles were found based on your criteria. Please edit your filters and try again.");
                            recycle.setVisibility(View.GONE);
                        }
                        break;
                    case LOADING:
                        break;
                    case ERROR:
                        Log.e(TAG, "onChanged: Error" + resource.data.getError());
                        hideLoading();
                        getBaseActivity().showSnackbar(llRootView, resource.message);
                        break;
                }
            }
        });
        recycle.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE && getBaseActivity().sp.getDeluxe()) {
                    if (IsLoadMore && recycle.getAdapter().getItemCount() > loadMoreRange) {
                        if (manager != null && manager.findLastCompletelyVisibleItemPosition() == list.size() - 1) {
                            pageCount++;
                            if (isFilterApply) {
                                CallSearchFilterApi();
                            } else {
                                CallAllUserApi(pageCount);
                            }
                            setLoadMore(false);
                        }
                    }
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int currFirstPos = manager.findFirstCompletelyVisibleItemPosition();
                int currLastPos = manager.findLastCompletelyVisibleItemPosition();
                posAdapter = currFirstPos;
                if (!getBaseActivity().sp.getDeluxe()) {
                    Log.e(TAG, "onScrolled: LastVisibleItemPos" + posAdapter);
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
                    if (manager.getItemCount() > 6 && !scrollDown && totalItemsViewed != 0 /*&& totalItemsViewed % 6 == 0*/) {
                        totalItemsViewed = 0;
                        //CommonDialogs.isDialogOpen = true;
                        CommonDialogs.DeluxePurChaseDialog(getContext(), SearchFragment.this);
                        recyclerView.stopScroll();
                    }
                }
            }
        });

    }

    private void getListFromSp() {
        String json = !isFilterApply ? getBaseActivity().sp.getMyString(SearchResponse) : getBaseActivity().sp.getMyString(FilterResponse);
        Type type = new TypeToken<List<User>>() {
        }.getType();
        list = gson.fromJson(json, type);
    }

    private void CallAllUserApi(int count) {
        filterRequest = new FilterRequest();
        showLoading();
        HashMap<String, Object> map = new HashMap<>();
        map.put(filterRequest.getPageNumberKey(), count);
        map.put(filterRequest.getLimitKey(), filterRequest.getLimit());
        map.put(filterRequest.getDistanceKey(), filterRequest.getDistance());
        map.put(filterRequest.getGenderKey(), filterRequest.getGender());
        map.put(filterRequest.getMaxAgePreferKey(), filterRequest.getMaxAgePrefer());
        map.put(filterRequest.getMinAgePreferKey(), filterRequest.getMinAgePrefer());
        map.put(filterRequest.getMaxHeightKey(), filterRequest.getMaxHeight());
        map.put(filterRequest.getMinHeightKey(), filterRequest.getMinHeight());
        ApiCall.getSearchFilterList(getBaseActivity().sp.getToken(), map, this);
        //homeViewModel.getUserListAllRequest(getBaseActivity().sp.getToken());
    }

    private void initBillingProcess() {
        bp = new BillingProcessor(getActivity(), LICENSE_KEY, this);
        bp.initialize();
    }
/*
    @Override
    public void OnClickContinue() {
        setDeluxeData();
    }
*/

    private void setDeluxeData() {
        tv_txt_deluxe.setVisibility(View.GONE);
        btn_search.setVisibility(View.GONE);
        isDeluxe = true;
        //recycle.setAdapter(new SeacrchUserAdapter(getContext(), null, true));
    }

    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_search:
                getBaseActivity().sp.setDialogOpen(true);
                if (getBaseActivity().sp.getDeluxe()) {
                    CommonDialogs.showAlreadyPremiumUser(getContext(), getContext().getResources().getString(R.string.you_have_active_deluxe_subscription));
                } else {
                    //CommonDialogs.purchaseDialog(getContext(), "BlackGentry Premium", "", this);
                    // CommonDialogs.isDialogOpen = true;
                    CommonDialogs.DeluxePurChaseDialog(getContext(), this);
                }
                /*CommonDialogs dialogs = CommonDialogs.DeluxePurChaseDialog(getContext(), this);
                dialogs.setOnDeluxeContinuebtn(this);*/
                break;
            case R.id.img_filter:
                startActivityForResult(new Intent(getActivity(), FilterActivity.class), 2323);
                getActivity().overridePendingTransition(R.anim.slide_in_top, R.anim.nothing);
                break;
        }
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
            showLoading();
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
        Log.e(TAG, " errorCode=" + errorCode);
    }

    @Override
    public void onBillingInitialized() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 2323 && resultCode == 2323) {
            if (getBaseActivity().sp.getFilterModel() != null) {
                isFilterApply = true;
                isDeluxe = getBaseActivity().sp.getDeluxe();
                filterRequest = getBaseActivity().sp.getFilterModel();
                pageCount = 1;
                getBaseActivity().sp.clearByKey(MyPageIndex);
                getBaseActivity().sp.clearByKey(RecycleLastPos);
                if (list != null && list.size() > 0) {
                    list.clear();
                    getBaseActivity().sp.clearByKey(FilterResponse);
                }
                CallSearchFilterApi();
            } else {
                getBaseActivity().sp.clearByKey(MyPageIndex);
                getBaseActivity().sp.clearByKey(RecycleLastPos);
                isFilterApply = false;
                isDeluxe = getBaseActivity().sp.getDeluxe();
                pageCount = 1;
                if (list != null && list.size() > 0)
                list.clear();
                CallAllUserApi(pageCount);
                /*getListFromSp();
                if (list == null) {
                    pageCount = 1;
                    CallAllUserApi(pageCount);
                } else {
                    setResponseData();
                    recycle.getAdapter().notifyDataSetChanged();
                }*/
            }
        } else if (!bp.handleActivityResult(requestCode, resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSuccessSearchFilterList(WhoLikedYouReponce response) {
        hideLoading();
        if (list == null)
            list = new ArrayList<>();

        if (list.size() == 0) {
            if (response.getUsers()==null||response.getUsers().size() == 0) {
                recycle.setVisibility(View.GONE);
                tv_no_data.setText("No profiles were found based on your criteria. Please edit your filters and try again.");
            } else {
                setLoadMore(true);
                recycle.setVisibility(View.VISIBLE);
                list = (ArrayList<User>) response.getUsers();
                recycle.setAdapter(new SearchUserAdapter(mActivity, list, isDeluxe, SearchFragment.this));
                recycle.getAdapter().notifyDataSetChanged();
            }
        } else {
            setLoadMore(response.getUsers().size() == loadMoreRange + 1);
            int size = list.size();
            list.addAll(response.getUsers());
            recycle.getAdapter().notifyItemRangeInserted(size, list.size());
        }
        Log.e(TAG, "onSuccessSearchFilterList: " + list.size());
        if (list != null && list.size() > 0) {
            if (isFilterApply) {
                getBaseActivity().sp.saveString(FilterResponse, gson.toJson(list));
            } else {
                getBaseActivity().sp.saveString(SearchResponse, gson.toJson(list));
            }
        }
       /* hideLoading();
        list = (ArrayList<User>) response.getUsers();
        if (list.size() > 0) {
            recycle.setVisibility(View.VISIBLE);
            recycle.setAdapter(new SearchUserAdapter(mActivity, list, isDeluxe, SearchFragment.this));
        } else {
            tv_no_data.setText("No profiles were found based on your criteria. Please edit your filters and try again.");
            recycle.setVisibility(View.GONE);
        }*/
    }

    @Override
    public void NoUsermatched(String messge) {
        hideLoading();
        //getBaseActivity().showSnackBar(llRootView, messge);
        //  tv_no_data.setText(messge);
        if (list == null || list.size() == 0) {
            tv_no_data.setText("No profiles were found based on your criteria. Please edit your filters and try again.");
            recycle.setVisibility(View.GONE);
        }/* else {
            showSnackBar(recycle, "No profiles were found based on your criteria. Please edit your filters and try again.");
        }*/
        /*if (messge.equalsIgnoreCase("Your not a deluxe user !")) {
            CallAllUserApi(pageCount);
            //getBaseActivity().sp.removeFilter();
        }*/
    }

    @Override
    public void onError(String error) {
        hideLoading();
        //getBaseActivity().showSnackBar(llRootView, error);
        if (list == null || list.size() == 0) {
            tv_no_data.setText(error);
            recycle.setVisibility(View.GONE);
        }/* else {
            showSnackBar(recycle, error);
        }*/
    }
}
