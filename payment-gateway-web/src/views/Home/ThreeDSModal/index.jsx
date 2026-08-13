import React, { useEffect } from 'react';
import { Modal } from 'antd';

const ThreeDSModal = ({ visible, onClose, acsUrl, cReq }) => {
    useEffect(() => {
        if (visible && acsUrl && cReq) {
            const form = document.createElement('form');
            form.method = 'POST';
            form.action = acsUrl;
            form.target = 'threeDSIframe';

            const input = document.createElement('input');
            input.type = 'hidden';
            input.name = 'creq';
            input.value = cReq;
            form.appendChild(input);

            console.log('Posting to:', acsUrl);
            console.log('cReq:', cReq);

            document.body.appendChild(form);

            setTimeout(() => {
                form.submit();
                document.body.removeChild(form);
            }, 0);
        }
    }, [visible, acsUrl, cReq]);

    return (
        <Modal
            title="3DS Authentication"
            open={visible}
            onCancel={onClose}
            footer={null}
            width={600}
        >
            <iframe
                name="threeDSIframe"
                title="3DS Challenge"
                style={{ width: '100%', height: '500px', border: 'none' }}
            />
        </Modal>
    );
};

export default ThreeDSModal;