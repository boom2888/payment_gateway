<template>
  <el-card shadow="hover" class="hourly-success-rate-card">
    <div slot="header" class="card-header">
      <span class="card-title">
        <i class="el-icon-data-line chart-title-icon" />
        {{ $t('payment.dashboard.hourlySuccessRateTitle') }}
      </span>
    </div>
    <div class="chart-container">
      <div ref="hourlySuccessRateChart" class="chart" :style="{ height: chartHeight + 'px' }" />
    </div>
  </el-card>
</template>

<script>
import * as echarts from 'echarts'
import { getHourlySuccessRateApi } from '@/api/payment/homeStatistics'

export default {
  name: 'HourlySuccessRateChart',
  props: {
    chartHeight: {
      type: Number,
      default: 400
    },
    autoRefresh: {
      type: Boolean,
      default: false
    },
    refreshInterval: {
      type: Number,
      default: 30
    },
    startDate: {
      type: String,
      default: null
    },
    endDate: {
      type: String,
      default: null
    },
    merchantId: {
      type: [String, Number],
      default: null
    }
  },
  data() {
    return {
      chart: null,
      refreshTimer: null,
      loading: false,
      chartData: {
        hourLabels: [],
        successRates: [],
        totalOrders: [],
        successOrders: [],
        failedOrders: []
      }
    }
  },
  watch: {
    startDate() {
      this.loadData()
    },
    endDate() {
      this.loadData()
    },
    merchantId() {
      this.loadData()
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.initChart()
      this.loadData()
      if (this.autoRefresh) {
        this.startAutoRefresh()
      }
    })
  },
  beforeDestroy() {
    if (this.chart) {
      this.chart.dispose()
    }
    if (this.refreshTimer) {
      clearInterval(this.refreshTimer)
    }
  },
  methods: {
    initChart() {
      this.chart = echarts.init(this.$refs.hourlySuccessRateChart)
      
      // 监听窗口大小变化
      window.addEventListener('resize', this.handleResize)
    },

    async loadData() {
      if (!this.startDate || !this.endDate) {
        return
      }

      this.loading = true
      try {
        const params = {
          startDate: this.startDate,
          endDate: this.endDate
        }
        
        if (this.merchantId) {
          params.merchantId = this.merchantId
        }

        const response = await getHourlySuccessRateApi(params)
        
        if (response && response.data) {
          this.chartData = response.data
          this.updateChart()
        }
      } catch (error) {
        console.error('加载每小时成功率数据失败:', error)
        this.$message.error(this.$t('payment.dashboard.loadHourlyDataFailed'))
      } finally {
        this.loading = false
      }
    },

    updateChart() {
      if (!this.chart) return

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
          axisPointer: {
            type: 'cross',
            label: {
              backgroundColor: '#6a7985'
            }
          },
          formatter: (params) => {
            const dataIndex = params[0].dataIndex
            const hour = this.chartData.hourLabels[dataIndex]
            const successRate = this.chartData.successRates[dataIndex]
            const totalOrders = this.chartData.totalOrders[dataIndex]
            const successOrders = this.chartData.successOrders[dataIndex]
            const failedOrders = this.chartData.failedOrders[dataIndex]

            return `
              <div style="padding: 12px; min-width: 200px;">
                <div style="margin-bottom: 12px; font-weight: bold; font-size: 14px; color: #303133; border-bottom: 1px solid #e4e7ed; padding-bottom: 8px;">
                  ${hour}
                </div>
                <div style="margin-bottom: 8px; display: flex; align-items: center;">
                  <span style="display: inline-block; width: 8px; height: 8px; background: #67C23A; border-radius: 50%; margin-right: 8px;"></span>
                  <span style="color: #606266;">${this.$t('payment.dashboard.hourlySuccessRate')}：</span>
                  <span style="font-weight: bold; color: #67C23A;">${successRate}%</span>
                </div>
                <div style="margin-bottom: 6px; display: flex; align-items: center;">
                  <span style="display: inline-block; width: 8px; height: 8px; background: #409EFF; border-radius: 50%; margin-right: 8px;"></span>
                  <span style="color: #606266;">${this.$t('payment.dashboard.successOrders')}：</span>
                  <span style="font-weight: bold; color: #409EFF;">${successOrders}</span>
                </div>
                <div style="margin-bottom: 6px; display: flex; align-items: center;">
                  <span style="display: inline-block; width: 8px; height: 8px; background: #F56C6C; border-radius: 50%; margin-right: 8px;"></span>
                  <span style="color: #606266;">${this.$t('payment.dashboard.failedOrders')}：</span>
                  <span style="font-weight: bold; color: #F56C6C;">${failedOrders}</span>
                </div>
                <div style="display: flex; align-items: center;">
                  <span style="display: inline-block; width: 8px; height: 8px; background: #909399; border-radius: 50%; margin-right: 8px;"></span>
                  <span style="color: #606266;">${this.$t('payment.dashboard.totalOrders')}：</span>
                  <span style="font-weight: bold; color: #909399;">${totalOrders}</span>
                </div>
              </div>
            `
          }
        },
        legend: {
          show: true,
          top: '5%',
          right: '15%',
          data: [
            this.$t('payment.dashboard.hourlySuccessRate'),
            this.$t('payment.dashboard.successOrders'),
            this.$t('payment.dashboard.failedOrders')
          ],
          textStyle: {
            color: '#606266',
            fontSize: 12
          },
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
              color: '#67C23A',
              borderColor: '#67C23A'
            },
            textStyle: {
              color: '#606266'
            },
            borderColor: '#e4e7ed',
            fillerColor: 'rgba(103, 194, 58, 0.2)'
          },
          {
            type: 'inside',
            xAxisIndex: [0],
            zoomLock: true,
            start: 0,
            end: 100
          }
        ],
        xAxis: {
          type: 'category',
          boundaryGap: true,
          data: this.chartData.hourLabels || [],
          axisLine: {
            lineStyle: {
              color: '#e4e7ed'
            }
          },
          axisLabel: {
            color: '#606266',
            fontSize: 12,
            rotate: 45
          },
          axisTick: {
            show: false
          }
        },
        yAxis: [
          {
            type: 'value',
            name: this.$t('payment.dashboard.successRateUnit'),
            nameTextStyle: {
              color: '#606266',
              fontSize: 12
            },
            position: 'left',
            axisLine: {
              show: false
            },
            axisLabel: {
              color: '#606266',
              fontSize: 12,
              formatter: '{value}%'
            },
            axisTick: {
              show: false
            },
            splitLine: {
              lineStyle: {
                color: '#f5f7fa',
                type: 'dashed'
              }
            },
            min: 0,
            max: 100
          },
          {
            type: 'value',
            name: this.$t('payment.dashboard.orderCountUnit'),
            nameTextStyle: {
              color: '#606266',
              fontSize: 12
            },
            position: 'right',
            axisLine: {
              show: false
            },
            axisLabel: {
              color: '#606266',
              fontSize: 12
            },
            axisTick: {
              show: false
            },
            splitLine: {
              show: false
            }
          }
        ],
        series: [
          {
            name: this.$t('payment.dashboard.successOrders'),
            type: 'bar',
            yAxisIndex: 1,
            data: this.chartData.successOrders || [],
            stack: 'orders',
            itemStyle: {
              color: {
                type: 'linear',
                x: 0,
                y: 0,
                x2: 0,
                y2: 1,
                colorStops: [
                  {
                    offset: 0,
                    color: '#409EFF'
                  },
                  {
                    offset: 1,
                    color: '#66B1FF'
                  }
                ]
              },
              borderRadius: [0, 0, 4, 4]
            },
            emphasis: {
              itemStyle: {
                color: {
                  type: 'linear',
                  x: 0,
                  y: 0,
                  x2: 0,
                  y2: 1,
                  colorStops: [
                    {
                      offset: 0,
                      color: '#337ECC'
                    },
                    {
                      offset: 1,
                      color: '#4A9EFF'
                    }
                  ]
                }
              }
            },
            barWidth: '60%'
          },
          {
            name: this.$t('payment.dashboard.failedOrders'),
            type: 'bar',
            yAxisIndex: 1,
            data: this.chartData.failedOrders || [],
            stack: 'orders',
            itemStyle: {
              color: {
                type: 'linear',
                x: 0,
                y: 0,
                x2: 0,
                y2: 1,
                colorStops: [
                  {
                    offset: 0,
                    color: '#F56C6C'
                  },
                  {
                    offset: 1,
                    color: '#F78989'
                  }
                ]
              },
              borderRadius: [4, 4, 0, 0]
            },
            emphasis: {
              itemStyle: {
                color: {
                  type: 'linear',
                  x: 0,
                  y: 0,
                  x2: 0,
                  y2: 1,
                  colorStops: [
                    {
                      offset: 0,
                      color: '#DD6161'
                    },
                    {
                      offset: 1,
                      color: '#F56C6C'
                    }
                  ]
                }
              }
            },
            barWidth: '60%'
          },
          {
            name: this.$t('payment.dashboard.hourlySuccessRate'),
            type: 'line',
            yAxisIndex: 0,
            data: this.chartData.successRates || [],
            smooth: true,
            symbol: 'circle',
            symbolSize: 8,
            z: 10,
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
                    color: 'rgba(103, 194, 58, 0.3)'
                  },
                  {
                    offset: 1,
                    color: 'rgba(103, 194, 58, 0.05)'
                  }
                ]
              }
            }
          }
        ]
      }

      this.chart.setOption(option)
    },

    refreshData() {
      this.loadData()
      this.$emit('refresh')
    },

    handleResize() {
      if (this.chart) {
        this.chart.resize()
      }
    },

    startAutoRefresh() {
      this.refreshTimer = setInterval(() => {
        this.loadData()
      }, this.refreshInterval * 1000)
    }
  }
}
</script>

<style lang="scss" scoped>
.hourly-success-rate-card {
  border: none;
  border-radius: 16px;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  background: linear-gradient(135deg, #ffffff 0%, #f8fafc 100%);
  height: 100%;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
  border: 1px solid rgba(0, 0, 0, 0.06);

  &:hover {
    transform: translateY(-4px);
    box-shadow: 0 12px 32px rgba(0, 0, 0, 0.12);
    border-color: rgba(103, 194, 58, 0.2);
  }
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0;
}

.card-title {
  font-size: 18px;
  font-weight: 700;
  color: #1a202c;
  display: flex;
  align-items: center;
  letter-spacing: -0.025em;
}

.chart-title-icon {
  margin-right: 10px;
  font-size: 20px;
  color: #67C23A;
  background: linear-gradient(135deg, #67C23A 0%, #85CE61 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.chart-container {
  position: relative;
  padding: 8px;
  background: rgba(255, 255, 255, 0.5);
  border-radius: 12px;
  backdrop-filter: blur(10px);
}

.chart {
  width: 100%;
  border-radius: 8px;
  background: transparent;
}
</style>

