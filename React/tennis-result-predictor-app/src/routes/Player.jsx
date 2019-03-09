import React , { useContext, useState, useEffect } from 'react';
import { AppContext } from '../AppContextProvider';
import { get_request, post_request } from './../util/Request';
import { DEFALULT_SERVER_URL, VISIBLE_MATCHES } from './../constants';
import styled from 'styled-components';
import media from 'styled-media-query';

const CursorDiv = styled.div `
    cursor: pointer;
`;

const FlagIcon = styled.img `
        padding-left: 10px;
`;

const PlayerNameLink = styled.a `
    &:hover {
        color: #28a745;
    }
`;

const PlayerName = styled.tr `
    color: #28a745;
    font-size: 34px;
`;

const CardImage = styled.img `
    
`;

const NameTD = styled.td `
    width: 200px;
`;

function Player(props){
    
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

    function renderTournaments(){
        return state.tournaments.slice(0, state.nrOfVisibleTournaments).map( tournament => {
            return (
                <CursorDiv href="#" className="list-group-item list-group-item-action" key={tournament.tournament_year_id}>
                    <div className="d-flex w-100 justify-content-between">
                        <h5 className="mb-1">{tournament.tourney.name}</h5>
                        <h5 className="">{tournament.year}</h5>
                    </div>

                    <p className="mb-1"> { context.locales[context.actual].winner_player } <PlayerNameLink className="mb-1" href={tournament.player.playerUrl}><strong> {tournament.player.firstName}  {tournament.player.lastName}</strong></PlayerNameLink></p>
                    
                    <small> { context.locales[context.actual].tournament_location } {tournament.tourney.location}</small>
                </CursorDiv>
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
        console.log(matches)
        return matches.slice(matches.length - state.nrOfVisibleMatches).map(match => {
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
        <div className="">
            <div className="row">
                <div className="col">
                    <div className="card">
                        <CardImage src="https://www.tennisworldusa.org/imgb/72038/february-26-2007-roger-federer-counts-to-161-passing-connors-record.jpg" className="card-img" alt=""/>
                        <div className="card-body">
                            <table className="table text-left">
                                <thead>
                                    <PlayerName className="font-weight-bold">
                                        {state.player.firstName} {state.player.lastName}
                                    </PlayerName>
                                </thead>
                                <tbody>
                                    <tr>
                                        <td>
                                            <span className="font-weight-bold"> { context.locales[context.actual].country } </span>
                                        </td>
                                        <td>
                                            {state.player.flagCode}  <FlagIcon src={`./../images/flags/${state.player.flagCode}.png`} alt="" />
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <span className="font-weight-bold">{ context.locales[context.actual].birthdate } </span>
                                        </td>
                                        <td>
                                            {state.player.birthdate}
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <span className="font-weight-bold">{ context.locales[context.actual].handedness } </span>
                                        </td>
                                        <td>
                                            {state.player.handedness}
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <span className="font-weight-bold">{ context.locales[context.actual].won_tournaments } </span>
                                        </td>
                                        <td>
                                            {state.tournaments.length}
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <span className="font-weight-bold">{ context.locales[context.actual].won_grand_slams } </span>
                                        </td>
                                        <td>
                                            {state.grand_slams.length}
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <span className="font-weight-bold">{ context.locales[context.actual].height } </span>
                                        </td>
                                        <td>
                                            {state.player.heightCm} cm
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <span className="font-weight-bold">{ context.locales[context.actual].turned_pro } </span>
                                        </td>
                                        <td>
                                            {state.player.turnedPro}
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <span className="font-weight-bold">{ context.locales[context.actual].weight_kg } </span>
                                        </td>
                                        <td>
                                            {state.player.weightKg} kg
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <span className="font-weight-bold">{ context.locales[context.actual].weight_lbs } </span>
                                        </td>
                                        <td>
                                            {state.player.weightLbs}
                                        </td>
                                    </tr>
                                </tbody>
                            </table>                  
                        </div>
                    </div>
                </div>
                
                <div className="col col-lg-3">
                        <a href="#" className="list-group-item list-group-item-action">
                            <h1 className="mb-1 text-center font-weight-bold"> { context.locales[context.actual].latest_matches } </h1>                     
                        </a>
                    { renderMatchResult(state.last_matches) }
                    <CursorDiv className="list-group-item list-group-item-action" onClick={() => increaseNrOfVisibleMatches(5)}>
                            <p className="mb-1 text-center font-weight-bold"> { context.locales[context.actual].show_more } </p>                                
                    </CursorDiv>
                </div>

                <div className="col">
                    <div className="list-group">
                        <a className="list-group-item list-group-item-action">
                            <h1 className="mb-1 text-center font-weight-bold"> { context.locales[context.actual].latest_won_tournaments } </h1>                     
                        </a>
                        {renderTournaments()}
                        <CursorDiv className="list-group-item list-group-item-action" onClick={() => increaseNrOfVisibleTournaments(5)}>
                            <p className="mb-1 text-center font-weight-bold"> { context.locales[context.actual].show_more } </p>                                
                        </CursorDiv>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default Player;