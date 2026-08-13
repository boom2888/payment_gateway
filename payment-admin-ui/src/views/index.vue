<template>
  <div class="app-container">
    <!-- 顶部统计卡片数据 -->
    <div class="statistics">
      <el-row :gutter="20">
        <!-- 今日订单总数 -->
        <el-col :span="6">
          <el-card shadow="hover" class="metric-card">
            <div class="metric-content">
              <div class="metric-icon">
                <i class="el-icon-s-order" />
              </div>
              <div class="metric-info">
                <div class="metric-title">{{ $t('dashboard.todaySuccessOrders') }}</div>
                <div class="metric-value">
                  <count-to :start-val="0" :end-val="todayOrderCount" :duration="2000" class="metric-number" />
                </div>
              </div>
            </div>
          </el-card>
        </el-col>

        <!-- 今日交易总额 -->
        <el-col :span="6">
          <el-card shadow="hover" class="metric-card">
            <div class="metric-content">
              <div class="metric-icon">
                <i class="el-icon-wallet" />
              </div>
              <div class="metric-info">
                <div class="metric-title">{{ $t('dashboard.todaySuccessAmount') }}</div>
                <div class="metric-value">
                  <span class="metric-prefix">$</span>
                  <count-to
                    :start-val="0"
                    :end-val="parseFloat(todayTotalAmount)"
                    :duration="2000"
                    :decimals="2"
                    class="metric-number"
                  />
                </div>
              </div>
            </div>
          </el-card>
        </el-col>

        <!-- 昨日交易总额 -->
        <el-col :span="6">
          <el-card shadow="hover" class="metric-card">
            <div class="metric-content">
              <div class="metric-icon">
                <i class="el-icon-money" />
              </div>
              <div class="metric-info">
                <div class="metric-title">{{ $t('dashboard.yesterdaySuccessAmount') }}</div>
                <div class="metric-value">
                  <span class="metric-prefix">$</span>
                  <count-to
                    :start-val="0"
                    :end-val="parseFloat(yesterdayTotalAmount)"
                    :duration="2000"
                    :decimals="2"
                    class="metric-number"
                  />
                </div>
              </div>
            </div>
          </el-card>
        </el-col>

        <!-- 近7天交易总额 -->
        <el-col :span="6">
          <el-card shadow="hover" class="metric-card">
            <div class="metric-content">
              <div class="metric-icon">
                <i class="el-icon-data-analysis" />
              </div>
              <div class="metric-info">
                <div class="metric-title">{{ $t('dashboard.last7DaysSuccessAmount') }}</div>
                <div class="metric-value">
                  <span class="metric-prefix">$</span>
                  <count-to
                    :start-val="0"
                    :end-val="parseFloat(last7DaysTotalAmount)"
                    :duration="2000"
                    :decimals="2"
                    class="metric-number"
                  />
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 商户交易数据排名,商户交易额排名,商户进入不需要展示出来  -->
    <div v-if="checkPermi(['payment:index:merchantName'])" class="ranking-section">
      <el-row :gutter="20">
        <!-- 商户交易数排名 -->
        <el-col :span="12">
          <el-card v-loading="transactionCountLoading" shadow="hover" class="ranking-card">
            <div slot="header" class="ranking-header">
              <span class="ranking-title">
                <i class="el-icon-data-line ranking-icon" />
                {{ $t('dashboard.merchantTransactionCountRanking') }}
              </span>
              <div class="ranking-controls">
                <el-date-picker
                  v-model="transactionCountDateRange"
                  type="daterange"
                  size="small"
                  range-separator="~"
                  :start-placeholder="$t('dashboard.startDate')"
                  :end-placeholder="$t('dashboard.endDate')"
                  style="width: 250px; margin-right: 10px"
                  :picker-options="pickerOptions"
                  @input="handleTransactionCountDateChange"
                />
                <!-- <el-link type="primary" :underline="false">更多>></el-link> -->
              </div>
            </div>
            <div class="ranking-content">
              <div v-if="!transactionCountLoading && transactionCountRanking.length === 0" class="no-data">
                <i class="el-icon-info" />
                <span>{{ $t('dashboard.noData') }}</span>
              </div>
              <div v-for="(item, index) in transactionCountRanking" :key="index" class="ranking-item">
                <div class="ranking-value">
                  <count-to
                    :start-val="0"
                    :end-val="item.value"
                    :duration="1500"
                    :delay="index * 200"
                    class="ranking-count"
                  />
                </div>
                <div class="ranking-name">{{ item.name }}</div>
                <div class="ranking-position">
                  {{ $t('dashboard.rankingPosition', { position: index + 1 }) }}
                </div>
              </div>
            </div>
          </el-card>
        </el-col>

        <!-- 商户交易额排名 -->
        <el-col :span="12">
          <el-card v-loading="transactionAmountLoading" shadow="hover" class="ranking-card">
            <div slot="header" class="ranking-header">
              <span class="ranking-title">
                <i class="el-icon-money ranking-icon" />
                {{ $t('dashboard.merchantTransactionAmountRankingUSD') }}
              </span>
              <div class="ranking-controls">
                <el-date-picker
                  v-model="transactionAmountDateRange"
                  type="daterange"
                  size="small"
                  range-separator="~"
                  :start-placeholder="$t('dashboard.startDate')"
                  :end-placeholder="$t('dashboard.endDate')"
                  style="width: 250px; margin-right: 10px"
                  :picker-options="pickerOptions"
                  @input="handleTransactionAmountDateChange"
                />
                <!-- <el-link type="primary" :underline="false">更多>></el-link> -->
              </div>
            </div>
            <div class="ranking-content">
              <div v-if="!transactionAmountLoading && transactionAmountRanking.length === 0" class="no-data">
                <i class="el-icon-info" />
                <span>{{ $t('dashboard.noData') }}</span>
              </div>
              <div v-for="(item, index) in transactionAmountRanking" :key="index" class="ranking-item">
                <div class="ranking-value">
                  <count-to
                    :start-val="0"
                    :end-val="parseFloat(item.value)"
                    :duration="1500"
                    :delay="index * 200"
                    :decimals="2"
                    class="ranking-count"
                  />
                </div>
                <div class="ranking-name">{{ item.name }}</div>
                <div class="ranking-position">
                  {{ $t('dashboard.rankingPosition', { position: index + 1 }) }}
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 数据操作区域,通过时间范围选择,商户选择,来获取数据从而控制下面echarts -->
    <!-- 占位元素，当吸顶时保持布局 -->
    <div v-if="isSticky" class="chart-controls-placeholder" />

    <div ref="chartControls" class="chart-controls" :class="{ 'is-sticky': isSticky }">
      <el-card class="chart-card" :body-style="{ padding: '0' }" shadow="hover">
        <div slot="header" class="chart-controls-header">
          <div class="chart-controls-title">
            <i class="el-icon-setting" />
            <span>{{ $t('dashboard.chartDataFilter') }}</span>
          </div>
        </div>

        <div class="chart-controls-content">
          <div class="controls-left">
            <!-- 这是个图形数据条件查询得条件 -->
            <!-- 时间范围选择 -->
            <div class="control-item">
              <label class="control-label">{{ $t('dashboard.timeRange') }}：</label>
              <el-date-picker
                v-model="globalDateRange"
                type="daterange"
                size="small"
                range-separator="~"
                :start-placeholder="$t('dashboard.startDate')"
                :end-placeholder="$t('dashboard.endDate')"
                style="width: 280px"
                :picker-options="pickerOptions"
                @input="handleGlobalDateChange"
              />
            </div>

            <!-- 商户选择 -->
            <div v-if="checkPermi(['payment:index:merchantName'])" class="control-item">
              <label class="control-label">{{ $t('dashboard.merchant') }}：</label>
              <el-select
                v-model="selectedMerchant"
                :placeholder="$t('dashboard.selectMerchant')"
                clearable
                size="small"
                filterable
                @change="handleMerchantChange"
              >
                <el-option
                  v-for="dict in merchantIdOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="dict.dictValue"
                />
              </el-select>
            </div>
          </div>

          <div class="controls-right">
            <!-- <el-button type="primary" size="small" @click="refreshOrderStatusData" icon="el-icon-refresh">
              刷新数据
            </el-button> -->
            <span class="chart-controls-info">
              <i class="el-icon-info" />
              <span>{{ getDataRangeInfo() }}</span>
            </span>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 每小时订单成功率统计 -->
    <div class="chart-section">
      <HourlySuccessRateChart
        :chart-height="400"
        :auto-refresh="true"
        :refresh-interval="300"
        :start-date="globalDateRange && globalDateRange.length === 2 ? formatDate(globalDateRange[0]) : null"
        :end-date="globalDateRange && globalDateRange.length === 2 ? formatDate(globalDateRange[1]) : null"
        :merchant-id="selectedMerchant"
      />
    </div>

    <!-- 本月订单数量,本周订单数量 -->
    <div class="chart-section">
      <OrderTrendChart
        :chart-height="400"
        :auto-refresh="true"
        :refresh-interval="5"
        :monthly-order-count="monthlyOrderCount"
        :monthly-trend="monthlyTrend"
        :weekly-order-count="weeklyOrderCount"
        :weekly-trend="weeklyTrend"
        :completed-order-data="completedOrderData"
        :cancelled-order-data="cancelledOrderData"
        :refunded-order-data="refundedOrderData"
        :chargeback-order-data="chargebackOrderData"
        :chargeback-cancelled-order-data="chargebackCancelledOrderData"
        :dispute-order-data="disputeOrderData"
        :processing-order-data="processingOrderData"
        :failed-order-data="failedOrderData"
      />
    </div>

    <!-- 交易订单总额统计 -->
    <div class="chart-section">
      <OrderTurnover
        :chart-height="400"
        :auto-refresh="true"
        :refresh-interval="5"
        :monthly-order-amount="monthlyOrderAmount"
        :monthly-trend="monthlyAmountTrend"
        :weekly-order-amount="weeklyOrderAmount"
        :weekly-trend="weeklyAmountTrend"
        :completed-order-amount-data="completedOrderAmountData"
        :cancelled-order-amount-data="cancelledOrderAmountData"
        :refunded-order-amount-data="refundedOrderAmountData"
        :chargeback-order-amount-data="chargebackOrderAmountData"
        :chargeback-cancelled-order-amount-data="chargebackCancelledOrderAmountData"
        :dispute-order-amount-data="disputeOrderAmountData"
        :processing-order-amount-data="processingOrderAmountData"
        :failed-order-amount-data="failedOrderAmountData"
      />
    </div>

    <!-- 订单均价统计 -->
    <div class="chart-section">
      <OrderAverage
        :chart-height="400"
        :auto-refresh="true"
        :refresh-interval="5"
        :monthly-avg-price="monthlyAvgPrice"
        :weekly-avg-price="weeklyAvgPrice"
        :monthly-trend="monthlyAvgTrend"
        :weekly-trend="weeklyAvgTrend"
        :average-price-data="averagePriceData"
      />
    </div>



    <!-- 退款率/拒付率/取消率 和 支付方式 -->
    <div class="chart-section">
      <el-row :gutter="20" class="equal-height-row">
        <!-- 退款率/拒付率/取消率 -->
        <el-col :span="12">
          <RefundRateChart
            :chart-height="425"
            :auto-refresh="true"
            :refresh-interval="30"
            :refund-rate-data="refundRateData"
            :chargeback-rate-data="chargebackRateData"
            :cancel-rate-data="cancelRateData"
            :success-rate-data="successRateData"
            @refresh="refreshRefundRateData"
          />
        </el-col>

        <!-- 支付方式 -->
        <el-col :span="12">
          <PaymentMethodChart
            :chart-height="350"
            :auto-refresh="true"
            :refresh-interval="30"
            :payment-method-data="paymentMethodData"
            @refresh="refreshPaymentMethodData"
          />
        </el-col>
      </el-row>

      <!-- 失败原因分析 -->
      <el-row :gutter="20" style="margin-top: 20px;">
        <el-col :span="12">
          <FailReasonChart
            :chart-height="350"
            :auto-refresh="true"
            :refresh-interval="30"
            :fail-reason-data="failReasonData"
            @refresh="refreshFailReasonData"
          />
        </el-col>
        <el-col :span="12">
          <!-- 预留位置，可以添加其他图表 -->
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import {
  getMerchantRankingApi,
  getOrderAverageDataApi,
  getOrderTrendDataApi,
  getOrderTurnoverDataApi,
  getPaymentMethodDataApi,
  getRefundRateDataApi,
  getTopMetricsDataApi,
  getFailReasonStatsApi
} from '@/api/payment/homeStatistics'
import { checkPermi } from '@/utils/permission'
import CountTo from 'vue-count-to'
import OrderAverage from './components/OrderAverage'
import OrderTrendChart from './components/OrderTrendChart'
import OrderTurnover from './components/OrderTurnover'
import PaymentMethodChart from './components/PaymentMethodChart'
import FailReasonChart from './components/FailReasonChart'
import RefundRateChart from './components/RefundRateChart'
import HourlySuccessRateChart from './components/HourlySuccessRateChart'

export default {
  dicts: ['project_type'],
  components: {
    OrderTrendChart,
    OrderTurnover,
    OrderAverage,
    RefundRateChart,
    PaymentMethodChart,
    HourlySuccessRateChart,
    FailReasonChart,
    CountTo
  },
  data() {
    return {
      // 吸顶状态
      isSticky: false, // 筛选条件是否处于吸顶状态
      stickyOffsetTop: 0, // 筛选条件原始位置距离顶部的距离
      scrollTimer: null, // 滚动事件节流定时器
      // 顶部统计卡片数据
      todayOrderCount: 0, // 今日订单总数
      todayTotalAmount: '0.00', // 今日交易总额
      yesterdayTotalAmount: '0.00', // 昨日交易总额
      last7DaysTotalAmount: '0.00', // 近7天交易总额

      // 商户交易数排名数据
      transactionCountPeriod: 'last7days',
      transactionCountDateRange: [],
      transactionCountRanking: [],
      transactionCountLoading: false,

      // 商户交易额排名数据
      transactionAmountPeriod: 'last7days',
      transactionAmountDateRange: [],
      transactionAmountRanking: [],
      transactionAmountLoading: false,

      // 全局数据筛选条件
      globalDateRange: [], // 全局时间范围
      selectedMerchant: null, // 选中的商户ID
      merchantIdOptions: [], // 商户列表

      // 1========================================================================= 订单数量统计
      monthlyOrderCount: 0, // 本月订单数量
      weeklyOrderCount: 0, // 本周订单数量
      monthlyTrend: null, // 本月趋势,同比上涨还是下跌
      weeklyTrend: null, // 本周趋势,同比上涨还是下跌
      orderStatsLoading: false,
      completedOrderData: {
        // 已完成订单
        dates: [],
        values: []
      },
      cancelledOrderData: {
        // 已取消订单
        dates: [],
        values: []
      },
      refundedOrderData: {
        // 已退款订单
        dates: [],
        values: []
      },
      chargebackOrderData: {
        // 拒付订单
        dates: [],
        values: []
      },
      chargebackCancelledOrderData: {
        // 拒付取消订单
        dates: [],
        values: []
      },
      disputeOrderData: {
        // 争议订单
        dates: [],
        values: []
      },
      processingOrderData: {
        // 处理中订单
        dates: [],
        values: []
      },
      failedOrderData: {
        // 已失败订单
        dates: [],
        values: []
      },

      // 2===============================================================================订单金额统计数据
      monthlyOrderAmount: 0, // 本月订单总金额
      weeklyOrderAmount: 0, // 本周订单总金额
      monthlyAmountTrend: null, // 本月趋势,同比上涨还是下跌
      weeklyAmountTrend: null, // 本周趋势,同比上涨还是下跌
      completedOrderAmountData: {
        dates: [],
        values: []
      },
      cancelledOrderAmountData: {
        dates: [],
        values: []
      },
      refundedOrderAmountData: {
        dates: [],
        values: []
      },
      chargebackOrderAmountData: {
        dates: [],
        values: []
      },
      chargebackCancelledOrderAmountData: {
        dates: [],
        values: []
      },
      disputeOrderAmountData: {
        dates: [],
        values: []
      },
      // 新增两条统计线分别是:处理中金额,已失败金额
      processingOrderAmountData: {
        dates: [],
        values: []
      },
      failedOrderAmountData: {
        dates: [],
        values: []
      },

      // 3==============================================================================订单均价相关数据
      monthlyAvgPrice: 0, // 本月订单均价
      weeklyAvgPrice: 0, // 本周订单均价
      monthlyAvgTrend: null, // 本月均价趋势
      weeklyAvgTrend: null, // 本周均价趋势
      averagePriceData: {
        dates: [],
        values: []
      },
      refundRateData: {
        dates: [],
        values: []
      },
      chargebackRateData: {
        dates: [],
        values: []
      },
      cancelRateData: {
        dates: [],
        values: []
      },
      successRateData: {
        dates: [],
        values: []
      },

      // 4===================================================================================================支付方式数据..
      paymentMethodData: [],

      // 5===================================================================================================失败原因数据..
      failReasonData: []
    }
  },
  computed: {
    // 默认支付方式数据（使用国际化）
    defaultPaymentMethodData() {
      return [
        {
          name: this.$t('payment.dashboard.cardPayment'),
          value: 45.5,
          children: [
            { name: 'Visa', value: 28.5 },
            { name: 'Master', value: 17.0 }
          ]
        },
        { name: this.$t('payment.dashboard.googlePay'), value: 28.3 },
        { name: this.$t('payment.dashboard.applePay'), value: 26.2 }
      ]
    },

    // 日期选择器快捷选项配置
    pickerOptions() {
      return {
        shortcuts: [
          {
            text: this.$t('payment.dashboard.lastDay'),
            onClick(picker) {
              const end = new Date()
              const start = new Date()
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 1)
              picker.$emit('pick', [start, end])
            }
          },
          {
            text: this.$t('payment.dashboard.last3Days'),
            onClick(picker) {
              const end = new Date()
              const start = new Date()
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 3)
              picker.$emit('pick', [start, end])
            }
          },
          {
            text: this.$t('payment.dashboard.lastWeek'),
            onClick(picker) {
              const end = new Date()
              const start = new Date()
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
              picker.$emit('pick', [start, end])
            }
          },
          {
            text: this.$t('payment.dashboard.lastMonth'),
            onClick(picker) {
              const end = new Date()
              const start = new Date()
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
              picker.$emit('pick', [start, end])
            }
          },
          {
            text: this.$t('payment.dashboard.last3Months'),
            onClick(picker) {
              const end = new Date()
              const start = new Date()
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 90)
              picker.$emit('pick', [start, end])
            }
          }
        ]
      }
    }
  },
  created() {
    // 如果有商户相关权限，加载商户列表和排名数据
    if (this.checkPermi(['payment:index:merchantName'])) {
      this.getDicts('MERCHANT_ID').then(response => {
        this.merchantIdOptions = response.data
      })

      // 加载商户排名数据
      this.initRankingDateRanges()
      this.loadMerchantRankingData()
    }

    // 加载顶部统计数据
    this.loadTopMetricsData()

    // 使用统一的API方法加载所有图表数据
    this.initDefaultDateRange()
    this.loadAllChartsData()
  },
  mounted() {
    // 添加滚动监听
    this.initStickyEffect()
    // 初始化支付方式数据
    this.paymentMethodData = this.defaultPaymentMethodData
  },
  beforeDestroy() {
    // 移除滚动监听
    this.destroyStickyEffect()
  },

  methods: {
    checkPermi,
    // 初始化吸顶效果
    initStickyEffect() {
      this.$nextTick(() => {
        // 获取筛选条件元素的初始位置
        const chartControlsEl = this.$refs.chartControls
        if (chartControlsEl) {
          // 计算元素距离页面顶部的距离
          this.stickyOffsetTop = chartControlsEl.offsetTop
          // 添加滚动监听
          window.addEventListener('scroll', this.handleScroll)
          // 初始化检查一次
          this.handleScroll()
        }
      })
    },

    // 销毁吸顶效果
    destroyStickyEffect() {
      window.removeEventListener('scroll', this.handleScroll)
      // 清理定时器
      if (this.scrollTimer) {
        cancelAnimationFrame(this.scrollTimer)
        this.scrollTimer = null
      }
    },

    // 处理滚动事件（响应式优化）
    handleScroll() {
      // 使用 requestAnimationFrame 优化性能
      if (this.scrollTimer) return
      this.scrollTimer = requestAnimationFrame(() => {
        const scrollTop = window.pageYOffset || document.documentElement.scrollTop
        const wasSticky = this.isSticky

        // 当滚动距离超过筛选条件原始位置时，激活吸顶效果
        this.isSticky = scrollTop >= this.stickyOffsetTop - 10 // 减去10px作为缓冲

        // 状态变化时显示消息
        if (wasSticky !== this.isSticky) {
          // 动态调整left位置，适应不同的侧边栏状态
          if (this.isSticky) {
            this.$nextTick(() => {
              const sidebarEl = document.querySelector('.sidebar-container')
              const chartControlsEl = this.$refs.chartControls
              if (sidebarEl && chartControlsEl) {
                const sidebarWidth = sidebarEl.offsetWidth
                chartControlsEl.style.left = sidebarWidth + 'px'
              }
            })
          }
        }

        this.scrollTimer = null
      })
    },

    // 初始化默认时间范围（最近7天）
    initDefaultDateRange() {
      const end = new Date()
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
      this.globalDateRange = [start, end]
    },

    // 初始化排名数据的日期范围（最近7天）
    initRankingDateRanges() {
      const end = new Date()
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 7) // 最近7天

      // 设置交易数排名日期范围
      this.transactionCountDateRange = [start, end]
      // 设置交易额排名日期范围
      this.transactionAmountDateRange = [start, end]
    },

    // 处理全局时间范围变化
    handleGlobalDateChange(dateRange) {
      console.log('全局时间范围变化:', dateRange)
      if (dateRange && dateRange.length === 2) {
        // 重新加载所有图表数据
        this.loadAllChartsData()
      }
    },

    // 处理商户选择变化
    handleMerchantChange(merchantId) {
      console.log('商户选择变化:', merchantId)
      // 重新加载所有图表数据
      if (this.globalDateRange && this.globalDateRange.length === 2) {
        this.loadAllChartsData()
      }
    },

    // 处理交易数排名日期选择变化
    handleTransactionCountDateChange(dateRange) {
      console.log('交易数排名日期范围变化:', dateRange)
      if (dateRange && dateRange.length === 2) {
        // 重新加载商户排名数据
        this.loadMerchantRankingData()
      }
    },

    // 处理交易额排名日期选择变化
    handleTransactionAmountDateChange(dateRange) {
      console.log('交易额排名日期范围变化:', dateRange)
      if (dateRange && dateRange.length === 2) {
        // 重新加载商户排名数据
        this.loadMerchantRankingData()
      }
    },

    // 获取数据范围信息
    getDataRangeInfo() {
      if (this.checkPermi(['payment:index:merchantName']) && this.globalDateRange && this.globalDateRange.length === 2) {
        const startDate = this.formatDate(this.globalDateRange[0])
        const endDate = this.formatDate(this.globalDateRange[1])
        const merchantName = this.selectedMerchant
          ? this.merchantIdOptions.find(m => m.dictValue === this.selectedMerchant)?.dictLabel ||
            this.$t('dashboard.unknownMerchant')
          : this.$t('dashboard.allMerchants')
        return this.$t('dashboard.dataRangeInfo', { startDate, endDate, merchantName })
      }
      return this.$t('dashboard.defaultDataInfo')
    },

    // 加载顶部统计数据
    async loadTopMetricsData() {
      try {
        console.log('加载顶部统计数据...')
        const response = await getTopMetricsDataApi()
        if (response && response.data) {
          this.todayOrderCount = response.data.todayOrderCount ? response.data.todayOrderCount : 0
          this.todayTotalAmount = response.data.todayTotalAmount ? response.data.todayTotalAmount.toFixed(2) : '0.00'
          this.yesterdayTotalAmount = response.data.yesterdayTotalAmount
            ? response.data.yesterdayTotalAmount.toFixed(2)
            : '0.00'
          this.last7DaysTotalAmount = response.data.last7DaysTotalAmount
            ? response.data.last7DaysTotalAmount.toFixed(2)
            : '0.00'
          console.log('顶部统计数据加载成功:', response.data)
        }
      } catch (error) {
        console.error('加载顶部统计数据失败:', error)
        this.$message.error(this.$t('dashboard.topMetricsLoadFailed'))
      }
    },

    // 加载商户排名数据
    async loadMerchantRankingData() {
      try {
        console.log('加载商户排名数据...')

        // 开始加载
        this.transactionCountLoading = true
        this.transactionAmountLoading = true

        const params = {
          transactionCountStartDate: this.transactionCountDateRange[0]
            ? this.formatDate(this.transactionCountDateRange[0])
            : null,
          transactionCountEndDate: this.transactionCountDateRange[1]
            ? this.formatDate(this.transactionCountDateRange[1])
            : null,
          transactionAmountStartDate: this.transactionAmountDateRange[0]
            ? this.formatDate(this.transactionAmountDateRange[0])
            : null,
          transactionAmountEndDate: this.transactionAmountDateRange[1]
            ? this.formatDate(this.transactionAmountDateRange[1])
            : null
        }

        const response = await getMerchantRankingApi(params)
        console.log('商户排名API返回数据:', response)
        if (response && response.data) {
          // 更新交易数排名数据
          this.transactionCountRanking = response.data.transactionCountRanking || []
          // 更新交易额排名数据
          this.transactionAmountRanking = response.data.transactionAmountRanking || []
          console.log('商户排名数据加载成功')
        }
      } catch (error) {
        console.error('加载商户排名数据失败:', error)
        this.$message.error(this.$t('dashboard.merchantRankingLoadFailed'))
      } finally {
        // 结束加载
        this.transactionCountLoading = false
        this.transactionAmountLoading = false
      }
    },

    // 加载所有图表数据（根据筛选条件）,并行加载所有图表数据
    async loadAllChartsData() {
      try {
        const promises = [
          this.loadOrderTrendChartData(), // 1. 订单数量趋势图表数据
          this.loadOrderTurnoverChartData(), // 2. 订单交易额图表数据
          this.loadOrderAverageChartData(), // 3. 订单均价图表数据
          this.loadRefundRateChartData(), // 4. 退款率/拒付率/取消率图表数据
          this.loadPaymentMethodChartData(), // 5. 支付方式图表数据
          this.loadFailReasonChartData() // 6. 失败原因图表数据
        ]

        // 等待所有图表数据加载完成
        await Promise.all(promises)

        console.log('所有图表数据加载完成')
        this.$message.success(this.$t('dashboard.dataUpdated'))
      } catch (error) {
        console.error('加载图表数据失败:', error)
        this.$message.error(this.$t('dashboard.chartDataLoadFailed'))
      }
    },

    // 构建通用API请求参数
    buildChartApiParams() {
      const params = {
        startDate: this.globalDateRange[0] ? this.formatDate(this.globalDateRange[0]) : null,
        endDate: this.globalDateRange[1] ? this.formatDate(this.globalDateRange[1]) : null
      }
      if (this.selectedMerchant) {
        params.merchantId = this.selectedMerchant
      }
      return params
    },

    // 1. 加载订单趋势图表数据
    async loadOrderTrendChartData() {
      try {
        const params = this.buildChartApiParams()
        console.log('加载订单趋势数据，筛选参数:', params)
        const response = await getOrderTrendDataApi(params)
        console.log('订单趋势API返回数据:', response.data)
        if (response && response.data) {
          const apiData = response.data
          // 本月/本周订单数量
          this.monthlyOrderCount = apiData.monthly.orderCount
          this.weeklyOrderCount = apiData.weekly.orderCount
          // 本月/本周订单数量趋势
          this.monthlyTrend = apiData.monthly.trend
          this.weeklyTrend = apiData.weekly.trend
          // 订单状态趋势数据（数量维度）
          if (apiData.statusTrend) {
            const trend = apiData.statusTrend
            this.completedOrderData = { dates: trend.dates, values: trend.completed }
            this.cancelledOrderData = { dates: trend.dates, values: trend.cancelled }
            this.refundedOrderData = { dates: trend.dates, values: trend.refunded }
            this.chargebackOrderData = { dates: trend.dates, values: trend.chargeback }
            this.chargebackCancelledOrderData = { dates: trend.dates, values: trend.chargebackCancelled }
            this.disputeOrderData = { dates: trend.dates, values: trend.dispute }
            this.processingOrderData = { dates: trend.dates, values: trend.processing }
            this.failedOrderData = { dates: trend.dates, values: trend.failed }
          }
        }
      } catch (error) {
        console.error('加载订单趋势数据失败:', error)
        throw error
      }
    },

    // 2. 加载订单交易额图表数据
    async loadOrderTurnoverChartData() {
      try {
        const params = this.buildChartApiParams()
        console.log('加载订单交易额数据，筛选参数:', params)
        const response = await getOrderTurnoverDataApi(params)
        console.log('订单交易额API返回数据:', response.data)
        if (response && response.data) {
          const apiData = response.data
          // 本月/本周订单金额
          this.monthlyOrderAmount = apiData.monthly.orderAmount
          this.weeklyOrderAmount = apiData.weekly.orderAmount
          // 本月/本周订单金额趋势
          this.monthlyAmountTrend = apiData.monthly.trend
          this.weeklyAmountTrend = apiData.weekly.trend
          // 订单金额趋势数据（金额维度）
          if (apiData.amountTrend) {
            const amountTrend = apiData.amountTrend
            this.completedOrderAmountData = { dates: amountTrend.dates, values: amountTrend.completed }
            this.cancelledOrderAmountData = { dates: amountTrend.dates, values: amountTrend.cancelled }
            this.refundedOrderAmountData = { dates: amountTrend.dates, values: amountTrend.refunded }
            this.chargebackOrderAmountData = { dates: amountTrend.dates, values: amountTrend.chargeback }
            this.chargebackCancelledOrderAmountData = {
              dates: amountTrend.dates,
              values: amountTrend.chargebackCancelled
            }
            this.disputeOrderAmountData = { dates: amountTrend.dates, values: amountTrend.dispute }
            this.processingOrderAmountData = { dates: amountTrend.dates, values: amountTrend.processing }
            this.failedOrderAmountData = { dates: amountTrend.dates, values: amountTrend.failed }
          }
          console.log('订单交易额数据加载成功')
        }
      } catch (error) {
        console.error('加载订单交易额数据失败:', error)
        throw error
      }
    },

    // 3. 加载订单均价图表数据
    async loadOrderAverageChartData() {
      try {
        const params = this.buildChartApiParams()
        console.log('加载订单均价数据，筛选参数:', params)
        const response = await getOrderAverageDataApi(params)
        console.log('订单均价API返回数据:', response.data)
        if (response && response.data) {
          const apiData = response.data
          // 本月/本周订单均价
          this.monthlyAvgPrice = apiData.monthly.avgPrice
          this.weeklyAvgPrice = apiData.weekly.avgPrice
          // 本月/本周订单均价趋势
          this.monthlyAvgTrend = apiData.monthly.trend
          this.weeklyAvgTrend = apiData.weekly.trend
          // 订单均价历史趋势数据
          if (apiData.priceTrend) {
            this.averagePriceData = {
              dates: apiData.priceTrend.dates,
              values: apiData.priceTrend.values
            }
          }
          console.log('订单均价数据加载成功')
        }
      } catch (error) {
        console.error('加载订单均价数据失败:', error)
        throw error
      }
    },

    // 4. 加载退款率/拒付率/取消率图表数据
    async loadRefundRateChartData() {
      try {
        const params = this.buildChartApiParams()
        console.log('加载退款率数据，筛选参数:', params)
        const response = await getRefundRateDataApi(params)

        if (response && response.data) {
          console.log('退款率API返回数据:', response.data)
          if (response.data) {
            const rates = response.data
            this.refundRateData = { dates: rates.dates, values: rates.refundRate }
            this.chargebackRateData = { dates: rates.dates, values: rates.chargebackRate }
            this.cancelRateData = { dates: rates.dates, values: rates.cancelRate }
            this.successRateData = { dates: rates.dates, values: rates.successRate }
          }
          console.log('退款率数据加载成功')
        }
      } catch (error) {
        console.error('加载退款率数据失败:', error)
        throw error
      }
    },

    // 5. 加载支付方式图表数据
    async loadPaymentMethodChartData() {
      try {
        const params = this.buildChartApiParams()
        console.log('加载支付方式数据，筛选参数:', params)
        const response = await getPaymentMethodDataApi(params)
        console.log('支付方式API返回数据:', response.data)
        if (response && response.data) {
          const apiData = response.data
          if (apiData && apiData.methods) {
            // 将后端返回的 key 映射为国际化文本
            this.paymentMethodData = apiData.methods.map(method => {
              const mappedMethod = { ...method }
              // 映射支付方式名称
              if (method.name === 'cardPay') {
                mappedMethod.name = this.$t('payment.dashboard.cardPayment')
              } else if (method.name === 'googlePay') {
                mappedMethod.name = this.$t('payment.dashboard.googlePay')
              } else if (method.name === 'applePay') {
                mappedMethod.name = this.$t('payment.dashboard.applePay')
              }
              return mappedMethod
            })
          }
          console.log('支付方式数据加载成功，映射后:', this.paymentMethodData)
        }
      } catch (error) {
        console.error('加载支付方式数据失败:', error)
        throw error
      }
    },

    // 6. 加载失败原因图表数据
    async loadFailReasonChartData() {
      try {
        const params = this.buildChartApiParams()
        console.log('加载失败原因数据，筛选参数:', params)
        const response = await getFailReasonStatsApi(params)
        console.log('失败原因API返回数据:', response.data)
        if (response && response.data) {
          const apiData = response.data
          if (apiData && apiData.failReasons) {
            this.failReasonData = apiData.failReasons
          }
          console.log('失败原因数据加载成功')
        }
      } catch (error) {
        console.error('加载失败原因数据失败:', error)
        this.$message.error(this.$t('payment.dashboard.loadFailReasonDataFailed'))
        throw error
      }
    },

    // API调用方法 - 1. 获取订单趋势数据
    async getOrderTrendData(params) {
      // TODO: 替换为实际的API调用
      // return await request({
      //   url: '/api/dashboard/order-trend',
      //   method: 'get',
      //   params: params
      // });

      // 临时返回模拟的API数据结构
      return {
        code: 200,
        message: '成功',
        data: this.getMockOrderTrendResponse(params)
      }
    },

    // API调用方法 - 2. 获取订单交易额数据
    async getOrderTurnoverData(params) {
      // TODO: 替换为实际的API调用
      // return await request({
      //   url: '/api/dashboard/order-turnover',
      //   method: 'get',
      //   params: params
      // });

      // 临时返回模拟的API数据结构
      return {
        code: 200,
        message: '成功',
        data: this.getMockOrderTurnoverResponse(params)
      }
    },

    // API调用方法 - 3. 获取订单均价数据
    async getOrderAverageData(params) {
      // TODO: 替换为实际的API调用
      // return await request({
      //   url: '/api/dashboard/order-average',
      //   method: 'get',
      //   params: params
      // });

      // 临时返回模拟的API数据结构
      return {
        code: 200,
        message: '成功',
        data: this.getMockOrderAverageResponse(params)
      }
    },

    // API调用方法 - 4. 获取退款率数据
    async getRefundRateData(params) {
      // TODO: 替换为实际的API调用
      // return await request({
      //   url: '/api/dashboard/refund-rate',
      //   method: 'get',
      //   params: params
      // });

      // 临时返回模拟的API数据结构
      return {
        code: 200,
        message: '成功',
        data: this.getMockRefundRateResponse(params)
      }
    },

    // API调用方法 - 5. 获取支付方式数据
    async getPaymentMethodData(params) {
      // TODO: 替换为实际的API调用
      // return await request({
      //   url: '/api/dashboard/payment-method',
      //   method: 'get',
      //   params: params
      // });

      // 临时返回模拟的API数据结构
      return {
        code: 200,
        message: '成功',
        data: this.getMockPaymentMethodResponse(params)
      }
    },

    // 数据分配方法 - 1. 分配订单趋势数据
    assignOrderTrendData(apiData) {
      try {
        console.log('分配订单趋势数据:', apiData)
        if (apiData.orderCountStats) {
          // 本月/本周订单数量
          this.monthlyOrderCount = apiData.orderCountStats.monthly.orderCount
          this.weeklyOrderCount = apiData.orderCountStats.weekly.orderCount
          // 本月/本周订单数量趋势
          this.monthlyTrend = apiData.orderCountStats.monthly.trend
          this.weeklyTrend = apiData.orderCountStats.weekly.trend

          // 订单状态趋势数据（数量维度）
          if (apiData.orderCountStats.statusTrend) {
            const trend = apiData.orderCountStats.statusTrend
            this.completedOrderData = { dates: trend.dates, values: trend.completed }
            this.cancelledOrderData = { dates: trend.dates, values: trend.cancelled }
            this.refundedOrderData = { dates: trend.dates, values: trend.refunded }
            this.chargebackOrderData = { dates: trend.dates, values: trend.chargeback }
            this.chargebackCancelledOrderData = { dates: trend.dates, values: trend.chargebackCancelled }
            this.disputeOrderData = { dates: trend.dates, values: trend.dispute }
          }
        }
      } catch (error) {
        console.error('分配订单趋势数据时出错:', error)
        throw error
      }
    },

    // 模拟API响应数据结构 - 1. 订单趋势数据（用于开发测试，实际使用时删除）
    getMockOrderTrendResponse(params) {
      return {
        orderCountStats: {
          // 本月/本周订单数量和趋势
          monthly: {
            orderCount: Math.floor((Math.random() * 5000 + 10000) * this.getMerchantMultiplier()),
            trend: Math.round((Math.random() - 0.5) * 40 * 10) / 10
          },
          weekly: {
            orderCount: Math.floor((Math.random() * 1500 + 2000) * this.getMerchantMultiplier()),
            trend: Math.round((Math.random() - 0.5) * 60 * 10) / 10
          },
          // 订单状态趋势数据（数量维度）
          statusTrend: {
            dates: this.generateDateRange(30),
            completed: this.generateTrendData(30, 800, 1500, 'increasing'),
            cancelled: this.generateTrendData(30, 100, 350, 'stable'),
            refunded: this.generateTrendData(30, 50, 200, 'decreasing'),
            chargeback: this.generateTrendData(30, 15, 100, 'random'),
            chargebackCancelled: this.generateTrendData(30, 5, 50, 'stable'),
            dispute: this.generateTrendData(30, 2, 30, 'random')
          }
        }
      }
    },

    // 模拟API响应数据结构 - 2. 订单交易额数据（用于开发测试，实际使用时删除）
    getMockOrderTurnoverResponse(params) {
      return {
        orderAmountStats: {
          // 本月/本周订单金额和趋势
          monthly: {
            orderAmount: Math.floor((Math.random() * 500000 + 1000000) * this.getMerchantMultiplier()),
            trend: Math.round((Math.random() - 0.5) * 40 * 10) / 10
          },
          weekly: {
            orderAmount: Math.floor((Math.random() * 150000 + 200000) * this.getMerchantMultiplier()),
            trend: Math.round((Math.random() - 0.5) * 60 * 10) / 10
          },
          // 订单金额趋势数据（金额维度）
          amountTrend: {
            dates: this.generateDateRange(30),
            completed: this.generateAmountTrendData(30, 10000, 50000, 'increasing'),
            cancelled: this.generateAmountTrendData(30, 1000, 8000, 'stable'),
            refunded: this.generateAmountTrendData(30, 500, 5000, 'decreasing'),
            chargeback: this.generateAmountTrendData(30, 200, 3000, 'random'),
            chargebackCancelled: this.generateAmountTrendData(30, 100, 1500, 'stable'),
            dispute: this.generateAmountTrendData(30, 50, 800, 'random')
          }
        }
      }
    },

    // 模拟API响应数据结构 - 3. 订单均价数据（用于开发测试，实际使用时删除）
    getMockOrderAverageResponse(params) {
      return {
        orderAvgPriceStats: {
          // 本月/本周订单均价和趋势
          monthly: {
            avgPrice: Math.floor(Math.random() * 100) + 80,
            trend: Math.round((Math.random() - 0.5) * 30 * 10) / 10
          },
          weekly: {
            avgPrice: Math.floor(Math.random() * 100) + 75,
            trend: Math.round((Math.random() - 0.5) * 40 * 10) / 10
          },
          // 订单均价历史数据
          priceTrend: {
            dates: this.generateDateRange(30),
            values: this.generateAveragePriceTrendData(30, 60, 200)
          }
        }
      }
    },

    // 模拟API响应数据结构 - 4. 退款率数据（用于开发测试，实际使用时删除）
    getMockRefundRateResponse(params) {
      return {
        refundRateStats: {
          dates: this.generateDateRange(30),
          refundRate: this.generateRateTrendData(30, 1, 4),
          chargebackRate: this.generateRateTrendData(30, 0.5, 2.5),
          cancelRate: this.generateRateTrendData(30, 3, 8),
          successRate: this.generateRateTrendData(30, 3, 4)
        }
      }
    },

    // 模拟API响应数据结构 - 5. 支付方式数据（用于开发测试，实际使用时删除）
    getMockPaymentMethodResponse(params) {
      return {
        paymentMethodStats: {
          methods: this.generatePaymentMethodsData()
        }
      }
    },

    // 获取商户数据倍数（根据选中的商户）
    getMerchantMultiplier() {
      if (this.selectedMerchant === 'all') return 1
      const merchantIndex = this.merchantIdOptions.findIndex(m => m.dictValue === this.selectedMerchant)
      return 0.6 + merchantIndex * 0.15 // 0.6 - 1.2之间
    },

    // 格式化日期为 YYYY-MM-DD 格式
    formatDate(date) {
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      return `${year}-${month}-${day}`
    },

    // 生成过去N天的日期数组（根据筛选条件）
    generateDateRange(days = 7) {
      const dates = []
      let endDate, startDate

      // 如果有全局时间范围，使用全局时间范围
      if (this.globalDateRange && this.globalDateRange.length === 2) {
        endDate = new Date(this.globalDateRange[1])
        startDate = new Date(this.globalDateRange[0])

        // 计算天数差
        const timeDiff = endDate.getTime() - startDate.getTime()
        days = Math.ceil(timeDiff / (1000 * 3600 * 24)) + 1
      } else {
        // 默认使用过去N天
        endDate = new Date()
        startDate = new Date()
        startDate.setDate(startDate.getDate() - days + 1)
      }

      for (let i = 0; i < days; i++) {
        const date = new Date(startDate)
        date.setDate(date.getDate() + i)
        dates.push(this.formatChartDate(date))
      }
      return dates
    },

    // 格式化图表显示的日期格式
    formatChartDate(date) {
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      return `${month}-${day}`
    },

    // 生成指定范围内的随机数组
    generateRandomData(length, min, max, trendType = 'random') {
      const data = []
      let baseValue = Math.floor((min + max) / 2)

      for (let i = 0; i < length; i++) {
        let value

        switch (trendType) {
          case 'increasing':
            // 递增趋势，有些波动
            baseValue += Math.random() * (max - min) * 0.1
            value = Math.floor(baseValue + (Math.random() - 0.5) * (max - min) * 0.3)
            break
          case 'decreasing':
            // 递减趋势，有些波动
            baseValue -= Math.random() * (max - min) * 0.1
            value = Math.floor(baseValue + (Math.random() - 0.5) * (max - min) * 0.3)
            break
          case 'stable':
            // 相对稳定，小幅波动
            value = Math.floor(baseValue + (Math.random() - 0.5) * (max - min) * 0.2)
            break
          default:
            // 随机波动
            value = Math.floor(Math.random() * (max - min + 1)) + min
        }

        // 确保值在范围内
        value = Math.max(min, Math.min(max, value))
        data.push(value)
      }

      return data
    },

    // 刷新退款率数据
    refreshRefundRateData() {
      // 只重新加载退款率图表数据
      this.loadRefundRateChartData()
    },

    // 刷新支付方式数据
    refreshPaymentMethodData() {
      // 只重新加载支付方式图表数据
      this.loadPaymentMethodChartData()
    },

    // 刷新失败原因数据
    refreshFailReasonData() {
      // 只重新加载失败原因图表数据
      this.loadFailReasonChartData()
    },

    // 简化的数据生成方法（用于API模拟）
    generateTrendData(length, min, max, trendType = 'random') {
      const data = []
      let baseValue = Math.floor((min + max) / 2)

      for (let i = 0; i < length; i++) {
        let value
        switch (trendType) {
          case 'increasing':
            baseValue += Math.random() * (max - min) * 0.1
            value = Math.floor(baseValue + (Math.random() - 0.5) * (max - min) * 0.3)
            break
          case 'decreasing':
            baseValue -= Math.random() * (max - min) * 0.1
            value = Math.floor(baseValue + (Math.random() - 0.5) * (max - min) * 0.3)
            break
          case 'stable':
            value = Math.floor(baseValue + (Math.random() - 0.5) * (max - min) * 0.2)
            break
          default:
            value = Math.floor(Math.random() * (max - min + 1)) + min
        }
        value = Math.max(min, Math.min(max, value))
        data.push(value)
      }
      return data
    },

    // 生成金额趋势数据
    generateAmountTrendData(length, min, max, trendType = 'random') {
      const data = []
      let baseValue = (min + max) / 2

      for (let i = 0; i < length; i++) {
        let value
        switch (trendType) {
          case 'increasing':
            baseValue += Math.random() * (max - min) * 0.1
            value = baseValue + (Math.random() - 0.5) * (max - min) * 0.3
            break
          case 'decreasing':
            baseValue -= Math.random() * (max - min) * 0.1
            value = baseValue + (Math.random() - 0.5) * (max - min) * 0.3
            break
          case 'stable':
            value = baseValue + (Math.random() - 0.5) * (max - min) * 0.2
            break
          default:
            value = Math.random() * (max - min) + min
        }
        value = Math.max(min, Math.min(max, value))
        data.push(parseFloat(value.toFixed(2)))
      }
      return data
    },

    // 生成均价趋势数据
    generateAveragePriceTrendData(length, min, max) {
      const data = []
      let basePrice = Math.floor((min + max) / 2)

      for (let i = 0; i < length; i++) {
        const fluctuation = (Math.random() - 0.5) * 20
        basePrice += fluctuation * 0.4
        basePrice = Math.max(min, Math.min(max, basePrice))
        data.push(parseFloat(basePrice.toFixed(2)))
      }
      return data
    },

    // 生成比率趋势数据
    generateRateTrendData(length, min, max) {
      const data = []
      let baseRate = (min + max) / 2

      for (let i = 0; i < length; i++) {
        const fluctuation = (Math.random() - 0.5) * (max - min) * 0.3
        baseRate += fluctuation * 0.1
        baseRate = Math.max(min, Math.min(max, baseRate))
        data.push(parseFloat(baseRate.toFixed(2)))
      }
      return data
    },

    // 生成支付方式数据
    generatePaymentMethodsData() {
      const cardPaymentText = this.$t('payment.dashboard.cardPayment')
      const googlePayText = this.$t('payment.dashboard.googlePay')
      const applePayText = this.$t('payment.dashboard.applePay')

      const baseRatios = {
        [cardPaymentText]: 0.4 + Math.random() * 0.2,
        [googlePayText]: 0.2 + Math.random() * 0.15,
        [applePayText]: 0.15 + Math.random() * 0.15
      }

      const total = Object.values(baseRatios).reduce((sum, val) => sum + val, 0)
      Object.keys(baseRatios).forEach(key => {
        baseRatios[key] = (baseRatios[key] / total) * 100
      })

      const cardSubRatios = {
        Visa: 0.55 + Math.random() * 0.2,
        Master: 0.25 + Math.random() * 0.2
      }

      const cardTotal = Object.values(cardSubRatios).reduce((sum, val) => sum + val, 0)
      Object.keys(cardSubRatios).forEach(key => {
        cardSubRatios[key] = (cardSubRatios[key] / cardTotal) * baseRatios[cardPaymentText]
      })

      return [
        {
          name: cardPaymentText,
          value: parseFloat(baseRatios[cardPaymentText].toFixed(1)),
          children: [
            { name: 'Visa', value: parseFloat(cardSubRatios['Visa'].toFixed(1)) },
            { name: 'Master', value: parseFloat(cardSubRatios['Master'].toFixed(1)) }
          ]
        },
        { name: googlePayText, value: parseFloat(baseRatios[googlePayText].toFixed(1)) },
        { name: applePayText, value: parseFloat(baseRatios[applePayText].toFixed(1)) }
        // { name: '其他', value: parseFloat(baseRatios['其他'].toFixed(1)) }
      ]
    }
  }
}
</script>
<style lang="scss" scoped>
.app-container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: 100vh;
}

.metric-card {
  border: none;
  border-radius: 12px;
  transition: all 0.3s ease;
  background: white;

  &:hover {
    transform: translateY(-5px);
    box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
  }
}

.metric-content {
  display: flex;
  align-items: center;
  padding: 20px;
  min-height: 120px;
}

.metric-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  background: linear-gradient(135deg, #20bf6b, #0fb9b1);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20px;
  flex-shrink: 0;

  i {
    font-size: 28px;
    color: white;
  }
}

.metric-info {
  flex: 1;
  min-width: 0;
}

.metric-title {
  font-size: 14px;
  color: #909399;
  margin-bottom: 12px;
  font-weight: 500;
  line-height: 1.4;
}

.metric-value {
  display: flex;
  align-items: baseline;
  flex-wrap: wrap;
}

.metric-prefix {
  font-size: 16px;
  color: #20bf6b;
  font-weight: 600;
  margin-right: 4px;
}

.metric-number {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
  line-height: 1.2;
}

// 排名卡片样式
.ranking-section {
  margin-top: 30px;
}

// 图表区域样式
.chart-section {
  margin-top: 30px;
}

// 确保同一行的组件高度一致
.equal-height-row {
  .el-col {
    display: flex;

    > * {
      width: 100%;
    }
  }
}

// 图表控制区域样式
.chart-controls {
  margin-top: 20px;
  margin-bottom: 20px;

  // 吸顶状态
  &.is-sticky {
    position: fixed;
    top: 0;
    left: 0; // 默认值，JavaScript会动态设置正确的值
    right: 0;
    z-index: 1000;
    margin-top: 0;
    margin-bottom: 0;
    animation: stickSlideDown 0.3s ease-out;

    .chart-card {
      border-radius: 0;
      box-shadow: 0 2px 12px rgba(0, 0, 0, 0.15);
      background: rgba(255, 255, 255, 0.95);
      backdrop-filter: blur(10px);
    }

    // 在吸顶状态下调整内边距，确保内容适应全宽
    .chart-controls-content {
      max-width: none; // 允许全宽
      padding-left: 40px;
      padding-right: 40px;
    }

    .chart-controls-header {
      padding-left: 40px;
      padding-right: 40px;
    }
  }
}

// 占位元素样式，保持布局不变
.chart-controls-placeholder {
  height: 110px; // 根据实际筛选条件高度调整
  margin-top: 20px;
  margin-bottom: 20px;
}

// 吸顶动画效果
@keyframes stickSlideDown {
  from {
    transform: translateY(-100%);
    opacity: 0;
  }

  to {
    transform: translateY(0);
    opacity: 1;
  }
}

// Chart Card 样式
.chart-card {
  border-radius: 12px;
  transition: all 0.3s ease;
}

.chart-controls-header {
  padding: 0;
  border-bottom: none;
  margin-bottom: 0;
}

.chart-controls-title {
  display: flex;
  align-items: center;
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 0;

  i {
    margin-right: 8px;
    font-size: 18px;
    color: #409eff;
  }
}

.chart-controls-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  flex-wrap: wrap;
  gap: 16px;
}

.controls-left {
  display: flex;
  align-items: center;
  gap: 24px;
  flex-wrap: wrap;
}

.controls-right {
  display: flex;
  align-items: center;
  gap: 16px;
}

.control-item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.control-label {
  font-size: 14px;
  color: #606266;
  font-weight: 500;
  white-space: nowrap;
  margin: 0;
}

.chart-controls-info {
  display: flex;
  align-items: center;
  color: #909399;
  font-size: 14px;

  i {
    margin-right: 6px;
    font-size: 16px;
  }

  span {
    white-space: nowrap;
  }
}

// 订单统计区域样式
.order-stats-section {
  margin-bottom: 30px;
}

.order-stat-card {
  border: none;
  border-radius: 12px;
  transition: all 0.3s ease;
  background: white;
  height: 100%;

  &:hover {
    transform: translateY(-3px);
    box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
  }
}

.order-stat-content {
  display: flex;
  align-items: center;
  padding: 24px;
  min-height: 140px;
}

.order-stat-icon {
  width: 70px;
  height: 70px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 24px;
  flex-shrink: 0;

  &.monthly {
    background: linear-gradient(135deg, #667eea, #764ba2);
  }

  &.weekly {
    background: linear-gradient(135deg, #f093fb, #f5576c);
  }

  i {
    font-size: 32px;
    color: white;
  }
}

.order-stat-info {
  flex: 1;
  min-width: 0;
}

.order-stat-title {
  font-size: 16px;
  color: #909399;
  margin-bottom: 12px;
  font-weight: 500;
  line-height: 1.4;
}

.order-stat-value {
  display: flex;
  align-items: baseline;
  margin-bottom: 12px;
}

.order-stat-number {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
  line-height: 1.2;
  margin-right: 8px;
}

.order-stat-unit {
  font-size: 16px;
  color: #606266;
  font-weight: 500;
}

.order-stat-trend {
  display: flex;
  align-items: center;
  font-size: 14px;

  .trend-up {
    color: #67c23a;
  }

  .trend-down {
    color: #f56c6c;
  }

  .trend-text {
    color: #909399;
    margin-left: 8px;
  }

  i {
    font-size: 12px;
    margin-right: 4px;
  }
}

.ranking-card {
  border: none;
  border-radius: 12px;
  transition: all 0.3s ease;
  background: white;
  height: 100%;

  &:hover {
    transform: translateY(-3px);
    box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
  }
}

.ranking-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0;
}

.ranking-title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  display: flex;
  align-items: center;
}

.ranking-icon {
  margin-right: 8px;
  font-size: 18px;
  color: #409eff;
}

.ranking-controls {
  display: flex;
  align-items: center;
}

.ranking-content {
  display: flex;
  justify-content: space-between;
  padding: 20px 0;
  min-height: 120px;
}

.ranking-item {
  flex: 1;
  text-align: center;
  padding: 0 10px;
  position: relative;

  &:not(:last-child)::after {
    content: '';
    position: absolute;
    right: 0;
    top: 50%;
    transform: translateY(-50%);
    width: 1px;
    height: 60%;
    background-color: #e4e7ed;
  }
}

.ranking-value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 8px;
  line-height: 1.2;
}

.ranking-count {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
  line-height: 1.2;
}

.ranking-name {
  font-size: 18px;
  color: #606266;
  margin-bottom: 8px;
  line-height: 1.4;
}

.ranking-position {
  font-size: 18px;
  color: #909399;
  line-height: 1.4;
}

.ranking-number {
  color: #f56c6c;
  font-weight: bold;
  font-size: 14px;
}

// 响应式设计
@media (max-width: 1200px) {
  .el-col {
    margin-bottom: 20px;
  }

  .metric-content {
    padding: 16px;
    min-height: 100px;
  }

  .metric-icon {
    width: 50px;
    height: 50px;
    margin-right: 16px;

    i {
      font-size: 24px;
    }
  }

  .metric-number {
    font-size: 20px;
  }

  // 排名卡片响应式
  .ranking-content {
    padding: 16px 0;
    min-height: 100px;
  }

  .ranking-value {
    font-size: 20px;
  }

  .ranking-controls {
    flex-wrap: wrap;
    gap: 8px;
  }
}

@media (max-width: 768px) {
  .app-container {
    padding: 15px;
  }

  // 图表控制区域响应式
  .chart-controls {
    margin-top: 15px;
    margin-bottom: 15px;

    // 移动端吸顶状态调整
    &.is-sticky {
      left: 0; // 移动端不需要为侧边栏留空间

      .chart-card {
        border-radius: 0;
      }

      .chart-controls-content {
        max-width: none;
        padding: 16px;
      }

      .chart-controls-header {
        max-width: none;
        padding: 0 16px;
      }
    }
  }

  // 移动端占位元素高度调整
  .chart-controls-placeholder {
    height: 140px; // 移动端高度稍高一些
  }

  .chart-controls-header {
    padding: 0;
  }

  .chart-controls-title {
    font-size: 15px;
    margin-bottom: 0;
  }

  .chart-controls-content {
    flex-direction: column;
    align-items: stretch;
    gap: 16px;
    padding: 16px;
  }

  .controls-left {
    flex-direction: column;
    align-items: stretch;
    gap: 16px;
  }

  .controls-right {
    justify-content: space-between;
    flex-wrap: wrap;
  }

  .control-item {
    flex-direction: column;
    align-items: stretch;
    gap: 8px;
  }

  .control-label {
    font-size: 13px;
  }

  .metric-content {
    flex-direction: column;
    text-align: center;
    padding: 20px 16px;
  }

  .metric-icon {
    margin-right: 0;
    margin-bottom: 16px;
  }

  // 订单统计卡片移动端响应式
  .order-stats-section {
    margin-bottom: 20px;
  }

  .order-stat-content {
    padding: 20px;
    min-height: 120px;
  }

  .order-stat-icon {
    width: 60px;
    height: 60px;
    margin-right: 20px;

    i {
      font-size: 28px;
    }
  }

  .order-stat-number {
    font-size: 24px;
  }

  // 排名卡片移动端响应式
  .ranking-section {
    margin-top: 20px;
  }

  .ranking-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }

  .ranking-controls {
    width: 100%;
    justify-content: space-between;
  }

  .ranking-content {
    flex-direction: column;
    gap: 20px;
    padding: 16px 0;
  }

  .ranking-item {
    &:not(:last-child)::after {
      display: none;
    }
  }
}

// 空数据状态样式
.no-data {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 40px 20px;
  color: #909399;
  font-size: 14px;
  min-height: 120px;

  i {
    font-size: 48px;
    margin-bottom: 12px;
    color: #c0c4cc;
  }

  span {
    font-size: 14px;
    color: #909399;
  }
}
</style>
