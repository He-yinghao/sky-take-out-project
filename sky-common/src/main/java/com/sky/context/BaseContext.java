package com.sky.context;

public class BaseContext {
    /**
     * 使用 ThreadLocal 来存储当前线程的 ID
     */
    public static ThreadLocal<Integer> threadLocal = new ThreadLocal<>();

    /**
     * 设置当前线程的 ID
     * @param id 当前线程的 ID
     */
    public static void setCurrentId(Integer id) {
        threadLocal.set(id);
    }

    /**
     * 获取当前线程的 ID
     * @return 当前线程的 ID，如果没有设置则返回 null
     */
    public static Integer getCurrentId() {
        return threadLocal.get();
    }

    /**
     * 清除当前线程的 ID
     */
    public static void clearCurrentId() {
        threadLocal.remove();
    }
}

