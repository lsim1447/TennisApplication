import React , { useContext, useState, useEffect } from 'react';
import { get_request } from '../util/Request';
import styled from 'styled-components';
import { DEFALULT_SERVER_URL } from '../constants';
import AutoCompletePlayer from '../components/autocomplete/AutoCompletePlayer';
import { PlayerContext } from './../context-providers/PlayerContextProvider';
import { AppContext } from '../AppContextProvider';
import { Clickable, PlayerCardImage } from './../util/OftenUsedElements';
import Matches from './../util/elements/Matches';
import { getGrandSlamsFromMatches } from './../util/FunctionUtil';
import { renderTournaments } from './../util/renders/TournamentResults';
import PlayerInfoTable from '../util/elements/PlayerInfoTable';

const PlayerSelectorContainer = styled.div `
    padding-top: 50px;
    padding-bottom: 50px;
    background-image: url('https://www.tennisworldusa.org/imgb/40797/if-tennis-players-were-game-of-thrones-characters.jpg')
`;

const PlayerSelectorCol = styled.div `
    @media (max-width: 1200px) {
        flex: none;
        max-width: 50%;
    }

    @media (max-width: 768px) {
        flex: none;
        max-width: 100%;
    }
`;

const TournamentsContainer = styled.div `

    @media (min-width: 992px) {
        padding-left: 15% ;
        padding-right: 15%;
    }
`;

function Player(props){
    
    const context =  useContext(AppContext);
    const playerContext = useContext(PlayerContext);
    
    const [state, setState] = useState({
        players: [],
        nrOfVisibleTournaments: 5,
        nrOfVisibleMatches: 5,
    })

    useEffect(() => {
        get_request(`${DEFALULT_SERVER_URL}/player/all`)
            .then(players => {
                setState({...state, players: players})
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
        <div>
            <PlayerSelectorContainer className="row justify-content-center">
                <PlayerSelectorCol className="col-4">
                    <AutoCompletePlayer 
                        items={state.players} 
                        placeholder={"Please select a player"} 
                        changeSelectedPlayer={playerContext ? playerContext.changeSelectedPlayer : null}
                        changeTournaments={ playerContext ? playerContext.changeTournaments : null }
                        changeGrandSlams={ playerContext ? playerContext.changeGrandSlams : null }
                        changeLastMatches={ playerContext ? playerContext.changeLastMatches : null}
                    />
                </PlayerSelectorCol>
            </PlayerSelectorContainer>
            <div className="row">
                <div className="col col-lg-3">
                    <div className="card">
                        <PlayerCardImage src={`./../images/players/${playerContext.selectedPlayer.playerSlug}.jpg`} className="card-img" alt="" onError={(e)=>{e.target.onerror = null; e.target.src="./../../images/players/unknown.jpg"}}/>
                        <div className="card-body">
                            <PlayerInfoTable 
                                selectedPlayer = { playerContext.selectedPlayer }
                                tournaments    = { playerContext.tournaments }
                                grand_slams    = { playerContext.grand_slams }
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

                <div className="col col-lg">
                    <TournamentsContainer className="list-group">
                        <a className="list-group-item list-group-item-action">
                            <h1 className="mb-1 text-center font-weight-bold"> { context.locales[context.actual].latest_won_tournaments } </h1>                     
                        </a>
                        { renderTournaments(playerContext.tournaments, state.nrOfVisibleTournaments, context.locales[context.actual].winner_player, context.locales[context.actual].tournament_location) }
                        <Clickable className="list-group-item list-group-item-action" onClick={() => increaseNrOfVisibleTournaments(5)}>
                            <p className="mb-1 text-center font-weight-bold"> { context.locales[context.actual].show_more } </p>                                
                        </Clickable>
                    </TournamentsContainer>
                </div>

                <div className="col col-lg-3">
                    <a href="#" className="list-group-item list-group-item-action">
                        <h1 className="mb-1 text-center font-weight-bold"> { context.locales[context.actual].latest_matches } </h1>                     
                    </a>
                    <Matches
                        matches = { playerContext.last_matches }
                        grand_slam_matches = { getGrandSlamsFromMatches(playerContext.last_matches) }
                        selectedPlayerOne = { playerContext.selectedPlayer }
                        selectedPlayerTwo = { playerContext.selectedPlayer }
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

export default Player;