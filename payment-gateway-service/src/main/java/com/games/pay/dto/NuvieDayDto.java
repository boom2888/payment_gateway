package com.games.pay.dto;

import lombok.Data;
import org.apache.poi.hpsf.Decimal;

import java.math.BigDecimal;

@Data
public class NuvieDayDto {

    private String day;
    private String movementImportId;
    private BigDecimal b2c2FxRate;
}
