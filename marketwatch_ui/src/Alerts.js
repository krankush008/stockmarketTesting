import React, { useEffect, useState} from 'react'
import axios from 'axios'

const Alerts = ({ allBonds, selectedBonds, dispatch }) => {
  const [yourBonds, setYourBonds] = useState([]);

  const handleCheckboxChange = (bond) => {
    if (selectedBonds.has(bond.isin)) {
      const updatedSelectedBonds = new Map(selectedBonds);
      updatedSelectedBonds.delete(bond.isin);
      dispatch({ type: 'SET_SELECTED_BONDS', payload: updatedSelectedBonds });
    } else {
      dispatch({
        type: 'SET_SELECTED_BONDS',
        payload: new Map(selectedBonds).set(bond.isin, { bond, threshold: '' })
      });
    }
  };

  const handleThresholdChange = (bond, threshold) => {
    if (threshold === '' && selectedBonds.has(bond.isin)) {
      dispatch({
        type: 'SET_SELECTED_BONDS',
        payload: new Map(selectedBonds).set(bond.isin, { bond, threshold: '' })
      });
    } else {
      dispatch({
        type: 'SET_SELECTED_BONDS',
        payload: new Map(selectedBonds).set(bond.isin, { bond, threshold: parseInt(threshold) })
      });
    }
  };

  useEffect(() => {
    const fetchYourBonds = async () => {
      try {
        const userSelectedBondsResponse = await axios.get(`http://localhost:8080/api/getAlertsByUserId/${124}`);
        const userSelectedBonds = userSelectedBondsResponse.data.map(bond => ({
            isin: bond.bondId,
            xirr: bond.xirr
        }));
        const yourBonds = allBonds.filter(bond => {
            return userSelectedBonds.some(selectedBond => selectedBond.isin === bond.isin);
        }).map(filteredBond => ({
            ...filteredBond,
            xirr: userSelectedBonds.find(selectedBond => selectedBond.isin === filteredBond.isin).xirr
        }));
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
      } catch (error) {
        console.error('Error fetching all bonds data:', error);
      }
    }
    fetchYourBonds();
  }, [allBonds]);

  return (
    <ul className="filtered-bonds-list">
      {yourBonds.map((bond) => (
        <li key={bond.bond.isin}>
          <input
            type="checkbox"
            className="bond-checkbox"
            checked={selectedBonds.has(bond.bond.isin)}
            onChange={() => handleCheckboxChange(bond.bond)}
          />
          ID: {bond.bond.isin}, Credit Score: {bond.bond.creditScore}, Maturity: {bond.bond.maturityDate}
          {selectedBonds.has(bond.bond.isin) && selectedBonds.get(bond.bond.isin).threshold === '' && (
            <span style={{ color: 'red', marginLeft: '10px' }}>Please enter threshold</span>
          )}
          Threshold:
          <input
            type="number"
            className="threshold-input"
            min="0"
            value={selectedBonds.has(bond.bond.isin) ? selectedBonds.get(bond.bond.isin).threshold : ''}
            onChange={(e) => handleThresholdChange(bond.bond, e.target.value)}
          />
        </li>
      ))}
    </ul>
  );
}
export default Alerts