<template>
  <el-card shadow="hover" class="refund-rate-card">
    <div slot="header" class="card-header">
      <span class="card-title">
        <i class="el-icon-warning chart-title-icon" />
        {{ $t('payment.dashboard.refundRateTitle') }}
      </span>
      <!-- <div class="card-controls">
        <el-button type="primary" size="small" @click="refreshData" icon="el-icon-refresh">
          {{ $t('payment.dashboard.refreshButton') }}
        </el-button>
      </div> -->
    </div>
    <div class="chart-container">
      <div ref="refundRateChart" class="chart" :style="{ height: chartHeight + 'px' }" />
    </div>
  </el-card>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'RefundRateChart',
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
    refundRateData: {
      type: Object,
      default: () => ({
        dates: [],
        values: []
      })
    },
    chargebackRateData: {
      type: Object,
      default: () => ({
        dates: [],
        values: []
      })
    },
    cancelRateData: {
      type: Object,
      default: () => ({
        dates: [],
        values: []
      })
    },
    successRateData: {
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
      refreshTimer: null
    }
  },
  watch: {
    refundRateData: {
      handler() {
        this.updateChart()
      },
      deep: true
    },
    chargebackRateData: {
      handler() {
        this.updateChart()
      },
      deep: true
    },
    cancelRateData: {
      handler() {
        this.updateChart()
      },
      deep: true
    },
    successRateData: {
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
      this.chart = echarts.init(this.$refs.refundRateChart)
      // 延迟更新图表，确保props传入
      setTimeout(() => {
        this.updateChart()
      }, 100)

      // 监听窗口大小变化
      window.addEventListener('resize', this.handleResize)
    },

    updateChart() {
      if (!this.chart) return

      // 获取日期数据来计算 dataZoom 范围
      const dates = this.refundRateData.dates || []
      const { start, end } = this.calculateDataZoomRange(dates)

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
          formatter: function (params) {
            let html = `<div style="padding: 8px;">`
            html += `<div style="margin-bottom: 8px; font-weight: bold;">${params[0].axisValue}</div>`

            params.forEach(item => {
              html += `
                <div style="display: flex; align-items: center; margin-bottom: 4px;">
                  <span style="display: inline-block; width: 10px; height: 10px; background-color: ${item.color}; border-radius: 50%; margin-right: 8px;"></span>
                  <span>${item.seriesName}：${item.value}%</span>
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
            zoomLock: true,
            start: 0,
            end: 100
          }
        ],
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: this.refundRateData.dates || [],
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
          }
        },
        series: [
          {
            name: this.$t('payment.dashboard.refundRate'),
            type: 'line',
            data: this.refundRateData.values || [],
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
                    color: 'rgba(245, 108, 108, 0.3)'
                  },
                  {
                    offset: 1,
                    color: 'rgba(245, 108, 108, 0.05)'
                  }
                ]
              }
            }
          },
          {
            name: this.$t('payment.dashboard.chargebackRate'),
            type: 'line',
            data: this.chargebackRateData.values || [],
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
                    color: 'rgba(230, 162, 60, 0.3)'
                  },
                  {
                    offset: 1,
                    color: 'rgba(230, 162, 60, 0.05)'
                  }
                ]
              }
            }
          },
          {
            name: this.$t('payment.dashboard.cancelRate'),
            type: 'line',
            data: this.cancelRateData.values || [],
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
                    color: 'rgba(144, 147, 153, 0.3)'
                  },
                  {
                    offset: 1,
                    color: 'rgba(144, 147, 153, 0.05)'
                  }
                ]
              }
            }
          },
          {
            name: this.$t('payment.dashboard.successRate'),
            type: 'line',
            data: this.successRateData.values || [],
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
        ],
        animation: true,
        animationDuration: 1000,
        animationEasing: 'cubicOut'
      }

      this.chart.setOption(option)
    },

    handleResize() {
      if (this.chart) {
        this.chart.resize()
      }
    },

    refreshData() {
      this.$emit('refresh')
    },

    startAutoRefresh() {
      //   this.refreshTimer = setInterval(() => {
      //     this.refreshData();
      //   }, this.refreshInterval * 1000);
    },

    // 计算 dataZoom 的显示范围，默认显示最近7天
    calculateDataZoomRange(dates) {
      if (!dates || dates.length === 0) {
        return { start: 0, end: 100 }
      }

      // 如果数据量小于等于7天，显示全部
      if (dates.length <= 7) {
        return { start: 0, end: 100 }
      }

      // 如果数据量超过7天，只显示最近7天
      const totalDays = dates.length
      const displayDays = 7
      const start = ((totalDays - displayDays) / totalDays) * 100
      const end = 100

      return { start: Math.max(0, start), end: end }
    }
  }
}
</script>

<style lang="scss" scoped>
.refund-rate-card {
  border: none;
  border-radius: 12px;
  background: white;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  height: 100%;
  display: flex;
  flex-direction: column;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0;
  flex-shrink: 0;
}

.card-title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  display: flex;
  align-items: center;
}

.chart-title-icon {
  margin-right: 8px !important;
  font-size: 18px !important;
  color: #409eff !important;
}

.card-controls {
  display: flex;
  align-items: center;
  gap: 10px;
}

.chart-container {
  position: relative;
  flex: 1;
  width: 95%; /* 设置固定宽度，不使用100% */
  margin: 0 auto; /* 居中显示 */
}

.chart {
  width: 100%;
  min-width: 400px; /* 设置最小宽度确保图表正常显示 */
}

// 响应式设计
@media (max-width: 768px) {
  .card-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }

  .card-controls {
    width: 100%;
    justify-content: flex-end;
  }
}
</style>
