import React , { useContext, useState, useEffect } from 'react';
import { AppContext } from '../AppContextProvider';
import { get_request, post_request } from './../util/Request';
import { DEFALULT_SERVER_URL } from '../constants';
import styled from 'styled-components';
import TrainingProcessChart from './../components/TrainingProcessChart';

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
    
    const [ state, setState ] = useState({
        filename: '',
        description: '',
        tableData: [
            {
                date: "6/22/2019",
                percentage: "68",
                filename: "1991-2016-stats-all-surface-tournament-matches"
            },
            {
                date: "6/21/2019",
                percentage: "66",
                filename: "2013-2016-stats-all-surface-tournament-matches"
            },
            {
                date: "6/19/2019",
                percentage: "63",
                filename: "1991-2016-stats-all-surface-tournament-matches"
            },
            {
                date: "6/22/2019",
                percentage: "68",
                filename: "1991-2016-stats-all-surface-tournament-matches"
            },
            {
                date: "6/21/2019",
                percentage: "66",
                filename: "2013-2016-stats-all-surface-tournament-matches"
            },
            {
                date: "6/19/2019",
                percentage: "63",
                filename: "1991-2016-stats-all-surface-tournament-matches"
            },
        ],
        selectedRow: {}
    });

    useEffect(() => {
        console.log('training...');
    });

    function rowOnClick(row){
        setState({...state, selectedRow: row})
    }

    function renderTableRows(data){
        return data.map((row, index) => {
            return (
                <tr key={index} data-toggle="modal" data-target=".bd-example-modal-lg" onClick={() => rowOnClick(row)}>
                    <th scope="row">{index+1}</th>
                    <td>{row.date}</td>
                    <td>{row.percentage}%</td>
                    <td>{row.filename}</td>
                </tr>
            )
        })
    }

    function train(e){
        get_request(`${DEFALULT_SERVER_URL}/prediction/training`)
            .then( response => {
                console.log('response waze = ', response)
            })
    }

    return (
        <div className="row">
            <LateralDiv className="col-sm-2"></LateralDiv>
            <div className="col-sm-8">
                <Title className="display-4 text-center">Setup training </Title>
                <FormContainer>
                    <form>
                        <div className="form-group row">
                            <div className="col-sm-3">
                                <label htmlFor="inputEmail3" className="col-form-label"><h3>File name:</h3></label>
                            </div>
                            <div className="col-sm-9">
                                <input 
                                    type="text" 
                                    className="form-control" 
                                    id="inputEmail3" 
                                    placeholder="ex. 1991-2019-all-surface-tournament-stats" 
                                    value={state.filename} 
                                    onChange={(e) => setState({...state, filename: e.value})}/>
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
                                    placeholder="write something about the training"
                                    value={state.filename} 
                                    onChange={(e) => setState({...state, filename: e.value})}/>
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
                                <th scope="col">Date</th>
                                <th scope="col">Highest percentage</th>
                                <th scope="col">Filename</th>
                            </tr>
                        </thead>
                        <tbody>
                            { renderTableRows(state.tableData)}
                        </tbody>
                    </table>
                </TableContainer>
            </div>
            <LateralDiv className="col-sm-2"></LateralDiv>

            <div className="modal fade bd-example-modal-lg" tabIndex="-1" role="dialog" aria-labelledby="myLargeModalLabel" aria-hidden="true">
                <div className="modal-dialog modal-lg">
                    <div className="modal-content">
                        <div className="modal-header">
                            <h5 className="modal-title" id="exampleModalCenterTitle">Training details</h5>
                            <button type="button" className="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div className="modal-body">
                            <div> Date =  {state.selectedRow.date} </div>
                            <div> Percentage =  {state.selectedRow.percentage}% </div>
                            <div> Filename =  {state.selectedRow.filename} </div>
                            <TrainingProcessChart />
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