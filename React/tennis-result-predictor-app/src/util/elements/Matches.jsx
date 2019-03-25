import React  from 'react';
import { MatchesContainer } from './../OftenUsedElements';
import { wonMatchesOn } from './../FunctionUtil';
import { renderStats } from '../renders/MatchResults';

export default function Matches(props){
    const {
        matches,
        grand_slam_matches,
        selectedPlayerOne,
        selectedPlayerTwo, 
        nrOfVisibleMatches, 
        same
    } = props;
    const isTour = props.isTour ? props.isTour : 0;

    function renderTabsOrNot(){
        if (isTour !== 0 || matches.length === 0) return;
        return (
            <ul className="nav nav-pills mb-3" id="pills-tab" role="tablist">
                <li className="nav-item">
                    <a className="nav-link active font-weight-bold" id="pills-latest-tab" data-toggle="pill" href="#pills-latest" role="tab" aria-controls="pills-latest" aria-selected="true"> Latest </a>
                </li>
                <li className="nav-item">
                    <a className="nav-link font-weight-bold" id="pills-grand-slam-tab" data-toggle="pill" href="#pills-grand-slam" role="tab" aria-controls="pills-grand-slam" aria-selected="false"> Grand Slams </a>
                </li>
                <div className="nav-item dropdown">
                    <button style={{color: "#007bff"}} className="btn btn-transparent dropdown-toggle font-weight-bold" type="button" id="dropdownMenuButton" data-toggle="dropdown">
                        Surface
                    </button>
                    <div className="dropdown-menu" aria-labelledby="dropdownMenuButton">
                        <a className="dropdown-item" id="pills-surface-hard-tab"   data-toggle="pill" href="#pills-surface-hard"   role="tab" aria-controls="pills-surface-hard"   aria-selected="false"> Hard   </a>
                        <a className="dropdown-item" id="pills-surface-clay-tab"   data-toggle="pill" href="#pills-surface-clay"   role="tab" aria-controls="pills-surface-clay"   aria-selected="false"> Clay   </a>
                        <a className="dropdown-item" id="pills-surface-grass-tab"  data-toggle="pill" href="#pills-surface-grass"  role="tab" aria-controls="pills-surface-grass"  aria-selected="false"> Grass  </a>
                        <a className="dropdown-item" id="pills-surface-carpet-tab" data-toggle="pill" href="#pills-surface-carpet" role="tab" aria-controls="pills-surface-carpet" aria-selected="false"> Carpet </a>
                    </div>
                </div>
            </ul>
        )
    }

    return (
        <MatchesContainer isEmpty={props.matches.length === 0}>
            { renderTabsOrNot() }
            <div className="tab-content" id="pills-tabContent">
                <div className="tab-pane fade show active" id="pills-latest" role="tabpanel" aria-labelledby="pills-latest-tab">
                    { renderStats(matches, selectedPlayerOne, selectedPlayerTwo, nrOfVisibleMatches, same, isTour) }
                </div>
                <div className="tab-pane fade" id="pills-grand-slam" role="tabpanel" aria-labelledby="pills-grand-slam-tab">
                    { renderStats(grand_slam_matches, selectedPlayerOne, selectedPlayerTwo, nrOfVisibleMatches, same, isTour) }
                </div>
                <div className="tab-pane fade" id="pills-surface-hard" role="tabpanel" aria-labelledby="pills-surface-hard-tab">
                    { renderStats(wonMatchesOn(matches, 'Hard'), selectedPlayerOne, selectedPlayerTwo, nrOfVisibleMatches, same, isTour) }
                </div>
                <div className="tab-pane fade" id="pills-surface-clay" role="tabpanel" aria-labelledby="pills-surface-clay-tab">
                    { renderStats(wonMatchesOn(matches, 'Clay'), selectedPlayerOne, selectedPlayerTwo, nrOfVisibleMatches, same, isTour) }
                </div>
                <div className="tab-pane fade" id="pills-surface-grass" role="tabpanel" aria-labelledby="pills-surface-grass-tab">
                    { renderStats(wonMatchesOn(matches, 'Grass'), selectedPlayerOne, selectedPlayerTwo, nrOfVisibleMatches, same, isTour) }
                </div>
                <div className="tab-pane fade" id="pills-surface-carpet" role="tabpanel" aria-labelledby="pills-surface-carpet-tab">
                    { renderStats(wonMatchesOn(matches, 'Carpet'), selectedPlayerOne, selectedPlayerTwo, nrOfVisibleMatches, same, isTour) }
                </div>
            </div>
        </MatchesContainer>
    )
}