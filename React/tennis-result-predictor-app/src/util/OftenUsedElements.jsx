import styled from 'styled-components';
import { HOVER_COLOR } from './../constants';

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