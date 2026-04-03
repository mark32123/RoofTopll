-- 插入系统预设的健身动作
INSERT INTO exercise (name, category, is_big_three, user_id) VALUES
('深蹲', '腿部', true, 0),
('卧推', '胸部', true, 0),
('硬拉', '背部', true, 0),
('杠铃划船', '背部', false, 0),
('过头推举', '肩部', false, 0),
('引体向上', '背部', false, 0),
('双杠臂屈伸', '胸部', false, 0),
('腿举', '腿部', false, 0),
('腿弯举', '腿部', false, 0),
('哑铃飞鸟', '胸部', false, 0),
('二头弯举', '手臂', false, 0),
('三头下压', '手臂', false, 0);
