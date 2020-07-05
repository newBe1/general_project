package com.example.service;

import com.example.entity.OperateLog;
import java.util.List;

/**
 * 操作日志记录(OperateLog)表服务接口
 *
 * @author makejava
 * @since 2020-07-05 15:45:55
 */
public interface OperateLogService {

    /**
     * 通过ID查询单条数据
     *
     * @param operateId 主键
     * @return 实例对象
     */
    OperateLog queryById(Long operateId);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<OperateLog> queryAllByLimit(int offset, int limit);
    
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
     * @return 实例对象
     */
    OperateLog insert(OperateLog operateLog);

    /**
     * 修改数据
     *
     * @param operateLog 实例对象
     * @return 实例对象
     */
    OperateLog update(OperateLog operateLog);

    /**
     * 通过主键删除数据
     *
     * @param operateId 主键
     * @return 是否成功
     */
    boolean deleteById(Long operateId);

}