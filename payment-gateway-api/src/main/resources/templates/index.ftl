<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Goods Shop</title>

    <style>
        body {
            margin: 0;
            font-family: Arial;
            background: #f5f6fa;
        }

        .container { padding: 16px; }

        .grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
            gap: 16px;
        }

        .card {
            background: #fff;
            border-radius: 10px;
            overflow: hidden;
            box-shadow: 0 3px 10px rgba(0,0,0,0.1);
        }

        .card img {
            width: 100%;
            height: 120px;
            object-fit: cover;
        }

        .info { padding: 10px; }

        .price {
            color: #6772e5;
            font-weight: bold;
        }

        button {
            width: 100%;
            margin-top: 6px;
            padding: 6px;
            border: none;
            border-radius: 6px;
            background: #6772e5;
            color: #fff;
        }

        /* loading */
        .loading-mask {
            position: fixed;
            inset: 0;
            background: rgba(0,0,0,0.3);
            display: none;
            justify-content: center;
            align-items: center;
            z-index: 999;
        }

        .spinner {
            width: 50px;
            height: 50px;
            border: 4px solid rgba(255,255,255,0.3);
            border-top: 4px solid #fff;
            border-radius: 50%;
            animation: spin 1s linear infinite;
        }

        @keyframes spin {
            to { transform: rotate(360deg); }
        }

        /* dialog */
        .dialog-mask {
            position: fixed;
            inset: 0;
            background: rgba(0,0,0,0.6);
            display: none;
            justify-content: center;
            align-items: center;
            z-index: 1000;
        }

        .dialog {
            width: 90%;
            max-width: 500px;
            height: 80%;
            background: #fff;
            border-radius: 12px;
            overflow: hidden;
        }

        .dialog iframe {
            width: 100%;
            height: 100%;
            border: none;
        }
    </style>
</head>

<body>

<div class="container">
    <h2>Goods Marketplace</h2>
    <div class="grid" id="list"></div>
</div>

<!-- loading -->
<div class="loading-mask" id="loading">
    <div class="spinner"></div>
</div>

<!-- dialog -->
<div class="dialog-mask" id="dialog">
    <div class="dialog">
        <iframe id="payFrame"></iframe>
    </div>
</div>

<#noparse>
    <script>
        const list = document.getElementById("list");
        const loading = document.getElementById("loading");
        const dialog = document.getElementById("dialog");
        const payFrame = document.getElementById("payFrame");

        // ===== 状态控制 =====
        function setLoading(type) {
            if (type === "loading") {
                loading.style.display = "flex";
            } else {
                loading.style.display = "none";
            }
        }

        function changeOpen(open) {
            dialog.style.display = open ? "flex" : "none";
            if (!open) payFrame.src = "";
        }

        // ===== 模拟订单轮询 =====
        function getOrderOn() {
            console.log("查询订单状态...");
        }

        // ===== 商品数据 =====
        const products = [];
        for (let i = 1; i <= 10; i++) {
            products.push({
                id: i,
                // name: "数字货币NFT #" + i,
                enName: "Goods" + i,
                price: i + 2,
                img: "https://picsum.photos/200?random=" + i
            });
        }

        // ===== 渲染 =====
        function render() {
            list.innerHTML = "";

            products.forEach(p => {
                const div = document.createElement("div");
                div.className = "card";

                div.innerHTML =
                    '<img src="' + p.img + '" />' +
                    '<div class="info">' +
                    '<small>' + p.enName + '</small>' +
                    '<div class="price">€' + p.price + '</div>' +
                    '<button>Buy</button>' +
                    '</div>';

                div.querySelector("button").onclick = () => buy(p.price, p.enName);

                list.appendChild(div);
            });
        }

        // ===== 购买 =====
        function buy(price, enName) {
            setLoading("loading");

            fetch("/api/buy?price=" + price + "&currency=EUR" +"&productName=" + enName)
                .then(res => res.json())
                .then(res => {
                    setLoading("none");

                    if (res.payUrl) {
                        payFrame.src = res.payUrl;
                        changeOpen(true);
                    } else {
                        alert("未返回支付链接");
                    }
                })
                .catch(() => {
                    setLoading("none");
                    alert("请求失败");
                });
        }

        // ===== 监听 iframe 事件 =====
        window.addEventListener("message", function ({ data }) {
            const { type } = data || {};

            if (type === "page_loaded") {
                console.log("page fully loaded");

            } else if (type === "logged_in_success") {
                console.log("token:", data.token);

            } else if (type === "order_3ds_status") {
                if (data.is3DS) console.log("3DS checking is triggered");
                else console.log("3DS checking is not triggered");

            } else if (type === "kyc_required") {
                setLoading("iframe");

            } else if (type === "logged_in_failure") {
                alert(JSON.stringify(data.errorCode));

            } else if (type === "order_cancelled") {
                console.log("order cancelled");
                changeOpen(false);        // 关闭弹窗
                setLoading("none");

            } else if (type === "order_completed") {
                console.log("order completed");
                changeOpen(false);        // 关闭弹窗
                setLoading("none");

                setTimeout(() => {
                    getOrderOn();
                }, 1000);
            }
        });

        // 初始化
        render();
    </script>
</#noparse>

</body>
</html>