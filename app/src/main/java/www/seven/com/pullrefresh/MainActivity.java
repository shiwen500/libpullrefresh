package www.seven.com.pullrefresh;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import www.seven.com.libpullrefresh.interfaces.OnRefreshListener;
import www.seven.com.libpullrefresh.interfaces.PullToRefresh;
import www.seven.com.libpullrefresh.views.CustomLoadingView;

public class MainActivity extends AppCompatActivity {

    private ListView listView;

    private PullToRefresh mPullToRefresh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.list);
        mPullToRefresh = (PullToRefresh) findViewById(R.id.refreshview);

        List<String> data = new ArrayList<>();
        for (int i = 0; i < 4; ++i) {
            data.add("index" + i);
        }
        ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showProgress();
            }
        });

        mPullToRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onUpToLoad(View view) {
                new Thread() {
                    @Override
                    public void run() {
                        super.run();

                        // 模拟耗时动作，不能再主线程里面
                        try {
                            sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                // 耗时动作执行完成后，需要停止刷新。
                                mPullToRefresh.finishLoad();
                            }
                        });
                    }
                }.start();
            }

            @Override
            public void onDownToRefresh(View view) {

                new Thread() {
                    @Override
                    public void run() {
                        super.run();

                        // 模拟耗时动作，不能再主线程里面
                        try {
                            sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                // 耗时动作执行完成后，需要停止刷新。
                                mPullToRefresh.finishRefresh();
                            }
                        });
                    }
                }.start();
            }
        });
    }

    public void showProgress() {

        ProgressDialog pd = new ProgressDialog(this);

        WindowManager.LayoutParams lp = pd.getWindow()
                .getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        pd.getWindow().setAttributes(lp);
        pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        pd.show();

        View view = View.inflate(this, R.layout.loading_view_demo4, null);

        CustomLoadingView customLoadingView = (CustomLoadingView) view.findViewById(R.id.progress);
        customLoadingView.setProgress(100);
        customLoadingView.loading();

        pd.setContentView(view);


    }
}
