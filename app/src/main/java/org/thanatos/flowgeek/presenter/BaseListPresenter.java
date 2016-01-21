package org.thanatos.flowgeek.presenter;

import android.support.v4.app.Fragment;
import android.util.Log;

import org.thanatos.flowgeek.AppManager;
import org.thanatos.flowgeek.model.CacheManager;

import java.util.ArrayList;

import nucleus.presenter.RxPresenter;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by thanatos on 15/12/22.
 */
@SuppressWarnings("all")
public abstract class BaseListPresenter<View extends Fragment> extends RxPresenter<View> {

    /**
     * 请求网络数据
     *
     * @param mode
     * @param pageNum
     */
    public abstract void requestData(int mode, int pageNum);

    /**
     * 缓存数据
     *
     * @param data
     */
    public <T> void cacheData(ArrayList<T> data, String CACHE_KEY) {
        Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(Subscriber<? super Void> subscriber) {
                CacheManager.saveObject(AppManager.context, data, CACHE_KEY);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (d) -> {
                            Log.d("thanatos", "cache file " + CACHE_KEY + " successful");
                        },
                        (err) -> {
                            err.printStackTrace();
                        });
    }

    /**
     * 读取缓存
     *
     * @param cache_key
     * @param <T>
     * @return
     */
    public <T> Observable<T> getCacheFile(String CACHE_KEY) {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                if (!CacheManager.isExist4DataCache(AppManager.context, CACHE_KEY)) {
                    subscriber.onNext(null);
                    return;
                }
                subscriber.onNext(CacheManager.<T>readObject(AppManager.context, CACHE_KEY));
                subscriber.onCompleted();
            }
        });
    }


}
