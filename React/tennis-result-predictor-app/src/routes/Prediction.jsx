import React , { useContext, useState, useEffect } from 'react';
import { get_request } from '../util/Request';
import styled from 'styled-components';
import { DEFALULT_SERVER_URL } from '../constants';
import AutoCompletePredicter from '../components/autocomplete/AutoCompletePredicter';
import { AppContext } from '../AppContextProvider';
import { PredicterContext } from './../context-providers/PredicterContextProvider';
import { StatisticsContext } from './../context-providers/StatisticsContextProvider';

import StatProgressBar from './../components/StatProgressBar';
import { isGrandSlam, wonMatchesBy, wonMatchesOn } from './../util/FunctionUtil';
import Matches from './../util/elements/Matches';
import { Clickable } from './../util/OftenUsedElements';
import PredictModal from './../components/PredictModal';

const HeaderDiv = styled.div `
    height: 250px;
    background-image: url('./../images/others/background1.jpg');
    background-repeat: no-repeat;
    margin-bottom: 25px;
`;

const CardDiv = styled.div `
    margin-top: 15px;
`;

const PlayerCard = styled.div ``;

const PlayerCardsContainer = styled.div ``;

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

const VSButton = styled.button `
    @media (max-width: 992px) {
        background-color: #dc3545;
        color: white;
        width: 100%
        border: 3px solid white;
        padding: 10px 30px;
        margin-bottom: 12px;
        font-weight: bold;
    }
    @media (min-width: 992px) {
        background-color: #dc3545;
        color: white;
        font-size: 18px;
        padding: 10px 30px;
        border-radius: 50%;
        border: 3px solid white;
        margin-bottom: 12px;
        font-weight: bold;
        &: hover {
            color: black;
        }
    }
`;

function Prediction(props){
    
    const context =  useContext(AppContext);
    const contextPredicter =  useContext(PredicterContext);
    const statisticsContext = useContext(StatisticsContext);

    const [state, setState] = useState({
        players: [],
        nrOfVisibleMatches: 7,
    })

    function increaseNrOfVisibleMatches(value){
        const tmp_nr_of_visible_matches = state.nrOfVisibleMatches + value;
        setState({...state, nrOfVisibleMatches: tmp_nr_of_visible_matches})
    }

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


    useEffect(() => {
        get_request(`${DEFALULT_SERVER_URL}/player/all`)
            .then(players => {
                setState({...state, players: players})
            })
    }, []);

    function renderStatistics(){
        if (!contextPredicter.matches_between_those_two || contextPredicter.matches_between_those_two.length === 0) return;

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
        statisticsContext.changeNrOfWonTournamentByPlayerOne(contextPredicter.tournaments_player_one.length);
        statisticsContext.changeNrOfWonTournamentByPlayerTwo(contextPredicter.tournaments_player_two.length);
        statisticsContext.changeNrOfTotalWonTournament(contextPredicter.tournaments_player_one.length + contextPredicter.tournaments_player_two.length);

        const p1_gr_sl = contextPredicter.tournaments_player_one.filter(t => isGrandSlam(t));
        const p2_gr_sl = contextPredicter.tournaments_player_two.filter(t => isGrandSlam(t));
        to_progress.push({
            title: 'Won Grands Slams',
            firstPlayerValue:  p1_gr_sl.length,
            firstPlayerTotal:  p1_gr_sl.length + p2_gr_sl.length,
            secondPlayerValue: p2_gr_sl.length,
            secondPlayerTotal: p1_gr_sl.length + p2_gr_sl.length,
        });
        statisticsContext.changeNrOfWonGrandSlamByPlayerOne(p1_gr_sl.length);
        statisticsContext.changeNrOfWonGrandSlamByPlayerTwo(p2_gr_sl.length);
        statisticsContext.changeNrOfTotalWonGrandSlam(p1_gr_sl.length + p2_gr_sl.length);

        to_progress.push({
            title: 'Duels',
            firstPlayerValue:  player_one_won_matches.length,
            firstPlayerTotal:  player_one_won_matches.length + player_two_won_matches.length,
            secondPlayerValue: player_two_won_matches.length,
            secondPlayerTotal: player_one_won_matches.length + player_two_won_matches.length,
        });
        statisticsContext.changeNrOfWonDuelByPlayerOne(player_one_won_matches.length);
        statisticsContext.changeNrOfWonDuelByPlayerTwo(player_two_won_matches.length);
        statisticsContext.changeNrOfTotalDuelsBetweenTwo(player_one_won_matches.length + player_two_won_matches.length);

        const player_one_won_matches_on_gr = player_one_won_matches.filter(t => isGrandSlam(t.tournament));
        const player_two_won_matches_on_gr = player_two_won_matches.filter(t => isGrandSlam(t.tournament));
        to_progress.push({
            title: 'Duels on Grand Slam',
            firstPlayerValue:  player_one_won_matches_on_gr.length,
            firstPlayerTotal:  player_one_won_matches_on_gr.length + player_two_won_matches_on_gr.length,
            secondPlayerValue: player_two_won_matches_on_gr.length,
            secondPlayerTotal: player_one_won_matches_on_gr.length + player_two_won_matches_on_gr.length,
        });
        statisticsContext.changeNrOfWonGrandSlamDuelsByPlayerOne(player_one_won_matches_on_gr.length);
        statisticsContext.changeNrOfWonGrandSlamDuelsByPlayerTwo(player_two_won_matches_on_gr.length);
        statisticsContext.changeNrOfTotalGrandSlamDuels(player_one_won_matches_on_gr.length + player_two_won_matches_on_gr.length);

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
        statisticsContext.changeNrOfWonGrandSlamDuelsByPlayerOne(player_one_won_matches_on_clay.length);
        statisticsContext.changeNrOfWonGrandSlamDuelsByPlayerTwo(player_two_won_matches_on_clay.length);
        statisticsContext.changeNrOfTotalGrandSlamDuels(all_matches_on_clay.length);

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
        statisticsContext.changeNrOfDuelsOnGrassByPlayerOne(player_one_won_matches_on_grass.length);
        statisticsContext.changeNrOfDuelsOnGrassByPlayerTwo(player_two_won_matches_on_grass.length);
        statisticsContext.changeNrOfTotalDuelsOnGrass(all_matches_on_grass.length);

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
        statisticsContext.changeNrOfDuelsOnHardByPlayerOne(player_one_won_matches_on_hard.length);
        statisticsContext.changeNrOfDuelsOnHardByPlayerTwo(player_two_won_matches_on_hard.length);
        statisticsContext.changeNrOfTotalDuelsOnHard(all_matches_on_hard.length);

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
        statisticsContext.changeNrOfDuelsOnCarpetByPlayerOne(player_one_won_matches_on_carpet.length);
        statisticsContext.changeNrOfDuelsOnCarpetByPlayerTwo(player_two_won_matches_on_carpet.length);
        statisticsContext.changeNrOfTotalDuelsOnCarpet(all_matches_on_carpet.length);
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
                        <div className="row">
                            <div className="col-lg text-center">
                                <VSButton type="button" data-toggle="modal" data-target="#exampleModal" disabled={!contextPredicter.matches_between_those_two || contextPredicter.matches_between_those_two.length === 0}> Predict duel</VSButton>
                                <PredictModal />
                            </div>
                        </div>
                        <StatisticsCardsContainer>
                            <StatisticsH1>General statistics</StatisticsH1>
                            { renderStatistics() }
                        </StatisticsCardsContainer>
                    </div>
                </PlayerCardsContainer>

                <div className="col-lg-3">
                    <Matches
                        matches = {contextPredicter.matches_between_those_two}
                        grand_slam_matches = { contextPredicter.grand_slam_matches_between_those_two }
                        selectedPlayerOne = { contextPredicter.selectedPlayerOne }
                        selectedPlayerTwo = { contextPredicter.selectedPlayerTwo }
                        nrOfVisibleMatches = { state.nrOfVisibleMatches }
                        same = { -1 }
                    />
                    <Clickable className="list-group-item list-group-item-action" onClick={() => increaseNrOfVisibleMatches(5)}>
                        <p className="mb-1 text-center font-weight-bold"> { context.locales[context.actual].show_more } </p>                                
                    </Clickable>
                </div>
            </div>
        </div>
    );
}

export default Prediction;