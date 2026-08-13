<!-- 订单均价统计 -->
<template>
  <div class="order-trend-chart">
    <el-card shadow="hover" class="chart-card">
      <div>
        <div slot="header" class="chart-header">
          <span class="chart-title">
            <i class="el-icon-wallet chart-title-icon" />
            {{ $t('payment.dashboard.orderAverageTitle') }}
          </span>
        </div>

        <!-- 订单金额统计 -->
        <div class="order-stats-section">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-card shadow="hover" class="order-stat-card">
                <div class="order-stat-content">
                  <div class="order-stat-icon monthly">
                    <i class="el-icon-coin" />
                  </div>
                  <div class="order-stat-info">
                    <div class="order-stat-title">{{ $t('payment.dashboard.monthlyAvgPrice') }}</div>
                    <div class="order-stat-value">
                      <span class="order-stat-number">{{ monthlyAvgPrice.toFixed(2) }}</span>
                      <span class="order-stat-unit">{{ $t('payment.dashboard.currencyUnit') }}</span>
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
                    <i class="el-icon-s-finance" />
                  </div>
                  <div class="order-stat-info">
                    <div class="order-stat-title">{{ $t('payment.dashboard.weeklyAvgPrice') }}</div>
                    <div class="order-stat-value">
                      <span class="order-stat-number">{{ weeklyAvgPrice.toFixed(2) }}</span>
                      <span class="order-stat-unit">{{ $t('payment.dashboard.currencyUnit') }}</span>
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
  name: 'OrderAverage',
  props: {
    // 图表高度
    chartHeight: {
      type: Number,
      default: 400
    },
    // 本月订单均价
    monthlyAvgPrice: {
      type: Number,
      default: 0
    },
    // 本周订单均价
    weeklyAvgPrice: {
      type: Number,
      default: 0
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
    // 订单均价数据
    averagePriceData: {
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
    // 监听订单均价数据变化，自动更新图表
    averagePriceData: {
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
            params.forEach(item => {
              html += `
                <div style="display: flex; align-items: center; margin-bottom: 4px;">
                  <span style="display: inline-block; width: 10px; height: 10px; background-color: ${item.color}; border-radius: 50%; margin-right: 8px;"></span>
                  <span>${item.seriesName}：${this.$t('payment.dashboard.currencyUnit')}${item.value}</span>
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
            formatter: '${value}'
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
            name: this.$t('payment.dashboard.averagePrice'),
            type: 'line',
            data: [],
            smooth: true,
            symbol: 'circle',
            symbolSize: 8,
            lineStyle: {
              color: '#409EFF',
              width: 4
            },
            itemStyle: {
              color: '#409EFF',
              borderColor: '#fff',
              borderWidth: 3
            },
            areaStyle: {
              color: {
                type: 'linear',
                x: 0,
                y: 0,
                x2: 0,
                y2: 1,
                colorStops: [
                  {
                    offset: 0,
                    color: 'rgba(64, 158, 255, 0.2)'
                  },
                  {
                    offset: 1,
                    color: 'rgba(64, 158, 255, 0.05)'
                  }
                ]
              }
            },
            emphasis: {
              focus: 'series',
              itemStyle: {
                color: '#409EFF',
                borderColor: '#fff',
                borderWidth: 4,
                shadowBlur: 15,
                shadowColor: 'rgba(64, 158, 255, 0.5)'
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

      // 直接使用传入的订单均价数据
      const dates = this.averagePriceData.dates || []
      const values = this.averagePriceData.values || []

      // 计算 dataZoom 的显示范围，如果数据超过30天，只显示最近30天
      const { start, end } = this.calculateDataZoomRange(dates)

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
          data: dates
        },
        series: [
          {
            data: values
          }
        ]
      }

      this.chart.setOption(option)
    },

    // 处理窗口大小变化
    handleResize() {
      if (this.chart) {
        this.$nextTick(() => {
          this.chart.resize()
        })
      }
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
