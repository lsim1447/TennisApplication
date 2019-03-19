import React  from 'react';
import { PlayerName, FlagIcon } from './../OftenUsedElements';

export default function PlayerInfoTable(props){

    const {
        selectedPlayer,
        tournaments,
        grand_slams,
        locales
    } = props;

    return (
        <table className="table text-left">
            <thead>
                <PlayerName className="font-weight-bold">
                    {selectedPlayer.firstName} {selectedPlayer.lastName}
                </PlayerName>
            </thead>
            <tbody>
                <tr>
                    <td>
                        <span className="font-weight-bold"> { locales.country } </span>
                    </td>
                    <td>
                        {selectedPlayer.flagCode}  <FlagIcon src={`./../images/flags/${selectedPlayer.flagCode}.png`} alt="" />
                    </td>
                </tr>
                <tr>
                    <td>
                        <span className="font-weight-bold"> { locales.birthdate } </span>
                    </td>
                    <td>
                        {selectedPlayer.birthdate}
                    </td>
                </tr>
                <tr>
                    <td>
                        <span className="font-weight-bold"> { locales.handedness } </span>
                    </td>
                    <td>
                        {selectedPlayer.handedness}
                    </td>
                </tr>
                <tr>
                    <td>
                        <span className="font-weight-bold"> { locales.won_tournaments } </span>
                    </td>
                    <td>
                        { tournaments.length }
                    </td>
                </tr>
                <tr>
                    <td>
                        <span className="font-weight-bold"> { locales.won_grand_slams } </span>
                    </td>
                    <td>
                        { grand_slams.length }
                    </td>
                </tr>
                <tr>
                    <td>
                        <span className="font-weight-bold"> { locales.height } </span>
                    </td>
                    <td>
                        {selectedPlayer.heightCm} cm
                    </td>
                </tr>
                <tr>
                    <td>
                        <span className="font-weight-bold">{ locales.turned_pro } </span>
                    </td>
                    <td>
                        {selectedPlayer.turnedPro}
                    </td>
                </tr>
                <tr>
                    <td>
                        <span className="font-weight-bold">{ locales.weight_kg } </span>
                    </td>
                    <td>
                        {selectedPlayer.weightKg} kg
                    </td>
                </tr>
                <tr>
                    <td>
                        <span className="font-weight-bold">{ locales.weight_lbs } </span>
                    </td>
                    <td>
                        {selectedPlayer.weightLbs}
                    </td>
                </tr>
            </tbody>
        </table>                  
    )
}