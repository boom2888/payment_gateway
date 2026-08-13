package com.games.pay.dto;

import lombok.Data;
import com.games.common.annotation.Excel;

import java.math.BigDecimal;

@Data
public class ReconReportAllDto extends ReconReportMerchantDto {
    @Excel(name = "Payout ID", sort = 1)
    private String payoutId;

    @Excel(name = "Row Labels", sort = 2)
    private String rowLabels;

    @Excel(name = "Sum of Amount", sort = 3)
    private BigDecimal sumOfAmount;

    @Excel(name = "Sum of Discount Fee", sort = 4)
    private BigDecimal sumOfDiscountFee;

    @Excel(name = "Sum of Transaction Fee", sort = 5)
    private BigDecimal sumOfTransactionFee;

    @Excel(name = "Sum of Reserve", sort = 6)
    private BigDecimal sumOfReserve;

    @Excel(name = "Sum of Net Amount", sort = 7)
    private BigDecimal sumOfNetAmount;

    @Excel(name = "Sum of Settlement Amount", sort = 8)
    private BigDecimal sumOfSettlementAmount;

    @Excel(name = "Sum of Settlement Reserve", sort = 9)
    private BigDecimal sumOfSettlementReserve;

    @Excel(name = "Refund", sort = 10)
    private Integer refund;

    @Excel(name = "RDR", sort = 11)
    private Integer rdr;

    @Excel(name = "CB", sort = 12)
    private Integer cb;

    @Excel(name = "CB Cancelled", sort = 13)
    private Integer cbCancelled;

    @Excel(name = "", sort = 14)
    private String unnamed13;

    @Excel(name = "", sort = 15)
    private String unnamed14;

    private Long clientUniqueId;
}
