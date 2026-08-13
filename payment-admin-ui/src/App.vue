<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script>
export default  {
  name:  'App'
}
</script>
<style>
/* 全局样式：保证能命中 body 里动态插入的节点 */
.copy-hover-popover {
  position: fixed;
  z-index: 9999;
  max-width: 400px;
  max-height: 200px;
  overflow-y: auto;
  background: #fff;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  padding: 8px 12px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  font-size: 14px;
  line-height: 1.5;
  word-break: break-word;
}

.copy-hover-body {
  margin-bottom: 8px;
}

.copy-btn {
  float: none;          /* 去掉右浮动 */
  margin: 0 auto;       /* 水平居中（保险起见）*/
}

.ellipsis-text {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>
