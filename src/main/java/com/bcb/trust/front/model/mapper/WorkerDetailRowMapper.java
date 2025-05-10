package com.bcb.trust.front.model.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.bcb.trust.front.model.dto.WorkerDetail;

public class WorkerDetailRowMapper implements RowMapper<WorkerDetail> {

    @Override
    public WorkerDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
        WorkerDetail record = new WorkerDetail();
        record.setDatClave(rs.getString("DAT_CLAVE"));
        record.setDatContrato(rs.getInt("DAT_CONTRATO"));
        record.setDatDato(rs.getString("DAT_DATO"));
        record.setDatDescripcion(rs.getString("DAT_DESCRIPCION"));
        record.setDatEstatus(rs.getString("DAT_ESTATUS"));
        record.setDatFechaAlta(rs.getDate("DAT_FEC_ALTA"));
        record.setDatFechaBaja(rs.getDate("DAT_FEC_BAJA"));
        record.setDatFecUltMod(rs.getDate("DAT_FEC_ULT_MOD"));
        record.setDatId(rs.getInt("DAT_ID"));
        record.setDatNivel(rs.getInt("DAT_NIVEL"));
        record.setDatParentId(rs.getInt("DAT_PARENT_ID"));

        return record;
    }

}
