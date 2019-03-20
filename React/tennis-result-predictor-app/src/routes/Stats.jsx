import React , { useState, useEffect } from 'react';
import { get_request } from './../util/Request';
import { DEFALULT_SERVER_URL } from './../constants';
import styled from 'styled-components';
import StatProgressBar from './../components/StatProgressBar';
import PlayerProfile from './../components/PlayerProfile';
import { Clickable } from './../util/OftenUsedElements';

const Container = styled.div `
    @media (min-width: 768px) {
        margin-left: 20%;
        margin-right: 20%;
    }
`;

const DescriptionText = styled.p `
    font-size: 42px;
    font-weight: bold;
    padding-top: 24px;
    padding-bottom: 24px;

    @media (max-width: 768px) {
        font-size: 24px;
    }
`;

function Stats(props) {
    
    const [state, setState] = useState({
        match: {}, 
        stats: {},
        winnerPlayer: {},
        loserPlayer: {},
        tournament: {},
        progress: {
            title: 'Total points won',
            firstPlayerValue: 1,
            secondPlayerValue: 6,
        }
    })

    useEffect(() => {
        get_request(`${DEFALULT_SERVER_URL}/matches/one?id=${encodeURIComponent(props.match.params.id)}`)
            .then(match => {
                get_request(`${DEFALULT_SERVER_URL}/stats/one?id=${encodeURIComponent(props.match.params.id)}`)
                    .then(stats => {
                        setState({...state, match: match, stats: stats, winnerPlayer: match.winnerPlayer, loserPlayer: match.loserPlayer});
                        renderProgressBars(stats)
                    })
            })
    }, []);
    
    function renderProgressBars(stats){
        const to_progress = [];
        to_progress.push({
            title: 'Aces',
            firstPlayerValue:  stats['winner_aces'],
            firstPlayerTotal:  stats['winner_aces'] + stats['loser_aces'],
            secondPlayerValue: stats['loser_aces'],
            secondPlayerTotal: stats['winner_aces'] + stats['loser_aces'],
        });
        to_progress.push({
            title: 'Double Faults',
            firstPlayerValue:  stats['winner_double_faults'],
            firstPlayerTotal:  stats['winner_double_faults'] + stats['loser_double_faults'],
            secondPlayerValue: stats['loser_double_faults'],
            secondPlayerTotal: stats['winner_double_faults'] + stats['loser_double_faults'],
        });
        to_progress.push({
            title: '1st Serve Percentage',
            firstPlayerValue:  stats['winner_first_serves_in'],
            firstPlayerTotal:  stats['winner_first_serves_total'],
            secondPlayerValue: stats['loser_first_serves_in'],
            secondPlayerTotal: stats['loser_first_serves_total'],
        });
        to_progress.push({
            title: '1st Serve Points Won',
            firstPlayerValue:  stats['winner_first_serve_points_won'],
            firstPlayerTotal:  stats['winner_first_serves_total'],
            secondPlayerValue: stats['loser_first_serve_points_won'],
            secondPlayerTotal: stats['loser_first_serves_total'],
        });
        to_progress.push({
            title: '2nd Serve Points Won',
            firstPlayerValue:  stats['winner_second_serve_points_won'],
            firstPlayerTotal:  stats['winner_second_serve_points_total'],
            secondPlayerValue: stats['loser_second_serve_points_won'],
            secondPlayerTotal: stats['loser_second_serve_points_total'],
        });
        to_progress.push({
            title: 'Break Points Saved',
            firstPlayerValue:  stats['winner_break_points_saved'],
            firstPlayerTotal:  stats['winner_break_points_serve_total'],
            secondPlayerValue: stats['loser_break_points_saved'],
            secondPlayerTotal: stats['loser_break_points_serve_total'],
        });
        to_progress.push({
            title: 'Break Points Converted',
            firstPlayerValue:  stats['winner_break_points_converted'],
            firstPlayerTotal:  stats['winner_break_points_return_total'],
            secondPlayerValue: stats['loser_break_points_converted'],
            secondPlayerTotal: stats['loser_break_points_return_total'],
        });
        to_progress.push({
            title: 'Service Games Played',
            firstPlayerValue:  stats['winner_service_games_played'],
            firstPlayerTotal:  stats['winner_service_games_played'] + stats['loser_service_games_played'],
            secondPlayerValue: stats['loser_service_games_played'],
            secondPlayerTotal: stats['loser_service_games_played'] + stats['winner_service_games_played'],
        });
        to_progress.push({
            title: 'Service Points Won',
            firstPlayerValue:  stats['winner_service_points_won'],
            firstPlayerTotal:  stats['winner_service_points_total'],
            secondPlayerValue: stats['loser_service_points_won'],
            secondPlayerTotal: stats['loser_service_points_total'] ,
        });
        to_progress.push({
            title: 'Return Points Won',
            firstPlayerValue:  stats['winner_return_points_won'],
            firstPlayerTotal:  stats['winner_return_points_total'],
            secondPlayerValue: stats['loser_return_points_won'],
            secondPlayerTotal: stats['loser_return_points_total'] ,
        });
        to_progress.push({
            title: 'Total Points Won',
            firstPlayerValue:  stats['winner_total_points_won'],
            firstPlayerTotal:  stats['winner_total_points_total'],
            secondPlayerValue: stats['loser_total_points_won'],
            secondPlayerTotal: stats['winner_total_points_total'] ,
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
        <Container> 
            <div className="row">
                <div className="col">
                    <Clickable href={`/tournament/${state.match.tournament ? state.match.tournament.tournament_year_id : ''}`} className="text-center"> 
                        <DescriptionText> {state.match.tournament? state.match.tournament.tourney.name : ''}, {state.match.round_name},  {state.match.tournament? state.match.tournament.year : ''}</DescriptionText> 
                    </Clickable>
                </div>
            </div>

            <div className="row">
                <div className="col">
                    <PlayerProfile player={state.winnerPlayer} isWinner={true} imageUrl={`./../../images/players/${state.winnerPlayer.playerSlug}.jpg`}/>
                </div>
                <div className="col">
                    <PlayerProfile player={state.loserPlayer} isWinner={false} imageUrl={`./../../images/players/${state.loserPlayer.playerSlug}.jpg`}/>                
                </div>
            </div>
            
            { renderProgressBars(state.stats)}
        </Container>
    )
}

export default Stats;