import React , { useContext, useState, useEffect } from 'react';
import { AppContext } from '../AppContextProvider';
import { get_request } from '../util/Request';
import { DEFALULT_SERVER_URL } from '../constants';

function Tournament(props) {
    
    const context =  useContext(AppContext);

    const [state, setState] = useState({
        tournaments: []
    })

    useEffect(() => {
        get_request(`${DEFALULT_SERVER_URL}/tournament/all`)
            .then(tournaments => {
                setState({...state, tournaments: tournaments})
            })
    }, []);

    return (
        <div> 
              Number of tournaments in Database: {state.tournaments.length}  
        </div>
    )
}

export default Tournament;