/*
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jess.arms.integration;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.jess.arms.base.delegate.IFragment;
import com.jess.arms.utils.ArmsUtils;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

/**
 * ================================================
 * {@link FragmentManager.FragmentLifecycleCallbacks} 默认实现类
 * <p>
 * Created by JessYan on 04/09/2017 16:04
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
@Singleton
public class FragmentLifecycle extends FragmentManager.FragmentLifecycleCallbacks {
    
    @Inject
    public FragmentLifecycle() {
    }
    
    @Override
    public void onFragmentAttached(@NonNull FragmentManager fm, @NonNull Fragment f, @NonNull Context context) {
    }
    
    @Override
    public void onFragmentCreated(@NonNull FragmentManager fm, @NonNull Fragment f, Bundle savedInstanceState) {
        if (f instanceof IFragment) {
            IFragment iFragment = (IFragment) f;
            //如果要使用eventbus请将此方法返回true
            if (iFragment.useEventBus()) {
                //注册到事件主线
                EventBusManager.getInstance().register(f);
            }
            iFragment.setupFragmentComponent(ArmsUtils.obtainAppComponentFromContext(f.getActivity()));
        }
    }
    
    @Override
    public void onFragmentViewCreated(@NonNull FragmentManager fm, @NonNull Fragment f, @NonNull View v, Bundle savedInstanceState) {
    }
    
    @Override
    public void onFragmentActivityCreated(@NonNull FragmentManager fm, @NonNull Fragment f, Bundle savedInstanceState) {
        if (f instanceof IFragment && f.isAdded()) {
            IFragment iFragment = (IFragment) f;
            iFragment.initData(savedInstanceState);
        }
    }
    
    @Override
    public void onFragmentStarted(@NonNull FragmentManager fm, @NonNull Fragment f) {
    }
    
    @Override
    public void onFragmentResumed(@NonNull FragmentManager fm, @NonNull Fragment f) {
    }
    
    @Override
    public void onFragmentPaused(@NonNull FragmentManager fm, @NonNull Fragment f) {
    }
    
    @Override
    public void onFragmentStopped(@NonNull FragmentManager fm, @NonNull Fragment f) {
    }
    
    @Override
    public void onFragmentSaveInstanceState(@NonNull FragmentManager fm, @NonNull Fragment f, @NonNull Bundle outState) {
    }
    
    @Override
    public void onFragmentViewDestroyed(@NonNull FragmentManager fm, @NonNull Fragment f) {
    }
    
    @Override
    public void onFragmentDestroyed(@NonNull FragmentManager fm, @NonNull Fragment f) {
        if (f instanceof IFragment) {
            IFragment iFragment = (IFragment) f;
            //如果要使用eventbus请将此方法返回true
            if (iFragment.useEventBus()) {
                //注册到事件主线
                EventBusManager.getInstance().unregister(f);
            }
        }
    }
    
    @Override
    public void onFragmentDetached(@NonNull FragmentManager fm, @NonNull Fragment f) {
    }
}
