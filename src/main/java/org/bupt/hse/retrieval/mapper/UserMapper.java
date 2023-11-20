package org.bupt.hse.retrieval.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.bupt.hse.retrieval.entity.UserDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Hu Saier
 * @since 2023-10-19
 */
@Mapper
public interface UserMapper extends BaseMapper<UserDO> {

}
