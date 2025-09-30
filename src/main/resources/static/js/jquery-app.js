// jQuery 상태 관리 패턴 - Global State Object
var AppState = {
    users: [],
    stats: { totalUsers: 0, activeUsers: 0, newToday: 0 },
    loading: false,
    filter: 'all', // 'all', 'active', 'new'

    // 상태 업데이트 및 렌더링
    setState: function(newState) {
        $.extend(this, newState);
        this.render();
    },

    // 상태 기반 렌더링
    render: function() {
        // 통계 렌더링
        $('#totalUsers').text(this.stats.totalUsers || '-');
        $('#activeUsers').text(this.stats.activeUsers || '-');
        $('#newToday').text(this.stats.newToday || '-');

        // 필터 버튼 상태
        $('.filter-btn').removeClass('active');
        $('.filter-btn[data-filter="' + this.filter + '"]').addClass('active');

        // 사용자 목록 렌더링
        this.renderUsers();

        // 상태 디버거
        $('#state-debug').text(JSON.stringify({
            userCount: this.users.length,
            filter: this.filter,
            loading: this.loading
        }, null, 2));
    },

    // 필터링된 사용자 목록 렌더링
    renderUsers: function() {
        var filteredUsers = this.getFilteredUsers();
        $('#userTableBody').empty();

        if (filteredUsers.length === 0) {
            $('#userTableBody').html('<tr><td colspan="4" style="text-align: center;">사용자가 없습니다.</td></tr>');
            return;
        }

        filteredUsers.forEach(function(user) {
            var row = '<tr>' +
                '<td>' + user.id + '</td>' +
                '<td>' + user.name + '</td>' +
                '<td>' + user.email + '</td>' +
                '<td>' + user.age + '</td>' +
                '</tr>';
            $('#userTableBody').append(row);
        });

        $('#filter-count').text('(' + filteredUsers.length + '명)');
    },

    // 필터 적용
    getFilteredUsers: function() {
        if (this.filter === 'all') return this.users;
        if (this.filter === 'active') return this.users.filter(u => u.age >= 18);
        if (this.filter === 'new') return this.users.slice(0, 3);
        return this.users;
    }
};

$(document).ready(function() {
    // 초기 데이터 로드
    loadStats();
    loadUsers();

    // 통계 로드 (상태 업데이트)
    function loadStats() {
        $.ajax({
            url: '/api/stats',
            method: 'GET',
            success: function(data) {
                AppState.setState({ stats: data });
            },
            error: function() {
                console.error('통계 데이터 로드 실패');
            }
        });
    }

    // 사용자 목록 로드 (상태 업데이트)
    function loadUsers() {
        AppState.setState({ loading: true });
        $('#loading').show();

        $.ajax({
            url: '/api/users',
            method: 'GET',
            success: function(users) {
                $('#loading').hide();
                AppState.setState({
                    users: users,
                    loading: false
                });
            },
            error: function() {
                $('#loading').hide();
                AppState.setState({ loading: false });
                showMessage('사용자 목록 로드에 실패했습니다.', 'error');
            }
        });
    }

    // 폼 제출
    $('#userForm').on('submit', function(e) {
        e.preventDefault();

        var userData = {
            name: $('#name').val(),
            email: $('#email').val(),
            age: parseInt($('#age').val())
        };

        $.ajax({
            url: '/api/users',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(userData),
            success: function(response) {
                showMessage(response.message, 'success');
                $('#userForm')[0].reset();
                loadUsers();
                loadStats();
            },
            error: function() {
                showMessage('사용자 추가에 실패했습니다.', 'error');
            }
        });
    });

    // 새로고침 버튼
    $('#refreshBtn').on('click', function() {
        loadUsers();
        loadStats();
    });

    // 필터 버튼 이벤트
    $('.filter-btn').on('click', function() {
        var filter = $(this).data('filter');
        AppState.setState({ filter: filter });
    });

    // 메시지 표시
    function showMessage(text, type) {
        var $message = $('#message');
        $message.text(text);
        $message.removeClass('success error');
        $message.addClass(type);

        setTimeout(function() {
            $message.fadeOut(function() {
                $message.hide().css('display', '');
            });
        }, 3000);
    }
});