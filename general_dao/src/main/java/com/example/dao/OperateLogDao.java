package com.example.dao;

import com.example.entity.OperateLog;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
 * 操作日志记录(OperateLog)表数据库访问层
 *
 * @author makejava
 * @since 2020-07-05 15:45:21
 */
 @Mapper()
public interface OperateLogDao {

    /**
     * 通过ID查询单条数据
     *
     * @param operateId 主键
     * @return 实例对象
     */
    OperateLog queryById(Long operateId);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<OperateLog> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param operateLog 实例对象
     * @return 对象列表
     */
    List<OperateLog> queryAll(OperateLog operateLog);

    /**
     * 新增数据
     *
     * @param operateLog 实例对象
     * @return 影响行数
     */
    int insert(OperateLog operateLog);

    /**
     * 修改数据
     *
     * @param operateLog 实例对象
     * @return 影响行数
     */
    int update(OperateLog operateLog);

    /**
     * 通过主键删除数据
     *
     * @param operateId 主键
     * @return 影响行数
     */
    int deleteById(Long operateId);

}