import React, { Component } from 'react';
import Router from '../Router';
import NavigationBar from './NavigationBar';
import Footer from './Footer';
import AppContextProvider from "./../AppContextProvider";

class App extends Component {

  render() {
    return (
      <AppContextProvider>
         <NavigationBar />
         <Router />
         <Footer />
      </AppContextProvider>
    );
  }
}

export default App;
