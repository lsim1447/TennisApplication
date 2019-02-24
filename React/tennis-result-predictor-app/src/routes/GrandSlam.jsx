import React , { useContext, useState, useEffect } from 'react';
import { AppContext } from '../AppContextProvider';
import { get_request, post_request } from './../util/Request';
import styled from 'styled-components';

const CursorDiv = styled.div `
    cursor: pointer;
`;

const PlayerNameLink = styled.a `
    &:hover {
        color: #28a745;
    }
`;

function GrandSlam(props){
        const context =  useContext(AppContext);    
        const [state, setState] = useState({
            nrOfVisibleTournaments: 8,
            tourney: '',
            tournaments: [],
            visibleTournaments: [],
            currentChampion: {},
            champions: [],
            mostSinglesTitle: {
                players: [],
                times: 0
            }
        })
    
        function increaseNrOfVisibleTournaments(value){
            const tmp_nr_of_visible_tournaments = state.nrOfVisibleTournaments + value;
            const tmp_visible_tournaments = state.tournaments.slice(0, state.nrOfVisibleTournaments);
            setState({...state, nrOfVisibleTournaments: tmp_nr_of_visible_tournaments, visibleTournaments: tmp_visible_tournaments})
        }
        
        function renderTournaments(){
            return state.visibleTournaments.map( tournament => {
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

        function renderAllTimeChampions(players){
            return players.map(player => {
                return (
                    <a key={player.player_id} href={`/players/${player.playerSlug}`} className="card-link"> {player.firstName} {player.lastName}</a>
                )
            })
        }

        function renderChampionsList(champions){
            return champions.map(champion => {
                return (
                    <li key={champion.player.player_id} className="list-group-item d-flex justify-content-between align-items-center">
                        { champion.player.firstName} {champion.player.lastName}
                        <span className="badge badge-primary badge-pill"> {champion.times} </span>
                    </li>
                )
            })
        }

        useEffect(() => {
            get_request(`http://localhost:8080/api/tourney/one?slug=${encodeURIComponent(props.match.params.id)}`)
                .then( tourney => {
                    post_request('http://localhost:8080/api/tournament/tourney', tourney)
                        .then(tournaments => {
                            get_request(`http://localhost:8080/api/tournament/all-time-champion?slug=${encodeURIComponent(tourney.slug)}`)
                                .then(all_time_champion => {
                                    get_request(`http://localhost:8080/api/tournament/champions?slug=${encodeURIComponent(props.match.params.id)}`)
                                        .then(champions => {
                                            
                                            const champions_list = champions.sort((a, b) => {
                                                return (a.times > b.times) ? -1 : 1;
                                            })

                                            const most_singles_title = {
                                                players: all_time_champion.players,
                                                times: all_time_champion.times
                                            }
                                            const champion = tournaments[tournaments.length - 1].player;
                                            setState({...state, champions: champions_list,tourney: tourney, currentChampion: champion, mostSinglesTitle: most_singles_title, tournaments: tournaments, visibleTournaments: tournaments.reverse().slice(0, state.nrOfVisibleTournaments)});
                                        })
                                    
                                })
                        })
                })
        }, []);
        
        return (
            <div>
                <div className="">
                    <div className="row">
                        <div className="col-xl">
                            <div className="card">
                                <img src={`./../images/${props.match.params.id}.jpg`} className="card-img-top" alt=""/>
                                <div className="card-body">
                                    <h4 className="card-title"> { context.locales[context.actual].tournament_name } {state.tourney.name}</h4>
                                </div>
                                <ul className="list-group list-group-flush">
                                    <li className="list-group-item"> { context.locales[context.actual].tournament_location } {state.tourney.location} </li>
                                    <li className="list-group-item"> { context.locales[context.actual].condition } {state.tourney.conditions} </li>
                                    <li className="list-group-item"> { context.locales[context.actual].surface } {state.tourney.surface} </li>
                                    <li className="list-group-item">
                                        { context.locales[context.actual].current_champion }
                                        <a href={`/players/${state.currentChampion.playerSlug}`} className="card-link"> {state.currentChampion.firstName} {state.currentChampion.lastName} </a>
                                    </li>
                                    <li className="list-group-item">
                                        { context.locales[context.actual].most_singles_title }
                                        {renderAllTimeChampions(state.mostSinglesTitle.players)} ({state.mostSinglesTitle.times} x)
                                    </li>
                                </ul>
                                <div className="card-body">
                                    <div className="font-italic font-weight-bold"> { context.locales[context.actual].about_the_tournament } </div>
                                    { 
                                        props.match.params.id  === 'roland-garros' ? context.locales[context.actual].roland_garros_short_description : 
                                        (props.match.params.id  === 'australian-open' ? context.locales[context.actual].australian_open_short_description : 
                                        (props.match.params.id  === 'us-open' ?  context.locales[context.actual].us_open_short_description :  context.locales[context.actual].wimbledon_short_description))
                                    }
                                </div>
                            </div>
                        </div>
                        <div className="col-xl">
                            <ul className="list-group">
                                <a href="#" className="list-group-item list-group-item-action">
                                    <h1 className="mb-1 text-center font-weight-bold"> { context.locales[context.actual].list_of_champions } </h1>                     
                                </a>
                                {renderChampionsList(state.champions)}
                            </ul>
                        </div>
                        <div className="col-xl">
                            <div className="list-group">
                                <a href="#" className="list-group-item list-group-item-action">
                                    <h1 className="mb-1 text-center font-weight-bold"> { context.locales[context.actual].former_champions } </h1>                     
                                </a>
                                {renderTournaments()}
                                <CursorDiv className="list-group-item list-group-item-action" onClick={() => increaseNrOfVisibleTournaments(5)}>
                                    <p className="mb-1 text-center font-weight-bold"> { context.locales[context.actual].show_more } </p>                                
                                </CursorDiv>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        );
}

export default GrandSlam;