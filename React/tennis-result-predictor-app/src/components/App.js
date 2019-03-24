import React, { Component } from 'react';
import Router from '../Router';
import NavigationBar from './NavigationBar';
import Footer from './Footer';
import AppContextProvider from "./../AppContextProvider";
import PlayerContextProvider from './../context-providers/PlayerContextProvider';
import TournamentContextProvider from './../context-providers/TournamentContextProvider';
import PredicterContextProvider from './../context-providers/PredicterContextProvider';
import StatisticsContextProvider from './../context-providers/StatisticsContextProvider';

class App extends Component {

  render() {
    return (
      <AppContextProvider>
          <PlayerContextProvider>
            <TournamentContextProvider>
              <PredicterContextProvider>
                <StatisticsContextProvider>
                  <NavigationBar>
                    <Router />
                    <Footer />
                  </NavigationBar>
                </StatisticsContextProvider>
              </PredicterContextProvider>
            </TournamentContextProvider>
          </PlayerContextProvider>
      </AppContextProvider>
    );
  }
}

export default App;
