package com.ns.yc.lifehelper.ui.other.douBook.activity;

import android.content.Intent;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.BaseActivity;
import com.ns.yc.lifehelper.ui.other.douBook.bean.DouBookDetailBean;
import com.ns.yc.lifehelper.ui.other.douBook.model.DouBookDetailModel;
import com.ns.yc.lifehelper.ui.other.douMovie.activity.MovieWebViewActivity;
import com.ns.yc.lifehelper.utils.ImageUtils;

import butterknife.Bind;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by PC on 2017/8/22.
 * 作者：PC
 */

public class DouBookDetailActivity extends BaseActivity {

    @Bind(R.id.tv_book_summary)
    TextView tvBookSummary;
    @Bind(R.id.tv_intro)
    TextView tvIntro;
    @Bind(R.id.tv_catalog)
    TextView tvCatalog;
    @Bind(R.id.iv_bg_image)
    ImageView ivBgImage;
    @Bind(R.id.iv_book_photo)
    ImageView ivBookPhoto;
    @Bind(R.id.tv_book_directors)
    TextView tvBookDirectors;
    @Bind(R.id.tv_book_rating_rate)
    TextView tvBookRatingRate;
    @Bind(R.id.tv_book_rating_number)
    TextView tvBookRatingNumber;
    @Bind(R.id.tv_one_casts)
    TextView tvOneCasts;
    @Bind(R.id.tv_book_genres)
    TextView tvBookGenres;
    @Bind(R.id.ll_book_detail)
    LinearLayout llBookDetail;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_casts)
    TextView tvCasts;
    @Bind(R.id.ll_tool_bar)
    LinearLayout llToolBar;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @Bind(R.id.app_bar)
    AppBarLayout appBar;
    private DouBookDetailActivity activity;
    private CollapsingToolbarLayoutState state;
    private String id;
    private String alt;

    private enum CollapsingToolbarLayoutState {
        EXPANDED,
        COLLAPSED,
        INTER
    }

    @Override
    public int getContentView() {
        return R.layout.activity_book_detail;
    }

    @Override
    public void initView() {
        activity = DouBookDetailActivity.this;
        initToolBar();
        initIntent();
    }

    private void initIntent() {
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String author = intent.getStringExtra("author");
        String image = intent.getStringExtra("image");
        String average = intent.getStringExtra("average");
        String numRaters = intent.getStringExtra("numRaters");
        String pubDate = intent.getStringExtra("pubdate");
        String publisher = intent.getStringExtra("publisher");
        id = intent.getStringExtra("id");
        alt = intent.getStringExtra("alt");

        tvName.setText(title);
        tvCasts.setText("作者："+ author);
        ImageUtils.loadImgByPicasso(activity,image,ivBookPhoto);
        tvBookDirectors.setText(author);
        tvBookRatingNumber.setText(numRaters+"人评分");
        tvBookRatingRate.setText("评分："+average);
        tvOneCasts.setText(pubDate);
        tvBookGenres.setText(publisher);
    }

    @Override
    public void initListener() {
        appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) {
                    if (state != CollapsingToolbarLayoutState.EXPANDED) {
                        state = CollapsingToolbarLayoutState.EXPANDED;  //修改状态标记为展开
                    }
                    toolbar.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
                } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    if (state != CollapsingToolbarLayoutState.COLLAPSED) {
                        state = CollapsingToolbarLayoutState.COLLAPSED; //修改状态标记为折叠
                    }
                    toolbar.setBackgroundColor(getResources().getColor(R.color.colorTheme));
                } else {
                    if (state != CollapsingToolbarLayoutState.INTER) {
                        state = CollapsingToolbarLayoutState.INTER;     //修改状态标记为中间
                    }
                    toolbar.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
                }
            }
        });
    }

    @Override
    public void initData() {
        getBookDetailData(id);
    }

    private void initToolBar() {
        toolbar.inflateMenu(R.menu.movie_menu_detail);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.movie_about:
                        Intent intent = new Intent(activity,MovieWebViewActivity.class);
                        intent.putExtra("alt",alt);
                        intent.putExtra("name","");
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                } else {
                    finish();
                }
            }
        });
    }

    private void getBookDetailData(String id) {
        DouBookDetailModel model = DouBookDetailModel.getInstance(this);
        model.getHotMovie(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DouBookDetailBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(DouBookDetailBean bookDetailBean) {
                        if(bookDetailBean!=null){
                            tvBookSummary.setText(bookDetailBean.getSummary());
                            tvIntro.setText(bookDetailBean.getAuthor_intro());
                            tvCatalog.setText(bookDetailBean.getCatalog());
                        }
                    }
                });
    }

}
