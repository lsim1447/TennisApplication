import React , { useContext, useState, useEffect } from 'react';
import { AppContext } from '../AppContextProvider';
import { get_request, post_request } from '../util/Request';
import { DEFALULT_SERVER_URL } from '../constants';
import { Clickable, PlayerNameLink } from './../util/OftenUsedElements';
import styled from 'styled-components';
import Matches from './../util/elements/Matches';
import { getGrandSlamsFromMatches } from './../util/FunctionUtil';

const TournamentName = styled.div `
    text-align: center;
    font-size: 54px;
    padding-top: 40px;
    padding-bottom: 40px;

    @media (max-width: 768px) {
        font-size: 32px;
        padding-top: 24px;
        padding-bottom: 24px;
    }
`;

function CustomTournament(props){
    
    const context =  useContext(AppContext);

    const [state, setState] = useState({
        tournament: {},
        tournaments: [],
        nrOfVisibleTournaments: 8,
        nrOfVisibleMatches: 6,
        matches: []
    })

    useEffect(() => {
        get_request(`${DEFALULT_SERVER_URL}/tournament/one?id=${encodeURIComponent(props.match.params.id)}`)
            .then(tournament => {
                post_request(`${DEFALULT_SERVER_URL}/tournament/tourney`, tournament.tourney)
                    .then( tournaments => {
                        get_request(`${DEFALULT_SERVER_URL}/matches/tournament?id=${encodeURIComponent(tournament.tournament_year_id)}`)
                            .then( matches => {
                                setState({...state, tournament: tournament, tournaments: tournaments.reverse(), matches: matches});
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


    function selectYear(tournament){
        get_request(`${DEFALULT_SERVER_URL}/matches/tournament?id=${encodeURIComponent(tournament.tournament_year_id)}`)
            .then( matches => {
                setState({...state, matches: matches});
            })
    }

    function renderTournaments(){
        return state.tournaments.slice(0, state.nrOfVisibleTournaments).map( tournament => {
            return (
                <Clickable href="#" className="list-group-item list-group-item-action" key={tournament.tournament_year_id} onClick={() => selectYear(tournament)}>
                    <div className="d-flex w-100 justify-content-between">
                        <h5 className="mb-1">{tournament.tourney.name}</h5>
                        <h5 className="">{tournament.year}</h5>
                    </div>

                    <p className="mb-1"> { context.locales[context.actual].winner_player } <PlayerNameLink className="mb-1" href={`/players/${tournament.player.playerSlug}`}><strong> {tournament.player.firstName}  {tournament.player.lastName}</strong></PlayerNameLink></p>
                    
                    <small> { context.locales[context.actual].tournament_location } {tournament.tourney.location}</small>
                </Clickable>
            )
        })
    }

    return (
        <div className="container">
            <TournamentName className="mb-1 font-weight-bold"> { state.tournament.tourney ? state.tournament.tourney.name : ''} </TournamentName>                     
            <div className="row">
                <div className="col-xl-6">
                    <div className="list-group">
                        <a href="#" className="list-group-item list-group-item-action">
                            <h1 className="mb-1 text-center font-weight-bold"> { context.locales[context.actual].former_champions } </h1>                     
                        </a>
                        {renderTournaments()}
                        <Clickable className="list-group-item list-group-item-action" onClick={() => increaseNrOfVisibleTournaments(5)}>
                            <p className="mb-1 text-center font-weight-bold"> { context.locales[context.actual].show_more } </p>                                
                        </Clickable>
                    </div>
                </div>

                <div className="col col-lg-6">
                    <Matches
                        matches = { state.matches }
                        grand_slam_matches = { getGrandSlamsFromMatches(state.matches) }
                        selectedPlayerOne = {{}}
                        selectedPlayerTwo = {{}}
                        nrOfVisibleMatches = { state.nrOfVisibleMatches }
                        same = {1}
                        isTour = {1}
                    />
                    <Clickable className="list-group-item list-group-item-action" onClick={() => increaseNrOfVisibleMatches(5)}>
                            <p className="mb-1 text-center font-weight-bold"> { context.locales[context.actual].show_more } </p>                                
                    </Clickable>
                </div>
            </div>
        </div>
    );
}

export default CustomTournament;