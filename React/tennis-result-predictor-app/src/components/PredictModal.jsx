import React , { useContext, useState, useEffect } from 'react';
import 'react-circular-progressbar/dist/styles.css';
import styled from 'styled-components';
import { isGrandSlam, wonMatchesBy, wonMatchesOn } from './../util/FunctionUtil';
import { get_request } from '../util/Request';
import { DEFALULT_SERVER_URL } from '../constants';
import { PredicterContext }  from './../context-providers/PredicterContextProvider';
import { StatisticsContext } from './../context-providers/StatisticsContextProvider';
import { CustomSelect, CustomOption, SelectLabel } from './../util/OftenUsedElements';
import CircularProgressbar from 'react-circular-progressbar';


const VSImageContainer = styled.div `
    @media (max-width: 992px) {
    }
    @media (min-width: 992px) {
        margin-right: 24px;
        padding-top: 75px;
    }
`;

const ProbabilityLabel = styled.label `
    font-size: 32px;
    font-weight: bold;
`;

const ProbabilityContainer = styled.div `
    background-color: white;
    border: 1px solid rgba(0,0,0,.2);
    margin-top: 24px;
    padding-top: 24px;
    padding-bottom: 24px;
`;

function PredictModal(props){

    const contextPredicter =  useContext(PredicterContext);
    const contextStatistics = useContext(StatisticsContext);

    console.log('contextStatistics = ', contextStatistics);
    const {
        selectedPlayerOne,
        selectedPlayerTwo,
        last_matches_player_one,
        last_matches_player_two,
        tournaments_player_one,
        tournaments_player_two,
        matches_between_those_two,
        grand_slam_matches_between_those_two,
    } = contextPredicter;

    const [state, setState] = useState({
        surfaces: ["Hard", "Clay", "Grass", "Carpet"],
        tourneys: [],
        possibleTourneys: [],
        selectedSurface: '',
        firstPlayerProbability: 64,
        secondPlayerProbability: 36,
        isLoading: true
    });


    useEffect(() => {
        get_request(`${DEFALULT_SERVER_URL}/tourney/all`)
            .then(tourneys => {
                setState({...state, tourneys: tourneys, possibleTourneys: tourneys})
            })
    }, []);

    function renderSurfaceOptions(list){
        return list.map((element, index) => {
            if (element === state.selectedSurface){
                return (
                    <CustomOption key={index} value={element} selected>{element}</CustomOption>
                )
            } else {
                return (
                    <CustomOption key={index} value={element}>{element}</CustomOption>
                )
            }
        })
    }

    function renderTourneyOptions(list){
        return list.map((element, index) => {
            return (
                <CustomOption key={index} value={element}>{element}</CustomOption>
            )
        })
    }

    function surfaceChange(e){
        const value = e.target.value;
        const possibleTourneys = state.tourneys.filter(t => t.surface === value);
        setState({...state, possibleTourneys: possibleTourneys })
    }

    function tourneyChange(e){
        const tourneyName = e.target.value;
        let tmp_surface = '';
        state.tourneys.forEach(element => {
            if (element.name === tourneyName) {
                tmp_surface = element.surface;
                return;
            }
        });
        setState({...state, selectedSurface: tmp_surface});
    }

    function predictResult(e){
        const selectedTournament = document.getElementById("tourney_dropdown").value;
        const selectedSurface = document.getElementById("surface_dropdown").value;
        
        const nrOfCheckedMatches = 100;
        const wonMatchesByPlayerOne = (wonMatchesBy(last_matches_player_one.slice(0, nrOfCheckedMatches), selectedPlayerOne)).length;
        const wonMatchesByPlayerTwo = (wonMatchesBy(last_matches_player_two.slice(0, nrOfCheckedMatches), selectedPlayerTwo)).length;

        const nrOfWonMatchesAgainsOpponentP1 = (wonMatchesBy(matches_between_those_two, selectedPlayerOne)).length;
        const nrOfWonMatchesAgainsOpponentP2 = (wonMatchesBy(matches_between_those_two, selectedPlayerTwo)).length;

        const nrOfWonGrandSlamMatchesAgainstOpponentP1 = (wonMatchesBy(grand_slam_matches_between_those_two, selectedPlayerOne)).length;
        const nrOfWonGrandSlamMatchesAgainstOpponentP2 = (wonMatchesBy(grand_slam_matches_between_those_two, selectedPlayerTwo)).length;
        
        console.log('wonMatchesByPlayerOne = ', wonMatchesByPlayerOne);
        console.log('wonMatchesByPlayerTwo = ', wonMatchesByPlayerTwo);
        console.log('nrOfWonMatchesAgainsOpponentP1 = ', nrOfWonMatchesAgainsOpponentP1);
        console.log('nrOfWonMatchesAgainsOpponentP2 = ', nrOfWonMatchesAgainsOpponentP2);
        console.log('nrOfWonGrandSlamMatchesAgainstOpponentP1 = ', nrOfWonGrandSlamMatchesAgainstOpponentP1);
        console.log('nrOfWonGrandSlamMatchesAgainstOpponentP2 = ', nrOfWonGrandSlamMatchesAgainstOpponentP2);

        const tmp1 = Math.floor(Math.random() * 100) + 1;
        const tmp2 = 100 - tmp1;
        setState({...state, firstPlayerProbability: tmp1, secondPlayerProbability: tmp2})
    }

    return (
        <div className="modal fade bd-example-modal-xl" id="exampleModal" tabIndex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
            <div className="modal-dialog modal-dialog-centered modal-xl" role="document">
                <div className="modal-content">
                    <div className="modal-header">
                        <h3 className="modal-title" id="exampleModalLabel">Let's predict the result of the match</h3>
                        <button type="button" className="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div className="modal-body">
                        <div className="container-fluid">
                            <div className="row">
                                <div className="col-lg">
                                    <div className="card">
                                        <img src={`./../images/players/${selectedPlayerOne.playerSlug}.jpg`} onError={(e)=>{e.target.onerror = null; e.target.src="./../../images/players/unknown.jpg"}} className="card-img" alt="..."/>
                                        <div className="card-body">
                                            <h5 className="card-title"> { selectedPlayerOne.firstName } { selectedPlayerOne.lastName } </h5>
                                        </div>
                                    </div>
                                </div>
                                <VSImageContainer className="col-lg-2">
                                    <img src="https://www.meat-me.fr/wp-content/uploads/2016/11/versus.png" alt="" />
                                </VSImageContainer>
                                <div className="col-lg">
                                    <div className="card">
                                        <img src={`./../images/players/${selectedPlayerTwo.playerSlug}.jpg`} onError={(e)=>{e.target.onerror = null; e.target.src="./../../images/players/unknown.jpg"}} className="card-img" alt="..."/>
                                        <div className="card-body">
                                            <h5 className="card-title"> { selectedPlayerTwo.firstName } { selectedPlayerTwo.lastName } </h5>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="row">
                                <div className="col col-lg">
                                    <SelectLabel>Surface:</SelectLabel>
                                    <CustomSelect id="surface_dropdown" onChange={(e) => surfaceChange(e)}>
                                        { renderSurfaceOptions(state.surfaces) }
                                    </CustomSelect>
                                </div>
                                <div className="col col-lg">
                                    <SelectLabel>Tournament:</SelectLabel>
                                    <CustomSelect id="tourney_dropdown" onChange={(e) => tourneyChange(e)}>
                                        { renderTourneyOptions(state.possibleTourneys.map(t => t.name).sort()) }
                                    </CustomSelect>
                                </div>
                            </div>
                            <ProbabilityContainer className="row justify-content-md-center">
                                <div className="col col-lg-4">
                                    <ProbabilityLabel> { selectedPlayerOne.firstName } { selectedPlayerOne.lastName }'s probability</ProbabilityLabel>
                                    <div>
                                        <CircularProgressbar style={{maxWidth: "100px"}} percentage={state.firstPlayerProbability} text={`${state.firstPlayerProbability}%`} />
                                    </div>
                                </div>
                                
                                <div className="col col-lg-4">
                                    <ProbabilityLabel> { selectedPlayerTwo.firstName } { selectedPlayerTwo.lastName }'s probability</ProbabilityLabel>
                                    <div>
                                        <CircularProgressbar style={{maxWidth: "100px"}} percentage={state.secondPlayerProbability} text={`${state.secondPlayerProbability}%`} />
                                    </div>
                                </div>
                            </ProbabilityContainer>
                        </div>
                    </div>
                    <div className="modal-footer">
                        <button style={{paddingLeft: "50px", paddingRight: "50px"}} type="button" className="btn btn-secondary" data-dismiss="modal">Close</button>
                        <button type="button" className="btn btn-primary" onClick={(e) => predictResult(e)}> Start prediction - let's duel </button>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default PredictModal;