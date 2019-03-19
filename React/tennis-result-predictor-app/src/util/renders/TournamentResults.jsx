import React  from 'react';
import { Clickable, PlayerNameLink } from './../OftenUsedElements';

export function renderTournaments(tournaments, nrOfVisibleTournaments, winner_player_label, tournament_location_label){
    return tournaments.slice(0, nrOfVisibleTournaments).map( tournament => {
        return (
            <Clickable href={`/tournament/${tournament.tournament_year_id}`} className="list-group-item list-group-item-action" key={tournament.tournament_year_id}>
                <div className="d-flex w-100 justify-content-between">
                    <h5 className="mb-1">{tournament.tourney.name}</h5>
                    <h5 className="">{tournament.year}</h5>
                </div>

                <p className="mb-1"> { winner_player_label } <PlayerNameLink className="mb-1" href={tournament.player.playerUrl}><strong> {tournament.player.firstName}  {tournament.player.lastName}</strong></PlayerNameLink></p>
                
                <small> { tournament_location_label } {tournament.tourney.location}</small>
            </Clickable>
        )
    })
}