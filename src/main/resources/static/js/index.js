document.addEventListener('DOMContentLoaded', function() {
    const testBtn = document.getElementById('testBtn');
    testBtn.addEventListener('click', function() {
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
        })
        .catch(error => console.error('Error fetching data:', error))
    });
});


