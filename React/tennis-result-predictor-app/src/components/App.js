import React, { Component } from 'react';
import Router from '../Router';
import NavigationBar from './NavigationBar';
import Footer from './Footer';
import AppContextProvider from "./../AppContextProvider";
import PlayerContextProvider from './../context-providers/PlayerContextProvider';

class App extends Component {

  render() {
    return (
      <AppContextProvider>
          <PlayerContextProvider>
            <NavigationBar />
            <Router />
            <Footer />
          </PlayerContextProvider>
      </AppContextProvider>
    );
  }
}

export default App;
