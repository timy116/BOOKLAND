package com.bookland.utils;

public class SnowFlakeUtil {
    private final static long START_STMP = 1480166465631L;

    private final static long SEQUENCE_BIT = 12;
    private final static long MACHINE_BIT = 5;
    private final static long DATACENTER_BIT = 5;

    // 最大值
    private final static long MAX_DATACENTER_NUM = ~(-1L << DATACENTER_BIT);
    private final static long MAX_MACHINE_NUM = ~(-1L << MACHINE_BIT);
    private final static long MAX_SEQUENCE = ~(-1L << SEQUENCE_BIT);

    // 向左位移
    private final static long MACHINE_LEFT = SEQUENCE_BIT;
    private final static long DATACENTER_LEFT = SEQUENCE_BIT + MACHINE_BIT;
    private final static long TIMESTMP_LEFT = DATACENTER_LEFT + DATACENTER_BIT;

    private final long datacenterId;
    private final long machineId;
    private long sequence = 0L;

    // 前一次 timeStamp
    private long lastStmp = -1L;

    public SnowFlakeUtil(long datacenterId, long machineId) {
        if (datacenterId > MAX_DATACENTER_NUM || datacenterId < 0)
        {
            throw new IllegalArgumentException("datacenterId can't be greater than MAX_DATACENTER_NUM or less than 0");
        }
        if (machineId > MAX_MACHINE_NUM || machineId < 0)
        {
            throw new IllegalArgumentException("machineId can't be greater than MAX_MACHINE_NUM or less than 0");
        }
        this.datacenterId = datacenterId;
        this.machineId = machineId;
    }

    // 編號過長，因此使用 int 截斷
    public synchronized Integer getNextId() {
        long currStmp = getNewstmp();
        if (currStmp < lastStmp) {
            throw new RuntimeException("Clock moved backwards.Refusing to generate id");
        }
        if (currStmp == lastStmp) {
            // 若在相同毫秒内，序列號自增
            sequence = (sequence + 1) & MAX_SEQUENCE;

            // 同一毫秒的序列數已達到最大
            if (sequence == 0L)
            {
                currStmp = getNextMill();
            }
        } else {
            // 若在不同毫秒内，則序列號置 reset to 0
            sequence = 0L;
        }
        lastStmp = currStmp;

        return Math.abs((int) ((currStmp - START_STMP) << TIMESTMP_LEFT
                | datacenterId << DATACENTER_LEFT
                | machineId << MACHINE_LEFT
                | sequence));
    }

    private long getNextMill()
    {
        long mill = getNewstmp();
        while (mill <= lastStmp)
        {
            mill = getNewstmp();
        }
        return mill;
    }

    private long getNewstmp()
    {
        return System.currentTimeMillis();
    }
}
