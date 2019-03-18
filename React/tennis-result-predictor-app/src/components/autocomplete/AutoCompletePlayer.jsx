import React from 'react';
import styled from 'styled-components';
import { AUTOCOMPLITE_SHOW_FIRST_N, TABLE_BACKGROUND, WHITE, DEFALULT_SERVER_URL, VISIBLE_MATCHES } from '../../constants';
import { get_request } from '../../util/Request';
import AnimateHeight from 'react-animate-height';

const AutoCompleteTextContainer = styled.div `
    width: 100%;
    border: 1px solid grey;
    box-shadow: 5px 5px 5px rgba(68, 68, 68, 0.6);
    font-family: Arial, Helvetica, sans-serif;
    font-size: 14px;
    color: black;
`;

const AutoCompleteInput = styled.input `
    width: 100%;
    border: none;

    font-family: Arial, Helvetica, sans-serif;
    font-size: 22px;
    color: black;
    padding: 10px 25px;
    box-sizing: border-box;
    outline: none;
    text-align: center;
`;

const AutoCompleteUl = styled.ul `
    list-style-type: none;
    text-align: center;
    margin: 0;
    padding: 0;
    border-top: 1px solid grey;
    color: ${WHITE};
    font-weight: bold;

    &: before {
        content: ""
    }
`;

const AutoCompleteLi = styled.li `
    padding: 10px 5px;
    cursor: pointer;
    background-color: ${TABLE_BACKGROUND}

    &: hover{
        background-color:  ${WHITE};
        color: ${TABLE_BACKGROUND};
    }
`;

export default class AutoCompletePlayer extends React.Component {
    constructor(props){
        super(props);

        this.state = {
            suggestions: [],
            text: '',
            height: 0,
            selectedPlayer: {},
        }
    }

    _handleEnterPress = (e) => {
        if (e.key === 'Enter') {
          this.suggestionSelected(this.state.suggestions[0]);
        }
    }

    onTextChange = (e) => {
        const { items } = this.props;
        const value = e.target.value;
        let suggestions = [];

        if (value.length > 0){
            const regex = new RegExp(`${value}`, 'i');
            suggestions = items.sort().filter(v => {
                const fullname = v.firstName + ' ' + v.lastName;
                return regex.test(fullname)
            })
            this.setState(() => ({ suggestions: suggestions, text: value, height: 'auto' }))
        } else {
            this.setState(() => ({ suggestions: suggestions, text: value, height: 0 }))
        }
    }

    renderSuggestions() {
        const { suggestions } = this.state;
        if (suggestions.length === 0) {
            return null;
        }
        return suggestions.slice(0, AUTOCOMPLITE_SHOW_FIRST_N).map((item) => {
            return (
                <AutoCompleteLi key={item.player_id} onClick={() => this.suggestionSelected(item)}> { item.firstName } { item.lastName } </AutoCompleteLi>
            )
        }) 
        
    }

    suggestionSelected(player){
        this.setState(() => ({
            text: '',
            selectedPlayer: player,
            suggestions: []
        }));
        this.updatePlayerData(player);
    }

    updatePlayerData(player){
        this.props.changeSelectedPlayer(player);

        get_request(`${DEFALULT_SERVER_URL}/tournament/won?slug=${encodeURIComponent(player.playerSlug)}`)
            .then(tournaments => {
                get_request(`${DEFALULT_SERVER_URL}/matches/player/last?slug=${encodeURIComponent(player.playerSlug)}&nr=${encodeURIComponent(VISIBLE_MATCHES)}`)
                    .then(lastmatches => {
                        const won_grand_slams = tournaments.filter(tournament => {
                            return (tournament.tourney.tourney_id === '520') || (tournament.tourney.tourney_id === '540') || (tournament.tourney.tourney_id === '560') || (tournament.tourney.tourney_id === '580');
                        })
                        this.props.changeTournaments(tournaments.reverse());
                        this.props.changeLastMatches(lastmatches);
                        this.props.changeGrandSlams(won_grand_slams);
                    })
            })
    }

    render(){
        const { text } = this.state;
        return(
            <AutoCompleteTextContainer>
                <AutoCompleteInput type="text" value={text} placeholder={this.props.placeholder} onChange={(e) => this.onTextChange(e)} onKeyUp={(e) => this._handleEnterPress(e)}/>
                    <AutoCompleteUl>
                        <AnimateHeight
                            duration={ 750 }
                            height={ this.state.height }
                        >
                            { this.renderSuggestions() }
                        </AnimateHeight>
                    </AutoCompleteUl>
            </AutoCompleteTextContainer>
        )
    }
}