import React, { Component } from 'react';
import { BrowserRouter, Switch, Route } from 'react-router-dom'

import Home from './routes/Home';
import PageNotFound from './routes/PageNotFound';
import CustomPlayer from './routes/CustomPlayer';
import Tournament from './routes/Tournament';
import GrandSlam from './routes/GrandSlam';
import Stats from './routes/Stats';
import CustomTournament from './routes/CustomTournament';
import Player from './routes/Player';

class Router extends Component {
    render(){
        return (
            <BrowserRouter>
                <Switch>
                    <Route exact path="/" render={ () => <Home />}/>
                    <Route exact path="/tournament" render={ (props) => <Tournament {...props}/>} />
                    <Route exact path="/tournament/:id" render={ (props) => <CustomTournament {...props}/>} />
                    <Route exact path="/grand-slam/:id" render={ (props) => <GrandSlam {...props}/>} />
                    <Route exact path="/players" render={ (props) => <Player {...props}/>} />
                    <Route exact path="/players/:slug" render={ (props) => <CustomPlayer {...props}/>} />
                    <Route exact path="/match/stats/:id" render={ (props) => <Stats {...props}/>} />
                    <Route render={ (props) => <PageNotFound {...props}/>} />
                </Switch>
            </BrowserRouter>
        );
    }
}

export default Router;