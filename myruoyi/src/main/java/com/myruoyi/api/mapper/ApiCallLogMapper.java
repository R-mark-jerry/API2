package com.myruoyi.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.myruoyi.api.entity.ApiCallLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * API调用记录Mapper接口
 *
 * @author myruoyi
 * @date 2024-01-01
 */
@Mapper
public interface ApiCallLogMapper extends BaseMapper<ApiCallLog> {

    /**
     * 根据API ID查询调用记录列表
     *
     * @param apiId API ID
     * @return 调用记录列表
     */
    List<ApiCallLog> selectCallLogsByApiId(@Param("apiId") Long apiId);

    /**
     * 根据用户ID查询调用记录列表
     *
     * @param userId 用户ID
     * @return 调用记录列表
     */
    List<ApiCallLog> selectCallLogsByUserId(@Param("userId") Long userId);

    /**
     * 根据时间范围查询调用记录列表
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 调用记录列表
     */
    List<ApiCallLog> selectCallLogsByTimeRange(@Param("startTime") LocalDateTime startTime,
                                               @Param("endTime") LocalDateTime endTime);

    /**
     * 统计API调用次数
     *
     * @param apiId API ID
     * @return 调用次数
     */
    Long countCallsByApiId(@Param("apiId") Long apiId);

    /**
     * 统计API成功调用次数
     *
     * @param apiId API ID
     * @return 成功调用次数
     */
    Long countSuccessCallsByApiId(@Param("apiId") Long apiId);

    /**
     * 统计API失败调用次数
     *
     * @param apiId API ID
     * @return 失败调用次数
     */
    Long countFailCallsByApiId(@Param("apiId") Long apiId);

    /**
     * 获取API平均响应时间
     *
     * @param apiId API ID
     * @return 平均响应时间
     */
    Double getAvgResponseTimeByApiId(@Param("apiId") Long apiId);

    /**
     * 获取API最大响应时间
     *
     * @param apiId API ID
     * @return 最大响应时间
     */
    Long getMaxResponseTimeByApiId(@Param("apiId") Long apiId);

    /**
     * 获取API最小响应时间
     *
     * @param apiId API ID
     * @return 最小响应时间
     */
    Long getMinResponseTimeByApiId(@Param("apiId") Long apiId);

    /**
     * 批量删除调用记录
     *
     * @param endTime 删除截止时间
     * @return 删除数量
     */
    int deleteCallLogsBefore(@Param("endTime") LocalDateTime endTime);
}