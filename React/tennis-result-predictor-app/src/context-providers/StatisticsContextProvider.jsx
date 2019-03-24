import React, { Component } from 'react';

export const StatisticsContext = React.createContext();
export const StatisticsContextConsumer = StatisticsContext.Consumer;

export default class StatisticsContextProvider extends Component {

    state = {
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
    };

    changeNrOfWonTournamentByPlayerOne = (newValue) => {
        this.setState({nrOfWonTournamentByPlayerOne: newValue})
    }
    changeNrOfWonTournamentByPlayerTwo = (newValue) => {
        this.setState({nrOfWonTournamentByPlayerTwo: newValue})
    }
    changeNrOfTotalWonTournament = (newValue) => {
        this.setState({nrOfTotalWonTournament: newValue})
    }

    changeNrOfWonGrandSlamByPlayerOne = (newValue) => {
        this.setState({nrOfWonGrandSlamByPlayerOne: newValue})
    }
    changeNrOfWonGrandSlamByPlayerTwo = (newValue) => {
        this.setState({nrOfWonGrandSlamByPlayerTwo: newValue})
    }
    changeNrOfTotalWonGrandSlam = (newValue) => {
        this.setState({nrOfTotalWonGrandSlam: newValue})
    }

    changeNrOfWonDuelByPlayerOne = (newValue) => {
        this.setState({nrOfWonDuelByPlayerOne: newValue})
    }
    changeNrOfWonDuelByPlayerTwo = (newValue) => {
        this.setState({nrOfWonDuelByPlayerTwo: newValue})
    }
    changeNrOfTotalDuelsBetweenTwo = (newValue) => {
        this.setState({nrOfTotalDuelsBetweenTwo: newValue})
    }

    changeNrOfWonGrandSlamDuelsByPlayerOne = (newValue) => {
        this.setState({nrOfWonGrandSlamDuelsByPlayerOne: newValue})
    }
    changeNrOfWonGrandSlamDuelsByPlayerTwo = (newValue) => {
        this.setState({nrOfWonGrandSlamDuelsByPlayerTwo: newValue})
    }
    changeNrOfTotalGrandSlamDuels = (newValue) => {
        this.setState({nrOfTotalGrandSlamDuels: newValue})
    }

    changeNrOfDuelsOnClayByPlayerOne = (newValue) => {
        this.setState({nrOfDuelsOnClayByPlayerOne: newValue})
    }
    changeNrOfDuelsOnClayByPlayerTwo = (newValue) => {
        this.setState({nrOfDuelsOnClayByPlayerTwo: newValue})
    }
    changeNrOfTotalDuelsOnClay = (newValue) => {
        this.setState({nrOfTotalDuelsOnClay: newValue})
    }

    changeNrOfDuelsOnGrassByPlayerOne = (newValue) => {
        this.setState({nrOfDuelsOnGrassByPlayerOne: newValue})
    }
    changeNrOfDuelsOnGrassByPlayerTwo = (newValue) => {
        this.setState({nrOfDuelsOnGrassByPlayerTwo: newValue})
    }
    changeNrOfTotalDuelsOnGrass = (newValue) => {
        this.setState({nrOfTotalDuelsOnGrass: newValue})
    }

    changeNrOfDuelsOnHardByPlayerOne = (newValue) => {
        this.setState({nrOfDuelsOnHardByPlayerOne: newValue})
    }
    changeNrOfDuelsOnHardByPlayerTwo = (newValue) => {
        this.setState({nrOfDuelsOnHardByPlayerTwo: newValue})
    }
    changeNrOfTotalDuelsOnHard = (newValue) => {
        this.setState({nrOfTotalDuelsOnHard: newValue})
    }

    changeNrOfDuelsOnCarpetByPlayerOne = (newValue) => {
        this.setState({nrOfDuelsOnCarpetByPlayerOne: newValue})
    }
    changeNrOfDuelsOnCarpetByPlayerTwo = (newValue) => {
        this.setState({nrOfDuelsOnCarpetByPlayerTwo: newValue})
    }
    changeNrOfTotalDuelsOnCarpet = (newValue) => {
        this.setState({nrOfTotalDuelsOnCarpet: newValue})
    }
    

    render(){
        return (
            <StatisticsContext.Provider 
                value={{ ...this.state,
                    changeNrOfWonTournamentByPlayerOne:          this.changeNrOfWonTournamentByPlayerOne,
                    changeNrOfWonTournamentByPlayerTwo:          this.changeNrOfWonTournamentByPlayerTwo,
                    changeNrOfTotalWonTournament:                this.changeNrOfTotalWonTournament,

                    changeNrOfWonGrandSlamByPlayerOne:           this.changeNrOfWonGrandSlamByPlayerOne,
                    changeNrOfWonGrandSlamByPlayerTwo:           this.changeNrOfWonGrandSlamByPlayerTwo,
                    changeNrOfTotalWonGrandSlam:                 this.changeNrOfTotalWonGrandSlam,

                    changeNrOfWonDuelByPlayerOne:                this.changeNrOfWonDuelByPlayerOne,
                    changeNrOfWonDuelByPlayerTwo:                this.changeNrOfWonDuelByPlayerTwo,
                    changeNrOfTotalDuelsBetweenTwo:              this.changeNrOfTotalDuelsBetweenTwo,

                    changeNrOfWonGrandSlamDuelsByPlayerOne:      this.changeNrOfWonGrandSlamDuelsByPlayerOne,
                    changeNrOfWonGrandSlamDuelsByPlayerTwo:      this.changeNrOfWonGrandSlamDuelsByPlayerTwo,
                    changeNrOfTotalGrandSlamDuels:               this.changeNrOfTotalGrandSlamDuels,

                    changeNrOfDuelsOnClayByPlayerOne:            this.changeNrOfDuelsOnClayByPlayerOne,
                    changeNrOfDuelsOnClayByPlayerTwo:            this.changeNrOfDuelsOnClayByPlayerTwo,
                    changeNrOfTotalDuelsOnClay:                  this.changeNrOfTotalDuelsOnClay,

                    changeNrOfDuelsOnGrassByPlayerOne:           this.changeNrOfDuelsOnGrassByPlayerOne,
                    changeNrOfDuelsOnGrassByPlayerTwo:           this.changeNrOfDuelsOnGrassByPlayerTwo,
                    changeNrOfTotalDuelsOnGrass:                 this.changeNrOfTotalDuelsOnGrass,

                    changeNrOfDuelsOnHardByPlayerOne:            this.changeNrOfDuelsOnHardByPlayerOne,
                    changeNrOfDuelsOnHardByPlayerTwo:            this.changeNrOfDuelsOnHardByPlayerTwo,
                    changeNrOfTotalDuelsOnHard:                  this.changeNrOfTotalDuelsOnHard,

                    changeNrOfDuelsOnCarpetByPlayerOne:          this.changeNrOfDuelsOnCarpetByPlayerOne,
                    changeNrOfDuelsOnCarpetByPlayerTwo:          this.changeNrOfDuelsOnCarpetByPlayerTwo,
                    changeNrOfTotalDuelsOnCarpet:                this.changeNrOfTotalDuelsOnCarpet,

                }}
            >
                { this.props.children }
            </StatisticsContext.Provider>
        );
    }
}