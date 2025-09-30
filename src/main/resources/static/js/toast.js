/**
 * Toast Notification Module
 * alert() 대신 사용할 수 있는 부드러운 알림 시스템
 */

const Toast = {
    container: null,

    // 초기화
    init() {
        if (!this.container) {
            this.container = document.createElement('div');
            this.container.id = 'toast-container';
            document.body.appendChild(this.container);
        }
    },

    // 토스트 표시
    show(message, type = 'info', duration = 3000) {
        this.init();

        const toast = document.createElement('div');
        toast.className = `toast toast-${type} toast-enter`;

        const icon = this.getIcon(type);

        toast.innerHTML = `
            <span class="toast-icon">${icon}</span>
            <span class="toast-message">${message}</span>
            <button class="toast-close" onclick="Toast.close(this)">×</button>
        `;

        this.container.appendChild(toast);

        // 애니메이션 시작
        setTimeout(() => {
            toast.classList.remove('toast-enter');
            toast.classList.add('toast-show');
        }, 10);

        // 자동 닫기
        if (duration > 0) {
            setTimeout(() => {
                this.close(toast);
            }, duration);
        }

        return toast;
    },

    // 토스트 닫기
    close(element) {
        const toast = element.classList ? element : element.parentElement;
        toast.classList.remove('toast-show');
        toast.classList.add('toast-exit');

        setTimeout(() => {
            if (toast.parentElement) {
                toast.parentElement.removeChild(toast);
            }
        }, 300);
    },

    // 아이콘 가져오기
    getIcon(type) {
        const icons = {
            success: '✓',
            error: '✕',
            warning: '⚠',
            info: 'ℹ'
        };
        return icons[type] || icons.info;
    },

    // 편의 메서드
    success(message, duration = 3000) {
        return this.show(message, 'success', duration);
    },

    error(message, duration = 4000) {
        return this.show(message, 'error', duration);
    },

    warning(message, duration = 3500) {
        return this.show(message, 'warning', duration);
    },

    info(message, duration = 3000) {
        return this.show(message, 'info', duration);
    }
};

// confirm 대체 - 프로미스 기반 확인 다이얼로그
const Confirm = {
    show(message, title = '확인') {
        return new Promise((resolve) => {
            const overlay = document.createElement('div');
            overlay.className = 'confirm-overlay confirm-enter';

            overlay.innerHTML = `
                <div class="confirm-dialog">
                    <div class="confirm-header">
                        <h3>${title}</h3>
                    </div>
                    <div class="confirm-body">
                        <p>${message}</p>
                    </div>
                    <div class="confirm-footer">
                        <button class="btn btn-secondary confirm-cancel">취소</button>
                        <button class="btn btn-primary confirm-ok">확인</button>
                    </div>
                </div>
            `;

            document.body.appendChild(overlay);

            setTimeout(() => {
                overlay.classList.remove('confirm-enter');
                overlay.classList.add('confirm-show');
            }, 10);

            const close = (result) => {
                overlay.classList.remove('confirm-show');
                overlay.classList.add('confirm-exit');

                setTimeout(() => {
                    if (overlay.parentElement) {
                        overlay.parentElement.removeChild(overlay);
                    }
                    resolve(result);
                }, 200);
            };

            overlay.querySelector('.confirm-ok').onclick = () => close(true);
            overlay.querySelector('.confirm-cancel').onclick = () => close(false);
            overlay.onclick = (e) => {
                if (e.target === overlay) close(false);
            };
        });
    }
};

// 전역으로 노출
window.Toast = Toast;
window.Confirm = Confirm;