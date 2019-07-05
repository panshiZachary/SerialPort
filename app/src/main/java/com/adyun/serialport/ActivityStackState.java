package com.adyun.serialport;

/**
 * Created by Zachary
 * on 2019/7/4.
 */
public class ActivityStackState {

    private static int activityState;

    private static final  ActivityStackState ourInsatnce = new ActivityStackState();

    public static ActivityStackState getOurInsatnce() {
        return ourInsatnce;
    }
    private ActivityStackState(){

    }


    public static int getActivityState() {
        return activityState;
    }

    public static void setActivityState(int activityState) {
        ActivityStackState.activityState = activityState;
    }
}
