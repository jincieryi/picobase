/*
 * Copyright 2020-2099 sa-token.cc
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
package com.picobase.basic;

import com.picobase.PbManager;
import com.picobase.context.PbHolder;
import com.picobase.error.PbErrorCode;
import com.picobase.exception.NotBasicAuthException;
import com.picobase.secure.SaBase64Util;
import com.picobase.util.PbInnerUtil;

/**
 * Sa-Token Http Basic 认证模块
 *
 * @author click33
 * @since 1.26.0
 */
public class SaBasicTemplate {

    /**
     * 默认的 Realm 领域名称
     */
    public static final String DEFAULT_REALM = "Sa-Token";

    /**
     * 在校验失败时，设置响应头，并抛出异常
     *
     * @param realm 领域
     */
    public void throwNotBasicAuthException(String realm) {
        PbHolder.getResponse().setStatus(401).setHeader("WWW-Authenticate", "Basic Realm=" + realm);
        throw new NotBasicAuthException().setCode(PbErrorCode.CODE_10311);
    }

    /**
     * 获取浏览器提交的 Basic 参数 （裁剪掉前缀并解码）
     *
     * @return 值
     */
    public String getAuthorizationValue() {

        // 获取前端提交的请求头 Authorization 参数
        String authorization = PbHolder.getRequest().getHeader("Authorization");

        // 如果不是以 Basic 作为前缀，则视为无效
        if (authorization == null || !authorization.startsWith("Basic ")) {
            return null;
        }

        // 裁剪前缀并解码
        return SaBase64Util.decode(authorization.substring(6));
    }

    /**
     * 对当前会话进行 Basic 校验（使用全局配置的账号密码），校验不通过则抛出异常
     */
    public void check() {
        check(DEFAULT_REALM, PbManager.getConfig().getBasic());
    }

    /**
     * 对当前会话进行 Basic 校验（手动设置账号密码），校验不通过则抛出异常
     *
     * @param account 账号（格式为 user:password）
     */
    public void check(String account) {
        check(DEFAULT_REALM, account);
    }

    /**
     * 对当前会话进行 Basic 校验（手动设置 Realm 和 账号密码），校验不通过则抛出异常
     *
     * @param realm   领域
     * @param account 账号（格式为 user:password）
     */
    public void check(String realm, String account) {
        if (PbInnerUtil.isEmpty(account)) {
            account = PbManager.getConfig().getBasic();
        }
        String authorization = getAuthorizationValue();
        if (PbInnerUtil.isEmpty(authorization) || !authorization.equals(account)) {
            throwNotBasicAuthException(realm);
        }
    }

}
