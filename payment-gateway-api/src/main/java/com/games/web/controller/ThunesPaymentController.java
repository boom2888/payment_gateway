//package com.games.web.controller;
//
//import cn.hutool.core.util.StrUtil;
//import cn.hutool.json.JSONObject;
//import cn.hutool.json.JSONUtil;
//import com.games.common.annotation.LogApi;
//import com.games.common.core.domain.R;
//import com.games.common.enums.ThunesStatusCode;
//import com.games.common.enums.ThunesTransferStatus;
//import com.games.common.exception.CustomException;
//import com.games.payment.domain.Shop;
//import com.games.payment.domain.ThunesTransferRecord;
//import com.games.payment.service.IShopService;
//import com.games.payment.service.IThunesTransferRecordService;
//import com.games.thunes.ThunesTransferService;
//import com.games.thunes.dto.TransferResponseDto;
//import com.games.thunes.dto.TransferStatusDto;
//import com.games.thunes.vo.TransferRequestVo;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.ApiResponse;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Date;
//
///**
// * Thunes 银行转账控制器
// *
// * @author System
// * @date 2025-01-08
// */
//@Api(tags = "Thunes Bank Transfer")
//@Slf4j
//@RestController
//@RequestMapping("/thunes")
//@RequiredArgsConstructor
//public class ThunesPaymentController {
//
//    private final ThunesTransferService thunesTransferService;
//    private final IShopService shopService;
//    private final IThunesTransferRecordService transferRecordService;
//
//    @ApiOperation("Initiate Bank Transfer")
//    @PostMapping("/transfer")
//    @ApiResponse(code = 200, message = "Transfer result", response = TransferResponseDto.class)
//    @LogApi(value = "TRANSFER", description = "Thunes银行转账")
//    public R<TransferResponseDto> transfer(@Validated @RequestBody TransferRequestVo request) {
//        log.info("收到Thunes转账请求 - 商户ID: {}, 外部订单号: {}", request.getMerchantId(), request.getExternalId());
//
//        try {
//            // 1. 验证商户存在
//            Shop shop = shopService.getById(request.getMerchantId());
//            if (shop == null) {
//                return R.fail(403, "商户不存在");
//            }
//
//            // 2. 验证签名
//            if (!thunesTransferService.verifyTransferSignature(shop, request)) {
//                return R.fail(401, "签名验证失败");
//            }
//
//            // 3. 处理转账
//            TransferResponseDto response = thunesTransferService.processTransfer(shop, request);
//
//            log.info("Thunes转账处理完成 - 转账ID: {}, 状态: {}", response.getTransferId(), response.getStatus());
//            return R.ok(response);
//
//        } catch (CustomException e) {
//            log.error("Thunes转账业务异常 - 商户ID: {}, 外部订单号: {}, 错误: {}",
//                    request.getMerchantId(), request.getExternalId(), e.getMessage());
//            return R.fail(500, e.getMessage());
//        } catch (Exception e) {
//            log.error("Thunes转账系统异常 - 商户ID: {}, 外部订单号: {}",
//                    request.getMerchantId(), request.getExternalId(), e);
//            return R.fail(500, "系统内部错误");
//        }
//    }
//
//    @ApiOperation("Query Transfer Status")
//    @GetMapping("/transfer/{transferId}/status")
//    @ApiResponse(code = 200, message = "Transfer status", response = TransferStatusDto.class)
//    public R<TransferStatusDto> getTransferStatus(@PathVariable Long transferId,
//            @RequestParam Long merchantId,
//            @RequestParam String signature) {
//        log.info("查询转账状态 - 转账ID: {}, 商户ID: {}", transferId, merchantId);
//
//        try {
//            // 1. 验证商户存在
//            Shop shop = shopService.getById(merchantId);
//            if (shop == null) {
//                return R.fail(403, "商户不存在");
//            }
//
//            // 2. 验证签名 - 使用转账ID作为签名内容
//            if (!thunesTransferService.verifyStatusQuerySignature(shop, transferId, signature)) {
//                return R.fail(401, "签名验证失败");
//            }
//
//            // 3. 查询转账状态
//            TransferStatusDto status = thunesTransferService.getTransferStatus(transferId, merchantId);
//
//            // 4. 验证转账记录属于该商户（需要从 Service 层获取商户ID）
//            // 这里需要修改 Service 方法来验证权限
//
//            return R.ok(status);
//        } catch (CustomException e) {
//            log.error("查询转账状态业务异常 - 转账ID: {}, 错误: {}", transferId, e.getMessage());
//            return R.fail(500, e.getMessage());
//        } catch (Exception e) {
//            log.error("查询转账状态系统异常 - 转账ID: {}", transferId, e);
//            return R.fail(500, "系统内部错误");
//        }
//    }
//
//    @ApiOperation("Handle Thunes Callback")
//    @PostMapping("/callback")
//    @ApiResponse(code = 200, message = "Callback handling result")
//    public R<String> handleCallback(@RequestBody String callbackData) {
//        log.info("收到Thunes回调数据: {}", callbackData);
//
//        try {
//            // 1. 解析回调数据
//            JSONObject callbackJson = JSONUtil.parseObj(callbackData);
//            String transactionId = callbackJson.getStr("id");
//            String status = callbackJson.getStr("status");
//            String statusClass = callbackJson.getStr("status_class");
//            String statusMessage = callbackJson.getStr("status_message");
//            String statusClassMessage = callbackJson.getStr("status_class_message");
//
//            if (StrUtil.isBlank(transactionId)) {
//                log.warn("回调数据中缺少交易ID");
//                return R.fail(400, "回调数据格式错误");
//            }
//
//            // 2. 查找对应的转账记录
//            ThunesTransferRecord record = transferRecordService.getByTransactionId(transactionId);
//            if (record == null) {
//                log.warn("未找到对应的转账记录 - 交易ID: {}", transactionId);
//                return R.fail(404, "转账记录不存在");
//            }
//
//            // 3. 获取有效状态码
//            String effectiveStatusCode = StrUtil.isNotBlank(status) ? status : statusClass;
//            String effectiveStatusMessage = StrUtil.isNotBlank(statusMessage) ? statusMessage : statusClassMessage;
//
//            // 4. 使用 ThunesStatusCode 解析状态
//            ThunesStatusCode thunesStatus = ThunesStatusCode.fromCode(effectiveStatusCode);
//            ThunesTransferStatus internalStatus = ThunesStatusCode.resolveInternalStatus(effectiveStatusCode);
//
//            // 5. 更新转账状态
//            record.setThunesStatus(effectiveStatusCode);
//            record.setStatus(internalStatus.getCode());
//            record.setCallbackReceivedAt(new Date());
//            record.setUpdatedAt(new Date());
//
//            // 6. 设置失败原因（针对失败或撤销状态）
//            if (ThunesStatusCode.isFailedStatus(effectiveStatusCode)) {
//                String failReason = ThunesStatusCode.resolveFailReason(effectiveStatusCode, effectiveStatusMessage);
//                record.setFailReason(failReason);
//                log.info("交易失败/撤销 - 交易ID: {}, 状态码: {}, 原因: {}", transactionId, effectiveStatusCode, failReason);
//            }
//
//            transferRecordService.updateById(record);
//
//            log.info("回调处理完成 - 交易ID: {}, Thunes状态: {}, 内部状态: {}, 状态描述: {}",
//                    transactionId, effectiveStatusCode, internalStatus.getCode(), thunesStatus.getDescription());
//
//            // 7. 通知商户
//            thunesTransferService.notifyMerchantCallback(record);
//
//            return R.ok("回调处理成功");
//        } catch (Exception e) {
//            log.error("处理Thunes回调异常", e);
//            return R.fail(500, "回调处理失败");
//        }
//    }
//
//    @ApiOperation("Mock Merchant Callback Receiver (For Testing)")
//    @PostMapping("/mock/merchant-callback")
//    @ApiResponse(code = 200, message = "Mock callback received successfully")
//    public R<String> mockMerchantCallback(@RequestBody String callbackData) {
//        log.info("==================== 模拟商户回调接收 ====================");
//        log.info("收到回调数据: {}", callbackData);
//        return R.ok("模拟回调接收成功");
//    }
//
//}
