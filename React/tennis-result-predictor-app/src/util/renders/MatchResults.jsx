import React  from 'react';
import { TableTournamentName, NameTD } from '../OftenUsedElements';

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

function renderMatchResult(matches, nrOfVisibleMatches){
    return matches.slice(0, nrOfVisibleMatches).map(match => {
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

export function renderStats(matches, selectedPlayerOne, selectedPlayerTwo, nrOfVisibleMatches, same, isTour){
    if ((selectedPlayerOne && selectedPlayerOne.firstName && selectedPlayerTwo && selectedPlayerTwo.firstName &&
        (selectedPlayerOne.firstName !== selectedPlayerTwo.firstName  || same === 1)) || (isTour !== 0)){
        return(
            <div>
                { renderMatchResult(matches, nrOfVisibleMatches) }
            </div>
        )
    }
}
