local key = ARGV[1]
local window_seconds = tonumber(ARGV[2])
local limit = tonumber(ARGV[3])

-- 获取当前时间戳（毫秒级），TIME 返回 {秒, 微秒}
local time_array = redis.call('TIME')
local current_time = tonumber(time_array[1]) * 1000 + math.floor(tonumber(time_array[2]) / 1000)
local window_ms = window_seconds * 1000
local start_time = current_time - window_ms

-- 生成唯一请求标识符（用微秒部分 + 递增计数器模拟随机性）
local request_id = tostring(current_time) .. ":" .. tostring(time_array[2])

-- 清理过期请求
redis.call('ZREMRANGEBYSCORE', key, '-inf', start_time)

-- 检查当前窗口请求数
local current_count = redis.call('ZCARD', key)

if current_count >= limit then
    -- 超过限流阈值，拒绝请求
    return 0
else
    -- 添加新请求到有序集合
    redis.call('ZADD', key, current_time, request_id)
    -- 设置键的过期时间（避免长期占用内存）
    redis.call('EXPIRE', key, window_seconds)
    return 1
end