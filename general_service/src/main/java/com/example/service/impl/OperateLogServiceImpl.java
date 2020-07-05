package com.example.service.impl;

import com.example.entity.OperateLog;
import com.example.dao.OperateLogDao;
import com.example.service.OperateLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 操作日志记录(OperateLog)表服务实现类
 *
 * @author makejava
 * @since 2020-07-05 15:45:55
 */
@Service("operateLogService")
public class OperateLogServiceImpl implements OperateLogService {
    @Resource
    private OperateLogDao operateLogDao;

    /**
     * 通过ID查询单条数据
     *
     * @param operateId 主键
     * @return 实例对象
     */
    @Override
    public OperateLog queryById(Long operateId) {
        return this.operateLogDao.queryById(operateId);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<OperateLog> queryAllByLimit(int offset, int limit) {
        return this.operateLogDao.queryAllByLimit(offset, limit);
    }
    
    /**
     * 通过实体作为筛选条件查询
     *
     * @param operateLog 实例对象
     * @return 对象列表
     */
    @Override
    public List<OperateLog> queryAll(OperateLog operateLog) {
        return this.operateLogDao.queryAll(operateLog);
    }

    /**
     * 新增数据
     *
     * @param operateLog 实例对象
     * @return 实例对象
     */
    @Override
    public OperateLog insert(OperateLog operateLog) {
        this.operateLogDao.insert(operateLog);
        return operateLog;
    }

    /**
     * 修改数据
     *
     * @param operateLog 实例对象
     * @return 实例对象
     */
    @Override
    public OperateLog update(OperateLog operateLog) {
        this.operateLogDao.update(operateLog);
        return this.queryById(operateLog.getOperateId());
    }

    /**
     * 通过主键删除数据
     *
     * @param operateId 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long operateId) {
        return this.operateLogDao.deleteById(operateId) > 0;
    }
}