package com.yy.base;

import android.app.Activity;


import com.yy.base.utils.LogUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Activity堆栈管理类
 */
public class ActivityController {

    private static ActivityController instance;

    public static ActivityController getInstance() {
        if (instance == null)
            instance = new ActivityController();
        return instance;
    }

    /**
     * 释放单例对象
     */
    public static void release() {
        if (instance.getActivityList() != null) {
            instance.dispose();
        }
        instance = null;
    }

    private List<Activity> activityList;

    public List<Activity> getActivityList() {
//		if (this.activityList == null)
//			this.activityList = new ArrayList<BasePresenterActivity>();
        return activityList;
    }

    private ActivityController() {

    }

    /**
     * 向Activity管理堆栈中存放Activity
     *
     * @param activity 目标Activity
     */
    public void addActivity(Activity activity) {
        if (this.activityList == null)
            this.activityList = new ArrayList<Activity>();
        activityList.add(activity);
    }



    /**
     * 将Activity从堆栈中移除
     *
     * @param activity 目标Acitivity
     */
    public void removeActivity(Activity activity) {
        if (activityList != null) {
            if (activityList.contains(activity)) {
                activityList.remove(activity);
            }
        }
    }

    /**
     * 完全退出
     */
    public void exit() {
        if (activityList != null && !activityList.isEmpty()) {
            try {
                for (Activity ac : activityList) {
                    ac.finish();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void dispose() {
        activityList = null;
    }

    /**
     * 根据class name获取activity
     * <p/>
     * Acitivity名称
     *
     * @return 获得的对象
     */
    public Activity getActivityByClassName(String activityName) {
        if (activityList == null || activityList.isEmpty()) {
            return null;
        }
        for (Activity ac : activityList) {
            if (ac.getClass().getName().indexOf(activityName) >= 0) {
                return ac;
            }
        }
        return null;
    }

    /**
     * 根据Activity类名获取Activity对象
     *
     * @param cs Activity的class对象
     * @return 获得的对象
     */
    public Activity getActivityByClass(Class<Activity> cs) {
        if (activityList == null || activityList.isEmpty()) {
            return null;
        }
        for (Activity ac : activityList) {
            if (ac.getClass().equals(cs)) {
                return ac;
            }
        }
        return null;
    }

    /**
     * 弹出activity
     *
     * @param activity
     */
    public void popActivity(Activity activity) {
        removeActivity(activity);
        activity.finish();
    }

    /**
     * 弹出activity到
     *
     * @param cs
     */
    public <T extends Activity> void popUntilActivity(Class<T>... cs) {
        if (activityList == null || activityList.isEmpty()) {
            return;
        }
        List<Activity> list = new ArrayList<Activity>();
        for (int i = activityList.size() - 1; i >= 0; i--) {
            Activity ac = activityList.get(i);
            boolean isTop = false;
            for (int j = 0; j < cs.length; j++) {
                if (ac.getClass().equals(cs[j])) {
                    isTop = true;
                    break;
                }
            }
            if (!isTop) {
                list.add(ac);
            } else
                break;
        }
        for (Iterator<Activity> iterator = list.iterator();
             iterator.hasNext(); ) {
            Activity activity = iterator.next();
            popActivity(activity);
        }
    }


    /**
     * 退出所有的activity
     */
    public void popAllActivity() {
        if (activityList == null || activityList.isEmpty()) {
            return;
        }
        try {
            for (Activity ac : activityList) {
                ac.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void popAllExc(Activity activity) {
        if (activityList == null || activityList.isEmpty()) {
            return;
        }
        try {
            for (int i = activityList.size() - 1; i >= 0; i--) {
                Activity basePresenterActivity = activityList.get(i);
                if (basePresenterActivity.getClass().getSimpleName()
                        .equals(activity.getClass().getSimpleName())) {
                    LogUtil.getUtils().d("===========popAllExc:"+activity.getClass().getSimpleName()+"==============");
                    continue;
                }
                basePresenterActivity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
