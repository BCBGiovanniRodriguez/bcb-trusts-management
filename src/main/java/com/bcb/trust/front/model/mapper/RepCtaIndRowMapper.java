package com.bcb.trust.front.model.mapper;


import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.bcb.trust.front.model.dto.RepCtaInd;

public class RepCtaIndRowMapper implements RowMapper<RepCtaInd> {

    @Override
    public RepCtaInd mapRow(ResultSet rs, int rowNum) throws SQLException {
        RepCtaInd record = new RepCtaInd();
        record.setRciFecha(rs.getString("RCI_FECHA"));
        record.setRciNomInvers(rs.getString("RCI_NOM_INVERS"));
        record.setRciDepositos(rs.getDouble("RCI_DEPOSITOS"));
        record.setRciRetiros(rs.getDouble("RCI_RETIROS"));
        record.setRciRetiros(rs.getDouble("RCI_RETIROS"));
        record.setRciSecuencial(rs.getInt("RCI_SECUENCIAL"));

        return record;
    }

}