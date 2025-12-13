package com.myruoyi.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.myruoyi.api.entity.ApiStatistics;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * API统计Mapper接口
 *
 * @author myruoyi
 * @date 2024-01-01
 */
@Mapper
public interface ApiStatisticsMapper extends BaseMapper<ApiStatistics> {

    /**
     * 根据API ID查询统计列表
     *
     * @param apiId API ID
     * @return 统计列表
     */
    List<ApiStatistics> selectStatisticsByApiId(@Param("apiId") Long apiId);

    /**
     * 根据日期范围查询统计列表
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 统计列表
     */
    List<ApiStatistics> selectStatisticsByDateRange(@Param("startDate") LocalDate startDate,
                                                 @Param("endDate") LocalDate endDate);

    /**
     * 根据API ID和日期查询统计
     *
     * @param apiId API ID
     * @param statDate 统计日期
     * @return 统计信息
     */
    ApiStatistics selectStatisticsByApiIdAndDate(@Param("apiId") Long apiId,
                                               @Param("statDate") LocalDate statDate);

    /**
     * 生成API统计数据
     *
     * @param statDate 统计日期
     */
    void generateApiStatistics(@Param("statDate") LocalDate statDate);
}