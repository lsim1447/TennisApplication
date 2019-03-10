import React , { useContext, useState, useEffect } from 'react';
import { get_request } from '../util/Request';
import styled from 'styled-components';
import { DEFALULT_SERVER_URL } from '../constants';
import AutoCompleteText from './../components/AutoCompleteText';
import PlayerContextProvider from './../context-providers/PlayerContextProvider';
import { PlayerContext } from './../context-providers/PlayerContextProvider';
import { AppContext } from '../AppContextProvider';
import { Clickable, PlayerNameLink } from './../util/OftenUsedElements';
import AnimateHeight from 'react-animate-height';

const FlagIcon = styled.img `
    padding-left: 10px;
`;

const PlayerName = styled.tr `
    color: #28a745;
    font-size: 34px;
`;

const CardImage = styled.img `
    max-height: 400px;
`;

const NameTD = styled.td `
    width: 200px;
`;

const PlayerSelectorContainer = styled.div `
    padding-top: 50px;
    padding-bottom: 50px;
    background-image: url('./images/gifs/net.gif')
`;

function Player(props){
    
    const context =  useContext(AppContext);
    const playerContext = useContext(PlayerContext);

    console.log('playerContext = ', playerContext);

    const [state, setState] = useState({
        players: [],
        nrOfVisibleTournaments: 5,
        nrOfVisibleMatches: 8,

        tournaments: [],
        grand_slams: [],
        last_matches: []
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

    function renderTournaments(){
        return playerContext.tournaments.slice(0, state.nrOfVisibleTournaments).map( tournament => {
            return (
                <Clickable href={`/tournament/${tournament.tournament_year_id}`} className="list-group-item list-group-item-action" key={tournament.tournament_year_id}>
                    <div className="d-flex w-100 justify-content-between">
                        <h5 className="mb-1">{tournament.tourney.name}</h5>
                        <h5 className="">{tournament.year}</h5>
                    </div>

                    <p className="mb-1"> { context.locales[context.actual].winner_player } <PlayerNameLink className="mb-1" href={tournament.player.playerUrl}><strong> {tournament.player.firstName}  {tournament.player.lastName}</strong></PlayerNameLink></p>
                    
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
        return matches.slice(0, state.nrOfVisibleMatches).map(match => {
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
                    <span className="font-weight-bold">{match.tournament.tourney.name}, {match.tournament.year}, {match.round_name}</span>
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
        <PlayerContextProvider>
            <PlayerSelectorContainer className="row">
                <div className="col col-lg-4"></div>
                    <div className="col">
                        <AutoCompleteText 
                            items={state.players} 
                            placeholder={"Please select a player"} 
                            changeSelectedPlayer={playerContext ? playerContext.changeSelectedPlayer : null}
                            changeTournaments={ playerContext ? playerContext.changeTournaments : null }
                            changeGrandSlams={ playerContext ? playerContext.changeGrandSlams : null }
                            changeLastMatches={ playerContext ? playerContext.changeLastMatches : null}
                        />
                    </div>
                <div className="col col-lg-4"></div>
            </PlayerSelectorContainer>
            <div className="row">
                <div className="col">
                    <div className="card">
                        <CardImage src={`./../images/players/${playerContext.selectedPlayer.playerSlug}.jpg`} className="card-img" alt="" onError={(e)=>{e.target.onerror = null; e.target.src="./../../images/players/unknown.jpg"}}/>
                        <div className="card-body">
                            <table className="table text-left">
                                <thead>
                                    <PlayerName className="font-weight-bold">
                                        {playerContext.selectedPlayer.firstName} {playerContext.selectedPlayer.lastName}
                                    </PlayerName>
                                </thead>
                                <tbody>
                                    <tr>
                                        <td>
                                            <span className="font-weight-bold"> { context.locales[context.actual].country } </span>
                                        </td>
                                        <td>
                                            {playerContext.selectedPlayer.flagCode}  <FlagIcon src={`./../images/flags/${playerContext.selectedPlayer.flagCode}.png`} alt="" />
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <span className="font-weight-bold">{ context.locales[context.actual].birthdate } </span>
                                        </td>
                                        <td>
                                            {playerContext.selectedPlayer.birthdate}
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <span className="font-weight-bold">{ context.locales[context.actual].handedness } </span>
                                        </td>
                                        <td>
                                            {playerContext.selectedPlayer.handedness}
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <span className="font-weight-bold">{ context.locales[context.actual].won_tournaments } </span>
                                        </td>
                                        <td>
                                            { playerContext.tournaments.length }
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <span className="font-weight-bold">{ context.locales[context.actual].won_grand_slams } </span>
                                        </td>
                                        <td>
                                            { playerContext.grand_slams.length }
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <span className="font-weight-bold">{ context.locales[context.actual].height } </span>
                                        </td>
                                        <td>
                                            {playerContext.selectedPlayer.heightCm} cm
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <span className="font-weight-bold">{ context.locales[context.actual].turned_pro } </span>
                                        </td>
                                        <td>
                                            {playerContext.selectedPlayer.turnedPro}
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <span className="font-weight-bold">{ context.locales[context.actual].weight_kg } </span>
                                        </td>
                                        <td>
                                            {playerContext.selectedPlayer.weightKg} kg
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <span className="font-weight-bold">{ context.locales[context.actual].weight_lbs } </span>
                                        </td>
                                        <td>
                                            {playerContext.selectedPlayer.weightLbs}
                                        </td>
                                    </tr>
                                </tbody>
                            </table>                  
                        </div>
                    </div>
                </div>
                <div className="col col-lg-4">
                    <a href="#" className="list-group-item list-group-item-action">
                        <h1 className="mb-1 text-center font-weight-bold"> { context.locales[context.actual].latest_matches } </h1>                     
                    </a>
                    { renderMatchResult(playerContext.last_matches) }
                    <Clickable className="list-group-item list-group-item-action" onClick={() => increaseNrOfVisibleMatches(5)}>
                            <p className="mb-1 text-center font-weight-bold"> { context.locales[context.actual].show_more } </p>                                
                    </Clickable>
                </div>
                <div className="col col-lg-4">
                    <div className="list-group">
                        <a className="list-group-item list-group-item-action">
                            <h1 className="mb-1 text-center font-weight-bold"> { context.locales[context.actual].latest_won_tournaments } </h1>                     
                        </a>
                        {renderTournaments()}
                        <Clickable className="list-group-item list-group-item-action" onClick={() => increaseNrOfVisibleTournaments(5)}>
                            <p className="mb-1 text-center font-weight-bold"> { context.locales[context.actual].show_more } </p>                                
                        </Clickable>
                    </div>
                </div>
            </div>
        </PlayerContextProvider>        
    );
}

export default Player;