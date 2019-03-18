import React , { useContext, useState } from 'react';
import styled from 'styled-components';
import { AUTOCOMPLITE_SHOW_FIRST_N, TABLE_BACKGROUND, WHITE, DEFALULT_SERVER_URL, VISIBLE_MATCHES } from '../../constants';
import { get_request } from '../../util/Request';
import AnimateHeight from 'react-animate-height';
import { PredicterContext } from './../../context-providers/PredicterContextProvider';
import { isGrandSlam } from './../../util/FunctionUtil';

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
    font-size: 18px;
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

function AutoCompletePredicter(props) {

    const [state, setState] = useState({
        suggestions: [],
        text: '',
        height: 0,
        selectedPlayer: {},
    })

    const contextPredicter = useContext(PredicterContext);

    function _handleEnterPress(e){
        if (e.key === 'Enter' && state.suggestions && state.suggestions.length > 0) {
          suggestionSelected(state.suggestions[0]);
        }
    }

    function onTextChange(e){
        const { items } = props;
        const value = e.target.value;
        let suggestions = [];

        if (value.length > 0){
            const regex = new RegExp(`${value}`, 'i');
            suggestions = items.sort().filter(v => {
                const fullname = v.firstName + ' ' + v.lastName;
                return regex.test(fullname)
            })
            setState({...state, suggestions: suggestions, text: value, height: 'auto'});
        } else {
            setState({...state, suggestions: suggestions, text: value, height: 0 });
        }
    }

    function renderSuggestions(){
        const suggestions = state.suggestions;
        if (suggestions.length === 0) {
            return null;
        }
        return suggestions.slice(0, AUTOCOMPLITE_SHOW_FIRST_N).map((item) => {
            return (
                <AutoCompleteLi key={item.player_id} onClick={() => suggestionSelected(item)}> { item.firstName } { item.lastName } </AutoCompleteLi>
            )
        }) 
    }

    function suggestionSelected(player){
        setState({
            ...state,
            text: '',
            selectedPlayer: player,
            suggestions: []
        });
        updatePlayerData(player);
    }

    function updatePlayerData(player){
        props.changeSelectedPlayer(player);

        get_request(`${DEFALULT_SERVER_URL}/tournament/won?slug=${encodeURIComponent(player.playerSlug)}`)
            .then(tournaments => {
                get_request(`${DEFALULT_SERVER_URL}/matches/player/last?slug=${encodeURIComponent(player.playerSlug)}&nr=${encodeURIComponent(VISIBLE_MATCHES)}`)
                    .then(lastmatches => {
                        props.changeTournaments(tournaments.reverse());
                        props.changeLastMatches(lastmatches);
                        
                        let { selectedPlayerOne, selectedPlayerTwo } = contextPredicter;
                        if (props.which == 1){
                            selectedPlayerOne = player;
                        } else {
                            selectedPlayerTwo = player;
                        }
                        if (selectedPlayerOne.firstName && selectedPlayerTwo.firstName &&
                            selectedPlayerOne.firstName !== selectedPlayerTwo.firstName){
                            
                            get_request(`${DEFALULT_SERVER_URL}/matches/player/all/between/two?playerOne=${encodeURIComponent(selectedPlayerOne.playerSlug)}&playerTwo=${encodeURIComponent(selectedPlayerTwo.playerSlug)}`)
                                .then(common_matches => {
                                    const grand_slam_matches = common_matches.filter(match => {
                                        return isGrandSlam(match.tournament)
                                    })
                                    props.changeGrandSlamMatchesBetweenTwo(grand_slam_matches);
                                    props.changeLastMatchesBetweenTwo(common_matches);
                                })
                        }
                    })
            })
    }

    return(
        <AutoCompleteTextContainer>
            <AutoCompleteInput type="text" value={state.text} placeholder={props.placeholder} onChange={(e) => onTextChange(e)} onKeyUp={(e) => _handleEnterPress(e)}/>
                <AutoCompleteUl>
                    <AnimateHeight
                        duration={ 750 }
                        height={ state.height }
                    >
                        { renderSuggestions() }
                    </AnimateHeight>
                </AutoCompleteUl>
        </AutoCompleteTextContainer>
    )
}

export default AutoCompletePredicter;