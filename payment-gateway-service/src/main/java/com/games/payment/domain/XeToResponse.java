package com.games.payment.domain;

import lombok.Data;

@Data
public class XeToResponse {
    /*
    "quotecurrency": "USD",
      "mid": 1.2345
  ]
     */
    private String quotecurrency;
    private double mid;
}
