# PSP Payment Gateway

PSP 是一个 Java/Spring Boot 多模块支付网关项目，包含管理后台、对外支付 API、App API，以及两个独立前端。

## 项目结构

| 目录 | 说明 | 默认地址 |
| --- | --- | --- |
| `payment-gateway-admin` | 管理后台 API | `http://127.0.0.1:7777/api` |
| `payment-gateway-api` | 商户及支付 API | `http://127.0.0.1:8800/api` |
| `payment-gateway-app` | App API | `http://127.0.0.1:8888/v1` |
| `payment-gateway-service` | 支付与业务服务 | 后端共享模块 |
| `payment-emi-service` | EMI 业务服务 | 后端共享模块 |
| `payment-gateway-framework` | Spring Security、JWT、Web 配置 | 后端共享模块 |
| `payment-gateway-system` | 用户、角色、部门与权限 | 后端共享模块 |
| `admin-ui` | Vue 2 管理端 | 静态站点 |
| `payment-gateway-web` | React/Vite 支付页 | 静态站点 |

## 环境要求

- Linux x86_64，建议 Ubuntu 22.04 或同等级发行版
- JDK 17（当前 Maven 编译目标为 Java 17）
- Maven 3.8+
- MySQL 8.0
- Redis 6+
- Node.js 20 与 Yarn 1.22
- Nginx 1.20+

生产环境应使用独立的低权限系统用户运行服务，不要使用 `root`。

## 部署前准备

### 数据库

仓库目前不包含数据库初始化 SQL。首次部署前需从现有环境或 DBA 获取与当前代码版本匹配的完整 schema 和基础数据，并分别创建数据库账号。

数据库账号只应拥有业务所需的 `SELECT`、`INSERT`、`UPDATE`、`DELETE` 权限，不应授予 `SUPER`、`FILE` 或全局管理权限。

### 必需环境变量

以下秘密不得写入 Git、启动脚本或 systemd unit，生产环境应通过 Secret Manager、Vault、Kubernetes Secret 或权限为 `0600` 的 EnvironmentFile 注入。

| 变量 | 用途 | 要求 |
| --- | --- | --- |
| `JWT_SECRET` | JWT HS512 签名 | 必填，至少 64 字节，每个环境使用不同随机值 |
| `DB_PASSWORD` | MySQL 密码 | 必填 |
| `REDIS_PASSWORD` | Redis 密码 | 必填 |
| `CORS_ALLOWED_ORIGINS` | 浏览器跨域白名单 | 逗号分隔的完整 Origin，不允许 `*` |
| `BINDERR_CLIENT_SECRET` | Binderr OAuth | 使用 Binderr 时必填 |
| `BINDERR_WEBHOOK_SECRET` | Binderr webhook 验证 | 管理后台必填，并与发送方配置一致 |
| `TELEGRAM_BOT_TOKEN` | Telegram 通知 | 启用通知时必填 |
| `CARD_FINGER_SECRET` | 卡指纹计算 | 支付 API 必填 |
| `MAIL_PASSWORD` | SMTP 密码 | 启用邮件时必填 |
| `SUMSUB_APP_TOKEN` | Sumsub App Token | 启用 Sumsub 时必填 |
| `SUMSUB_SECRET_KEY` | Sumsub API Secret | 启用 Sumsub 时必填 |
| `SUMSUB_WEBHOOK_SECRET` | Sumsub webhook 验证 | 启用 Sumsub 时必填 |
| `APP_SECRET_KEY` | App 服务密钥 | 启用 App 服务时必填 |
| `XE_ACCOUNT_ID` | XE 汇率服务账号 | 启用汇率服务时必填 |
| `XE_API_KEY` | XE 汇率服务密钥 | 启用汇率服务时必填，必须使用重新签发的密钥 |
| `STRIPE_API_KEY` | Stripe 服务端密钥 | 启用 Stripe 时必填 |
| `STRIPE_ENDPOINT_SECRET` | Stripe webhook 签名密钥 | 启用 Stripe webhook 时必填 |
| `STRIPE_CONNECT_ENDPOINT_SECRET` | Stripe Connect webhook 签名密钥 | 启用 Connect webhook 时必填 |
| `NUVEI_MERCHANT_KEY` | Nuvei 商户密钥 | 启用 Nuvei 时必填 |
| `NUVEI_INTEGRATION_MERCHANT_KEY` | Nuvei 新集成商户密钥 | 可选，默认使用 `NUVEI_MERCHANT_KEY` |
| `CARATPAY_MERCHANT_KEY` | Caratpay 商户密钥 | 启用 Caratpay 时必填 |
| `B2C2_API_KEY` | B2C2 API 密钥 | 启用 B2C2 时必填 |
| `SHIFT4_SIGNATURE_KEY` | Shift4 签名密钥 | 启用 Shift4 时必填 |
| `THUNES_API_KEY` | Thunes API 密钥 | 启用 Thunes 时必填 |
| `THUNES_API_SECRET` | Thunes API Secret | 启用 Thunes 时必填 |
| `COINSDO_API_KEY` | CoinsDo API 密钥 | 启用 App 服务时必填 |

历史提交中出现过的 XE 与 Stripe 密钥必须在服务商控制台吊销并重新签发；从当前源码删除不能使已泄露的密钥恢复安全。

生成 JWT 密钥示例：

```bash
openssl rand -base64 64
```

环境文件示例 `/etc/psp/psp.env`：

```bash
JWT_SECRET=replace-with-at-least-64-random-bytes
DB_PASSWORD=replace-me
REDIS_PASSWORD=replace-me
CORS_ALLOWED_ORIGINS=https://admin.example.com,https://pay.example.com
BINDERR_CLIENT_SECRET=replace-me
BINDERR_WEBHOOK_SECRET=replace-me
TELEGRAM_BOT_TOKEN=replace-me
CARD_FINGER_SECRET=replace-me
MAIL_PASSWORD=replace-me
SUMSUB_APP_TOKEN=replace-me
SUMSUB_SECRET_KEY=replace-me
SUMSUB_WEBHOOK_SECRET=replace-me
APP_SECRET_KEY=replace-me
STRIPE_API_KEY=replace-me
STRIPE_ENDPOINT_SECRET=replace-me
STRIPE_CONNECT_ENDPOINT_SECRET=replace-me
NUVEI_MERCHANT_KEY=replace-me
CARATPAY_MERCHANT_KEY=replace-me
B2C2_API_KEY=replace-me
SHIFT4_SIGNATURE_KEY=replace-me
THUNES_API_KEY=replace-me
THUNES_API_SECRET=replace-me
COINSDO_API_KEY=replace-me
```

```bash
sudo chown psp:psp /etc/psp/psp.env
sudo chmod 600 /etc/psp/psp.env
```

## 后端配置

仓库内三个 `application.yml` 默认激活 `dev`。生产部署必须通过命令行指定 Profile：

```bash
--spring.profiles.active=prod
```

`payment-gateway-api` 已包含 `application-prod.yml`。管理后台和 App 模块目前没有完整的生产 Profile，部署时必须提供外部配置文件，例如：

```text
/etc/psp/admin/application-prod.yml
/etc/psp/api/application-prod.yml
/etc/psp/app/application-prod.yml
```

外部配置至少应覆盖：

- MySQL URL 和用户名
- Redis host、port、database
- 文件上传目录与公开文件域名
- SMTP、S3 和第三方支付平台地址
- Binderr、Sumsub、Telegram 等功能开关与非秘密标识
- `security.cors.allowed-origins`

不要直接使用仓库中的开发数据库地址部署生产环境。

## 构建后端

在仓库根目录执行：

```bash
mvn clean test
mvn clean package -DskipTests -Pprod
```

主要产物：

```text
payment-gateway-admin/target/payment-gateway-admin-v2.jar
payment-gateway-api/target/payment-gateway-api-v2.jar
payment-gateway-app/target/payment-gateway-app-v2.jar
```

将 JAR 部署到 `/opt/psp`：

```bash
sudo install -d -o psp -g psp /opt/psp
sudo install -o psp -g psp payment-gateway-admin/target/payment-gateway-admin-v2.jar /opt/psp/
sudo install -o psp -g psp payment-gateway-api/target/payment-gateway-api-v2.jar /opt/psp/
sudo install -o psp -g psp payment-gateway-app/target/payment-gateway-app-v2.jar /opt/psp/
```

## 启动后端

单进程验证示例：

```bash
set -a
. /etc/psp/psp.env
set +a

java -Xms512m -Xmx1024m \
  -jar /opt/psp/payment-gateway-api-v2.jar \
  --spring.profiles.active=prod \
  --spring.config.additional-location=file:/etc/psp/api/
```

生产环境建议使用 systemd。以支付 API 为例，新建 `/etc/systemd/system/psp-api.service`：

```ini
[Unit]
Description=PSP Payment API
After=network-online.target
Wants=network-online.target

[Service]
Type=simple
User=psp
Group=psp
WorkingDirectory=/opt/psp
EnvironmentFile=/etc/psp/psp.env
ExecStart=/usr/bin/java -Xms512m -Xmx1024m -jar /opt/psp/payment-gateway-api-v2.jar --spring.profiles.active=prod --spring.config.additional-location=file:/etc/psp/api/
Restart=on-failure
RestartSec=5
SuccessExitStatus=143
NoNewPrivileges=true
PrivateTmp=true
UMask=0077

[Install]
WantedBy=multi-user.target
```

Admin 和 App 服务复制该 unit，并分别修改 JAR、配置目录和服务名。启动服务：

```bash
sudo systemctl daemon-reload
sudo systemctl enable --now psp-api
sudo systemctl status psp-api
journalctl -u psp-api -f
```

## 构建前端

### 管理端

生产 API 地址位于 `admin-ui/.env.production`。确认域名正确后执行：

```bash
cd admin-ui
yarn install --frozen-lockfile
yarn build
```

构建结果位于 `admin-ui/dist/`。

### 支付页面

生产 API 地址和 Stripe publishable key 位于 `payment-gateway-web/.env.production`。确认配置正确后执行：

```bash
cd payment-gateway-web
yarn install --frozen-lockfile
yarn build
```

构建结果位于 `payment-gateway-web/dist/`。前端只能配置 Stripe publishable key，禁止放入 Stripe secret key。

## Nginx 示例

管理端站点示例：

```nginx
server {
    listen 443 ssl http2;
    server_name admin.example.com;

    root /var/www/psp-admin;
    index index.html;

    location / {
        try_files $uri $uri/ /index.html;
    }

    location /api/ {
        proxy_pass http://127.0.0.1:7777/api/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        client_max_body_size 100m;
    }
}
```

支付页将静态目录改为 `/var/www/psp-pay`，并把 `/api/` 代理到 `127.0.0.1:8800`。只对外开放 Nginx 的 80/443，后端的 7777、8800、8888 端口应仅监听内网或由防火墙阻断公网访问。

## 健康检查

```bash
curl --fail http://127.0.0.1:7777/api/common/safeCheck
curl --fail http://127.0.0.1:8800/api/common/safeCheck
```

App 模块当前没有独立的公开健康检查接口，建议由进程状态或受保护的 Actuator 健康端点监控。

## 发布流程

1. 备份数据库，并记录当前 JAR 和前端版本。
2. 在 CI 中执行 `mvn clean test` 和两个前端构建。
3. 将新 JAR、静态文件和外部配置上传到临时目录。
4. 先发布数据库兼容变更，再滚动重启 API、Admin 和 App。
5. 检查健康接口、登录、创建订单、支付回调和 webhook。
6. 观察错误率、支付成功率与日志，确认后清理旧版本。

回滚时恢复上一版本 JAR/静态文件并重启服务。数据库变更必须使用预先验证的反向迁移，禁止直接回滚数据库文件。

## 上线安全检查

- 所有历史泄露过的密码和 Token 已完成轮换。
- `JWT_SECRET` 长度不少于 64 字节，且不同环境不复用。
- `CORS_ALLOWED_ORIGINS` 只包含正式前端域名。
- Git 仓库和构建产物中不存在明文秘密。
- MySQL、Redis 和后端端口不可从公网直接访问。
- TLS 证书有效，HTTP 强制跳转 HTTPS。
- Binderr、Sumsub、Stripe 等 webhook 验签已在真实环境验证。
- 日志中不包含 PAN、CVV、支付 token、Authorization 或原始支付载荷。
- 文件上传目录不可执行，并与应用程序目录隔离。
- 定期执行依赖漏洞扫描，并安排 Spring Boot、POI、JJWT 和前端旧依赖升级。

## 常见问题

### 启动时报 `token.secret must be at least 64 bytes`

未设置 `JWT_SECRET`，或值少于 64 字节。重新生成密钥并通过安全环境变量注入。

### 浏览器请求被 CORS 拒绝

将浏览器实际 Origin 加入 `CORS_ALLOWED_ORIGINS`，例如：

```bash
CORS_ALLOWED_ORIGINS=https://admin.example.com,https://pay.example.com
```

不要使用 `*`，也不要在 Origin 后添加路径。

### 服务启动后无法连接数据库

确认外部 `application-prod.yml` 已被加载，并检查数据库地址、账号、防火墙、TLS 和时区配置：

```bash
journalctl -u psp-api -n 200 --no-pager
```
