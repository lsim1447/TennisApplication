import React , { useContext } from 'react';
import { AppContext } from '../AppContextProvider';

import styled from 'styled-components';

const FooterContainer = styled.div `
    bottom:0;
    min-height: 80px;
    background-color: #343a40;
    color: white;
    text-align: center;
    clear: both;
    position: relative;
`;

const FooterHeader = styled.div `
    font-size: 38px;
    padding-bottom: 12px;
`;

function Footer() {
    const context =  useContext(AppContext);

    return (
        <FooterContainer className="jumbotron">
            <div className="container">

                <FooterHeader className="font-italic font-weight-bold">
                { context.locales[context.actual].footer_header }
                </FooterHeader>
                
                <div className="row d-flex justify-content-center">

                    <div className="col-md-9">
                        <div className="embed-responsive embed-responsive-16by9 mb-4">
                            <iframe className="embed-responsive-item" src="https://www.youtube.com/embed/FlimVrUzCAw" allowFullScreen></iframe>
                        </div>
                    </div>

                </div>
                
                <p className="text-break font-italic font-weight-bold">
                    { context.locales[context.actual].footer_where_comes_data }
                </p>

                <div className="text-center">
                    <span className="font-italic"> © 2018 Copyright: </span> 
                    Tennis Predicor Match Results
                    <br/>
                    <span className="font-weight-bold">Lázár Szilárd</span>
                </div>
            </div>
        </FooterContainer>

    )
}

export default Footer;