import React, { Component } from 'react';

export const TournamentContext = React.createContext();
export const TournamentContextConsumer = TournamentContext.Consumer;

export default class TournamentContextProvider extends Component {

    state = {
        selectedTourney: {},
        tournaments: [],
        all_matches: [],
        last_matches: []
    };

    changeSelectedTourney = (newTourney) => {
        this.setState({selectedTourney: newTourney})
    }

    changeTournaments = (newTournaments) => {
        this.setState({tournaments: newTournaments});
    }

    changeAllMatches = (allMatches) => {
        this.setState({all_matches: allMatches});
    }

    changeLastMatches = (newLastMatches) => {
        this.setState({last_matches: newLastMatches});
    }

    render(){
        return (
            <TournamentContext.Provider value={{ ...this.state, changeSelectedTourney: this.changeSelectedTourney, changeTournaments: this.changeTournaments, changeLastMatches: this.changeLastMatches, changeAllMatches: this.changeAllMatches}}>
                { this.props.children }
            </TournamentContext.Provider>
        );
    }
}