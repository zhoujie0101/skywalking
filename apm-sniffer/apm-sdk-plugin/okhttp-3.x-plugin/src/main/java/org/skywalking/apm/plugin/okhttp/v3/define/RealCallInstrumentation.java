/*
 * Copyright 2017, OpenSkywalking Organization All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Project repository: https://github.com/OpenSkywalking/skywalking
 */

package org.skywalking.apm.plugin.okhttp.v3.define;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.skywalking.apm.agent.core.plugin.interceptor.ConstructorInterceptPoint;
import org.skywalking.apm.agent.core.plugin.interceptor.InstanceMethodsInterceptPoint;
import org.skywalking.apm.agent.core.plugin.interceptor.enhance.ClassInstanceMethodsEnhancePluginDefine;
import org.skywalking.apm.agent.core.plugin.match.ClassMatch;
import org.skywalking.apm.plugin.okhttp.v3.RealCallInterceptor;

import static net.bytebuddy.matcher.ElementMatchers.named;
import static net.bytebuddy.matcher.ElementMatchers.takesArguments;
import static org.skywalking.apm.agent.core.plugin.match.NameMatch.byName;

/**
 * {@link RealCallInstrumentation} presents that skywalking intercepts {@link okhttp3.RealCall#RealCall(OkHttpClient,
 * Request, boolean)}, {@link okhttp3.RealCall#execute()} by using {@link RealCallInterceptor}.
 *
 * @author peng-yongsheng
 */
public class RealCallInstrumentation extends ClassInstanceMethodsEnhancePluginDefine {

    /**
     * Enhance class.
     */
    private static final String ENHANCE_CLASS = "okhttp3.RealCall";

    /**
     * Intercept class.
     */
    private static final String INTERCEPT_CLASS = "org.skywalking.apm.plugin.okhttp.v3.RealCallInterceptor";

    @Override protected ClassMatch enhanceClass() {
        return byName(ENHANCE_CLASS);
    }

    @Override protected ConstructorInterceptPoint[] getConstructorsInterceptPoints() {
        return new ConstructorInterceptPoint[] {
            new ConstructorInterceptPoint() {
                @Override public ElementMatcher<MethodDescription> getConstructorMatcher() {
                    return takesArguments(OkHttpClient.class, Request.class, boolean.class);
                }

                @Override public String getConstructorInterceptor() {
                    return INTERCEPT_CLASS;
                }
            }
        };
    }

    @Override protected InstanceMethodsInterceptPoint[] getInstanceMethodsInterceptPoints() {
        return new InstanceMethodsInterceptPoint[] {
            new InstanceMethodsInterceptPoint() {
                @Override public ElementMatcher<MethodDescription> getMethodsMatcher() {
                    return named("execute");
                }

                @Override public String getMethodsInterceptor() {
                    return INTERCEPT_CLASS;
                }

                @Override public boolean isOverrideArgs() {
                    return false;
                }
            }
        };
    }
}
