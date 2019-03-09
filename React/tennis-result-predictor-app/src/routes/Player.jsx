import React , { useContext, useState, useEffect } from 'react';
import { AppContext } from '../AppContextProvider';
import { get_request } from '../util/Request';
import { DEFALULT_SERVER_URL } from '../constants';


function Player(props){
    
    const context =  useContext(AppContext);

    const [state, setState] = useState({
        players: []
    })

    useEffect(() => {
        get_request(`${DEFALULT_SERVER_URL}/player/all`)
            .then(players => {
                setState({...state, players: players})
            })
    }, []);

    return (
        <div>
           Number of players in Database: {state.players.length}
        </div>
    );
}

export default Player;