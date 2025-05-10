package com.bcb.trust.front.model.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.bcb.trust.front.entity.IndividualReportAcount;

public class IndividualReportAccountRowMapper implements RowMapper<IndividualReportAcount> {

    @Override
    public IndividualReportAcount mapRow(ResultSet rs, int rowNum) throws SQLException {
        IndividualReportAcount individualReportAcount = new IndividualReportAcount();

        individualReportAcount.setSecuentialIra(rs.getInt("RCI_SECUENCIAL"));
        individualReportAcount.setTrustNumberIra(rs.getInt("RCI_NUM_FIDEICOMISO"));
        individualReportAcount.setTrustNameIra(rs.getString("RCI_NOM_FIDEICOMISO"));
        individualReportAcount.setPeriodIra(rs.getString("RCI_PERIODO"));
        individualReportAcount.setDateIra(rs.getString("RCI_FECHA"));
        individualReportAcount.setNameLevel1Ira(rs.getString("RCI_NOM_NIVEL1"));
        individualReportAcount.setNameLevel2Ira(rs.getString("RCI_NOM_NIVEL2"));
        individualReportAcount.setNameLevel3Ira(rs.getString("RCI_NOM_NIVEL3"));
        individualReportAcount.setNameInversIra(rs.getString("RCI_NOM_INVERS"));
        individualReportAcount.setNumberN1Ira(rs.getInt("RCI_NUM_N1"));
        individualReportAcount.setNumberN2Ira(rs.getInt("RCI_NUM_N2"));
        individualReportAcount.setNumberN3Ira(rs.getInt("RCI_NUM_N3"));
        individualReportAcount.setNameN1Ira(rs.getString("RCI_NOM_N1"));
        individualReportAcount.setNameN2Ira(rs.getString("RCI_NOM_N2"));
        individualReportAcount.setNumberInverIra(rs.getInt("RCI_NUM_INVER"));
        individualReportAcount.setPreviousBalanceIra(rs.getDouble("RCI_SALDO_ANT"));
        individualReportAcount.setRateIra(rs.getDouble("RCI_TASA"));
        individualReportAcount.setDepositsIra(rs.getDouble("RCI_DEPOSITOS"));
        individualReportAcount.setWithdrawsIra(rs.getDouble("RCI_RETIROS"));
        individualReportAcount.setInterestIra(rs.getDouble("RCI_INTERESES"));
        individualReportAcount.setTaxIsrIra(rs.getDouble("RCI_ISR"));
        individualReportAcount.setPartialBalanceIra(rs.getDouble("RCI_SALDO_PARCIAL"));
        individualReportAcount.setParticipationIra(rs.getDouble("RCI_PARTICIPACION"));
        individualReportAcount.setFinalBalanceIra(rs.getDouble("RCI_SALDO_FINAL"));
        individualReportAcount.setAgreementIra(rs.getString("RCI_ACUERDO"));
        individualReportAcount.setDocumentIra(rs.getString("RCI_OFICIO"));
        individualReportAcount.setBeneficiaryIra(rs.getString("RCI_BENEFICIARIO"));

        return individualReportAcount;
    }

}
