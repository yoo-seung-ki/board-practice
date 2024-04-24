import React, { useEffect } from 'react';
import axios from 'axios';

function Users() {
    useEffect(() => {
        const fetchUsers = async () => {
            try {
                const response = await axios.get('http://localhost:8080/api/users');
                console.log(response.data);
            } catch (error) {
                console.error('Error fetching data:', error);
            }
        };

        fetchUsers();
    }, []); // 빈 배열은 컴포넌트가 마운트될 때 이 함수를 한 번만 실행하라는 의미입니다.

    return (
        <div>
            <h1>Users</h1>
            {/* 여기에 사용자 데이터를 렌더링할 수 있습니다. */}
        </div>
    );
}

export default Users;
