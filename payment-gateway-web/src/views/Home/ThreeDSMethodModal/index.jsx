import React, { useEffect, useRef, useState } from 'react';
import { Modal } from 'antd';

const ThreeDSMethodModal = ({ visible, onClose, methodUrl, methodData }) => {
    const iframeRef = useRef(null);
    const [iframeLoaded, setIframeLoaded] = useState(false);

    useEffect(() => {
        if (visible && iframeLoaded && methodUrl && methodData) {
            const form = document.createElement('form');
            form.method = 'POST';
            form.action = methodUrl;
            form.target = 'threeDSMethodIframe';

            const input = document.createElement('input');
            input.type = 'hidden';
            input.name = 'threeDSMethodData';
            input.value = methodData;
            form.appendChild(input);
            debugger
            document.body.appendChild(form);
            form.submit();
            document.body.removeChild(form);
        }
    }, [visible, iframeLoaded, methodUrl, methodData]);

    return (
        <Modal
            title="3DS Method Verification"
            open={visible}
            onCancel={() => {
                setIframeLoaded(false);
                onClose();
            }}
            footer={null}
            width={600}
            destroyOnClose
        >
            <iframe
                ref={iframeRef}
                name="threeDSMethodIframe"
                title="3DS Method"
                style={{ width: '100%', height: '300px', border: 'none' }}
                onLoad={() => setIframeLoaded(true)}
            />
        </Modal>
    );
};

export default ThreeDSMethodModal;