import React , { useContext, useState, useEffect } from 'react';
import { get_request } from '../util/Request';
import styled from 'styled-components';
import { DEFALULT_SERVER_URL } from '../constants';
import AutoCompleteTournament from '../components/autocomplete/AutoCompleteTournament';
import { TournamentContext } from './../context-providers/TournamentContextProvider';
import { AppContext } from '../AppContextProvider';
import { Clickable } from './../util/OftenUsedElements';
import Matches from './../util/elements/Matches';
import { renderTournaments } from './../util/renders/TournamentResults';
import { getGrandSlamsFromMatches } from './../util/FunctionUtil';

const TournamentSelectorContainer = styled.div `
    padding-top: 50px;
    padding-bottom: 50px;
    background-image: url('https://www.tennisworldusa.org/imgb/40797/if-tennis-players-were-game-of-thrones-characters.jpg')
`;

const TournamentSelectorCol = styled.div `
    @media (max-width: 1200px) {
        flex: none;
        max-width: 50%;
    }

    @media (max-width: 768px) {
        flex: none;
        max-width: 100%;
    }
`;

function Tournament(props) {
    
    const context =  useContext(AppContext);
    const tournamentContext = useContext(TournamentContext);

    const [state, setState] = useState({
        tourneys: [],
        tournament: {},
        tournaments: [],
        nrOfVisibleTournaments: 8,
        nrOfVisibleMatches: 8,
        matches: []
    })

    useEffect(() => {
        get_request(`${DEFALULT_SERVER_URL}/tourney/all`)
            .then(tourneys => {
                setState({...state, tourneys: tourneys})
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
            <TournamentSelectorContainer className="row justify-content-center">
                <TournamentSelectorCol className="col-lg-6">
                    <AutoCompleteTournament 
                        items={ state.tourneys } 
                        placeholder={ "Please select a tournament" } 
                        changeSelectedTourney={ tournamentContext ? tournamentContext.changeSelectedTourney : null }
                        changeTournaments={ tournamentContext ? tournamentContext.changeTournaments : null }
                        changeLastMatches={ tournamentContext ? tournamentContext.changeLastMatches : null }
                        changeAllMatches={ tournamentContext ? tournamentContext.changeAllMatches : null }
                    />
                </TournamentSelectorCol>
            </TournamentSelectorContainer> 

            <div className="container">
                <div className="row">
                    <div className="col col-lg">
                        <div className="list-group">
                            <a href="#" className="list-group-item list-group-item-action">
                                <h1 className="mb-1 text-center font-weight-bold"> { context.locales[context.actual].former_champions } </h1>                     
                            </a>
                            { renderTournaments(tournamentContext.tournaments, state.nrOfVisibleTournaments, context.locales[context.actual].winner_player, context.locales[context.actual].tournament_location) }
                            <Clickable className="list-group-item list-group-item-action" onClick={() => increaseNrOfVisibleTournaments(5)}>
                                <p className="mb-1 text-center font-weight-bold"> { context.locales[context.actual].show_more } </p>                                
                            </Clickable>
                        </div>
                    </div>

                    <div className="col col-lg">
                        <a href="#" className="list-group-item list-group-item-action">
                            <h1 className="mb-1 text-center font-weight-bold"> { context.locales[context.actual].latest_matches } </h1>                     
                        </a>
                        <Matches
                            matches = { tournamentContext.last_matches }
                            grand_slam_matches = { getGrandSlamsFromMatches(tournamentContext.last_matches) }
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
            
        </div>
    )
}

export default Tournament;