
#!/bin/bash


export PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin
source /etc/profile
export JENKINS_NODE_COOKIE=dontKillMe

echo "删除旧文件完成..."
rm -rf /xp/www/admin.deepay.ai/*

cd /root/.jenkins/workspace/payment-gateway-admin-maven/admin-ui/

npm install
npm run build
echo "打包完成..."

pwd

mv dist/* /xp/www/admin.deepay.ai/



echo "替换页面完成..."




