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
                <div className="card">
                    <ProfileImage src={this.props.imageUrl} className="img-fluid img-thumbnail" alt="" onError={(e)=>{e.target.onerror = null; e.target.src="./../../images/players/unknown.jpg"}} />
                    <div className="card-body text-center">
                        {this.props.isWinner ? 
                            <ImageText className="card-text text-success" href={`/players/${this.props.player.playerSlug}`}> {this.props.player.firstName} {this.props.player.lastName} </ImageText>
                            :
                            <ImageText className="card-text text-danger" href={`/players/${this.props.player.playerSlug}`}> {this.props.player.firstName} {this.props.player.lastName} </ImageText>
                        }
                        
                    </div>  
                </div>
            </Container>
        )
    }
}

export default PlayerProfile;