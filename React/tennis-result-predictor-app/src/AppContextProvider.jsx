import React, { Component } from 'react';

import en from './locales/en.json';
import hu from './locales/hu.json';

const locales = {en, hu};


export const AppContext = React.createContext();
export const AppContextConsumer = AppContext.Consumer;

export default class AppContextProvider extends Component {

    state = {
        locales: locales,
        actual: 'en'
    };

    changeLocation = (actual) => {
        this.setState({actual: actual})
    }

    render(){
        return (
            <AppContext.Provider value={{ ...this.state, changeLocation: this.changeLocation }}>
                { this.props.children }
            </AppContext.Provider>
        );
    }
}