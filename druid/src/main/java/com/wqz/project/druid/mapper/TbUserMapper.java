package com.wqz.project.druid.mapper;

import com.wqz.project.druid.model.TbUser;
import com.wqz.project.druid.utils.mapper.BaseSinglePkTableMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TbUserMapper extends BaseSinglePkTableMapper<TbUser> {

}