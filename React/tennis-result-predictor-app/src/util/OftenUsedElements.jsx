import styled from 'styled-components';
import { HOVER_COLOR, TABLE_BACKGROUND } from './../constants';

export const Clickable = styled.a `
    cursor: pointer;
    color: black;
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
    border-left: 4px solid ${TABLE_BACKGROUND};
    border-right: 4px solid ${TABLE_BACKGROUND};
    border-top: 4px solid ${TABLE_BACKGROUND};
`;

export const NameTD = styled.td `
    width: 200px;
`;

export const MatchesContainer = styled.div `
    padding-top: 15px;
    padding-left: 15px;
    padding-bottom: 5px;
    background-image: url('./../images/others/light-background.jpg');
`;

export const FlagIcon = styled.img `
    padding-left: 10px;
`;

export const PlayerName = styled.tr `
    color: #28a745;
    font-size: 34px;
`;

export const CardImage = styled.img `
    max-height: 400px;
`;

export const PlayerCardImage = styled.img `
    max-height: 400px;
`;