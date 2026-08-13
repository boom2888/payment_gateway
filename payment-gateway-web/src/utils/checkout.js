export
const initCheckout = (params) => {
        console.log('test',params)
        const onResult = function(result) {
                console.log(result,'result')
                console.log('Final callback data received by caller', JSON.stringify(result))
        }
        // debugger;
        /*
           CHECKOUT INITIALIZARION
           ---------------------------------------
        */
        checkout  ({
                env: 'test', //test prod for production
                renderTo: '.container_for_checkout', //your placeholder for the checkout component
                //credentials
                sessionToken: params.sessionToken,//document.getElementById('session').value,
                merchantSiteId: params.merchantSiteId,//document.getElementById('metchantSiteID').value,
                merchantId: params.merchantId, // document.getElementById('metchantID').value,
                //amount and currency for presentation purposes
                currency: params.currency,
                amount: params.amount,
                //basic settings
                country: 'DE',
                locale: 'en',
                fullName: 'CL-BRW1', //simulates 3DS challenge, FL-BRW1 will simulate frictionless
                email: 'integration@nuvei.com',

                onResult: onResult, //with this callback you will recieve the transaction outcome and can react to it

                // optional UI settings
                payButton: "amountButton",
                showResponseMessage: true,
                disableDeclineRecovery: false,
                autoOpenPM: true,
                alwaysCollectCvv: false,
                showCardLogos: true, // default
                blockCards: ["visa","mastercard","maestro","amex"],
                logLevel: 0,
                // i18n : {
                //     my_methods : "Meine Zahlungsmethoden*"
                // },
                fieldStyle: {
                        "base": {
                                "iconColor": "#c4f0ff",
                                "color": "#6b778c",
                                "fontWeight": 400,
                                "fontFamily": "Nunito Sans",
                                "fontSize": "18px",
                                "fontSmoothing": "antialiased",
                                ":-webkit-autofill": {
                                        "color": "#ffffff"
                                },
                                "::placeholder": {
                                        "color": "#6b778c" // default one picked from chrome
                                }
                        },
                        "invalid": {
                                "iconColor": "#FFC7EE",
                                "color": "#FFC7EE"
                        }
                }
        })
}
