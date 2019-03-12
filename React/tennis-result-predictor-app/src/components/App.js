import React, { Component } from 'react';
import Router from '../Router';
import NavigationBar from './NavigationBar';
import Footer from './Footer';
import AppContextProvider from "./../AppContextProvider";
import PlayerContextProvider from './../context-providers/PlayerContextProvider';
import TournamentContextProvider from './../context-providers/TournamentContextProvider';

class App extends Component {

  render() {
    return (
      <AppContextProvider>
          <PlayerContextProvider>
            <TournamentContextProvider>
              <NavigationBar>
                <Router />
                <Footer />
              </NavigationBar>
            </TournamentContextProvider>
          </PlayerContextProvider>
      </AppContextProvider>
    );
  }
}

export default App;
