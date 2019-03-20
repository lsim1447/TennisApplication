import React , { Component } from 'react';
import styled from 'styled-components';

const Container = styled.div `
    padding-top: 20px;
`;

class StatProgressBar extends Component {

    render(){
        let player_one_percentage = ((this.props.data.firstPlayerValue / this.props.data.firstPlayerTotal) * 100);
        let player_two_percentage = ((this.props.data.secondPlayerValue / this.props.data.secondPlayerTotal) * 100);
        player_one_percentage = player_one_percentage ? player_one_percentage : 0;
        player_two_percentage = player_two_percentage ? player_two_percentage : 0;
        return (
            <Container> 
                <div className="row">
                    <div className="col font-weight-bold" style={{ paddingLeft: "30px" }}>
                        ({this.props.data.firstPlayerValue}/{this.props.data.firstPlayerTotal})
                    </div>

                    <div className="col col-lg-2 font-weight-bold text-center">
                        {this.props.data.title}
                    </div>

                    <div className="col font-weight-bold text-right" style={{ paddingRight: "30px" }}>
                        ({this.props.data.secondPlayerValue}/{this.props.data.secondPlayerTotal})
                    </div>
                </div>

                <div className="row">
                    <div className="col">
                        <div className="progress justify-content-end">
                            <div className={"progress-bar " + (player_one_percentage > player_two_percentage ? 'bg-danger' : 'bg-secondary')} role="progressbar" style={{width: `${player_one_percentage}%`}}> {Math.round(player_one_percentage)}%</div>
                        </div>
                    </div>

                    <div className="col">
                        <div className="progress">
                            <div className={"progress-bar " + (player_one_percentage < player_two_percentage ? 'bg-danger' : 'bg-secondary')} role="progressbar" style={{width: `${player_two_percentage}%`}}>{Math.round(player_two_percentage)}%</div>
                        </div>
                    </div>
                </div>
            </Container>
        )
    }
}

export default StatProgressBar;