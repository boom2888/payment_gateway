<template>
  <div class="app-container">
    <el-row>
      <el-col :span="12" class="card-box">
        <el-card>
          <div slot="header">
            <span>{{ $t('monitor.server.cpu') }}</span>
          </div>
          <div class="el-table el-table--enable-row-hover el-table--medium">
            <table cellspacing="0" style="width: 100%">
              <thead>
                <tr>
                  <th class="is-leaf">
                    <div class="cell">{{ $t('monitor.server.cpuNum') }}</div>
                  </th>
                  <th class="is-leaf">
                    <div class="cell">{{ $t('monitor.server.cpuUsed') }}</div>
                  </th>
                </tr>
              </thead>
              <tbody>
                <tr>
                  <td>
                    <div class="cell">{{ $t('monitor.server.cpuNum') }}</div>
                  </td>
                  <td>
                    <div v-if="server.cpu" class="cell">{{ server.cpu.cpuNum }}</div>
                  </td>
                </tr>
                <tr>
                  <td>
                    <div class="cell">{{ $t('monitor.server.cpuUsed') }}</div>
                  </td>
                  <td>
                    <div v-if="server.cpu" class="cell">{{ server.cpu.used }}%</div>
                  </td>
                </tr>
                <tr>
                  <td>
                    <div class="cell">{{ $t('monitor.server.cpuSys') }}</div>
                  </td>
                  <td>
                    <div v-if="server.cpu" class="cell">{{ server.cpu.sys }}%</div>
                  </td>
                </tr>
                <tr>
                  <td>
                    <div class="cell">{{ $t('monitor.server.cpuFree') }}</div>
                  </td>
                  <td>
                    <div v-if="server.cpu" class="cell">{{ server.cpu.free }}%</div>
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
            <span>{{ $t('monitor.server.mem') }}</span>
          </div>
          <div class="el-table el-table--enable-row-hover el-table--medium">
            <table cellspacing="0" style="width: 100%">
              <thead>
                <tr>
                  <th class="is-leaf">
                    <div class="cell">{{ $t('monitor.server.mem') }}</div>
                  </th>
                  <th class="is-leaf">
                    <div class="cell">{{ $t('monitor.server.mem') }}</div>
                  </th>
                  <th class="is-leaf">
                    <div class="cell">{{ $t('monitor.server.jvm') }}</div>
                  </th>
                </tr>
              </thead>
              <tbody>
                <tr>
                  <td>
                    <div class="cell">{{ $t('monitor.server.memTotal') }}</div>
                  </td>
                  <td>
                    <div v-if="server.mem" class="cell">{{ server.mem.total }}G</div>
                  </td>
                  <td>
                    <div v-if="server.jvm" class="cell">{{ server.jvm.total }}M</div>
                  </td>
                </tr>
                <tr>
                  <td>
                    <div class="cell">{{ $t('monitor.server.memUsed') }}</div>
                  </td>
                  <td>
                    <div v-if="server.mem" class="cell">{{ server.mem.used }}G</div>
                  </td>
                  <td>
                    <div v-if="server.jvm" class="cell">{{ server.jvm.used }}M</div>
                  </td>
                </tr>
                <tr>
                  <td>
                    <div class="cell">{{ $t('monitor.server.memFree') }}</div>
                  </td>
                  <td>
                    <div v-if="server.mem" class="cell">{{ server.mem.free }}G</div>
                  </td>
                  <td>
                    <div v-if="server.jvm" class="cell">{{ server.jvm.free }}M</div>
                  </td>
                </tr>
                <tr>
                  <td>
                    <div class="cell">{{ $t('monitor.server.memUsage') }}</div>
                  </td>
                  <td>
                    <div v-if="server.mem" class="cell" :class="{ 'text-danger': server.mem.usage > 80 }">
                      {{ server.mem.usage }}%
                    </div>
                  </td>
                  <td>
                    <div v-if="server.jvm" class="cell" :class="{ 'text-danger': server.jvm.usage > 80 }">
                      {{ server.jvm.usage }}%
                    </div>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </el-card>
      </el-col>
      <el-col :span="24" class="card-box">
        <el-card>
          <div slot="header">
            <span>{{ $t('monitor.server.serverInfo') }}</span>
          </div>
          <div class="el-table el-table--enable-row-hover el-table--medium">
            <table cellspacing="0" style="width: 100%">
              <tbody>
                <tr>
                  <td>
                    <div class="cell">{{ $t('monitor.server.computerName') }}</div>
                  </td>
                  <td>
                    <div v-if="server.sys" class="cell">{{ server.sys.computerName }}</div>
                  </td>
                  <td>
                    <div class="cell">{{ $t('monitor.server.osName') }}</div>
                  </td>
                  <td>
                    <div v-if="server.sys" class="cell">{{ server.sys.osName }}</div>
                  </td>
                </tr>
                <tr>
                  <td>
                    <div class="cell">{{ $t('monitor.server.computerIp') }}</div>
                  </td>
                  <td>
                    <div v-if="server.sys" class="cell">{{ server.sys.computerIp }}</div>
                  </td>
                  <td>
                    <div class="cell">{{ $t('monitor.server.osArch') }}</div>
                  </td>
                  <td>
                    <div v-if="server.sys" class="cell">{{ server.sys.osArch }}</div>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </el-card>
      </el-col>
      <el-col :span="24" class="card-box">
        <el-card>
          <div slot="header">
            <span>{{ $t('monitor.server.javaInfo') }}</span>
          </div>
          <div class="el-table el-table--enable-row-hover el-table--medium">
            <table cellspacing="0" style="width: 100%">
              <tbody>
                <tr>
                  <td>
                    <div class="cell">{{ $t('monitor.server.javaName') }}</div>
                  </td>
                  <td>
                    <div v-if="server.jvm" class="cell">{{ server.jvm.name }}</div>
                  </td>
                  <td>
                    <div class="cell">{{ $t('monitor.server.javaVersion') }}</div>
                  </td>
                  <td>
                    <div v-if="server.jvm" class="cell">{{ server.jvm.version }}</div>
                  </td>
                </tr>
                <tr>
                  <td>
                    <div class="cell">{{ $t('monitor.server.startTime') }}</div>
                  </td>
                  <td>
                    <div v-if="server.jvm" class="cell">{{ server.jvm.startTime }}</div>
                  </td>
                  <td>
                    <div class="cell">{{ $t('monitor.server.runTime') }}</div>
                  </td>
                  <td>
                    <div v-if="server.jvm" class="cell">{{ server.jvm.runTime }}</div>
                  </td>
                </tr>
                <tr>
                  <td colspan="1">
                    <div class="cell">{{ $t('monitor.server.home') }}</div>
                  </td>
                  <td colspan="3">
                    <div v-if="server.jvm" class="cell">{{ server.jvm.home }}</div>
                  </td>
                </tr>
                <tr>
                  <td colspan="1">
                    <div class="cell">{{ $t('monitor.server.userDir') }}</div>
                  </td>
                  <td colspan="3">
                    <div v-if="server.sys" class="cell">{{ server.sys.userDir }}</div>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </el-card>
      </el-col>
      <el-col :span="24" class="card-box">
        <el-card>
          <div slot="header">
            <span>{{ $t('monitor.server.disk') }}</span>
          </div>
          <div class="el-table el-table--enable-row-hover el-table--medium">
            <table cellspacing="0" style="width: 100%">
              <thead>
                <tr>
                  <th class="is-leaf">
                    <div class="cell">{{ $t('monitor.server.dirName') }}</div>
                  </th>
                  <th class="is-leaf">
                    <div class="cell">{{ $t('monitor.server.sysTypeName') }}</div>
                  </th>
                  <th class="is-leaf">
                    <div class="cell">{{ $t('monitor.server.typeName') }}</div>
                  </th>
                  <th class="is-leaf">
                    <div class="cell">{{ $t('monitor.server.total') }}</div>
                  </th>
                  <th class="is-leaf">
                    <div class="cell">{{ $t('monitor.server.free') }}</div>
                  </th>
                  <th class="is-leaf">
                    <div class="cell">{{ $t('monitor.server.used') }}</div>
                  </th>
                  <th class="is-leaf">
                    <div class="cell">{{ $t('monitor.server.usage') }}</div>
                  </th>
                </tr>
              </thead>
              <tbody v-if="server.sysFiles">
                <tr v-for="sysFile in server.sysFiles" :key="sysFile.id">
                  <td>
                    <div class="cell">{{ sysFile.dirName }}</div>
                  </td>
                  <td>
                    <div class="cell">{{ sysFile.sysTypeName }}</div>
                  </td>
                  <td>
                    <div class="cell">{{ sysFile.typeName }}</div>
                  </td>
                  <td>
                    <div class="cell">{{ sysFile.total }}</div>
                  </td>
                  <td>
                    <div class="cell">{{ sysFile.free }}</div>
                  </td>
                  <td>
                    <div class="cell">{{ sysFile.used }}</div>
                  </td>
                  <td>
                    <div class="cell" :class="{ 'text-danger': sysFile.usage > 80 }">{{ sysFile.usage }}%</div>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getServer } from '@/api/monitor/server'

export default {
  name: 'Server',
  data() {
    return {
      // 加载层信息
      loading: [],
      // 服务器信息
      server: []
    }
  },
  created() {
    this.getList()
    this.openLoading()
  },
  methods: {
    /** 查询服务器信息 */
    getList() {
      getServer().then(response => {
        this.server = response.data
        this.loading.close()
      })
    },
    // 打开加载层
    openLoading() {
      this.loading = this.$loading({
        lock: true,
        text: this.$t('monitor.server.loadingText'),
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      })
    }
  }
}
</script>
