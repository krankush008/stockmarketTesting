import React from 'react';
import './App.css';
import UserComponent from './UserComponent'; // Import the UserComponent
import BondFilterUpdated from './BondFilterUpdated';
import Alerts from './Alerts';

function App() {
  return (
    <div className="App">
      <main>
        <Alerts />
      <BondFilterUpdated/>
      </main>
    </div>
  );
}

export default App;
