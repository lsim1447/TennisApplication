import React , { Component } from 'react';
import styled from 'styled-components';

const Container = styled.div `

`;

const ProfileImage = styled.img `
    max-height: 325px;
`;

const ImageText = styled.a `
    font-weight: bold;
    font-size: 24px;
    cursor: pointer;
`;

class PlayerProfile extends Component {

    render(){
        return (
            <Container> 
                <div className="card mb-3">
                    <div className="row no-gutters">
                        <div className="col-md-4">
                            <ProfileImage src={this.props.imageUrl} className="img-fluid img-thumbnail" alt="" onError={(e)=>{e.target.onerror = null; e.target.src="./../../images/players/unknown.jpg"}} />
                        </div>
                        <div className="col-md-8">
                            <div className="card-body">
                                {
                                    this.props.isWinner ? 
                                    <ImageText className="card-text text-success" href={`/players/${this.props.player.playerSlug}`}> {this.props.player.firstName} {this.props.player.lastName} </ImageText>
                                    :
                                    <ImageText className="card-text text-danger" href={`/players/${this.props.player.playerSlug}`}> {this.props.player.firstName} {this.props.player.lastName} </ImageText>
                                }
                                <p className="card-text">This is a wider card with supporting text below as a natural lead-in to additional content. This content is a little bit longer.</p>
                                <p className="card-text"><small class="text-muted">Last updated 3 mins ago</small></p>
                            </div>
                        </div>
                    </div>
                </div>
            </Container>
        )
    }
}

export default PlayerProfile;