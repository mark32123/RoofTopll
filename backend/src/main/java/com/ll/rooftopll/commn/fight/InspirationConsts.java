package com.ll.rooftopll.commn.fight;

import java.math.BigDecimal;
import java.util.Random;

public class InspirationConsts {

    private static final Random RANDOM = new Random();

    // 针对整体训练表现的话术
    private static final String[] BEAST_MODE = {
            "今天的你简直是健身房的野兽！",
            "这种强度，教练都看呆了！",
            "又是超越自我的一天，man！"
    };

    private static final String[] STEADY_MODE = {
            "汗水不会骗人，每一组都是在给未来投资。",
            "完成比完美更重要，打卡成功！",
            "这就是自律的力量，继续保持。"
    };

    private static final String[] QUICK_MODE = {
            "即便时间仓促，你也选择了坚持，为你点赞！",
            "速战速决，也是一种高效的训练策略。"
    };

    // 针对动作突破的标签
    public static final String TAG_BREAKTHROUGH = "🔥 突破历史记录";
    public static final String TAG_PROGRESS = "📈 状态回升";
    public static final String TAG_NORMAL = "✅ 稳步前进";

    /**
     * 根据训练数据随机生成激励语
     */
    public static String getSessionInspiration(long minutes, BigDecimal volume) {
        if (volume.compareTo(new BigDecimal("10000")) > 0) {
            return BEAST_MODE[RANDOM.nextInt(BEAST_MODE.length)];
        }
        if (minutes < 30 && volume.compareTo(BigDecimal.ZERO) > 0) {
            return QUICK_MODE[RANDOM.nextInt(QUICK_MODE.length)];
        }
        return STEADY_MODE[RANDOM.nextInt(STEADY_MODE.length)];
    }
}
