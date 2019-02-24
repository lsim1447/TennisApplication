import React , { Component } from 'react';

class Tournament extends Component {
    
    render(){
        return (
            <div> 
                <select class="js-example-basic-single" name="state">
                    <option value="AL">Alabama</option>
                    <option value="BA">Barcelona</option>
                    <option value="WY">Wyoming</option>
                </select>
            </div>
        )
    }
}

export default Tournament;