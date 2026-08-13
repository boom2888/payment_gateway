# 获取占用 9000 端口的 PID

source /etc/profile

cd /root/.jenkins/workspace/pay-gw-admin-git/payment-gateway-web/

# 检查 node_modules 是否存在
echo "📦 安装依赖中..."
npm install

echo "🚀 开始打包应用"
npm run build

rm -rf /xp/www/api.deepay.ai/*

mv dist/* /xp/www/api.deepay.ai/

echo "服务更新成功"


#forever start -a -l forever.log -o out.log -e err.log tsx src/Controller.ts





