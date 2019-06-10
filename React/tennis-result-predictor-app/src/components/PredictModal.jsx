import React , { useContext, useState, useEffect } from 'react';
import 'react-circular-progressbar/dist/styles.css';
import styled from 'styled-components';
import { get_request } from '../util/Request';
import { DEFALULT_SERVER_URL, all_matches_marks, matches_on_seleceted_surface_marks, head_to_head_matches_marks } from '../constants';
import { PredicterContext }  from './../context-providers/PredicterContextProvider';
import { SelectLabel, Icon } from './../util/OftenUsedElements';
import CircularProgressbar from 'react-circular-progressbar';
import Select from 'react-select';
import Slider from 'rc-slider';
import 'rc-slider/assets/index.css';
import 'rc-tooltip/assets/bootstrap.css';

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
    color: white;
`;

const ProbabilityContainer = styled.div `
    background-image: url('./../images/others/modal-bet-container.jpg');
    border: 1px solid rgba(0,0,0,.2);
    margin-top: 24px;
    padding-top: 24px;
    padding-bottom: 24px;
`;

const SliderContainer = styled.div `
    .rc-slider-mark {
        font-size: 18px;
    }
    padding-bottom: 45px;
    margin-top: 25px;
`;

const SlidersContainer = styled.div `
    margin-top: 24px;
    border: 2px solid rgba(0,0,0,.125);;
    padding: 10px 20px 10px 20px;

    @media (max-width: 992px) {
        padding: 0px;
    }
`;

function PredictModal(props){

    const contextPredicter =  useContext(PredicterContext);

    const {
        selectedPlayerOne,
        selectedPlayerTwo,
    } = contextPredicter;

    const [state, setState] = useState({
        tourneys: [],
        selectedTourney: null,
        selectedSurface: null,
        firstPlayerProbability: 50,
        secondPlayerProbability: 50,

        nrOfAllCheckedMatches: 20,
        nrOfCheckedMatchesOnSelectedSurface: 5,
        nrOfHeadToHeadMatches: 2,

        isLoading: true,
        surfaceOptions: [
            { value: 'Hard', label: 'Hard'},
            { value: 'Grass', label: 'Grass'},
            { value: 'Clay', label: 'Clay'},
            { value: 'Carpet', label: 'Carpet'},
        ],
        tournamentOptions: [],
    });

    function convertTourneysToOption(tourneys){
        return tourneys.map(tourney => {
            return {
                value: tourney.surface,
                label: tourney.name
            }
        })
    }

    function sliderAllValueChanged(value){
        setState({...state, nrOfAllCheckedMatches: value});
    }
    function sliderSurfaceValueChanged(value){
        setState({...state, nrOfCheckedMatchesOnSelectedSurface: value});
    }
    function sliderHeadToHeadValueChanged(value){
        setState({...state, nrOfHeadToHeadMatches: value});
    }

    useEffect(() => {
        get_request(`${DEFALULT_SERVER_URL}/tourney/all`)
            .then(tourneys => {
                setState({...state, tourneys: tourneys, tournamentOptions: convertTourneysToOption(tourneys)})
                
            })
    }, []);

    function surfaceOnChanged(surface){
        const  tournament_options = convertTourneysToOption(state.tourneys.filter(t => t.surface === surface.value));

        setState({ ...state, tournamentOptions: tournament_options, selectedSurface: surface, selectedTourney: tournament_options[0] })
    }

    function tourneyOnChanged(tourney){
        const selectedSurface = state.surfaceOptions.filter(t => t.value === tourney.value);
        setState({ ...state, selectedTourney: tourney, selectedSurface: selectedSurface})
    }

    function predictResult(e){
        const selectedSurface = state.selectedTourney.value;
        const selectedTourney = state.selectedTourney.label;
        
        /*
        get_request(`${DEFALULT_SERVER_URL}/prediction/training`)
            .then( response => {
                console.log('response waze = ', response)
            })
        */
           
        get_request(`${DEFALULT_SERVER_URL}/prediction/calculate?playerOneSlug=${encodeURIComponent(selectedPlayerOne.playerSlug)}&playerTwoSlug=${encodeURIComponent(selectedPlayerTwo.playerSlug)}&surface=${encodeURIComponent(selectedSurface)}&tourneyName=${encodeURIComponent(selectedTourney)}&nrOfAllCheckedMatches=${encodeURIComponent(state.nrOfAllCheckedMatches)}&nrOfCheckedMatchesOnSelectedSurface=${encodeURIComponent(state.nrOfCheckedMatchesOnSelectedSurface)}&nrOfHeadToHeadMatches=${encodeURIComponent(state.nrOfHeadToHeadMatches)}`)
            .then( response => {
                setState({...state, firstPlayerProbability: response[0], secondPlayerProbability: response[1]})
            })
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
                                <div className="col-lg">
                                    <p>
                                        <a style={{marginTop: "24px"}} className="btn btn-primary" data-toggle="collapse" href="#collapseExample" role="button" aria-expanded="false" aria-controls="collapseExample">
                                            Settings
                                            <Icon src="./../images/icons/settings.jpg" alt="" />
                                        </a>
                                        
                                    </p>
                                    <div className="collapse" id="collapseExample">
                                        <div className="alert alert-danger alert-dismissible fade show" role="alert">
                                            <strong>Be careful!</strong> These settings affect the outcome of the prediction!
                                            <button type="button" className="close" data-dismiss="alert" aria-label="Close">
                                                <span aria-hidden="true">&times;</span>
                                            </button>
                                        </div>

                                        <div className="row">
                                            <div className="col col-lg">
                                                <SelectLabel>Surface:</SelectLabel>
                                                <Select value={state.selectedSurface} options={state.surfaceOptions} onChange={(e) => surfaceOnChanged(e)}></Select>
                                            </div>
                                            <div className="col col-lg">
                                                <SelectLabel>Tournament:</SelectLabel>
                                                <Select value={state.selectedTourney} options={state.tournamentOptions} onChange={(e) => tourneyOnChanged(e)}></Select>
                                            </div>
                                        </div>
                                        <SlidersContainer>
                                            <h1>Advanced settings</h1>
                                            <SliderContainer className="row">
                                                <div className="col col-lg">
                                                    <SelectLabel>ALL (latest):</SelectLabel>
                                                    <Slider min={0} max={100} defaultValue={state.nrOfAllCheckedMatches} marks={all_matches_marks} step={null} onChange={(e) => sliderAllValueChanged(e)}/>
                                                </div>
                                            </SliderContainer>

                                            <SliderContainer className="row">
                                                <div className="col col-lg">
                                                    <SelectLabel>On the selected surface (latest):</SelectLabel>
                                                    <Slider min={0} max={30} defaultValue={state.nrOfCheckedMatchesOnSelectedSurface} marks={matches_on_seleceted_surface_marks} step={null} onChange={(e) => sliderSurfaceValueChanged(e)}/>
                                                </div>
                                            </SliderContainer>

                                            <SliderContainer className="row">
                                                <div className="col col-lg">
                                                    <SelectLabel>Head to Head (latest):</SelectLabel>
                                                    <Slider min={0} max={10} defaultValue={state.nrOfHeadToHeadMatches} marks={head_to_head_matches_marks} step={null} onChange={(e) => sliderHeadToHeadValueChanged(e)}/>
                                                </div>
                                            </SliderContainer>
                                        </SlidersContainer>
                                    </div>
                                </div>
                            </div>

                            <ProbabilityContainer>
                                <div className="row justify-content-md-center">
                                    <div className="col col-lg-4">
                                        <ProbabilityLabel> { selectedPlayerOne.firstName } { selectedPlayerOne.lastName }'s probability</ProbabilityLabel>
                                    </div>
                                    
                                    <div className="col col-lg-4">
                                        <ProbabilityLabel> { selectedPlayerTwo.firstName } { selectedPlayerTwo.lastName }'s probability</ProbabilityLabel>
                                    </div>
                                </div>
                                <div className="row justify-content-md-center">
                                    <div className="col col-lg-4">
                                        <div style={{maxWidth: "150px", marginLeft: "25%"}}>
                                            <CircularProgressbar 
                                                percentage={state.firstPlayerProbability} 
                                                text={`${state.firstPlayerProbability}%`} 
                                                background
                                                backgroundPadding={6}
                                                styles={{
                                                    background: {
                                                        fill: '#3e98c7',
                                                    },
                                                    text: {
                                                        fill: '#fff',
                                                    },
                                                    path: {
                                                        stroke: '#fff',
                                                    },
                                                    trail: { stroke: 'transparent' },
                                                }}
                                            />
                                        </div>
                                    </div>
                                    
                                    <div className="col col-lg-4">
                                        <div style={{maxWidth: "150px", marginLeft: "25%"}}>
                                            <CircularProgressbar 
                                                percentage={state.secondPlayerProbability} 
                                                text={`${state.secondPlayerProbability}%`} 
                                                background
                                                backgroundPadding={6}
                                                styles={{
                                                    background: {
                                                        fill: '#3e98c7',
                                                    },
                                                    text: {
                                                        fill: '#fff',
                                                    },
                                                    path: {
                                                        stroke: '#fff',
                                                    },
                                                    trail: { stroke: 'transparent' },
                                                }}
                                            />
                                        </div>
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