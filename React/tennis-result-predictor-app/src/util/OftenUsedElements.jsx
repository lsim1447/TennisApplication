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