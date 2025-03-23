local key = ARGV[1]
local window_seconds = tonumber(ARGV[2])
local limit = tonumber(ARGV[3])

-- 获取当前时间戳（毫秒级）
local current_time = tonumber(redis.call('TIME')[1]) * 1000 + tonumber(redis.call('TIME')[2])
local window_ms = window_seconds * 1000
local start_time = current_time - window_ms

-- 生成唯一请求标识符
local request_id = tostring(current_time) .. ":" .. tostring(redis.call('RANDINT', 100000, 999999))

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
    return 1
end