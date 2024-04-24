import React, { useState, useEffect } from 'react';
import axios from 'axios';

function Users() {
    const [users, setUsers] = useState([]); // 사용자 데이터를 저장할 상태
    const [loading, setLoading] = useState(true); // 로딩 상태

    useEffect(() => {
        const fetchUsers = async () => {
            try {
                const response = await axios.get('http://localhost:8080/Users/users');
                setUsers(response.data); // 데이터를 상태에 저장
                setLoading(false); // 로딩 상태 업데이트
                console.log(response.data);
            } catch (error) {
                console.error('Error fetching data:', error);
                setLoading(false); // 에러 발생 시 로딩 상태 업데이트
            }
        };

        fetchUsers();
    }, []); // 컴포넌트가 마운트될 때 한 번만 실행

    if (loading) return <div>Loading...</div>; // 로딩 중 표시
    if (!users.length) return <div>No users found.</div>; // 사용자가 없을 때 표시

    return (
        <div>
            <h1>Users</h1>
            <ul>
                {users.map(user => (
                    <li key={user.id}>{user.name}</li> // 사용자 이름을 리스트로 렌더링
                ))}
            </ul>
        </div>
    );
}

export default Users;