package com.example.service.impl;

import com.example.entity.OperateLog;
import com.example.service.OperateLogService;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: Ryan
 * Date: 2020-06-15
 * Time: 15:29
 */
@Service
public class OperateLogServiceImpl implements OperateLogService {
    @Override
    public void add(OperateLog operateLog) {
        System.out.println("--------------添加日志成功-----------");
    }
}