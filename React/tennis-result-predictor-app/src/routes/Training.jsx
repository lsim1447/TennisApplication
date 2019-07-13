import React , { useContext, useState, useEffect } from 'react';
import { AppContext } from '../AppContextProvider';
import { get_request, post_request } from './../util/Request';
import { DEFALULT_SERVER_URL } from '../constants';
import styled from 'styled-components';
import TrainingProcessChart from './../components/TrainingProcessChart';
import swal from 'sweetalert2';

const FormContainer = styled.div `
    width: 100%;
    margin-top: 15px;
    padding: 25px 50px 25px 50px;
    border: 2px solid grey;

    @media (min-width: 992px) {
        margin-left: auto;
        margin-right: auto;
    }

`;
const LateralDiv = styled.div `
    background-image: url('https://is1-ssl.mzstatic.com/image/thumb/Purple123/v4/e8/0a/17/e80a170b-070d-d167-a51d-b74614b597af/AppIcon-0-1x_U007emarketing-0-0-85-220-9.png/246x0w.jpg');
`;
const TableContainer = styled.div `
    margin-top: 50px;
    @media (min-width: 992px) {
        margin-left: auto;
        margin-right: auto;
    }
`;
const Title = styled.h1 `
    margin-top: 24px
    margin-bottom: 24px;
`;

function Training(props) {
    const context =  useContext(AppContext);
    
    const [ version, setVersion ] = useState('');
    const [ description, setDescription ] = useState('');

    const [ state, setState ] = useState({
        trainingDescriptions: [],
        selectedRow: {
            trainingDescription: {},
            list: []
        },
        valuesToChart: []
    });

    function getTrainings(){
        get_request(`${DEFALULT_SERVER_URL}/training/description/all`)
            .then( response => {
                setState({...state, trainingDescriptions: response});
            })
    }

    useEffect(() => {
        getTrainings();
    }, []);

    function rowOnClick(row){
        get_request(`${DEFALULT_SERVER_URL}/training/results/data/training_id?training_id=${encodeURIComponent(row.training_id)}`)
            .then( response => {
                const values = response.list.map(element => element.percentage);
                setState({...state, selectedRow: response, valuesToChart: values});
            })
    }

    function renderTableRows(data){
        return data.map((row, index) => {
            return (
                <tr key={index} data-toggle="modal" data-target=".bd-example-modal-lg" onClick={() => rowOnClick(row)}>
                    <th scope="row">{index+1}</th>
                    <td>{row.training_id}</td>
                    <td>{row.date}</td>
                    <td>{row.highestRate}%</td>
                    <td>{row.description}</td>
                </tr>
            )
        })
    }

    function train(e){
        get_request(`${DEFALULT_SERVER_URL}/prediction/training?version=${encodeURIComponent(version)}&description=${encodeURIComponent(description)}`)
            .then( response => {
                getTrainings();
                swal.fire({
                    position: 'center',
                    type: 'success',
                    title: 'The training has been finished!',
                    showConfirmButton: false,
                });
            })
    }

    return (
        <div className="row">
            <LateralDiv className="col-sm-2"></LateralDiv>
            <div className="col-sm-8">
                <Title className="display-4 text-center"><strong>Setup training</strong></Title>
                <FormContainer>
                    <form>
                        <div className="form-group row">
                            <div className="col-sm-3">
                                <label htmlFor="inputVersion" className="col-form-label"><h3>Training Version:</h3></label>
                            </div>
                            <div className="col-sm-9">
                                <input 
                                    type="text" 
                                    className="form-control" 
                                    id="inputVersion" 
                                    placeholder="This version number will be the part of the training files"
                                    value={version} 
                                    onChange={(e) => setVersion(e.target.value)}/>
                            </div>
                        </div>
                        <div className="form-group row">
                            <div className="col-sm-3">
                                <label htmlFor="inputDescription" className="col-form-label"><h3>Description:</h3></label>
                            </div>
                            <div className="col-sm-9">
                                <input 
                                    type="text" 
                                    className="form-control" 
                                    id="inputDescription" 
                                    placeholder="Write something about this training which will help you identify this training in the feature"
                                    value={description} 
                                    onChange={(e) => setDescription(e.target.value)}/>
                            </div>
                        </div>
                        <fieldset className="form-group">
                            <div className="row">
                                <div className="col-sm-3">
                                    <legend className="col-form-label pt-0"><h3>Criterias:</h3></legend>
                                </div>
                                <div className="col-sm-4">
                                    <div className="form-check">
                                        <div className="custom-control custom-checkbox">
                                            <input type="checkbox" className="custom-control-input" id="customCheck1" />
                                            <label className="custom-control-label" htmlFor="customCheck1">All matches</label>
                                        </div>
                                    </div>
                                    <div className="form-check">
                                        <div className="custom-control custom-checkbox">
                                            <input type="checkbox" className="custom-control-input" id="customCheck2" />
                                            <label className="custom-control-label" htmlFor="customCheck2">Surface matches</label>
                                        </div>
                                    </div>
                                    <div className="form-check disabled">
                                        <div className="custom-control custom-checkbox">
                                            <input type="checkbox" className="custom-control-input" id="customCheck3" />
                                            <label className="custom-control-label" htmlFor="customCheck3">Tournament matches</label>
                                        </div>
                                    </div>
                                </div>
                                <div className="col-sm-5">
                                    <div className="form-check">
                                        <div className="custom-control custom-checkbox">
                                            <input type="checkbox" className="custom-control-input" id="customCheck4" />
                                            <label className="custom-control-label" htmlFor="customCheck4">1v1 All matches</label>
                                        </div>
                                    </div>
                                    <div className="form-check">
                                        <div className="custom-control custom-checkbox">
                                            <input type="checkbox" className="custom-control-input" id="customCheck5" />
                                            <label className="custom-control-label" htmlFor="customCheck5">1v1 Surface matches</label>
                                        </div>
                                    </div>
                                    <div className="form-check disabled">
                                        <div className="custom-control custom-checkbox">
                                            <input type="checkbox" className="custom-control-input" id="customCheck6" />
                                            <label className="custom-control-label" htmlFor="customCheck6">1v1 Tournament matches</label>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </fieldset>

                        <fieldset className="form-group">
                            <div className="row">
                                <div className="col-sm-3">
                                    <legend className="col-form-label pt-0"><h3>Statistics:</h3></legend>
                                </div>
                                <div className="col-sm-4">
                                    <div className="form-check">
                                        <div className="custom-control custom-checkbox">
                                            <input type="checkbox" className="custom-control-input" id="customCheck7" />
                                            <label className="custom-control-label" htmlFor="customCheck7">Service Point Won Rate</label>
                                        </div>
                                    </div>
                                    <div className="form-check">
                                        <div className="custom-control custom-checkbox">
                                            <input type="checkbox" className="custom-control-input" id="customCheck8" />
                                            <label className="custom-control-label" htmlFor="customCheck8">Return Point Won Rate</label>
                                        </div>
                                    </div>
                                    <div className="form-check disabled">
                                        <div className="custom-control custom-checkbox">
                                            <input type="checkbox" className="custom-control-input" id="customCheck9" />
                                            <label className="custom-control-label" htmlFor="customCheck9">First Serve In Rate</label>
                                        </div>
                                    </div>
                                    <div className="form-check disabled">
                                        <div className="custom-control custom-checkbox">
                                            <input type="checkbox" className="custom-control-input" id="customCheck10" />
                                            <label className="custom-control-label" htmlFor="customCheck10">First Serve Won Rate</label>
                                        </div>
                                    </div>
                                    <div className="form-check disabled">
                                        <div className="custom-control custom-checkbox">
                                            <input type="checkbox" className="custom-control-input" id="customCheck11" />
                                            <label className="custom-control-label" htmlFor="customCheck11">Break Point Converted Rate</label>
                                        </div>
                                    </div>
                                    <div className="form-check disabled">
                                        <div className="custom-control custom-checkbox">
                                            <input type="checkbox" className="custom-control-input" id="customCheck12" />
                                            <label className="custom-control-label" htmlFor="customCheck12">Break Point Saved Rate</label>
                                        </div>
                                    </div>
                                </div>
                                <div className="col-sm-5">
                                    <div className="form-check">
                                        <div className="custom-control custom-checkbox">
                                            <input type="checkbox" className="custom-control-input" id="customCheck13" />
                                            <label className="custom-control-label" htmlFor="customCheck13">1v1 Service Point Won Rate</label>
                                        </div>
                                    </div>
                                    <div className="form-check">
                                        <div className="custom-control custom-checkbox">
                                            <input type="checkbox" className="custom-control-input" id="customCheck14" />
                                            <label className="custom-control-label" htmlFor="customCheck14">1v1 Return Point Won Rate</label>
                                        </div>
                                    </div>
                                    <div className="form-check disabled">
                                        <div className="custom-control custom-checkbox">
                                            <input type="checkbox" className="custom-control-input" id="customCheck15" />
                                            <label className="custom-control-label" htmlFor="customCheck15">1v1 First Serve In Rate</label>
                                        </div>
                                    </div>
                                    <div className="form-check disabled">
                                        <div className="custom-control custom-checkbox">
                                            <input type="checkbox" className="custom-control-input" id="customCheck16" />
                                            <label className="custom-control-label" htmlFor="customCheck16">1v1 First Serve Won Rate</label>
                                        </div>
                                    </div>
                                    <div className="form-check disabled">
                                        <div className="custom-control custom-checkbox">
                                            <input type="checkbox" className="custom-control-input" id="customCheck17" />
                                            <label className="custom-control-label" htmlFor="customCheck17">1v1 Break Point Converted Rate</label>
                                        </div>
                                    </div>
                                    <div className="form-check disabled">
                                        <div className="custom-control custom-checkbox">
                                            <input type="checkbox" className="custom-control-input" id="customCheck18" />
                                            <label className="custom-control-label" htmlFor="customCheck18">1v1 Break Point Saved Rate</label>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </fieldset>
                        
                        <div className="form-group row">
                            <div className="col-sm-12">
                                <button type="button" className="btn btn-primary" onClick={(e) => train(e)} style={{width: "100%", marginTop: "25px"}}>Start training</button>
                            </div>
                        </div>
                    </form>
                </FormContainer>
                <TableContainer>
                    <table className="table table-hover table-dark">
                        <thead>
                            <tr>
                                <th scope="col">#</th>
                                <th scope="col">Version</th>
                                <th scope="col">Date</th>
                                <th scope="col">Highest percentage</th>
                                <th scope="col">Description</th>
                            </tr>
                        </thead>
                        <tbody>
                            { renderTableRows(state.trainingDescriptions)}
                        </tbody>
                    </table>
                </TableContainer>
            </div>
            <LateralDiv className="col-sm-2"></LateralDiv>

            <div className="modal fade bd-example-modal-lg" tabIndex="-1" role="dialog" aria-labelledby="myLargeModalLabel" aria-hidden="true">
                <div className="modal-dialog modal-lg">
                    <div className="modal-content">
                        <div className="modal-header">
                            <h2 className="modal-title" id="exampleModalCenterTitle">Training details</h2>
                            <button type="button" className="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div className="modal-body">
                            <div className="text-center"> <h4>{state.selectedRow.trainingDescription.description}</h4></div>
                            <div className="text-center">
                                (<label><strong> Date: </strong> {state.selectedRow.trainingDescription.date} </label> 
                                <label style={{paddingLeft: "12px"}}><strong> Final percentage: </strong> {state.selectedRow.trainingDescription.highestRate}% </label>)
                            </div>
                            <TrainingProcessChart data={state.valuesToChart} labels={Array.from(Array(state.valuesToChart.length).keys())}/>
                        </div>
                        <div className="modal-footer">
                            <button type="button" className="btn btn-secondary" data-dismiss="modal">Close</button>
                            <button type="button" className="btn btn-primary">Save changes</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
    
}

export default Training;