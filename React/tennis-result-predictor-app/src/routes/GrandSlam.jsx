import React , { useContext, useState, useEffect } from 'react';
import { AppContext } from '../AppContextProvider';
import { get_request, post_request } from './../util/Request';
import { DEFALULT_SERVER_URL, HOVER_COLOR } from './../constants';
import { Clickable, PlayerNameLink } from './../util/OftenUsedElements';

function GrandSlam(props){
        const context =  useContext(AppContext);    
        const [state, setState] = useState({
            nrOfVisibleTournaments: 7,
            nrOfVisibleChampions: 11,
            tourney: '',
            tournaments: [],
            currentChampion: {},
            champions: [],
            mostSinglesTitle: {
                players: [],
                times: 0
            }
        })
    
        function increaseNrOfVisibleTournaments(value){
            const tmp_nr_of_visible_tournaments = state.nrOfVisibleTournaments + value;
            setState({...state, nrOfVisibleTournaments: tmp_nr_of_visible_tournaments})
        }
        
        function increaseNrOfVisibleChampions(value){
            const tmp_nr_champ = state.nrOfVisibleChampions + value;
            setState({...state, nrOfVisibleChampions: tmp_nr_champ});
        }

        function renderTournaments(){
            return state.tournaments.slice(0, state.nrOfVisibleTournaments).map( tournament => {
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

        function renderAllTimeChampions(players){
            return players.map(player => {
                return (
                    <PlayerNameLink key={player.player_id} href={`/players/${player.playerSlug}`} className="card-link font-weight-bold"> {player.firstName} {player.lastName}</PlayerNameLink>
                )
            })
        }

        function renderChampionsList(champions){
            return champions.slice(0, state.nrOfVisibleChampions).map(champion => {
                return (
                    <li key={champion.player.player_id} className="list-group-item d-flex justify-content-between align-items-center">
                        <PlayerNameLink href={`/players/${champion.player.playerSlug}`} className="font-weight-bold"> { champion.player.firstName} {champion.player.lastName} </PlayerNameLink>
                        <span className="badge badge-primary badge-pill"> {champion.times} </span>
                    </li>
                )
            })
        }

        useEffect(() => {
            get_request(`${DEFALULT_SERVER_URL}/tourney/one?slug=${encodeURIComponent(props.match.params.id)}`)
                .then( tourney => {
                    post_request(`${DEFALULT_SERVER_URL}/tournament/tourney`, tourney)
                        .then(tournaments => {
                            get_request(`${DEFALULT_SERVER_URL}/tournament/all-time-champion?slug=${encodeURIComponent(tourney.slug)}`)
                                .then(all_time_champion => {
                                    get_request(`${DEFALULT_SERVER_URL}/tournament/champions?slug=${encodeURIComponent(props.match.params.id)}`)
                                        .then(champions => {
                                            
                                            const champions_list = champions.sort((a, b) => {
                                                return (a.times > b.times) ? -1 : 1;
                                            })

                                            const most_singles_title = {
                                                players: all_time_champion.players,
                                                times: all_time_champion.times
                                            }
                                            const champion = tournaments[tournaments.length - 1].player;
                                            setState({...state, champions: champions_list,tourney: tourney, currentChampion: champion, mostSinglesTitle: most_singles_title, tournaments: tournaments.reverse()});
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
                                <table className="table text-left">
                                    <tbody>
                                        <tr>
                                            <td>
                                                { context.locales[context.actual].tournament_location }
                                            </td>
                                            <td>
                                                {state.tourney.location}
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                { context.locales[context.actual].condition }
                                            </td>
                                            <td>
                                                {state.tourney.conditions}
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                { context.locales[context.actual].surface }
                                            </td>
                                            <td>
                                                {state.tourney.surface}
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                { context.locales[context.actual].current_champion }
                                            </td>
                                            <td>
                                                <PlayerNameLink href={`/players/${state.currentChampion.playerSlug}`} className="card-link font-weight-bold"> {state.currentChampion.firstName} {state.currentChampion.lastName} </PlayerNameLink>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                { context.locales[context.actual].most_singles_title }
                                            </td>
                                            <td>
                                                {renderAllTimeChampions(state.mostSinglesTitle.players)} ({state.mostSinglesTitle.times} x)
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>     

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
                                <Clickable className="list-group-item list-group-item-action" onClick={() => increaseNrOfVisibleChampions(5)}>
                                    <p className="mb-1 text-center font-weight-bold"> { context.locales[context.actual].show_more } </p>                                
                                </Clickable>
                            </ul>
                        </div>
                        <div className="col-xl">
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
            </div>
        );
}

export default GrandSlam;