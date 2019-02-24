export  function get_request(URL){
    return fetch(URL, {
            method: 'GET',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json; charset=UTF-8; '
            },
        })
        .then( (response) => response.json())
        .catch(error => console.warn(error));
}

export  function post_request(URL, data){
    return fetch(URL, {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json; charset=UTF-8; '
            },
            body: JSON.stringify(data)
        })
        .then( (response) => response.json())
        .catch(error => console.warn(error));
}