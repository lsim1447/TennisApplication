import styled from 'styled-components';
import { HOVER_COLOR, TABLE_BACKGROUND } from './../constants';

export const Clickable = styled.a `
    color: black;
    cursor: pointer;
    
    &: hover{
        text-decoration: none;
        color: ${HOVER_COLOR};
    }
`;

export const PlayerNameLink = styled.a `
    &:hover {
        color: ${HOVER_COLOR};
        text-decoration: none;
    }
`;

export const TableTournamentName = styled.div `
    border-top: 4px solid ${TABLE_BACKGROUND};
    border-right: 4px solid ${TABLE_BACKGROUND};
    border-left: 4px solid ${TABLE_BACKGROUND};
`;

export const NameTD = styled.td `
    width: 200px;
`;

export const MatchesContainer = styled.div `
    background-image: url('./../images/others/light-background.jpg');
    padding-top: ${props => props.isEmpty ? "0" : "15px"};
    padding-left: ${props => props.isEmpty ? "0" : "15px"};
    padding-right: ${props => props.isEmpty ? "0" : "15px"};
    padding-bottom: ${props => props.isEmpty ? "0" : "5px"};
`;

export const FlagIcon = styled.img `
    padding-left: 10px;
`;

export const PlayerName = styled.tr `
    font-size: 34px;
    color: #28a745;
`;

export const CardImage = styled.img `
    max-height: 400px;
`;

export const PlayerCardImage = styled.img `
    max-height: 400px;
`;

export const CustomSelect = styled.select `
    border: 2px solid rgba(0,0,0,.2);
    font-size: 18px;
    height: 40px;
    padding-left: 24px;
    width: 100%;    
`;

export const CustomOption = styled.option `

`;

export const SelectLabel = styled.label `
    font-size: 18px;
    margin-top: 15px;
    margin-bottom: 5px;
    text-align: left;
    width: 100%;
    font-weight: 600;
`;