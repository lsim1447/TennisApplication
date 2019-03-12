import React , { useContext, useState, useEffect } from 'react';
import { get_request, post_request } from '../util/Request';
import styled from 'styled-components';
import { DEFALULT_SERVER_URL, TABLE_BACKGROUND } from '../constants';
import AutoCompleteTournament from '../components/autocomplete/AutoCompleteTournament';
import TournamentContextProvider from './../context-providers/TournamentContextProvider';
import { TournamentContext } from './../context-providers/TournamentContextProvider';
import { AppContext } from '../AppContextProvider';
import { Clickable, PlayerNameLink } from './../util/OftenUsedElements';

const NameTD = styled.td `
    width: 200px;
`;

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

const TableTournamentName = styled.div `
    border-left: 4px solid ${TABLE_BACKGROUND};
    border-right: 4px solid ${TABLE_BACKGROUND};
    border-top: 4px solid ${TABLE_BACKGROUND};
`;

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


    function selectYear(tournament){
        let sorted_matches = [];
        sorted_matches = tournamentContext.all_matches.filter(match => {
            return match.tournament.tournament_year_id === tournament.tournament_year_id
        });
        tournamentContext.changeLastMatches(sorted_matches);
    }

    function renderTournaments(){
        return tournamentContext.tournaments.slice(0, state.nrOfVisibleTournaments).map( tournament => {
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

    function renderSetResults(games){
        let index = games.length;
        while (index < 5){
            games.push(" ");
            index = index + 1;
        }
        return games.map((game, index) => {
            return (
                <td key={index} className="text-left">{game}</td>
            );
        })
    }

    function renderMatchResult(matches){
        return matches.slice(0,state.nrOfVisibleMatches).map(match => {
            let results = match.match_score_tiebreaks.split(" ");
            let player1 = [];
            let player2 = [];
            results.forEach(element => {
                if (element.indexOf('(') > -1){      // if was tiebreak in the set
                    const val = parseInt(element.charAt(3));
                    if (element.charAt(0) === '7'){
                        if (val >= 5){
                            player1.push(`7 (${val + 2})`)
                            player2.push(`6 (${val})`);
                        } else {
                            player1.push("7 (7)");
                            player2.push(`7 (${val})`);
                        }
                    } else {
                        if (val > 5){
                            player1.push(`6 (${val})`)
                            player2.push(`7 (${val+2})`);
                        } else {
                            player1.push(`6 (${val})`);
                            player2.push("7 (7)");
                        }
                    }
                } else {
                    player1.push(element.charAt(0));
                    player2.push(element.charAt(1));
                } 
            });
            return (
                <div key={match.match_id}>
                    <TableTournamentName className="font-weight-bold text-center">{match.tournament.tourney.name}, {match.tournament.year}, {match.round_name}</TableTournamentName>
                    <a href={`/match/stats/${match.match_id}`}>
                        <table className="table table-dark">
                            <tbody>
                                <tr>
                                    <NameTD className="font-weight-bold">{match.winnerPlayer.firstName} {match.winnerPlayer.lastName}</NameTD>
                                    { renderSetResults(player1) }
                                </tr>
                                <tr>
                                    <NameTD>{match.loserPlayer.firstName} {match.loserPlayer.lastName}</NameTD>
                                    { renderSetResults(player2) }
                                </tr>
                            </tbody>
                        </table>
                    </a>
                </div>
            )
        })
    }

    return (
        <div> 
            <TournamentSelectorContainer className="row justify-content-center">
                <TournamentSelectorCol className="col-4">
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

            <div className="row">
                <div className="col col-lg-6">
                    { renderMatchResult(tournamentContext.last_matches) }
                    <Clickable className="list-group-item list-group-item-action" onClick={() => increaseNrOfVisibleMatches(5)}>
                            <p className="mb-1 text-center font-weight-bold"> { context.locales[context.actual].show_more } </p>                                
                    </Clickable>
                </div>

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
            </div>
        </div>
    )
}

export default Tournament;