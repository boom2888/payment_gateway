package com.games.pay.dto;

import com.games.common.core.domain.entity.SysDictData;
import com.games.payment.domain.Customer;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 客户对象 customer
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class CustomerWithUserDto extends Customer {

    private String displayName;
    public SysDictData toDict() {
        SysDictData data = new SysDictData();
        data.setDictLabel(displayName);
        data.setDictValue(getId().toString());
        return data;
    }
}
