package com.ns.yc.lifehelper.ui.other.expressDelivery.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.api.ConstantALiYunApi;
import com.ns.yc.lifehelper.base.BaseActivity;
import com.ns.yc.lifehelper.ui.other.expressDelivery.bean.ExpressDeliverySearchBean;
import com.ns.yc.lifehelper.ui.other.expressDelivery.adapter.ExpressDeliveryAdapter;
import com.ns.yc.lifehelper.ui.other.expressDelivery.model.ExpressDeliverySearchModel;
import com.ns.yc.lifehelper.ui.weight.itemLine.RecycleViewItemLine;

import butterknife.Bind;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by PC on 2017/8/21.
 * 作者：PC
 */

public class ExpressDeliveryInfoActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.recyclerView)
    EasyRecyclerView recyclerView;
    @Bind(R.id.ll_title_menu)
    FrameLayout llTitleMenu;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    private String number = "";
    private String type = "";
    private ExpressDeliveryInfoActivity activity;
    private ExpressDeliveryAdapter adapter;

    @Override
    public int getContentView() {
        return R.layout.base_easy_recycle_list;
    }

    @Override
    public void initView() {
        initToolBar();
        activity = ExpressDeliveryInfoActivity.this;
        getIntentData();
        initRecycleView();
    }

    private void initToolBar() {
        toolbarTitle.setText("查询快递结果");
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            number = intent.getStringExtra("number");
            type = intent.getStringExtra("type");
        }
    }

    @Override
    public void initListener() {
        llTitleMenu.setOnClickListener(this);
    }

    @Override
    public void initData() {
        getData();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_title_menu:
                finish();
                break;
        }
    }


    private void initRecycleView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        final RecycleViewItemLine line = new RecycleViewItemLine(activity, LinearLayout.HORIZONTAL,
                SizeUtils.dp2px(1), Color.parseColor("#f5f5f7"));
        recyclerView.addItemDecoration(line);
        adapter = new ExpressDeliveryAdapter(activity);
        recyclerView.setAdapter(adapter);
        AddHeader();
        //设置没有数据
        adapter.setNoMore(R.layout.view_recycle_no_more, new RecyclerArrayAdapter.OnNoMoreListener() {
            @Override
            public void onNoMoreShow() {
                if (NetworkUtils.isConnected()) {
                    adapter.resumeMore();
                } else {
                    Toast.makeText(activity, "网络不可用", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNoMoreClick() {
                if (NetworkUtils.isConnected()) {
                    adapter.resumeMore();
                } else {
                    Toast.makeText(activity, "网络不可用", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //设置错误
        adapter.setError(R.layout.view_recycle_error, new RecyclerArrayAdapter.OnErrorListener() {
            @Override
            public void onErrorShow() {
                adapter.resumeMore();
            }

            @Override
            public void onErrorClick() {
                adapter.resumeMore();
            }
        });

        //刷新
        recyclerView.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkUtils.isConnected()) {
                    if (adapter.getAllData().size() > 0) {
                        getData();
                    } else {
                        recyclerView.setRefreshing(false);
                    }
                } else {
                    recyclerView.setRefreshing(false);
                    Toast.makeText(activity, "网络不可用", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void AddHeader() {
        adapter.removeAllHeader();
        adapter.addHeader(new RecyclerArrayAdapter.ItemView() {
            @Override
            public View onCreateView(ViewGroup parent) {
                View view = LayoutInflater.from(activity).inflate(R.layout.head_express_delivery, parent, false);
                return view;
            }

            @Override
            public void onBindView(View headerView) {

            }
        });
    }


    private void getData() {
        recyclerView.showProgress();
        ExpressDeliverySearchModel model = ExpressDeliverySearchModel.getInstance(this);
        model.queryInfo(ConstantALiYunApi.Key, number, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ExpressDeliverySearchBean>() {
                    @Override
                    public void onCompleted() {
                        recyclerView.showRecycler();
                    }

                    @Override
                    public void onError(Throwable e) {
                        recyclerView.showError();
                        recyclerView.setErrorView(R.layout.view_custom_empty_data);
                    }

                    @Override
                    public void onNext(ExpressDeliverySearchBean expressDeliverySearchBean) {
                        if (adapter == null) {
                            adapter = new ExpressDeliveryAdapter(activity);
                        }

                        if (expressDeliverySearchBean != null) {
                            adapter.clear();
                            ExpressDeliverySearchBean.ResultBean result = expressDeliverySearchBean.getResult();
                            adapter.addAll(result.getList());
                            adapter.notifyDataSetChanged();
                        } else {
                            recyclerView.showEmpty();
                            recyclerView.setEmptyView(R.layout.view_custom_empty_data);
                        }
                    }
                });
    }


}
