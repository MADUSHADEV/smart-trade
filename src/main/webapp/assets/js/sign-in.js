async function signIn() {
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    const user_DTO = {
        email: email,
        password: password
    }
    const response = await fetch('signIn', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(user_DTO)
    });
    if (!response.ok) {
        throw new Error("Error");
    }
    const data = await response.json();
    if (data.success) {
        window.location.href = "index.html";
    } else {
        alert(data.message);
    }
}