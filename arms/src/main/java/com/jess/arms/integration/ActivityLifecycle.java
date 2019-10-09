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

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.jess.arms.base.delegate.IActivity;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.integration.cache.IntelligentCache;
import com.jess.arms.utils.ArmsUtils;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import dagger.Lazy;


/**
 * ================================================
 * {@link Application.ActivityLifecycleCallbacks} 默认实现类
 *
 * @see <a href="http://www.jianshu.com/p/75a5c24174b2">ActivityLifecycleCallbacks 分析文章</a>
 * Created by JessYan on 21/02/2017 14:23
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
@Singleton
public class ActivityLifecycle implements Application.ActivityLifecycleCallbacks {
    
    @Inject
    Application mApplication;
    @Inject
    Cache<String, Object> mExtras;
    @Inject
    Lazy<FragmentManager.FragmentLifecycleCallbacks> mFragmentLifecycle;
    @Inject
    Lazy<List<FragmentManager.FragmentLifecycleCallbacks>> mFragmentLifecycles;
    
    @Inject
    public ActivityLifecycle() {
    }
    
    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        //配置ActivityDelegate
        if (activity instanceof IActivity) {
            IActivity iActivity = (IActivity) activity;
            //如果要使用 EventBus 请将此方法返回 true
            if (iActivity.useEventBus()) {
                //注册到事件主线
                EventBusManager.getInstance().register(activity);
            }
            
            //这里提供 AppComponent 对象给 BaseActivity 的子类, 用于 Dagger2 的依赖注入
            iActivity.setupActivityComponent(ArmsUtils.obtainAppComponentFromContext(activity));
        }
        
        registerFragmentCallbacks(activity);
    }
    
    @Override
    public void onActivityStarted(Activity activity) {
    }
    
    @Override
    public void onActivityResumed(Activity activity) {
    
    }
    
    @Override
    public void onActivityPaused(Activity activity) {
    }
    
    @Override
    public void onActivityStopped(Activity activity) {
    }
    
    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }
    
    @Override
    public void onActivityDestroyed(Activity activity) {
        if (activity instanceof IActivity) {
            IActivity iActivity = (IActivity) activity;
            //如果要使用 EventBus 请将此方法返回 true
            if (iActivity.useEventBus()) {
                EventBusManager.getInstance().unregister(activity);
            }
        }
    }
    
    /**
     * 给每个 Activity 的所有 Fragment 设置监听其生命周期, Activity 可以通过 {@link IActivity#useFragment()}
     * 设置是否使用监听,如果这个 Activity 返回 false 的话,这个 Activity 下面的所有 Fragment 将不能使用 {@link com.jess.arms.base.delegate.IFragment}
     */
    private void registerFragmentCallbacks(Activity activity) {
        boolean useFragment = !(activity instanceof IActivity) || ((IActivity) activity).useFragment();
        if (activity instanceof FragmentActivity && useFragment) {
            
            //mFragmentLifecycle 为 Fragment 生命周期实现类, 用于框架内部对每个 Fragment 的必要操作, 如给每个 Fragment 配置 FragmentDelegate
            //注册框架内部已实现的 Fragment 生命周期逻辑
            ((FragmentActivity) activity).getSupportFragmentManager().registerFragmentLifecycleCallbacks(mFragmentLifecycle.get(), true);
            
            if (mExtras.containsKey(IntelligentCache.getKeyOfKeep(ConfigModule.class.getName()))) {
                Object o = mExtras.get(IntelligentCache.getKeyOfKeep(ConfigModule.class.getName()));
                if (o instanceof List) {
                    List<ConfigModule> modules = (List<ConfigModule>) o;
                    for (ConfigModule module : modules) {
                        module.injectFragmentLifecycle(mApplication, mFragmentLifecycles.get());
                    }
                    mExtras.remove(IntelligentCache.getKeyOfKeep(ConfigModule.class.getName()));
                }
            }
            
            //注册框架外部, 开发者扩展的 Fragment 生命周期逻辑
            for (FragmentManager.FragmentLifecycleCallbacks fragmentLifecycle : mFragmentLifecycles.get()) {
                ((FragmentActivity) activity).getSupportFragmentManager().registerFragmentLifecycleCallbacks(fragmentLifecycle, true);
            }
        }
    }
    
    
}
