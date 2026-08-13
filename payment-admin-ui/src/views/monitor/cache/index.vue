<template>
  <div class="app-container">
    <el-row>
      <el-col :span="24" class="card-box">
        <el-card>
          <div slot="header">
            <span>{{ $t('monitor.cache.baseInfo') }}</span>
          </div>
          <div class="el-table el-table--enable-row-hover el-table--medium">
            <table cellspacing="0" style="width: 100%">
              <tbody>
                <tr>
                  <td>
                    <div class="cell">{{ $t('monitor.cache.redisVersion') }}</div>
                  </td>
                  <td>
                    <div v-if="cache.info" class="cell">{{ cache.info.redis_version }}</div>
                  </td>
                  <td>
                    <div class="cell">{{ $t('monitor.cache.redisMode') }}</div>
                  </td>
                  <td>
                    <div v-if="cache.info" class="cell">
                      {{
                        cache.info.redis_mode == 'standalone'
                          ? $t('monitor.cache.standalone')
                          : $t('monitor.cache.cluster')
                      }}
                    </div>
                  </td>
                  <td>
                    <div class="cell">{{ $t('monitor.cache.port') }}</div>
                  </td>
                  <td>
                    <div v-if="cache.info" class="cell">{{ cache.info.tcp_port }}</div>
                  </td>
                  <td>
                    <div class="cell">{{ $t('monitor.cache.clients') }}</div>
                  </td>
                  <td>
                    <div v-if="cache.info" class="cell">{{ cache.info.connected_clients }}</div>
                  </td>
                </tr>
                <tr>
                  <td>
                    <div class="cell">{{ $t('monitor.cache.uptime') }}</div>
                  </td>
                  <td>
                    <div v-if="cache.info" class="cell">{{ cache.info.uptime_in_days }}</div>
                  </td>
                  <td>
                    <div class="cell">{{ $t('monitor.cache.usedMemory') }}</div>
                  </td>
                  <td>
                    <div v-if="cache.info" class="cell">{{ cache.info.used_memory_human }}</div>
                  </td>
                  <td>
                    <div class="cell">{{ $t('monitor.cache.usedCPU') }}</div>
                  </td>
                  <td>
                    <div v-if="cache.info" class="cell">
                      {{ parseFloat(cache.info.used_cpu_user_children).toFixed(2) }}
                    </div>
                  </td>
                  <td>
                    <div class="cell">{{ $t('monitor.cache.memoryConfig') }}</div>
                  </td>
                  <td>
                    <div v-if="cache.info" class="cell">{{ cache.info.maxmemory_human }}</div>
                  </td>
                </tr>
                <tr>
                  <td>
                    <div class="cell">{{ $t('monitor.cache.aofEnabled') }}</div>
                  </td>
                  <td>
                    <div v-if="cache.info" class="cell">
                      {{ cache.info.aof_enabled == '0' ? $t('monitor.cache.no') : $t('monitor.cache.yes') }}
                    </div>
                  </td>
                  <td>
                    <div class="cell">{{ $t('monitor.cache.rdbSuccess') }}</div>
                  </td>
                  <td>
                    <div v-if="cache.info" class="cell">{{ cache.info.rdb_last_bgsave_status }}</div>
                  </td>
                  <td>
                    <div class="cell">{{ $t('monitor.cache.keyCount') }}</div>
                  </td>
                  <td>
                    <div v-if="cache.dbSize" class="cell">{{ cache.dbSize }}</div>
                  </td>
                  <td>
                    <div class="cell">{{ $t('monitor.cache.netInOut') }}</div>
                  </td>
                  <td>
                    <div v-if="cache.info" class="cell">
                      {{ cache.info.instantaneous_input_kbps }}kps/{{ cache.info.instantaneous_output_kbps }}kps
                    </div>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </el-card>
      </el-col>

      <el-col :span="12" class="card-box">
        <el-card>
          <div slot="header">
            <span>{{ $t('monitor.cache.commandStats') }}</span>
          </div>
          <div class="el-table el-table--enable-row-hover el-table--medium">
            <div ref="commandstats" style="height: 420px" />
          </div>
        </el-card>
      </el-col>

      <el-col :span="12" class="card-box">
        <el-card>
          <div slot="header">
            <span>{{ $t('monitor.cache.memoryInfo') }}</span>
          </div>
          <div class="el-table el-table--enable-row-hover el-table--medium">
            <div ref="usedmemory" style="height: 420px" />
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getCache } from '@/api/monitor/cache'
import * as echarts from 'echarts'

export default {
  name: 'Server',
  data() {
    return {
      // 加载层信息
      loading: [],
      // 统计命令信息
      commandstats: null,
      // 使用内存
      usedmemory: null,
      // cache信息
      cache: []
    }
  },
  created() {
    this.getList()
    this.openLoading()
  },
  methods: {
    /** 查缓存询信息 */
    getList() {
      getCache().then(response => {
        this.cache = response.data
        this.loading.close()

        this.commandstats = echarts.init(this.$refs.commandstats, 'macarons')
        this.commandstats.setOption({
          tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b} : {c} ({d}%)'
          },
          series: [
            {
              name: this.$t('monitor.cache.commandStats'),
              type: 'pie',
              roseType: 'radius',
              radius: [15, 95],
              center: ['50%', '38%'],
              data: response.data.commandStats,
              animationEasing: 'cubicInOut',
              animationDuration: 1000
            }
          ]
        })
        this.usedmemory = echarts.init(this.$refs.usedmemory, 'macarons')
        this.usedmemory.setOption({
          tooltip: {
            formatter: '{b} <br/>{a} : ' + this.cache.info.used_memory_human
          },
          series: [
            {
              name: this.$t('monitor.cache.peak'),
              type: 'gauge',
              min: 0,
              max: 1000,
              detail: {
                formatter: this.cache.info.used_memory_human
              },
              data: [
                {
                  value: parseFloat(this.cache.info.used_memory_human),
                  name: this.$t('monitor.cache.memoryConsume')
                }
              ]
            }
          ]
        })
      })
    },
    // 打开加载层
    openLoading() {
      this.loading = this.$loading({
        lock: true,
        text: this.$t('monitor.cache.loadingText'),
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      })
    }
  }
}
</script>
