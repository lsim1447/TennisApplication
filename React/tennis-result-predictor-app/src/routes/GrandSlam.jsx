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

                        <p className="mb-1">Winner player: <PlayerNameLink className="mb-1" href={tournament.player.playerUrl}><strong> {tournament.player.firstName}  {tournament.player.lastName}</strong></PlayerNameLink></p>
                        
                        <small>Location: {tournament.tourney.location}</small>
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

        useEffect(() => {
            get_request(`http://localhost:8080/api/tourney/one?slug=${encodeURIComponent(props.match.params.id)}`)
                .then( tourney => {
                    post_request('http://localhost:8080/api/tournament/tourney', tourney)
                        .then(tournaments => {
                            get_request(`http://localhost:8080/api/tournament/all-time-champion?slug=${encodeURIComponent(tourney.slug)}`)
                                .then(all_time_champion => {
                                    const most_singles_title = {
                                        players: all_time_champion.players,
                                        times: all_time_champion.times
                                    }
                                    const champion = tournaments[tournaments.length - 1].player;
                                    setState({...state, tourney: tourney, currentChampion: champion, mostSinglesTitle: most_singles_title, tournaments: tournaments, visibleTournaments: tournaments.reverse().slice(0, state.nrOfVisibleTournaments)});
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
                                    <h4 className="card-title">Tournament name: {state.tourney.name}</h4>
                                </div>
                                <ul className="list-group list-group-flush">
                                    <li className="list-group-item">Location: {state.tourney.location} </li>
                                    <li className="list-group-item">Condition: {state.tourney.conditions} </li>
                                    <li className="list-group-item">Surface: {state.tourney.surface} </li>
                                    <li className="list-group-item">
                                        Current champion: <a href={`/players/${state.currentChampion.playerSlug}`} className="card-link"> {state.currentChampion.firstName} {state.currentChampion.lastName} </a>
                                    </li>
                                    <li className="list-group-item">
                                        Most singles title: {renderAllTimeChampions(state.mostSinglesTitle.players)} ({state.mostSinglesTitle.times} x)
                                    </li>
                                </ul>
                                <div className="card-body">
                                    <div className="font-italic font-weight-bold">About the tournament:</div>
                                    The French Open (French: Championnats Internationaux de France de Tennis), also called Roland-Garros (French: [ʁɔlɑ̃ ɡaʁos]), is a major tennis tournament held over two weeks between late May and early June at the Stade Roland-Garros in Paris, France. The venue is named after the French aviator Roland Garros. It is the premier clay court tennis championship event in the world and the second of four annual Grand Slam tournaments, the other three being the Australian Open, Wimbledon and the US Open. The French Open is currently the only Grand Slam event held on clay, and it is the zenith of the spring clay court season. Because of the seven rounds needed for a championship, the slow-playing surface and the best-of-five-set men's singles matches (without a tiebreak in the final set), the event is widely considered to be the most physically demanding tennis tournament in the world.
                                </div>
                            </div>
                        </div>
                        <div className="col-xl">
                            One of three columns
                        </div>
                        <div className="col-xl">
                            <div className="list-group">
                                <a href="#" className="list-group-item list-group-item-action">
                                    <h1 className="mb-1 text-center font-weight-bold"> Former champions </h1>                     
                                </a>
                                {renderTournaments()}
                                <CursorDiv className="list-group-item list-group-item-action" onClick={() => increaseNrOfVisibleTournaments(5)}>
                                    <p className="mb-1 text-center font-weight-bold">Show more</p>                                
                                </CursorDiv>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        );
}

export default GrandSlam;