package com.games.thunes;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.games.common.constant.ThunesConstants;
import com.games.common.exception.CustomException;
import com.games.framework.config.ThunesConfig;
import com.games.thunes.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Thunes API 服务类
 *
 * @author System
 * @date 2025-01-08
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ThunesApiService {

    private final ThunesConfig thunesConfig;
    private final ObjectMapper objectMapper;

    /**
     * 获取安全的超时时间
     */
    private int getSafeTimeout() {
        return thunesConfig.getTimeout() != null ? thunesConfig.getTimeout() : ThunesConstants.DEFAULT_TIMEOUT;
    }

    /**
     * 获取支付方列表
     *
     * @param countryCode 国家代码（可选）
     * @param currency    货币代码（可选）
     * @return 支付方列表
     */
    public List<PayerDto> getPayers(String countryCode, String currency) {
        String url = thunesConfig.getApiBasePath() + ThunesConstants.ENDPOINT_PAYERS;

        // 构建查询参数
        StringBuilder queryParams = new StringBuilder();
        if (StrUtil.isNotBlank(countryCode)) {
            queryParams.append("country_iso_code=").append(countryCode);
        }
        if (StrUtil.isNotBlank(currency)) {
            if (queryParams.length() > 0) {
                queryParams.append("&");
            }
            queryParams.append("currency=").append(currency);
        }

        if (queryParams.length() > 0) {
            url += "?" + queryParams.toString();
        }

        HttpRequest request = HttpRequest.get(url)
                .header(ThunesConstants.HEADER_AUTHORIZATION,
                        ThunesConstants.AUTH_TYPE_BASIC + " " + thunesConfig.getBasicAuthString())
                .header(ThunesConstants.HEADER_CONTENT_TYPE, ThunesConstants.CONTENT_TYPE_JSON)
                .timeout(getSafeTimeout());

        try (HttpResponse response = request.execute()) {
            log.info("Thunes getPayers 请求: {}, 响应状态: {}", url, response.getStatus());

            if (!response.isOk()) {
                String errorMsg = "获取支付方列表失败: " + response.getStatus() + " - " + response.body();
                log.error(errorMsg);
                throw new CustomException(errorMsg);
            }

            String responseBody = response.body();
            log.info("Thunes getPayers 响应: {}", responseBody);

            return JSONUtil.toList(responseBody, PayerDto.class);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error("调用 Thunes getPayers API 异常", e);
            throw new CustomException("调用 Thunes API 失败: " + e.getMessage());
        }
    }

    /**
     * 创建报价
     *
     * @param request 报价请求
     * @return 报价信息
     */
    public QuotationDto createQuotation(QuotationRequestDto request) {
        String url = thunesConfig.getApiBasePath() + ThunesConstants.ENDPOINT_QUOTATIONS;

        String requestBody = JSONUtil.toJsonStr(request);
        log.info("Thunes createQuotation 请求: {}", url);

        HttpRequest httpRequest = HttpRequest.post(url)
                .header(ThunesConstants.HEADER_AUTHORIZATION,
                        ThunesConstants.AUTH_TYPE_BASIC + " " + thunesConfig.getBasicAuthString())
                .header(ThunesConstants.HEADER_CONTENT_TYPE, ThunesConstants.CONTENT_TYPE_JSON)
                .body(requestBody)
                .timeout(thunesConfig.getTimeout());

        try (HttpResponse response = httpRequest.execute()) {
            log.info("Thunes createQuotation 响应状态: {}", response.getStatus());

            if (!response.isOk()) {
                String responseBody = response.body();
                log.error("Thunes createQuotation 失败响应: {}", responseBody);

                // 特殊处理源国家未授权错误
                if (responseBody.contains("1000998") && responseBody.contains("Source country not authorized")) {
                    throw new CustomException(
                            "Thunes 账户未被授权使用指定的源国家。" +
                                    "请联系 Thunes 支持团队为您的账户开通相应的源国家权限。" +
                                    "错误详情: " + responseBody);
                }

                // 特殊处理支付方不活跃错误
                if (responseBody.contains("1003001") && responseBody.contains("Payer is inactive")) {
                    throw new CustomException(
                            "选择的支付方在您的 Thunes 账户中不活跃。" +
                                    "请联系 Thunes 支持团队激活所需的支付方，或系统将自动尝试其他可用的支付方。" +
                                    "错误详情: " + responseBody);
                }

                String errorMsg = "创建报价失败: " + response.getStatus() + " - " + responseBody;
                throw new CustomException(errorMsg);
            }

            String responseBody = response.body();
            log.info("Thunes createQuotation 响应: {}", responseBody);

            return JSONUtil.toBean(responseBody, QuotationDto.class);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error("调用 Thunes createQuotation API 异常", e);
            throw new CustomException("调用 Thunes API 失败: " + e.getMessage());
        }
    }

    /**
     * 创建交易
     *
     * @param quotationId 报价ID
     * @param request     交易请求
     * @return 交易信息
     */
    public TransactionDto createTransaction(String quotationId, TransactionRequestDto request) {
        String url = thunesConfig.getApiBasePath() + ThunesConstants.ENDPOINT_QUOTATIONS + "/" + quotationId
                + ThunesConstants.ENDPOINT_TRANSACTIONS;

        String requestBody;
        try {
            requestBody = objectMapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            log.error("Thunes createTransaction 请求序列化失败", e);
            throw new CustomException("创建交易请求序列化失败: " + e.getMessage());
        }
        log.info("Thunes createTransaction 请求: {}", url);

        HttpRequest httpRequest = HttpRequest.post(url)
                .header(ThunesConstants.HEADER_AUTHORIZATION,
                        ThunesConstants.AUTH_TYPE_BASIC + " " + thunesConfig.getBasicAuthString())
                .header(ThunesConstants.HEADER_CONTENT_TYPE, ThunesConstants.CONTENT_TYPE_JSON)
                .body(requestBody)
                .timeout(thunesConfig.getTimeout());

        try (HttpResponse response = httpRequest.execute()) {
            log.info("Thunes createTransaction 响应状态: {}", response.getStatus());

            if (!response.isOk()) {
                String errorMsg = "创建交易失败: " + response.getStatus() + " - " + response.body();
                log.error(errorMsg);
                throw new CustomException(errorMsg);
            }

            String responseBody = response.body();
            log.info("Thunes createTransaction 响应: {}", responseBody);

            return JSONUtil.toBean(responseBody, TransactionDto.class);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error("调用 Thunes createTransaction API 异常", e);
            throw new CustomException("调用 Thunes API 失败: " + e.getMessage());
        }
    }

    /**
     * 确认交易
     *
     * @param transactionId 交易ID
     * @return 交易信息
     */
    public TransactionDto confirmTransaction(String transactionId) {
        String url = thunesConfig.getApiBasePath() + ThunesConstants.ENDPOINT_TRANSACTIONS + "/" + transactionId
                + "/confirm";

        log.info("Thunes confirmTransaction 请求: {}", url);

        HttpRequest httpRequest = HttpRequest.post(url)
                .header(ThunesConstants.HEADER_AUTHORIZATION,
                        ThunesConstants.AUTH_TYPE_BASIC + " " + thunesConfig.getBasicAuthString())
                .header(ThunesConstants.HEADER_CONTENT_TYPE, ThunesConstants.CONTENT_TYPE_JSON)
                .timeout(thunesConfig.getTimeout());

        try (HttpResponse response = httpRequest.execute()) {
            log.info("Thunes confirmTransaction 响应状态: {}", response.getStatus());

            if (!response.isOk()) {
                String errorMsg = "确认交易失败: " + response.getStatus() + " - " + response.body();
                log.error(errorMsg);
                throw new CustomException(errorMsg);
            }

            String responseBody = response.body();
            log.info("Thunes confirmTransaction 响应: {}", responseBody);

            return JSONUtil.toBean(responseBody, TransactionDto.class);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error("调用 Thunes confirmTransaction API 异常", e);
            throw new CustomException("调用 Thunes API 失败: " + e.getMessage());
        }
    }

    /**
     * 查询交易状态
     *
     * @param transactionId 交易ID
     * @return 交易信息
     */
    public TransactionDto getTransactionStatus(String transactionId) {
        String url = thunesConfig.getApiBasePath() + ThunesConstants.ENDPOINT_TRANSACTIONS + "/" + transactionId;

        log.info("Thunes getTransactionStatus 请求: {}", url);

        HttpRequest httpRequest = HttpRequest.get(url)
                .header(ThunesConstants.HEADER_AUTHORIZATION,
                        ThunesConstants.AUTH_TYPE_BASIC + " " + thunesConfig.getBasicAuthString())
                .header(ThunesConstants.HEADER_CONTENT_TYPE, ThunesConstants.CONTENT_TYPE_JSON)
                .timeout(thunesConfig.getTimeout());

        try (HttpResponse response = httpRequest.execute()) {
            log.info("Thunes getTransactionStatus 响应状态: {}", response.getStatus());

            if (!response.isOk()) {
                String errorMsg = "查询交易状态失败: " + response.getStatus() + " - " + response.body();
                log.error(errorMsg);
                throw new CustomException(errorMsg);
            }

            String responseBody = response.body();
            log.info("Thunes getTransactionStatus 响应: {}", responseBody);

            return JSONUtil.toBean(responseBody, TransactionDto.class);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error("调用 Thunes getTransactionStatus API 异常", e);
            throw new CustomException("调用 Thunes API 失败: " + e.getMessage());
        }
    }

    /**
     * 验证收款方账户状态
     *
     * @param payerId         支付方ID
     * @param transactionType 交易类型
     * @param request         验证请求
     * @return 验证结果
     */
    public CreditPartyVerificationResponseDto verifyCreditParty(Long payerId, String transactionType,
            CreditPartyVerificationRequestDto request) {
        String url = thunesConfig.getApiBasePath() + ThunesConstants.ENDPOINT_PAYERS + "/" + payerId +
                "/" + transactionType + ThunesConstants.ENDPOINT_CREDIT_PARTY_VERIFICATION;

        String requestBody = JSONUtil.toJsonStr(request);
        log.info("Thunes verifyCreditParty 请求: {}", url);

        HttpRequest httpRequest = HttpRequest.post(url)
                .header(ThunesConstants.HEADER_AUTHORIZATION,
                        ThunesConstants.AUTH_TYPE_BASIC + " " + thunesConfig.getBasicAuthString())
                .header(ThunesConstants.HEADER_CONTENT_TYPE, ThunesConstants.CONTENT_TYPE_JSON)
                .body(requestBody)
                .timeout(getSafeTimeout());

        try (HttpResponse response = httpRequest.execute()) {
            log.info("Thunes verifyCreditParty 响应状态: {}", response.getStatus());

            if (!response.isOk()) {
                String responseBody = response.body();
                log.warn("收款方验证失败: {} - {}", response.getStatus(), responseBody);

                // 验证失败不抛异常，返回失败结果
                CreditPartyVerificationResponseDto failedResponse = new CreditPartyVerificationResponseDto();
                failedResponse.setAccount_status("VERIFICATION_FAILED");
                failedResponse.setMessage("验证请求失败: " + responseBody);
                return failedResponse;
            }

            String responseBody = response.body();
            log.info("Thunes verifyCreditParty 响应: {}", responseBody);

            return JSONUtil.toBean(responseBody, CreditPartyVerificationResponseDto.class);
        } catch (Exception e) {
            log.error("调用 Thunes verifyCreditParty API 异常", e);

            // 异常情况返回失败结果，不中断主流程
            CreditPartyVerificationResponseDto failedResponse = new CreditPartyVerificationResponseDto();
            failedResponse.setAccount_status("VERIFICATION_ERROR");
            failedResponse.setMessage("验证服务异常: " + e.getMessage());
            return failedResponse;
        }
    }
}
