package com.example.mohamed.git_task;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.kosalgeek.android.caching.FileCacher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
//TextView tv;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private ArrayList<RepoModel>myDataset;
    private SwipeRefreshLayout swipeRefreshLayout;
    private EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;
    private FileCacher<ArrayList<RepoModel>> stringCacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //tv=findViewById(R.id.tv);
        myDataset=new ArrayList<>();
        mRecyclerView =  findViewById(R.id.my_recycler_view);
        swipeRefreshLayout=findViewById(R.id.swiperefresh);
        //mRecyclerView.setHasFixedSize(true);
        stringCacher= new FileCacher<>(MainActivity.this, "repCache.txt");

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //deleteCache(MainActivity.this);
                if(checkInternetConnection()){
                    mRecyclerView.clearOnScrollListeners();

                    myDataset.clear();
                    mAdapter.notifyDataSetChanged();
                    if(stringCacher.hasCache()){
                        try {
                            stringCacher.clearCache();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    mRecyclerView.addOnScrollListener(endlessRecyclerViewScrollListener);
                    scrollData(1);

                }
                else{
                    if(stringCacher.hasCache()){
                        System.out.println("No internet and has cache");
                        getCache();
                    }else{
                        System.out.println("No internet No cache");
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }

            }
        });

        mLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        endlessRecyclerViewScrollListener=new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                scrollData(page);
                System.out.println("total items count: "+totalItemsCount);
                System.out.println("PageNum: "+page);
            }
        };
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


       mRecyclerView.addOnScrollListener(endlessRecyclerViewScrollListener);

        mAdapter = new MyAdapter(this,myDataset);
        mRecyclerView.setAdapter(mAdapter);

        if(!checkInternetConnection()){
            //getCache();
            if(stringCacher.hasCache()){
                System.out.println("No internet and has cache");
                getCache();
            }else{

                System.out.println("No internet No cache");
            }

        }else{
            try {
                stringCacher.clearCache();
                scrollData(1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }


    void scrollData(int page){

        int cacheSize = 10 * 1024 * 1024; // 10 MB
        Cache cache = new Cache(getCacheDir(), cacheSize);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cache(cache)
                .build();
        //String baseURL="https://jsonplaceholder.typicode.com/";
        //String baseURL="https://api.github.com/users/MohamedElshatlawy/";
        String baseURL="https://api.github.com/users/square/";
        Retrofit retrofit=new Retrofit.Builder().client(okHttpClient).
                addConverterFactory(GsonConverterFactory.create()).
                baseUrl(baseURL).build();

        GitService gitService = retrofit.create(GitService.class);
        gitService.getPosts(page,10).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    String res=response.body().string();

                    //tv.setText(res);
                    JSONArray jsonArray=new JSONArray(res);
                    if(jsonArray.length()>0){
                        if(swipeRefreshLayout.isRefreshing()){
                            swipeRefreshLayout.setRefreshing(false);
                        }
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject o=jsonArray.getJSONObject(i);
                            String repName=o.getString("name");
                            String repDesc=o.getString("description");
                            boolean forkFlag=false;

                            if(o.has("fork")){
                                forkFlag=o.getBoolean("fork");
                            }
                            String rep_url=o.getString("html_url");

                            JSONObject ownerOb=o.getJSONObject("owner");
                            String repOwner=ownerOb.getString("login");
                            String owner_url=ownerOb.getString("html_url");


                            RepoModel repoModel=new RepoModel(repName,repOwner,repDesc,forkFlag,rep_url,owner_url);
                            myDataset.add(repoModel);
                            /*String cacheItem=repName+","+repOwner+","+repDesc+","+forkFlag+","+
                                    rep_url+","+owner_url+"\n";
                            */


                            //System.out.println("\ntitle:"+o.getString("title"));
                        }
                    }


                    System.out.println("Array size:"+jsonArray.length());
                    setCache(myDataset);


                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    void loadData(View v){
        scrollData(1);

    }

    void setCache(ArrayList<RepoModel>  cacheItem){

        try {
            System.out.println(cacheItem);
            stringCacher.clearCache();
            stringCacher.writeCache(cacheItem);
            /*if(stringCacher.hasCache()){
                Toast.makeText(this,"has cache inside setcache",Toast.LENGTH_LONG).show();
                stringCacher.clearCache();
                stringCacher= new FileCacher<>(MainActivity.this, "repCache.txt");
            }*/
            //stringCacher.writeCache(cacheItem);

            if(stringCacher.hasCache()){
                System.out.println("Inside SetCache DoneCache");

            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    void getCache(){
        //Toast.makeText(this,"inside get cache",Toast.LENGTH_LONG).show();
        if(swipeRefreshLayout.isRefreshing())
            swipeRefreshLayout.setRefreshing(false);
            try {
          //      Toast.makeText(this,"has cache",Toast.LENGTH_LONG).show();
                myDataset=stringCacher.readCache();
                System.out.println("size:"+myDataset.size());
                mAdapter=new MyAdapter(this,myDataset);
                mRecyclerView.setAdapter(mAdapter);
                //mAdapter.notifyDataSetChanged();
            } catch (IOException e) {
                e.printStackTrace();

        }

    }
    boolean checkInternetConnection(){
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

}
