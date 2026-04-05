package com.youkang.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.youkang.system.domain.PriceConfig;
import com.youkang.system.domain.resp.price.PriceConfigResp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 价格配置Mapper接口
 *
 * @author youkang
 */
public interface PriceConfigMapper extends BaseMapper<PriceConfig> {

    /**
     * 查询课题组价格列表（合并模板和自定义价格）
     *
     * @param groupId 课题组ID
     * @return 价格配置列表
     */
    List<PriceConfigResp> selectPriceListByGroupId(@Param("groupId") Integer groupId);

    /**
     * 根据条件查找相邻的价格配置（用于联动修改范围）
     *
     * @param sampleType        样品类型
     * @param project           测序项目
     * @param plasmidLengthMin  质粒长度下限
     * @param plasmidLengthMax  质粒长度上限
     * @param fragmentSizeMin   片段大小下限
     * @param fragmentSizeMax   片段大小上限
     * @param groupId           课题组ID（NULL = 模板）
     * @return 价格配置列表
     */
    PriceConfig selectAdjacentPrice(
            @Param("sampleType") String sampleType,
            @Param("project") String project,
            @Param("plasmidLengthMin") Integer plasmidLengthMin,
            @Param("plasmidLengthMax") Integer plasmidLengthMax,
            @Param("fragmentSizeMin") Integer fragmentSizeMin,
            @Param("fragmentSizeMax") Integer fragmentSizeMax,
            @Param("groupId") Integer groupId);
}
