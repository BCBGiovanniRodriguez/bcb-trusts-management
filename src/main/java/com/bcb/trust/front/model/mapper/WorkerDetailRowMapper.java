package com.bcb.trust.front.model.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;

import com.bcb.trust.front.model.dto.WorkerDetail;

public class WorkerDetailRowMapper implements RowMapper<WorkerDetail> {

    @Override
    public WorkerDetail mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        WorkerDetail record = new WorkerDetail();
        record.setDatContrato(rs.getInt("DAT_CONTRATO"));
        record.setDatNivel(rs.getInt("DAT_NIVEL"));
        record.setDatId(rs.getInt("DAT_ID"));
        record.setDatParentId(rs.getInt("DAT_PARENT_ID"));
        record.setDatClave(rs.getString("DAT_CLAVE"));
        record.setDatDato(rs.getString("DAT_DATO"));
        record.setDatDescripcion(rs.getString("DAT_DESCRIPCION"));
        record.setDatFechaAlta(rs.getDate("DAT_FEC_ALTA") != null ? rs.getDate("DAT_FEC_ALTA").toLocalDate() : null);
        record.setDatFecUltMod(rs.getDate("DAT_FEC_ULT_MOD") != null ? rs.getDate("DAT_FEC_ULT_MOD").toLocalDate() : null);
        record.setDatEstatus(rs.getString("DAT_ESTATUS"));
        record.setDatFechaBaja(rs.getDate("DAT_FEC_BAJA") != null ? rs.getDate("DAT_FEC_BAJA").toLocalDate() : null);

        return record;
    }

}
