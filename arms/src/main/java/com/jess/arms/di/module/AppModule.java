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
package com.jess.arms.di.module;

import android.app.Application;

import com.jess.arms.integration.ActivityLifecycle;
import com.jess.arms.integration.FragmentLifecycle;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.integration.RepositoryManager;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.integration.cache.CacheType;
import com.jess.arms.integration.lifecycle.ActivityLifecycleForRxLifecycle;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import androidx.fragment.app.FragmentManager;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * ================================================
 * 提供一些框架必须的实例的 {@link Module}
 * <p>
 * Created by JessYan on 8/4/2016.
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
@Module
public abstract class AppModule {
    
    
    @Singleton
    @Provides
    static Cache<String, Object> provideExtras(Cache.Factory<String, Object> cacheFactory) {
        return cacheFactory.build(CacheType.EXTRAS);
    }
    
    @Singleton
    @Provides
    static List<FragmentManager.FragmentLifecycleCallbacks> provideFragmentLifecycles() {
        return new ArrayList<>();
    }
    
    @Binds
    abstract IRepositoryManager bindRepositoryManager(RepositoryManager repositoryManager);
    
    @Binds
    @Named("ActivityLifecycle")
    abstract Application.ActivityLifecycleCallbacks bindActivityLifecycle(ActivityLifecycle activityLifecycle);
    
    @Binds
    @Named("ActivityLifecycleForRxLifecycle")
    abstract Application.ActivityLifecycleCallbacks bindActivityLifecycleForRxLifecycle(ActivityLifecycleForRxLifecycle activityLifecycleForRxLifecycle);
    
    @Binds
    abstract FragmentManager.FragmentLifecycleCallbacks bindFragmentLifecycle(FragmentLifecycle fragmentLifecycle);
    
}
