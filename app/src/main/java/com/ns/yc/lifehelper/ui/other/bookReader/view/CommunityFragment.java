package com.ns.yc.lifehelper.ui.other.bookReader.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.SizeUtils;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.BaseFragment;
import com.ns.yc.lifehelper.listener.OnItemClickListener;
import com.ns.yc.lifehelper.ui.other.bookReader.BookReaderActivity;
import com.ns.yc.lifehelper.ui.other.bookReader.activity.BookReaderDiscussionActivity;
import com.ns.yc.lifehelper.ui.other.bookReader.activity.BookReaderGirlActivity;
import com.ns.yc.lifehelper.ui.other.bookReader.activity.BookReaderHelpActivity;
import com.ns.yc.lifehelper.ui.other.bookReader.activity.BookReaderOriginalActivity;
import com.ns.yc.lifehelper.ui.other.bookReader.activity.BookReaderReviewActivity;
import com.ns.yc.lifehelper.ui.other.bookReader.adapter.ReaderFindAdapter;
import com.ns.yc.lifehelper.ui.other.bookReader.bean.support.FindBean;
import com.ns.yc.lifehelper.ui.weight.itemLine.RecycleViewItemLine;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/18
 * 描    述：小说阅读器主页面
 * 修订历史：
 * ================================================
 */
public class CommunityFragment extends BaseFragment {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    private BookReaderActivity activity;
    private List<FindBean> mList = new ArrayList<>();
    private ReaderFindAdapter adapter;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (BookReaderActivity) context;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    @Override
    public int getContentView() {
        return R.layout.fragment_reader_find;
    }

    @Override
    public void initView() {
        initViewData();
        initRecycleView();
    }


    @Override
    public void initListener() {

    }


    @Override
    public void initData() {

    }


    private void initViewData() {
        mList.clear();
        mList.add(new FindBean("综合讨论区", R.drawable.discuss_section));
        mList.add(new FindBean("书评区", R.drawable.comment_section));
        mList.add(new FindBean("书荒互助区", R.drawable.helper_section));
        mList.add(new FindBean("女生区", R.drawable.girl_section));
        mList.add(new FindBean("原创区",R.drawable.yuanchuang));
    }


    private void initRecycleView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        RecycleViewItemLine line = new RecycleViewItemLine(activity, LinearLayout.HORIZONTAL,
                SizeUtils.px2dp(2), activity.getResources().getColor(R.color.grayLine));
        recyclerView.addItemDecoration(line);
        adapter = new ReaderFindAdapter(mList,activity);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (position){
                    case 0:
                        startActivity(BookReaderDiscussionActivity.class);
                        break;
                    case 1:
                        startActivity(BookReaderReviewActivity.class);
                        break;
                    case 2:
                        startActivity(BookReaderHelpActivity.class);
                        break;
                    case 3:
                        startActivity(BookReaderGirlActivity.class);
                        break;
                    case 4:
                        startActivity(BookReaderOriginalActivity.class);
                        break;
                }
            }
        });
    }

}
