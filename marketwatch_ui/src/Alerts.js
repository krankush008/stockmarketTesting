import React, { useEffect, useState  , useReducer} from 'react'
import axios from 'axios'
import { initialState ,  reducer } from './BondFilterUpdated';
const Alerts = ({ allBonds, selectedBonds, dispatch }) => {
    const [yourBonds, setYourBonds] = useState([]);
    useEffect(() => {
        const fetchYourBonds = async () => {
            try {
                const response = await axios.get('http://localhost:3001/bonds');
                const responseBonds = response.data.map(bond => ({
                    isin: bond.bonds_id,
                    xirr: bond.xirr
                }));
                console.log('responseBonds:', responseBonds);
                const yourBonds = allBonds.filter(bond => {
                    return responseBonds.some(responseBond => responseBond.isin === bond.isin);
                }).map(filteredBond => ({
                    ...filteredBond,
                    xirr: responseBonds.find(responseBond => responseBond.isin === filteredBond.isin).xirr
                }));
                console.log('yourBonds:', yourBonds)
                const selectedBonds = yourBonds.map(bond => {
                    return {
                        "bond": {
                            "isin": bond.isin,
                            "creditScore": bond.creditScore,
                            "maturityDate": bond.maturityDate
                        },
                        "threshold": bond.xirr
                    }

                
                }
                );
            
                const selectedBondsMap = new Map();
                selectedBonds.forEach(bond => {
                    selectedBondsMap.set(bond.bond.isin, bond);
                });


                dispatch({ type: 'SET_SELECTED_BONDS', payload: selectedBondsMap});
                setYourBonds(selectedBonds);
                console.log('selectedBonds:', selectedBonds);
            } catch (error) {
                console.error('Error fetching all bonds data:', error);
            }
        }
        fetchYourBonds();
    }, [allBonds]);




    
    return (
        <ul className="filtered-bonds-list">
            {
                yourBonds.map(bond => {
                    return (
                        <li key={bond.bond.isin}>
                            {bond.bond.isin} - {bond.bond.creditScore} - {bond.bond.maturityDate} - {bond.threshold}
                        </li>
                    )
                })
            }
        </ul>
    )
}

export default Alerts