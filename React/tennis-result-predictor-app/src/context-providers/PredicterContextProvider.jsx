import React, { Component } from 'react';

export const PredicterContext = React.createContext();
export const PredicterContextConsumer = PredicterContext.Consumer;

export default class PredicterContextProvider extends Component {

    state = {
        selectedPlayerOne: {},
        selectedPlayerTwo: {},

        last_matches_player_one: [],
        last_matches_player_two: [],

        tournaments_player_one: [],
        tournaments_player_two: [],

        matches_between_those_two: [],
        grand_slam_matches_between_those_two: [],

        statistics: {}
    };

    changeStatistics = (newStatistics) => {
        this.setState({statistics: newStatistics})
    }

    changeSelectedPlayerOne = (newPlayer) => {
        this.setState({selectedPlayerOne: newPlayer})
    }

    changeSelectedPlayerTwo = (newPlayer) => {
        this.setState({selectedPlayerTwo: newPlayer})
    }

    changePlayerOneTournaments = (newTournaments) => {
        this.setState({tournaments_player_one: newTournaments});
    }

    changePlayerTwoTournaments = (newTournaments) => {
        this.setState({tournaments_player_two: newTournaments});
    }

    changePlayerOneLastMatches = (newLastMatches) => {
        this.setState({last_matches_player_one: newLastMatches});
    }

    changePlayerTwoLastMatches = (newLastMatches) => {
        this.setState({last_matches_player_two: newLastMatches});
    }

    changeLastMatchesBetweenTwo = (newLastMatchesBetweenTwo) => {
        this.setState({matches_between_those_two: newLastMatchesBetweenTwo});
    }

    changeGrandSlamMatchesBetweenTwo = (newGrandSlamMatchesBetweenTwo) => {
        this.setState({grand_slam_matches_between_those_two: newGrandSlamMatchesBetweenTwo});
    }

    render(){
        return (
            <PredicterContext.Provider 
                value={{ ...this.state,
                    changeSelectedPlayerOne:          this.changeSelectedPlayerOne,
                    changeSelectedPlayerTwo:          this.changeSelectedPlayerTwo,
                    changePlayerOneTournaments:       this.changePlayerOneTournaments,
                    changePlayerTwoTournaments:       this.changePlayerTwoTournaments,
                    changePlayerOneLastMatches:       this.changePlayerOneLastMatches,
                    changePlayerTwoLastMatches:       this.changePlayerTwoLastMatches,
                    changeLastMatchesBetweenTwo:      this.changeLastMatchesBetweenTwo,
                    changeGrandSlamMatchesBetweenTwo: this.changeGrandSlamMatchesBetweenTwo
                }}
            >
                { this.props.children }
            </PredicterContext.Provider>
        );
    }
}