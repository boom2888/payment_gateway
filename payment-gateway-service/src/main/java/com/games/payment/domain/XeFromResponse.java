package com.games.payment.domain;

import lombok.Data;
import java.util.List;

@Data
public class XeFromResponse {
    /*
    "terms": "https://www.xe.com/legal/",
  "privacy": "http://www.xe.com/privacy.php",
  "from": "CAD",
  "amount": 1,
  "timestamp": "2025-07-09T00:00:00Z",
  "to": [
    {
      "quotecurrency": "USD",
      "mid": 1.2345
    }
  ]
     */
    private String code;
    private String message;
    private String documentation_url;

    private String terms;
    private String privacy;
    private String from;
    private double amount;
    private String timestamp;
    private List<XeToResponse> to;
}
