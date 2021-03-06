package com.stdio.astropanda.slideShow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.stdio.astropanda.R;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class SlideActivity6 extends AppCompatActivity {

    private final CompositeDisposable disposables = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide6);
        doSomeWork();
    }

    private void doSomeWork() {
        disposables.add(getObservable()
                // Run on a background thread
                .subscribeOn(Schedulers.io())
                // Be notified on the main thread
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getObserver()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.clear(); // clearing it : do not emit after destroy
    }

    private Observable<? extends Long> getObservable() {
        return Observable.interval(2, 2, TimeUnit.SECONDS);
    }

    private DisposableObserver<Long> getObserver() {
        return new DisposableObserver<Long>() {

            @Override
            public void onNext(Long value) {
                startActivity(new Intent(SlideActivity6.this, SlideActivity1.class));
                Animatoo.animateFade(SlideActivity6.this);
                finish();
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        };
    }
}
