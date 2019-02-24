import React , { useContext, useState, useEffect } from 'react';
import { AppContext } from '../AppContextProvider';
import { get_request, post_request } from './../util/Request';

function Player(){
    
    const context =  useContext(AppContext);

    const [state, setState] = useState({
        players: []
    })

    const getWonTournaments = (player) => {
        console.log("player = ", player)
        post_request('http://localhost:8080/api/tournament/name', player)
            .then( result => {
                console.log('won tournament = ', result);
            })
    }

    useEffect(() => {
        get_request('http://localhost:8080//api/player/all')
            .then( response => {
                setState({...state, players: response});
            })

    }, []);

    return (
        <div>
            Number of players: {state.players.length}
            <button onClick={() => getWonTournaments(state.players[3042])}> POST - get Won Tournaments </button>
        </div>
    );
}

export default Player;