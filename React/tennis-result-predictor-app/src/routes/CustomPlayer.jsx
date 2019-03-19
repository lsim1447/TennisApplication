import React , { useContext, useState, useEffect } from 'react';
import { AppContext } from '../AppContextProvider';
import { get_request, post_request } from '../util/Request';
import { DEFALULT_SERVER_URL, VISIBLE_MATCHES } from '../constants';
import styled from 'styled-components';
import { Clickable, PlayerCardImage } from './../util/OftenUsedElements';
import Matches from './../util/elements/Matches';
import { getGrandSlamsFromMatches } from './../util/FunctionUtil';
import { renderTournaments } from './../util/renders/TournamentResults';
import PlayerInfoTable from '../util/elements/PlayerInfoTable';

function CustomPlayer(props){
    
    const context =  useContext(AppContext);

    const [state, setState] = useState({
        nrOfVisibleTournaments: 5,
        nrOfVisibleMatches: 8,
        player: {},
        tournaments: [],
        grand_slams: [],
        last_matches: []
    })

    useEffect(() => {
        get_request(`${DEFALULT_SERVER_URL}/player/one?slug=${encodeURIComponent(props.match.params.slug)}`)
            .then(player => {
                get_request(`${DEFALULT_SERVER_URL}/tournament/won?slug=${encodeURIComponent(props.match.params.slug)}`)
                    .then(tournaments => {
                        get_request(`${DEFALULT_SERVER_URL}/matches/player/last?slug=${encodeURIComponent(props.match.params.slug)}&nr=${encodeURIComponent(VISIBLE_MATCHES)}`)
                            .then(lastmatches => {
                                const won_grand_slams = tournaments.filter(tournament => {
                                    return (tournament.tourney.tourney_id === '520') || (tournament.tourney.tourney_id === '540') || (tournament.tourney.tourney_id === '560') || (tournament.tourney.tourney_id === '580');
                                })
                                setState({...state, tournaments: tournaments.reverse(), grand_slams: won_grand_slams, player: player, last_matches: lastmatches});
                              })
                    })
            })
    }, []);

    function increaseNrOfVisibleTournaments(value){
        const tmp_nr_of_visible_tournaments = state.nrOfVisibleTournaments + value;
        setState({...state, nrOfVisibleTournaments: tmp_nr_of_visible_tournaments})
    }

    function increaseNrOfVisibleMatches(value){
        const tmp_nr_of_visible_matches = state.nrOfVisibleMatches + value;
        setState({...state, nrOfVisibleMatches: tmp_nr_of_visible_matches})
    }

    return (
        <div className="">
            <div className="row">
                <div className="col-lg-3">
                    <div className="card">
                        <PlayerCardImage src={`./../images/players/${state.player.playerSlug}.jpg`} className="card-img" alt="" onError={(e)=>{e.target.onerror = null; e.target.src="./../../images/players/unknown.jpg"}}/>
                        <div className="card-body">
                            <PlayerInfoTable 
                                selectedPlayer = { state.player }
                                tournaments    = { state.tournaments }
                                grand_slams    = { state.grand_slams }
                                locales        = { {
                                    country:         context.locales[context.actual].country,
                                    birthdate:       context.locales[context.actual].birthdate,
                                    handedness:      context.locales[context.actual].handedness,
                                    won_tournaments: context.locales[context.actual].won_tournaments,
                                    won_grand_slams: context.locales[context.actual].won_grand_slams,
                                    height:          context.locales[context.actual].height,
                                    turned_pro:      context.locales[context.actual].turned_pro,
                                    weight_kg:       context.locales[context.actual].weight_kg,
                                    weight_lbs:      context.locales[context.actual].weight_lbs
                                }}
                            />    
                        </div>
                    </div>
                </div>

                <div className="col">
                    <div className="list-group">
                        <a className="list-group-item list-group-item-action">
                            <h1 className="mb-1 text-center font-weight-bold"> { context.locales[context.actual].latest_won_tournaments } </h1>                     
                        </a>
                        { renderTournaments(state.tournaments, state.nrOfVisibleTournaments, context.locales[context.actual].winner_player, context.locales[context.actual].tournament_location) }
                        <Clickable className="list-group-item list-group-item-action" onClick={() => increaseNrOfVisibleTournaments(5)}>
                            <p className="mb-1 text-center font-weight-bold"> { context.locales[context.actual].show_more } </p>                                
                        </Clickable>
                    </div>
                </div>

                <div className="col col-lg-3">
                    <a href="#" className="list-group-item list-group-item-action">
                        <h1 className="mb-1 text-center font-weight-bold"> { context.locales[context.actual].latest_matches } </h1>                     
                    </a>
                    <Matches
                        matches = { state.last_matches }
                        grand_slam_matches = { getGrandSlamsFromMatches(state.last_matches) }
                        selectedPlayerOne = { state.player }
                        selectedPlayerTwo = { state.player }
                        nrOfVisibleMatches = { state.nrOfVisibleMatches }
                        same = {1}
                    />
                    <Clickable className="list-group-item list-group-item-action" onClick={() => increaseNrOfVisibleMatches(5)}>
                            <p className="mb-1 text-center font-weight-bold"> { context.locales[context.actual].show_more } </p>                                
                    </Clickable>
                </div>
            </div>
        </div>
    );
}

export default CustomPlayer;