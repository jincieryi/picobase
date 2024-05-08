package com.picobase.jwt.error;

/**
 * 定义 jwt 所有异常细分状态码
 */
public interface PbJwtErrorCode {

	/** 对 jwt 字符串解析失败 */
	int CODE_30201 = 30201;

	/** 此 jwt 的签名无效 */
	int CODE_30202 = 30202;

	/** 此 jwt 的 loginType 字段不符合预期 */
	int CODE_30203 = 30203;

	/** 此 jwt 已超时 */
	int CODE_30204 = 30204;

	/** 没有配置jwt秘钥 */
	int CODE_30205 = 30205;

	/** 登录时提供的账号id为空 */
	int CODE_30206 = 30206;

}
