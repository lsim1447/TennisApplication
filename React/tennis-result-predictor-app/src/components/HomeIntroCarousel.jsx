import React , { useContext } from 'react';
import styled from 'styled-components';
import { AppContext } from '../AppContextProvider';

const Carousel = styled.div `
    @media (min-width: 768px) {
        margin-left: 20%;
        margin-right: 20%;
        margin-top: 5%;
    }
`;

function HomeIntroCarousel() {

    const context =  useContext(AppContext);

    return (
        <Carousel className="bd-example center">
            <div id="carouselExampleCaptions" className="carousel slide" data-ride="carousel">
                <ol className="carousel-indicators">
                    <li data-target="#carouselExampleCaptions" data-slide-to="0" className="active"></li>
                    <li data-target="#carouselExampleCaptions" data-slide-to="1"></li>
                    <li data-target="#carouselExampleCaptions" data-slide-to="2"></li>
                    <li data-target="#carouselExampleCaptions" data-slide-to="3"></li>
                </ol>
                <div className="carousel-inner">
                    <div className="carousel-item active">
                        <a href="/grand-slam/australian-open">
                            <img src="./images/grand-slams/australian-open.jpg" className="d-block w-100" alt="..."/>
                        </a>
                        <div className="carousel-caption d-none d-md-block">
                        <h5>Australian Open</h5>
                        <p>
                            { context.locales[context.actual].australian_open_description }
                        </p>
                        </div>
                    </div>
                    <div className="carousel-item">
                        <a href="/grand-slam/roland-garros">
                            <img src="./images/grand-slams/roland-garros.jpg" className="d-block w-100" alt="..."/>
                        </a>
                        <div className="carousel-caption d-none d-md-block">
                        <h5>Roland Garros</h5>
                        <p>
                            { context.locales[context.actual].roland_garros_description }
                        </p>
                        </div>
                    </div>
                    <div className="carousel-item">
                        <a href="/grand-slam/wimbledon">
                            <img src="./images/grand-slams/wimbledon.jpg" className="d-block w-100" alt="..."/>
                        </a>
                        <div className="carousel-caption d-none d-md-block">
                        <h5>Wimbledon</h5>
                        <p>
                            { context.locales[context.actual].wimbledon_description }
                        </p>
                        </div>
                    </div>
                    <div className="carousel-item">
                        <a href="/grand-slam/us-open">
                            <img src="./images/grand-slams/us-open.jpg" className="d-block w-100" alt="..."/>
                        </a>
                        <div className="carousel-caption d-none d-md-block">
                        <h5>US Open</h5>
                        <p>
                            { context.locales[context.actual].us_open_description }
                        </p>
                        </div>
                    </div>
                </div>
                <a className="carousel-control-prev" href="#carouselExampleCaptions" role="button" data-slide="prev">
                    <span className="carousel-control-prev-icon" aria-hidden="true"></span>
                    <span className="sr-only">Previous</span>
                </a>
                <a className="carousel-control-next" href="#carouselExampleCaptions" role="button" data-slide="next">
                    <span className="carousel-control-next-icon" aria-hidden="true"></span>
                    <span className="sr-only">Next</span>
                </a>
            </div>
        </Carousel>
    )
}

export default HomeIntroCarousel;