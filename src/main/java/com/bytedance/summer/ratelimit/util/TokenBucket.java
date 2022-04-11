package com.bytedance.summer.ratelimit.util;


import java.util.concurrent.ExecutorService;

import static java.lang.Math.floor;
import static java.lang.Math.min;

/**
 * @author pjl
 * @date 2019/8/4 12:41
 * 令牌桶实现：
 * 初始化需要桶容量以及每秒放入令牌数。
 * 有个线程专门按时往桶里加令牌。
 * 如果需求令牌时，桶里令牌不够，直接返回失败。这一步是非阻塞的
 * 如果有令牌，而扣减失败，也算失败。扣减是阻塞的
 * 添加令牌按理说一定会成功。
 */
public class TokenBucket {
    private int minSleep = 10;//最低补给间隔
    private double maxToken;//桶容量

    private TokenAdder tokenAdder;//令牌制造机
    private double tokenNum;//当前令牌数

    TokenBucket() {
        maxToken = 10;
        tokenAdder = new TokenAdder(5);
        tokenNum = maxToken;
    }

    public TokenBucket(int maxToken, int tokenPerSecond) {
        if (maxToken <= 0) {
            throw new IllegalArgumentException("maxToken must be bigger than 0");
        }
        if (tokenPerSecond <= 0) {
            throw new IllegalArgumentException("tokenPerSecond must be bigger than 0");
        }
        this.maxToken = (double) maxToken;
        tokenNum = this.maxToken;
        tokenAdder = new TokenAdder((double) tokenPerSecond);
        tokenAdder.start();
    }

    public TokenBucket(int maxToken, int tokenPerSecond,int minSleep) {
        this.minSleep=minSleep;
        if (maxToken <= 0) {
            throw new IllegalArgumentException("maxToken must be bigger than 0");
        }
        if (tokenPerSecond <= 0) {
            throw new IllegalArgumentException("tokenPerSecond must be bigger than 0");
        }
        this.maxToken = (double) maxToken;
        tokenNum = this.maxToken;
        tokenAdder = new TokenAdder((double) tokenPerSecond);
        tokenAdder.start();
    }
    /*
    获取token
     */
    public boolean getToken() {
        //没令牌直接失败，不会阻塞
        if (tokenNum < 1)
            return false;
            //有令牌并且减成功，返回成功，否则也失败
        else return changeTokenNum(-1);
    }

    /*
    修改token数量
    这里设置为同步方法
     */
    private synchronized boolean changeTokenNum(double deltaToken) {
        if (deltaToken < 1) {
            /* 减令牌操作，需要保证令牌数低于0。
             * 区别于增加操作，低于0时返回false
             */
            if (deltaToken + tokenNum < 0)
                return false;
            tokenNum += deltaToken;
            return true;
        } else if (deltaToken > 0) {
            //增加令牌操作，需要保证令牌数不超过上限
            tokenNum = min(maxToken, tokenNum + deltaToken);
            return true;
        } else
            return true;
    }
    /*
    令牌生产机
     */
    class TokenAdder extends Thread {
        //默认每minSleep进行一次补给
        long sleep = minSleep;
        double deltaToken;
        /*
        给定每秒令牌数，自动计算每个多少秒放一个令牌
        如果频率太高肯定也是不好的，设置最少10ms一次
        为了这里写的舒服，令牌都变成double型了。。。。
         */
        TokenAdder(double tokenPerSecond) {
            double avgInteval = 1000 / tokenPerSecond;
            /*
             *保证每次放入令牌时间间隔不低于minSleep
             */
            if (avgInteval > minSleep) {
                sleep = Math.round(avgInteval);
                deltaToken = 1;
            } else {
                deltaToken = tokenPerSecond * minSleep / 1000;
            }
        }

        /*
        自动添加令牌的线程
         */
        public void run() {
            while (true) {
                changeTokenNum(deltaToken);
                try {
                    sleep(sleep);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
