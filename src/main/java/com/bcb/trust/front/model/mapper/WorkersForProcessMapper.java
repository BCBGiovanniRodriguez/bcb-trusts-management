package com.bcb.trust.front.model.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;

import com.bcb.trust.front.model.dto.WorkerForProcess;

public class WorkersForProcessMapper implements RowMapper<WorkerForProcess> {

    @Override
    @Nullable
    public WorkerForProcess mapRow(ResultSet rs, int rowNum) throws SQLException {
        WorkerForProcess record = new WorkerForProcess();

        record.setSubaccount(rs.getString("subaccount"));
        record.setStatus(rs.getInt("status"));
        record.setMessage(rs.getString("message"));
        
        return record;
    }
    
}
