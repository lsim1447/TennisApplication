import React , { Component } from 'react';

class CarouselIndicator extends Component {
    
    constructor(props){
        super(props);
    }

    render(){
        return (
            <div id="carouselExampleIndicators" className="carousel slide" data-ride="carousel">
                <ol className="carousel-indicators">
                    <li data-target="#carouselExampleIndicators" data-slide-to="0" class="active"></li>
                    <li data-target="#carouselExampleIndicators" data-slide-to="1"></li>
                    <li data-target="#carouselExampleIndicators" data-slide-to="2"></li>
                    <li data-target="#carouselExampleIndicators" data-slide-to="3"></li>
                </ol>
                <div className="carousel-inner">
                    <div className="carousel-item active">
                        <img className="d-block w-100" src={`./../images/grand-slams/${this.props.grandslam}/${this.props.grandslam}-1.jpg`} alt="First slide"/>
                    </div>
                    <div className="carousel-item">
                        <img className="d-block w-100" src={`./../images/grand-slams/${this.props.grandslam}/${this.props.grandslam}-2.jpg`} alt="Second slide"/>
                    </div>
                    <div className="carousel-item">
                        <img className="d-block w-100" src={`./../images/grand-slams/${this.props.grandslam}/${this.props.grandslam}-3.jpg`} alt="Third slide"/>
                    </div>
                    <div className="carousel-item">
                        <img className="d-block w-100" src={`./../images/grand-slams/${this.props.grandslam}/${this.props.grandslam}-4.jpg`} alt="Forth slide"/>
                    </div>
                </div>
                <a className="carousel-control-prev" href="#carouselExampleIndicators" role="button" data-slide="prev">
                    <span className="carousel-control-prev-icon" aria-hidden="true"></span>
                    <span className="sr-only">Previous</span>
                </a>
                <a className="carousel-control-next" href="#carouselExampleIndicators" role="button" data-slide="next">
                    <span className="carousel-control-next-icon" aria-hidden="true"></span>
                    <span className="sr-only">Next</span>
                </a>
            </div>
        )
    }
}

export default CarouselIndicator;