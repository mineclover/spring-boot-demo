$(document).ready(function () {
    let currentUser = null;

    // 초기화
    loadUsers();
    loadCurrentUser();
    loadPosts();

    // 사용자 목록 로드
    function loadUsers() {
        $.get('/session/users', function (users) {
            $('#userList').empty();
            users.forEach(function (user) {
                const badge = user.role === 'ADMIN' ? '<span style="color:#f39c12">★</span>' : '';
                $('#userList').append(`
                    <div class="user-card" data-id="${user.id}">
                        <h3>${badge} ${user.username}</h3>
                        <p>${user.email}</p>
                        <p style="font-size:0.8rem;margin-top:0.3rem">${user.role}</p>
                    </div>
                `);
            });

            // 클릭 이벤트
            $('.user-card').click(function () {
                const userId = $(this).data('id');
                login(userId);
            });
        });
    }

    // 로그인
    function login(userId) {
        $.ajax({
            url: '/session/login',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ userId: userId }),
            success: function (response) {
                if (response.success) {
                    currentUser = response.user;
                    updateUI();
                    loadPersonalInfo();
                    loadPosts();
                    Toast.success(response.message);
                }
            }
        });
    }

    // 로그아웃
    $('#logoutBtn').click(function () {
        $.post('/session/logout', function (response) {
            currentUser = null;
            updateUI();
            loadPosts();
            Toast.info(response.message);
        });
    });

    // 현재 사용자 정보 로드
    function loadCurrentUser() {
        $.get('/session/current', function (response) {
            if (response.loggedIn) {
                currentUser = response.user;
                updateUI();
                loadPersonalInfo();
            }
        });
    }

    // UI 업데이트
    function updateUI() {
        $('.user-card').removeClass('active');

        if (currentUser) {
            $(`.user-card[data-id="${currentUser.id}"]`).addClass('active');
            $('#loginStatus').text(`${currentUser.username} (${currentUser.role})`);
            $('#logoutBtn').show();
            $('#postForm').show();
            $('#loginRequired').hide();

            // 관리자인 경우 복원 버튼 표시
            if (currentUser.role === 'ADMIN') {
                $('#restoreDemoBtn').show();
            } else {
                $('#restoreDemoBtn').hide();
            }
        } else {
            $('#loginStatus').text('로그인되지 않음');
            $('#logoutBtn').hide();
            $('#personalInfo').html('<p class="no-data">로그인 후 확인 가능합니다</p>');
            $('#postForm').hide();
            $('#loginRequired').show();
            $('#restoreDemoBtn').hide();
        }
    }

    // 개인 정보 로드
    function loadPersonalInfo() {
        $.get('/session/my-info', function (response) {
            if (response.error) {
                $('#personalInfo').html(`<p class="no-data">${response.error}</p>`);
                return;
            }

            const user = response.user;
            const privateData = response.privateData;

            $('#personalInfo').html(`
                <div class="info-item"><strong>사용자명:</strong>${user.username}</div>
                <div class="info-item"><strong>이메일:</strong>${user.email}</div>
                <div class="info-item"><strong>권한:</strong>${user.role}</div>
                <div class="info-item"><strong>세션 ID:</strong>${privateData.sessionId}</div>
                <div class="info-item"><strong>로그인 시간:</strong>${new Date(privateData.loginTime).toLocaleString()}</div>
                <div class="info-item" style="background:#fff3cd;padding:0.8rem;border-radius:5px;margin-top:0.8rem">
                    <strong>🔒 비밀 정보:</strong>${privateData.secretInfo}
                </div>
            `);
        });
    }

    // 게시글 목록 로드
    function loadPosts() {
        $.get('/session/posts', function (posts) {
            $('#postList').empty();

            if (posts.length === 0) {
                $('#postList').html('<p class="no-data">게시글이 없습니다</p>');
                return;
            }

            posts.forEach(function (post) {
                const canDelete = currentUser
                    && (post.authorId === currentUser.id || currentUser.role === 'ADMIN');

                const deleteBtn = canDelete
                    ? `<button class="btn btn-delete" onclick="deletePost(${post.id})">삭제</button>` : '';

                const authorBadge = post.authorName === '관리자' ? '<span style="color:#f39c12">★</span>' : '';

                $('#postList').append(`
                    <div class="post-card">
                        <div class="post-header">
                            <div>
                                <div class="post-title">${post.title}</div>
                                <div class="post-meta">
                                    <span>${authorBadge} ${post.authorName}</span>
                                    <span class="post-time">${formatDate(post.createdAt)}</span>
                                </div>
                            </div>
                            ${deleteBtn}
                        </div>
                        <div class="post-content">${post.content}</div>
                    </div>
                `);
            });
        });
    }

    // 게시글 작성
    $('#submitPost').click(function () {
        const title = $('#postTitle').val().trim();
        const content = $('#postContent').val().trim();

        if (!title || !content) {
            Toast.warning('제목과 내용을 입력하세요');
            return;
        }

        $.ajax({
            url: '/session/posts',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ title, content }),
            success: function (response) {
                if (response.success) {
                    $('#postTitle').val('');
                    $('#postContent').val('');
                    loadPosts();
                    Toast.success(response.message);
                } else {
                    Toast.error(response.message);
                }
            }
        });
    });

    // 게시글 삭제 (전역 함수)
    window.deletePost = async function (postId) {
        const confirmed = await Confirm.show('정말 삭제하시겠습니까?', '게시글 삭제');
        if (!confirmed) return;

        $.ajax({
            url: `/session/posts/${postId}`,
            method: 'DELETE',
            success: function (response) {
                if (response.success) {
                    Toast.success(response.message);
                    loadPosts();
                } else {
                    Toast.error(response.message);
                }
            }
        });
    };

    // 날짜 포맷
    function formatDate(dateString) {
        const date = new Date(dateString);
        const now = new Date();
        const diff = Math.floor((now - date) / 1000);

        if (diff < 60) return '방금 전';
        if (diff < 3600) return Math.floor(diff / 60) + '분 전';
        if (diff < 86400) return Math.floor(diff / 3600) + '시간 전';
        return date.toLocaleDateString();
    }

    // 데모 게시글 복원
    $('#restoreDemoBtn').click(async function () {
        const confirmed = await Confirm.show('데모 게시글을 복원하시겠습니까?', '데모 복원');
        if (!confirmed) return;

        $.ajax({
            url: '/session/posts/restore-demo',
            method: 'POST',
            success: function (response) {
                if (response.success) {
                    Toast.success(response.message);
                    loadPosts();
                } else {
                    Toast.error(response.message);
                }
            }
        });
    });
});
