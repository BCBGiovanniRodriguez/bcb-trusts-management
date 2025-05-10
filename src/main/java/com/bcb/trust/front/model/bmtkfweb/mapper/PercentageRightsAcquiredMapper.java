package com.bcb.trust.front.model.bmtkfweb.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.bcb.trust.front.model.bmtkfweb.dto.PercentageRightsAcquired;

public class PercentageRightsAcquiredMapper implements RowMapper<PercentageRightsAcquired> {

    @Override
    public PercentageRightsAcquired mapRow(ResultSet rs, int rowNum) throws SQLException {
        PercentageRightsAcquired record = new PercentageRightsAcquired();

        record.setYear(rs.getInt("DER_ANT"));
        record.setPercentage(rs.getDouble("DER_PCJ"));

        return record;
    }

}
