import React from 'react';
import styled from 'styled-components';
import { AUTOCOMPLITE_SHOW_FIRST_N, TABLE_BACKGROUND, WHITE, DEFALULT_SERVER_URL, VISIBLE_MATCHES } from './../../constants';
import { get_request, post_request } from '../../util/Request';
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

export default class AutoCompleteTournament extends React.Component {
    constructor(props){
        super(props);

        this.state = {
            suggestions: [],
            text: '',
            height: 0,
            selectedTourney: {},
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
            suggestions = items.sort().filter(t => {
                return regex.test(t.name)
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
                <AutoCompleteLi key={item.tourney_id} onClick={() => this.suggestionSelected(item)}> { item.name } </AutoCompleteLi>
            )
        }) 
        
    }

    suggestionSelected(tourney){
        this.setState(() => ({
            text: '',
            selectedTourney: tourney,
            suggestions: []
        }));
        this.updateData(tourney);
    }

    updateData(tourney){
        this.props.changeSelectedTourney(tourney);
        post_request(`${DEFALULT_SERVER_URL}/tournament/tourney`, tourney)
            .then( tournaments => {
                this.props.changeTournaments(tournaments.reverse());
                get_request(`${DEFALULT_SERVER_URL}/matches/tourney?id=${encodeURIComponent(tourney.tourney_id)}`)
                    .then( matches => {
                        this.props.changeLastMatches(matches);
                        this.props.changeAllMatches(matches);
                    });
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