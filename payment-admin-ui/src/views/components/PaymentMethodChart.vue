<template>
  <el-card shadow="hover" class="payment-method-card">
    <div slot="header" class="card-header">
      <span class="card-title">
        <i class="el-icon-bank-card chart-title-icon" />
        {{ $t('payment.dashboard.paymentMethodTitle') }}
      </span>
      <!-- <div class="card-controls">
        <el-button type="primary" size="small" @click="refreshData" icon="el-icon-refresh">
          {{ $t('payment.dashboard.refreshButton') }}
        </el-button>
      </div> -->
    </div>
    <div class="chart-container">
      <div ref="paymentMethodChart" class="chart" :style="{ height: chartHeight + 'px' }" />
      <div class="chart-legend">
        <div v-for="(item, index) in legendData" :key="index" class="legend-item">
          <span class="legend-color" :style="{ backgroundColor: item.color }" />
          <span class="legend-text">{{ item.name }}</span>
          <span class="legend-value">{{ item.value }}%</span>
        </div>
      </div>
    </div>
  </el-card>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'PaymentMethodChart',
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
    paymentMethodData: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      chart: null,
      refreshTimer: null,
      legendData: [],
      colors: ['#5470c6', '#91cc75', '#fac858', '#ee6666', '#73c0de', '#3ba272', '#fc8452', '#9a60b4', '#ea7ccc'],
      // 卡类型专用配色
      cardColors: {
        'Visa': '#4A90E2', // 清爽蓝色
        'Master': '#F5A623', // 温暖橙色
        'MasterCard': '#F5A623',
        'UNKNOWN': '#D0D0D0' // 浅灰色
      }
    }
  },
  watch: {
    paymentMethodData: {
      handler() {
        this.updateChart()
        this.updateLegendData()
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
      this.chart = echarts.init(this.$refs.paymentMethodChart)
      // 延迟更新图表，确保props传入
      setTimeout(() => {
        this.updateChart()
        this.updateLegendData()
      }, 100)

      // 监听窗口大小变化
      window.addEventListener('resize', this.handleResize)
    },

    updateChart() {
      if (!this.chart) return

      // 将嵌套数据展平为单层饼图数据
      const flattenedData = this.flattenPaymentData()

      const option = {
        tooltip: {
          trigger: 'item',
          formatter: params => {
            return `<div style="display: flex; align-items: center;">
              <span style="display: inline-block; width: 10px; height: 10px; background: ${params.color}; margin-right: 8px; border-radius: 50%;"></span>
              ${params.name}: ${params.value}%
            </div>`
          }
        },
        series: [
          {
            type: 'pie',
            radius: ['40%', '70%'],
            center: ['50%', '50%'],
            avoidLabelOverlap: false,
            itemStyle: {
              borderRadius: 8,
              borderColor: '#fff',
              borderWidth: 2
            },
            label: {
              show: false
            },
            emphasis: {
              label: {
                show: true,
                fontSize: '14',
                fontWeight: 'bold'
              },
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(255, 255, 255, 0.5)'
              }
            },
            labelLine: {
              show: false
            },
            data: flattenedData
          }
        ]
      }

      this.chart.setOption(option)
    },

    // 将嵌套数据展平为单层数据
    flattenPaymentData() {
      const flattened = []
      let colorIndex = 0

      this.paymentMethodData.forEach(item => {
        // 如果有子节点，展开子节点
        if (item.children && item.children.length > 0) {
          item.children.forEach(child => {
            flattened.push({
              name: child.name,
              value: child.value,
              itemStyle: {
                color: this.cardColors[child.name] || this.colors[colorIndex++ % this.colors.length]
              }
            })
          })
        } else {
          // 没有子节点，直接添加
          flattened.push({
            name: item.name,
            value: item.value,
            itemStyle: {
              color: this.colors[colorIndex++ % this.colors.length]
            }
          })
        }
      })

      return flattened
    },

    updateLegendData() {
      const legends = []
      let colorIndex = 0

      this.paymentMethodData.forEach(item => {
        // 如果有子节点，展开子节点
        if (item.children && item.children.length > 0) {
          item.children.forEach(child => {
            legends.push({
              name: child.name,
              value: child.value,
              color: this.cardColors[child.name] || this.colors[colorIndex++ % this.colors.length]
            })
          })
        } else {
          // 没有子节点，直接添加
          legends.push({
            name: item.name,
            value: item.value,
            color: this.colors[colorIndex++ % this.colors.length]
          })
        }
      })

      this.legendData = legends
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
    }
  }
}
</script>

<style lang="scss" scoped>
.payment-method-card {
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
  margin-right: 8px;
  font-size: 18px;
  color: #409eff;
}

.card-controls {
  display: flex;
  align-items: center;
  gap: 10px;
}

.chart-container {
  position: relative;
  flex: 1;
  display: flex;
  flex-direction: column;
  width: 95%; /* 设置固定宽度，不使用100% */
  margin: 0 auto; /* 居中显示 */
}

.chart {
  width: 100%;
  flex: 1;
  min-width: 300px; /* 设置最小宽度确保图表正常显示 */
}

.chart-legend {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(120px, 1fr));
  gap: 12px;
  margin-top: 16px;
  padding-top: 16px;
  border-top: 1px solid #f0f2f5;
  flex-shrink: 0;
  min-height: 60px;
  align-content: start;
}

.legend-item {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 6px 10px;
  background: #f8f9fa;
  border-radius: 6px;
  transition: all 0.3s ease;
  min-height: 32px;

  &:hover {
    background: #e9ecef;
    transform: translateY(-1px);
  }
}

.legend-color {
  width: 12px;
  height: 12px;
  border-radius: 50%;
  flex-shrink: 0;
}

.legend-text {
  flex: 1;
  font-size: 14px;
  color: #606266;
  font-weight: 500;
  min-width: 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.legend-value {
  font-size: 14px;
  color: #303133;
  font-weight: 600;
  flex-shrink: 0;
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

  .chart-legend {
    grid-template-columns: 1fr;
  }
}
</style>
