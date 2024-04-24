document.addEventListener('DOMContentLoaded', function() {
    const testBtn1 = document.getElementById('testBtn1');
    testBtn1.addEventListener('click', function() {
        fetch('/Users/users', {
            method:'GET'
        })
        .then(response => {
            if (!response.ok) { // HTTP 상태 코드가 200-299 범위에 있지 않은 경우
                    throw new Error('Network response was not ok: ' + response.statusText);
                }
            return response.json();
        })
        .then(data => {
            console.log(data);
            alert("name : " + data[0].name + ", email : " + data[0].email);
        })
        .catch(error => console.error('Error fetching data:', error))
    });
});

document.addEventListener('DOMContentLoaded', function() {
    const testBtn2 = document.getElementById('testBtn2');
    let userIdInput = document.getElementById('getUserByUserId');
    testBtn2.addEventListener('click', function() {
        fetch('/Users/users/' + userIdInput.value, {
            method : 'GET',
        })
        .then(response => {
            if(!response.ok) {
                throw new Error('유저 정보를 가져오지 못했습니다.');
            }
            return response.json();
        })
        .then(data => {
            console.log(data);
            alert("name : " + data.name);
        })
        .catch(error => console.log('Error : ', error))
    })
})

