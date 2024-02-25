import React, { useState, useEffect, useMemo} from 'react';
import { parse, differenceInMonths, getMonth, parseISO } from 'date-fns';
import axios from 'axios';

const BondFilter = () => {
  const [showFilters, setShowFilters] = useState(false);
  const [issuer, setIssuer] = useState('');
  const [issuers, setIssuers] = useState([]);
  const [creditScore, setCreditScore] = useState('');
  const [creditScores, setCreditScores] = useState([]);
  const [maturity, setMaturity] = useState('');
  const [xirrThreshold, setXirrThreshold] = useState('');
  const [xirrThresholds, setXirrThresholds] = useState({});
  const [allBonds, setAllBonds] = useState([]);
  const [filteredBonds, setFilteredBonds] = useState([]);
  const [selectedBonds, setSelectedBonds] = useState([]);
  const [xirrError, setXirrError] = useState('');
  const [filters, setFilters] = useState([]);
  const [filteredBondsMap, setFilteredBondsMap] = useState({});

  const handleBondSelect = (filterIndex, bond, isChecked) => {
   
    const filter1 = JSON.parse(localStorage.getItem('filters'));
    const updatedFilters = [...filter1];
    const selectedBonds = updatedFilters[filterIndex].selectedBonds;

    if (!isChecked) {
        // Deselect bond
        updatedFilters[filterIndex].selectedBonds = selectedBonds.filter((selected) => selected !== bond);
    } else {
        // Select bond
        updatedFilters[filterIndex].selectedBonds = [...selectedBonds, bond];
    }

    // Save updatedFilters to localStorage
    localStorage.setItem('filters',JSON.stringify(updatedFilters));

    console.log("updatedFilters "+JSON.stringify(updatedFilters));
    console.log("hhah "+JSON.stringify(filters));
  };
  
  // useEffect(() => {
  //   console.log('Filters state changed:', JSON.stringify(filters));
  // }, [filters]);
  useEffect(() => {
    console.log("akr rka");
    const rawFilters = localStorage.getItem('filters');
    console.log('Raw Filters:'+ typeof rawFilters);
    const savedFilters = JSON.parse(rawFilters);
    if (savedFilters) {
        setFilters((savedFilters));
        console.log('Filtersak:', savedFilters);
    }
}, []);

  const bondsData = selectedBonds.map(bond => ({
    user_id: 5,
    bonds_id: bond.bonds_id,
    xirr: bond.xirr,
  }));

  const handleSubmit = async () => {
    const hasEmptyXIRR = selectedBonds.some((bond) => bond.xirr === '');

  if (hasEmptyXIRR) {
    // Display an error message or handle the case where XIRR is empty
    setXirrError('XIRR value is required for selected bonds.');
    return;
  }

    console.log("Selected bonds = "+JSON.stringify(selectedBonds[0]));
    try {
      const response = await axios.put('http://localhost:8080/api/createAlerts', bondsData, {
        headers: {
          'Content-Type': 'application/json',
        },
      });

      if (response.ok) {
        console.log('Alerts created successfully!');
        // Optionally, clear the selected bonds after successful submission
        setSelectedBonds([]);
      } else {
        console.error('Failed to create alerts.');
      }
    } catch (error) {
      console.error('Error creating alerts:', error);
    }
  };

  /*
  const filterBonds = async () => {
    try {
      // Simulate fetching data from the backend based on the selected filters
      const response = await fetch(`http://localhost:8080/api/bonds?issuer=${issuer}&creditScore=${creditScore}&maturity=${maturity}`);
      const data = await response.json();
      setFilteredBonds(data);
    } catch (error) {
      console.error('Error fetching data:', error);
    }
  };
  */
  useEffect(() => {
    // Simulate fetching all bond data on component mount
    const fetchAllBonds = async () => {

        try {
          const response = await axios.get('http://localhost:8080/api/bonds');
         // const data = await response.json();
          console.log("ankush hsukna",response.data);
          setAllBonds(response.data);
          const uniqueIssuers = [...new Set(response.data.map(bond => bond.isin))];
          const uniqueCreditScores = [...new Set(response.data.map(bond => bond.creditScore))];
          
          setCreditScores(uniqueCreditScores);
          console.log("Choton uniqueIssuers",uniqueIssuers);
        } catch (error) {
          console.error('Error fetching all bonds data:', error);
        };
    };
//itguytgy
    fetchAllBonds();
    const maturityDateString = '20-Feb-2026';

    // Manually parse the maturity date string
    const parsedMaturityDate = parse(maturityDateString, 'dd-MMM-yyyy', new Date());

    // Calculate the month difference
    const monthsDifference = differenceInMonths(parsedMaturityDate, new Date());

    console.log("Monthly diff"+monthsDifference); 
  }, []);

  const calculateMonthsDifference = (maturityDate) => {
    console.log("matchurity date = "+maturityDate);
    const today = new Date();
    //const isoDateString = maturityDate.replace(/(\d+)-(\w+)-(\d+)/, '$3-$2-$1T00:00:00.000Z');
    const parsedMaturityDate = parse(maturityDate, 'dd-MMM-yyyy', new Date());
    console.log("parsedMaturityDate = "+parsedMaturityDate);
    const monthsDifference = differenceInMonths(parsedMaturityDate, today);
    console.log("mmdiff anku"+monthsDifference);
    return monthsDifference;
  };
  
  // Memoized filter function to prevent unnecessary filtering on every render
  const filterBonds = () => {
    console.log("choton kumar");
    
    const filtered = allBonds.filter((bond) => {
      console.log("bond.creditScore = " + JSON.stringify(bond) + " creditScore= " + maturity + "months diff= "+calculateMonthsDifference(bond.maturityDate));
      if (!creditScore || bond.creditScore === creditScore) {
        if (!maturity || calculateMonthsDifference(bond.maturityDate) == maturity) {
          return true;
        }
      }
      return false;
    });
  
    setFilteredBonds(filtered);
  };

  const filterBonds1 = (index) => {
    filters[index].bonds=[];
    filters[index].selectedBonds=[];
    const filter = filters[index];
    const filtered = allBonds.filter((bond) => {
      const maturityMatches = calculateMonthsDifference(bond.maturityDate) == filter.maturity;
      const creditScoreMatches = bond.creditScore === filter.creditScore;
    
      return maturityMatches && creditScoreMatches;
    });
    console.log("filtered ank = "+JSON.stringify(filtered));
    filters[index].bonds = filtered;
  };
  const handleAddFilter = () => {
    const newFilter = {
      creditScore: '',
      maturity: '',
      xirrThreshold: '',
      bonds: [],
      selectedBonds: []
    };
    const updatedFilters = [...filters, newFilter];

    localStorage.setItem('filters', JSON.stringify(updatedFilters));
  };

  const handleRemoveFilter = (index) => {
    const updatedFilters = filters.filter((_, i) => i !== index);
    localStorage.setItem('filters', updatedFilters);
  };

  const handleFilterChange = (index, field, value) => {
    const updatedFilters = [...filters];
    updatedFilters[index][field] = value;
    localStorage.setItem('filters', JSON.stringify(updatedFilters));
  };
  const handleThresholdChange = (isin, value) => {
    setXirrThresholds((prevThresholds) => ({ ...prevThresholds, [isin]: value }));
  };
  

  return (
    <div>
      <h2>Bond Filters</h2>
      <div>
      <button onClick={handleAddFilter}>Add Filter</button>
      {console.log("Mankfilters = "+filters)}
      {filters.map((filter, index) => (
        <div key={index}>
          <label>Credit Score:</label>
          <select
            id={`creditScore-${index}`}
            onChange={(e) => handleFilterChange(index, 'creditScore', e.target.value)}
            value={filter.creditScore}
          >
            <option value="">Select Credit Score</option>
            {allBonds.map((option) => (
              <option key={option.creditScore} value={option.creditScore}>
                {option.creditScore}
              </option>
            ))}
          </select>


          <button onClick={() => handleRemoveFilter(index)}>Remove</button> 

          <label>Maturity:</label>
          <select
            id={`maturity-${index}`}
            onChange={(e) => handleFilterChange(index, 'maturity', e.target.value)}
            value={filter.maturity}
          >
            <option value="">Select Maturity</option>
            <option value="1">1</option>
            <option value="2">2</option>
            <option value="3">3</option>
            <option value="4">4</option>
            <option value="5">5</option>
            <option value="6">6</option>
            <option value="7">7</option>
            <option value="8">8</option>
            <option value="9">9</option>
            <option value="10">10</option>
            <option value="11">11</option>
            <option value="12">12</option>
            <option value="23">23</option>
          </select>   

          <button type="button" onClick={filterBonds1(index)}>Apply Filters</button>
          <h3>Filtered Bonds:</h3>
<div>
{console.log("myfilters "+JSON.stringify(filters))}
  <ul>
    {filters[index].bonds.map((bond) => (
      <li key={bond.isin}>
        <label>
          <input
            type="checkbox"
            onChange={(e) => handleBondSelect(index, bond, e.target.checked)}
          />
          ID: {bond.isin}, Credit Score: {bond.creditScore}, Maturity: {bond.maturity}, XIRR: {bond.xirr}, selectedBonds: {filters[index].selectedBonds}
          {filters[index].selectedBonds.includes(bond) && (
            <>
              <input
                type="number"
                id={`xirrThreshold-${bond.isin}`}
                onChange={(e) => handleThresholdChange(bond.isin, e.target.value)}
                value={xirrThresholds[bond.isin] || ''}
                required  // Add the 'required' attribute conditionally
              />
              Threshold
            </>
          )}
        </label>
      </li>
    ))}
  </ul>
</div>


          </div>
      ))}    


    </div>

    </div>
  );
};

export default BondFilter;
