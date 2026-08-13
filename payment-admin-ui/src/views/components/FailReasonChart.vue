<template>
  <el-card shadow="hover" class="fail-reason-card">
    <div slot="header" class="card-header">
      <span class="card-title">
        <i class="el-icon-warning chart-title-icon" />
        {{ $t('payment.dashboard.failReasonTitle') }}
      </span>
    </div>
    <div class="chart-container">
      <div ref="failReasonChart" class="chart" :style="{ height: chartHeight + 'px' }" />
      <div class="chart-legend">
        <div v-for="(item, index) in visibleLegendData" :key="index" class="legend-item">
          <div class="legend-left">
            <span class="legend-color" :style="{ backgroundColor: item.color }" />
            <span 
              class="legend-text" 
              :class="{ 'ellipsis-text': isTextOverflow(item.name) }"
              v-copy-hover="getCopyHoverValue(item)"
            >
              {{ item.name }}
            </span>
          </div>
          <div class="legend-right">
            <span class="legend-value">{{ item.value }}% ({{ item.count }}笔)</span>
          </div>
        </div>
        <div v-if="legendData.length > maxVisibleItems" class="legend-toggle" @click="toggleLegend">
          <i :class="isLegendExpanded ? 'el-icon-arrow-up' : 'el-icon-arrow-down'" />
          <span>{{ isLegendExpanded ? '收起' : `展开更多 (${legendData.length - maxVisibleItems})` }}</span>
        </div>
      </div>
    </div>
  </el-card>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'FailReasonChart',
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
    failReasonData: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      chart: null,
      refreshTimer: null,
      legendData: [],
      colors: ['#F56C6C', '#E6A23C', '#F78989', '#FFB6C1', '#FFA07A', '#FF7F50', '#FF6347', '#FF4500', '#DC143C'],
      isLegendExpanded: false,
      maxVisibleItems: 5 // 默认显示前5个
    }
  },
  computed: {
    // 计算可见的图例数据
    visibleLegendData() {
      if (this.isLegendExpanded) {
        return this.legendData
      }
      return this.legendData.slice(0, this.maxVisibleItems)
    }
  },
  watch: {
    failReasonData: {
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
      this.chart = echarts.init(this.$refs.failReasonChart)
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

      // 为数据添加颜色
      const chartData = this.failReasonData.map((item, index) => ({
        ...item,
        itemStyle: {
          color: this.colors[index % this.colors.length]
        }
      }))

      const option = {
        tooltip: {
          trigger: 'item',
          backgroundColor: 'rgba(255, 255, 255, 0.95)',
          borderColor: '#e4e7ed',
          borderWidth: 1,
          borderRadius: 8,
          textStyle: {
            color: '#303133'
          },
          formatter: params => {
            return `
              <div style="padding: 12px; min-width: 200px; font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;">
                <div style="margin-bottom: 8px; font-weight: 600; font-size: 14px; color: #303133; border-bottom: 1px solid #e4e7ed; padding-bottom: 8px;">
                  ${params.name}
                </div>
                <div style="margin-bottom: 6px; display: flex; align-items: center; justify-content: space-between;">
                  <div style="display: flex; align-items: center;">
                    <span style="display: inline-block; width: 8px; height: 8px; background: ${params.color}; border-radius: 50%; margin-right: 8px;"></span>
                    <span style="color: #606266; font-size: 12px;">占比：</span>
                  </div>
                  <span style="font-weight: 600; color: #F56C6C; font-size: 13px;">${params.value}%</span>
                </div>
                <div style="display: flex; align-items: center; justify-content: space-between;">
                  <div style="display: flex; align-items: center;">
                    <span style="display: inline-block; width: 8px; height: 8px; background: #909399; border-radius: 50%; margin-right: 8px;"></span>
                    <span style="color: #606266; font-size: 12px;">订单数：</span>
                  </div>
                  <span style="font-weight: 600; color: #606266; font-size: 13px;">${params.data.count}笔</span>
                </div>
              </div>
            `
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
                shadowColor: 'rgba(245, 108, 108, 0.5)'
              }
            },
            labelLine: {
              show: false
            },
            data: chartData
          }
        ]
      }

      this.chart.setOption(option)
    },

    updateLegendData() {
      this.legendData = this.failReasonData.map((item, index) => ({
        name: item.name,
        value: item.value,
        count: item.count,
        fullName: item.fullName || item.name, // 确保fullName字段存在
        color: this.colors[index % this.colors.length]
      }))
    },

    handleResize() {
      if (this.chart) {
        this.chart.resize()
      }
    },

    refreshData() {
      this.$emit('refresh')
    },

    // 切换图例展开/折叠
    toggleLegend() {
      this.isLegendExpanded = !this.isLegendExpanded
    },

    // 判断文本是否会溢出
    isTextOverflow(text) {
      if (!text) return false
      // 只有当文字长度大于120个字符时才启用悬停功能
      return text.length > 120
    },

    // 获取复制悬停的值
    getCopyHoverValue(item) {
      // 只有当文本会溢出且有完整内容时才启用悬停功能
      if (this.isTextOverflow(item.name) && (item.fullName || item.name)) {
        return item.fullName || item.name
      }
      // 返回undefined，这样v-copy-hover指令就不会显示悬停效果
      return undefined
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
.fail-reason-card {
  border: none;
  border-radius: 16px;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  background: linear-gradient(135deg, #ffffff 0%, #fef7f7 100%);
  height: 100%;
  display: flex;
  flex-direction: column;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
  border: 1px solid rgba(245, 108, 108, 0.1);

  &:hover {
    transform: translateY(-4px);
    box-shadow: 0 12px 32px rgba(245, 108, 108, 0.15);
    border-color: rgba(245, 108, 108, 0.2);
  }
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0;
  flex-shrink: 0;
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
  color: #F56C6C;
  background: linear-gradient(135deg, #F56C6C 0%, #F78989 100%);
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
  flex: 1;
  display: flex;
  flex-direction: column;
}

.chart {
  width: 100%;
  border-radius: 8px;
  background: transparent;
  flex: 1;
}

.chart-legend {
  margin-top: 16px;
  padding: 12px;
  background: rgba(255, 255, 255, 0.8);
  border-radius: 8px;
  border: 1px solid rgba(245, 108, 108, 0.1);
}

.legend-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 8px;
  padding: 4px 8px;
  border-radius: 6px;
  transition: background-color 0.2s ease;

  &:hover {
    background-color: rgba(245, 108, 108, 0.05);
  }

  &:last-child {
    margin-bottom: 0;
  }
}

.legend-left {
  display: flex;
  align-items: center;
  flex: 1;
  min-width: 0; /* 允许收缩 */
}

.legend-right {
  display: flex;
  align-items: center;
  flex-shrink: 0; /* 防止收缩 */
}

.legend-color {
  width: 12px;
  height: 12px;
  border-radius: 50%;
  margin-right: 8px;
  flex-shrink: 0;
}

.legend-text {
  font-size: 13px;
  color: #606266;
  font-weight: 500;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  max-width: 400px; /* 加倍宽度，显示更多文字 */
  min-width: 0; /* 确保可以收缩 */
}

.legend-value {
  font-size: 13px;
  color: #F56C6C;
  font-weight: 600;
  text-align: right;
  white-space: nowrap; /* 防止换行 */
}

.legend-toggle {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 8px 12px;
  margin-top: 8px;
  background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
  border: 1px solid #dee2e6;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 12px;
  color: #6c757d;
}

.legend-toggle:hover {
  background: linear-gradient(135deg, #e9ecef 0%, #dee2e6 100%);
  border-color: #adb5bd;
  color: #495057;
}

.legend-toggle i {
  margin-right: 4px;
  font-size: 12px;
}
</style>
