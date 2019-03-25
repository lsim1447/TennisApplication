import React , { Component } from 'react';

export const StatisticsContext = React.createContext();
export const StatisticsContextConsumer = StatisticsContext.Consumer;

export default class StatisticsContextProvider extends Component {
    
    state = {
        statistics: {
            nrOfWonTournamentByPlayerOne:     0,
            nrOfWonTournamentByPlayerTwo:     0,
            nrOfTotalWonTournament:           0,
            nrOfWonGrandSlamByPlayerOne:      0,
            nrOfWonGrandSlamByPlayerTwo:      0,
            nrOfTotalWonGrandSlam:            0,
            nrOfWonDuelByPlayerOne:           0,
            nrOfWonDuelByPlayerTwo:           0,
            nrOfTotalDuelsBetweenTwo:         0,
            nrOfWonGrandSlamDuelsByPlayerOne: 0,
            nrOfWonGrandSlamDuelsByPlayerTwo: 0,
            nrOfTotalGrandSlamDuels:          0,
            nrOfDuelsOnClayByPlayerOne:       0,
            nrOfDuelsOnClayByPlayerTwo:       0,
            nrOfTotalDuelsOnClay:             0,
            nrOfDuelsOnGrassByPlayerOne:      0,
            nrOfDuelsOnGrassByPlayerTwo:      0,
            nrOfTotalDuelsOnGrass:            0,
            nrOfDuelsOnHardByPlayerOne:       0,
            nrOfDuelsOnHardByPlayerTwo:       0,
            nrOfTotalDuelsOnHard:             0,
            nrOfDuelsOnCarpetByPlayerOne:     0,
            nrOfDuelsOnCarpetByPlayerTwo:     0,
            nrOfTotalDuelsOnCarpet:           0,
       }
    }
    
    changeStatistics = (newValue) => {
        this.setState({statistics: newValue})
    }

    render(){
        return (
            <StatisticsContext.Provider 
                value={{ ...this.state,
                    changeStatistics: this.changeStatistics,
                }}
            >
                { this.props.children }
            </StatisticsContext.Provider>
        );
    }
    
}