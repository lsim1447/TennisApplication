import React , { Component } from 'react';
import styled from 'styled-components';

const ErrorTitle = styled.div `
    text-align: center;
    font-size: 50px;
    padding-top: 350px;
    display: block;
`;

const GoToTheHomePage = styled.div `
    font-size: 24px;
    color: black;
    text-align: center;
    font-style: italic;
    &:hover {
        color: red;
    }
`;

class PageNotFound extends Component {

    onClick(){
        this.props.history.push("/");
    }

    render(){
        return (
            <div>
                <ErrorTitle> 404    -    Page not found </ErrorTitle>
                <GoToTheHomePage 
                    onClick={() => {this.onClick()}}>
                    Go back to the home page
                </GoToTheHomePage>
            </div>
        )
    }
}

export default PageNotFound;