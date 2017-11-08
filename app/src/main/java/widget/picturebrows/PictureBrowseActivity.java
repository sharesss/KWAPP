package widget.picturebrows;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ts.fmxt.R;

import java.util.ArrayList;

import utils.StringUtils;


/**
 * @author kp
 * @Description:描述:图片查看器
 */
public class PictureBrowseActivity extends FragmentActivity {
    private static final String STATE_POSITION = "STATE_POSITION";

    private HackyViewPager mPager;
    private RelativeLayout rl_title;
    private int pagerPosition;
    private TextView indicator,indicators;
    private ImageView iv_del;
    public int delTag = 0;
    ArrayList<String> arrayList = null;
    ArrayList<String> nameList = null;
    ImagePagerAdapter mAdapter;
    private String type;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pagerPosition = getIntent().getIntExtra("position", 0);
        String urls = getIntent().getStringExtra("urls");
        String name = getIntent().getStringExtra("name");
        type = name;
        setContentView(R.layout.activity_picture_brows_view);
        mPager = (HackyViewPager) findViewById(R.id.pager);
        indicator = (TextView) findViewById(R.id.indicator);
        indicators= (TextView) findViewById(R.id.indicators);
        iv_del = (ImageView) findViewById(R.id.iv_del);
        rl_title = (RelativeLayout) findViewById(R.id.rl_title);
        findViewById(R.id.btn_finish).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if(type.equals("type")){
            iv_del.setVisibility(View.GONE);
            rl_title.setVisibility(View.GONE);
            indicator.setVisibility(View.GONE);
        }
        findViewById(R.id.iv_del).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    if (nameList.size() == delTag)
                        delTag = 0;
//                    Bundle bundle = new Bundle();
//
//                    bundle.putString("name", nameList.get(delTag));
//                    ReceiverUtils.sendReceiver(ReceiverUtils.CONSUMER_DEL, bundle);
                    arrayList.remove(delTag);
                    nameList.remove(delTag);
                    if (arrayList.size() == 0) {
                        finish();
                    }
                    ImagePagerAdapter mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), arrayList, nameList);
                    mPager.setAdapter(mAdapter);
                    indicator.setText(arrayList.size() == 1 ? "" : String.format("%s of %s", 1, mPager.getAdapter().getCount()));
                } catch (Exception e) {
                    finish();
                }

            }
        });

        if (!StringUtils.isEmpty(urls)) {
            arrayList = new ArrayList<String>();
            nameList = new ArrayList<String>();
            urls = urls.substring(0, urls.length() - 1);
            String[] urlArrayList = urls.split(",");

            if(type.equals("type")){
                name = "9,8,7,6,5,4,3,2,1,0";
            }
            name = name.substring(0, name.length() - 1);
            String[] nameArrayList = name.split(",");

            for (int i = 0; i < urlArrayList.length; i++) {
                arrayList.add(urlArrayList[i]);
                nameList.add(StringUtils.isEmpty(name) ? "" : nameArrayList[i]);
            }
            if(type.equals("type")){
                indicators.setVisibility(View.VISIBLE);
            }else{
                indicator.setVisibility(arrayList.size() > 1 ? View.VISIBLE : View.GONE);
            }

        }

        mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), arrayList, nameList);
        mPager.setAdapter(mAdapter);
        if(type.equals("type")){
            indicators.setText(String.format("%s/%s", pagerPosition + 1, mPager.getAdapter().getCount()));
        }else{
            indicator.setText(String.format("%s of %s", pagerPosition + 1, mPager.getAdapter().getCount()));
        }


        // 更新下标
        mPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageSelected(int arg0) {
                delTag = arg0;
                String showNum = String.format("%s of %s", arg0 + 1, mPager.getAdapter().getCount());
                String showNums= String.format("%s/%s", arg0 + 1, mPager.getAdapter().getCount());
                if(type.equals("type")){
                    indicators.setText(showNums);
                }else{
                    indicator.setText(showNum);
                }

            }

        });
        if (savedInstanceState != null) {
            pagerPosition = savedInstanceState.getInt(STATE_POSITION);
        }

        mPager.setCurrentItem(pagerPosition);
    }


    /**
     * 返回 Click Listener
     *
     * @param v
     */
    public void clickBack(View v) {
        switch (v.getId()) {
            case R.id.btn_finish:
                finish();
                break;
            default:
                break;
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_POSITION, mPager.getCurrentItem());
    }

    private class ImagePagerAdapter extends FragmentStatePagerAdapter {

        public ArrayList<String> fileList;
        public ArrayList<String> nameList;

        public ImagePagerAdapter(FragmentManager fm, ArrayList<String> fileList, ArrayList<String> nameList) {
            super(fm);
            this.fileList = fileList;
            this.nameList = nameList;
        }

        @Override
        public int getCount() {
            return fileList == null ? 0 : fileList.size();
        }

        @Override
        public Fragment getItem(int position) {
            String url = fileList.get(position);
            return ImageDetailFragment.newInstance(url, nameList.get(position));
        }

    }
}
