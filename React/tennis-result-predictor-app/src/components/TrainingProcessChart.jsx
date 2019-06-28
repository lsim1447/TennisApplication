import React , { useContext, useState, useEffect } from 'react';
import {Line} from 'react-chartjs-2';
  
function TrainingProcessChart(props) {
    const [ state, setState ] = useState({
        data: {
            labels: [],
            datasets: [
              {
                label: 'Punctuality of training',
                fill: false,
                lineTension: 0.1,
                backgroundColor: 'rgba(75,192,192,0.4)',
                borderColor: 'rgba(75,192,192,1)',
                borderCapStyle: 'butt',
                borderDash: [],
                borderDashOffset: 0.0,
                borderJoinStyle: 'miter',
                pointBorderColor: 'rgba(75,192,192,1)',
                pointBackgroundColor: '#fff',
                pointBorderWidth: 1,
                pointHoverRadius: 5,
                pointHoverBackgroundColor: 'rgba(75,192,192,1)',
                pointHoverBorderColor: 'rgba(220,220,220,1)',
                pointHoverBorderWidth: 2,
                pointRadius: 1,
                pointHitRadius: 10,
                data: []
              }
            ]
        }
    });
    
    useEffect(() => {
        const tmp_data = {
            labels: props.labels,
            datasets: [
              {
                label: 'Punctuality of training',
                fill: false,
                lineTension: 0.1,
                backgroundColor: 'rgba(75,192,192,0.4)',
                borderColor: 'rgba(75,192,192,1)',
                borderCapStyle: 'butt',
                borderDash: [],
                borderDashOffset: 0.0,
                borderJoinStyle: 'miter',
                pointBorderColor: 'rgba(75,192,192,1)',
                pointBackgroundColor: '#fff',
                pointBorderWidth: 1,
                pointHoverRadius: 5,
                pointHoverBackgroundColor: 'rgba(75,192,192,1)',
                pointHoverBorderColor: 'rgba(220,220,220,1)',
                pointHoverBorderWidth: 2,
                pointRadius: 1,
                pointHitRadius: 10,
                data: props.data
              }
            ]
        }
        setState({...state, data: tmp_data});
    }, [props.data]);

    return (
        <div>
            <Line data={state.data} />
        </div>
    );
}            

export default TrainingProcessChart;