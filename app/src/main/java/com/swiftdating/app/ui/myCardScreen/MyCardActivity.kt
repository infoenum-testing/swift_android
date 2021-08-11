package com.swiftdating.app.ui.myCardScreen

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ArrayAdapter
import android.widget.GridView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.swiftdating.app.R
import com.swiftdating.app.common.CommonUtils
import com.swiftdating.app.common.CustomPagerAdapter
import com.swiftdating.app.common.ScreenUtils
import com.swiftdating.app.data.network.CallServer
import com.swiftdating.app.data.network.Status
import com.swiftdating.app.model.DetailTagModel
import com.swiftdating.app.model.ImageModel
import com.swiftdating.app.model.responsemodel.InstagramImageModel
import com.swiftdating.app.model.responsemodel.ProfileOfUser
import com.swiftdating.app.ui.base.BaseActivity
import com.swiftdating.app.ui.editProfileScreen.viewmodel.EditProfileViewModel
import com.swiftdating.app.ui.homeScreen.adapter.DetailTagAdapter
import com.swiftdating.app.ui.viewpagerScreen.ViewPagerActivity
import com.bumptech.glide.Glide
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_my_card.*
import kotlinx.android.synthetic.main.detailview.*
import java.util.*
import kotlin.collections.ArrayList

class MyCardActivity : BaseActivity() {
    var adapter: DetailTagAdapter? = null
    var customPagerAdapter: CustomPagerAdapter? = null
    var tagList: MutableList<DetailTagModel> = ArrayList()
    lateinit var editProfileViewModel: EditProfileViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_my_card)
        setContentView(R.layout.profile_layout)
        clView.visibility = GONE
        subscribeModel()
        setData()
        showLoading()
        editProfileViewModel.myProfileRequest(sp.token)
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


    private fun subscribeModel() {
        editProfileViewModel = ViewModelProviders.of(this).get(EditProfileViewModel::class.java)
        editProfileViewModel.myProfileResponse().observe(this, Observer { resource ->
            if (resource == null) {
                return@Observer
            }
            when (resource.status) {
                Status.LOADING -> {
                }
                Status.SUCCESS -> {
                    hideLoading()
                    if (resource.data!!.success) {
                        setDatum(resource.data.insta)
                    } else {
                        showSnackbar(tv_preview, "Something Went Wrong")
                    }
                }
                Status.ERROR -> {
                    hideLoading()
                }
            }
        })
    }

    private fun setDatum(instaList: ArrayList<InstagramImageModel.Datum>?) {
        if (instaList != null && instaList.isNotEmpty()) {
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

    }

    /**
     *  Method to set Data
     */
    private fun setData() {

        iv_back.setOnClickListener { view -> onBackPressed() }

        left_overlay.visibility = GONE
        right_overlay.visibility = GONE
        top_overlay.visibility = GONE
        bottom_overlay.visibility = GONE
        tv_report.visibility = GONE

        val gson = Gson()
        val json = sp.user
        Log.e("token_________", sp.token)
        val obj = gson.fromJson(json, ProfileOfUser::class.java)
        val jsonImage = sp.userImage
        val type = object : TypeToken<List<ImageModel>>() {

        }.type
        val imagelist = gson.fromJson<List<ImageModel>>(jsonImage, type)

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

        view1.visibility = GONE


        if (!TextUtils.isEmpty(obj!!.name)) {
            name.text = obj.name
        }
        if (!TextUtils.isEmpty(obj.dob)) {
            tvage.text = ", " + CommonUtils.getAge(obj.dob)
        }

        if (sp.linkedIn) {
            llLinkedin.visibility = View.VISIBLE
        } else {
            llLinkedin.visibility = GONE
        }

        ll_superlike.visibility = GONE

        tv_distance.visibility = GONE
        currentLoc.visibility = GONE
        if (!TextUtils.isEmpty(obj.city)) {
            tv_lives_in.text = obj.city
        } else
            cv_live.visibility = GONE

        if (!TextUtils.isEmpty(obj.question1)) {
            tvQuestion1.text = obj.question1
            tvAnswer1.text = obj.answer1
        } else
            cvQuestion1.visibility = GONE

        if (!TextUtils.isEmpty(obj.question2)) {
            tvQuestion2.text = obj.question2
            tvAnswer2.text = obj.answer2
        } else
            cvQuestion2.visibility = GONE

        if (!TextUtils.isEmpty(obj.question3)) {
            tvQuestion3.text = obj.question3
            tvAnswer3.text = obj.answer3
        } else
            cvQuestion3.visibility = GONE


        if (!TextUtils.isEmpty(obj.occupation))
            tvOccupation.text = obj.occupation
        else
            cv_occupation.visibility = GONE

        if (!TextUtils.isEmpty(obj.school))
            tvSchool.text = obj.school
        else
            cvSchool.visibility = GONE

        if (!TextUtils.isEmpty(obj.aboutme)) {
            cv_about_me.visibility = View.VISIBLE
            tv_about_me.text = obj.aboutme
        } else
            cv_about_me.visibility = GONE

        if (!TextUtils.isEmpty(obj.ambitions)) {
            cv_interest.visibility = View.VISIBLE
            tv_interest.text = obj.ambitions
        } else
            cv_interest.visibility = GONE




        Glide.with(item_image).load(CallServer.BaseImage + imagelist[0].imageUrl).centerCrop()
                .into(item_image);

        if (imagelist.size > 1) {
            Glide.with(ivUserImage2).load(CallServer.BaseImage + imagelist[1].imageUrl).centerCrop()
                    .into(ivUserImage2);

            if (imagelist.size > 2) {
                Glide.with(ivUserImage3).load(CallServer.BaseImage + imagelist[2].imageUrl).centerCrop()
                        .into(ivUserImage3);
                if (imagelist.size > 3) {
                    Glide.with(ivUserImage4).load(CallServer.BaseImage + imagelist[3].imageUrl).centerCrop()
                            .into(ivUserImage4)
                    if (imagelist.size > 4) {
                        Glide.with(ivUserImage5).load(CallServer.BaseImage + imagelist[4].imageUrl).centerCrop()
                                .into(ivUserImage5);
                        if (imagelist.size > 5) {
                            Glide.with(ivUserImage6).load(CallServer.BaseImage + imagelist[5].imageUrl).centerCrop()
                                    .into(ivUserImage6);
                        } else {
                            ivUserImage6.visibility = GONE
                        }
                    } else {
                        ivUserImage5.visibility = GONE
                        ivUserImage6.visibility = GONE
                    }
                } else {
                    ivUserImage4.visibility = GONE
                    ivUserImage5.visibility = GONE
                    ivUserImage6.visibility = GONE
                }
            } else {
                ivUserImage3.visibility = GONE
                ivUserImage4.visibility = GONE
                ivUserImage5.visibility = GONE
                ivUserImage6.visibility = GONE
            }
        } else {
            ivUserImage2.visibility = GONE
            ivUserImage3.visibility = GONE
            ivUserImage4.visibility = GONE
            ivUserImage5.visibility = GONE
            ivUserImage6.visibility = GONE
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
        Log.e("TAG", "setData: " + obj.political)
        if (!TextUtils.isEmpty(obj.political)) {
            tagList.add(DetailTagModel(obj.political, R.drawable.ic_political))
        }
        adapter = DetailTagAdapter(mActivity, tagList)
        rv_about.adapter = adapter
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

    }


}


