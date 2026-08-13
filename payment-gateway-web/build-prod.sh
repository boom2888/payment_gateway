# 获取占用 9000 端口的 PID

source /etc/profile


echo "📦 安装依赖中..."
npm install

echo "🚀 开始打包应用"
npm run build

rm -rf /xp/www/web.wynpay.io/*
rm -rf /xp/www/deepay.ai/*

cp dist/* /xp/www/deepay.ai/
mv dist/* /xp/www/web.wynpay.io/


echo "服务更新成功"


#forever start -a -l forever.log -o out.log -e err.log tsx src/Controller.ts





