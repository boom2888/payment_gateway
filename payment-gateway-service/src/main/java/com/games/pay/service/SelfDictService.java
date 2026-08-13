package com.games.pay.service;

import cn.hutool.core.util.StrUtil;
import com.games.common.core.domain.entity.SysDictData;
import com.games.pay.dto.CustomerWithUserDto;
import com.games.pay.enums.SelfDirEnums;
import com.games.payment.domain.*;
import com.games.payment.service.*;
import com.games.system.service.ISelfDictService;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 业务扩展自定义字典服务类
 */
@Service
public class SelfDictService implements ISelfDictService {
    @Resource
    private IShopService shopService;
    @Resource
    private MyCustomerService myCustomerService;
    @Resource
    private ICustomerWalletService customerWalletService;
    @Resource
    private ICurrencyService currencyService;
    @Resource
    private ICryptoService cryptoCurrencyService;
    @Resource
    private IBankAccountService bankAccountService;
    @Resource
    private IMovementImportService movementImportService;

    @Override
    public List<SysDictData>  selectDictDataByType(String dictType) {
        List<SysDictData> data = new ArrayList<>();
        if(StrUtil.equals(SelfDirEnums.MERCHANT_ID.name(), dictType)){
            List<Shop> nftTypes = shopService.list();
            nftTypes.forEach(item-> data.add(item.toDict()));
        }else if(StrUtil.equals(SelfDirEnums.CUSTOMER_ID.name(), dictType)){
            List<CustomerWithUserDto> nftTypes = myCustomerService.selectCustomerWithUser();
            nftTypes.forEach(item-> data.add(item.toDict()));
        }else if(StrUtil.equals(SelfDirEnums.CUSTOMER_WALLET_ID.name(), dictType)){
            List<CustomerWallet> nftTypes = customerWalletService.list();
            nftTypes.forEach(item-> data.add(item.toDict()));
        }else if(StrUtil.equals(SelfDirEnums.CURRENCY_ID.name(), dictType)){
            List<Currency> nftTypes = currencyService.list();
            nftTypes.forEach(item-> data.add(item.toDict()));
        }else if(StrUtil.equals(SelfDirEnums.CRYPTO_ID.name(), dictType)){
            List<Crypto> nftTypes = cryptoCurrencyService.list();
            nftTypes.forEach(item-> data.add(item.toDict()));
        }else if(StrUtil.equals(SelfDirEnums.CRYPTO_ID.name(), dictType)){
            List<Crypto> nftTypes = cryptoCurrencyService.list();
            nftTypes.forEach(item-> data.add(item.toDict()));
        }else if(StrUtil.equals(SelfDirEnums.BANK_ACCOUNT_ID.name(), dictType)){
            List<BankAccount> nftTypes = bankAccountService.list();
            nftTypes.forEach(item-> data.add(item.toDict()));
        }else if(StrUtil.equals(SelfDirEnums.MOVEMENT_IMPORT_ID.name(), dictType)){
            List<MovementImport> movementImportList = movementImportService.list();
            movementImportList.forEach(item-> data.add(item.toDict()));
        }else {
            return null;
        }
        return data;
    }
}
