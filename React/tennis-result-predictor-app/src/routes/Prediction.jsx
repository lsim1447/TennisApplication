import React , { useContext, useState, useEffect } from 'react';
import { get_request } from '../util/Request';
import styled from 'styled-components';
import { DEFALULT_SERVER_URL } from '../constants';
import AutoCompletePredicter from '../components/autocomplete/AutoCompletePredicter';
import { PredicterContext } from './../context-providers/PredicterContextProvider';
import { AppContext } from '../AppContextProvider';
import { TableTournamentName } from './../util/OftenUsedElements';
import StatProgressBar from './../components/StatProgressBar';
import { isGrandSlam, wonMatchesBy, wonMatchesOn } from './../util/FunctionUtil';

const NameTD = styled.td `
    width: 200px;
`;

const HeaderDiv = styled.div `
    height: 250px;
    background-image: url('./../images/others/background1.jpg');
    background-repeat: no-repeat;
    margin-bottom: 25px;
`;

const CardDiv = styled.div `
    margin-top: 15px;
`;

const MatchesContainer = styled.div `
    padding-top: 15px;
    background-image: url('./../images/others/light-background.jpg');
`;

const PlayerCard = styled.div `

`;

const PlayerCardsContainer = styled.div `

`;

const StatisticsCardsContainer = styled.div `
    border: 1px solid rgba(0,0,0,.125);
    padding-left: 24px;
    padding-right: 24px;
    padding-bottom: 24px;
`;

const StatisticsH1 = styled.h1 `
    text-align: center;
    padding-top: 12px;
`;

function Prediction(props){
    
    const contextPredicter =  useContext(PredicterContext);
    const context =  useContext(AppContext);

    const [state, setState] = useState({
        players: [],
        nrOfVisibleMatches: 100,
    })

    function renderCardPlayerName(player){
        if (player.firstName){
            return (
                <h5 className="card-title">
                    {player.firstName} {player.lastName} ({player.flagCode})
                </h5>
            )
        } else {
            return (
                <h5 className="card-title">
                    Player name (Flag Code)
                </h5>
            )
        }
    }

    function renderBirthDate(player){
        if (player.firstName){
            return (
                <p className="card-text">
                    {player.birthdate}
                </p>
            )
        } else {
            return (
                <p className="card-text">
                    YYYY.MM.DD
                </p>
            )
        }
    }

    function renderAge(player){
        if (player.firstName){
            const birthdate = new Date(player.birthdate.replace(".", "-"));
            const ageDifMs = Date.now() - birthdate.getTime();
            const ageDate = new Date(ageDifMs);
            const age = Math.abs(ageDate.getUTCFullYear() - 1970);
            return (
                <p className="card-text">
                    { age }
                </p>
            )
        } else {
            return (
                <p className="card-text">
                    - 
                </p>
            )
        }
    }

    function renderNrOfWonTournaments(tournaments){
        if (tournaments.length > 0){
            return (
                <p className="card-text">
                    {tournaments.length}
                </p>
            )
        } else {
            return (
                <p className="card-text">
                    0
                </p>
            )
        }
    }

    function renderNrOfWonGrandSlams(tournaments){
        return  tournaments.filter(tournament => {
            return (tournament.tourney.tourney_id === '520') || (tournament.tourney.tourney_id === '540') || (tournament.tourney.tourney_id === '560') || (tournament.tourney.tourney_id === '580');
        }).length;
    }

    function renderStats(matches){
        if (contextPredicter.selectedPlayerOne.firstName && contextPredicter.selectedPlayerTwo.firstName &&
            contextPredicter.selectedPlayerOne.firstName !== contextPredicter.selectedPlayerTwo.firstName){
            return(
                <div>
                    {renderMatchResult(matches)}
                </div>
            )
        }
    }

    useEffect(() => {
        get_request(`${DEFALULT_SERVER_URL}/player/all`)
            .then(players => {
                setState({...state, players: players})
            })
    }, []);

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

    function renderStatistics(){
        if (!contextPredicter.matches_between_those_two || contextPredicter.matches_between_those_two.length == 0) return;

        const to_progress = [];

        const player_one_won_matches = wonMatchesBy(contextPredicter.matches_between_those_two, contextPredicter.selectedPlayerOne);
        const player_two_won_matches = wonMatchesBy(contextPredicter.matches_between_those_two, contextPredicter.selectedPlayerTwo);
        to_progress.push({
            title: 'Won Tournaments',
            firstPlayerValue:  contextPredicter.tournaments_player_one.length,
            firstPlayerTotal:  contextPredicter.tournaments_player_one.length + contextPredicter.tournaments_player_two.length,
            secondPlayerValue: contextPredicter.tournaments_player_two.length,
            secondPlayerTotal: contextPredicter.tournaments_player_one.length + contextPredicter.tournaments_player_two.length,
        });

        const p1_gr_sl = contextPredicter.tournaments_player_one.filter(t => isGrandSlam(t));
        const p2_gr_sl = contextPredicter.tournaments_player_two.filter(t => isGrandSlam(t));
        to_progress.push({
            title: 'Won Grands Slams',
            firstPlayerValue:  p1_gr_sl.length,
            firstPlayerTotal:  p1_gr_sl.length + p2_gr_sl.length,
            secondPlayerValue: p2_gr_sl.length,
            secondPlayerTotal: p1_gr_sl.length + p2_gr_sl.length,
        });

        to_progress.push({
            title: 'Duels',
            firstPlayerValue:  player_one_won_matches.length,
            firstPlayerTotal:  player_one_won_matches.length + player_two_won_matches.length,
            secondPlayerValue: player_two_won_matches.length,
            secondPlayerTotal: player_one_won_matches.length + player_two_won_matches.length,
        });

        const player_one_won_matches_on_gr = player_one_won_matches.filter(t => isGrandSlam(t.tournament));
        const player_two_won_matches_on_gr = player_two_won_matches.filter(t => isGrandSlam(t.tournament));
        to_progress.push({
            title: 'Duels on Grand Slam',
            firstPlayerValue:  player_one_won_matches_on_gr.length,
            firstPlayerTotal:  player_one_won_matches_on_gr.length + player_two_won_matches_on_gr.length,
            secondPlayerValue: player_two_won_matches_on_gr.length,
            secondPlayerTotal: player_one_won_matches_on_gr.length + player_two_won_matches_on_gr.length,
        });


        const player_one_won_matches_on_clay = wonMatchesOn(player_one_won_matches, 'Clay');
        const player_two_won_matches_on_clay = wonMatchesOn(player_two_won_matches, 'Clay');
        const all_matches_on_clay = wonMatchesOn(contextPredicter.matches_between_those_two, 'Clay');
        to_progress.push({
            title: 'Duels on Clay',
            firstPlayerValue:  player_one_won_matches_on_clay.length,
            firstPlayerTotal:  all_matches_on_clay.length,
            secondPlayerValue: player_two_won_matches_on_clay.length,
            secondPlayerTotal: all_matches_on_clay.length,
        });

        const player_one_won_matches_on_grass = wonMatchesOn(player_one_won_matches, 'Grass');
        const player_two_won_matches_on_grass = wonMatchesOn(player_two_won_matches, 'Grass');
        const all_matches_on_grass = wonMatchesOn(contextPredicter.matches_between_those_two, 'Grass');
        to_progress.push({
            title: 'Duels on Grass',
            firstPlayerValue:  player_one_won_matches_on_grass.length,
            firstPlayerTotal:  all_matches_on_grass.length,
            secondPlayerValue: player_two_won_matches_on_grass.length,
            secondPlayerTotal: all_matches_on_grass.length,
        });

        const player_one_won_matches_on_hard = wonMatchesOn(player_one_won_matches, 'Hard');
        const player_two_won_matches_on_hard = wonMatchesOn(player_two_won_matches, 'Hard');
        const all_matches_on_hard = wonMatchesOn(contextPredicter.matches_between_those_two, 'Hard');
        to_progress.push({
            title: 'Duels on Hard',
            firstPlayerValue:  player_one_won_matches_on_hard.length,
            firstPlayerTotal:  all_matches_on_hard.length,
            secondPlayerValue: player_two_won_matches_on_hard.length,
            secondPlayerTotal: all_matches_on_hard.length,
        });

        const player_one_won_matches_on_carpet = wonMatchesOn(player_one_won_matches, 'Carpet');
        const player_two_won_matches_on_carpet = wonMatchesOn(player_two_won_matches, 'Carpet');
        const all_matches_on_carpet = wonMatchesOn(contextPredicter.matches_between_those_two, 'Carpet');
        to_progress.push({
            title: 'Duels on Carpet',
            firstPlayerValue:  player_one_won_matches_on_carpet.length,
            firstPlayerTotal:  all_matches_on_carpet.length,
            secondPlayerValue: player_two_won_matches_on_carpet.length,
            secondPlayerTotal: all_matches_on_carpet.length,
        });
        return(
            to_progress.map((stat, index) => {
                return(
                    <StatProgressBar data={stat} key={index}/>
                )
            })
        )
    }

    return (
        <div>
            <HeaderDiv></HeaderDiv>
                <div className="row">
                    <PlayerCardsContainer className="col-lg-9">
                        <div className="container">
                            <div className="row">
                                <PlayerCard className="col-lg">
                                    <AutoCompletePredicter
                                        which       = {1}
                                        items       = {state.players} 
                                        placeholder = {"Please select a player"} 
                                        changeSelectedPlayer             = { contextPredicter ? contextPredicter.changeSelectedPlayerOne : null}
                                        changeTournaments                = { contextPredicter ? contextPredicter.changePlayerOneTournaments : null }
                                        changeLastMatches                = { contextPredicter ? contextPredicter.changePlayerOneLastMatches : null}
                                        changeLastMatchesBetweenTwo      = { contextPredicter ? contextPredicter.changeLastMatchesBetweenTwo : null}
                                        changeGrandSlamMatchesBetweenTwo = { contextPredicter ? contextPredicter.changeGrandSlamMatchesBetweenTwo : null}
                                    />
                                    <CardDiv className="card mb-3">
                                        <div className="row no-gutters">
                                            <div className="col-md-4">
                                                <img src={`./../images/players/${contextPredicter.selectedPlayerOne.playerSlug}.jpg`} onError={(e)=>{e.target.onerror = null; e.target.src="./../../images/players/unknown.jpg"}} className="card-img" alt="..."/>
                                            </div>
                                            <div className="col-md-8">
                                                <div className="card-body">
                                                    { renderCardPlayerName(contextPredicter.selectedPlayerOne) }
                                                    <table className="table text-left">
                                                        <tbody>
                                                            <tr>
                                                                <td>
                                                                    { context.locales[context.actual].birthdate }
                                                                </td>
                                                                <td>
                                                                    { renderBirthDate(contextPredicter.selectedPlayerOne) }
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td>
                                                                    { context.locales[context.actual].age }
                                                                </td>
                                                                <td>
                                                                    { renderAge(contextPredicter.selectedPlayerOne) }
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td>
                                                                    { context.locales[context.actual].won_tournaments }
                                                                </td>
                                                                <td>
                                                                    { renderNrOfWonTournaments(contextPredicter.tournaments_player_one) }
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td>
                                                                    { context.locales[context.actual].won_grand_slams }
                                                                </td>
                                                                <td>
                                                                    { renderNrOfWonGrandSlams(contextPredicter.tournaments_player_one) }
                                                                </td>
                                                            </tr>
                                                        </tbody>
                                                    </table>
                                                    <p className="card-text"><small className="text-muted">Last updated 3 mins ago</small></p>
                                                </div>
                                            </div>
                                        </div>
                                    </CardDiv>
                                </PlayerCard>
                                <PlayerCard className="col-lg">
                                    <AutoCompletePredicter
                                        which       = {2}
                                        items       = {state.players} 
                                        placeholder = {"Please select a player"} 
                                        changeSelectedPlayer             = { contextPredicter ? contextPredicter.changeSelectedPlayerTwo : null}
                                        changeTournaments                = { contextPredicter ? contextPredicter.changePlayerTwoTournaments : null }
                                        changeLastMatches                = { contextPredicter ? contextPredicter.changePlayerTwoLastMatches : null}
                                        changeLastMatchesBetweenTwo      = { contextPredicter ? contextPredicter.changeLastMatchesBetweenTwo : null}
                                        changeGrandSlamMatchesBetweenTwo = { contextPredicter ? contextPredicter.changeGrandSlamMatchesBetweenTwo : null}
                                    />
                                    <CardDiv className="card mb-3">
                                        <div className="row no-gutters">
                                            <div className="col-md-4">
                                                <img src={`./../images/players/${contextPredicter.selectedPlayerTwo.playerSlug}.jpg`} onError={(e)=>{e.target.onerror = null; e.target.src="./../../images/players/unknown.jpg"}} className="card-img" alt="..."/>
                                            </div>
                                            <div className="col-md-8">
                                                <div className="card-body">
                                                    { renderCardPlayerName(contextPredicter.selectedPlayerTwo) }
                                                    <table className="table text-left">
                                                        <tbody>
                                                            <tr>
                                                                <td>
                                                                    { context.locales[context.actual].birthdate }
                                                                </td>
                                                                <td>
                                                                    { renderBirthDate(contextPredicter.selectedPlayerTwo) }
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td>
                                                                    { context.locales[context.actual].age }
                                                                </td>
                                                                <td>
                                                                    { renderAge(contextPredicter.selectedPlayerTwo) }
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td>
                                                                    { context.locales[context.actual].won_tournaments }
                                                                </td>
                                                                <td>
                                                                    { renderNrOfWonTournaments(contextPredicter.tournaments_player_two) }
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td>
                                                                    { context.locales[context.actual].won_grand_slams }
                                                                </td>
                                                                <td>
                                                                    { renderNrOfWonGrandSlams(contextPredicter.tournaments_player_two) }
                                                                </td>
                                                            </tr>
                                                        </tbody>
                                                    </table>
                                                    <p className="card-text"><small className="text-muted">Last updated 3 mins ago</small></p>
                                                </div>
                                            </div>
                                        </div>
                                    </CardDiv>
                                </PlayerCard>
                            </div>
                            <StatisticsCardsContainer>
                                <StatisticsH1>General statistics</StatisticsH1>
                                { renderStatistics() }
                            </StatisticsCardsContainer>
                        </div>
                
                    </PlayerCardsContainer>
                    
                    <MatchesContainer className="col-lg-3">
                        <ul className="nav nav-pills mb-3" id="pills-tab" role="tablist">
                            <li className="nav-item">
                                <a className="nav-link active font-weight-bold" id="pills-latest-tab" data-toggle="pill" href="#pills-latest" role="tab" aria-controls="pills-latest" aria-selected="true"> Latest </a>
                            </li>
                            <li className="nav-item">
                                <a className="nav-link font-weight-bold" id="pills-grand-slam-tab" data-toggle="pill" href="#pills-grand-slam" role="tab" aria-controls="pills-grand-slam" aria-selected="false"> Grand Slams </a>
                            </li>
                            <div className="nav-item dropdown">
                                <button style={{color: "#007bff"}} className="btn btn-transparent dropdown-toggle font-weight-bold" type="button" id="dropdownMenuButton" data-toggle="dropdown">
                                    Surface
                                </button>
                                <div className="dropdown-menu" aria-labelledby="dropdownMenuButton">
                                    <a className="dropdown-item" id="pills-surface-hard-tab"   data-toggle="pill" href="#pills-surface-hard"   role="tab" aria-controls="pills-surface-hard"   aria-selected="false"> Hard   </a>
                                    <a className="dropdown-item" id="pills-surface-clay-tab"   data-toggle="pill" href="#pills-surface-clay"   role="tab" aria-controls="pills-surface-clay"   aria-selected="false"> Clay   </a>
                                    <a className="dropdown-item" id="pills-surface-grass-tab"  data-toggle="pill" href="#pills-surface-grass"  role="tab" aria-controls="pills-surface-grass"  aria-selected="false"> Grass  </a>
                                    <a className="dropdown-item" id="pills-surface-carpet-tab" data-toggle="pill" href="#pills-surface-carpet" role="tab" aria-controls="pills-surface-carpet" aria-selected="false"> Carpet </a>
                                </div>
                            </div>
                        </ul>
                        <div className="tab-content" id="pills-tabContent">
                            <div className="tab-pane fade show active" id="pills-latest" role="tabpanel" aria-labelledby="pills-latest-tab">
                                { renderStats(contextPredicter.matches_between_those_two) }
                            </div>
                            <div className="tab-pane fade" id="pills-grand-slam" role="tabpanel" aria-labelledby="pills-grand-slam-tab">
                                { renderStats(contextPredicter.grand_slam_matches_between_those_two) }
                            </div>
                            <div className="tab-pane fade" id="pills-surface-hard" role="tabpanel" aria-labelledby="pills-surface-hard-tab">
                                { renderStats(wonMatchesOn(contextPredicter.matches_between_those_two, 'Hard')) }
                            </div>
                            <div className="tab-pane fade" id="pills-surface-clay" role="tabpanel" aria-labelledby="pills-surface-clay-tab">
                                { renderStats(wonMatchesOn(contextPredicter.matches_between_those_two, 'Clay')) }
                            </div>
                            <div className="tab-pane fade" id="pills-surface-grass" role="tabpanel" aria-labelledby="pills-surface-grass-tab">
                                { renderStats(wonMatchesOn(contextPredicter.matches_between_those_two, 'Grass')) }
                            </div>
                            <div className="tab-pane fade" id="pills-surface-carpet" role="tabpanel" aria-labelledby="pills-surface-carpet-tab">
                                { renderStats(wonMatchesOn(contextPredicter.matches_between_those_two, 'Carpet')) }
                            </div>
                        </div>
                </MatchesContainer>
            </div>
        </div>
    );
}

export default Prediction;