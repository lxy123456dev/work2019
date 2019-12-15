package com.leyou.mapper;

import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.annotation.RegisterMapper;
import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.special.InsertListMapper;


@RegisterMapper
public interface MyBaseMappers<T> extends IdListMapper<T,Long>, IdsMapper<T>, Mapper<T> {
}
