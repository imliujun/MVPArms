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
package com.jess.arms.di.component;

import android.app.Application;
import android.content.Context;

import com.jess.arms.base.delegate.AppDelegate;
import com.jess.arms.di.module.AppModule;
import com.jess.arms.di.module.ClientModule;
import com.jess.arms.di.module.GlobalConfigModule;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.utils.ArmsUtils;

import java.util.concurrent.ExecutorService;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import okhttp3.OkHttpClient;

/**
 * ================================================
 * 可通过 {@link ArmsUtils#obtainAppComponentFromContext(Context)} 拿到此接口的实现类
 * 拥有此接口的实现类即可调用对应的方法拿到 Dagger 提供的对应实例
 *
 * @see <a href="https://github.com/JessYanCoding/MVPArms/wiki#2.2">AppComponent wiki 官方文档</a>
 * Created by JessYan on 8/4/2016
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
@Singleton
@Component(modules = {AppModule.class, ClientModule.class, GlobalConfigModule.class})
public interface AppComponent {
    Application application();
    
    /**
     * 用于管理网络请求层, 以及数据缓存层
     *
     * @return {@link IRepositoryManager}
     */
    IRepositoryManager repositoryManager();
    
    
    /**
     * 网络请求框架
     *
     * @return {@link OkHttpClient}
     */
    OkHttpClient okHttpClient();
    
    
    /**
     * 用来存取一些整个 App 公用的数据, 切勿大量存放大容量数据, 这里的存放的数据和 {@link Application} 的生命周期一致
     *
     * @return {@link Cache}
     */
    Cache<String, Object> extras();
    
    /**
     * 用于创建框架所需缓存对象的工厂
     *
     * @return {@link Cache.Factory}
     */
    Cache.Factory<String, Object> cacheFactory();
    
    /**
     * 返回一个全局公用的线程池,适用于大多数异步需求。
     * 避免多个线程池创建带来的资源消耗。
     *
     * @return {@link ExecutorService}
     */
    ExecutorService executorService();
    
    void inject(AppDelegate delegate);
    
    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);
        
        Builder globalConfigModule(GlobalConfigModule globalConfigModule);
        
        AppComponent build();
    }
}
