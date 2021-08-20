package com.swiftdating.app.ui.userCardScreen


import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.Window
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.DefaultItemAnimator
import com.swiftdating.app.R
import com.swiftdating.app.callbacks.ReportInterface
import com.swiftdating.app.common.*
import com.swiftdating.app.data.network.*
import com.swiftdating.app.model.DetailTagModel
import com.swiftdating.app.model.requestmodel.ReactRequestModel
import com.swiftdating.app.model.requestmodel.ReportRequestModel
import com.swiftdating.app.model.requestmodel.SuperLikeCountModel
import com.swiftdating.app.model.responsemodel.*
import com.swiftdating.app.ui.base.BaseActivity
import com.swiftdating.app.ui.chatScreen.ChatWindow
import com.swiftdating.app.ui.homeScreen.adapter.CardProfileAdapter
import com.swiftdating.app.ui.homeScreen.adapter.DetailTagAdapter
import com.swiftdating.app.ui.homeScreen.viewmodel.HomeViewModel
import com.swiftdating.app.ui.viewpagerScreen.ViewPagerActivity
import com.anjlab.android.iab.v3.BillingProcessor
import com.anjlab.android.iab.v3.TransactionDetails
import com.bumptech.glide.Glide
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.yuyakaido.android.cardstackview.*
import kotlinx.android.synthetic.main.activity_my_card.*
import kotlinx.android.synthetic.main.activity_my_card.cancel
import kotlinx.android.synthetic.main.activity_my_card.clView
import kotlinx.android.synthetic.main.detailview.*
import okhttp3.ResponseBody
import java.util.*
import kotlin.collections.ArrayList

class UserCardActivity : BaseActivity(), ReportInterface, CommonDialogs.onProductConsume, BillingProcessor.IBillingHandler, CardStackListener, ApiCallback.ReportUserCallBack {
    lateinit var homeViewModel: HomeViewModel
    var customPagerAdapter: CustomPagerAdapter? = null
    var instaImageList: MutableList<InstagramImageModel.Datum> = ArrayList()

    var isfromSearch = false
    var adapter: DetailTagAdapter? = null
    var tagList: MutableList<DetailTagModel> = ArrayList()
    var tapPos = 3
    lateinit var list: List<User>
    private val manager by lazy { CardStackLayoutManager(this, this) }
    //public val card_stack_viewMy by lazy { view?.findViewById<CardStackView>(R.id.card_stack_viewMy) }


    /*   val like by lazy { view?.findViewById<ImageView>(R.id.love) }
       val superlike by lazy { view?.findViewById<ImageView>(R.id.superlike) }
   */
    private var CHAT_REQUEST = 10000
    lateinit var like: ImageView
    lateinit var superlike: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_card)
        like = findViewById(R.id.love)
        superlike = findViewById(R.id.superlike)
        init()
        subscribeModel()
        initBillingProcess()
        showLoading()
        homeViewModel.userDataRequest(intent.extras!!.getInt("userid").toString())
        //|| intent != null && intent.getBooleanExtra("isFromSearch", false)
        if (intent != null && intent.getBooleanExtra("isfromLike", false)) {
            clView.visibility = VISIBLE
            view1.visibility = View.VISIBLE
            CHAT_REQUEST = 2021
            isfromSearch = true
            //tv_preview.visibility = View.VISIBLE
        } else {
            isfromSearch = false
        }
        if (intent != null && intent.getBooleanExtra("isFromSearch", false)) {
            cl_second_chat.visibility = VISIBLE
            clView.visibility = GONE
            isfromSearch = true
        } else {
            isfromSearch = false
        }
        /*else if (intent != null && intent.getBooleanExtra("isFromSearch", false)) {
            cl_second_chat.visibility = VISIBLE
           // like.visibility = GONE
           // superlike.visibility = GONE
        }*/
    }

    private fun initBillingProcess() {
        bp = BillingProcessor(mActivity, AppConstants.LICENSE_KEY, this)
        bp!!.initialize()
    }

    private val TAG = "UserCardActivity"

    /**
     *  Method to Initialize
     */
    private fun init() {
        tapPos = intent.getIntExtra("tabPos", 3)
        iv_back.setOnClickListener { onBackPressed() }
        cancel.setOnClickListener { Log.e(TAG, "init: Cancel") }
        left_overlay.visibility = View.GONE
        right_overlay.visibility = View.GONE
        top_overlay.visibility = View.GONE
        bottom_overlay.visibility = View.GONE
        view1.visibility = View.GONE
        tv_preview.visibility = View.INVISIBLE
        ll_superlike.visibility = View.GONE
        clParent2.visibility = View.GONE

    }

    override fun onResume() {
        mActivity = this
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStart() {
        super.onStart()
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                IntentFilter("MyData"))
    }

    override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver)
    }


    /**
     *  Method to set Data
     */
    private fun setData(obj: ProfileOfUser, imagelist: List<ImageForUser>, data: String, instaList: List<InstagramImageModel.Datum>, id: String) {
        var percent = 75.0
        if (sp.profileCompleted != null && sp.profileCompleted != "")
            percent = sp.profileCompleted.toDouble()
        iv_back.setOnClickListener { onBackPressed() }
        val chat = findViewById<View>(R.id.chat)
        chat?.setOnClickListener {
            if (percent >= 75) {
                val intent1 = Intent(mContext, ChatWindow::class.java)
                intent1.putExtra("id", id.toInt())
                intent1.putExtra("name", obj.name)
                intent1.putExtra("tabPos", tapPos)
                intent1.putExtra("isExpired", false)
                intent1.putExtra("isFromCard", true)
                intent1.putExtra("image", imagelist[0].imageUrl)
                if (isfromSearch) {
                    intent1.putExtra("isFromSearch", true)
                }
                startActivityForResult(intent1, CHAT_REQUEST)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            } else {
                showSnackbar(chat, resources.getString(R.string.less_than_75))
            }
        }
        val chat2 = findViewById<View>(R.id.chat2)
        chat2?.setOnClickListener {
            if (percent >= 75) {
                val intent1 = Intent(mContext, ChatWindow::class.java)
                intent1.putExtra("id", id.toInt())
                intent1.putExtra("name", obj.name)
                intent1.putExtra("tabPos", tapPos)
                intent1.putExtra("isExpired", false)
                intent1.putExtra("isFromCard", true)
                intent1.putExtra("image", imagelist[0].imageUrl)
                if (isfromSearch) {
                    intent1.putExtra("isFromSearch", true)
                }
                startActivityForResult(intent1, CHAT_REQUEST)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            } else {
                showSnackbar(chat, resources.getString(R.string.less_than_75))
            }
        }
        superlike?.setOnClickListener {
            val user = sp.user
            val myobj: ProfileOfUser = Gson().fromJson(user, ProfileOfUser::class.java)
            val superlike = view?.findViewById<View>(R.id.superlike)
            if (myobj.completed > 75) {
                if (myobj.superLikesCount > 0) {
                    val setting = SwipeAnimationSetting.Builder()
                            .setDirection(Direction.Top)
                            .setDuration(Duration.Normal.duration)
                            .setInterpolator(AccelerateInterpolator())
                            .build()
                    manager.setSwipeAnimationSetting(setting)

                    homeViewModel.getUserReactRequest(ReactRequestModel("superlike", id.toInt()))
                    myobj.superLikesCount = myobj.superLikesCount - 1
                    sp.saveUserData(myobj, sp.profileCompleted)
                } else {
                    CommonDialogs.CrushPurChaseDialog(mActivity, this)
                }
            } else {
                showSnackbar(superlike, mContext!!.resources.getString(R.string.less_than_75))
            }


        }
        like.setOnClickListener {
            val user = sp.user
            val myobj: ProfileOfUser = Gson().fromJson(user, ProfileOfUser::class.java)
            if (myobj.completed > 75) {
                val setting = SwipeAnimationSetting.Builder()
                        .setDirection(Direction.Right)
                        .setDuration(Duration.Normal.duration)
                        .setInterpolator(AccelerateInterpolator())
                        .build()
                manager.setSwipeAnimationSetting(setting)

                homeViewModel.getUserReactRequest(ReactRequestModel("like", id.toInt()))

            } else {
                showSnackbar(superlike, mContext!!.resources.getString(R.string.less_than_75))
            }
        }

        val dislike = findViewById<View>(R.id.cancel)
        dislike?.setOnClickListener {
            val user = sp.user
            val myobj: ProfileOfUser = Gson().fromJson(user, ProfileOfUser::class.java)
            if (myobj.completed > 75) {
                val setting = SwipeAnimationSetting.Builder()
                        .setDirection(Direction.Left)
                        .setDuration(Duration.Normal.duration)
                        .setInterpolator(AccelerateInterpolator())
                        .build()
                manager.setSwipeAnimationSetting(setting)

                homeViewModel.getUserReactRequest(ReactRequestModel("dislike", id.toInt()))
            } else {
                showSnackbar(dislike, mContext!!.resources.getString(R.string.less_than_75))
            }
        }


        left_overlay.visibility = View.GONE
        right_overlay.visibility = View.GONE
        top_overlay.visibility = View.GONE
        bottom_overlay.visibility = View.GONE

        item_image.minimumHeight = ScreenUtils.getScreenHeight(mActivity) - (1 * ScreenUtils.getActionBarHeight(mActivity))
        item_image.maxHeight = ScreenUtils.getScreenHeight(mActivity) - (1 * ScreenUtils.getActionBarHeight(mActivity))

        ivUserImage2.maxHeight = 1700
        ivUserImage2.minimumHeight = 1700

        ivUserImage3.maxHeight = 1700
        ivUserImage3.minimumHeight = 1700

        ivUserImage4.maxHeight = 1700
        ivUserImage4.minimumHeight = 1700

        ivUserImage5.maxHeight = 1700
        ivUserImage5.minimumHeight = 1700

        ivUserImage6.maxHeight = 1700
        ivUserImage6.minimumHeight = 1700

        if (!TextUtils.isEmpty(obj!!.name)) {
            name.text = obj.name
        }
        if (!TextUtils.isEmpty(obj.dob)) {
            tvage.text = ", " + CommonUtils.getAge(obj.dob)
        }

        if (!TextUtils.isEmpty(data)) {
            if (!data.equals("No", ignoreCase = true)) {
                llLinkedin.visibility = View.VISIBLE
            } else {
                llLinkedin.visibility = View.GONE
            }
        } else {
            llLinkedin.visibility = View.GONE
        }

        if (!TextUtils.isEmpty(obj.city)) {
            tv_lives_in.text = obj.city
        } else
            cv_live.visibility = View.GONE

        if (!TextUtils.isEmpty(obj.question1)) {
            tvQuestion1.text = obj.question1
            tvAnswer1.text = obj.answer1
        } else
            cvQuestion1.visibility = View.GONE

        if (!TextUtils.isEmpty(obj.question2)) {
            tvQuestion2.text = obj.question2
            tvAnswer2.text = obj.answer2
        } else
            cvQuestion2.visibility = View.GONE

        if (!TextUtils.isEmpty(obj.question3)) {
            tvQuestion3.text = obj.question3
            tvAnswer3.text = obj.answer3
        } else
            cvQuestion3.visibility = View.GONE


        if (!TextUtils.isEmpty(obj.occupation))
            tvOccupation.text = obj.occupation
        else
            cv_occupation.visibility = View.GONE

        if (!TextUtils.isEmpty(obj.school))
            tvSchool.text = obj.school
        else
            cvSchool.visibility = View.GONE

        if (!TextUtils.isEmpty(obj.aboutme)) {
            cv_about_me.visibility = View.VISIBLE
            tv_about_me.text = obj.aboutme
        } else
            cv_about_me.visibility = View.GONE

        if (!TextUtils.isEmpty(obj.ambitions)) {
            cv_interest.visibility = View.VISIBLE
            tv_interest.text = obj.ambitions
        } else
            cv_interest.visibility = View.GONE



        Glide.with(item_image).load(CallServer.BaseImage + imagelist[0].imageUrl).centerCrop()
                .into(item_image);
        Glide.with(ivUserImage2).load(CallServer.BaseImage + imagelist[1].imageUrl).centerCrop()
                .into(ivUserImage2);
        Glide.with(ivUserImage3).load(CallServer.BaseImage + imagelist[2].imageUrl).centerCrop()
                .into(ivUserImage3);
        if (imagelist.size > 3) {
            Glide.with(ivUserImage4).load(CallServer.BaseImage + imagelist[3].imageUrl).centerCrop()
                    .into(ivUserImage4);
            if (imagelist.size > 4) {
                Glide.with(ivUserImage5).load(CallServer.BaseImage + imagelist[4].imageUrl).centerCrop()
                        .into(ivUserImage5);
                if (imagelist.size > 5) {
                    Glide.with(ivUserImage6).load(CallServer.BaseImage + imagelist[5].imageUrl).centerCrop()
                            .into(ivUserImage6);
                } else {
                    ivUserImage6.visibility = View.GONE
                }
            } else {
                ivUserImage5.visibility = View.GONE
                ivUserImage6.visibility = View.GONE

            }
        } else {
            ivUserImage4.visibility = View.GONE
            ivUserImage5.visibility = View.GONE
            ivUserImage6.visibility = View.GONE

        }

        if (instaList.isNotEmpty()) {
            instaView.visibility = VISIBLE

            val pages: Vector<View> = Vector<View>()
            if (instaList.size > 0) {
                val gridview1 = GridView(this)
                pages.add(gridview1)
                gridview1.numColumns = 3
                gridview1.adapter = ArrayAdapter<String>(this, R.layout.insta_photo_grid_view, arrayOf("A1", "B1", "C1", "D1", "E1", "F1"))
            }
            if (instaList.size > 6) {
                val gridview2 = GridView(this)
                pages.add(gridview2)
                gridview2.numColumns = 3
                gridview2.adapter = ArrayAdapter<String>(this, R.layout.insta_photo_grid_view, arrayOf("A2", "B2", "C2", "D2", "E2", "F2"))
            }
            if (instaList.size > 12) {
                val gridview3 = GridView(this)
                pages.add(gridview3)
                gridview3.numColumns = 3
                gridview3.adapter = ArrayAdapter<String>(this, R.layout.insta_photo_grid_view, arrayOf("A3", "B3", "C3", "D3", "E3", "F3"))
            }

            customPagerAdapter = CustomPagerAdapter(this, pages, instaList)
            view_pager.adapter = customPagerAdapter
            val tabLayout = findViewById<TabLayout>(R.id.tab_layout)
            tabLayout.setupWithViewPager(view_pager, true)
        }

        val layoutManager = FlexboxLayoutManager(mActivity)
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.flexWrap = FlexWrap.WRAP
        layoutManager.alignItems = AlignItems.STRETCH
        rv_about.layoutManager = layoutManager

        if (!TextUtils.isEmpty(obj.lookingFor)) {
            tagList.add(DetailTagModel(obj.lookingFor, R.drawable.ic_relation_white))
        }
        if (!TextUtils.isEmpty(obj.height)) {
            var hetS = obj.height
            if (hetS.contains(".")) {
                val het = hetS.toFloat()
                hetS = when {
                    het < 4 -> {
                        "< 4'0\""
                    }
                    het > 7 -> {
                        "> 7'0\""
                    }
                    else -> hetS.replace(".", "'") + "\""
                }
            }
            tagList.add(DetailTagModel(hetS, R.drawable.ic_height))
        }
        if (!TextUtils.isEmpty(obj.zodiacSign)) {
            tagList.add(DetailTagModel(obj.zodiacSign, R.drawable.ic_zodiacsign))
        }
        if (!TextUtils.isEmpty(obj.kids)) {
            tagList.add(DetailTagModel(obj.kids, R.drawable.ic_kids))
        }
        if (!TextUtils.isEmpty(obj.relegion)) {
            tagList.add(DetailTagModel(obj.relegion, R.drawable.ic_religion))
        }
        if (!TextUtils.isEmpty(obj.education)) {
            tagList.add(DetailTagModel(obj.education, R.drawable.ic_education))
        }
        if (!TextUtils.isEmpty(obj.exercise)) {
            tagList.add(DetailTagModel(obj.exercise, R.drawable.ic_exercise))
        }
        if (!TextUtils.isEmpty(obj.drink)) {
            tagList.add(DetailTagModel(obj.drink, R.drawable.ic_drink))
        }
        if (!TextUtils.isEmpty(obj.smoke)) {
            tagList.add(DetailTagModel(obj.smoke, R.drawable.smoke_img))
        }
        if (!TextUtils.isEmpty(obj.pets)) {
            tagList.add(DetailTagModel(obj.pets, R.drawable.dog_ic))
        }
        if (!TextUtils.isEmpty(obj.political)) {
            tagList.add(DetailTagModel(obj.political, R.drawable.ic_political))
        }


        adapter = DetailTagAdapter(mActivity, tagList)
        rv_about.adapter = adapter
        if (obj.distanceFromMatch != null)
            tv_distance.text = obj.distanceFromMatch.toString().split(".")[0] + " miles away"
        else
            tv_distance.text = obj.distance.toString().split(".")[0] + " miles away"

        item_image.setOnClickListener {
            val intent = Intent(mActivity, ViewPagerActivity::class.java)
            intent.putExtra("images", Gson().toJson(imagelist))
            intent.putExtra("position", 0)
            mActivity?.startActivity(intent);
        }
        ivUserImage2.setOnClickListener {
            val intent = Intent(mActivity, ViewPagerActivity::class.java)
            intent.putExtra("images", Gson().toJson(imagelist))
            intent.putExtra("position", 1)
            mActivity?.startActivity(intent);
        }
        ivUserImage3.setOnClickListener {
            val intent = Intent(mActivity, ViewPagerActivity::class.java)
            intent.putExtra("images", Gson().toJson(imagelist))
            intent.putExtra("position", 2)
            mActivity?.startActivity(intent);
        }
        ivUserImage4.setOnClickListener {
            val intent = Intent(mActivity, ViewPagerActivity::class.java)
            intent.putExtra("images", Gson().toJson(imagelist))
            intent.putExtra("position", 3)
            mActivity?.startActivity(intent);
        }
        ivUserImage5.setOnClickListener {
            val intent = Intent(mActivity, ViewPagerActivity::class.java)
            intent.putExtra("images", Gson().toJson(imagelist))
            intent.putExtra("position", 4)
            mActivity?.startActivity(intent);
        }
        ivUserImage6.setOnClickListener {
            val intent = Intent(mActivity, ViewPagerActivity::class.java)
            intent.putExtra("images", Gson().toJson(imagelist))
            intent.putExtra("position", 5)
            mActivity?.startActivity(intent);
        }

        tv_report.setOnClickListener {
        }
    }

    private fun showOtherDialog(dialog1: Dialog) {
        val dialog = Dialog(mActivity, R.style.PauseDialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        Objects.requireNonNull(dialog.window)!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_edit_text)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        val window = dialog.window
        window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val tv_message = dialog.findViewById<TextView>(R.id.tv_message)
        val etReason = dialog.findViewById<EditText>(R.id.et_reason)
        val ivcross = dialog.findViewById<ImageView>(R.id.ivcross)
        val btn_ok = dialog.findViewById<Button>(R.id.btn_ok)
        tv_message.text = this.resources.getString(R.string.reasontoReport)

        ivcross.setOnClickListener {
            hideKeyboardFromView(etReason)
            dialog.dismiss()
        }
        btn_ok.setOnClickListener {
            if (!TextUtils.isEmpty(etReason.text.toString())) {
                showMyLoading()
                hideKeyboardFromView(etReason)
                ApiCall.reportUser(sp.token, ReportRequestModel(intent.extras!!.getInt("userid"), etReason.text.toString()), this)
                dialog.dismiss()
                dialog1.dismiss()
            } else {
                Toast.makeText(mActivity, "Please enter the reason for report", Toast.LENGTH_LONG).show()
            }
        }
    }

    private var mProgressDialog: ProgressDialog? = null


    private fun showMyLoading() {
        if (mProgressDialog == null || !mProgressDialog!!.isShowing)
            mProgressDialog = CommonUtils.showLoadingDialog(this)
    }

    private fun hideMyLoading() {
        if (mProgressDialog != null && mProgressDialog!!.isShowing) {
            mProgressDialog!!.dismiss()
        }
    }

    /**
     * Method to initilize view model and handle response
     */
    private fun subscribeModel() {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        homeViewModel.userDataResponse().observe(this, Observer<Resource<UserListResponseModel>> { resource ->
            if (resource == null) {
                return@Observer
            }
            when (resource.status) {
                Status.LOADING -> {
                }
                Status.SUCCESS -> {
                    hideLoading()
                    if (resource.data!!.data != null) {
                        Log.e("token_________", resource.data.data.profileOfUser.toString())//sp.token)
                        val obj = resource.data.data.profileOfUser
                        var data = resource.data.data.getisLinkedinUser()
                        val imagelist = resource.data.data.imageForUser
                        val instaList = resource.data.data.insta
                        setupCardStackView(resource.data.data)
                        Log.e(TAG, "subscribeModel: " + data)
                        if (TextUtils.isEmpty(data)) {
                            data = ""
                        }
                        setData(obj,
                                imagelist,
                                data,
                                instaList,
                                "" +
                                        resource.data.data.id)
                        clParent2.visibility = VISIBLE
                    }
                }
                Status.ERROR -> {
                    hideLoading()
                    showSnackbar(iv_back, resource.message)
                }
            }
        })

        /*       homeViewModel.reportResponse().observe(this, Observer { resource ->
                   if (resource == null) {
                       return@Observer
                   }
                   when (resource.status) {
                       Status.LOADING -> {
                           Log.e(TAG, "subscribeModel: loading  ")
                       }
                       Status.SUCCESS -> {
                           if (resource.code == 500) {
                               CommonDialogs.dismiss()
                               hideMyLoading()
                               hideKeyboard()
                               showSnackBar(iv_back, "User has already been reported.")
                           } else {
                               CommonDialogs.dismiss()
                               hideMyLoading()
                               hideKeyboard()
                               if (resource.data!!.success) {
                                   if (resource.data.error != null && resource.data.error.code.contains("401")) {
                                       openActivityOnTokenExpire()
                                   } else {
                                       showSnackBar(iv_back, "User successfully reported")
                                       view?.findViewById<View>(R.id.cancel)!!.performClick()
                                       rewind.isEnabled = false
                                   }
                               } else {
                                   Log.e(TAG, "subscribeModel:3 ")
                                   showSnackBar(iv_back, "User has already been reported.")
                               }
                           }
                       }
                       Status.ERROR -> {
                           CommonDialogs.dismiss()
                           hideMyLoading()
                           showSnackBar(iv_back, resource.message)
                       }
                   }
               })
        */       homeViewModel.addSuperLikeResponse().observe(this, Observer { resource ->
            if (resource == null) {
                return@Observer
            }
            when (resource.status) {
                Status.LOADING -> {
                }
                Status.SUCCESS -> {
                    hideLoading()
                    if (resource.data!!.isSuccess) {
                        val gson = Gson()
                        val user = sp.user
                        val obj = gson.fromJson(user, ProfileOfUser::class.java)
                        obj.superLikesCount = resource.data.totalSuperlikes
                        sp.saveUserData(obj, sp.profileCompleted)
                        Toast.makeText(mActivity, "Item Purchased", Toast.LENGTH_LONG).show()

                    } else if (resource.code == 401) {
                        openActivityOnTokenExpire()
                    } else {
                        showSnackbar(iv_back, "Something went wrong")
                    }
                }
                Status.ERROR -> {
                    hideLoading()
                    showSnackbar(iv_back, resource.message)
                }
            }
        })

        homeViewModel.userReactResponse().observe(this, Observer<Resource<ReactResponseModel>> { resource ->
            if (resource == null) {
                return@Observer
            }
            when (resource.status) {
                Status.LOADING -> {
                }
                Status.SUCCESS -> {
                    if (resource.data!!.success!!) {
                        if (resource.data.react.reaction.contains("dislike")) {
                            sp.dislikeApi = true
                        }
                        sp.isDialogOpen = false
                        card_stack_viewMy.swipe()

                        setResult(Activity.RESULT_OK)
                        finish()
                    } else if (resource.data.error != null && resource.data.error.code.contains("401")) {
                        openActivityOnTokenExpire()
                    } else {
                        if (resource.data.message == "Your super likes tokens are finished.") {
                            sp.isDialogOpen = true
                            CommonDialogs.CrushPurChaseDialog(mActivity, this)
                            //"Crush Tokens", "You have run out of crush tokens. " +"Purchase more tokens below.",
                        } else if (resource.data.message.contains("REACHED TO 100", ignoreCase = true)) {
                            if (resource.data.swipesData == null || resource.data.swipesData.updatedAt == null) {
                                sp.isDialogOpen = true

                                CommonDialogs.PremuimPurChaseDialog(mActivity, this, sp)
                                /*"BlackGentry Premium", "You have reached the likes limit. You can send more likes " + "within 12 hours. Want unlimited likes? Subscribe below to BlackGentry Premium."*/
                            }
                        } else {
                            showSnackbar(iv_back, resource.data.message)
                        }
                    }
                }
                Status.ERROR -> {
                    showSnackbar(iv_back, resource.message)
                }
            }
        })
    }

    private var tokenSType: String = ""
    private var productId: String = ""
    private var purchaseType: Int = 0
    var price = 0.0
    private var selectedPosition = -1
    private var bp: BillingProcessor? = null
    override fun onClickToken(tokenType: String, tokensNum: Int, selectedPos: Int) {
        tokenSType = tokenType
        selectedPosition = tokensNum
        if (tokenType.equals("crushToken", ignoreCase = true)) {
            price = CommonDialogs.crushTokenPriceList.get(selectedPos).getPriceValue();
            productId = CommonDialogs.crushTokenArr[selectedPos];
        }
        bp?.purchase(mActivity, productId);
    }

    override fun onBillingInitialized() {
        if (CommonDialogs.vipTokenPriceList.size == 0 || CommonDialogs.timeTokenPriceList.size == 0 || CommonDialogs.crushTokenPriceList.size == 0 || CommonDialogs.PremiumPriceList.size == 0 || CommonDialogs.DeluxePriceList.size == 0) {
            CommonDialogs.onBillingInitialized(bp)
        }
        CommonDialogs.setBilling(bp)
    }

    override fun onPurchaseHistoryRestored() {
    }

    override fun onProductPurchased(productId: String?, details: TransactionDetails?) {
        showLoading()
        if (tokenSType.equals("crushToken", ignoreCase = true)) {
            bp!!.consumePurchase(productId)
            homeViewModel.addSuperLikeRequest(SuperLikeCountModel(selectedPosition, price))
        }
    }

    override fun onBillingError(errorCode: Int, error: Throwable?) {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (!bp!!.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data)
        } else if (requestCode == 2021) {
            Log.d(TAG, "onActivityResult: ")
        }
    }

    var cardSwipeCount = 0


    private fun setupCardStackView(obj: User) {
        list = listOf(obj)
        if (list.isEmpty()) {
/*
            img_vip_star?.isEnabled = false
            chat?.isEnabled = false
            tvNoMatch.visibility = VISIBLE
            btn_settings.visibility = VISIBLE
            imageView.visibility = View.GONE
*/
        } else {
/*            clView.visibility = View.VISIBLE
            img_vip_star?.isEnabled = true
            chat?.isEnabled = true
            tvNoMatch.visibility = View.GONE
            btn_settings.visibility = View.GONE
            cardStackView!!.visibility = VISIBLE*/
        }
        manager.setStackFrom(StackFrom.None)
        manager.setVisibleCount(3)
        manager.setTranslationInterval(8.0f)
        manager.setScaleInterval(0.95f)
        manager.setSwipeThreshold(0.3f)
        manager.setMaxDegree(80.0f)
        //var percent = 75.0
        if (sp.profileCompleted == null || sp.profileCompleted.equals(""))
            openActivityOnTokenExpire()
        /* else
             percent = sp.profileCompleted.toDouble()
         if (percent >= 75)
             manager.setDirections(listOf(Direction.Left, Direction.Right))
         else {
             manager.setDirections(listOf())
         }*/

        manager.setCanScrollHorizontal(false)
        manager.setCanScrollVertical(false)
        manager.setSwipeableMethod(SwipeableMethod.Automatic)
        manager.setOverlayInterpolator(LinearInterpolator())
        card_stack_viewMy?.layoutManager = manager
        cardSwipeCount = list.size
        val i = ScreenUtils.getScreenHeight(this) - (2 * ScreenUtils.getActionBarHeight(this)) - 30
        card_stack_viewMy?.adapter = CardProfileAdapter(this, list, i, this)

        card_stack_viewMy?.adapter!!.notifyDataSetChanged()
        card_stack_viewMy?.itemAnimator.apply {

            if (this is DefaultItemAnimator) {
                supportsChangeAnimations = false
            }
        }

    }

    override fun OnReportClick(id: Int) {
        /*val dialog = Dialog(mActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        Objects.requireNonNull(dialog.window)!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_report)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        val window = dialog.window
        window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val lls = arrayOfNulls<LinearLayout>(6)
        (dialog.findViewById<View>(R.id.tv_photo) as TextView).setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        val mm = (dialog.findViewById<View>(R.id.tv_photo) as TextView).paint.setMaskFilter(null)
        (dialog.findViewById<View>(R.id.tv_content) as TextView).setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        val mm1 = (dialog.findViewById<View>(R.id.tv_content) as TextView).paint.setMaskFilter(null)
        (dialog.findViewById<View>(R.id.tv_stolen) as TextView).setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        val mm2 = (dialog.findViewById<View>(R.id.tv_stolen) as TextView).paint.setMaskFilter(null)
        (dialog.findViewById<View>(R.id.tv_other) as TextView).setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        val mm3 = (dialog.findViewById<View>(R.id.tv_other) as TextView).paint.setMaskFilter(null)
        (dialog.findViewById<View>(R.id.tv_behave) as TextView).setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        val mm4 = (dialog.findViewById<View>(R.id.tv_behave) as TextView).paint.setMaskFilter(null)
        (dialog.findViewById<View>(R.id.tv_photo) as TextView).paint.maskFilter = mm
        (dialog.findViewById<View>(R.id.tv_content) as TextView).paint.maskFilter = mm1
        (dialog.findViewById<View>(R.id.tv_stolen) as TextView).paint.maskFilter = mm2
        (dialog.findViewById<View>(R.id.tv_other) as TextView).paint.maskFilter = mm3
        (dialog.findViewById<View>(R.id.tv_behave) as TextView).paint.maskFilter = mm4
        lls[0] = dialog.findViewById(R.id.ll_photo)
        lls[1] = dialog.findViewById(R.id.ll_content)
        lls[2] = dialog.findViewById(R.id.ll_age)
        lls[3] = dialog.findViewById(R.id.ll_stolen)
        lls[4] = dialog.findViewById(R.id.ll_behave)
        lls[5] = dialog.findViewById(R.id.ll_other)
        val btn_submit = dialog.findViewById<Button>(R.id.btn_submit)
        //lls[0]?.setBackground(mActivity.getDrawable(R.drawable.img_rectangle_outline))
        //lls[0]?.getBackground()?.setColorFilter(mActivity.resources.getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP)
        for (i in lls.indices) {
            lls[i]?.setOnClickListener(OnClickListener { v: View? ->
                pos = i
                btn_submit.setEnabled(true)
                btn_submit.setBackground(mActivity.getDrawable(R.drawable.gradientbtn))
                for (j in lls.indices) {
                    lls[j]?.background = mActivity.getDrawable(R.drawable.rounded_sheet)
                    lls[j]?.background?.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
                }
                lls[i]?.background = mActivity.getDrawable(R.drawable.img_rectangle_outline)
                lls[i]?.background?.setColorFilter(mActivity.resources.getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP)
                if (i == 5) showOtherDialog(dialog)

            })
        }

        val img_cancel = dialog.findViewById<ImageView>(R.id.img_cancel)
        img_cancel.setOnClickListener { v: View? -> dialog.dismiss() }

        btn_submit.setOnClickListener { v: View? ->
            dialog.dismiss()
            var reason = ""
            // baseActivity.showLoading()
            hideKeyboard()
            if (pos < 0) {
                Toast.makeText(context, "Please select the reason for report", Toast.LENGTH_LONG).show()
            } else {
                when (pos) {
                    0 -> {
                        reason = "Inappropriate Photos"
                    }
                    1 -> {
                        reason = "Inappropriate Content"
                    }
                    2 -> {
                        reason = "Inappropriate Behaviour"
                    }
                    3 -> {
                        reason = "Stolen Photo"
                    }
                    4 -> {
                        reason = "Under 18"
                    }

                }

                homeViewModel.reportRequest(ReportRequestModel(id, reason))*/
        var pos = -1
        val dialog = Dialog(mActivity, R.style.PauseDialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        Objects.requireNonNull(dialog.window)?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_report)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        val window = dialog.window
        window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val lls = arrayOfNulls<LinearLayout>(6)
        (dialog.findViewById<View>(R.id.tv_photo) as TextView).setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        val mm = (dialog.findViewById<View>(R.id.tv_photo) as TextView).paint.setMaskFilter(null)
        (dialog.findViewById<View>(R.id.tv_content) as TextView).setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        val mm1 = (dialog.findViewById<View>(R.id.tv_content) as TextView).paint.setMaskFilter(null)
        (dialog.findViewById<View>(R.id.tv_stolen) as TextView).setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        val mm2 = (dialog.findViewById<View>(R.id.tv_stolen) as TextView).paint.setMaskFilter(null)
        (dialog.findViewById<View>(R.id.tv_other) as TextView).setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        val mm3 = (dialog.findViewById<View>(R.id.tv_other) as TextView).paint.setMaskFilter(null)
        (dialog.findViewById<View>(R.id.tv_behave) as TextView).setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        val mm4 = (dialog.findViewById<View>(R.id.tv_behave) as TextView).paint.setMaskFilter(null)
        (dialog.findViewById<View>(R.id.tv_photo) as TextView).paint.maskFilter = mm
        (dialog.findViewById<View>(R.id.tv_content) as TextView).paint.maskFilter = mm1
        (dialog.findViewById<View>(R.id.tv_stolen) as TextView).paint.maskFilter = mm2
        (dialog.findViewById<View>(R.id.tv_other) as TextView).paint.maskFilter = mm3
        (dialog.findViewById<View>(R.id.tv_behave) as TextView).paint.maskFilter = mm4
        lls[0] = dialog.findViewById(R.id.ll_photo)
        lls[1] = dialog.findViewById(R.id.ll_content)
        lls[2] = dialog.findViewById(R.id.ll_behave)
        lls[3] = dialog.findViewById(R.id.ll_stolen)
        lls[4] = dialog.findViewById(R.id.ll_age)
        lls[5] = dialog.findViewById(R.id.ll_other)


        val btn_submit = dialog.findViewById<Button>(R.id.btn_submit)
        //lls[0]?.setBackground(mActivity.getDrawable(R.drawable.img_rectangle_outline))
        //lls[0]?.getBackground()?.setColorFilter(mActivity.resources.getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP)
        for (i in lls.indices) {
            lls[i]?.setOnClickListener { v: View? ->
                pos = i
                if (i == 5) {
                    dialog.dismiss()
                    showOtherDialog(dialog)
                } else {
                    var reason = ""
                    showMyLoading()
                    hideKeyboard()
                    when (pos) {
                        0 -> {
                            reason = "Inappropriate Photos"
                        }
                        1 -> {
                            reason = "Inappropriate Content"
                        }
                        2 -> {
                            reason = "Inappropriate Behaviour"
                        }
                        3 -> {
                            reason = "Stolen Photo"
                        }
                        4 -> {
                            reason = "Under 18"
                        }
                    }
                    dialog.dismiss()
                    homeViewModel.reportRequest(ReportRequestModel(id, reason))
                }
            }
        }



        btn_submit.setOnClickListener {
            dialog.dismiss()
        }
    }


    override fun onCardDragging(direction: Direction?, ratio: Float) {
        Log.e(TAG, "onCardRewound: ")
    }

    override fun onCardSwiped(direction: Direction?) {
        Log.e(TAG, "onCardRewound: ")
    }

    override fun onCardRewound() {
        Log.e(TAG, "onCardRewound: ")
    }

    override fun onCardCanceled() {
        Log.e(TAG, "onCardRewound: ")
    }

    override fun onCardAppeared(view: View?, position: Int) {
        Log.e(TAG, "onCardRewound: ")
    }

    override fun onCardDisappeared(view: View?, position: Int) {
        Log.e(TAG, "onCardRewound: ")
    }

    override fun onError(error: String) {
        hideMyLoading()
        if (error.equals("401", true)) {
            openActivityOnTokenExpire()
        } else {
            showSnackbar(iv_back, error)
        }
    }

    override fun onSuccessReportUser(response: ResponseBody) {
        hideMyLoading()
        hideKeyboard()
        showSnackbar(iv_back, "User successfully reported")
    }

}



