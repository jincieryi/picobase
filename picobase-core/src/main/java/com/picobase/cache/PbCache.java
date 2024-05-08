package com.picobase.cache;


import com.picobase.session.PbSession;

import java.util.List;

/**
 * Pb 缓存接口
 *
 * <p>
 * 此接口的不同实现类可将数据存储至不同位置，如：内存Map、Redis 等等。
 * 如果你要自定义数据存储策略，也需通过实现此接口来完成。
 * </p>
 */
public interface PbCache {

    /**
     * 常量，表示一个 key 永不过期 （在一个 key 被标注为永远不过期时返回此值）
     */
    long NEVER_EXPIRE = -1;

    /**
     * 常量，表示系统中不存在这个缓存（在对不存在的 key 获取剩余存活时间时返回此值）
     */
    long NOT_VALUE_EXPIRE = -2;


    // --------------------- 字符串读写 ---------------------

    /**
     * 获取 value，如无返空
     *
     * @param key 键名称
     * @return value
     */
    String get(String key);

    /**
     * 写入 value，并设定存活时间（单位: 秒）
     *
     * @param key     键名称
     * @param value   值
     * @param timeout 数据有效期（值大于0时限时存储，值=-1时永久存储，值=0或小于-2时不存储）
     */
    void set(String key, String value, long timeout);

    /**
     * 更新 value （过期时间不变）
     *
     * @param key   键名称
     * @param value 值
     */
    void update(String key, String value);

    /**
     * 删除 value
     *
     * @param key 键名称
     */
    void delete(String key);

    /**
     * 获取 value 的剩余存活时间（单位: 秒）
     *
     * @param key 指定 key
     * @return 这个 key 的剩余存活时间
     */
    long getTimeout(String key);

    /**
     * 修改 value 的剩余存活时间（单位: 秒）
     *
     * @param key     指定 key
     * @param timeout 过期时间（单位: 秒）
     */
    void updateTimeout(String key, long timeout);


    // --------------------- 对象读写 ---------------------

    /**
     * 获取 Object，如无返空
     *
     * @param key 键名称
     * @return object
     */
    Object getObject(String key);

    /**
     * 写入 Object，并设定存活时间 （单位: 秒）
     *
     * @param key     键名称
     * @param object  值
     * @param timeout 存活时间（值大于0时限时存储，值=-1时永久存储，值=0或小于-2时不存储）
     */
    void setObject(String key, Object object, long timeout);

    /**
     * 更新 Object （过期时间不变）
     *
     * @param key    键名称
     * @param object 值
     */
    void updateObject(String key, Object object);

    /**
     * 删除 Object
     *
     * @param key 键名称
     */
    void deleteObject(String key);

    /**
     * 获取 Object 的剩余存活时间 （单位: 秒）
     *
     * @param key 指定 key
     * @return 这个 key 的剩余存活时间
     */
    long getObjectTimeout(String key);

    /**
     * 修改 Object 的剩余存活时间（单位: 秒）
     *
     * @param key     指定 key
     * @param timeout 剩余存活时间
     */
    void updateObjectTimeout(String key, long timeout);


    // --------------------- PbSession 读写 （默认复用 Object 读写方法） ---------------------

    /**
     * 获取 PbSession，如无返空
     *
     * @param sessionId sessionId
     * @return PbSession
     */
    default PbSession getSession(String sessionId) {
        return (PbSession) getObject(sessionId);
    }

    /**
     * 写入 PbSession，并设定存活时间（单位: 秒）
     *
     * @param session 要保存的 PbSession 对象
     * @param timeout 过期时间（单位: 秒）
     */
    default void setSession(PbSession session, long timeout) {
        setObject(session.getId(), session, timeout);
    }

    /**
     * 更新 PbSession
     *
     * @param session 要更新的 PbSession 对象
     */
    default void updateSession(PbSession session) {
        updateObject(session.getId(), session);
    }

    /**
     * 删除 PbSession
     *
     * @param sessionId sessionId
     */
    default void deleteSession(String sessionId) {
        deleteObject(sessionId);
    }

    /**
     * 获取 PbSession 剩余存活时间（单位: 秒）
     *
     * @param sessionId 指定 PbSession
     * @return 这个 PbSession 的剩余存活时间
     */
    default long getSessionTimeout(String sessionId) {
        return getObjectTimeout(sessionId);
    }

    /**
     * 修改 PbSession 剩余存活时间（单位: 秒）
     *
     * @param sessionId 指定 PbSession
     * @param timeout   剩余存活时间
     */
    default void updateSessionTimeout(String sessionId, long timeout) {
        updateObjectTimeout(sessionId, timeout);
    }


    // --------------------- 会话管理 ---------------------

    /**
     * 搜索数据
     *
     * @param prefix   前缀
     * @param keyword  关键字
     * @param start    开始处索引
     * @param size     获取数量  (-1代表从 start 处一直取到末尾)
     * @param sortType 排序类型（true=正序，false=反序）
     * @return 查询到的数据集合
     */
    List<String> searchData(String prefix, String keyword, int start, int size, boolean sortType);


    // --------------------- 生命周期 ---------------------

    /**
     * 当此 PbCache 实例被装载时触发
     */
    default void init() {
    }

    /**
     * 当此 PbCache 实例被卸载时触发
     */
    default void destroy() {
    }

}
