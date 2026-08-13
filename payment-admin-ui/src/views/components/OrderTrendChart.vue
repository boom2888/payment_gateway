<!-- 订单交易数量统计 -->
<template>
  <div class="order-trend-chart">
    <el-card shadow="hover" class="chart-card">
      <div>
        <div slot="header" class="chart-header">
          <span class="chart-title">
            <i class="el-icon-data-line chart-title-icon" />
            {{ $t('payment.dashboard.orderTrendChartTitle') }}
          </span>
        </div>

        <!-- 订单统计 -->
        <div class="order-stats-section">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-card shadow="hover" class="order-stat-card">
                <div class="order-stat-content">
                  <div class="order-stat-icon monthly">
                    <i class="el-icon-date" />
                  </div>
                  <div class="order-stat-info">
                    <div class="order-stat-title">{{ $t('payment.dashboard.monthlyOrderCount') }}</div>
                    <div class="order-stat-value">
                      <span class="order-stat-number">{{ monthlyOrderCount }}</span>
                      <span class="order-stat-unit">{{ $t('payment.dashboard.orderUnit') }}</span>
                    </div>
                    <div v-if="monthlyTrend" class="order-stat-trend">
                      <i :class="monthlyTrend > 0 ? 'el-icon-arrow-up trend-up' : 'el-icon-arrow-down trend-down'" />
                      <span :class="monthlyTrend > 0 ? 'trend-up' : 'trend-down'">{{ Math.abs(monthlyTrend) }}%</span>
                      <span class="trend-text">{{ $t('payment.dashboard.compareWithLastMonth') }}</span>
                    </div>
                  </div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="12">
              <el-card shadow="hover" class="order-stat-card">
                <div class="order-stat-content">
                  <div class="order-stat-icon weekly">
                    <i class="el-icon-time" />
                  </div>
                  <div class="order-stat-info">
                    <div class="order-stat-title">{{ $t('payment.dashboard.weeklyOrderCount') }}</div>
                    <div class="order-stat-value">
                      <span class="order-stat-number">{{ weeklyOrderCount }}</span>
                      <span class="order-stat-unit">{{ $t('payment.dashboard.orderUnit') }}</span>
                    </div>
                    <div v-if="weeklyTrend" class="order-stat-trend">
                      <i :class="weeklyTrend > 0 ? 'el-icon-arrow-up trend-up' : 'el-icon-arrow-down trend-down'" />
                      <span :class="weeklyTrend > 0 ? 'trend-up' : 'trend-down'">{{ Math.abs(weeklyTrend) }}%</span>
                      <span class="trend-text">{{ $t('payment.dashboard.compareWithLastWeek') }}</span>
                    </div>
                  </div>
                </div>
              </el-card>
            </el-col>
          </el-row>
        </div>

        <div class="chart-content">
          <div ref="chartContainer" class="chart-container" :style="{ height: chartHeight + 'px' }" />
          <div v-if="loading" class="chart-loading">
            <i class="el-icon-loading" />
            <span>{{ $t('payment.dashboard.dataLoading') }}</span>
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'OrderTrendChart',
  props: {
    // 图表高度
    chartHeight: {
      type: Number,
      default: 400
    },
    // 本月订单数量
    monthlyOrderCount: {
      type: Number,
      default: 10
    },
    // 本周订单数量
    weeklyOrderCount: {
      type: Number,
      default: 20
    },
    // 本月趋势（百分比，正数为上升，负数为下降）
    monthlyTrend: {
      type: Number,
      default: null
    },
    // 本周趋势（百分比，正数为上升，负数为下降）
    weeklyTrend: {
      type: Number,
      default: null
    },
    // 已完成订单数据
    completedOrderData: {
      type: Object,
      default: () => ({
        dates: [],
        values: []
      })
    },
    // 已取消订单数据
    cancelledOrderData: {
      type: Object,
      default: () => ({
        dates: [],
        values: []
      })
    },
    // 已退款订单数据
    refundedOrderData: {
      type: Object,
      default: () => ({
        dates: [],
        values: []
      })
    },
    // 拒付订单数据
    chargebackOrderData: {
      type: Object,
      default: () => ({
        dates: [],
        values: []
      })
    },
    // 拒付取消订单数据
    chargebackCancelledOrderData: {
      type: Object,
      default: () => ({
        dates: [],
        values: []
      })
    },
    // 争议交易数据
    disputeOrderData: {
      type: Object,
      default: () => ({
        dates: [],
        values: []
      })
    },
    // 处理中订单数据
    processingOrderData: {
      type: Object,
      default: () => ({
        dates: [],
        values: []
      })
    },
    // 已失败订单数据
    failedOrderData: {
      type: Object,
      default: () => ({
        dates: [],
        values: []
      })
    }
  },
  data() {
    return {
      chart: null,
      loading: false
    }
  },

  watch: {
    // 监听props数据变化，自动更新图表
    completedOrderData: {
      handler() {
        console.log(1)
        this.updateChart()
      },
      deep: true
    },
    cancelledOrderData: {
      handler() {
        this.updateChart()
      },
      deep: true
    },
    refundedOrderData: {
      handler() {
        this.updateChart()
      },
      deep: true
    },
    chargebackOrderData: {
      handler() {
        this.updateChart()
      },
      deep: true
    },
    chargebackCancelledOrderData: {
      handler() {
        this.updateChart()
      },
      deep: true
    },
    disputeOrderData: {
      handler() {
        this.updateChart()
      },
      deep: true
    },
    processingOrderData: {
      handler() {
        this.updateChart()
      },
      deep: true
    },
    failedOrderData: {
      handler() {
        this.updateChart()
      },
      deep: true
    }
  },
  mounted() {
    // 延迟初始化，确保DOM渲染完成
    this.$nextTick(() => {
      this.initChart()
      // 再延迟一下更新数据，确保props传入
      setTimeout(() => {
        this.updateChart()
      }, 100)
    })

    // 窗口大小变化时重新绘制图表
    window.addEventListener('resize', this.handleResize)
  },
  beforeDestroy() {
    if (this.chart) {
      this.chart.dispose()
    }
    window.removeEventListener('resize', this.handleResize)
  },
  methods: {
    // 初始化图表
    initChart() {
      this.chart = echarts.init(this.$refs.chartContainer)

      const option = {
        title: {
          show: false
        },
        tooltip: {
          trigger: 'axis',
          backgroundColor: 'rgba(255, 255, 255, 0.95)',
          borderColor: '#e4e7ed',
          borderWidth: 1,
          textStyle: {
            color: '#303133'
          },
          formatter: params => {
            let html = `<div style="padding: 8px;">`
            html += `<div style="margin-bottom: 8px; font-weight: bold;">${params[0].axisValue}</div>`

            // 计算总订单数（已完成订单 + 已失败订单 + 处理中订单）
            let completedCount = 0
            let failedCount = 0
            let processingCount = 0

            const completedText = this.$t('payment.dashboard.completedOrders')
            const failedText = this.$t('payment.dashboard.failedOrders')
            const processingText = this.$t('payment.dashboard.processingOrders')

            params.forEach(item => {
              if (item.seriesName === completedText) {
                completedCount = item.value
              } else if (item.seriesName === failedText) {
                failedCount = item.value
              } else if (item.seriesName === processingText) {
                processingCount = item.value
              }
            })

            const totalOrders = completedCount + failedCount + processingCount

            // 显示总订单数（如果有已完成或已失败订单的数据）
            if (totalOrders > 0) {
              html += `
                <div style="display: flex; align-items: center; margin-bottom: 8px; padding: 4px 0; border-bottom: 1px solid #e4e7ed;">
                  <span style="display: inline-block; width: 10px; height: 10px; background-color: #409EFF; border-radius: 50%; margin-right: 8px;"></span>
                  <span style="font-weight: bold; color: #409EFF;">${this.$t('payment.dashboard.totalOrderCount')}：${totalOrders}${this.$t('payment.dashboard.orderUnit')}</span>
                  <span style="font-size: 12px; color: #909399; margin-left: 8px;">${this.$t('payment.dashboard.orderCountSummary')}</span>
                </div>
              `
            }

            params.forEach(item => {
              html += `
                <div style="display: flex; align-items: center; margin-bottom: 4px;">
                  <span style="display: inline-block; width: 10px; height: 10px; background-color: ${item.color}; border-radius: 50%; margin-right: 8px;"></span>
                  <span>${item.seriesName}：${item.value}${this.$t('payment.dashboard.orderUnit')}</span>
                </div>
              `
            })
            html += `</div>`
            return html
          }
        },
        legend: {
          show: true,
          top: '5%',
          right: '5%',
          textStyle: {
            color: '#606266',
            fontSize: 12
          },
          itemWidth: 14,
          itemHeight: 14,
          itemGap: 20
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '15%',
          top: '15%',
          containLabel: true
        },
        dataZoom: [
          {
            type: 'slider',
            show: true,
            xAxisIndex: [0],
            bottom: '3%',
            height: 20,
            start: 0,
            end: 100,
            handleStyle: {
              color: '#409eff',
              borderColor: '#409eff'
            },
            textStyle: {
              color: '#606266'
            },
            borderColor: '#e4e7ed',
            fillerColor: 'rgba(64, 158, 255, 0.2)',
            dataBackground: {
              lineStyle: {
                color: '#ddd'
              },
              areaStyle: {
                color: '#f5f5f5'
              }
            }
          },
          {
            type: 'inside',
            xAxisIndex: [0],
            start: 0,
            end: 100,
            zoomLock: true
          }
        ],
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: [],
          axisLine: {
            lineStyle: {
              color: '#e4e7ed'
            }
          },
          axisLabel: {
            color: '#606266',
            fontSize: 12
          },
          axisTick: {
            show: false
          }
        },
        yAxis: {
          type: 'value',
          axisLine: {
            show: false
          },
          axisLabel: {
            color: '#606266',
            fontSize: 12,
            formatter: '{value}'
          },
          axisTick: {
            show: false
          },
          splitLine: {
            lineStyle: {
              color: '#f5f7fa',
              type: 'dashed'
            }
          }
        },
        series: [
          {
            name: this.$t('payment.dashboard.completedOrders'),
            type: 'line',
            data: [],
            smooth: true,
            symbol: 'circle',
            symbolSize: 6,
            lineStyle: {
              color: '#67C23A',
              width: 3
            },
            itemStyle: {
              color: '#67C23A',
              borderColor: '#fff',
              borderWidth: 2
            },
            emphasis: {
              focus: 'series',
              itemStyle: {
                color: '#67C23A',
                borderColor: '#fff',
                borderWidth: 3,
                shadowBlur: 10,
                shadowColor: 'rgba(103, 194, 58, 0.5)'
              }
            }
          },
          {
            name: this.$t('payment.dashboard.cancelledOrders'),
            type: 'line',
            data: [],
            smooth: true,
            symbol: 'circle',
            symbolSize: 6,
            lineStyle: {
              color: '#F56C6C',
              width: 3
            },
            itemStyle: {
              color: '#F56C6C',
              borderColor: '#fff',
              borderWidth: 2
            },
            emphasis: {
              focus: 'series',
              itemStyle: {
                color: '#F56C6C',
                borderColor: '#fff',
                borderWidth: 3,
                shadowBlur: 10,
                shadowColor: 'rgba(245, 108, 108, 0.5)'
              }
            }
          },
          {
            name: this.$t('payment.dashboard.refundedOrders'),
            type: 'line',
            data: [],
            smooth: true,
            symbol: 'circle',
            symbolSize: 6,
            lineStyle: {
              color: '#E6A23C',
              width: 3
            },
            itemStyle: {
              color: '#E6A23C',
              borderColor: '#fff',
              borderWidth: 2
            },
            emphasis: {
              focus: 'series',
              itemStyle: {
                color: '#E6A23C',
                borderColor: '#fff',
                borderWidth: 3,
                shadowBlur: 10,
                shadowColor: 'rgba(230, 162, 60, 0.5)'
              }
            }
          },
          {
            name: this.$t('payment.dashboard.chargebackOrders'),
            type: 'line',
            data: [],
            smooth: true,
            symbol: 'circle',
            symbolSize: 6,
            lineStyle: {
              color: '#909399',
              width: 3
            },
            itemStyle: {
              color: '#909399',
              borderColor: '#fff',
              borderWidth: 2
            },
            emphasis: {
              focus: 'series',
              itemStyle: {
                color: '#909399',
                borderColor: '#fff',
                borderWidth: 3,
                shadowBlur: 10,
                shadowColor: 'rgba(144, 147, 153, 0.5)'
              }
            }
          },
          {
            name: this.$t('payment.dashboard.chargebackCancelledOrders'),
            type: 'line',
            data: [],
            smooth: true,
            symbol: 'circle',
            symbolSize: 6,
            lineStyle: {
              color: '#409EFF',
              width: 3
            },
            itemStyle: {
              color: '#409EFF',
              borderColor: '#fff',
              borderWidth: 2
            },
            emphasis: {
              focus: 'series',
              itemStyle: {
                color: '#409EFF',
                borderColor: '#fff',
                borderWidth: 3,
                shadowBlur: 10,
                shadowColor: 'rgba(64, 158, 255, 0.5)'
              }
            }
          },
          {
            name: this.$t('payment.dashboard.disputeTransactions'),
            type: 'line',
            data: [],
            smooth: true,
            symbol: 'circle',
            symbolSize: 6,
            lineStyle: {
              color: '#C45656',
              width: 3
            },
            itemStyle: {
              color: '#C45656',
              borderColor: '#fff',
              borderWidth: 2
            },
            emphasis: {
              focus: 'series',
              itemStyle: {
                color: '#C45656',
                borderColor: '#fff',
                borderWidth: 3,
                shadowBlur: 10,
                shadowColor: 'rgba(196, 86, 86, 0.5)'
              }
            }
          },
          {
            name: this.$t('payment.dashboard.processingOrders'),
            type: 'line',
            data: [],
            smooth: true,
            symbol: 'circle',
            symbolSize: 6,
            lineStyle: {
              color: '#F56C6C',
              width: 3
            },
            itemStyle: {
              color: '#F56C6C',
              borderColor: '#fff',
              borderWidth: 2
            },
            emphasis: {
              focus: 'series',
              itemStyle: {
                color: '#F56C6C',
                borderColor: '#fff',
                borderWidth: 3,
                shadowBlur: 10,
                shadowColor: 'rgba(245, 108, 108, 0.5)'
              }
            }
          },
          {
            name: this.$t('payment.dashboard.failedOrders'),
            type: 'line',
            data: [],
            smooth: true,
            symbol: 'circle',
            symbolSize: 6,
            lineStyle: {
              color: '#909399',
              width: 3
            },
            itemStyle: {
              color: '#909399',
              borderColor: '#fff',
              borderWidth: 2
            },
            emphasis: {
              focus: 'series',
              itemStyle: {
                color: '#909399',
                borderColor: '#fff',
                borderWidth: 3,
                shadowBlur: 10,
                shadowColor: 'rgba(144, 147, 153, 0.5)'
              }
            }
          }
        ],
        animation: true,
        animationDuration: 1000,
        animationEasing: 'cubicOut'
      }

      this.chart.setOption(option)
    },

    // 更新图表数据
    updateChart() {
      if (!this.chart) return

      // 合并所有数据的日期，确保X轴的一致性
      let allDates = []
      if (this.completedOrderData.dates.length > 0) {
        allDates = [...this.completedOrderData.dates]
      } else if (this.cancelledOrderData.dates.length > 0) {
        allDates = [...this.cancelledOrderData.dates]
      } else if (this.refundedOrderData.dates.length > 0) {
        allDates = [...this.refundedOrderData.dates]
      } else if (this.chargebackOrderData.dates.length > 0) {
        allDates = [...this.chargebackOrderData.dates]
      } else if (this.chargebackCancelledOrderData.dates.length > 0) {
        allDates = [...this.chargebackCancelledOrderData.dates]
      } else if (this.disputeOrderData.dates.length > 0) {
        allDates = [...this.disputeOrderData.dates]
      } else if (this.processingOrderData.dates.length > 0) {
        allDates = [...this.processingOrderData.dates]
      } else if (this.failedOrderData.dates.length > 0) {
        allDates = [...this.failedOrderData.dates]
      } else {
        // 如果没有传入数据，使用模拟数据
        allDates = this.generateMockDates()
      }

      // 计算 dataZoom 的显示范围，如果数据超过30天，只显示最近30天
      const { start, end } = this.calculateDataZoomRange(allDates)

      const option = {
        dataZoom: [
          {
            start: start,
            end: end
          },
          {
            start: start,
            end: end
          }
        ],
        xAxis: {
          data: allDates
        },
        series: [
          {
            data: this.completedOrderData.values.length > 0 ? this.completedOrderData.values : []
          },
          {
            data: this.cancelledOrderData.values.length > 0 ? this.cancelledOrderData.values : []
          },
          {
            data: this.refundedOrderData.values.length > 0 ? this.refundedOrderData.values : []
          },
          {
            data: this.chargebackOrderData.values.length > 0 ? this.chargebackOrderData.values : []
          },
          {
            data: this.chargebackCancelledOrderData.values.length > 0 ? this.chargebackCancelledOrderData.values : []
          },
          {
            data: this.disputeOrderData.values.length > 0 ? this.disputeOrderData.values : []
          },
          {
            data: this.processingOrderData.values.length > 0 ? this.processingOrderData.values : []
          },
          {
            data: this.failedOrderData.values.length > 0 ? this.failedOrderData.values : []
          }
        ]
      }

      this.chart.setOption(option)
    },

    // 生成模拟日期数据
    generateMockDates() {
      const dates = []
      const today = new Date()
      for (let i = 6; i >= 0; i--) {
        const date = new Date(today)
        date.setDate(date.getDate() - i)
        dates.push(this.formatDate(date))
      }
      return dates
    },

    // 生成模拟数据
    generateMockData(length, min, max) {
      const data = []
      for (let i = 0; i < length; i++) {
        const value = Math.floor(Math.random() * (max - min + 1)) + min
        data.push(value)
      }
      return data
    },

    // 处理窗口大小变化
    handleResize() {
      if (this.chart) {
        this.$nextTick(() => {
          this.chart.resize()
        })
      }
    },

    // 格式化日期
    formatDate(date) {
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      return `${month}-${day}`
    },

    // 计算 dataZoom 的显示范围
    calculateDataZoomRange(dates) {
      if (!dates || dates.length === 0) {
        return { start: 0, end: 100 }
      }

      // 如果数据量小于等于30天，显示全部
      if (dates.length <= 30) {
        return { start: 0, end: 100 }
      }

      // 如果数据量超过30天，只显示最近30天
      const totalDays = dates.length
      const displayDays = 30
      const start = ((totalDays - displayDays) / totalDays) * 100
      const end = 100

      return { start: Math.max(0, start), end: end }
    }
  }
}
</script>

<style lang="scss" scoped>
.order-trend-chart {
  // 订单统计区域样式
  .order-stats-section {
    margin-bottom: 10px;
    position: relative;

    // 添加底部阴影分隔效果
    &::after {
      content: '';
      position: absolute;
      bottom: 0;
      left: 20px;
      right: 20px;
      height: 1px;
      background: linear-gradient(
        90deg,
        transparent 0%,
        rgba(0, 0, 0, 0.08) 20%,
        rgba(0, 0, 0, 0.12) 50%,
        rgba(0, 0, 0, 0.08) 80%,
        transparent 100%
      );
      box-shadow:
        0 2px 8px rgba(0, 0, 0, 0.12),
        0 1px 4px rgba(0, 0, 0, 0.06);
      border-radius: 1px;
    }
  }

  .order-stat-card {
    border: none;
    border-radius: 12px;
    transition: all 0.3s ease;
    background: white;

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
    }
  }

  .order-stat-content {
    display: flex;
    align-items: center;
    padding: 16px;
    min-height: 100px;
  }

  .order-stat-icon {
    width: 50px;
    height: 50px;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-right: 16px;
    flex-shrink: 0;

    i {
      font-size: 20px;
      color: white;
    }

    &.monthly {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    }

    &.weekly {
      background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
    }
  }

  .order-stat-info {
    flex: 1;
    min-width: 0;
  }

  .order-stat-title {
    font-size: 14px;
    color: #909399;
    margin-bottom: 8px;
    font-weight: 500;
  }

  .order-stat-value {
    display: flex;
    align-items: baseline;
    margin-bottom: 8px;
    gap: 6px;
  }

  .order-stat-number {
    font-size: 22px;
    font-weight: bold;
    color: #303133;
    line-height: 1;
  }

  .order-stat-unit {
    font-size: 14px;
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
  }

  // 图表卡片样式
  .chart-card {
    border: none;
    border-radius: 12px;
    transition: all 0.3s ease;
    background: white;

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
    }
  }

  .chart-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 0;
  }

  .chart-title {
    font-size: 18px;
    font-weight: 600;
    color: #303133;
    display: flex;
    align-items: center;
  }

  .chart-title-icon {
    margin-right: 8px !important;
    font-size: 20px !important;
    color: #409eff !important;
  }

  .chart-content {
    position: relative;
    width: 95%; /* 设置固定宽度，不使用100% */
    margin: 0 auto; /* 居中显示 */

    .chart-container {
      width: 100%;
      min-width: 800px; /* 设置最小宽度确保图表正常显示 */
    }

    .chart-loading {
      position: absolute;
      top: 50%;
      left: 50%;
      transform: translate(-50%, -50%);
      display: flex;
      flex-direction: column;
      align-items: center;
      color: #909399;

      i {
        font-size: 24px;
        margin-bottom: 8px;
      }

      span {
        font-size: 14px;
      }
    }
  }
}

// 响应式设计
@media (max-width: 768px) {
  .order-trend-chart {
    .order-stats-section {
      margin-bottom: 15px;
      padding-bottom: 12px;

      // 移动端保持阴影分隔效果
      &::after {
        left: 15px;
        right: 15px;
        height: 1px;
        background: linear-gradient(
          90deg,
          transparent 0%,
          rgba(0, 0, 0, 0.06) 25%,
          rgba(0, 0, 0, 0.1) 50%,
          rgba(0, 0, 0, 0.06) 75%,
          transparent 100%
        );
        box-shadow:
          0 2px 6px rgba(0, 0, 0, 0.1),
          0 1px 3px rgba(0, 0, 0, 0.05);
      }
    }

    .order-stat-content {
      padding: 12px;
      min-height: 80px;
    }

    .order-stat-icon {
      width: 40px;
      height: 40px;
      margin-right: 12px;

      i {
        font-size: 16px;
      }
    }

    .order-stat-number {
      font-size: 18px;
    }

    .chart-header {
      flex-direction: column;
      align-items: flex-start;
      gap: 12px;
    }
  }
}
</style>
