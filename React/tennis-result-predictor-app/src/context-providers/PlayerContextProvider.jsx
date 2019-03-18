import React, { Component } from 'react';

export const PlayerContext = React.createContext();
export const PlayerContextConsumer = PlayerContext.Consumer;

export default class PlayerContextProvider extends Component {

    state = {
        selectedPlayer: {},
        tournaments: [],
        grand_slams: [],
        last_matches: []
    };

    changeSelectedPlayer = (newPlayer) => {
        this.setState({selectedPlayer: newPlayer})
    }

    changeTournaments = (newTournaments) => {
        this.setState({tournaments: newTournaments});
    }

    changeGrandSlams = (newGrandSlams) => {
        this.setState({grand_slams: newGrandSlams});
    }

    changeLastMatches = (newLastMatches) => {
        this.setState({last_matches: newLastMatches});
    }

    render(){
        return (
            <PlayerContext.Provider value={{ ...this.state, changeSelectedPlayer: this.changeSelectedPlayer, changeTournaments: this.changeTournaments, changeGrandSlams: this.changeGrandSlams,  changeLastMatches: this.changeLastMatches}}>
                { this.props.children }
            </PlayerContext.Provider>
        );
    }
}