import React , { useContext, useState, useEffect } from 'react';
import { AppContext } from '../AppContextProvider';

import styled from 'styled-components';

const TennisBallImage = styled.img `
    max-width: 50px;
    max-height: 50px;
    margin-right: 20px;
`;

const LocaleItem = styled.div `
    color: white;
    padding-left: 20px;
    font-weight: bold;
    cursor: pointer;
    &:hover {
        color: #28a745;
    }
`;

function NavigationBar(){
    const context =  useContext(AppContext);

    const [state, setState] = useState({
        grand_slams: []
    })

    useEffect(() => {
        fetch('http://localhost:8080/api/tourney/grand-slams', {
            method: 'GET',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json; charset=UTF-8; '
            },
        }).then( (response) => {
            return response.json() })   
                .then( (json) => {
                    //console.log(json);
                    setState({...state, grand_slams: json})
                });

    }, []);
    
    function renderGrandSlams(){
        return state.grand_slams.map( tourney => {
            return (
                <a className="dropdown-item" href={"/grand-slam/" + tourney.slug} key={tourney.tourney_id}>{tourney.name}</a>
            )
        })
    }

    return (
        <div>
            <nav className="navbar navbar-expand-lg navbar-dark bg-dark">
            <TennisBallImage src="./tennis-ball-ico.png" alt=""/>
            <a className="navbar-brand" href="/">Tennis Predictor (TP)</a>
            <button className="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                <span className="navbar-toggler-icon"></span>
            </button>

            <div className="collapse navbar-collapse" id="navbarSupportedContent">
                <ul className="navbar-nav mr-auto">
                    <li className="nav-item">
                        <a className="nav-link" href="/player">
                            { context.locales[context.actual].nav_menu_item_player }
                        </a>
                    </li>
                    <li className="nav-item">
                        <a className="nav-link" href="/tournament">
                            { context.locales[context.actual].nav_menu_item_tournaments }
                        </a>
                    </li>
                    <li className="nav-item dropdown">
                        <a className="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            { context.locales[context.actual].nav_menu_item_grand_slams }
                        </a>
                        <div className="dropdown-menu" aria-labelledby="navbarDropdown">
                            {renderGrandSlams()}
                        </div>
                    </li>
                </ul>
                <form className="form-inline my-2 my-lg-0">
                    <input className="form-control mr-sm-2" type="search" placeholder={context.locales[context.actual].nav_menu_item_search} aria-label="Search"/>
                    <button className="btn btn-outline-success my-2 my-sm-0" type="submit">
                        { context.locales[context.actual].nav_menu_item_search }
                    </button>

                    <LocaleItem onClick={() => context.changeLocation("hu")}> HU </LocaleItem>
                    <LocaleItem onClick={() => context.changeLocation("en")}> EN </LocaleItem>
                </form>
            </div>
            </nav>
        </div>
    )
}

export default NavigationBar;